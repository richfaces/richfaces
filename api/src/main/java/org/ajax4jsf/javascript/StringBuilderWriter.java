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

import java.io.IOException;
import java.io.Writer;

/**
 * @author Nick Belaevski
 * @since 3.3.2
 */
final class StringBuilderWriter extends Writer {
    private StringBuilder builder;

    public StringBuilderWriter(StringBuilder builder) {
        super();
        this.builder = builder;
    }

    /**
     * Closing this writer doesn't have any effect
     */
    @Override
    public void close() throws IOException {

        // do nothing
    }

    /*
     *  (non-Javadoc)
     * @see java.io.Writer#flush()
     */
    @Override
    public void flush() throws IOException {

        // do nothing
    }

    /*
     *  (non-Javadoc)
     * @see java.io.Writer#write(char[], int, int)
     */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        builder.append(cbuf, off, len);
    }

    @Override
    public void write(char[] cbuf) throws IOException {
        builder.append(cbuf);
    }

    @Override
    public void write(String str) throws IOException {
        builder.append(str);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        builder.append(str, off, off + len);
    }

    @Override
    public void write(int c) throws IOException {
        builder.append((char) c);
    }
}
