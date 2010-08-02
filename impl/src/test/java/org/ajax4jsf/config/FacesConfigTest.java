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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

import javax.faces.webapp.FacesServlet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

/**
 * @author asmirnov
 *
 */
public class FacesConfigTest extends TestCase {

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
     * Test method for {@link org.jboss.portletbridge.util.FacesConfig#parse(javax.portlet.PortletContext)}.
     */
    public void testParsePortletContext() {
        FacesConfig config = new FacesConfig();
        MockServletContext context = new MockServletContext() {
            @Override
            public InputStream getResourceAsStream(String path) {
                return this.getClass().getResourceAsStream(path);
            }
            @Override
            public String getInitParameter(String name) {
                if (FacesServlet.CONFIG_FILES_ATTR.equals(name)) {
                    return "/WEB-INF/a-faces-config.xml ,/WEB-INF/b-faces-config.xml";
                }

                return super.getInitParameter(name);
            }
        };

        config.parse(context);
        assertEquals(6, config.getExcludedAttributes().size());
    }

    /**
     * Test method for {@link org.jboss.portletbridge.util.FacesConfig#parseDefault(javax.portlet.PortletContext)}.
     */
    public void testParseDefault() throws Exception {
        FacesConfig config = new FacesConfig();
        MockServletContext context = new MockServletContext() {
            @Override
            public InputStream getResourceAsStream(String path) {
                return this.getClass().getResourceAsStream(path);
            }
            @Override
            public String getInitParameter(String name) {
                if (FacesServlet.CONFIG_FILES_ATTR.equals(name)) {
                    return "/WEB-INF/a-faces-config.xml ,/WEB-INF/b-faces-config.xml";
                }

                return super.getInitParameter(name);
            }
        };

        config.parseDefault(context);
        assertEquals(2, config.getExcludedAttributes().size());
    }

    /**
     * Test method for {@link org.jboss.portletbridge.util.FacesConfig#parseOptional(javax.portlet.PortletContext)}.
     */
    public void testParseOptional() throws Exception {
        FacesConfig config = new FacesConfig();
        MockServletContext context = new MockServletContext() {
            @Override
            public InputStream getResourceAsStream(String path) {
                return this.getClass().getResourceAsStream(path);
            }
            @Override
            public String getInitParameter(String name) {
                if (FacesServlet.CONFIG_FILES_ATTR.equals(name)) {
                    return "/WEB-INF/a-faces-config.xml ,/WEB-INF/b-faces-config.xml";
                }

                return super.getInitParameter(name);
            }
        };

        config.parseOptional(context);
        assertEquals(4, config.getExcludedAttributes().size());
    }

    /**
     * Test method for {@link org.jboss.portletbridge.util.FacesConfig#parseClasspath(javax.portlet.PortletContext)}.
     * @throws Exception
     */
    public void testParseClasspath() throws Exception {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            ClassLoader classLoaderWrapper = new ClassLoader(classLoader) {
                @Override
                public Enumeration<URL> getResources(String name) throws IOException {
                    if ("META-INF/faces-config.xml".equals(name)) {
                        Vector<URL> configs = new Vector<URL>(2);

                        configs.add(getResource("WEB-INF/a-faces-config.xml"));
                        configs.add(getResource("WEB-INF/b-faces-config.xml"));

                        return configs.elements();
                    }

                    return super.getResources(name);
                }
            };

            Thread.currentThread().setContextClassLoader(classLoaderWrapper);

            FacesConfig config = new FacesConfig();
            MockServletContext context = new MockServletContext();

            config.parseClasspath(context);
            assertEquals(4, config.getExcludedAttributes().size());
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    /**
     * Test method for {@link org.jboss.portletbridge.util.FacesConfig#parse(java.io.InputStream)}.
     * @throws ParsingException
     */
    public void testParseInputStream() throws Exception {
        InputStream facesConfigResource = this.getClass().getResourceAsStream("/WEB-INF/faces-config.xml");
        FacesConfig config = new FacesConfig();

        config.parse(facesConfigResource);
        assertEquals(2, config.getExcludedAttributes().size());
        assertTrue(config.getExcludedAttributes().contains("foo.bar"));
        assertTrue(config.getExcludedAttributes().contains("foo.baz.*"));
    }

    /**
     * Test method for {@link org.jboss.portletbridge.util.FacesConfig#getParser()}.
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void testGetParser() throws Exception {
        FacesConfig config = new FacesConfig();
        SAXParser parser = config.getParser();

        assertNotSame(parser, config.getParser());
    }
}
