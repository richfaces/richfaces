/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.resource;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.context.FacesContext;

import org.easymock.EasyMock;
import org.jboss.test.faces.AbstractFacesTest;
import org.richfaces.application.Module;
import org.richfaces.application.ServicesFactory;
import org.richfaces.application.ServicesFactoryImpl;
import org.richfaces.application.Uptime;
import org.richfaces.application.ServiceTracker;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class AbstractBaseResourceTest extends AbstractFacesTest {
    @Override
    public void setUp() throws Exception {
        super.setUp();
        setupFacesRequest();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetHeaders() throws Exception {
        MockResourceImpl mockResource = new MockResourceImpl();

        mockResource.setCacheable(true);
        mockResource.setEntityTag("\"etag0\"");
        mockResource.setContentLength(80);
        mockResource.setContentType("image/png");

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        calendar.clear();
        calendar.set(2009, Calendar.JULY, 13, 12, 45, 9);
        mockResource.setLastModified(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        mockResource.setCurrentTime(calendar.getTimeInMillis());
        calendar.add(Calendar.DATE, 7);
        mockResource.setExpired(calendar.getTime());

        Map<String, String> headers = mockResource.getResponseHeaders();

        assertEquals("Tue, 21 Jul 2009 12:45:09 GMT", headers.get("Expires"));
        assertEquals("Mon, 13 Jul 2009 12:45:09 GMT", headers.get("Last-Modified"));
        assertEquals("Tue, 14 Jul 2009 12:45:09 GMT", headers.get("Date"));
        assertEquals("\"etag0\"", headers.get("ETag"));
        assertEquals("max-age=604800", headers.get("Cache-Control"));
        assertEquals("80", headers.get("Content-Length"));
        assertEquals("image/png", headers.get("Content-Type"));
        assertEquals(7, headers.size());
        mockResource.setTimeToLive(14 * 24 * 60 * 60 /* 14 days */);
        headers = mockResource.getResponseHeaders();
        assertEquals("Tue, 28 Jul 2009 12:45:09 GMT", headers.get("Expires"));
        assertEquals("Mon, 13 Jul 2009 12:45:09 GMT", headers.get("Last-Modified"));
        assertEquals("Tue, 14 Jul 2009 12:45:09 GMT", headers.get("Date"));
        assertEquals("max-age=1209600", headers.get("Cache-Control"));
    }

    public void testGetHeadersNonCacheable() throws Exception {
        MockResourceImpl mockResource = new MockResourceImpl();

        mockResource.setCacheable(false);
        mockResource.setContentLength(120);
        mockResource.setContentType("image/jpg");
        mockResource.setEntityTag("\"etag1\"");

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        calendar.clear();
        calendar.set(2009, Calendar.JULY, 13, 12, 45, 9);
        mockResource.setLastModified(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        mockResource.setCurrentTime(calendar.getTimeInMillis());
        calendar.add(Calendar.DATE, 7);
        mockResource.setExpired(calendar.getTime());

        Map<String, String> headers = mockResource.getResponseHeaders();

        assertEquals("0", headers.get("Expires"));
        assertEquals("max-age=0, no-store, no-cache", headers.get("Cache-Control"));
        assertEquals("no-cache", headers.get("Pragma"));
        assertEquals("120", headers.get("Content-Length"));
        assertEquals("image/jpg", headers.get("Content-Type"));
        assertEquals("Mon, 13 Jul 2009 12:45:09 GMT", headers.get("Last-Modified"));
        assertEquals("Tue, 14 Jul 2009 12:45:09 GMT", headers.get("Date"));
        assertEquals(7, headers.size());
    }

    public void testGetRequestPath() throws Exception {
        String resourceState = "data";
        final ResourceCodec resourceCodec = EasyMock.createMock(ResourceCodec.class);

        EasyMock.expect(
            resourceCodec.encodeResourceRequestPath(EasyMock.same(facesContext), EasyMock.eq("custom.library"),
                EasyMock.eq("org.richfaces.resource.MockStateAwareResource"), EasyMock.aryEq(resourceState.getBytes()),
                EasyMock.eq("4_0_alpha"))).andReturn("/rfRes/Resource0/4_0_alpha/data?l=custom.library");

        EasyMock.expect(
            resourceCodec.encodeJSFMapping(EasyMock.same(facesContext),
                EasyMock.eq("/rfRes/Resource0/4_0_alpha/data?l=custom.library"))).andReturn(
            "/rfRes/Resource0/4_0_alpha/data.jsf?l=custom.library");

        EasyMock.expect(
            resourceCodec.encodeResourceRequestPath(EasyMock.same(facesContext), EasyMock.eq("custom.library"),
                EasyMock.eq("org.richfaces.resource.MockStateAwareResource"), EasyMock.eq(null), EasyMock.eq("4_0_alpha")))
            .andReturn("/rfRes/Resource1/4_0_alpha?l=custom.library");

        EasyMock.expect(
            resourceCodec.encodeJSFMapping(EasyMock.same(facesContext),
                EasyMock.eq("/rfRes/Resource1/4_0_alpha?l=custom.library"))).andReturn(
            "/rfRes/Resource1/4_0_alpha.jsf?l=custom.library");

        EasyMock.expect(
            resourceCodec.encodeResourceRequestPath(EasyMock.same(facesContext), EasyMock.<String>isNull(),
                EasyMock.eq("org.richfaces.resource.MockResource"), EasyMock.eq(null), EasyMock.eq("4_0_alpha"))).andReturn(
            "/rfRes/Resource2/4_0_alpha");

        EasyMock.expect(resourceCodec.encodeJSFMapping(EasyMock.same(facesContext), EasyMock.eq("/rfRes/Resource2/4_0_alpha")))
            .andReturn("/rfRes/Resource2/4_0_alpha.jsf");

        EasyMock.replay(resourceCodec);
        ServicesFactoryImpl injector = new ServicesFactoryImpl();
        injector.init(Collections.<Module>singletonList(new Module() {
            public void configure(ServicesFactory injector) {
                injector.setInstance(ResourceCodec.class, resourceCodec);
                injector.setInstance(Uptime.class, new Uptime());
            }
        }));
        ServiceTracker.setFactory(injector);

        MockStateAwareResourceImpl stateAwareResourceImpl = new MockStateAwareResourceImpl();
        stateAwareResourceImpl.setLibraryName("custom.library");
        stateAwareResourceImpl.setVersion("4_0_alpha");
        stateAwareResourceImpl.setState(resourceState);
        assertEquals("org.richfaces.resource.MockStateAwareResource", stateAwareResourceImpl.getResourceName());
        assertEquals("/rfRes/Resource0/4_0_alpha/data.jsf?l=custom.library", stateAwareResourceImpl.getRequestPath());
        stateAwareResourceImpl.setTransient(true);
        assertEquals("/rfRes/Resource1/4_0_alpha.jsf?l=custom.library", stateAwareResourceImpl.getRequestPath());

        MockResourceImpl resourceImpl = new MockResourceImpl();

        resourceImpl.setVersion("4_0_alpha");
        assertEquals("org.richfaces.resource.MockResource", resourceImpl.getResourceName());
        assertEquals("/rfRes/Resource2/4_0_alpha.jsf", resourceImpl.getRequestPath());
        EasyMock.verify(resourceCodec);
    }

    public void testGetURL() throws Exception {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        MockResourceImpl mockResource = new MockResourceImpl();

        mockResource.setContentLength(130);
        mockResource.setContentType("image/gif");
        mockResource.setInputStream(stream);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        calendar.clear();
        calendar.set(2009, Calendar.JUNE, 12, 21, 38, 59);

        Date lastModified = calendar.getTime();

        mockResource.setLastModified(lastModified);
        calendar.add(Calendar.MONTH, 1);

        Date expired = calendar.getTime();

        mockResource.setExpired(expired);

        URL url = mockResource.getURL();

        assertNotNull(url);
        assertEquals("jsfresource:org.richfaces.resource.MockResource", url.toExternalForm());

        URLConnection urlConnection = url.openConnection();

        assertNotNull(urlConnection);
        urlConnection.connect();
        assertEquals(130, urlConnection.getContentLength());
        assertEquals("image/gif", urlConnection.getContentType());
        assertEquals(lastModified.getTime(), urlConnection.getLastModified());
        assertSame(stream, urlConnection.getInputStream());
        assertSame(url, urlConnection.getURL());

        MockResourceImpl mockResource2 = new MockResourceImpl();
        URLConnection urlConnection2 = mockResource2.getURL().openConnection();

        urlConnection2.connect();
        assertEquals(-1, urlConnection2.getContentLength());
        assertNull(urlConnection2.getContentType());
        assertEquals(0, urlConnection2.getLastModified());
    }

    public void testDefaults() throws Exception {
        ResourceImpl defaultResource = new ResourceImpl();

        assertTrue(defaultResource.isCacheable(facesContext));
        assertEquals("org.richfaces.resource.ResourceImpl", defaultResource.getResourceName());
        assertEquals(-1, defaultResource.getContentLength(facesContext));
        assertNull(defaultResource.getEntityTag(facesContext));
        assertNull(defaultResource.getExpires(facesContext));

        Date lastModified = defaultResource.getLastModified(facesContext);

        assertNotNull(lastModified);
        assertTrue(System.currentTimeMillis() >= lastModified.getTime());

        ResourceImpl defaultETagResource = new ResourceImpl() {
            protected Date getLastModified(FacesContext context) {
                return new Date(12471234567890L);
            }

            ;

            protected int getContentLength(FacesContext context) {
                return 1297;
            }

            ;
        };

        assertEquals("W/\"1297-12471234567890\"", defaultETagResource.getEntityTag(facesContext));
    }

    public void testUserAgentNeedsUpdate() throws Exception {
        long currentTime = System.currentTimeMillis();
        MockResourceImpl actualResource = new MockResourceImpl();

        actualResource.setLastModified(new Date(currentTime - 30000));

        MockResourceImpl expiredResource = new MockResourceImpl();

        expiredResource.setLastModified(new Date(currentTime - 10000));
        this.connection.addRequestHeaders(Collections.singletonMap("If-Modified-Since",
            ResourceUtils.formatHttpDate(new Date(currentTime - 20000))));
        assertTrue(expiredResource.userAgentNeedsUpdate(facesContext));
        assertFalse(actualResource.userAgentNeedsUpdate(facesContext));
    }

    private class MockResourceImpl extends AbstractCacheableResource implements VersionedResource {
        private int contentLength = -1;
        private long currentTime;
        private String entityTag;
        private Date expired;
        private InputStream inputStream;
        private Date lastModified;
        private int ttl;
        private String version;

        public MockResourceImpl() {
            super();
            setResourceName("org.richfaces.resource.MockResource");
        }

        @Override
        protected int getContentLength(FacesContext context) {
            return contentLength;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setContentLength(int contentLength) {
            this.contentLength = contentLength;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        protected Date getLastModified(FacesContext context) {
            return lastModified;
        }

        public void setLastModified(Date lastModified) {
            this.lastModified = lastModified;
        }

        @Override
        public Date getExpires(FacesContext context) {
            return expired;
        }

        public void setExpired(Date expired) {
            this.expired = expired;
        }

        @Override
        public String getEntityTag(FacesContext context) {
            return entityTag;
        }

        /**
         * @param entityTag the entityTag to set
         */
        public void setEntityTag(String entityTag) {
            this.entityTag = entityTag;
        }

        @Override
        boolean isResourceRequest() {
            return true;
        }

        @Override
        protected long getCurrentTime() {
            return currentTime;
        }

        void setCurrentTime(long currentTime) {
            this.currentTime = currentTime;
        }

        @Override
        public int getTimeToLive(FacesContext context) {
            return ttl;
        }

        public void setTimeToLive(int ttl) {
            this.ttl = ttl;
        }
    }

    private class MockStateAwareResourceImpl extends MockResourceImpl implements StateHolderResource {
        private boolean _transient;
        private String resourceState;

        public MockStateAwareResourceImpl() {
            super();
            setResourceName("org.richfaces.resource.MockStateAwareResource");
        }

        public void setState(String resourceState) {
            this.resourceState = resourceState;
        }

        public void setTransient(boolean transient1) {
            _transient = transient1;
        }

        public boolean isTransient() {
            return _transient;
        }

        public void readState(FacesContext context, DataInput dataInput) throws IOException {
            resourceState = dataInput.readLine();
        }

        public void writeState(FacesContext context, DataOutput objectOutput) throws IOException {
            objectOutput.writeBytes(resourceState);
        }
    }

    private class ResourceImpl extends AbstractCacheableResource {
        public ResourceImpl() {
            super();
            setResourceName("org.richfaces.resource.ResourceImpl");
        }

        @Override
        public InputStream getInputStream() throws IOException {
            throw new UnsupportedOperationException();
        }
    }
}
