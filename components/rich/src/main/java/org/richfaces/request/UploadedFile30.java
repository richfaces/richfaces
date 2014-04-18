/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and individual contributors
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
import java.util.Collection;

import javax.servlet.http.Part;

import org.richfaces.model.UploadedFile;

/**
 * {@link UploadedFile} that delegates calls to instance of {@link Part} (from Servlet 3.0 API)
 *
 * @author Lukas Fryc
 */
public class UploadedFile30 extends BaseUploadedFile {

    private Part part;
    private String filename;

    public UploadedFile30(String parameterName, String filename, Part part) {
        super(parameterName);
        this.part = part;
        this.filename = filename;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return part.getInputStream();
    }

    @Override
    public String getName() {
        return filename;
    }

    @Override
    public long getSize() {
        return part.getSize();
    }

    @Override
    public void delete() throws IOException {
        part.delete();

    }

    @Override
    public void write(String fileName) throws IOException {
        part.write(fileName);
    }

    @Override
    public String getHeader(String headerName) {
        return part.getHeader(headerName);
    }

    @Override
    public Collection<String> getHeaderNames() {
        return part.getHeaderNames();
    }

    @Override
    public Collection<String> getHeaders(String headerName) {
        return part.getHeaders(headerName);
    }

}
