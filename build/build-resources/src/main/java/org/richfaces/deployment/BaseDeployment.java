/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.deployment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.webapp.FacesServlet;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.facesconfig20.FacesConfigVersionType;
import org.jboss.shrinkwrap.descriptor.api.facesconfig20.WebFacesConfigDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.richfaces.arquillian.configuration.FundamentalTestConfiguration;
import org.richfaces.arquillian.configuration.FundamentalTestConfigurationContext;

import com.google.common.base.Function;
import com.google.common.collect.Sets;

/**
 * Provides base for all test deployments
 *
 * @author Lukas Fryc
 */
public class BaseDeployment {

    private final Logger log = Logger.getLogger(BaseDeployment.class.getName());
    private final FundamentalTestConfiguration configuration = FundamentalTestConfigurationContext.getProxy();
    private final File cacheDir = new File("target/shrinkwrap-resolver-cache/");

    private WebArchive archive;

    private WebFacesConfigDescriptor facesConfig;
    private WebAppDescriptor webXml;

    private final Set<String> mavenDependencies = Sets.newHashSet();
    private final Set<String> excludedMavenDependencies = Sets.newHashSet();

    /**
     * Constructs base deployment with:
     *
     * <ul>
     * <li>archive named by test case</li>
     * <li>empty faces-config.xml</li>
     * <li>web.xml with default FacesServlet mapping, welcome file index.xhtml and disabled some features which are not
     * necessary by default and Development stage turned on</li>
     * </ul>
     *
     * @param testClass
     */
    protected BaseDeployment(Class<?> testClass) {
        this(testClass == null ? null : testClass.getSimpleName());
    }

    protected BaseDeployment(String archiveName) {
        if (archiveName != null && !archiveName.isEmpty()) {
            this.archive = ShrinkWrap.create(WebArchive.class, archiveName + ".war");
        } else {
            this.archive = ShrinkWrap.create(WebArchive.class);
        }

        this.facesConfig = Descriptors.create(WebFacesConfigDescriptor.class).version(FacesConfigVersionType._2_0);

        this.webXml = Descriptors.create(WebAppDescriptor.class)
            .version("3.0")
            .addNamespace("xmlns:web", "http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd")
            .getOrCreateWelcomeFileList()
            .welcomeFile("faces/index.xhtml")
            .up()
            .getOrCreateContextParam()
            .paramName("javax.faces.PROJECT_STAGE")
            .paramValue("Development")
            .up()
            .getOrCreateServlet()
            .servletName(FacesServlet.class.getSimpleName())
            .servletClass(FacesServlet.class.getName())
            .loadOnStartup(1)
            .up()
            .getOrCreateServletMapping()
            .servletName(FacesServlet.class.getSimpleName())
            .urlPattern("*.jsf")
            .up()
            .getOrCreateServletMapping()
            .servletName(FacesServlet.class.getSimpleName())
            .urlPattern("/faces/*")
            .up();

        // Servlet container setup
        if (configuration.servletContainerSetup()) {
            log.info("Adding Servlet Container extensions for JSF");
            withServletContainerSetup();
        }

        if (configuration.isCurrentRichFacesVersion()) {
            addRequiredMavenDependencies();
        } else {
            log.log(Level.INFO, "Running test against RichFaces version: {0}", configuration.getRichFacesVersion());
        }

        if (configuration.getMavenSettings() != null && !configuration.getMavenSettings().isEmpty()) {
            log.log(Level.INFO, "Use Maven Settings: {0}", configuration.getMavenSettings());
        }
    }

    /**
     * Provides {@link WebArchive} available for modifications
     */
    public WebArchive archive() {
        return archive;
    }

    /**
     * Returns the final testable archive - packages all the resources which were configured separately, also adds empty
     * beans.xml file
     */
    public WebArchive getFinalArchive() {
        WebArchive finalArchive = archive
            .addAsWebInfResource(new StringAsset(facesConfig.exportAsString()), "faces-config.xml")
            .addAsWebInfResource(new StringAsset(webXml.exportAsString()), "web.xml")
            .addAsWebInfResource(new File("src/test/resources/beans.xml"));

        // add library dependencies
        exportMavenDependenciesToArchive(finalArchive);

        return finalArchive;
    }

    /**
     * Allows to modify contents of faces-config.xml.
     *
     * Takes function which transforms original faces-config.xml and returns modified one
     */
    public void facesConfig(Function<WebFacesConfigDescriptor, WebFacesConfigDescriptor> transform) {
        this.facesConfig = transform.apply(this.facesConfig);
    }

    /**
     * Allows to modify contents of web.xml.
     *
     * Takes function which transforms original web.xml and returns modified one
     */
    public void webXml(Function<WebAppDescriptor, WebAppDescriptor> transform) {
        this.webXml = transform.apply(this.webXml);
    }

