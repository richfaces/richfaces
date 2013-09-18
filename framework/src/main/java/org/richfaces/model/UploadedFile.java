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
package org.richfaces.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.richfaces.exception.FileUploadException;

/**
 * @author Konstantin Mishin
 *
 */
public interface UploadedFile {
    String getContentType();

    byte[] getData() throws FileUploadException;

    InputStream getInputStream() throws IOException;

    String getName();

    long getSize();

    void delete() throws IOException;

    void write(String fileName) throws IOException;

    String getHeader(String headerName);

    Collection<String> getHeaderNames();

    Collection<String> getHeaders(String headerName);

    String getParameterName();
}
