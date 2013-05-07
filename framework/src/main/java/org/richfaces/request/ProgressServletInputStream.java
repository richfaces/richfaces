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
package org.richfaces.request;

import java.io.IOException;

import javax.servlet.ServletInputStream;

class ProgressServletInputStream extends ServletInputStream {
    private ServletInputStream wrappedStream;
    private ProgressControl progressControl;

    protected ProgressServletInputStream(ServletInputStream wrappedStream, ProgressControl progressControl) {
        this.wrappedStream = wrappedStream;
        this.progressControl = progressControl;
    }

    @Override
    public int read() throws IOException {
        int read = wrappedStream.read();
        if (read >= 0) {
            progressControl.advance(1);
        }
        return read;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int read = wrappedStream.read(b);
        if (read > 0) {
            progressControl.advance(read);
        }
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = wrappedStream.read(b, off, len);
        if (read > 0) {
            progressControl.advance(read);
        }
        return read;
    }

    @Override
    public int readLine(byte[] b, int off, int len) throws IOException {
        int read = wrappedStream.readLine(b, off, len);
        if (read > 0) {
            progressControl.advance(read);
        }
        return read;
    }

    public long skip(long n) throws IOException {
        return wrappedStream.skip(n);
    }

    public int available() throws IOException {
        return wrappedStream.available();
    }

    public void close() throws IOException {
        wrappedStream.close();
    }

    public void mark(int readlimit) {
        wrappedStream.mark(readlimit);
    }

    public void reset() throws IOException {
        wrappedStream.reset();
    }

    public boolean markSupported() {
        return wrappedStream.markSupported();
    }
}