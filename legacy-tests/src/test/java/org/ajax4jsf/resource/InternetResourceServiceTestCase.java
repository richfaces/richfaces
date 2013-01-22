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

import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;

import org.apache.shale.test.mock.MockHttpServletResponse;
import org.apache.shale.test.mock.MockServletOutputStream;

/**
 * @author shura
 *
 */
public class InternetResourceServiceTestCase extends AbstractAjax4JsfTestCase {

    /**
     * @param name
     */
    public InternetResourceServiceTestCase(String name) {
        super(name);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();

        // TODO fix when setContentLength in shale-test will start function
        // https://issues.apache.org/struts/browse/SHALE-495
        this.response = new MockHttpServletResponse() {
            public int getContentLength() {
                return Integer.parseInt(getHeader("Content-Length"));
            }
            public void setContentLength(int length) {
                addIntHeader("Content-Length", length);
            }
        };
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#tearDown()
     */
    public void tearDown() throws Exception {
        super.tearDown();
        InternetResourceBuilder.setInstance(null);
    }

    /**
     * Test method for {@link org.ajax4jsf.resource.InternetResourceService#init(javax.servlet.FilterConfig)}.
     * @throws Exception
     */
    public void testInit() throws Exception {
        InternetResourceService service = new InternetResourceService();
        MockFilterConfig mockFilterConfig = new MockFilterConfig(servletContext);

        mockFilterConfig.setInitParameter(InternetResourceService.ENABLE_CACHING_PARAMETER, "true");
        service.init(mockFilterConfig);
        assertSame(servletContext, service.getServletContext());
    }

    /**
     * Test method for {@link org.ajax4jsf.resource.InternetResourceService#serviceResource(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
     * @throws IOException
     * @throws Exception
     */
    public void testServiceResource() throws Exception, IOException {
        InternetResourceService service = new InternetResourceService();
        MockFilterConfig mockFilterConfig = new MockFilterConfig(servletContext);

        mockFilterConfig.setInitParameter(InternetResourceService.ENABLE_CACHING_PARAMETER, "true");
        service.init(mockFilterConfig);

        InternetResourceBuilder builder = InternetResourceBuilder.getInstance();
        InternetResource resource = builder.createResource(this, "test.js");
        MockResourceRequest resourceRequest = new MockResourceRequest(request);
        String key = resource.getKey();

        service.serviceResource(key, resourceRequest, response);

        byte[] content = ((MockServletOutputStream) response.getOutputStream()).content();
        String strContent = new String(content);

        assertEquals(strContent, "\nfunction Test(){};");
        response.setOutputStream(null);
        response.setWriter(null);
    }

    /**
     * Test method for {@link org.ajax4jsf.resource.InternetResourceService#serviceResource(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
     * @throws IOException
     * @throws Exception
     */
    public void testCachedServiceResource() throws Exception, IOException {
        InternetResourceService service = new InternetResourceService();
        MockFilterConfig mockFilterConfig = new MockFilterConfig(servletContext);

        mockFilterConfig.setInitParameter(InternetResourceService.ENABLE_CACHING_PARAMETER, "true");
        service.init(mockFilterConfig);

        InternetResourceBuilder builder = InternetResourceBuilder.getInstance();
        InternetResource resource = builder.createResource(this, MockCacheableResource.class.getName());
        MockResourceRequest resourceRequest = new MockResourceRequest(request);
        String key = resource.getKey();

        for (int i = 1; i <= 10; i++) {
            service.serviceResource(key, resourceRequest, response);

            byte[] content = ((MockServletOutputStream) response.getOutputStream()).content();
            String strContent = new String(content);

            assertEquals(strContent, "test");
            assertEquals(((MockCacheableResource) resource).getCounter(), 1);
            response.setOutputStream(null);
            response.setWriter(null);
        }
    }

    /**
     * Test method for {@link org.ajax4jsf.resource.InternetResourceService#serviceResource(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
     * @throws IOException
     * @throws Exception
     */
    public void testCachedServiceExpiration() throws Exception, IOException {
        InternetResourceService service = new InternetResourceService();
        MockFilterConfig mockFilterConfig = new MockFilterConfig(servletContext);

        mockFilterConfig.setInitParameter(InternetResourceService.ENABLE_CACHING_PARAMETER, "true");
        service.init(mockFilterConfig);

        InternetResourceBuilder builder = InternetResourceBuilder.getInstance();
        InternetResource resource = builder.createResource(this, MockCacheableResource.class.getName());
        MockResourceRequest resourceRequest = new MockResourceRequest(request);
        String key = resource.getKey();

        for (int j = 1; j <= 10; j++) {
            for (int i = 1; i <= 10; i++) {
                request.setPathElements(request.getContextPath(), request.getServletPath(), request.getPathInfo(),
                                        String.valueOf(i));
                service.serviceResource(key, resourceRequest, response);

                byte[] content = ((MockServletOutputStream) response.getOutputStream()).content();
                String strContent = new String(content);

                assertEquals(strContent, "test");
                response.setOutputStream(null);
                response.setWriter(null);
            }
        }

        int counter = ((MockCacheableResource) resource).getCounter();

        assertTrue("Resource was generated " + counter + " times", counter <= 10);
    }

    public void testSendResource() {

//      fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.ajax4jsf.resource.InternetResourceService#getProperties(java.lang.String)}.
     */
    public void testGetProperties() {

//      fail("Not yet implemented");
    }
}
