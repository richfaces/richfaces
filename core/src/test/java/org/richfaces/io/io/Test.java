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
package org.richfaces.io.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.ajax4jsf.io.FastBufferInputStream;
import org.ajax4jsf.io.FastBufferOutputStream;
import org.ajax4jsf.io.FastBufferReader;
import org.ajax4jsf.io.FastBufferWriter;
import org.junit.Ignore;

@Ignore
public final class Test {
    private static final int ARRAY_LENGTH = 27;
    private static final int READ_LENGTH = 22;
    private static final int READ_OFF = 4;
    private static final boolean OUT_STRING = true;
    private static final boolean BUILD_STRING = false;

    private Test() {
    }

    static void testStreams() throws IOException {
        String s = "This is a senseless text to test streams.\n";

        for (int i = 0; i < 10; i++) {
            s = s + s; // repeated 16 times
        }

        byte[] bytes = s.getBytes();
        FastBufferOutputStream output = new FastBufferOutputStream(16);

        // write it several times.
        for (int i = 0; i < 4; i++) {
            output.write(bytes);
        }

        // write it one more time by one byte
        for (int i = 0; i < bytes.length; i++) {
            output.write(bytes[i]);
        }

        FastBufferInputStream input = new FastBufferInputStream(output.getFirstBuffer());
        StringBuffer sb = new StringBuffer();

        // use for reading unconvenient array length.
        byte[] bs = new byte[ARRAY_LENGTH];
        int l = 0;

        while ((l = input.read(bs, READ_OFF, READ_LENGTH)) >= 0) {
            if (BUILD_STRING) {
                sb.append(new String(bs, READ_OFF, l));
            }
        }

        if (BUILD_STRING && OUT_STRING) {
            System.out.println(sb);
            System.out.println("Length=" + output.getLength());
        }
    }

    static void testStandardStreams() throws IOException {
        String s = "This is a senseless text to test streams.\n";

        for (int i = 0; i < 10; i++) {
            s = s + s; // repeated 16 times
        }

        byte[] bytes = s.getBytes();
        ByteArrayOutputStream output = new ByteArrayOutputStream(16);

        // write it several times.
        for (int i = 0; i < 4; i++) {
            output.write(bytes);
        }

        // write it one more time by one byte
        for (int i = 0; i < bytes.length; i++) {
            output.write(bytes[i]);
        }

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        StringBuffer sb = new StringBuffer();

        // use for reading unconvenient array length.
        byte[] bs = new byte[ARRAY_LENGTH];
        int l = 0;

        while ((l = input.read(bs, READ_OFF, READ_LENGTH)) >= 0) {
            if (BUILD_STRING) {
                sb.append(new String(bs, READ_OFF, l));
            }
        }

        if (BUILD_STRING && OUT_STRING) {
            System.out.println(sb);
        }
    }

    static void testReaders() throws IOException {
        String s = "This is a senseless text to test readers.\n";

        for (int i = 0; i < 10; i++) {
            s = s + s; // repeated 16 times
        }

        char[] bytes = s.toCharArray();
        FastBufferWriter output = new FastBufferWriter(16);

        // write it several times.
        for (int i = 0; i < 4; i++) {
            output.write(bytes);
        }

        // write it one more time by one byte
        for (int i = 0; i < bytes.length; i++) {
            output.write(bytes[i]);
        }

        FastBufferReader input = new FastBufferReader(output.getFirstBuffer());
        StringBuffer sb = new StringBuffer();

        // use for reading unconvenient array length.
        char[] bs = new char[ARRAY_LENGTH];
        int l = 0;

        while ((l = input.read(bs, READ_OFF, READ_LENGTH)) >= 0) {
            if (BUILD_STRING) {
                sb.append(new String(bs, READ_OFF, l));
            }
        }

        if (BUILD_STRING && OUT_STRING) {
            System.out.println(sb);
        }
    }

    static void testStandardReaders() throws IOException {
        String s = "This is a senseless text to test readers.\n";

        for (int i = 0; i < 10; i++) {
            s = s + s; // repeated 16 times
        }

        char[] bytes = s.toCharArray();
        StringWriter output = new StringWriter(16);

        // write it several times.
        for (int i = 0; i < 4; i++) {
            output.write(bytes);
        }

        // write it one more time by one byte
        for (int i = 0; i < bytes.length; i++) {
            output.write(bytes[i]);
        }

        StringReader input = new StringReader(output.toString());
        StringBuffer sb = new StringBuffer();

        // use for reading unconvenient array length.
        char[] bs = new char[ARRAY_LENGTH];
        int l = 0;

        while ((l = input.read(bs, READ_OFF, READ_LENGTH)) >= 0) {
            if (BUILD_STRING) {
                sb.append(new String(bs, READ_OFF, l));
            }
        }

        if (BUILD_STRING && OUT_STRING) {
            System.out.println(sb);
        }
    }

