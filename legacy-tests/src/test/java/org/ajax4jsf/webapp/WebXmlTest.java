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



package org.ajax4jsf.webapp;

import java.io.InputStream;

import javax.servlet.ServletException;

import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;

import org.apache.shale.test.mock.MockServletContext;

public class WebXmlTest extends AbstractAjax4JsfTestCase {
    MockServletContext context;

    public WebXmlTest(String arg0) {
        super(arg0);
    }

    public void setUp() throws Exception {
        super.setUp();
        context = new MockServletContext() {

            /*
             *  (non-Javadoc)
             * @see org.apache.shale.test.mock.MockServletContext#getResourceAsStream(java.lang.String)
             */
            public InputStream getResourceAsStream(String uri) {
                if (uri.equals(WebXml.WEB_XML)) {
                    return this.getClass().getResourceAsStream("/WEB-INF/complex-web.xml");
                } else {
                    return super.getResourceAsStream(uri);
                }
            }
        };
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     * Test method for 'org.ajax4jsf.webapp.WebXml.reset()'
     */
    public void testReset() {}

    /*
     * Test method for 'org.ajax4jsf.webapp.WebXml.init(ServletContext, String)'
     */
    public void testInit() throws ServletException {
        WebXml webXml = new WebXml();

        webXml.init(context, "ajax4jsf");
        assertFalse(webXml.isPrefixMapping());
        assertNull(webXml.getFacesFilterPrefix());
        assertNotNull(webXml.getFacesFilterSuffix());
        assertEquals(".jsf", webXml.getFacesFilterSuffix());
    }

    /*
     * Test method for 'org.ajax4jsf.webapp.WebXml.getInstance()'
     */
    public void testGetInstance() {}

    public void testDefaultResourcePrefix() throws Exception {
        WebXml webXml = new WebXml();

        webXml.init(context, "ajax4jsf");
        assertEquals('/' + WebXml.RESOURCE_URI_PREFIX_VERSIONED, webXml.getResourcePrefix());
        assertEquals('/' + WebXml.GLOBAL_RESOURCE_URI_PREFIX_VERSIONED, webXml.getGlobalResourcePrefix());
        assertEquals('/' + WebXml.SESSION_RESOURCE_URI_PREFIX_VERSIONED, webXml.getSessionResourcePrefix());
    }

    public void testOverrideResourcePrefix() throws Exception {
        context.addInitParameter(WebXml.RESOURCE_URI_PREFIX_PARAM, "rich-rsrc/");

        WebXml webXml = new WebXml();

        webXml.init(context, "ajax4jsf");
        assertEquals("/rich-rsrc/", webXml.getResourcePrefix());
        assertEquals(webXml.getResourcePrefix(), webXml.getGlobalResourcePrefix());
        assertEquals(webXml.getResourcePrefix(), webXml.getSessionResourcePrefix());
    }

    public void testCustomizedResourcePrefixes() throws Exception {
        context.addInitParameter(WebXml.SESSION_RESOURCE_URI_PREFIX_PARAM, "sess/");
        context.addInitParameter(WebXml.GLOBAL_RESOURCE_URI_PREFIX_PARAM, "glb/");

        WebXml webXml = new WebXml();

        webXml.init(context, "ajax4jsf");
        assertTrue(webXml.getResourcePrefix().startsWith("/a4j/"));
        assertTrue(webXml.getGlobalResourcePrefix().startsWith("/glb/"));
        assertTrue(webXml.getSessionResourcePrefix().startsWith("/sess/"));
    }

    public void testOverrideGlobalResourcePrefix() throws Exception {
        context.addInitParameter(WebXml.RESOURCE_URI_PREFIX_PARAM, "j4a/");
        context.addInitParameter(WebXml.GLOBAL_RESOURCE_URI_PREFIX_PARAM, "glb/");

        WebXml webXml = new WebXml();

        webXml.init(context, "ajax4jsf");
        assertTrue(webXml.getResourcePrefix().startsWith("/j4a/"));
        assertTrue(webXml.getGlobalResourcePrefix().startsWith("/glb/"));
        assertEquals(webXml.getResourcePrefix(), webXml.getSessionResourcePrefix());
    }

    public void testOverrideSessionResourcePrefix() throws Exception {
        context.addInitParameter(WebXml.RESOURCE_URI_PREFIX_PARAM, "j4a/");
        context.addInitParameter(WebXml.SESSION_RESOURCE_URI_PREFIX_PARAM, "ssn/");

        WebXml webXml = new WebXml();

        webXml.init(context, "ajax4jsf");
        assertTrue(webXml.getResourcePrefix().startsWith("/j4a/"));
        assertTrue(webXml.getSessionResourcePrefix().startsWith("/ssn/"));
        assertEquals(webXml.getResourcePrefix(), webXml.getGlobalResourcePrefix());
    }

    public void testOverrideAll() throws Exception {
        context.addInitParameter(WebXml.RESOURCE_URI_PREFIX_PARAM, "j4a/");
        context.addInitParameter(WebXml.GLOBAL_RESOURCE_URI_PREFIX_PARAM, "glb/");
        context.addInitParameter(WebXml.SESSION_RESOURCE_URI_PREFIX_PARAM, "ssn/");

        WebXml webXml = new WebXml();

        webXml.init(context, "ajax4jsf");
        assertTrue(webXml.getResourcePrefix().startsWith("/j4a/"));
        assertTrue(webXml.getSessionResourcePrefix().startsWith("/ssn/"));
        assertTrue(webXml.getGlobalResourcePrefix().startsWith("/glb/"));
    }

    /*
     * Test method for 'org.ajax4jsf.webapp.WebXml.getFacesResourceURL(FacesContext, String)'
     */
    public void testGetFacesResourceURL() throws ServletException {
        WebXml webXml = new WebXml();

        webXml.init(context, "ajax4jsf");

        String resourceURL = webXml.getFacesResourceURL(facesContext, "foo.Bar", false);

        System.out.println(resourceURL);
        assertEquals("/testContext/" + WebXml.SESSION_RESOURCE_URI_PREFIX_VERSIONED + "foo.Bar.jsf", resourceURL);
        resourceURL = webXml.getFacesResourceURL(facesContext, "rich.Tree", true);
        System.out.println(resourceURL);
        assertEquals("/testContext/" + WebXml.GLOBAL_RESOURCE_URI_PREFIX_VERSIONED + "rich.Tree.jsf", resourceURL);
    }

    public void testOverridenLegacyGetFacesResourceURL() throws ServletException {
        context.addInitParameter(WebXml.RESOURCE_URI_PREFIX_PARAM, "j4a/");

        WebXml webXml = new WebXml();

        webXml.init(context, "ajax4jsf");

        String resourceURL = webXml.getFacesResourceURL(facesContext, "foo.Bar", false);

        assertEquals("/testContext/j4a/foo.Bar.jsf", resourceURL);
        resourceURL = webXml.getFacesResourceURL(facesContext, "rich.Tree", true);
        assertEquals("/testContext/j4a/rich.Tree.jsf", resourceURL);
    }

    public void testOverridenGetFacesResourceURL() throws ServletException {
        context.addInitParameter(WebXml.GLOBAL_RESOURCE_URI_PREFIX_PARAM, "glb/");
        context.addInitParameter(WebXml.SESSION_RESOURCE_URI_PREFIX_PARAM, "ssn/");

        WebXml webXml = new WebXml();

        webXml.init(context, "ajax4jsf");

        String resourceURL = webXml.getFacesResourceURL(facesContext, "foo.Bar", false);

        assertEquals("/testContext/ssn/foo.Bar.jsf", resourceURL);
        resourceURL = webXml.getFacesResourceURL(facesContext, "rich.Tree", true);
        assertEquals("/testContext/glb/rich.Tree.jsf", resourceURL);
    }

    /*
     * Test method for 'org.ajax4jsf.webapp.WebXml.getFacesResourceKey(HttpServletRequest)'
     */
    public void testGetFacesResourceKey() {}

    /*
     * Test method for 'org.ajax4jsf.webapp.WebXml.isFacesRequest(HttpServletRequest)'
     */
    public void testIsFacesRequest() {}

    /*
     * Test method for 'org.ajax4jsf.webapp.WebXml.setFilterName(String)'
     */
    public void testSetFilterName() {}
}
