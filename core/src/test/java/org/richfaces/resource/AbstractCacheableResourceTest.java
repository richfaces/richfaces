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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.context.FacesContext;

import org.easymock.IAnswer;
import org.easymock.classextension.EasyMock;
import org.jboss.test.faces.AbstractFacesTest;
import org.jboss.test.faces.mock.FacesMock;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
@SuppressWarnings("deprecation")
public class AbstractCacheableResourceTest extends AbstractFacesTest {
    private static final int MILLISECONDS_IN_HOUR = 60 * 60 * 1000;
    /* HTTP Date format required by the HTTP/1.1 RFC */
    private static final String RFC1123_DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final SimpleDateFormat RFC1123_DATE_FORMATTER;

    static {
        SimpleDateFormat format = new SimpleDateFormat(RFC1123_DATE_PATTERN, Locale.US);

        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        RFC1123_DATE_FORMATTER = format;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setupFacesRequest();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.test.AbstractFacesTest#tearDown()
     */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInitialRequest() throws Exception {
        MockAbstractResource defaultResource = new MockAbstractResource();

        assertNull(defaultResource.isMatchesEntityTag(facesContext));
        assertNull(defaultResource.isMatchesLastModified(facesContext));

        MockAbstractResource entityTaggedResource = new MockAbstractResource();

        entityTaggedResource.setEntityTag("3456");
        assertNull(entityTaggedResource.isMatchesEntityTag(facesContext));
        assertNull(entityTaggedResource.isMatchesLastModified(facesContext));

        MockAbstractResource timeKnownResource = new MockAbstractResource();

        timeKnownResource.setLastModified(new Date(System.currentTimeMillis() - MILLISECONDS_IN_HOUR));
        assertNull(timeKnownResource.isMatchesEntityTag(facesContext));
        assertNull(timeKnownResource.isMatchesLastModified(facesContext));
    }

    public void testEntityTag() throws Exception {
        this.connection.addRequestHeaders(Collections.singletonMap("If-None-Match", "\"3456\""));

        MockAbstractResource defaultResource = new MockAbstractResource();

        assertFalse(defaultResource.isMatchesEntityTag(facesContext));

        MockAbstractResource entityTaggedResource = new MockAbstractResource();

        entityTaggedResource.setEntityTag("\"3456\"");
        assertTrue(entityTaggedResource.isMatchesEntityTag(facesContext));

        MockAbstractResource unmatchedTaggedResource = new MockAbstractResource();

        unmatchedTaggedResource.setEntityTag("\"123\"");
        assertFalse(unmatchedTaggedResource.isMatchesEntityTag(facesContext));
    }

    public void testWeakEntityTag() throws Exception {
        this.connection.addRequestHeaders(Collections.singletonMap("If-None-Match", "W/\"3456\""));

        MockAbstractResource defaultResource = new MockAbstractResource();

        assertEquals(Boolean.FALSE, defaultResource.isMatchesEntityTag(facesContext));

        MockAbstractResource entityTaggedResource = new MockAbstractResource();

        entityTaggedResource.setEntityTag("\"3456\"");
        assertEquals(Boolean.TRUE, entityTaggedResource.isMatchesEntityTag(facesContext));

        MockAbstractResource unmatchedTaggedResource = new MockAbstractResource();

        unmatchedTaggedResource.setEntityTag("\"123\"");
        assertEquals(Boolean.FALSE, unmatchedTaggedResource.isMatchesEntityTag(facesContext));
    }

    public void testMultiEntityTags() throws Exception {
        this.connection.addRequestHeaders(Collections.singletonMap("If-None-Match", "W/\"3456\", \"012\""));

        MockAbstractResource defaultResource = new MockAbstractResource();

        assertEquals(Boolean.FALSE, defaultResource.isMatchesEntityTag(facesContext));

        MockAbstractResource entityTaggedResource = new MockAbstractResource();

        entityTaggedResource.setEntityTag("\"3456\"");
        assertEquals(Boolean.TRUE, entityTaggedResource.isMatchesEntityTag(facesContext));

        MockAbstractResource anotherTaggedResource = new MockAbstractResource();

        anotherTaggedResource.setEntityTag("W/\"012\"");
        assertEquals(Boolean.TRUE, anotherTaggedResource.isMatchesEntityTag(facesContext));

        MockAbstractResource unmatchedTaggedResource = new MockAbstractResource();

        unmatchedTaggedResource.setEntityTag("\"123\"");
        assertEquals(Boolean.FALSE, unmatchedTaggedResource.isMatchesEntityTag(facesContext));
    }

    public void testIfModified() throws Exception {
        Date baseDate = new Date();
        Date beforeBaseDate = new Date(baseDate.getTime() - MILLISECONDS_IN_HOUR);
        Date afterBaseDate = new Date(baseDate.getTime() + MILLISECONDS_IN_HOUR);

        this.connection
                .addRequestHeaders(Collections.singletonMap("If-Modified-Since", RFC1123_DATE_FORMATTER.format(baseDate)));

        MockAbstractResource defaultResource = new MockAbstractResource();

        assertEquals(Boolean.FALSE, defaultResource.isMatchesLastModified(facesContext));

        MockAbstractResource actualResource = new MockAbstractResource();

        actualResource.setLastModified(beforeBaseDate);
        assertEquals(Boolean.TRUE, actualResource.isMatchesLastModified(facesContext));

        MockAbstractResource expiredResource = new MockAbstractResource();

        expiredResource.setLastModified(afterBaseDate);
        assertEquals(Boolean.FALSE, expiredResource.isMatchesLastModified(facesContext));
    }

    public void testUserAgentNeedsUpdate() throws Exception {
        BooleanAnswer tagMatches = new BooleanAnswer();
        BooleanAnswer lastModifiedMatches = new BooleanAnswer();
        BooleanAnswer cacheable = new BooleanAnswer();
        AbstractCacheableResource resource = FacesMock.createControl().createMock(AbstractTestResource.class,
                AbstractTestResource.class.getDeclaredMethods());

        EasyMock.expect(resource.isCacheable(facesContext)).andStubAnswer(cacheable);

        String matchHeaderValue = RFC1123_DATE_FORMATTER.format(new Date());

        this.connection.addRequestHeaders(Collections.singletonMap("If-None-Match", matchHeaderValue));
        EasyMock.expect(resource.isMatchesEntityTag(facesContext, matchHeaderValue)).andStubAnswer(tagMatches);

        String modifiedCondition = "\"1234\"";

        this.connection.addRequestHeaders(Collections.singletonMap("If-Modified-Since", modifiedCondition));
        EasyMock.expect(resource.isMatchesLastModified(facesContext, modifiedCondition)).andStubAnswer(lastModifiedMatches);
        EasyMock.replay(resource);
        cacheable.setValue(false);
        assertTrue(resource.userAgentNeedsUpdate(facesContext));
        cacheable.setValue(true);
        tagMatches.setValue(Boolean.TRUE);
        lastModifiedMatches.setValue(Boolean.TRUE);
        assertFalse(resource.userAgentNeedsUpdate(facesContext));
        tagMatches.setValue(Boolean.FALSE);
        lastModifiedMatches.setValue(Boolean.FALSE);
        assertTrue(resource.userAgentNeedsUpdate(facesContext));
        tagMatches.setValue(Boolean.FALSE);
        lastModifiedMatches.setValue(Boolean.TRUE);
        assertTrue(resource.userAgentNeedsUpdate(facesContext));
        tagMatches.setValue(Boolean.TRUE);
        lastModifiedMatches.setValue(Boolean.FALSE);
        assertTrue(resource.userAgentNeedsUpdate(facesContext));
        this.connection.addRequestHeaders(Collections.singletonMap("If-None-Match", (String) null));
        lastModifiedMatches.setValue(Boolean.FALSE);
        assertTrue(resource.userAgentNeedsUpdate(facesContext));
        lastModifiedMatches.setValue(Boolean.TRUE);
        assertFalse(resource.userAgentNeedsUpdate(facesContext));
        this.connection.addRequestHeaders(Collections.singletonMap("If-None-Match", matchHeaderValue));
        this.connection.addRequestHeaders(Collections.singletonMap("If-Modified-Since", (String) null));
        tagMatches.setValue(Boolean.FALSE);
        assertTrue(resource.userAgentNeedsUpdate(facesContext));
        tagMatches.setValue(Boolean.TRUE);
        assertFalse(resource.userAgentNeedsUpdate(facesContext));
        this.connection.addRequestHeaders(Collections.singletonMap("If-Modified-Since", (String) null));
        this.connection.addRequestHeaders(Collections.singletonMap("If-None-Match", (String) null));
        assertTrue(resource.userAgentNeedsUpdate(facesContext));
        cacheable.setValue(false);
        assertTrue(resource.userAgentNeedsUpdate(facesContext));
    }

    public abstract static class AbstractTestResource extends AbstractCacheableResource {
        @Override
        public String toString() {
            return "mock";
        }

        @Override
        public boolean isCacheable(FacesContext context) {
            return false;
        }

        @Override
        protected boolean isMatchesEntityTag(FacesContext context, String matchHeaderValue) {
            return super.isMatchesEntityTag(context, matchHeaderValue);
        }

        @Override
        protected boolean isMatchesLastModified(FacesContext context, String modifiedCondition) {
            return super.isMatchesLastModified(context, modifiedCondition);
        }
    }

    private static class BooleanAnswer implements IAnswer<Boolean> {
        private Boolean value;

        public Boolean answer() throws Throwable {
            return value;
        }

        public void setValue(Boolean value) {
            this.value = value;
        }
    }
}

class MockAbstractResource extends AbstractCacheableResource {
    private String entityTag;
    private Date lastModified;

    public void setEntityTag(String entityTag) {
        this.entityTag = entityTag;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String getEntityTag(FacesContext context) {
        return entityTag;
    }

    @Override
    protected Date getLastModified(FacesContext context) {
        return lastModified;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRequestPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URL getURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCacheable(FacesContext context) {
        throw new UnsupportedOperationException();
    }
}
