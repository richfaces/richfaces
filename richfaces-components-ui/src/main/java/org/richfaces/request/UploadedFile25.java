/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

/**
 * @author Nick Belaevski
 *
 */
public class UploadedFile25 extends BaseUploadedFile {
    private FileUploadResource uploadResource;
    private Multimap<String, String> headersMap;

    public UploadedFile25(String parameterName, FileUploadResource uploadResource, Multimap<String, String> headersMap) {
        super(parameterName);

        this.uploadResource = uploadResource;
        this.headersMap = headersMap;
    }

    public InputStream getInputStream() throws IOException {
        return uploadResource.getInputStream();
    }

    public void delete() throws IOException {
        uploadResource.delete();
    }

    public void write(String fileName) throws IOException {
        uploadResource.write(fileName);
    }

    public String getHeader(String headerName) {
        String lcHeaderName = headerName.toLowerCase(Locale.US);
        Collection<String> headers = headersMap.get(lcHeaderName);

        if (headers.isEmpty()) {
            return null;
        }

        return Iterables.get(headers, 0);
    }

    public Collection<String> getHeaderNames() {
        return new HashSet<String>(headersMap.keySet());
    }

    public Collection<String> getHeaders(String headerName) {
        String lcHeaderName = headerName.toLowerCase(Locale.US);
        Collection<String> headers = headersMap.get(lcHeaderName);

        return new ArrayList<String>(headers);
    }

    public long getSize() {
        return uploadResource.getSize();
    }
}
