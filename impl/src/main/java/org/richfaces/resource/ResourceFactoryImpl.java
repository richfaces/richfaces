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
package org.richfaces.resource;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.richfaces.application.DependencyInjector;
import org.richfaces.application.ServiceTracker;
import org.richfaces.util.PropertiesUtil;
import org.richfaces.util.Util;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski
 * 
 */
public class ResourceFactoryImpl implements ResourceFactory {

    private static final Joiner RESOURCE_QUALIFIER_JOINER = Joiner.on(':').skipNulls();
    
    private static final ResourceLogger LOGGER = ResourceLogger.INSTANCE;

    private static class ExternalStaticResourceFactory {

        private String resourceName;

        private String libraryName;

        private String resourceLocation;

        private boolean skinDependent;

        public Resource createResource() {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalStaticResource resource = new ExternalStaticResource(resourceLocation, skinDependent);
            
            resource.setResourceName(resourceName);
            resource.setLibraryName(libraryName);
            resource.setContentType(facesContext.getExternalContext().getMimeType(resourceLocation));
            
            return resource;
        }

        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }

        public void setLibraryName(String libraryName) {
            this.libraryName = libraryName;
        }

        public void setResourceLocation(String resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public void setSkinDependent(boolean skinDependent) {
            this.skinDependent = skinDependent;
        }
    }

    private ResourceHandler defaultHandler;

    private Map<String, ExternalStaticResourceFactory> externalStaticResourceFactories;

    public ResourceFactoryImpl(ResourceHandler defaultHandler) {
        super();
        this.defaultHandler = defaultHandler;

        initializeExternalResourcesMap();
    }

    private String getResourceNameFromQualifier(String qualifier) {
        int idx = qualifier.lastIndexOf(':');
        if (idx < 0) {
            return qualifier;
        }
        
        return qualifier.substring(idx + 1);
    }
    
    private String getLibraryNameFromQualifier(String qualifier) {
        int idx = qualifier.lastIndexOf(':');
        if (idx < 0) {
            return null;
        }
        
        return qualifier.substring(0, idx);
    }

    private String getResourceQualifier(String resourceName, String libraryName) {
        return RESOURCE_QUALIFIER_JOINER.join(libraryName, resourceName);
    }
    
    private void initializeExternalResourcesMap() {
        externalStaticResourceFactories = Maps.newHashMap();

        Properties properties = new Properties();
        PropertiesUtil.loadProperties(properties, ResourceFactory.STATIC_RESOURCE_MAPPINGS);

        Set<Entry<Object, Object>> entries = properties.entrySet();
        for (Entry<Object, Object> entry : entries) {
            String resourceQualifier = (String) entry.getKey();

            String resourceLocation = (String) entry.getValue();
            boolean skinDependent = false;
            if (resourceLocation.startsWith(SKINNED_RESOURCE_PREFIX)) {
                resourceLocation = resourceLocation.substring(SKINNED_RESOURCE_PREFIX.length());
                skinDependent = true;
            }
            
            ExternalStaticResourceFactory factory = new ExternalStaticResourceFactory();
            factory.setResourceLocation(resourceLocation);
            factory.setSkinDependent(skinDependent);
            factory.setResourceName(getResourceNameFromQualifier(resourceQualifier));
            factory.setLibraryName(getLibraryNameFromQualifier(resourceQualifier));

            externalStaticResourceFactories.put(resourceQualifier, factory);
        }

        externalStaticResourceFactories = Collections.unmodifiableMap(externalStaticResourceFactories);
    }

    private String extractParametersFromResourceName(String resourceName) {
        if (!(resourceName.lastIndexOf("?") != -1)) {
            return resourceName;
        }
        return resourceName.substring(0, resourceName.lastIndexOf("?"));
    }

