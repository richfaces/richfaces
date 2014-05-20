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

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski
 *
 */
abstract class BaseMultipartRequest extends HttpServletRequestWrapper implements MultipartRequest {
    static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    static final Map<String, String> HEADERS_MAP;

    static {
        HEADERS_MAP = Maps.newHashMap();

        HEADERS_MAP.put("accept", "text/html");
        HEADERS_MAP.put("faces-request", "partial/ajax");
        HEADERS_MAP.put("content-type", CONTENT_TYPE);
    }

    private String uploadId;

    /**
     * @param request
     */
    public BaseMultipartRequest(HttpServletRequest request, String uploadId) {
        super(request);
        this.uploadId = uploadId;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void release() {
    }

    @Override
    public String getHeader(String name) {
        String headerValue = HEADERS_MAP.get(name.toLowerCase(Locale.US));

        if (!Strings.isNullOrEmpty(headerValue)) {
            return headerValue;
        }

        return super.getHeader(name);
    }

    @Override
    public String getContentType() {
        return getHeader("Content-Type");
    }
}
