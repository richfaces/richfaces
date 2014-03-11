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

import org.richfaces.exception.FileUploadException;
import org.richfaces.model.UploadedFile;
import org.richfaces.util.StreamUtils;

/**
 * @author Nick Belaevski
 */
public abstract class BaseUploadedFile implements UploadedFile {
    private String parameterName;

    public BaseUploadedFile(String parameterName) {
        super();
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getName() {
        return getHeader(MultipartRequestParser.PARAM_FILENAME);
    }

    public String getContentType() {
        return getHeader(MultipartRequestParser.PARAM_CONTENT_TYPE);
    }

    public byte[] getData() {
        long size = getSize();

        if (size > Integer.MAX_VALUE) {
            throw new FileUploadException("Resource content is too long to be allocated as byte[]");
        }

        InputStream is = null;
        try {
            is = getInputStream();
            return StreamUtils.toByteArray(is);
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage(), e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // Swallow
            }
        }
    }

    @Override
    public String getFileExtension() {
        String name = this.getName();
        if (name != null) {
            int i = name.lastIndexOf('.');
            if (i > 0) {
                return name.substring(i + 1);
            }
        }
        return "";
    }
}
