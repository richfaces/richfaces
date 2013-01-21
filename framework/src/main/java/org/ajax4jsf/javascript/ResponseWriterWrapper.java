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

import javax.faces.context.ResponseWriter;

final class ResponseWriterWrapper extends Writer {
    private static final int BUFFER_SIZE = 1024;
    private final ResponseWriter responseWriter;
    private char[] writeBuffer;

    ResponseWriterWrapper(ResponseWriter responseWriter) {
        this.responseWriter = responseWriter;
    }

    @Override
    public void close() throws IOException {
        responseWriter.close();
    }

    @Override
    public void flush() throws IOException {
        responseWriter.flush();
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        responseWriter.writeText(cbuf, off, len);
    }

    @Override
    public void write(String str) throws IOException {
        responseWriter.writeText(str, null);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        char[] cbuf;

        if (len <= BUFFER_SIZE) {
            if (writeBuffer == null) {
                writeBuffer = new char[BUFFER_SIZE];
            }

            cbuf = writeBuffer;
        } else {
            cbuf = new char[len];
        }

        str.getChars(off, off + len, cbuf, 0);
        responseWriter.writeText(cbuf, 0, len);
    }

    @Override
    public void write(int c) throws IOException {
        if (writeBuffer == null) {
            writeBuffer = new char[BUFFER_SIZE];
        }

        writeBuffer[0] = (char) c;
        responseWriter.writeText(writeBuffer, 0, 1);
    }
}
