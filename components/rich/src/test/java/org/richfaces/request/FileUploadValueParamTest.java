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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Random;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Nick Belaevski
 *
 */
public class FileUploadValueParamTest {
    private FileUploadValueParam param;

    @Before
    public void setUp() throws Exception {
        param = new FileUploadValueParam("form:upload", "UTF-8");
        param.create();
    }

    @After
    public void tearDown() throws Exception {
        param = null;
    }

    @Test
    public void testBasics() throws Exception {
        param.handle("test".getBytes(), 4);
        param.complete();

        assertEquals("form:upload", param.getName());
        assertFalse(param.isFileParam());
        assertNull(param.getResource());
    }

    @Test
    public void testShortParam() throws Exception {
        byte[] bytes = "test".getBytes();
        param.handle(bytes, bytes.length);
        param.complete();

        assertEquals("test", param.getValue());
    }

    @Test
    public void testLongParam() throws Exception {
        StringBuilder sb = new StringBuilder();
        CharsetEncoder charsetEncoder = Charset.forName("UTF-8").newEncoder();

        for (int i = 0; i < 256; i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int count = new Random().nextInt(128) + 128;
            while (count != 0) {
                char c = (char) new Random().nextInt(Character.MAX_VALUE);

                if (charsetEncoder.canEncode(c)) {
                    baos.write(charsetEncoder.encode(CharBuffer.wrap(new char[] { c })).array());
                    count--;
                }
            }

            byte[] bs = baos.toByteArray();
            param.handle(bs, bs.length);
            sb.append(new String(bs, "UTF-8"));
        }

        param.complete();
        assertEquals(sb.toString(), param.getValue());
    }

    @Test
    public void testNullencoding() throws Exception {
        param = new FileUploadValueParam("form:upload", null);
        param.create();

        byte[] bytes = "testing...".getBytes();
        param.handle(bytes, bytes.length - 3);

        param.complete();

        assertEquals("testing", param.getValue());
    }
}
