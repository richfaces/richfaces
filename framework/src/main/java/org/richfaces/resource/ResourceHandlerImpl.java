/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2009 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.application.ServiceTracker;
import org.richfaces.cache.Cache;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.html.ResourceLibraryRenderer;
import org.richfaces.util.RequestStateManager.BooleanRequestStateVariable;
import org.richfaces.util.Util;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
// TODO extract caching
public class ResourceHandlerImpl extends ResourceHandlerWrapper {
    public static final String RICHFACES_RESOURCE_IDENTIFIER = "/rfRes/";
    public static final String RESOURCE_CACHE_NAME = "org.richfaces.ResourcesCache";
    public static final String HANDLER_START_TIME_ATTRIBUTE = ResourceHandlerImpl.class.getName() + ":StartTime";
    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();
    private ResourceFactory resourceFactory;
    private ResourceHandler defaultHandler;

    public ResourceHandlerImpl(ResourceHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
        this.resourceFactory = new ResourceFactoryImpl(defaultHandler);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormat.format("Instance of {0} resource handler created", getClass().getName()));
        }
    }

    public static String getResourcePathFromRequest(FacesContext context) {
        String resourceName = Util.decodeResourceURL(context);

        if (resourceName != null) {
            if (resourceName.startsWith(RICHFACES_RESOURCE_IDENTIFIER)) {
                return resourceName.substring(RICHFACES_RESOURCE_IDENTIFIER.length());
            } else {
                return null;
            }
        } else {
            LOGGER.warn("Resource key not found" + resourceName);
            return null;
        }
    }

    protected boolean isThisHandlerResourceRequest(FacesContext context) {
        Boolean resourceRequest = BooleanRequestStateVariable.ResourceRequest.get(context);

        if (resourceRequest == null) {
            String resourcePath = getResourcePathFromRequest(context);

            // TODO handle exclusions
            resourceRequest = (resourcePath != null) && (resourcePath.length() > 0);
            BooleanRequestStateVariable.ResourceRequest.set(context, resourceRequest);

            if (LOGGER.isDebugEnabled() && resourceRequest) {
                LOGGER.debug(MessageFormat.format("Resource request detected: {0}", resourcePath));
            }
        }

        return resourceRequest;
    }

    @Override
    public boolean isResourceRequest(FacesContext context) {
        return isThisHandlerResourceRequest(context) || defaultHandler.isResourceRequest(context);
    }

    private Resource lookupInCache(Cache cache, String resourceKey) {
        if (cache == null) {
            LOGGER.debug("No cache was provided");
            return null;
        }

        Resource resource = (Resource) cache.get(resourceKey);

        if (LOGGER.isDebugEnabled()) {
            if (resource == null) {
                LOGGER.debug("Resource was not located in cache");
            } else {
                LOGGER.debug("Resource was located in cache");
            }
        }

        return resource;
    }

    private static void sendNotModified(FacesContext context) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("User agent has actual resource copy - sending 304 status code");
        }

        // TODO send cacheable resource headers (ETag + LastModified)?
        context.getExternalContext().setResponseStatus(HttpServletResponse.SC_NOT_MODIFIED);
    }

    public static void sendResourceNotFound(FacesContext context) {
        context.getExternalContext().setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    public void handleResourceRequest(FacesContext context) throws IOException {
        if (isThisHandlerResourceRequest(context)) {
            ResourceCodec resourceCodec = ServiceTracker.getService(context, ResourceCodec.class);

            String resourcePath = getResourcePathFromRequest(context);

            assert (resourcePath != null) && (resourcePath.length() != 0);

            ResourceRequestData data = resourceCodec.decodeResource(context, resourcePath);
            assert (data != null);

            Cache cache = ServiceTracker.getService(context, Cache.class);
            Resource resource = lookupInCache(cache, data.getResourceKey());

            if (resource == null) {
                resource = resourceFactory.createResource(context, data);
            }

            if (resource == null) {
                sendResourceNotFound(context);
                return;
            }

            if (resource instanceof CacheableResource) {
                CacheableResource cacheableResource = (CacheableResource) resource;

                if (cacheableResource.isCacheable(context)) {

                    // TODO - we could move this part of code to ConcurrentMap so that
                    // only single thread does resource put
                    CachedResourceImpl cachedResource = new CachedResourceImpl();

                    cachedResource.initialize(resource);

                    // someone may provided this resource for us
                    // while we were reading it, check once again
                    resource = lookupInCache(cache, data.getResourceKey());

                    if (resource == null) {
                        // don't cache it on Development stage
                        if (!ProjectStage.Development.equals(context.getApplication().getProjectStage())) {
                            Date cacheExpirationDate = cachedResource.getExpired(context);
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug(new MessageFormat(
                                        "Storing {0} resource in cache until {1,date,dd MMM yyyy HH:mm:ss zzz}", Locale.US)
                                        .format(new Object[] { data.getResourceKey(), cacheExpirationDate }));
                            }
                            cache.put(data.getResourceKey(), cachedResource, cacheExpirationDate);
                        }
                        resource = cachedResource;
                    }
                }
            }

            if (resource.userAgentNeedsUpdate(context)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("User agent needs resource update, encoding resource");
                }

                ExternalContext externalContext = context.getExternalContext();
                Map<String, String> headers = resource.getResponseHeaders();

                for (Entry<String, String> headerEntry : headers.entrySet()) {
                    String headerName = headerEntry.getKey();
                    String headerValue = headerEntry.getValue();

                    // TODO should external context handles this itself?
                    if ("content-length".equals(headerName.toLowerCase(Locale.US))) {
                        try {
                            externalContext.setResponseContentLength(Integer.parseInt(headerValue));
                        } catch (NumberFormatException e) {

                            // TODO: handle exception
                        }
                    } else {
                        externalContext.setResponseHeader(headerName, headerValue);
                    }
                }

                // TODO null content type?
                String contentType = resource.getContentType();

                if (contentType != null) {
                    externalContext.setResponseContentType(contentType);
                }

                if (resource instanceof ContentProducerResource) {
                    ContentProducerResource contentProducerResource = (ContentProducerResource) resource;
                    contentProducerResource.encode(context);
                } else {
                    // TODO setup output buffer size according to configuration parameter
                    InputStream is = resource.getInputStream();
                    OutputStream os = externalContext.getResponseOutputStream();

                    try {
                        Util.copyStreamContent(is, os);
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug(e.getMessage(), e);
                                }
                            }
                        }

                        // TODO flush resource
                        // TODO dispose resource
                    }
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Resource succesfully encoded");
                }
            } else {
                sendNotModified(context);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Passing request to the next resource handler in chain");
            }

            defaultHandler.handleResourceRequest(context);
        }
    }

    @Override
    public Resource createResource(String resourceName, String libraryName, String contentType) {
        Resource resource = resourceFactory.createResource(resourceName, libraryName, contentType);
        if (resource == null) {
            resource = defaultHandler.createResource(resourceName, libraryName, contentType);
        }

        return resource;
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        return createResource(resourceName, libraryName, null);
    }

    @Override
    public Resource createResource(String resourceName) {
        return createResource(resourceName, null, null);
    }

    @Override
    public String getRendererTypeForResourceName(String resourceName) {

        if (resourceName.endsWith(".ecss")) {
            return "javax.faces.resource.Stylesheet";
        }

        if (resourceName.endsWith(ResourceLibraryRenderer.RESOURCE_LIBRARY_EXTENSION)) {
            return ResourceLibraryRenderer.RENDERER_TYPE;
        }

        return defaultHandler.getRendererTypeForResourceName(resourceName);
    }

    @Override
    public boolean libraryExists(String libraryName) {
        return defaultHandler.libraryExists(libraryName);
    }

    @Override
    public ResourceHandler getWrapped() {
        return defaultHandler;
    }
}