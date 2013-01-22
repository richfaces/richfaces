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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.ajax4jsf.resource.FacesResourceContext;

import org.apache.shale.test.mock.MockHttpServletResponse;

import com.gargoylesoftware.htmlunit.KeyValuePair;

/**
 * @author Nick - mailto:nbelaevski@exadel.com
 * created 03.04.2007
 *
 */
public class MockFacesResourceContext extends FacesResourceContext {

    // used to encode headers properly
    private MockHttpServletResponse mockResponse = new MockHttpServletResponse();
    private Set headerSet = new LinkedHashSet();
    private Integer contentLength;
    private String contentType;

    public MockFacesResourceContext(FacesContext facesContext) {
        super(facesContext);
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public int getContentLength() {
        if (contentLength == null) {
            throw new IllegalStateException("Content length hasn't been set yet!");
        }

        return contentLength.intValue();
    }

    public void setDateHeader(String name, long value) {
        headerSet.add(name);
        mockResponse.setDateHeader(name, value);
    }

    public void setHeader(String name, String value) {
        headerSet.add(name);
        mockResponse.setHeader(name, value);
    }

    public void setIntHeader(String name, int value) {
        headerSet.add(name);
        mockResponse.setIntHeader(name, value);
    }

    public List getHeaders() {
        List headers = new ArrayList();

        for (Iterator iterator = headerSet.iterator(); iterator.hasNext(); ) {
            String headerName = (String) iterator.next();
            String headerValue = mockResponse.getHeader(headerName);

            if (headerValue != null) {
                headers.add(new KeyValuePair(headerName, headerValue));
            }
        }

        return headers;
    }
}
