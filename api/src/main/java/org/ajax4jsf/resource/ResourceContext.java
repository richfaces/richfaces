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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Set;

/**
 * Abstraction context class for rendering resource ( image, script, style )
 * can be work in 2 variants - for simple ServletRequest and as JSF context encapsulation.
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:56:57 $
 *
 */
public abstract class ResourceContext {
    private boolean cacheEnabled = false;
    private Object resourceData;

    // response headers

    /**
     * Delegate to {@link javax.servlet.ServletResponse} setHeader
     * @param name name of header
     * @param value new value
     */
    public abstract void setHeader(String name, String value);

    /**
     * Delegate to {@link javax.servlet.ServletResponse} setHeader
     * @param name name of header
     * @param value new value
     */
    public abstract void setIntHeader(String name, int value);

    /**
     * Delegate to {@link javax.servlet.ServletResponse} setHeader
     * @param name name of header
     * @param value new value
     */
    public abstract void setDateHeader(String name, long value);

    /**
     * @return
     * @throws IOException
     */
    public abstract OutputStream getOutputStream() throws IOException;

    /**
     * @return
     */
    @Deprecated
    public abstract String getQueryString();

    /**
     * @return
     */
    @Deprecated
    public abstract String getPathInfo();

    /**
     * @return
     */
    @Deprecated
    public abstract String getServletPath();

    /**
     * Get session attribute for given name. session not created
     * @param name attribute name
     * @return value for attribute, or null.
     */
    public abstract Object getSessionAttribute(String name);

    /**
     * Get ServletContext attribute by name.
     * @param name
     * @return value for attribute, or null.
     */
    public abstract Object getContextAttribute(String name);

    /**
     * <p>Return a <code>URL</code> for the application resource mapped to the
     * specified path, if it exists; otherwise, return <code>null</code>.</p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.ServletContext</code> method
     * <code>getResource(path)</code>.</p>
     *
     * <p><em>Portlet:</em> This must be the value returned by the
     * <code>javax.portlet.PortletContext</code> method
     * <code>getResource(path)</code>.</p>
     *
     * @param path The path to the requested resource, which must
     *  start with a slash ("/" character
     *
     * @throws MalformedURLException if the specified path
     *  is not in the correct form
     * @throws NullPointerException if <code>path</code>
     *  is <code>null</code>
     */
    public abstract URL getResource(String path) throws MalformedURLException;

    /**
     * <p>Return an <code>InputStream</code> for an application resource
     * mapped to the specified path, if it exists; otherwise, return
     * <code>null</code>.</p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.ServletContext</code> method
     * <code>getResourceAsStream(path)</code>.</p>
     *
     * <p><em>Portlet:</em> This must be the value returned by the
     * <code>javax.portlet.PortletContext</code> method
     * <code>getResourceAsStream(path)</code>.</p>
     *
     * @param path The path to the requested resource, which must
     *  start with a slash ("/" character
     *
     * @throws NullPointerException if <code>path</code>
     *  is <code>null</code>
     */
    public abstract InputStream getResourceAsStream(String path);

    /**
     * <p>Return the <code>Set</code> of resource paths for all application
     * resources whose resource path starts with the specified argument.</p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.ServletContext</code> method
     * <code>getResourcePaths(path).</code></p>
     *
     * <p><em>Portlet:</em> This must be the value returned by the
     * <code>javax.portlet.PortletContext</code> method
     * <code>getResourcePaths(path).</code></p>
     *
     * @param path Partial path used to match resources, which must
     *  start with a slash ("/") character
     *
     * @throws NullPointerException if <code>path</code>
     *  is <code>null</code>
     */
    public abstract Set<String> getResourcePaths(String path);

    /**
     * Get request parameter for given name.
     * @param dataParameter
     * @return
     */
    public abstract String getRequestParameter(String dataParameter);

    /**
     * @return Returns the cacheEnabled.
     */
    public boolean isCacheEnabled() {
        return this.cacheEnabled;
    }

    /**
     * @param cacheEnabled The cacheEnabled to set.
     */
    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    /**
     * get output writer for send response.
     * @return
     */

    // TODO method signature changed
    public abstract Writer getWriter() throws IOException;

    /**
     * Setup response content type as {@see javax.servlet.ServletResponse#setContentType(java.lang.String)}
     * @param contentType
     */
    public abstract void setContentType(String contentType);

    /**
     * Setup response content length as {@see javax.servlet.ServletResponse#setContentLength(int)}
     * @param contentLength
     */
    public abstract void setContentLength(int contentLength);

    public abstract String getInitParameter(String name);

    public Object getResourceData() {

        // TODO Auto-generated method stub
        return resourceData;
    }

    public void setResourceData(Object data) {
        resourceData = data;
    }

    /**
     * Release any data used by this context.
     * Close buffers used by cached context, release FacesContext, if exist.
     */
    public void release() {}
}
