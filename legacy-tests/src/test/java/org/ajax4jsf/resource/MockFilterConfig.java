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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * @author shura
 *
 */
public class MockFilterConfig implements FilterConfig {
    private Map initParameters = new HashMap();
    private ServletContext servletContext;

    /**
     * @param servletContext
     */
    public MockFilterConfig(ServletContext servletContext) {
        super();
        this.servletContext = servletContext;
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.FilterConfig#getFilterName()
     */
    public String getFilterName() {

        // TODO Auto-generated method stub
        return "A4J";
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.FilterConfig#getInitParameter(java.lang.String)
     */
    public String getInitParameter(String arg0) {

        // TODO Auto-generated method stub
        return (String) initParameters.get(arg0);
    }

    public void setInitParameter(String name, String value) {
        initParameters.put(name, value);
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.FilterConfig#getInitParameterNames()
     */
    public Enumeration getInitParameterNames() {
        final Iterator parametersIterator = initParameters.keySet().iterator();

        return new Enumeration() {
            public boolean hasMoreElements() {

                // TODO Auto-generated method stub
                return parametersIterator.hasNext();
            }
            public Object nextElement() {

                // TODO Auto-generated method stub
                return parametersIterator.next();
            }
        };
    }

    /*
     *  (non-Javadoc)
     * @see javax.servlet.FilterConfig#getServletContext()
     */
    public ServletContext getServletContext() {

        // TODO Auto-generated method stub
        return servletContext;
    }
}
