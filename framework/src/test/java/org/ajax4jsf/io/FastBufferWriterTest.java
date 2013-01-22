/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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
package org.ajax4jsf.io;

import junit.framework.TestCase;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public class FastBufferWriterTest extends TestCase {
    /**
     * Test method for {@link org.ajax4jsf.io.FastBufferWriter#reset()}.
     */
    public void testResetOneBuffer() throws Exception {
        FastBufferWriter stream = new FastBufferWriter(256);

        for (int i = 0; i < 255; i++) {
            stream.write(i);
        }

        assertEquals(255, stream.getLength());

        CharBuffer firstBuffer = stream.getFirstBuffer();

        assertNull(firstBuffer.getNext());
        assertNull(firstBuffer.getPrevious());
        stream.reset();
        assertEquals(0, stream.getLength());
        firstBuffer = stream.getFirstBuffer();
        assertEquals(0, firstBuffer.getUsedSize());
        assertNull(firstBuffer.getNext());
        assertNull(firstBuffer.getPrevious());
    }

    /**
     * Test method for {@link org.ajax4jsf.io.FastBufferWriter#reset()}.
     */
    public void testResetTwoBuffers() throws Exception {
        FastBufferWriter stream = new FastBufferWriter(256);

        for (int i = 0; i < 257; i++) {
            stream.write(i);
        }

        assertEquals(257, stream.getLength());

        CharBuffer firstBuffer = stream.getFirstBuffer();

        assertNotNull(firstBuffer.getNext());
        assertNull(firstBuffer.getPrevious());
        stream.reset();
        assertEquals(0, stream.getLength());
        firstBuffer = stream.getFirstBuffer();
        assertEquals(0, firstBuffer.getUsedSize());
        assertNull(firstBuffer.getNext());
        assertNull(firstBuffer.getPrevious());
    }

    public void testCompact() throws Exception {
        int itemsTowWrite = 16384 + 16000;
        FastBufferWriter writer = new FastBufferWriter(16384);

        for (int i = 0; i < itemsTowWrite; i++) {
            writer.write(i);
        }

        writer.close();

        CharBuffer firstBuffer = writer.getFirstBuffer();

        assertNotNull(firstBuffer);

        CharBuffer nextBuffer = firstBuffer.getNext();

        assertNotNull(nextBuffer);
        assertNull(nextBuffer.getNext());
        assertTrue(firstBuffer.getUsedSize() == firstBuffer.getCacheSize());
        assertTrue(nextBuffer.getUsedSize() < nextBuffer.getCacheSize());
        firstBuffer.compact();
        assertTrue(firstBuffer.getUsedSize() == firstBuffer.getCacheSize());
        assertTrue(nextBuffer.getUsedSize() == nextBuffer.getCacheSize());

        FastBufferReader reader = new FastBufferReader(firstBuffer);

        for (int i = 0; i < itemsTowWrite; i++) {
            assertEquals(i, reader.read());
        }

        reader.close();
    }
}
