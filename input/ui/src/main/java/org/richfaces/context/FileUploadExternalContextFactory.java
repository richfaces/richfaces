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
package org.richfaces.context;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.richfaces.exception.FileUploadException;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.model.UploadedFile;
import org.richfaces.request.MultipartRequest;
import org.richfaces.request.MultipartRequest.ResponseState;
import org.richfaces.request.MultipartRequest25;
import org.richfaces.request.MultipartRequestParser;
import org.richfaces.request.ProgressControl;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;


/**
 * @author Nick Belaevski
 * 
 */
public class FileUploadExternalContextFactory extends ExternalContextFactory implements FacesWrapper<ExternalContextFactory> {

    private static class HttpSessionMap extends AbstractMap<String, Object> {

        private HttpSession session;
        
        public HttpSessionMap(HttpSession session) {
            super();
            this.session = session;
        }

        @Override
        public Object get(Object key) {
            return session.getAttribute((String) key);
        }
        
        @Override
        public Object put(String key, Object value) {
            session.setAttribute(key, value);
            return null;
        }

        @Override
        public Object remove(Object key) {
            session.removeAttribute((String) key);
            return null;
        }

        @Override
        public Set<java.util.Map.Entry<String, Object>> entrySet() {
            throw new UnsupportedOperationException();
        }
    }
    
    public static final String UID_KEY = "rf_fu_uid";

    private static final Logger LOGGER = RichfacesLogger.CONTEXT.getLogger();

    private static final Pattern AMPERSAND = Pattern.compile("&+");

    private ExternalContextFactory wrappedFactory;

    public FileUploadExternalContextFactory(ExternalContextFactory wrappedFactory) {
        super();
        
        //TODO Use ConfigurationServiceHelper to initialize InitParameters.
        this.wrappedFactory = wrappedFactory;
    }

    @Override
    public ExternalContextFactory getWrapped() {
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
    
    @Override
    public ExternalContext getExternalContext(Object context, Object request, Object response) throws FacesException {
        Object wrappedRequest = request;

        if (wrappedRequest instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) wrappedRequest;

            if (httpRequest.getContentType() != null && httpRequest.getContentType().startsWith("multipart/")) {
                String uid = getParameterValueFromQueryString(httpRequest.getQueryString(), UID_KEY);

                if (uid != null) {
                    long contentLength = Long.parseLong(httpRequest.getHeader("Content-Length"));
                    
                    Map<String,Object> contextMap = new HttpSessionMap(httpRequest.getSession());
                    
                    ProgressControl progressControl = new ProgressControl(contextMap, uid, contentLength);
                    
                    wrappedRequest = wrapMultipartRequestServlet25((ServletContext) context, httpRequest, uid, 
                        contentLength, progressControl);
                }
            }
        }

        return getWrapped().getExternalContext(context, wrappedRequest, response);
    }

    private MultipartRequest wrapMultipartRequestServlet25(ServletContext servletContext, HttpServletRequest request, 
        String uploadId, long contentLength, ProgressControl progressControl) {
        
        MultipartRequest multipartRequest;
        
        long maxRequestSize = getMaxRequestSize(servletContext);
        if (maxRequestSize == 0 || contentLength <= maxRequestSize) {
            MultipartRequestParser requestParser = new MultipartRequestParser(request, isCreateTempFiles(servletContext), 
                getTempFilesDirectory(servletContext), progressControl);
            
            ResponseState result = ResponseState.ok;
            
            try {
                requestParser.parse();
            } catch (FileUploadException e) {
                result = ResponseState.serverError;
            }
            
            multipartRequest = new MultipartRequest25(request, uploadId, progressControl, requestParser.getParameters(), 
                requestParser.getUploadedFiles(), result);
        } else {
            multipartRequest = new MultipartRequest25(request, uploadId, progressControl, LinkedListMultimap.<String, String>create(), 
                Lists.<UploadedFile>newArrayList(), ResponseState.sizeExceeded);
        }

        request.setAttribute(MultipartRequest.REQUEST_ATTRIBUTE_NAME, multipartRequest);
        
        return multipartRequest;
    }

}