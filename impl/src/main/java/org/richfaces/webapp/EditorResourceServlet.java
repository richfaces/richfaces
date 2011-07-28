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
package org.richfaces.webapp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.ProjectStage;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.faces.adapters.ApplicationAdapter;
import org.richfaces.faces.adapters.ApplicationMap;
import org.richfaces.faces.adapters.ExternalContextAdapter;
import org.richfaces.faces.adapters.FacesContextAdapter;
import org.richfaces.faces.adapters.RequestHeaderMap;
import org.richfaces.faces.adapters.RequestParameterMap;
import org.richfaces.faces.adapters.RequestParameterValuesMap;
import org.richfaces.faces.adapters.SessionMap;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.resource.ResourceHandlerImpl;

/**
 * <p>
 * Servlet which serves resources for rich:editor component.
 * </p>
 *
 * <p>
 * These resources are generated dynamically and some of them cannot be used with FacesServlet mappings by suffixes (e.g. .jsf).
 * Therefore this servlet provides prefix mappings and serves only selected resources (from editor resource library) to avoid
 * security problems.
 * </p>
 *
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class EditorResourceServlet extends HttpServlet {

    private static final long serialVersionUID = -6924458376293250930L;

    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();
    private static final String PROJECT_STAGE_INIT_PARAM = "javax.faces.PROJECT_STAGE";

    private static final ThreadLocal<ResourceHandler> DEFAULT_FACES_RESOURCE_HANDLER = new ThreadLocal<ResourceHandler>() {
        protected ResourceHandler initialValue() {
            return new com.sun.faces.application.resource.ResourceHandlerImpl();
        };
    };

    private static final ThreadLocal<ResourceHandler> RICHFACES_RESOURCE_HANDLER = new ThreadLocal<ResourceHandler>() {
        protected ResourceHandler initialValue() {
            return new ResourceHandlerImpl(DEFAULT_FACES_RESOURCE_HANDLER.get());
        };
    };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        FacesContext facesContext = new CustomFacesContext(request, response);

        String resourcePath = ResourceHandlerImpl.getResourcePathFromRequest(facesContext);

        if (resourcePath.startsWith("org.richfaces.ckeditor/")) {
            ResourceHandler richFacesResourceHandler = RICHFACES_RESOURCE_HANDLER.get();
            richFacesResourceHandler.handleResourceRequest(facesContext);
        } else {
            sendResourceNotFound(facesContext);
        }
    }

    private static void sendResourceNotFound(FacesContext context) {
        context.getExternalContext().setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private class CustomFacesContext extends FacesContextAdapter {

        private HttpServletRequest request;
        private HttpServletResponse response;
        private ServletContext servletContext;

        private ResourceHandler defaultHandler;
        private HashMap<Object, Object> attributes;
        private ProjectStage projectStage;

        public CustomFacesContext(HttpServletRequest request, HttpServletResponse response) {
            super();
            this.request = request;
            this.response = response;
            this.servletContext = request.getServletContext();

            FacesContext.setCurrentInstance(this);

            this.defaultHandler = DEFAULT_FACES_RESOURCE_HANDLER.get();
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

        private class CustomApplication extends ApplicationAdapter {
            @Override
            public ResourceHandler getResourceHandler() {
                return defaultHandler;
            }
        }

        @Override
        public Map<Object, Object> getAttributes() {
            if (attributes == null) {
                attributes = new HashMap<Object, Object>();
            }
            return attributes;
        }

        private class CustomExternalContext extends ExternalContextAdapter {

            private Map<String, String> fallbackContentTypeMap = null;
            private Map<String, String> requestParameterMap = null;
            private Map<String, String> requestHeaderMap = null;
            private Map<String, Object> applicationMap = null;
            private Map<String, String[]> requestParameterValuesMap = null;
            private Map<String, Object> sessionMap = null;

            public CustomExternalContext(HttpServletRequest request, HttpServletResponse response) {
                super();

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
                if (null == requestParameterMap) {
                    requestParameterMap = Collections.unmodifiableMap(new RequestParameterMap(request));
                }
                return requestParameterMap;
            }

            @Override
            public void setResponseStatus(int statusCode) {
                response.setStatus(statusCode);
            }

            @Override
            public Map<String, String> getRequestHeaderMap() {
                if (null == requestHeaderMap) {
                    requestHeaderMap = Collections.unmodifiableMap(new RequestHeaderMap((HttpServletRequest) request));
                }
                return requestHeaderMap;
            }

            @Override
            public Object getRequest() {
                return request;
            }

            @Override
            public Map<String, Object> getApplicationMap() {
                if (applicationMap == null) {
                    applicationMap = new ApplicationMap(servletContext);
                }
                return applicationMap;
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
                if (null == requestParameterValuesMap) {
                    requestParameterValuesMap = Collections.unmodifiableMap(new RequestParameterValuesMap(request));
                }
                return requestParameterValuesMap;
            }

            @Override
            public Map<String, Object> getSessionMap() {
                if (sessionMap == null) {
                    sessionMap = new SessionMap((HttpServletRequest) request, FacesContext.getCurrentInstance()
                            .getApplication().getProjectStage());
                }
                return sessionMap;
            }

        }
    }

}
