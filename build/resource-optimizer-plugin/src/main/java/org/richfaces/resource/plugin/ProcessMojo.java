/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.resource.plugin;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.faces.application.Resource;
import javax.faces.application.ResourceDependency;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.richfaces.application.Module;
import org.richfaces.application.ServicesFactoryImpl;
import org.richfaces.log.Logger;
import org.richfaces.resource.ResourceFactory;
import org.richfaces.resource.ResourceFactoryImpl;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.optimizer.Faces;
import org.richfaces.resource.optimizer.FileNameMapping;
import org.richfaces.resource.optimizer.ProcessMode;
import org.richfaces.resource.optimizer.ResourceLibraryExpander;
import org.richfaces.resource.optimizer.concurrent.CountingExecutorCompletionService;
import org.richfaces.resource.optimizer.faces.FacesImpl;
import org.richfaces.resource.optimizer.faces.ServiceFactoryModule;
import org.richfaces.resource.optimizer.naming.FileNameMapperImpl;
import org.richfaces.resource.optimizer.resource.handler.impl.DynamicResourceHandler;
import org.richfaces.resource.optimizer.resource.handler.impl.StaticResourceHandler;
import org.richfaces.resource.optimizer.resource.scan.ResourcesScanner;
import org.richfaces.resource.optimizer.resource.scan.impl.DynamicResourcesScanner;
import org.richfaces.resource.optimizer.resource.scan.impl.ResourceOrderingScanner;
import org.richfaces.resource.optimizer.resource.scan.impl.StaticResourcesScanner;
import org.richfaces.resource.optimizer.resource.util.ResourceConstants;
import org.richfaces.resource.optimizer.resource.util.ResourceUtil;
import org.richfaces.resource.optimizer.resource.writer.ResourceProcessor;
import org.richfaces.resource.optimizer.resource.writer.impl.CSSCompressingProcessor;
import org.richfaces.resource.optimizer.resource.writer.impl.JavaScriptCompressingProcessor;
import org.richfaces.resource.optimizer.resource.writer.impl.JavaScriptPackagingProcessor;
import org.richfaces.resource.optimizer.resource.writer.impl.ResourceWriterImpl;
import org.richfaces.resource.optimizer.task.ResourceTaskFactoryImpl;
import org.richfaces.resource.optimizer.util.MorePredicates;
import org.richfaces.resource.optimizer.vfs.VFS;
import org.richfaces.resource.optimizer.vfs.VFSRoot;
import org.richfaces.resource.optimizer.vfs.VirtualFile;
import org.richfaces.application.ServiceTracker;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

/**
 * Scans for resource depe`ndencies (ResourceDependency annotations) on the class-path and collect them in order to pre-generate
 * resources them and optionally pack or compress them.
 */
