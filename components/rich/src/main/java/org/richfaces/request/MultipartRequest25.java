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
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.richfaces.exception.FileUploadException;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.model.UploadedFile;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class MultipartRequest25 extends BaseMultipartRequest {
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    private static final Function<Collection<String>, Object> MULTIMAP_VALUE_TRANSFORMER = new Function<Collection<String>, Object>() {
        public Object apply(Collection<String> input) {
            if (input.isEmpty()) {
                return null;
            }

            if (input.size() == 1) {
                return Iterables.get(input, 0);
            }

            return input.toArray(new String[input.size()]);
        }
    };
    private MultipartRequestParser requestParser;
    private ResponseState responseState;
    private Iterable<UploadedFile> uploadedFiles;
    private Multimap<String, String> params;

    public MultipartRequest25(HttpServletRequest request, String uploadId, MultipartRequestParser requestParser) {

        super(request, uploadId);

        this.requestParser = requestParser;
    }

    private void parseIfNecessary() {
        if (responseState != null) {
            return;
        }

        try {
            requestParser.parse();

            uploadedFiles = requestParser.getUploadedFiles();
            params = requestParser.getParameters();
            responseState = ResponseState.ok;
        } catch (FileUploadException e) {
            LOGGER.error(e.getMessage(), e);
            responseState = ResponseState.serverError;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Enumeration getParameterNames() {
        Collection<Object> result = Sets.newHashSet();

        Enumeration names = super.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();

            result.add(name);
        }

        parseIfNecessary();
        result.addAll(params.keySet());

        return Iterators.asEnumeration(result.iterator());
    }

    @Override
    public String getParameter(String name) {

        String parameter = super.getParameter(name);
        if (parameter != null) {
            return parameter;
        }

        parseIfNecessary();
        Collection<String> values = params.get(name);

        if (values.isEmpty()) {
            return null;
        }

        return Iterables.get(values, 0);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (parameterValues != null) {
            return parameterValues;
        }

        parseIfNecessary();
        Collection<String> values = params.get(name);

        if (values.isEmpty()) {
            return null;
        }

        return values.toArray(new String[values.size()]);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Map getParameterMap() {
        Map parameterMap = Maps.newHashMap(super.getParameterMap());
        parseIfNecessary();
        parameterMap.putAll(Maps.transformValues(params.asMap(), MULTIMAP_VALUE_TRANSFORMER));

        return parameterMap;
    }

    public Iterable<UploadedFile> getUploadedFiles() {
        parseIfNecessary();

        return uploadedFiles;
    }

    public void release() {
        super.release();

        if (uploadedFiles != null) {
            for (UploadedFile uploadedFile : uploadedFiles) {
                try {
                    uploadedFile.delete();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    public ResponseState getResponseState() {
        parseIfNecessary();

        return responseState;
    }
}