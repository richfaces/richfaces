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

import junit.framework.TestCase;

import org.ajax4jsf.io.ByteBuffer;
import org.ajax4jsf.io.FastBufferOutputStream;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public class FastBufferOutputStreamTest extends TestCase {
    /**
     * Test method for {@link org.ajax4jsf.io.FastBufferOutputStream#reset()}.
     */
    public void testResetOneBuffer() throws Exception {
        FastBufferOutputStream stream = new FastBufferOutputStream(256);

        for (int i = 0; i < 255; i++) {
            stream.write(i);
        }

        assertEquals(255, stream.getLength());

        ByteBuffer firstBuffer = stream.getFirstBuffer();

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
     * Test method for {@link org.ajax4jsf.io.FastBufferOutputStream#reset()}.
     */
    public void testResetTwoBuffers() throws Exception {
        FastBufferOutputStream stream = new FastBufferOutputStream(256);

        for (int i = 0; i < 257; i++) {
            stream.write(i);
        }

        assertEquals(257, stream.getLength());

        ByteBuffer firstBuffer = stream.getFirstBuffer();

        assertNotNull(firstBuffer.getNext());
        assertNull(firstBuffer.getPrevious());
        stream.reset();
        assertEquals(0, stream.getLength());
        firstBuffer = stream.getFirstBuffer();
        assertEquals(0, firstBuffer.getUsedSize());
        assertNull(firstBuffer.getNext());
        assertNull(firstBuffer.getPrevious());
    }
}
