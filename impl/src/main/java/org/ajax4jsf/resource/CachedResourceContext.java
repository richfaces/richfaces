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

import org.ajax4jsf.webapp.CacheContent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:56:59 $
 */
public class CachedResourceContext extends ResourceContext {
    private CacheContent content;
    private ResourceContext parent;

    /**
     * @param parent
     */
    public CachedResourceContext(ResourceContext parent) {
        this.setCacheEnabled(true);
        this.parent = parent;
        this.content = new CacheContent();
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#getOutputStream()
     */
    public OutputStream getOutputStream() throws IOException {
        return content.getOutputStream();
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#getPathInfo()
     */
    public String getPathInfo() {
        return this.parent.getPathInfo();
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#getQueryString()
     */
    public String getQueryString() {
        return this.parent.getQueryString();
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#getRequestParameter(java.lang.String)
     */
    @Override
    public String getRequestParameter(String dataParameter) {
        return this.parent.getRequestParameter(dataParameter);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(String path) {
        return this.parent.getResourceAsStream(path);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#getSessionAttribute(java.lang.String)
     */
    @Override
    public Object getSessionAttribute(String name) {
        return this.parent.getSessionAttribute(name);
    }

    /**
     * @param name
     * @return
     * @see org.ajax4jsf.resource.ResourceContext#getContextAttribute(java.lang.String)
     */
    @Override
    public Object getContextAttribute(String name) {
        return parent.getContextAttribute(name);
    }

    /**
     * @param path
     * @return
     * @throws MalformedURLException
     * @see org.ajax4jsf.resource.ResourceContext#getResource(java.lang.String)
     */
    @Override
    public URL getResource(String path) throws MalformedURLException {
        return parent.getResource(path);
    }

    /**
     * @param path
     * @return
     * @see org.ajax4jsf.resource.ResourceContext#getResourcePaths(java.lang.String)
     */
    @Override
    public Set<String> getResourcePaths(String path) {
        return parent.getResourcePaths(path);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#setDateHeader(java.lang.String, long)
     */
    @Override
    public void setDateHeader(String name, long value) {
        this.content.setDateHeader(name, value);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#setHeader(java.lang.String, java.lang.String)
     */
    @Override
    public void setHeader(String name, String value) {
        this.content.setHeader(name, value);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#setIntHeader(java.lang.String, int)
     */
    @Override
    public void setIntHeader(String name, int value) {
        this.content.setIntHeader(name, value);
    }

    /**
     * @return Returns the content.
     */
    public CacheContent getContent() {
        return this.content;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceContext#getWriter()
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        return content.getWriter();
    }

    @Override
    public void setContentType(String contentType) {
        this.content.setContentType(contentType);
    }

    @Override
    public void setContentLength(int contentLength) {
        this.content.setContentLength(contentLength);
    }

    @Override
    public Object getResourceData() {
        return parent.getResourceData();
    }

    @Override
    public void setResourceData(Object codec) {
        parent.setResourceData(codec);
    }

    @Override
    public String getInitParameter(String name) {
        return parent.getInitParameter(name);
    }

    @Override
    public String getServletPath() {
        return parent.getServletPath();
    }

    @Override
    public void release() {
        super.release();
    }
}
