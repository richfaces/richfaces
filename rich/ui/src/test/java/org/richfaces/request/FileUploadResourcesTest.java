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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

/**
 * @author Nick Belaevski
 *
 */
public class FileUploadResourcesTest {
    private FileUploadMemoryResource memoryResource;
    private FileUploadDiscResource discResource;
    private String memoryTempDirectory;
    private String discTempDirectory;

    private String createTempDirectory() {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
        if (!tmpDir.mkdirs()) {
            fail();
        }

        return tmpDir.getAbsolutePath();
    }

    private void eraseDirectory(String location) {
        File file = new File(location);

        for (File f : file.listFiles()) {
            f.delete();
        }

        file.delete();
    }

    private byte[] getRandomBytes(int size) {
        byte[] bs = new byte[size];
        new Random().nextBytes(bs);
        return bs;
    }

    private byte[] readFully(InputStream is) throws IOException {
        try {
            return ByteStreams.toByteArray(is);
        } finally {
            Closeables.closeQuietly(is);
        }
    }

    private byte[] readFully(File file) throws IOException {
        assertNotNull(file);
        return readFully(new FileInputStream(file));
    }

    private File getSingleFile(String location) {
        File root = new File(location);

        File[] files = root.listFiles();
        switch (files.length) {
            case 0:
                return null;
            case 1:
                return files[0];
            default:
                throw new IllegalStateException("File is not single");
        }
    }

    @Before
    public void setUp() throws Exception {
        memoryTempDirectory = createTempDirectory();
        discTempDirectory = createTempDirectory();

        memoryResource = new FileUploadMemoryResource("form:memoryUpload", memoryTempDirectory);
        memoryResource.create();
        discResource = new FileUploadDiscResource("form:discUpload", discTempDirectory);
        discResource.create();
    }

    @After
    public void tearDown() throws Exception {
        eraseDirectory(memoryTempDirectory);
        eraseDirectory(discTempDirectory);

        memoryTempDirectory = null;
        discTempDirectory = null;

        memoryResource = null;
        discResource = null;
    }

    @Test
    public void testBasics() throws Exception {
        int length = 5;
        byte[] bytes = getRandomBytes(length);

        memoryResource.handle(bytes, bytes.length);
        discResource.handle(bytes, bytes.length);

        memoryResource.complete();
        discResource.complete();

        assertTrue(memoryResource.isFileParam());
        assertTrue(discResource.isFileParam());

        InputStream is = null;

        try {
            is = memoryResource.getInputStream();
            assertNotNull(is);
        } finally {
            Closeables.closeQuietly(is);
        }
        try {
            is = discResource.getInputStream();
            assertNotNull(is);
        } finally {
            Closeables.closeQuietly(is);
        }

        assertSame(memoryResource, memoryResource.getResource());
        assertSame(discResource, discResource.getResource());

        assertNull(memoryResource.getValue());
        assertNull(discResource.getValue());

        assertEquals("form:memoryUpload", memoryResource.getName());
        assertEquals("form:discUpload", discResource.getName());
    }

    @Test
    public void testSmallData() throws Exception {
        // should be less than 128 - initial buffer size
        int length = 68;
        byte[] bytes = getRandomBytes(length);

        memoryResource.handle(bytes, bytes.length);
        discResource.handle(bytes, bytes.length);

        memoryResource.complete();
        discResource.complete();

        checkResourcesContent(bytes);
    }

    @Test
    public void testLargeData() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int count = new Random().nextInt(256) + 256;
        for (int i = 0; i < count; i++) {
            int size = new Random().nextInt(512) + 512;
            byte[] bytes = getRandomBytes(size);

            memoryResource.handle(bytes, bytes.length);
            discResource.handle(bytes, bytes.length);

            baos.write(bytes);
        }

        memoryResource.complete();
        discResource.complete();

        checkResourcesContent(baos.toByteArray());
    }

    private void checkResourcesContent(byte[] expected) throws IOException {
        int length = expected.length;

        assertEquals(length, memoryResource.getSize());
        assertEquals(length, discResource.getSize());

        assertArrayEquals(expected, readFully(memoryResource.getInputStream()));
        assertArrayEquals(expected, readFully(discResource.getInputStream()));

        assertNull(getSingleFile(memoryTempDirectory));
        assertArrayEquals(expected, readFully(getSingleFile(discTempDirectory)));

        memoryResource.write("out");
        discResource.write("out");

        File memoryOutFile = getSingleFile(memoryTempDirectory);
        File discOutFile = getSingleFile(discTempDirectory);

        assertEquals("out", memoryOutFile.getName());
        assertEquals("out", discOutFile.getName());

        assertTrue(memoryOutFile.getAbsolutePath().startsWith(memoryTempDirectory));
        assertTrue(discOutFile.getAbsolutePath().startsWith(discTempDirectory));

        assertArrayEquals(expected, readFully(memoryOutFile));
        assertArrayEquals(expected, readFully(discOutFile));
    }

    @Test
    public void testDelete() throws Exception {
        int length = 5;
        byte[] bytes = getRandomBytes(length);

        memoryResource.handle(bytes, bytes.length);
        discResource.handle(bytes, bytes.length);

        memoryResource.complete();
        discResource.complete();

        assertNull(getSingleFile(memoryTempDirectory));
        assertNotNull(getSingleFile(discTempDirectory));

        memoryResource.delete();
        discResource.delete();

        assertNull(getSingleFile(memoryTempDirectory));
        assertNull(getSingleFile(discTempDirectory));
    }
}
