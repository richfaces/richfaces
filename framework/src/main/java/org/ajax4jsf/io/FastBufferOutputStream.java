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
package org.ajax4jsf.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * Class for writing to chain of byte arrays extending OutputStream.
 *
 * @author glory
 */
public class FastBufferOutputStream extends OutputStream {
    /**
     * The beginning of the chain of byte arrays.
     */
    ByteBuffer firstBuffer;
    /**
     * Currently filled link of the chain of byte arrays.
     */
    ByteBuffer lastBuffer;
    /**
     * Total number of written bytes.
     */
    int length;

    /**
     * Creates instance of default initial capacity.
     */
    public FastBufferOutputStream() {
        this(256);
    }

    /**
     * Creates instance for an already existing chain of byte arrays.
     *
     * @param firstBuffer
     */
    public FastBufferOutputStream(ByteBuffer firstBuffer) {
        this.firstBuffer = firstBuffer;
        this.lastBuffer = firstBuffer;
    }

    /**
     * Creates instance with required initial capacity.
     *
     * @param initialSize
     */
    public FastBufferOutputStream(int initialSize) {
        this(new ByteBuffer(initialSize));
    }

    /**
     * @see java.io.OutputStream.write(int c)
     */
    public void write(int c) throws IOException {
        lastBuffer = lastBuffer.append((byte) c);
        length++;
    }

    /**
     * @see java.io.OutputStream.write(byte b[])
     */
    public void write(byte[] b) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        }

        int limit = b.length;

        length += limit;
        lastBuffer = lastBuffer.append(b, 0, limit);
    }

    /**
     * @see java.io.OutputStream.write(byte[] b, int off, int len)
     */
    public void write(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }

        lastBuffer = lastBuffer.append(b, off, len);
        length += len;
    }

    /**
     * Returns the total number of written bytes.
     *
     * @return
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the first link of the chain of byte arrays.
     *
     * @return
     */
    public ByteBuffer getFirstBuffer() {
        return firstBuffer;
    }

    public byte[] toByteArray() {
        ByteBuffer b = getFirstBuffer();

        if (b == null) {
            return new byte[0];
        }

        ByteBuffer l = b;

        while (l.getNext() != null) {
            l = l.getNext();
        }

        byte[] result = new byte[l.getTotalSize()];
        int index = 0;

        while (b != null) {
            int s = b.getUsedSize();

            System.arraycopy(b.getBytes(), 0, result, index, s);
            index += s;
            b = b.getNext();
        }

        return result;
    }

    /**
     * Writes all data written up to the moment to out.
     *
     * @param out
     * @throws IOException
     */
    public void writeTo(OutputStream out) throws IOException {
        ByteBuffer b = getFirstBuffer();

        while (b != null) {
            out.write(b.getBytes(), 0, b.getUsedSize());
            b = b.getNext();
        }
    }

    /**
     * Writes all data written up to the moment to out.
     *
     * @param out
     * @throws IOException
     */
    public void writeTo(Writer out, String encoding) throws IOException {
        ByteBuffer b = getFirstBuffer();

        while (b != null) {
            CharBuffer c = b.toCharBuffer(encoding);

            out.write(c.getChars(), 0, c.getUsedSize());
            b = b.getNext();
        }
    }

    /**
     * Returns instance of FastBufferWriter containing all data written to this output stream.
     *
     * @param encoding
     * @return
     * @throws UnsupportedEncodingException
     */
    public FastBufferWriter convertToWriter(String encoding) throws UnsupportedEncodingException {
        ByteBuffer c = firstBuffer;
        CharBuffer first = c.toCharBuffer(encoding);
        CharBuffer b = first;

        while (c != null) {
            c = c.getNext();

            if (c == null) {
                break;
            }

            CharBuffer n = c.toCharBuffer(encoding);

            b.setNext(n);
            b = n;
        }

        return new FastBufferWriter(first);
    }

    /**
     * Returns instance of FastBufferWriter containing all data written to this output stream.
     *
     * @return
     */
    public FastBufferWriter convertToWriter() {
        ByteBuffer c = firstBuffer;
        CharBuffer first = c.toCharBuffer();
        CharBuffer b = first;

        while (c != null) {
            c = c.getNext();

            if (c == null) {
                break;
            }

            CharBuffer n = c.toCharBuffer();

            b.setNext(n);
            b = n;
        }

        return new FastBufferWriter(first);
    }

    /**
     * Resets stream to empty state
     *
     * @since 3.3.0
     */
    public void reset() {
        this.firstBuffer.reset();
        this.lastBuffer = this.firstBuffer;
        this.length = 0;
    }
}
