/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.jboss.test.faces.mock.Environment;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.exception.FileUploadException;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;

/**
 * @author Nick Belaevski
 *
 */
@RunWith(MockTestRunner.class)
public class UploadedFile25Test {
    private static final String TEST_DATA = "test data";
    private static final IAnswer<InputStream> INPUT_STREAM_SUPPLIER = new IAnswer<InputStream>() {
        public InputStream answer() throws Throwable {
            return new ByteArrayInputStream(TEST_DATA.getBytes());
        }
    };
    private UploadedFile25 uploadedFile;
    private MockUploadResource uploadResource;


    @Mock
    @Environment
    protected MockFacesEnvironment environment;

    @Before
    public void setUp() throws Exception {
        Multimap<String, String> headers = LinkedListMultimap.<String, String>create();

        headers.put("content-type", "image/png");
        headers.put("filename", "x.png");
        headers.put("x-rftest", "set");
        headers.put("x-rftest", "of");
        headers.put("x-rftest", "values");

        uploadResource = environment.createMock(MockUploadResource.class);
        uploadedFile = new UploadedFile25("form:fileUpload", uploadResource, headers);
    }

    @After
    public void tearDown() throws Exception {
        uploadResource = null;
        uploadedFile = null;
    }

    @Test
    public void testAttributes() throws Exception {
        assertEquals("form:fileUpload", uploadedFile.getParameterName());
        assertEquals("image/png", uploadedFile.getContentType());
        assertEquals("x.png", uploadedFile.getName());
    }

    @Test
    public void testHeaders() throws Exception {
        assertEquals("image/png", uploadedFile.getHeader("Content-Type"));
        assertEquals("set", uploadedFile.getHeader("X-RFTest"));
        assertNull(uploadedFile.getHeader("X-RFUnknown"));

        assertEquals(Arrays.asList("image/png"), Lists.newArrayList(uploadedFile.getHeaders("Content-Type")));
        assertEquals(Arrays.asList("set", "of", "values"), Lists.newArrayList(uploadedFile.getHeaders("X-RFTest")));
        assertEquals(Arrays.asList(), Lists.newArrayList(uploadedFile.getHeaders("X-RFUnknown")));

        Collection<String> sortedHeaderNames = Sets.newTreeSet(uploadedFile.getHeaderNames());
        assertEquals(Sets.newLinkedHashSet(Arrays.asList("content-type", "filename", "x-rftest")), sortedHeaderNames);
    }

    @Test
    public void testResource() throws Exception {
        EasyMock.expect(uploadResource.getSize()).andStubReturn((long) TEST_DATA.length());
        EasyMock.expect(uploadResource.getInputStream()).andStubAnswer(INPUT_STREAM_SUPPLIER);

        uploadResource.write(EasyMock.eq("output.png"));
        EasyMock.expectLastCall();

        uploadResource.delete();
        EasyMock.expectLastCall();

        environment.getControl().replay();

        assertEquals(TEST_DATA.length(), uploadedFile.getSize());
        assertNotNull(uploadedFile.getInputStream());

        assertEquals(TEST_DATA, new String(ByteStreams.toByteArray(uploadedFile.getInputStream()), "US-ASCII"));
        assertEquals(TEST_DATA, new String(uploadedFile.getData(), "US-ASCII"));

        uploadedFile.write("output.png");
        uploadedFile.delete();
    }

    @Test
    public void testGetDataExceptions() throws Exception {
        EasyMock.expect(uploadResource.getSize()).andStubReturn((long) Long.MAX_VALUE);
        EasyMock.expect(uploadResource.getInputStream()).andStubAnswer(INPUT_STREAM_SUPPLIER);

        environment.getControl().replay();

        try {
            uploadedFile.getData();

            fail();
        } catch (FileUploadException e) {
            // expected - ignore
        }

        environment.getControl().reset();

        EasyMock.expect(uploadResource.getSize()).andStubReturn(1L);
        EasyMock.expect(uploadResource.getInputStream()).andStubThrow(new IOException("No stream available"));

        environment.getControl().replay();

        try {
            uploadedFile.getData();

            fail();
        } catch (FileUploadException e) {
            // expected - ignore
            assertTrue(e.getCause() instanceof IOException);
        }
    }

    public abstract class MockUploadResource extends FileUploadResource  {
        public MockUploadResource() {
            super("", "");
        }
    }
}
