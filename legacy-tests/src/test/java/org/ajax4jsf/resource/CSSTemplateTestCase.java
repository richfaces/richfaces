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

import org.ajax4jsf.renderkit.compiler.TemplateContext;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import org.ajax4jsf.webapp.WebXml;

import org.apache.shale.test.mock.MockPrintWriter;

/**
 * @author shura (latest modification by $Author: ishabalov $)
 * @version $Revision: 1.1.2.4 $ $Date: 2007/02/20 20:58:10 $
 *
 */
public class CSSTemplateTestCase extends AbstractAjax4JsfTestCase {
    private static boolean methodCalled = false;
    private static boolean methodParamOk = false;

    /**
     * Constructor for CSSTemplateTestCase.
     * @param name
     */
    public CSSTemplateTestCase(String name) {
        super(name);
    }

    /*
     * @see VcpJsfTestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
        CSSTemplateTestCase.methodCalled = false;
        CSSTemplateTestCase.methodParamOk = false;
    }

    /*
     * @see VcpJsfTestCase#tearDown()
     */
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCallWithContext() throws Exception {
        String templatePath = "org/ajax4jsf/resource/call.xml";
        TemplateCSSResource resource = new TemplateCSSResource(templatePath);
        ResourceContext context = new FacesResourceContext(facesContext);

        try {
            resource.send(context);
        } catch (IOException e) {
            assertTrue("error send style", false);
        }

        assertTrue(methodCalled);
    }

    public static void contextCallWithParam(TemplateContext context, String param) {
        methodCalled = true;
        methodParamOk = "Test".equals(param);
    }

    public void testCallWithContextAndParam() throws Exception {
        String templatePath = "org/ajax4jsf/resource/callWithParam.xml";
        TemplateCSSResource resource = new TemplateCSSResource(templatePath);
        ResourceContext context = new FacesResourceContext(facesContext);

        try {
            resource.send(context);
        } catch (IOException e) {
            assertTrue("error send style", false);
        }

        assertTrue(methodCalled);
    }

    public static void contextCall(TemplateContext context) {
        methodCalled = true;
    }

    /*
     * Test method for 'org.ajax4jsf.resource.TemplateCSSResource.send(ResourceContext)'
     */
    public void testSend() throws IOException {
        String templatePath = "org/ajax4jsf/resource/styles.xml";
        TemplateCSSResource resource = new TemplateCSSResource(templatePath);
        ResourceContext context = new FacesResourceContext(facesContext);

        try {
            resource.send(context);
        } catch (IOException e) {
            assertTrue("error send style", false);
        }

        MockPrintWriter printWriter = (MockPrintWriter) response.getWriter();
        String content = String.valueOf(printWriter.content());

        System.out.println(content);
    }

    /*
     * Test method for 'org.ajax4jsf.resource.TemplateCSSResource.TemplateCSSResource(String)'
     */
    public void testTemplateCSSResource() throws IOException {
        String templatePath = "org/ajax4jsf/resource/styles.xml";
        TemplateCSSResource resource = new TemplateCSSResource(templatePath);

        assertEquals(templatePath, resource.getKey());

        InternetResource resource2 = InternetResourceBuilder.getInstance().createResource(this, resource.getKey());
        ResourceContext ctx = new FacesResourceContext(this.facesContext);
        InputStream stream = resource.getResourceAsStream(ctx);
        byte[] resourceContent = new byte[8196];
        int length = stream.read(resourceContent);
        InputStream stream2 = resource2.getResourceAsStream(ctx);
        byte[] resource2Content = new byte[8196];
        int length2 = stream2.read(resource2Content);

        assertEquals(length, length2);
    }

    /*
     * Test method for 'org.ajax4jsf.resource.InternetResourceBase.isCacheable()'
     */
    public void testIsCacheable() {}

    /*
     * Test method for 'org.ajax4jsf.resource.InternetResourceBase.getContentType()'
     */
    public void testGetContentType() {
        String templatePath = "org/ajax4jsf/resource/styles.xml";
        TemplateCSSResource resource = new TemplateCSSResource(templatePath);

        assertEquals("text/css", resource.getContentType(null));
    }

    /*
     * Test method for 'org.ajax4jsf.resource.InternetResourceBase.getUri(FacesContext, Object)'
     */
    public void testGetUri() {
        String templatePath = "org/ajax4jsf/resource/styles.xml";
        TemplateCSSResource resource = new TemplateCSSResource(templatePath);

//      System.out.println(resource.getUri(facesContext,null));
//      System.out.println(expextedUri);
        String uri = resource.getUri(facesContext, null);

        assertTrue(uri.startsWith(request.getContextPath() + "/" + WebXml.RESOURCE_URI_PREFIX));
        assertTrue(uri.startsWith(request.getContextPath() + "/" + WebXml.SESSION_RESOURCE_URI_PREFIX_VERSIONED
                                  + resource.getKey()));
    }

    /*
     * Test method for 'org.ajax4jsf.resource.InternetResourceBase.getResourceAsStream(ResourceContext)'
     */
    public void testGetResourceAsStream() {}

    /*
     * Test method for 'org.ajax4jsf.resource.InternetResourceBase.sendHeaders(ResourceContext)'
     */
    public void testSendHeaders() {
        String templatePath = "org/ajax4jsf/resource/styles.xml";
        TemplateCSSResource resource = new TemplateCSSResource(templatePath);
        ResourceContext context = new FacesResourceContext(facesContext);

        resource.sendHeaders(context);

        String header = response.getContentType();

        assertNotNull(header);
        assertTrue(header.startsWith("text/css"));
    }
}
