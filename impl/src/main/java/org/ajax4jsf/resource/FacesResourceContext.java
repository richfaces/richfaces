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

import org.richfaces.util.Util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * @author asmirnov@exadel.com (latest modification by $Author: nick_belaevski $)
 * @version $Revision: 1.1.2.2 $ $Date: 2007/01/11 16:52:14 $
 */
public class FacesResourceContext extends ResourceContext {
    private ExternalContext externalContext;
    private FacesContext facesContext;

    /**
     * @param facesContext
     */
    public FacesResourceContext(FacesContext facesContext) {
        this.facesContext = facesContext;
        this.externalContext = facesContext.getExternalContext();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.ResourceContext#setHeader(java.lang.String,
     *      java.lang.String)
     */
    public void setHeader(String name, String value) {
        externalContext.setResponseHeader(name, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.ResourceContext#setIntHeader(java.lang.String,
     *      int)
     */
    public void setIntHeader(String name, int value) {
        externalContext.setResponseHeader(name, String.valueOf(value));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.ResourceContext#setDateHeader(java.lang.String,
     *      long)
     */
    public void setDateHeader(String name, long value) {
        externalContext.setResponseHeader(name, Util.formatHttpDate(value));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.ResourceContext#getOutputStream()
     */
    public OutputStream getOutputStream() throws IOException {
        return externalContext.getResponseOutputStream();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.ResourceContext#getWriter()
     */
    public Writer getWriter() throws IOException {
        return externalContext.getResponseOutputWriter();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.ResourceContext#getQueryString()
     */
    public String getQueryString() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.ResourceContext#getPathInfo()
     */
    public String getPathInfo() {
        return externalContext.getRequestPathInfo();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.ResourceContext#getSessionAttribute(java.lang.String)
     */
    public Object getSessionAttribute(String name) {
        return externalContext.getSessionMap().get(name);
    }

    public InputStream getResourceAsStream(String path) {
        return externalContext.getResourceAsStream(path);
    }

    public String getRequestParameter(String dataParameter) {
        return (String) externalContext.getRequestParameterMap().get(dataParameter);
    }

    public void setContentType(String contentType) {
        externalContext.setResponseContentType(contentType);
    }

    @Override
    public Object getContextAttribute(String name) {
        return externalContext.getApplicationMap().get(name);
    }

    public void setContentLength(int contentLength) {
        externalContext.setResponseContentLength(contentLength);
    }

    public String getInitParameter(String name) {

        // TODO Auto-generated method stub
        return externalContext.getInitParameter(name);
    }

    /**
     * @param path
     * @return
     * @throws MalformedURLException
     * @see javax.faces.context.ExternalContext#getResource(java.lang.String)
     */
    public URL getResource(String path) throws MalformedURLException {
        return externalContext.getResource(path);
    }

    public String getServletPath() {

        // TODO Auto-generated method stub
        return externalContext.getRequestServletPath();
    }

    /**
     * @param path
     * @return
     * @see javax.faces.context.ExternalContext#getResourcePaths(java.lang.String)
     */
    public Set<String> getResourcePaths(String path) {
        return externalContext.getResourcePaths(path);
    }

    public void release() {
        super.release();
        externalContext = null;
        facesContext = null;
    }

    // added by nick 11.01.2007 - getters for contexts added
    public FacesContext getFacesContext() {
        return facesContext;
    }

    public ExternalContext getExternalContext() {
        return externalContext;
    }

    // by nick
}