    /**
     * Resolves maven dependencies, either by {@link MavenDependencyResolver} or from file cache
     */
    private void exportMavenDependenciesToArchive(WebArchive finalArchive) {

        Set<File> jarFiles = Sets.newHashSet();

        for (String dependency : mavenDependencies) {
            try {
                File dependencyDir = new File(cacheDir, dependency);

                if (!dependencyDir.exists()) {
                    resolveMavenDependency(dependency, dependencyDir);
                } else if (dependencyDirIsStale(dependencyDir)) {
                    cleanDependencyDir(dependencyDir);
                    resolveMavenDependency(dependency, dependencyDir);
                }

                File[] listFiles = dependencyDir.listFiles(new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".jar");
                    }
                });

                jarFiles.addAll(Arrays.asList(listFiles));
            } catch (Exception e) {
                throw new IllegalStateException("Can't resolve maven dependency: " + dependency, e);
            }
        }

        File[] files = jarFiles.toArray(new File[jarFiles.size()]);
        finalArchive.addAsLibraries(files);
    }

    private boolean dependencyDirIsStale(File dir) {
        long lastModified = dir.lastModified();
        long expires = lastModified + 720000;

        return System.currentTimeMillis() > expires;
    }

    private void cleanDependencyDir(File dir) {
        for (File file : dir.listFiles()) {
            file.delete();
        }
        dir.delete();
    }

    /**
     * Adds maven artifact as library dependency
     */
    public BaseDeployment addMavenDependency(String... dependencies) {
        mavenDependencies.addAll(Arrays.asList(dependencies));
        return this;
    }

    /**
     * Adds patters for Maven library dependency exclusion
     */
    public BaseDeployment excludeMavenDependency(String... dependencies) {
        excludedMavenDependencies.addAll(Arrays.asList(dependencies));
        return this;
    }

    /**
     * Adds dependencies which are necessary to deploy onto Servlet containers (Tomcat, Jetty)
     */
    private BaseDeployment withServletContainerSetup() {
        addMavenDependency(configuration.getJsfImplementation());

        addMavenDependency("org.jboss.weld.servlet:weld-servlet");

        addMavenDependency("javax.annotation:jsr250-api:1.0");
        addMavenDependency("javax.servlet:jstl:1.2");

        webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            public WebAppDescriptor apply(WebAppDescriptor webXml) {

                // setup Weld Servlet
                webXml
                    .createListener()
                    .listenerClass("org.jboss.weld.environment.servlet.Listener");

                return webXml;
            }
        });

        return this;
    }

    /**
     * Adds Hibernate validator dependency, but only when using Servlet container (Tomcat, Jetty).
     */
    public BaseDeployment addHibernateValidatorWhenUsingServletContainer() {
        if (configuration.servletContainerSetup()) {
            return addMavenDependency("org.hibernate:hibernate-validator");
        }
        return this;
    }

    /**
     * <p>
     * Add basic dependencies which RichFaces depends on.</p>
     *
     * <p>
     * This dependencies would be brought transitively by org.richfaces:richfaces:jar artifact, however in snapshot builds we
     * don't rely on this dependency and we use target/richfaces.jar instead.</p>
     */
    private void addRequiredMavenDependencies() {
        addMavenDependency("com.google.guava:guava", "net.sourceforge.cssparser:cssparser");
    }

    /**
     * Resolves Maven dependency and writes it to the cache, so it can be reused next run
     */
    private void resolveMavenDependency(String missingDependency, File dir) {

        MavenResolverSystem resolver = getMavenResolver();

        JavaArchive[] dependencies;

        if (missingDependency.matches("^[^:]+:[^:]+:[^:]+")) {
            // resolution of the artifact without a version specified
            dependencies = resolver.resolve(missingDependency).withClassPathResolution(false).withTransitivity()
                .as(JavaArchive.class);
        } else {
            // resolution of the artifact without a version specified
            dependencies = resolver.loadPomFromFile("pom.xml").resolve(missingDependency)
                .withClassPathResolution(false).withTransitivity().as(JavaArchive.class);
        }

        for (JavaArchive archive : dependencies) {
            dir.mkdirs();
            if (mavenDependencyExcluded(archive.getName())) {
                continue;
            }
            final File outputFile = new File(dir, archive.getName());
            archive.as(ZipExporter.class).exportTo(outputFile, true);
        }
    }

    private MavenResolverSystem getMavenResolver() {
        String mavenSettings = configuration.getMavenSettings();
        if (mavenSettings == null || mavenSettings.isEmpty()) {
            return Maven.resolver();
        } else {
            return Maven.configureResolver().fromFile(mavenSettings);
        }
    }

    private boolean mavenDependencyExcluded(String archiveName) {
        for (String exclude : excludedMavenDependencies) {
            if (archiveName.contains(exclude)) {
                return true;
            }
        }
        return false;
    }
}
