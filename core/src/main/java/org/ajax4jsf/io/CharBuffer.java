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
package org.ajax4jsf.io;

import java.io.UnsupportedEncodingException;

/**
 * A single link in chain of char arrays.
 *
 * @author glory
 */
public class CharBuffer {
    private static final int MAX_WASTE = 16384;
    private static final int MIN_CACHE_SIZE = 64;
    /**
     * Length of char array.
     */
    private int cacheSize;
    /**
     * Stored characters
     */
    private char[] chars;
    private CharBuffer next;
    private CharBuffer prev;
    /**
     * number of characters stored in the array.
     */
    private int usedSize;

    /**
     * Creates instance of CharBuffer already filled by chars.
     *
     * @param chars
     */
    public CharBuffer(char[] chars) {
        this.chars = chars;
        usedSize = chars.length;
        cacheSize = usedSize;
    }

    /**
     * Creates instance of CharBuffer with char array of required length.
     *
     * @param cacheSize length of char array
     */
    public CharBuffer(int cacheSize) {
        if (cacheSize < MIN_CACHE_SIZE) {
            this.cacheSize = MIN_CACHE_SIZE;
        } else {
            this.cacheSize = cacheSize;
        }

        chars = new char[this.cacheSize];
        usedSize = 0;
    }

    /**
     * Appends character to array chars if there are unfilled positions in it. Otherwise creates next link in the chain, and
     * appends the character to it.
     *
     * @param c
     * @return instance of CharBuffer to which character was appended.
     */
    public CharBuffer append(char c) {
        if (next != null) {
            return next.append(c);
        }

        if (usedSize < cacheSize) {
            chars[usedSize] = c;
            usedSize++;

            return this;
        } else {
            next = new CharBuffer(cacheSize * 2);
            next.prev = this;

            return next.append(c);
        }
    }

    /**
     * Appends segment of a char array to array if there are unfilled positions in it. Otherwise creates next link in the chain,
     * and appends data to it.
     *
     * @param cs
     * @param off
     * @param len
     * @return instance of CharBuffer to which char array was appended.
     */
    public CharBuffer append(char[] cs, int off, int len) {
        if (next != null) {
            return next.append(cs, off, len);
        }

        if (len + usedSize <= cacheSize) {
            System.arraycopy(cs, off, chars, usedSize, len);
            usedSize += len;

            return this;
        }

        int av = cacheSize - usedSize;

        if (av > 0) {
            System.arraycopy(cs, off, chars, usedSize, av);
            usedSize += av;
            off += av;
            len -= av;
        }

        next = new CharBuffer(cacheSize * 2);
        next.prev = this;

        return next.append(cs, off, len);
    }

    /**
     * Returns stored char array.
     *
     * @return stored char array
     */
    public char[] getChars() {
        return chars;
    }

    /**
     * Returns character at index. No check is fulfilled to provide high speed.
     *
     * @param index
     * @return character at index
     */
    public char getCharAt(int index) {
        return chars[index];
    }

    /**
     * Returns actual number of characters stored in this link.
     *
     * @return actual number of characters stored in this link
     */
    public int getUsedSize() {
        return usedSize;
    }

    /**
     * Returns capacity of this link.
     *
     * @return capacity of this link
     */
    public int getCacheSize() {
        return cacheSize;
    }

    /**
     * Returns total number of characters stored in this link and all its predecessors.
     *
     * @return total number of characters stored in this link and all its predecessors
     */
    public int getTotalSize() {
        return (prev == null) ? usedSize : prev.getTotalSize() + usedSize;
    }

    /**
     * Returns the previous link in the chain.
     *
     * @return the previous link in the chain
     */
    public CharBuffer getPrevious() {
        return prev;
    }

    /**
     * Returns the next link in the chain.
     *
     * @return the next link in the chain
     */
    public CharBuffer getNext() {
        return next;
    }

    /**
     * Sets the next link in the chain.
     *
     * @param b
     */
    public void setNext(CharBuffer b) {
        next = b;

        if (b != null) {
            b.prev = this;
        }
    }

    /**
     * Transforms this instance to instance of ByteBuffer (a link of chain of byte arrays).
     *
     * @param encoding
     * @return link of chain of byte arrays
     * @throws UnsupportedEncodingException
     */
    public ByteBuffer toByteBuffer(String encoding) throws UnsupportedEncodingException {
        byte[] bs = new String(chars, 0, usedSize).getBytes(encoding);

        return new ByteBuffer(bs);
    }

    /**
     * Transforms this instance to instance of ByteBuffer (a link of chain of byte arrays).
     *
     * @return link of chain of byte arrays
     */
    public ByteBuffer toByteBuffer() {
        byte[] bs = new String(chars, 0, usedSize).getBytes();

        return new ByteBuffer(bs);
    }

    /**
     * Resets this char buffer to empty state
     *
     * @since 3.3.0
     */
    public void reset() {
        usedSize = 0;
        next = null;
        prev = null;
    }

    /**
     * @since 4.0
     */
    public void compact() {
        if (chars.length - usedSize > MAX_WASTE) {
            char[] cs = new char[usedSize];

            System.arraycopy(chars, 0, cs, 0, usedSize);
            this.chars = cs;
            this.cacheSize = cs.length;
        }

        if (next != null) {
            next.compact();
        }
    }
}
