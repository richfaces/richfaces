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
package org.ajax4jsf.javascript;

import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;

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
