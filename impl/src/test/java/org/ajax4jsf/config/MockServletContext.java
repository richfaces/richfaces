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



package org.ajax4jsf.config;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author asmirnov
 *
 */
public class MockServletContext implements ServletContext {

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name) {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getAttributeNames()
     */
    public Enumeration getAttributeNames() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getContext(java.lang.String)
     */
    public ServletContext getContext(String uripath) {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getContextPath()
     */
    public String getContextPath() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
     */
    public String getInitParameter(String name) {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getInitParameterNames()
     */
    public Enumeration getInitParameterNames() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getMajorVersion()
     */
    public int getMajorVersion() {

        // TODO Auto-generated method stub
        return 0;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
     */
    public String getMimeType(String file) {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getMinorVersion()
     */
    public int getMinorVersion() {

        // TODO Auto-generated method stub
        return 0;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
     */
    public RequestDispatcher getNamedDispatcher(String name) {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
     */
    public String getRealPath(String path) {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(String path) {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getResource(java.lang.String)
     */
    public URL getResource(String path) throws MalformedURLException {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream(String path) {
        return getClass().getResourceAsStream(path);
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
     */
    public Set getResourcePaths(String path) {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getServerInfo()
     */
    public String getServerInfo() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getServlet(java.lang.String)
     */
    public Servlet getServlet(String name) throws ServletException {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getServletContextName()
     */
    public String getServletContextName() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getServletNames()
     */
    public Enumeration getServletNames() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#getServlets()
     */
    public Enumeration getServlets() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#log(java.lang.String)
     */
    public void log(String msg) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#log(java.lang.Exception, java.lang.String)
     */
    public void log(Exception exception, String msg) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#log(java.lang.String, java.lang.Throwable)
     */
    public void log(String message, Throwable throwable) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.ServletContext#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String name, Object object) {

        // TODO Auto-generated method stub
    }
}
