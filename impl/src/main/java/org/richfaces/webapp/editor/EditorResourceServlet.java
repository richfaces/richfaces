package org.richfaces.webapp.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.application.ServiceTracker;
import org.richfaces.cache.Cache;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.resource.CacheableResource;
import org.richfaces.resource.CachedResourceImpl;
import org.richfaces.resource.ContentProducerResource;
import org.richfaces.resource.ResourceCodec;
import org.richfaces.resource.ResourceFactory;
import org.richfaces.resource.ResourceFactoryImpl;
import org.richfaces.resource.ResourceRequestData;
import org.richfaces.util.Util;

import com.sun.faces.context.ApplicationMap;

public class EditorResourceServlet extends HttpServlet {

    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();
    private static final String RICHFACES_RESOURCE_IDENTIFIER = EditorResourceServletContainerInitializer.EDITOR_RESOURCES_DEFAULT_MAPPING;

    public EditorResourceServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        FacesContext facesContext = new CustomFacesContext(request, response);
        CustomFacesContext.setFacesContext(facesContext);

        ResourceFactory resourceFactory = new ResourceFactoryImpl(new com.sun.faces.application.resource.ResourceHandlerImpl());

        ResourceCodec resourceCodec = ServiceTracker.getService(ResourceCodec.class);

        String resourcePath = getResourcePathFromRequest(facesContext);

        assert (resourcePath != null) && (resourcePath.length() != 0);

        ResourceRequestData data = resourceCodec.decodeResource(facesContext, resourcePath);
        assert (data != null);

        Cache cache = ServiceTracker.getService(Cache.class);
        Resource resource = lookupInCache(cache, data.getResourceKey());

        if (resource == null) {
            resource = resourceFactory.createResource(facesContext, data);
        }

        if (resource == null) {
            sendResourceNotFound(facesContext);
            return;
        }

        if (resource instanceof CacheableResource) {
            CacheableResource cacheableResource = (CacheableResource) resource;

            if (cacheableResource.isCacheable(facesContext)) {

                // TODO - we could move this part of code to ConcurrentMap so that
                // only single thread does resource put
                CachedResourceImpl cachedResource = new CachedResourceImpl();

                cachedResource.initialize(resource);

                // someone may provided this resource for us
                // while we were reading it, check once again
                resource = lookupInCache(cache, data.getResourceKey());

                if (resource == null) {
                    Date cacheExpirationDate = cachedResource.getExpired(facesContext);

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(new MessageFormat("Storing {0} resource in cache until {1,date,dd MMM yyyy HH:mm:ss zzz}",
                                Locale.US).format(new Object[] { data.getResourceKey(), cacheExpirationDate }));
                    }

                    cache.put(data.getResourceKey(), cachedResource, cacheExpirationDate);
                    resource = cachedResource;
                }
            }
        }

        if (resource.userAgentNeedsUpdate(facesContext)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("User agent needs resource update, encoding resource");
            }

            ExternalContext externalContext = facesContext.getExternalContext();
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
                contentProducerResource.encode(facesContext);
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
            sendNotModified(facesContext);
        }
    }

    private Resource lookupInCache(Cache cache, String resourceKey) {
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

    protected static String getResourcePathFromRequest(FacesContext context) {
        return Util.decodeResourceURL(context);
    }

    private static String decodeResourceURL(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        String resourceName = null;
        String facesMapping = Util.getMappingForRequest(context);

        if (facesMapping != null) {
            if (facesMapping.startsWith("/")) {

                // prefix mapping
                resourceName = externalContext.getRequestPathInfo();
            } else {
                String requestServletPath = externalContext.getRequestServletPath();

                resourceName = requestServletPath.substring(0, requestServletPath.length() - facesMapping.length());
            }
        }

        return resourceName;
    }

    private static void sendResourceNotFound(FacesContext context) {
        context.getExternalContext().setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private static void sendNotModified(FacesContext context) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("User agent has actual resource copy - sending 304 status code");
        }

        // TODO send cacheable resource headers (ETag + LastModified)?
        context.getExternalContext().setResponseStatus(HttpServletResponse.SC_NOT_MODIFIED);
    }

    private static class CustomFacesContext extends FakeFacesContext {

        private HttpServletRequest request;
        private HttpServletResponse response;

        public CustomFacesContext(HttpServletRequest request, HttpServletResponse response) {
            super();
            this.request = request;
            this.response = response;
        }

        public static void setFacesContext(FacesContext context) {
            FacesContext.setCurrentInstance(context);
        }

        @Override
        public ExternalContext getExternalContext() {
            return new CustomExternalContext(request, response);
        }

        @Override
        public boolean isProjectStage(ProjectStage stage) {
            // TODO implement properly
            return stage == ProjectStage.Development;
        }
    }

    private static class CustomExternalContext extends FakeExternalContext {

        private HttpServletRequest request;
        private HttpServletResponse response;
        private ServletContext servletContext;

        public CustomExternalContext(HttpServletRequest request, HttpServletResponse response) {
            super();
            this.request = request;
            this.response = response;
            this.servletContext = request.getServletContext();
        }

        @Override
        public String getRequestPathInfo() {
            return request.getPathInfo();
        }

        @Override
        public String getRequestServletPath() {
            return request.getServletPath();
        }

        @Override
        public Map<String, String> getRequestParameterMap() {
            return new RequestParameterMap(request);
        }

        @Override
        public void setResponseStatus(int statusCode) {
            response.setStatus(statusCode);
        }

        @Override
        public Map<String, String> getRequestHeaderMap() {
            return Collections.unmodifiableMap(new RequestHeaderMap((HttpServletRequest) request));
        }

        @Override
        public Object getRequest() {
            return request;
        }

        @Override
        public Map<String, Object> getApplicationMap() {
            return new ApplicationMap(servletContext);
        }

    }
}
