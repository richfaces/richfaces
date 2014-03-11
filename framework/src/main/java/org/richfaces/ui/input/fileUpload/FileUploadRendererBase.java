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
package org.richfaces.ui.input.fileUpload;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedList;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.richfaces.exception.FileUploadException;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.model.UploadedFile;
import org.richfaces.renderkit.RendererBase;
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

    private Iterable<UploadedFile> initializeUploadedFiles(ExternalContext context, HttpServletRequest request) {
        try {
            Collection<Part> parts = request.getParts();
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
            context.setResponseStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new FileUploadException("couldn't parse request parts", e);
        }
    }

    private long getMaxRequestSize(ServletContext servletContext) {
        String param = servletContext.getInitParameter("org.richfaces.fileUpload.maxRequestSize");
        if (param != null) {
            return Long.parseLong(param);
        }

        return 0;
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

                    long maxRequestSize = getMaxRequestSize(httpRequest.getServletContext());

                    if (maxRequestSize != 0 && contentLength > maxRequestSize) {
                        externalContext.setResponseStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
                        return;
                    }

                    Iterable<UploadedFile> uploadedFiles = initializeUploadedFiles(externalContext, httpRequest);

                    for (UploadedFile file : uploadedFiles) {
                        if (fileUpload.acceptsFile(file)) {
                            fileUpload.queueEvent(new FileUploadEvent(fileUpload, file));
                        }
                    }

                    externalContext.setResponseStatus(HttpServletResponse.SC_OK);
                    externalContext.setResponseContentType("text/html");
                    try {
                        Writer writer = externalContext.getResponseOutputWriter();
                        writer.write("<html id=\"" + MultipartRequestParser.UID_KEY + uid
                            + ":" + HttpServletResponse.SC_OK + "\"/>");
                        writer.close();
                    } catch (IOException e) {
                        RichfacesLogger.CONTEXT.getLogger().error(e.getMessage(), e);
                    }
                }
            }
        }
    }
}