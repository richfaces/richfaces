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
package org.richfaces.resource;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.richfaces.application.DependencyInjector;
import org.richfaces.application.ServiceTracker;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.resource.external.MappedResourceFactory;
import org.richfaces.resource.external.ResourceTracker;
import org.richfaces.resource.mapping.ResourcePath;
import org.richfaces.webapp.ResourceServlet;

import com.google.common.base.Function;
import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 *
 */
public class ResourceFactoryImpl implements ResourceFactory {

    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();

    private static final String MAPPED_RESOURCES_RESOLUTION_STACK = MappedResourceFactory.class.getName()
            + ".MAPPED_RESOURCES_RESOLUTION_STACK";

    private static final Function<Entry<String, String>, MappedResourceData> DYNAMIC_MAPPINGS_DATA_PRODUCER = new Function<Entry<String, String>, MappedResourceData>() {
        public MappedResourceData apply(Entry<String, String> from) {
            String resourceLocation = from.getValue();
            Map<String, String> params = ResourceUtils.parseResourceParameters(resourceLocation);
            String resourceQualifier = extractParametersFromResourceName(resourceLocation);

            return new MappedResourceData(ResourceKey.create(resourceQualifier), params);
        }
    };
    private ResourceHandler defaultHandler;
    // private Map<ResourceKey, ExternalStaticResourceFactory> externalStaticResourceFactories;
    private Map<ResourceKey, MappedResourceData> mappedResourceDataMap;
    private MappedResourceFactory mappedResourceFactory;
    private ResourceTracker resourceTracker;

    public ResourceFactoryImpl(ResourceHandler defaultHandler) {
        super();

        this.defaultHandler = defaultHandler;
        this.mappedResourceDataMap = ResourceUtils.readMappings(DYNAMIC_MAPPINGS_DATA_PRODUCER,
                ResourceFactory.DYNAMIC_RESOURCE_MAPPINGS);
        this.mappedResourceFactory = ServiceTracker.getProxy(MappedResourceFactory.class);
        this.resourceTracker = ServiceTracker.getProxy(ResourceTracker.class);
    }

    private static String extractParametersFromResourceName(String resourceName) {
        if (!(resourceName.lastIndexOf("{") != -1)) {
            return resourceName;
        }
        return resourceName.substring(0, resourceName.lastIndexOf("{"));
    }

    private void logResourceProblem(FacesContext context, Throwable throwable, String messagePattern, Object... arguments) {
        boolean isProductionStage = context.isProjectStage(ProjectStage.Production);

        if (LOGGER.isWarnEnabled() || (!isProductionStage && LOGGER.isInfoEnabled())) {
            String formattedMessage = MessageFormat.format(messagePattern, arguments);

            if (throwable != null) {
                LOGGER.warn(formattedMessage, throwable);
            } else {
                if (isProductionStage) {
                    LOGGER.info(formattedMessage);
                } else {
                    LOGGER.warn(formattedMessage);
                }
            }
        }
    }

    private void logMissingResource(FacesContext context, String resourceData) {
        logResourceProblem(context, null, "Resource {0} was not found", resourceData);
    }

    private Resource createCompiledCSSResource(ResourceKey resourceKey) {
        Resource sourceResource = defaultHandler.createResource(resourceKey.getResourceName(), resourceKey.getLibraryName(),
                "text/plain");
        if (sourceResource != null) {
            return new CompiledCSSResource(sourceResource);
        }

        return null;
    }