@Mojo(name="process", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class ProcessMojo extends AbstractMojo {
    private static final URL[] EMPTY_URL_ARRAY = new URL[0];
    private static final Function<String, Predicate<CharSequence>> REGEX_CONTAINS_BUILDER_FUNCTION = new Function<String, Predicate<CharSequence>>() {
        public Predicate<CharSequence> apply(String from) {
            Predicate<CharSequence> containsPredicate = Predicates.containsPattern(from);
            return Predicates.and(Predicates.notNull(), containsPredicate);
        }
    };
    private static final Function<Resource, String> CONTENT_TYPE_FUNCTION = new Function<Resource, String>() {
        public String apply(Resource from) {
            return from.getContentType();
        }
    };
    private static final Function<Resource, String> RESOURCE_QUALIFIER_FUNCTION = new Function<Resource, String>() {
        public String apply(Resource from) {
            return ResourceUtil.getResourceQualifier(from);
        }
    };
    private final Function<String, URL> filePathToURL = new Function<String, URL>() {
        public URL apply(String from) {
            try {
                File file = new File(from);
                if (file.exists()) {
                    return file.toURI().toURL();
                }
            } catch (MalformedURLException e) {
                getLog().error("Bad URL in classpath", e);
            }

            return null;
        }
    };
    /**
     * Output directory for processed resources
     */
    @Parameter(property="resourcesOutputDir", required=true)
    private String resourcesOutputDir;

    /**
     * Configures what prefix should be placed to each file before the library and name of the resource
     */
    @Parameter(property="staticResourcePrefix", defaultValue="" )
    private String staticResourcePrefix;

    /**
     * Output file for resource mapping configuration
     */
    @Parameter(property="staticResourceMappingFile", required=true)
    private String staticResourceMappingFile;

    /**
     * The list of RichFaces skins to be processed
     */
    @Parameter(property="skins", required=true)
    // TODO handle base skins
    private String[] skins;
    @Parameter(property="project", readonly=true)
    private MavenProject project;
    /**
     * The list of mime-types to be included in processing
     */
    @Parameter
    private List<String> includedContentTypes;
    /**
     * The list of mime-types to be excluded in processing
     */
    @Parameter
    private List<String> excludedContentTypes;
    /**
     * List of included files.
     */
    @Parameter
    private List<String> includedFiles;
    /**
     * List of excluded files
     */
    @Parameter
    private List<String> excludedFiles;
    /**
     * Turns on compression with YUI Compressor (JavaScript/CSS compression)
     */
    @Parameter(property="compress")
    private boolean compress = true;
    /**
     * Turns on packing of JavaScript/CSS resources
     */
    @Parameter(property="pack")
    private String pack;
    /**
     * Mapping of file names to output file names
     */
    @Parameter
    // TODO review usage of properties?
    private FileNameMapping[] fileNameMappings = new FileNameMapping[0];
    @Parameter
    private ProcessMode processMode = ProcessMode.embedded;
    /**
     * The expression determines the root of the webapp resources
     */
    @Parameter(defaultValue="${basedir}/src/main/webapp")
    private String webRoot;
    /**
     * The encoding used for resource processing
     */
    @Parameter(property="encoding", defaultValue="${project.build.sourceEncoding}")
    private String encoding;
    // TODO handle resource locales
    private Locale resourceLocales;
    private Collection<ResourceKey> foundResources = Sets.newHashSet();
    private Ordering<ResourceKey> resourceOrdering;
    private Set<ResourceKey> resourcesWithKnownOrder;

    private Logger logger;

    /**
     * Checks if pack is assigned a boolean value. If yes it changes it to an expected value and prints a warning about the
     * deprecation.
     */
    private void checkBooleanPack() {
        if (pack != null) {
            if (pack.equals("true")) {
                pack = "packed";
                getLogger().warn("Boolean values for <pack> are deprecated, instead of \"true\" use \"packed\" for instance.");
            }

            if (pack.equals("false")) {
                pack = null;
                getLogger().warn("Boolean values for <pack> are deprecated, instead of \"false\" leave the parameter empty.");
            }
        }
    }

    // TODO executor parameters
    private ExecutorService createExecutorService() {
        int poolSize = pack != null ? 1 : Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(poolSize);
    }

    private Collection<ResourceProcessor> getDefaultResourceProcessors() {

        Charset charset = Charset.defaultCharset();
        if (!Strings.isNullOrEmpty(encoding)) {
            charset = Charset.forName(encoding);
        } else {
            getLog()
                .warn(
                    "Encoding is not set explicitly, CDK resources plugin will use default platform encoding for processing char-based resources");
        }
        if (compress) {
            return Arrays.<ResourceProcessor>asList(new JavaScriptCompressingProcessor(charset, getLogger()),
                new CSSCompressingProcessor(charset));
        } else {
            return Arrays.<ResourceProcessor>asList(new JavaScriptPackagingProcessor(charset));
        }

    }

    public Logger getLogger() {
        if (logger == null) {
            logger = new LoggerWrapper(getLog());
        }
        return logger;
    }

    private Predicate<Resource> createResourcesFilter() {
        Predicate<CharSequence> qualifierPredicate = MorePredicates.compose(includedFiles, excludedFiles,
            REGEX_CONTAINS_BUILDER_FUNCTION);

        Predicate<Resource> qualifierResourcePredicate = Predicates.compose(qualifierPredicate, RESOURCE_QUALIFIER_FUNCTION);

        Predicate<CharSequence> contentTypePredicate = MorePredicates.compose(includedContentTypes, excludedContentTypes,
            REGEX_CONTAINS_BUILDER_FUNCTION);
        Predicate<Resource> contentTypeResourcePredicate = Predicates.compose(contentTypePredicate, CONTENT_TYPE_FUNCTION);

        return Predicates.and(qualifierResourcePredicate, contentTypeResourcePredicate);
    }

    private URL resolveWebRoot() throws MalformedURLException {
        File result = new File(webRoot);
        if (!result.exists()) {
            result = new File(project.getBasedir(), webRoot);
        }
        if (!result.exists()) {
            return null;
        }

        return result.toURI().toURL();
    }

    private void scanDynamicResources(Collection<VFSRoot> cpFiles, ResourceFactory resourceFactory) throws Exception {
        ResourcesScanner scanner = new DynamicResourcesScanner(cpFiles, resourceFactory);
        scanner.scan();
        foundResources.addAll(scanner.getResources());
    }

    private void scanStaticResources(Collection<VirtualFile> resourceRoots) throws Exception {
        ResourcesScanner scanner = new StaticResourcesScanner(resourceRoots);
        scanner.scan();
        foundResources.addAll(scanner.getResources());
    }

    private void scanResourceOrdering(Collection<VFSRoot> cpFiles) throws Exception {
        ResourceOrderingScanner scanner = new ResourceOrderingScanner(cpFiles, getLogger());
        scanner.scan();
        resourceOrdering = scanner.getCompleteOrdering();
        resourcesWithKnownOrder = Sets.newLinkedHashSet(scanner.getResources());
    }

    private Collection<VFSRoot> fromUrls(Iterable<URL> urls) throws URISyntaxException, IOException {
        Collection<VFSRoot> result = Lists.newArrayList();

        for (URL url : urls) {
            if (url == null) {
                continue;
            }

            VFSRoot vfsRoot = VFS.getRoot(url);
            vfsRoot.initialize();
            result.add(vfsRoot);
        }

        return result;
    }

    private Collection<VFSRoot> getClasspathVfs(URL[] urls) throws URISyntaxException, IOException {
        return fromUrls(Arrays.asList(urls));
    }

    private Collection<VFSRoot> getWebrootVfs() throws URISyntaxException, IOException {
        return fromUrls(Collections.singletonList(resolveWebRoot()));
    }

    protected URL[] getProjectClassPath() {
        try {
            List<String> classpath = new ArrayList<String>();
            classpath.addAll(project.getCompileClasspathElements());
            classpath.add(project.getBuild().getOutputDirectory());

            URL[] urlClasspath = filter(transform(classpath, filePathToURL), notNull()).toArray(EMPTY_URL_ARRAY);
            return urlClasspath;
        } catch (DependencyResolutionRequiredException e) {
            getLog().error("Dependencies not resolved ", e);
        }

        return new URL[0];
    }

    protected ClassLoader createProjectClassLoader(URL[] cp) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        classLoader = new URLClassLoader(cp, classLoader);
        return classLoader;
    }

    /**
     * Initializes {@link ServiceTracker} to be able use it inside RichFaces framework code in order to handle dynamic
     * resources.
     *
     * Fake {@link ServiceFactoryModule} is used for this purpose.
     *
     * @throws IllegalStateException when initialization fails
     */
    private void initializeServiceTracker() {
        ServicesFactoryImpl servicesFactory = new ServicesFactoryImpl();
        ServiceTracker.setFactory(servicesFactory);

        ArrayList<Module> modules = new ArrayList<Module>();
        modules.add(new ServiceFactoryModule());
        // try {
        // modules.addAll(ServiceLoader.loadServices(Module.class));
        // } catch (ServiceException e) {
        // throw new IllegalStateException(e);
        // }
        servicesFactory.init(modules);
    }

    /**
     * Will determine ordering of resources from {@link ResourceDependency} annotations on renderers.
     *
     * Sorts foundResources using the determined ordering.
     */
    private void reorderFoundResources(Collection<VFSRoot> cpResources, DynamicResourceHandler dynamicResourceHandler,
        ResourceFactory resourceFactory) throws Exception {
        Faces faces = new FacesImpl(null, new FileNameMapperImpl(fileNameMappings), dynamicResourceHandler);
        faces.start();
        initializeServiceTracker();

        // if there are some resource libraries (.reslib), we need to expand them
        foundResources = new ResourceLibraryExpander().expandResourceLibraries(foundResources);

        faces.startRequest();
        scanResourceOrdering(cpResources);
        faces.stopRequest();

        faces.stop();

        foundResources = resourceOrdering.sortedCopy(foundResources);

        // do not package two versions of JFS JavaScript (remove it from resources for packaging)
        foundResources.remove(ResourceConstants.JSF_UNCOMPRESSED);
        // we need to load java.faces:jsf-uncompressed.js, but we will package
        resourcesWithKnownOrder.add(ResourceConstants.JSF_UNCOMPRESSED);

        getLog().debug("resourcesWithKnownOrder: " + resourcesWithKnownOrder);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        Faces faces = null;
        ExecutorService executorService = null;

        Collection<VFSRoot> webResources = null;
        Collection<VFSRoot> cpResources = null;

        try {
            URL[] projectCP = getProjectClassPath();
            ClassLoader projectCL = createProjectClassLoader(projectCP);
            Thread.currentThread().setContextClassLoader(projectCL);

            webResources = getWebrootVfs();
            cpResources = getClasspathVfs(projectCP);

            Collection<VirtualFile> resourceRoots = ResourceUtil.getResourceRoots(cpResources, webResources);
            getLog().debug("resourceRoots: " + resourceRoots);
            scanStaticResources(resourceRoots);
            StaticResourceHandler staticResourceHandler = new StaticResourceHandler(resourceRoots);
            ResourceFactory resourceFactory = new ResourceFactoryImpl(staticResourceHandler);

            scanDynamicResources(cpResources, resourceFactory);

            DynamicResourceHandler dynamicResourceHandler = new DynamicResourceHandler(staticResourceHandler, resourceFactory);

            getLog().debug("foundResources: " + foundResources);

            checkBooleanPack();

            if (pack != null) {
                reorderFoundResources(cpResources, dynamicResourceHandler, resourceFactory);
            }

            faces = new FacesImpl(null, new FileNameMapperImpl(fileNameMappings), dynamicResourceHandler);
            faces.start();

            ResourceWriterImpl resourceWriter = new ResourceWriterImpl(new File(resourcesOutputDir),
                getDefaultResourceProcessors(), getLogger(), resourcesWithKnownOrder);
            ResourceTaskFactoryImpl taskFactory = new ResourceTaskFactoryImpl(faces, pack);
            taskFactory.setResourceWriter(resourceWriter);

            executorService = createExecutorService();
            CompletionService<Object> completionService = new CountingExecutorCompletionService<Object>(executorService);
            taskFactory.setCompletionService(completionService);
            taskFactory.setSkins(skins);
            taskFactory.setLog(getLogger());
            taskFactory.setFilter(createResourcesFilter());
            taskFactory.submit(foundResources);

            getLog().debug(completionService.toString());

            Future<Object> future = null;
            while (true) {
                future = completionService.take();
                if (future != null) {
                    try {
                        future.get();
                    } catch (ExecutionException e) {
                        getLog().error(e);
                    }
                } else {
                    break;
                }
            }

            getLog().debug(completionService.toString());

            resourceWriter.writeProcessedResourceMappings(new File(staticResourceMappingFile), staticResourcePrefix);
            resourceWriter.close();
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        } finally {

            if (cpResources != null) {
                for (VFSRoot vfsRoot : cpResources) {
                    try {
                        vfsRoot.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            if (webResources != null) {
                for (VFSRoot vfsRoot : webResources) {
                    try {
                        vfsRoot.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            // TODO review finally block
            if (executorService != null) {
                executorService.shutdown();
            }
            if (faces != null) {
                faces.stop();
            }
            Thread.currentThread().setContextClassLoader(contextCL);
        }
    }
}
