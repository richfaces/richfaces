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

import java.util.List;

import junit.framework.TestCase;

import org.ajax4jsf.config.WebappHandler.ServletMappingHandler;
import org.xml.sax.ContentHandler;

/**
 * @author asmirnov
 *
 */
public class WebappHandlerTest extends TestCase {
    private static final char[] FACES_SERVLET = "Faces Servlet".toCharArray();
    private static final char[] FACES_SERVLET_CLASS = "javax.faces.webapp.FacesServlet".toCharArray();
    private static final String SERVLET = "servlet";
    private static final String SERVLET_CLASS = "servlet-class";
    private static final String SERVLET_MAPPING = "servlet-mapping";
    private static final String SERVLET_NAME = "servlet-name";
    private static final String WEBAPP = "web-app";
    private MockXmlReader reader;

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        reader = new MockXmlReader();
    }

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testServletElement() throws Exception {
        WebappHandler handler = new WebappHandler(reader);

        reader.setContentHandler(handler);
        handler.startElement(null, WEBAPP, WEBAPP, null);
        assertEquals(handler, reader.getContentHandler());
        handler.startElement(null, SERVLET, SERVLET, null);

        ContentHandler servletHandler = reader.getContentHandler();

        assertSame(WebappHandler.ServletHandler.class, reader.getContentHandler().getClass());
        servletHandler.startElement(null, SERVLET_NAME, SERVLET_NAME, null);
        assertSame(StringContentHandler.class, reader.getContentHandler().getClass());

        ContentHandler servletNameHandler = reader.getContentHandler();

        servletNameHandler.characters(FACES_SERVLET, 0, FACES_SERVLET.length);
        servletNameHandler.endElement(null, SERVLET_NAME, SERVLET_NAME);
        assertSame(servletHandler, reader.getContentHandler());
        servletHandler.startElement(null, SERVLET_CLASS, SERVLET_CLASS, null);
        assertSame(StringContentHandler.class, reader.getContentHandler().getClass());

        ContentHandler servletClassHandler = reader.getContentHandler();

        servletClassHandler.characters(FACES_SERVLET_CLASS, 0, FACES_SERVLET_CLASS.length);
        servletClassHandler.endElement(null, SERVLET_CLASS, SERVLET_CLASS);
        assertSame(servletHandler, reader.getContentHandler());
        servletHandler.endElement(null, SERVLET, SERVLET);
        assertEquals(handler, reader.getContentHandler());
        handler.endElement(null, WEBAPP, WEBAPP);
        handler.endDocument();

        List<ServletBean> servlets = handler.getServlets();

        assertEquals(1, servlets.size());
        assertEquals(0, servlets.indexOf(new ServletBean("Faces Servlet", null)));
    }

    public void testMappingElement() throws Exception {
        WebappHandler handler = new WebappHandler(reader);

        reader.setContentHandler(handler);
        handler.startElement(null, WEBAPP, WEBAPP, null);
        assertEquals(handler, reader.getContentHandler());
        handler.startElement(null, SERVLET_MAPPING, SERVLET_MAPPING, null);

        ContentHandler servletHandler = reader.getContentHandler();

        assertSame(ServletMappingHandler.class, reader.getContentHandler().getClass());
        servletHandler.startElement(null, SERVLET_NAME, SERVLET_NAME, null);
        assertSame(StringContentHandler.class, reader.getContentHandler().getClass());
    }
}
