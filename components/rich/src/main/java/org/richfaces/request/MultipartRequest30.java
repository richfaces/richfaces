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

import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.richfaces.exception.FileUploadException;
import org.richfaces.model.UploadedFile;

/**
 * Provides a way of parsing files from multipart/form-data requests based on Servlet 3.0 {@link Part} interface.
 *
 * @author Lukas Fryc
 */
public class MultipartRequest30 extends BaseMultipartRequest {

    private ResponseState state = ResponseState.ok;
    private Iterable<UploadedFile> uploadedFiles;

    public MultipartRequest30(HttpServletRequest request, String uploadId, ProgressControl progressControl) {
        super(request, uploadId, progressControl);

        uploadedFiles = initializeUploadedFiles();
    }

    /**
     * Iterates through request parts and takes wraps the parts which has filename parameter in {@link UploadedFile} interface in Content-Disposition header.
     */
    private Iterable<UploadedFile> initializeUploadedFiles() {
        try {
            Collection<Part> parts = super.getParts();
            Collection<UploadedFile> files = new LinkedList<>();
            for (Part part : parts) {
                String contentDisposition = part.getHeader("Content-Disposition");
                String filename = MultipartRequestParser.parseFileName(contentDisposition);
                if (filename != null) {
                    files.add(new UploadedFile30(part.getName(), filename, part));
                }
            }
            return files;
        } catch (Exception e) {
            state = ResponseState.serverError;
            throw new FileUploadException("couldn't parse request parts", e);
        }
    }

    @Override
    public ResponseState getResponseState() {
        return state;
    }

    @Override
    public Iterable<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }
}
