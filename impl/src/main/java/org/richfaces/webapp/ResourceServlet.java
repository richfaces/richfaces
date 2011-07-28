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
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.webapp.FacesServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

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
public class ResourceServlet implements Servlet {

    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();

    private static final String JAVAX_FACES_RESOURCE_IDENTIFIER = "/javax.faces.resource/";

    private static final Library[] LIBRARIES_TO_SERVE = new Library[] { new CKEditorLibrary() };

    private ServletConfig servletConfig;
    private FacesServlet facesServlet;

    private interface Library {
        boolean allowServerRequest(String resourcePath, HttpServletRequest request);
    }

    public void init(ServletConfig config) throws ServletException {
        servletConfig = config;

        facesServlet = new FacesServlet();
        facesServlet.init(config);
    }

    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    public String getServletInfo() {
        return (this.getClass().getName());
    }

    public void destroy() {
        facesServlet.destroy();
        facesServlet = null;
    }

    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        HttpServletRequest req;
        HttpServletResponse res;

        try {
            req = (HttpServletRequest) request;
            res = (HttpServletResponse) response;
        } catch (ClassCastException e) {
            throw new ServletException("non-HTTP request or response");
        }

        httpService(req, res);
    }

    private void httpService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (handleRequestByEditorResourceServlet(request)) {
            facesServlet.service(request, response);
        } else {
            sendResourceNotFound(response);
        }
    }

    private boolean handleRequestByEditorResourceServlet(HttpServletRequest request) {
        String resourcePath = getResourcePathFromRequest(request);

        for (Library library : LIBRARIES_TO_SERVE) {
            if (library.allowServerRequest(resourcePath, request)) {
                return true;
            }
        }

        return false;
    }

    private static String getResourcePathFromRequest(HttpServletRequest request) {
        String resourceName = decodeResourceURL(request);

        if (resourceName != null) {
            if (resourceName.startsWith(JAVAX_FACES_RESOURCE_IDENTIFIER)) {
                return resourceName.substring(JAVAX_FACES_RESOURCE_IDENTIFIER.length());
            } else {
                return null;
            }
        } else {
            LOGGER.warn("Resource key not found" + resourceName);
            return null;
        }
    }

    private static String decodeResourceURL(HttpServletRequest request) {
        String resourceName = null;
        String facesMapping = getMappingForRequest(request);

        if (facesMapping != null) {
            if (facesMapping.startsWith("/")) {

                // prefix mapping
                resourceName = request.getPathInfo();
            } else {
                String requestServletPath = request.getServletPath();

                resourceName = requestServletPath.substring(0, requestServletPath.length() - facesMapping.length());
            }
        }

        return resourceName;
    }

    private static void sendResourceNotFound(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private static String getMappingForRequest(HttpServletRequest request) {
        String servletPath = request.getServletPath();

        if (servletPath == null) {
            return null;
        }

        if (servletPath.length() == 0) {
            return "/";
        }

        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            return servletPath;
        }

        int idx = servletPath.lastIndexOf('.');

        if (idx < 0) {
            return servletPath;
        } else {
            return servletPath.substring(idx);
        }
    }

    private static class CKEditorLibrary implements Library {
        private Set<String> ALLOWED_PARAMETERS = Collections.unmodifiableSortedSet(new TreeSet<String>(Arrays.asList("t")));

        public boolean allowServerRequest(String resourcePath, HttpServletRequest request) {
            if (resourcePath.startsWith("org.richfaces.ckeditor/")) {
                Enumeration<String> parameters = request.getParameterNames();
                while (parameters.hasMoreElements()) {
                    String parameter = parameters.nextElement();
                    if (!ALLOWED_PARAMETERS.contains(parameter)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }
}
