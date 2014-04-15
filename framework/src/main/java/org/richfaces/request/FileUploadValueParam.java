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
import java.io.UnsupportedEncodingException;

import javax.faces.FacesException;

import org.ajax4jsf.io.ByteBuffer;

/**
 * @author Nick Belaevski
 *
 */
final class FileUploadValueParam implements FileUploadParam {
    private ByteBuffer buffer;
    private String name;
    private String encoding;
    private String value;

    public FileUploadValueParam(String name, String encoding) {
        super();

        this.name = name;
        this.encoding = encoding;
    }

    private byte[] getBufferBytes() {
        byte[] bs = new byte[buffer.getLast().getTotalSize()];

        int pos = 0;
        ByteBuffer currentBuffer = buffer;
        while (currentBuffer != null) {
            System.arraycopy(currentBuffer.getBytes(), 0, bs, pos, currentBuffer.getUsedSize());
            pos += currentBuffer.getUsedSize();
            currentBuffer = currentBuffer.getNext();
        }

        return bs;
    }

    public void handle(byte[] bytes, int length) throws IOException {
        buffer.append(bytes, 0, length);
    }

    public void create() throws IOException {
        buffer = new ByteBuffer(256);
    }

    public void complete() {
        byte[] bytes = getBufferBytes();
        buffer = null;

        try {
            value = (encoding != null) ? new String(bytes, encoding) : new String(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new FacesException(e.getMessage(), e);
        }
    }

    public String getName() {
        return name;
    }

    public boolean isFileParam() {
        return false;
    }

    public String getValue() {
        return value;
    }

    public FileUploadResource getResource() {
        return null;
    }
}
