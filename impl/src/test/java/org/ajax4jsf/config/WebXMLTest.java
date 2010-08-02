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
import java.util.List;

import junit.framework.TestCase;

/**
 * @author asmirnov
 *
 */
public class WebXMLTest extends TestCase {

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link org.WebXMLParser.portletbridge.util.WebXML#init(java.io.InputStream, String)}.
     */
    public void testParse() throws Exception {
        WebXMLParser webXml = new WebXMLParser();
        InputStream inputStream = this.getClass().getResourceAsStream("/WEB-INF/web.xml");

        webXml.init(inputStream, "foo");
        inputStream.close();

        List<String> facesServletMappings = webXml.getFacesServletMappings();

        assertEquals(2, facesServletMappings.size());
        assertEquals("*.jsf", webXml.getFacesServletMappings().get(1));
        assertEquals("/faces/*", webXml.getFacesServletMappings().get(0));
        assertEquals(".jsf", webXml.getFacesServletSuffix());
        assertEquals("/faces", webXml.getFacesServletPrefix());
    }
}