    private Resource createCompiledCSSResource(String resourceName, String libraryName) {
        Resource sourceResource = defaultHandler.createResource(resourceName, libraryName, "text/plain");
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

    /**
     * Should be called only if {@link #isResourceExists(String)} returns <code>true</code>
     * 
     * @param resourceName
     * @return
     */
    protected Resource createHandlerDependentResource(String resourceName, Map<String, String> parameters) {
        Resource resource = null;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        if (contextClassLoader != null) {
            try {
                Class<?> loadedClass = Class.forName(resourceName, false, contextClassLoader);

                boolean legitimateResource = false;

                DynamicResource annotation = loadedClass.getAnnotation(DynamicResource.class);
                legitimateResource = (annotation != null);
                if (legitimateResource) {
                    LOGGER.debug(MessageFormat.format("Dynamic resource annotation is present on resource class {0}",
                        resourceName));
                } else {
                    LOGGER.debug(MessageFormat.format(
                        "Dynamic resource annotation is not present on resource class {0}", resourceName));
                }

                if (!legitimateResource) {
                    // TODO resource marker extension name?
                    URL resourceMarkerUrl = contextClassLoader.getResource("META-INF/" + resourceName
                        + ".resource.properties");

                    legitimateResource = resourceMarkerUrl != null;

                    if (LOGGER.isDebugEnabled()) {
                        if (legitimateResource) {
                            LOGGER.debug(MessageFormat.format("Marker file for {0} resource found in classpath",
                                resourceName));
                        } else {
                            LOGGER.debug(MessageFormat.format("Marker file for {0} resource does not exist",
                                resourceName));
                        }
                    }
                }

                if (legitimateResource) {
                    Object wrappedResource;
                    if (Java2DUserResource.class.isAssignableFrom(loadedClass)) {
                        Java2DUserResource java2DUserResource = (Java2DUserResource) loadedClass.newInstance();
                        wrappedResource = java2DUserResource;
                        resource = new Java2DUserResourceWrapperImpl(java2DUserResource);
                    } else if (UserResource.class.isAssignableFrom(loadedClass)) {
                        UserResource userResource = (UserResource) loadedClass.newInstance();
                        wrappedResource = userResource;
                        resource = new UserResourceWrapperImpl(userResource);
                    } else {
                        Class<? extends Resource> resourceClass = loadedClass.asSubclass(Resource.class);
                        resource = (Resource) resourceClass.newInstance();
                        wrappedResource = resource;
                    }

                    if (parameters != null) {
                        injectProperties(wrappedResource, parameters);
                    }

                    resource.setResourceName(resourceName);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(MessageFormat
                            .format("Successfully created instance of {0} resource", resourceName));
                    }
                }
            } catch (ClassNotFoundException e) {
                // do nothing
            } catch (Exception e) {
                LOGGER.logResourceProblem(FacesContext.getCurrentInstance(), e, "Error creating resource {0}",
                    resourceName);
            } catch (LinkageError e) {
                LOGGER.logResourceProblem(FacesContext.getCurrentInstance(), e, "Error creating resource {0}",
                    resourceName);
            }
        }

        return resource;
    }

    public Resource createResource(FacesContext context, ResourceCodecData resourceData) {
        String resourceName = resourceData.getResourceName();

        if ((resourceName == null) || (resourceName.length() == 0)) {
            return null;
        }

        String libraryName = resourceData.getLibraryName();
        Resource resource = createResource(resourceName, libraryName, null);

        if (resource == null) {
            LOGGER.logMissingResource(context, resourceData.getResourceKey());
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
                LOGGER.logResourceProblem(context, null, "Resource {0} of version {1} was not found", resourceName,
                    requestedVersion);
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

        Util.restoreResourceState(context, resource, decodedData);

        return resource;
    }

    public Resource createResource(String resourceName, String libraryName, String contentType) {
        String resourceQualifier = getResourceQualifier(resourceName, libraryName);
        ExternalStaticResourceFactory externalStaticResourceFactory = externalStaticResourceFactories.get(resourceQualifier);
        if (externalStaticResourceFactory != null) {
            return externalStaticResourceFactory.createResource();
        }
        
        Resource result = null;
        Map<String, String> params = Util.parseResourceParameters(resourceName);
        resourceName = extractParametersFromResourceName(resourceName);
        if (resourceName.endsWith(".ecss")) {
            // TODO nick - params?
            result = createCompiledCSSResource(resourceName, libraryName);
        } else {
            // TODO nick - libraryName as package name?
            if ((resourceName != null) && ((libraryName == null) || (libraryName.length() == 0))) {
                result = createHandlerDependentResource(resourceName, params);
            }
        }

        return result;
    }

}
