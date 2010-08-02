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



package org.ajax4jsf.tests;

import java.io.IOException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.faces.FacesException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.EnumerationUtils;
import org.apache.shale.test.mock.MockExternalContext12;

/**
 * @author Siarhej Chalipau
 *
 */
public class MockExternalContext extends MockExternalContext12 {
    protected final static Comparator CASE_INSENSITIVE_COMPARATOR = new Comparator() {
        public int compare(Object arg0, Object arg1) {
            String s0 = (String) arg0;
            String s1 = (String) arg1;

            return s0.toUpperCase().compareTo(s1.toUpperCase());
        }
    };
    private Map requestHeaderMap = null;
    private Map requestHeaderValuesMap = null;
    private Iterator requestParameterNamesIterator = null;

    /*
     * Realizes methods unimplemented by org.apache.shale.test.mock.MockExternalContext operations.
     *
     */
    private Map requestParameterValuesMap = null;
    private Set resourcePathsSet = null;

    public MockExternalContext(org.apache.shale.test.mock.MockExternalContext baseContext) {
        super((ServletContext) baseContext.getContext(), (HttpServletRequest) baseContext.getRequest(),
              (HttpServletResponse) baseContext.getResponse());
    }

    public Map getRequestParameterValuesMap() {
        if (null == requestParameterValuesMap) {
            requestParameterValuesMap = new HashMap();

            HttpServletRequest request = (HttpServletRequest) getRequest();

            for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
                String name = (String) e.nextElement();

                requestParameterValuesMap.put(name, request.getParameterValues(name));
            }
        }

        return Collections.unmodifiableMap(requestParameterValuesMap);
    }

    public void dispatch(String requestURI) throws IOException, FacesException {

        // TODO hans, should be implemented
        super.dispatch(requestURI);
    }

    public Map getRequestHeaderMap() {
        if (null == requestHeaderMap) {
            requestHeaderMap = new TreeMap(CASE_INSENSITIVE_COMPARATOR);

            HttpServletRequest request = (HttpServletRequest) getRequest();

            for (Enumeration e = request.getHeaderNames(); e.hasMoreElements(); ) {
                String name = (String) e.nextElement();

                requestHeaderMap.put(name, request.getHeader(name));
            }
        }

        return Collections.unmodifiableMap(requestHeaderMap);
    }

    public Map getRequestHeaderValuesMap() {
        if (null == requestHeaderValuesMap) {
            requestHeaderValuesMap = new TreeMap(CASE_INSENSITIVE_COMPARATOR);

            HttpServletRequest request = (HttpServletRequest) getRequest();

            for (Enumeration e = request.getHeaderNames(); e.hasMoreElements(); ) {
                String name = (String) e.nextElement();

                requestHeaderValuesMap.put(name, EnumerationUtils.toList(request.getHeaders(name)).toArray());
            }
        }

        return Collections.unmodifiableMap(requestHeaderValuesMap);
    }

    public Iterator getRequestParameterNames() {
        if (null == requestParameterNamesIterator) {
            requestParameterNamesIterator = getRequestParameterValuesMap().keySet().iterator();
        }

        return requestParameterNamesIterator;
    }

    /**
     * <p>Add the specified request parameter for this request.</p>
     *
     * @param key Parameter name
     * @param value Parameter value
     */
    public void addRequestParameterMap(String key, String value) {
        super.addRequestParameterMap(key, value);

        String[] currentValue = (String[]) getRequestParameterValuesMap().get(key);

        if (null == currentValue) {
            requestParameterValuesMap.put(key, new String[] {value});
        } else {
            String[] newArray = new String[currentValue.length + 1];

            System.arraycopy(currentValue, 0, newArray, 0, currentValue.length);
            newArray[currentValue.length] = value;
            requestParameterValuesMap.put(key, newArray);
        }
    }

    public Set getResourcePaths(String path) {

        // TODO hans, should be implemented
        if (null == resourcePathsSet) {
            resourcePathsSet = new HashSet();
        }

        return resourcePathsSet;
    }

    public void redirect(String requestURI) throws IOException {

        // TODO hans, should be implemented
        super.redirect(requestURI);
    }
}