    protected void injectProperties(Object resource, Map<String, String> parameters) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        Map<Object, Object> attributes = facesContext.getAttributes();
        try {
            attributes.put(ResourceParameterELResolver.CONTEXT_ATTRIBUTE_NAME, parameters);
            ServiceTracker.getService(DependencyInjector.class).inject(facesContext, resource);
        } finally {
            attributes.remove(ResourceParameterELResolver.CONTEXT_ATTRIBUTE_NAME);
        }
    }

    private boolean isCacheableSet(Class<?> c) {
        DynamicUserResource annotation = c.getAnnotation(DynamicUserResource.class);
        return annotation != null && annotation.cacheable();
    }

    private boolean isVersionedSet(Class<?> c) {
        DynamicUserResource annotation = c.getAnnotation(DynamicUserResource.class);
        return annotation != null && annotation.versioned();
    }

    private Resource createDynamicUserResourceInstance(Class<?> loadedClass) throws Exception, LinkageError {
        String resourceName = loadedClass.getName();

        boolean checkResult = false;

        DynamicUserResource dynamicUserResource = loadedClass.getAnnotation(DynamicUserResource.class);

        if (dynamicUserResource != null) {
            checkResult = true;

            LOGGER.debug(MessageFormat.format("Dynamic resource annotation is present on resource class {0}", resourceName));
        }

        if (!checkResult) {
            DynamicResource dynamicResource = loadedClass.getAnnotation(DynamicResource.class);
            if (dynamicResource != null) {
                LOGGER.debug(MessageFormat.format("Dynamic resource annotation is present on resource class {0}", resourceName));

                checkResult = true;
            }
        }

        if (!checkResult) {
            LOGGER.debug(MessageFormat.format("Dynamic resource annotation is not present on resource class {0}", resourceName));

            checkResult = checkResourceMarker(resourceName);
        }

        if (!checkResult) {
            return null;
        }

        Resource result = null;

        if (Java2DUserResource.class.isAssignableFrom(loadedClass)) {
            Java2DUserResource java2DUserResource = (Java2DUserResource) loadedClass.newInstance();
            result = createResource(java2DUserResource);
        } else if (UserResource.class.isAssignableFrom(loadedClass)) {
            UserResource userResource = (UserResource) loadedClass.newInstance();
            result = createResource(userResource);
        }

        return result;
    }

    private Resource createDynamicResourceInstance(Class<?> loadedClass) throws Exception, LinkageError {
        String resourceName = loadedClass.getName();

        boolean checkResult = false;

        DynamicResource annotation = loadedClass.getAnnotation(DynamicResource.class);

        if (annotation != null) {
            LOGGER.debug(MessageFormat.format("Dynamic resource annotation is present on resource class {0}", resourceName));

            checkResult = true;
        } else {
            LOGGER.debug(MessageFormat.format("Dynamic resource annotation is not present on resource class {0}", resourceName));
        }

        if (!checkResult) {
            checkResult = checkResourceMarker(resourceName);
        }

        if (!checkResult) {
            return null;
        }

        Class<? extends Resource> resourceClass = loadedClass.asSubclass(Resource.class);
        Resource result = (Resource) resourceClass.newInstance();

        return result;
    }

    /**
     * Should be called only if {@link #isResourceExists(String)} returns <code>true</code>
     *
     * @param resourceKey
     * @param parameters
     * @return
     */
    protected Resource createHandlerDependentResource(ResourceKey resourceKey, Map<String, String> parameters) {
        if (!Strings.isNullOrEmpty(resourceKey.getLibraryName())) {
            return null;
        }

        String resourceName = resourceKey.getResourceName();

        Resource resource = null;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        if (contextClassLoader != null) {
            try {
                Class<?> loadedClass = Class.forName(resourceName, false, contextClassLoader);

                resource = createDynamicUserResourceInstance(loadedClass);
                if (resource == null) {
                    resource = createDynamicResourceInstance(loadedClass);
                }

                if (resource != null) {
                    resource.setResourceName(resourceName);

                    if (parameters != null) {
                        if (resource instanceof BaseResourceWrapper<?>) {
                            BaseResourceWrapper<?> baseResourceWrapper = (BaseResourceWrapper<?>) resource;

                            injectProperties(baseResourceWrapper.getWrapped(), parameters);
                        } else {
                            injectProperties(resource, parameters);
                        }
                    }

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(MessageFormat.format("Successfully created instance of {0} resource", resourceName));
                    }
                }
            } catch (ClassNotFoundException e) {
                // do nothing
            } catch (Exception e) {
                logResourceProblem(FacesContext.getCurrentInstance(), e, "Error creating resource {0}", resourceName);
            } catch (LinkageError e) {
                logResourceProblem(FacesContext.getCurrentInstance(), e, "Error creating resource {0}", resourceName);
            }
        }

        return resource;
    }

    private boolean checkResourceMarker(String resourceName) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        URL resourceMarkerUrl = contextClassLoader.getResource("META-INF/" + resourceName + ".resource.properties");

        boolean result = (resourceMarkerUrl != null);

        if (LOGGER.isDebugEnabled()) {
            if (result) {
                LOGGER.debug(MessageFormat.format("Marker file for {0} resource found in classpath", resourceName));
            } else {
                LOGGER.debug(MessageFormat.format("Marker file for {0} resource does not exist", resourceName));
            }
        }
        return result;
    }

    public Resource createResource(FacesContext context, ResourceRequestData resourceData) {
        String resourceName = resourceData.getResourceName();

        if ((resourceName == null) || (resourceName.length() == 0)) {
            return null;
        }

        String libraryName = resourceData.getLibraryName();
        Resource resource = createDynamicResource(new ResourceKey(resourceName, libraryName), false);

        if (resource == null) {
            logMissingResource(context, resourceData.getResourceKey());
            return null;
        }

        if (resource instanceof VersionedResource) {
            VersionedResource versionedResource = (VersionedResource) resource;
            String existingVersion = versionedResource.getVersion();
            String requestedVersion = resourceData.getVersion();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(MessageFormat.format("Client requested {0} version of resource, server has {1} version",
                        String.valueOf(requestedVersion), String.valueOf(existingVersion)));
            }

            if ((existingVersion != null) && (requestedVersion != null) && !existingVersion.equals(requestedVersion)) {
                logResourceProblem(context, null, "Resource {0} of version {1} was not found", resourceName, requestedVersion);
                return null;
            }
        }

        Object decodedData = resourceData.getData();

        if (LOGGER.isDebugEnabled()) {
            if (decodedData != null) {
                LOGGER.debug("Resource state data succesfully decoded");
            } else {
                LOGGER.debug("Resource state data decoded as null");
            }
        }

        ResourceUtils.restoreResourceState(context, resource, decodedData);

        return resource;
    }

    public Resource createResource(String resourceName, String libraryName, String contentType) {
        ResourceKey resourceKey = new ResourceKey(resourceName, libraryName);

        Resource resource = createMappedResource(resourceKey);

        if (resource != null) {
            return resource;
        } else {
            return createDynamicResource(resourceKey, true);
        }
    }

    private Resource createMappedResource(ResourceKey resourceKey) {
        final FacesContext context = FacesContext.getCurrentInstance();

        // do not map resources for ResourceServlet requests (they should be already mapped)
        if (context.getExternalContext().getRequestMap().get(ResourceServlet.RESOURCE_SERVLET_REQUEST_FLAG) == Boolean.TRUE) {
            return null;
        }

        Resource mappedResource = resolveMappedResource(context, resourceKey);

        if (mappedResource == null) {
            return null;
        }

        resourceTracker.markResourceRendered(context, resourceKey);
        ResourcePath path = new ResourcePath(mappedResource.getRequestPath());
        Set<ResourceKey> aggregatedResources = mappedResourceFactory.getAggregatedResources(path);
        for (ResourceKey key : aggregatedResources) {
            resourceTracker.markResourceRendered(context, key);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Resource '%s' is redirected to following resource path: %s", resourceKey, path));
            if (aggregatedResources.size() >= 1) {
                LOGGER.debug(String.format("Following resources are marked as rendered: %s", resourceKey, aggregatedResources));
            }
        }

        return mappedResource;
    }

    private Resource resolveMappedResource(FacesContext context, ResourceKey resourceKey) {
        // check whether we are not resolving given key (prevents infinite loop)
        Deque<ResourceKey> mappedResourcesResolutionStack = getMappedResourcesResolutionStack(context);

        if (mappedResourcesResolutionStack.contains(resourceKey)) {
            return null;
        }

        mappedResourcesResolutionStack.push(resourceKey);
        try {
            return mappedResourceFactory.createResource(context, resourceKey);
        } finally {
            mappedResourcesResolutionStack.pop();
        }
    }

    private Deque<ResourceKey> getMappedResourcesResolutionStack(FacesContext context) {
        @SuppressWarnings("unchecked")
        LinkedList<ResourceKey> list = (LinkedList<ResourceKey>) context.getAttributes().get(MAPPED_RESOURCES_RESOLUTION_STACK);
        if (list == null) {
            list = new LinkedList<ResourceKey>();
            context.getAttributes().put(MAPPED_RESOURCES_RESOLUTION_STACK, list);
        }
        return list;
    }

    protected Resource createDynamicResource(ResourceKey resourceKey, boolean useDependencyInjection) {
        Resource result = null;

        Map<String, String> params = null;

        MappedResourceData mappedResourceData = mappedResourceDataMap.get(resourceKey);
        ResourceKey actualKey;
        if (mappedResourceData != null) {
            actualKey = mappedResourceData.getResourceKey();
            if (useDependencyInjection) {
                params = mappedResourceData.getParams();
            }
        } else {
            actualKey = resourceKey;
            if (useDependencyInjection) {
                params = Collections.<String, String>emptyMap();
            }
        }

        if (Strings.isNullOrEmpty(resourceKey.getResourceName())) {
            return null;
        }

        if (actualKey.getResourceName().endsWith(".ecss")) {
            // TODO nick - params?
            result = createCompiledCSSResource(actualKey);
        } else {
            result = createHandlerDependentResource(actualKey, params);
        }

        if (result != null) {
            result.setLibraryName(resourceKey.getLibraryName());
            result.setResourceName(resourceKey.getResourceName());
        } else if (mappedResourceData != null) {
            result = defaultHandler.createResource(actualKey.getResourceName(), actualKey.getLibraryName());
        }

        return result;
    }

    public Collection<ResourceKey> getMappedDynamicResourceKeys() {
        return Collections.unmodifiableSet(mappedResourceDataMap.keySet());
    }

    protected Resource createResource(Java2DUserResource resource) {
        boolean cacheable = isCacheableSet(resource.getClass());
        boolean versioned = isVersionedSet(resource.getClass());

        if (resource instanceof Java2DAnimatedUserResource) {
            Java2DAnimatedUserResource java2DAnimatedUserResource = (Java2DAnimatedUserResource) resource;
            return new Java2DAnimatedUserResourceWrapperImpl(java2DAnimatedUserResource, cacheable, versioned);
        } else {
            return new Java2DUserResourceWrapperImpl(resource, cacheable, versioned);
        }
    }

    protected Resource createResource(UserResource resource) {
        boolean cacheable = isCacheableSet(resource.getClass());
        boolean versioned = isVersionedSet(resource.getClass());

        return new UserResourceWrapperImpl(resource, cacheable, versioned);
    }

    private static class MappedResourceData {
        private ResourceKey resourceKey;
        private Map<String, String> params;

        MappedResourceData(ResourceKey resourceKey, Map<String, String> params) {
            this.resourceKey = resourceKey;
            this.params = params;
        }

        public ResourceKey getResourceKey() {
            return resourceKey;
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

}
