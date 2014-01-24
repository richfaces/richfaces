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
package org.richfaces.fileUpload;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContextWrapper;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.ServletVersion;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.request.MultipartRequest;
import org.richfaces.request.MultipartRequest25;
import org.richfaces.request.MultipartRequest30;
import org.richfaces.request.MultipartRequestParser;
import org.richfaces.request.MultipartRequestSizeExceeded;
import org.richfaces.request.ProgressControl;

/**
 * @author Nick Belaevski
 *
 */
public class FileUploadFacesContextFactory extends FacesContextFactory implements FacesWrapper<FacesContextFactory> {
    private static final class FileUploadFacesContext extends FacesContextWrapper {
        private FacesContext facesContext;

        public FileUploadFacesContext(FacesContext facesContext) {
            super();
            this.facesContext = facesContext;
        }

        @Override
        public FacesContext getWrapped() {
            return facesContext;
        }

        @Override
        public void release() {
            MultipartRequest multipartRequest = (MultipartRequest) getExternalContext().getRequestMap().get(
                MultipartRequest.REQUEST_ATTRIBUTE_NAME);

            if (multipartRequest != null) {
                multipartRequest.release();
            }

            super.release();
        }
    }

    public static final String UID_KEY = "rf_fu_uid";
    private static final Logger LOGGER = RichfacesLogger.CONTEXT.getLogger();
    private static final Pattern AMPERSAND = Pattern.compile("&+");
    private FacesContextFactory wrappedFactory;

    public FileUploadFacesContextFactory(FacesContextFactory wrappedFactory) {
        super();
        this.wrappedFactory = wrappedFactory;
    }

    @Override
    public FacesContextFactory getWrapped() {
        return wrappedFactory;
    }

    private String getParameterValueFromQueryString(String queryString, String paramName) {
        if (queryString != null) {
            String[] nvPairs = AMPERSAND.split(queryString);
            for (String nvPair : nvPairs) {
                if (nvPair.length() == 0) {
                    continue;
                }

                int eqIdx = nvPair.indexOf('=');
                if (eqIdx >= 0) {
                    try {
                        String name = URLDecoder.decode(nvPair.substring(0, eqIdx), "UTF-8");

                        if (paramName.equals(name)) {
                            return URLDecoder.decode(nvPair.substring(eqIdx + 1), "UTF-8");
                        }
                    } catch (UnsupportedEncodingException e) {
                        // log warning and skip this parameter
                        LOGGER.debug(e.getMessage(), e);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public FacesContext getFacesContext(Object context, Object request, Object response, Lifecycle lifecycle)
        throws FacesException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            if (httpRequest.getContentType() != null && httpRequest.getContentType().startsWith("multipart/")) {
                String uid = getParameterValueFromQueryString(httpRequest.getQueryString(), UID_KEY);

                if (uid != null) {
                    long contentLength = Long.parseLong(httpRequest.getHeader("Content-Length"));

                    ProgressControl progressControl = new ProgressControl(uid, contentLength);



                    HttpServletRequest wrappedRequest;
                    if (ServletVersion.getCurrent().isCompliantWith(ServletVersion.SERVLET_3_0)) {
                        wrappedRequest = wrapMultipartRequestServlet30((ServletContext) context, httpRequest, uid,
                                contentLength, progressControl);
                    } else {
                        wrappedRequest = wrapMultipartRequestServlet25((ServletContext) context, httpRequest,
                            uid, contentLength, progressControl);
                    }

                    FacesContext facesContext = wrappedFactory.getFacesContext(context, wrappedRequest, response, lifecycle);
                    progressControl.setContextMap(facesContext.getExternalContext().getSessionMap());
                    return new FileUploadFacesContext(facesContext);
                }
            }
        }

        return wrappedFactory.getFacesContext(context, request, response, lifecycle);
    }

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

    private long getMaxRequestSize(ServletContext servletContext) {
        String param = servletContext.getInitParameter("org.richfaces.fileUpload.maxRequestSize");
        if (param != null) {
            return Long.parseLong(param);
        }

        return 0;
    }

    private HttpServletRequest wrapMultipartRequestServlet25(ServletContext servletContext, HttpServletRequest request,
        String uploadId, long contentLength, ProgressControl progressControl) {

        HttpServletRequest multipartRequest;

        long maxRequestSize = getMaxRequestSize(servletContext);
        if (maxRequestSize == 0 || contentLength <= maxRequestSize) {
            boolean createTempFiles = isCreateTempFiles(servletContext);
            String tempFilesDirectory = getTempFilesDirectory(servletContext);

            MultipartRequestParser requestParser = new MultipartRequestParser(request, createTempFiles, tempFilesDirectory,
                progressControl);

            multipartRequest = new MultipartRequest25(request, uploadId, progressControl, requestParser);
        } else {
            multipartRequest = new MultipartRequestSizeExceeded(request, uploadId, progressControl);
        }

        request.setAttribute(MultipartRequest.REQUEST_ATTRIBUTE_NAME, multipartRequest);

        return multipartRequest;
    }

    private HttpServletRequest wrapMultipartRequestServlet30(ServletContext servletContext, HttpServletRequest request,
            String uploadId, long contentLength, ProgressControl progressControl) {

        HttpServletRequest multipartRequest;

        long maxRequestSize = getMaxRequestSize(servletContext);
        if (maxRequestSize == 0 || contentLength <= maxRequestSize) {
            multipartRequest = new MultipartRequest30(request, uploadId, progressControl);


        } else {
            multipartRequest = new MultipartRequestSizeExceeded(request, uploadId, progressControl);
        }

        request.setAttribute(MultipartRequest.REQUEST_ATTRIBUTE_NAME, multipartRequest);

        return multipartRequest;
    }
}
