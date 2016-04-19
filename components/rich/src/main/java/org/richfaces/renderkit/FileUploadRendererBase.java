/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.richfaces.component.AbstractFileUpload;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.exception.FileUploadException;
import org.richfaces.model.UploadedFile;
import org.richfaces.request.MultipartRequest;
import org.richfaces.request.MultipartRequest25;
import org.richfaces.request.MultipartRequestParser;
import org.richfaces.request.UploadedFile30;

/**
 * @author Konstantin Mishin
 * @author Nick Belaevski
 * @author Lukas Fryc
 * @author Simone Cinti
 * @author Michal Petrov
 */
public class FileUploadRendererBase extends RendererBase {

    private boolean isCreateTempFiles(ServletContext servletContext) {
        String param = servletContext.getInitParameter("org.richfaces.fileUpload.createTempFiles");
        if (param != null) {
            return Boolean.parseBoolean(param);
        }

        return true;
    }

    private String getTempFilesDirectory(ServletContext servletContext) {
        String result = servletContext.getInitParameter("org.richfaces.fileUpload.tempFilesDirectory");
        if (result == null) {
            File servletTempDir = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            if (servletTempDir != null) {
                result = servletTempDir.getAbsolutePath();
            }
        }
        if (result == null) {
            result = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
        }

        return result;
    }

    private Iterable<UploadedFile> initializeUploadedFiles(ExternalContext context, HttpServletRequest request, String uploadId) {
        try {
            List<UploadedFile> files = new LinkedList<UploadedFile>();

            // check if Servlet 3.0+ is being used
            if (request.getParts().size() > 0) {
                Collection<Part> parts = request.getParts();

                for (Part part : parts) {
                    String contentDisposition = part.getHeader("Content-Disposition");
                    String filename = MultipartRequestParser.parseFileName(contentDisposition);
                    if (filename != null) {
                        // RF-14092: request encoded in UTF8 (XHR2 default) will be parsed as Latin1 (HTTP default)
                        // on Tomcat it can be any encoding (org.apache.catalina.filters.SetCharacterEncodingFilter)
                        String encoding = request.getCharacterEncoding();
                        if (encoding == null) {
                            encoding = "iso-8859-1";
                        }
                        files
                            .add(new UploadedFile30(part.getName(), new String(filename.getBytes(encoding), "utf-8"), part));
                    }
                }
            } else {
                boolean createTempFiles = isCreateTempFiles(request.getServletContext());
                String tempFilesDirectory = getTempFilesDirectory(request.getServletContext());
                MultipartRequestParser requestParser = new MultipartRequestParser(request, createTempFiles, tempFilesDirectory);
                MultipartRequest multipartRequest = new MultipartRequest25(request, uploadId, requestParser);

                files = (List<UploadedFile>) multipartRequest.getUploadedFiles();
            }
            return files;
        } catch (Exception e) {
            context.setResponseStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new FileUploadException("couldn't parse request parts", e);
        }
    }

    public static long getMaxRequestSize(ServletContext servletContext) {
        String param = servletContext.getInitParameter("org.richfaces.fileUpload.maxRequestSize");
        if (param != null) {
            return Long.parseLong(param);
        }

        return 0;
    }

    public static long getMaxRequestSize() {
        return getMaxRequestSize((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext());
    }

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        final AbstractFileUpload fileUpload = (AbstractFileUpload) component;
        final ExternalContext externalContext = context.getExternalContext();

        Object request = externalContext.getRequest();

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            if (httpRequest.getContentType() != null && httpRequest.getContentType().startsWith("multipart/")) {
                String uid = MultipartRequestParser.getParameterValueFromQueryString(httpRequest.getQueryString());

                if (uid != null) {
                    long contentLength = Long.parseLong(httpRequest.getHeader("Content-Length"));

                    long maxRequestSize = fileUpload.getMaxFileSize() != 0 ? fileUpload.getMaxFileSize()
                        : getMaxRequestSize(httpRequest.getServletContext());

                    if (maxRequestSize != 0 && contentLength > maxRequestSize) {
                        externalContext.setResponseStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
                        return;
                    }

                    Iterable<UploadedFile> uploadedFiles = initializeUploadedFiles(externalContext, httpRequest, uid);

                    for (UploadedFile file : uploadedFiles) {
                        if (fileUpload.acceptsFile(file)) {
                            fileUpload.queueEvent(new FileUploadEvent(fileUpload, file));
                        }
                    }
                }
            }
        }
    }
}