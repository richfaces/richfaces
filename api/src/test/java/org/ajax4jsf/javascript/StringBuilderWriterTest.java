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



package org.ajax4jsf.javascript;

import java.io.Writer;

import junit.framework.TestCase;

/**
 * @author Nick Belaevski
 * @since 3.3.2
 */
public class StringBuilderWriterTest extends TestCase {
    private StringBuilder builder;
    private Writer writer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.builder = new StringBuilder();
        this.writer = new StringBuilderWriter(this.builder);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.builder = null;
        this.writer = null;
    }

    public void testWrite() throws Exception {
        writer.write(new char[] {'a', 'b'});
        assertEquals("ab", builder.toString());
    }

    public void testWrite2() throws Exception {
        writer.write(0x12345678);

        String s = builder.toString();

        assertEquals(1, s.length());
        assertEquals(0x5678, s.charAt(0));
    }

    public void testWrite3() throws Exception {
        writer.write("test");
        assertEquals("test", builder.toString());
    }

    public void testWrite4() throws Exception {
        writer.write("abcd".toCharArray(), 1, 2);
        assertEquals("bc", builder.toString());
        writer.write("efgh".toCharArray(), 0, 3);
        assertEquals("bcefg", builder.toString());
        writer.write("ijkl".toCharArray(), 2, 2);
        assertEquals("bcefgkl", builder.toString());
    }

    public void testWrite5() throws Exception {
        writer.write("abcd", 1, 2);
        assertEquals("bc", builder.toString());
        writer.write("efgh", 0, 3);
        assertEquals("bcefg", builder.toString());
        writer.write("ijklm", 2, 3);
        assertEquals("bcefgklm", builder.toString());
    }

    public void testFlush() throws Exception {
        writer.flush();
    }

    public void testClose() throws Exception {
        writer.close();
    }
}
