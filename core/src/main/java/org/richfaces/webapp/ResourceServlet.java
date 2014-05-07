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
package org.richfaces.webapp;

import java.io.IOException;

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
import org.richfaces.resource.ResourceHandlerImpl;

/**
 * <p>
 * Servlet which serves mapped resources that makes sure that resources can address relative resource links.
 * </p>
 *
 * <p>
 * ResourceServlet use prefix mapping, i.e. make sure it is not necessary to use suffixes or query parameters to address resources.
 * </p>
 *
 * <p>
 * This servlet is used together with default FacesServlet registered by application.
 * </p>
 *
 * <p>
 * For security reasons, the servlet handles just requests for JSF resources (context path starting with /javax.faces.resource/).
 * </p>
 */
public class ResourceServlet implements Servlet {

    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();

    private static final String JAVAX_FACES_RESOURCE_IDENTIFIER = "/javax.faces.resource/";

    public static final String RESOURCE_SERVLET_REQUEST_FLAG = ResourceServlet.class.getName();

    private ServletConfig servletConfig;
    private FacesServlet facesServlet;

    /*
     * (non-Javadoc)
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        servletConfig = config;

        facesServlet = new FacesServlet();
        facesServlet.init(config);
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Servlet#getServletConfig()
     */
    @Override
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return (this.getClass().getName());
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Servlet#destroy()
     */
    @Override
    public void destroy() {
        facesServlet.destroy();
        facesServlet = null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Servlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
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
        if (handleRequestByResourceServlet(request)) {
            request.setAttribute(RESOURCE_SERVLET_REQUEST_FLAG, Boolean.TRUE);
            facesServlet.service(request, response);
        } else {
            sendResourceNotFound(response);
        }
    }

    private boolean handleRequestByResourceServlet(HttpServletRequest request) {
        String resourcePath = getResourcePathFromRequest(request);

        if (resourcePath == null) {
            LOGGER.debug("ResourceServlet detected request which is not JSF resource request: " + request.getPathInfo());
            return false;
        }

        // when resource path found in the request path, then we can handle the request
        return true;
    }

    private static String getResourcePathFromRequest(HttpServletRequest request) {
        String resourceName = decodeResourceURL(request);

        if (resourceName != null) {
            if (resourceName.startsWith(JAVAX_FACES_RESOURCE_IDENTIFIER)) {
                return resourceName.substring(JAVAX_FACES_RESOURCE_IDENTIFIER.length());
            }
            if (resourceName.startsWith(ResourceHandlerImpl.RICHFACES_RESOURCE_IDENTIFIER)) {
                return resourceName;
            }
        }
        return null;
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
}
