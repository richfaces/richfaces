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
import java.io.InputStream;

import com.google.common.io.Files;
import org.ajax4jsf.io.ByteBuffer;
import org.ajax4jsf.io.FastBufferInputStream;

/**
 * @author Nick Belaevski
 */
class FileUploadMemoryResource extends FileUploadResource {
    private ByteBuffer buffer;

    public FileUploadMemoryResource(String name, String uploadLocation) {
        super(name, uploadLocation);
    }

    private void checkNotDeleted() throws IOException {
        if (buffer == null) {
            throw new IOException("Resource has been deleted");
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        checkNotDeleted();

        return new FastBufferInputStream(buffer);
    }

    @Override
    public long getSize() {
        return buffer.getLast().getTotalSize();
    }

    @Override
    public void write(String fileName) throws IOException {
        checkNotDeleted();
        Files.asByteSink(getOutputFile(fileName)).writeFrom(getInputStream());
    }

    @Override
    public void delete() throws IOException {
        buffer = null;
    }

    public void handle(byte[] bytes, int length) throws IOException {
        buffer.append(bytes, 0, length);
    }

    public void create() {
        buffer = new ByteBuffer(128);
    }

    public void complete() {
        buffer.compact();
    }
}
