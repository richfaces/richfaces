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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import org.jboss.test.faces.AbstractFacesTest;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class CachedResourceImplTest extends AbstractFacesTest {
    /**
     *
     */
    private static final String CACHE_CONTROL = "Cache-Control";
    /**
     *
     */
    private static final String EXPIRES = "Expires";
    /**
     *
     */
    private static final String LAST_MODIFIED = "Last-Modified";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setupFacesRequest();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    private CachedResourceImpl createCachedResource() throws IOException {
        return createCachedResource(createTestHeaders());
    }

    private CachedResourceImpl createCachedResource(Map<String, String> headers) throws IOException {
        return createCachedResource(headers, null);
    }

    private CachedResourceImpl createCachedResource(Map<String, String> headers, InputStream stream) throws IOException {

        MockHeadersResourceImpl mockResource = new MockHeadersResourceImpl(headers, stream);
        CachedResourceImpl cachedResource = new MockCachedResourceImpl();

        cachedResource.initialize(mockResource);

        return cachedResource;
    }

    private Map<String, String> createTestHeaders() {
        Map<String, String> headers = new HashMap<String, String>();

        headers.put("ETag", "W/\"123\"");
        headers.put(LAST_MODIFIED, "Tue, 21 Jul 2009 12:45:09 GMT");
        headers.put(EXPIRES, "Tue, 28 Jul 2009 12:45:09 GMT");
        headers.put(CACHE_CONTROL, "public, max-age=86400");

        return headers;
    }

    private Calendar createBaseDateCalendar() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        calendar.clear();
        calendar.set(2009, Calendar.JULY, 21, 12, 45, 9);

        return calendar;
    }

    public void testInitialize() throws Exception {
        CachedResourceImpl cachedResource = createCachedResource();
        Calendar calendar = createBaseDateCalendar();
        Date baseDate = calendar.getTime();

        assertEquals("W/\"123\"", cachedResource.getEntityTag(facesContext));
        assertEquals(baseDate, cachedResource.getLastModified(facesContext));
        assertEquals(createTestHeaders(), cachedResource.getResponseHeaders());
        assertEquals("image/png", cachedResource.getContentType());

        Date expired = cachedResource.getExpired(facesContext);

        assertNotNull(expired);
        assertTrue(expired.after(baseDate));
        calendar.add(Calendar.DATE, 1);
        assertFalse(calendar.getTime().before(expired));
    }

    public void testInitializeExpires() throws Exception {
        Map<String, String> headers = createTestHeaders();

        headers.remove(CACHE_CONTROL);

        CachedResourceImpl cachedResource = createCachedResource(headers);
        Date expired = cachedResource.getExpired(facesContext);

        assertNotNull(expired);

        Calendar calendar = createBaseDateCalendar();

        assertTrue(expired.after(calendar.getTime()));
        calendar.add(Calendar.DATE, 1);
        assertFalse(expired.before(calendar.getTime()));
        calendar.add(Calendar.DATE, 6);
        assertTrue(expired.before(calendar.getTime()));
    }

    public void testInitializeMaxAge() throws Exception {
        Map<String, String> headers = createTestHeaders();

        headers.remove(EXPIRES);

        CachedResourceImpl cachedResource = createCachedResource(headers);
        Date expired = cachedResource.getExpired(facesContext);

        assertNotNull(expired);

        Calendar calendar = createBaseDateCalendar();

        assertTrue(expired.after(calendar.getTime()));
        calendar.add(Calendar.DATE, 1);
        assertTrue(expired.before(calendar.getTime()));
    }

    public void testInitializeSMaxAge() throws Exception {
        Map<String, String> headers = createTestHeaders();

        headers.put(CACHE_CONTROL, "public, max-age=86400, s-maxage=172800");

        CachedResourceImpl cachedResource = createCachedResource(headers);
        Date expired = cachedResource.getExpired(facesContext);

        assertNotNull(expired);

        Calendar calendar = createBaseDateCalendar();

        assertTrue(expired.after(calendar.getTime()));
        calendar.add(Calendar.DATE, 1);
        assertFalse(expired.before(calendar.getTime()));
        calendar.add(Calendar.DATE, 1);
        assertTrue(expired.before(calendar.getTime()));
    }

    public void testGetStream() throws Exception {
        ByteArrayInputStream testStream = new ByteArrayInputStream("test".getBytes("US-ASCII"));
        CachedResourceImpl cachedResource = createCachedResource(createTestHeaders(), testStream);
        InputStream stream = cachedResource.getInputStream();

        assertNotNull(stream);

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "US-ASCII"));

        assertEquals("test", reader.readLine());
        assertNull(reader.readLine());
        reader.close();
    }

    public void testGetBigStream() throws Exception {
        byte[] bs = new byte[127123];

        new Random().nextBytes(bs);

        ByteArrayInputStream bigStream = new ByteArrayInputStream(bs);
        CachedResourceImpl cachedResource = createCachedResource(createTestHeaders(), bigStream);
        InputStream stream = cachedResource.getInputStream();
        byte[] testBs = new byte[bs.length];

        assertEquals(testBs.length, stream.read(testBs));
        assertTrue(Arrays.equals(testBs, bs));
        assertEquals(-1, stream.read());
        stream.close();
    }

    public void testCacheable() throws Exception {
        CachedResourceImpl cachedResource = createCachedResource();

        assertTrue(cachedResource.isCacheable(facesContext));
    }

    public void testDefaults() throws Exception {
        CachedResourceImpl cachedResource = createCachedResource();

        try {
            cachedResource.getURL();
            fail();
        } catch (UnsupportedOperationException e) {

            // ok
        }

        try {
            cachedResource.getRequestPath();
            fail();
        } catch (UnsupportedOperationException e) {

            // ok
        }
    }

    public void testUserAgentNeedsUpdate() throws Exception {
        CachedResourceImpl cachedResource = createCachedResource();

        this.connection.addRequestHeaders(Collections.singletonMap("If-Modified-Since", "Tue, 21 Jul 2009 14:45:09 GMT"));
        assertFalse(cachedResource.userAgentNeedsUpdate(facesContext));
    }

    public void testUserAgentNeedsUpdate2() throws Exception {
        CachedResourceImpl cachedResource = createCachedResource();

        this.connection.addRequestHeaders(Collections.singletonMap("If-Modified-Since", "Tue, 21 Jul 2009 09:45:09 GMT"));
        assertTrue(cachedResource.userAgentNeedsUpdate(facesContext));
    }

    private class MockCachedResourceImpl extends CachedResourceImpl {
        private final long currentTime;

        MockCachedResourceImpl() {
            super();
            this.currentTime = createBaseDateCalendar().getTimeInMillis();
        }

        @Override
        long getCurrentTime() {
            return currentTime;
        }
    }

    private class MockHeadersResourceImpl extends Resource {
        private Map<String, String> headers;
        private InputStream stream;

        MockHeadersResourceImpl(Map<String, String> headers, InputStream stream) {
            super();
            setResourceName(getClass().getName());
            setContentType("image/png");
            this.headers = headers;
            this.stream = stream != null ? stream : new ByteArrayInputStream(new byte[0]);
        }

        @Override
        public Map<String, String> getResponseHeaders() {
            return headers;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return stream;
        }

        @Override
        public String getRequestPath() {
            throw new UnsupportedOperationException();
        }

        @Override
        public URL getURL() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean userAgentNeedsUpdate(FacesContext context) {
            throw new UnsupportedOperationException();
        }
    }

    ;
}
