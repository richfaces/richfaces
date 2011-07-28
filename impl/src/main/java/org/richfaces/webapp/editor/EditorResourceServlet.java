package org.richfaces.webapp.editor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.cache.Cache;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.resource.ResourceHandlerImpl;
import org.richfaces.util.Util;

public class EditorResourceServlet extends HttpServlet {

    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();
    private static final String RICHFACES_RESOURCE_IDENTIFIER = EditorResourceServletContainerInitializer.EDITOR_RESOURCES_DEFAULT_MAPPING;
    private static final String PROJECT_STAGE_INIT_PARAM = "javax.faces.PROJECT_STAGE";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FacesContext facesContext = new CustomFacesContext(request, response);

        String resourcePath = ResourceHandlerImpl.getResourcePathFromRequest(facesContext);

        if (resourcePath.startsWith("org.richfaces.ckeditor/")) {
            ResourceHandler resourceHandler = new ResourceHandlerImpl(facesContext.getApplication().getResourceHandler());
            resourceHandler.handleResourceRequest(facesContext);
        } else {
            sendResourceNotFound(facesContext);
        }
    }

    private static void sendResourceNotFound(FacesContext context) {
        context.getExternalContext().setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private class CustomFacesContext extends FakeFacesContext {

        private HttpServletRequest request;
        private HttpServletResponse response;
        private ServletContext servletContext;

        private ResourceHandler resourceHandler;
        private HashMap<Object, Object> attributes;
        private ProjectStage projectStage;

        public CustomFacesContext(HttpServletRequest request, HttpServletResponse response) {
            super();
            this.request = request;
            this.response = response;
            this.servletContext = request.getServletContext();
            FacesContext.setCurrentInstance(this);
            this.resourceHandler = new com.sun.faces.application.resource.ResourceHandlerImpl();
        }

        @Override
        public ExternalContext getExternalContext() {
            return new CustomExternalContext(request, response);
        }

        @Override
        public boolean isProjectStage(ProjectStage stage) {
            return stage == getProjectStageFromWebXml();
        }

        private ProjectStage getProjectStageFromWebXml() {
            if (projectStage == null) {
                String projectStageString = servletContext.getInitParameter(PROJECT_STAGE_INIT_PARAM);
                projectStage = ProjectStage.valueOf(projectStageString);
            }
            return projectStage;
        }

        @Override
        public Application getApplication() {
            return new CustomApplication();
        }

        private class CustomApplication extends FakeApplication {
            @Override
            public ResourceHandler getResourceHandler() {
                return resourceHandler;
            }
        }

        @Override
        public Map<Object, Object> getAttributes() {
            if (attributes == null) {
                attributes = new HashMap<Object, Object>();
            }
            return attributes;
        }
    }

    private static class CustomExternalContext extends FakeExternalContext {

        private HttpServletRequest request;
        private HttpServletResponse response;
        private ServletContext servletContext;
        private Map<String, String> fallbackContentTypeMap = null;

        public CustomExternalContext(HttpServletRequest request, HttpServletResponse response) {
            super();
            this.request = request;
            this.response = response;
            this.servletContext = request.getServletContext();

            fallbackContentTypeMap = new HashMap<String, String>(3, 1.0f);
            fallbackContentTypeMap.put("js", "text/javascript");
            fallbackContentTypeMap.put("css", "text/css");
            fallbackContentTypeMap.put("groovy", "application/x-groovy");
            fallbackContentTypeMap.put("properties", "text/plain");
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

        @Override
        public String getMimeType(String file) {
            String mimeType = servletContext.getMimeType(file);
            if (mimeType == null) {
                mimeType = getFallbackMimeType(file);
            }
            if (mimeType == null && LOGGER.isWarnEnabled()) {
                LOGGER.warn("jsf.externalcontext.no.mime.type.found");
            }
            return mimeType;
        }

        public String getFallbackMimeType(String file) {

            if (file == null || file.length() == 0) {
                return null;
            }
            int idx = file.lastIndexOf('.');
            if (idx == -1) {
                return null;
            }
            String extension = file.substring(idx + 1);
            if (extension.length() == 0) {
                return null;
            }
            return fallbackContentTypeMap.get(extension);
        }

        @Override
        public void setResponseContentType(String contentType) {
            response.setContentType(contentType);
        }

        @Override
        public void setResponseContentLength(int length) {
            response.setContentLength(length);
        }

        @Override
        public void setResponseHeader(String name, String value) {
            ((HttpServletResponse) response).setHeader(name, value);
        }

        @Override
        public OutputStream getResponseOutputStream() throws IOException {
            return response.getOutputStream();
        }

        @Override
        public Writer getResponseOutputWriter() throws IOException {
            return response.getWriter();
        }

        public Map<String, String[]> getRequestParameterValuesMap() {
            // TODO cache all maps
            return Collections.unmodifiableMap(new RequestParameterValuesMap(request));
        }

        @Override
        public Map<String, Object> getSessionMap() {
            return new SessionMap((HttpServletRequest) request, FacesContext.getCurrentInstance().getApplication()
                    .getProjectStage());
        }

    }
}
