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
package org.richfaces.resource.optimizer.resource.handler.impl;

import static org.richfaces.resource.optimizer.strings.Constants.SLASH_JOINER;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceSkinUtils;
import org.richfaces.resource.optimizer.FileNameMapper;
import org.richfaces.application.ServiceTracker;

/**
 * @author Nick Belaevski
 *
 */
public class DynamicResourceWrapper extends Resource {
    /**
     *
     */
    private static final String ECSS_EXTENSION = ".ecss";
    private Resource resource;

    public DynamicResourceWrapper(Resource resource) {
        super();
        this.resource = resource;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return resource.getInputStream();
    }

    public String getContentType() {
        return resource.getContentType();
    }

    public void setContentType(String contentType) {
        resource.setContentType(contentType);
    }

    public String getLibraryName() {
        return resource.getLibraryName();
    }

    public void setLibraryName(String libraryName) {
        resource.setLibraryName(libraryName);
    }

    public String getResourceName() {
        return resource.getResourceName();
    }

    public void setResourceName(String resourceName) {
        resource.setResourceName(resourceName);
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        throw new UnsupportedOperationException();
    }

    private FileNameMapper getFileNameMapper() {
        return ServiceTracker.getService(FileNameMapper.class);
    }

    @Override
    public String getRequestPath() {
        String resourceExtension = getResourceExtension();

        String resourceName = getResourceName();
        if (resourceName.endsWith(ECSS_EXTENSION)) {
            resourceName = resourceName.substring(0, resourceName.length() - ECSS_EXTENSION.length());
        }

        if (resourceExtension != null && !resourceName.endsWith(resourceExtension)) {
            resourceName = resourceName + resourceExtension;
        }

        String resourcePath = SLASH_JOINER.join(getLibraryName(), resourceName);
        String filename = getFileNameMapper().createName(resourcePath);
        String filenameWithSkinPlaceholder = ResourceSkinUtils.prefixPathWithSkinPlaceholder(filename);
        return filenameWithSkinPlaceholder;
    }

    @Override
    public URL getURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        throw new UnsupportedOperationException();
    }

    protected String getResourceExtension() {
        String contentType = getContentType();
        if (contentType == null) {
            return null;
        }

        if (contentType.startsWith("text/") || contentType.startsWith("image/")) {
            String[] split = contentType.split("/");
            if (split.length == 2) {
                return '.' + split[1];
            }
        }

        return null;
    }
}