    static void testTransitionFromWriterToStream() throws IOException {
        String s = "This is a senseless text to test transform from writer to stream.\n";

        for (int i = 0; i < 10; i++) {
            s = s + s; // repeated 16 times
        }

        char[] bytes = s.toCharArray();
        FastBufferWriter output = new FastBufferWriter(16);

        // write it several times.
        for (int i = 0; i < 4; i++) {
            output.write(bytes);
        }

        // write it one more time by one byte
        for (int i = 0; i < bytes.length; i++) {
            output.write(bytes[i]);
        }

        FastBufferOutputStream output2 = output.convertToOutputStream("UTF-8");
        FastBufferInputStream input = new FastBufferInputStream(output2.getFirstBuffer());
        StringBuffer sb = new StringBuffer();

        // use for reading unconvenient array length.
        byte[] bs = new byte[ARRAY_LENGTH];
        int l = 0;

        while ((l = input.read(bs, READ_OFF, READ_LENGTH)) >= 0) {
            if (BUILD_STRING) {
                sb.append(new String(bs, READ_OFF, l));
            }
        }

        if (BUILD_STRING && OUT_STRING) {
            System.out.println(sb);
        }
    }

    static void testStandardTransitionFromWriterToStream() throws IOException {
        String s = "This is a senseless text to test transform from writer to stream.\n";

        for (int i = 0; i < 10; i++) {
            s = s + s; // repeated 16 times
        }

        char[] bytes = s.toCharArray();
        StringWriter output = new StringWriter(16);

        // write it several times.
        for (int i = 0; i < 4; i++) {
            output.write(bytes);
        }

        // write it one more time by one byte
        for (int i = 0; i < bytes.length; i++) {
            output.write(bytes[i]);
        }

        String str = output.toString();
        ByteArrayInputStream input = new ByteArrayInputStream(str.getBytes());
        StringBuffer sb = new StringBuffer();

        // use for reading unconvenient array length.
        byte[] bs = new byte[ARRAY_LENGTH];
        int l = 0;

        while ((l = input.read(bs, READ_OFF, READ_LENGTH)) >= 0) {
            if (BUILD_STRING) {
                sb.append(new String(bs, READ_OFF, l));
            }
        }

        if (BUILD_STRING && OUT_STRING) {
            System.out.println(sb);
        }
    }

    static void testTransitionFromStreamToWriter() throws IOException {
        String s = "This is a senseless text to test transform from stream to writer.\n";

        for (int i = 0; i < 10; i++) {
            s = s + s; // repeated 16 times
        }

        byte[] bytes = s.getBytes();
        FastBufferOutputStream output = new FastBufferOutputStream(16);

        // write it several times.
        for (int i = 0; i < 4; i++) {
            output.write(bytes);
        }

        // write it one more time by one byte
        for (int i = 0; i < bytes.length; i++) {
            output.write(bytes[i]);
        }

        FastBufferWriter output2 = output.convertToWriter("UTF-8");
        FastBufferReader input = new FastBufferReader(output2.getFirstBuffer());
        StringBuffer sb = new StringBuffer();

        // use for reading unconvenient array length.
        char[] bs = new char[ARRAY_LENGTH];
        int l = 0;

        while ((l = input.read(bs, READ_OFF, READ_LENGTH)) >= 0) {
            if (BUILD_STRING) {
                sb.append(new String(bs, READ_OFF, l));
            }
        }

        if (BUILD_STRING && OUT_STRING) {
            System.out.println(sb);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        long t = System.currentTimeMillis();

        try {
            for (int i = 0; i < 10; i++) {

                // testStreams();
                // testStandardStreams();
                // testReaders();
                // testStandardReaders();
                // testTransitionFromWriterToStream();
                testStandardTransitionFromWriterToStream();

                // testTransitionFromStreamToWriter();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long dt = System.currentTimeMillis() - t;

        System.out.println(dt);
    }
}
