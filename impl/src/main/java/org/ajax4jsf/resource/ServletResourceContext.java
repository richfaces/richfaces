/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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

package org.ajax4jsf.resource;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:01 $
 */
public class ServletResourceContext extends ResourceContext {
    private ServletContext context;
    private HttpServletRequest request;
    private HttpServletResponse response;

    /**
     * @param request
     * @param response
     */
    public ServletResourceContext(ServletContext context, HttpServletRequest request, HttpServletResponse response) {
        this.context = context;
        this.request = request;
        this.response = response;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#setHeader(java.lang.String, java.lang.String)
     */
    @Override
    public void setHeader(String name, String value) {
        response.setHeader(name, value);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#setIntHeader(java.lang.String, int)
     */
    public void setIntHeader(String name, int value) {
        response.setIntHeader(name, value);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#setDateHeader(java.lang.String, long)
     */
    @Override
    public void setDateHeader(String name, long value) {
        response.setDateHeader(name, value);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#getOutputStream()
     */
    @Override
    public OutputStream getOutputStream() throws IOException {

        // TODO Auto-generated method stub
        return response.getOutputStream();
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#getQueryString()
     */
    @Override
    public String getQueryString() {
        return request.getQueryString();
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#getPathInfo()
     */
    @Override
    public String getPathInfo() {
        return request.getPathInfo();
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#getSessionAttribute(java.lang.String)
     */
    @Override
    public Object getSessionAttribute(String name) {
        return request.getSession(false).getAttribute(name);
    }

    /**
     * @param name
     * @return
     * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
     */
    @Override
    public Object getContextAttribute(String name) {
        return context.getAttribute(name);
    }

    /**
     * @param path
     * @return
     * @throws MalformedURLException
     * @see javax.servlet.ServletContext#getResource(java.lang.String)
     */
    @Override
    public URL getResource(String path) throws MalformedURLException {
        return context.getResource(path);
    }

    /**
     * @param path
     * @return
     * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set getResourcePaths(String path) {
        return context.getResourcePaths(path);
    }

    @Override
    public InputStream getResourceAsStream(String path) {

        // TODO Auto-generated method stub
        return context.getResourceAsStream(path);
    }

    @Override
    public String getRequestParameter(String dataParameter) {

        // TODO Auto-generated method stub
        return request.getParameter(dataParameter);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#getWriter()
     */
    @Override
    public PrintWriter getWriter() throws IOException {

        // TODO Auto-generated method stub
        return response.getWriter();
    }

    @Override
    public void setContentType(String contentType) {
        response.setContentType(contentType);
    }

    @Override
    public void setContentLength(int contentLength) {
        response.setContentLength(contentLength);
    }

    @Override
    public String getInitParameter(String name) {

        // TODO Auto-generated method stub
        return context.getInitParameter(name);
    }

    @Override
    public String getServletPath() {

        // TODO Auto-generated method stub
        return request.getServletPath();
    }
}
