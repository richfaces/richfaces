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

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.TimeZone;

import javax.faces.context.FacesContext;

import org.ajax4jsf.tests.AbstractThreadedAjax4JsfTestCase;

import org.apache.shale.test.mock.MockHttpServletRequest;
import org.apache.shale.test.mock.MockHttpServletResponse;
import org.apache.shale.test.mock.MockServletOutputStream;

/**
 * @author shura
 *
 */
public class ResourceServiceThreadsTestCase extends AbstractThreadedAjax4JsfTestCase {
    String key;
    InternetResource resource;
    InternetResourceService service;

    /**
     * @param s
     */
    public ResourceServiceThreadsTestCase(String s) {
        super(s);

        // TODO Auto-generated constructor stub
    }

    public void setUp() throws Exception {
        super.setUp();
        service = new InternetResourceService();

        MockFilterConfig mockFilterConfig = new MockFilterConfig(servletContext);

        mockFilterConfig.setInitParameter(InternetResourceService.ENABLE_CACHING_PARAMETER, "true");
        service.init(mockFilterConfig);

        InternetResourceBuilder builder = InternetResourceBuilder.getInstance();

        resource = builder.createResource(this, MockCacheableResource.class.getName());
        key = resource.getKey();
    }

    public void tearDown() throws Exception {
        super.tearDown();
        InternetResourceBuilder.setInstance(null);
        resource = null;
        service = null;
        key = null;
    }

    public void testTreadServiceResource() {
        TestCaseRunnable[] runnables = new TestCaseRunnable[1000];

        for (int i = 0; i < runnables.length; i++) {
            runnables[i] = new ResourceRunner(String.valueOf(i % 10));
        }

        this.runTestCaseRunnables(runnables);

        int counter = ((MockCacheableResource) resource).getCounter();

        assertTrue("Resource was generated " + counter + " times", counter <= 10);
    }

    public class ResourceRunner extends TestCaseRunnable {
        private String data;

        /**
         * @param data
         */
        public ResourceRunner(String data) {
            super();
            this.data = data;
        }

        public void runTestCase(FacesContext context) throws Throwable {
            MockHttpServletResponse response = new MockHttpServletResponse() {

                /*
                 *  (non-Javadoc)
                 * @see org.apache.shale.test.mock.MockHttpServletResponse#addDateHeader(java.lang.String, long)
                 * Default shale implementation is not thread-safe.
                 */
                public void addDateHeader(String name, long value) {
                    SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

                    format.setTimeZone(TimeZone.getTimeZone("GMT"));
                    addHeader(name, format.format(new Date(value)));
                }

                // TODO fix when setContentLength in shale-test will start function
                // https://issues.apache.org/struts/browse/SHALE-495
                public int getContentLength() {
                    return Integer.parseInt(getHeader("Content-Length"));
                }
                public void setContentLength(int length) {
                    addIntHeader("Content-Length", length);
                }
            };
            MockHttpServletRequest req = new MockHttpServletRequest(session);

            req.setServletContext(servletContext);
            req.setPathElements(request.getContextPath(), request.getServletPath(), request.getPathInfo(), data);

            MockResourceRequest resourceRequest = new MockResourceRequest(req);

            service.serviceResource(key, resourceRequest, response);

            byte[] content = ((MockServletOutputStream) response.getOutputStream()).content();
            String strContent = new String(content);

            assertEquals(strContent, "test");
        }
    }
}
