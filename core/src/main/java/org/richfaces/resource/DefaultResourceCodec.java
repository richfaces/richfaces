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
package org.richfaces.resource;

import java.util.Map;

import javax.faces.context.FacesContext;

public final class DefaultResourceCodec implements ResourceCodec {
    private static final String VERSION_PARAM = "v";
    private static final String DATA_BYTES_ARRAY_PARAM = "db";
    private static final String DATA_OBJECT_PARAM = "do";
    private static final String LIBRARY_NAME_PARAM = "ln";

    String encodeResource(DefaultCodecResourceRequestData data) {
        return encodeResource(data.getLibraryName(), data.getResourceName(), data.getDataString(), data.isDataSerialized(),
            data.getVersion());
    }

    String encodeResource(String libraryName, String resourceName, String encodedResourceData, boolean dataIsSerialized,
        String resourceVersion) {

        boolean parameterAppended = false;

        StringBuilder sb = new StringBuilder();
        sb.append(resourceName);

        if (resourceVersion != null && resourceVersion.length() != 0) {
            if (!parameterAppended) {
                sb.append('?');
                parameterAppended = true;
            }

            sb.append(VERSION_PARAM);
            sb.append('=');
            sb.append(ResourceUtils.encodeURIQueryPart(resourceVersion));
        }

        if (encodedResourceData != null && encodedResourceData.length() != 0) {
            if (!parameterAppended) {
                sb.append('?');
                parameterAppended = true;
            } else {
                sb.append('&');
            }

            sb.append(dataIsSerialized ? DATA_OBJECT_PARAM : DATA_BYTES_ARRAY_PARAM);
            sb.append('=');
            sb.append(ResourceUtils.encodeURIQueryPart(encodedResourceData));
        }

        if (libraryName != null && libraryName.length() != 0) {
            if (!parameterAppended) {
                sb.append('?');
                parameterAppended = true;
            } else {
                sb.append('&');
            }

            sb.append(LIBRARY_NAME_PARAM);
            sb.append('=');
            sb.append(ResourceUtils.encodeURIQueryPart(libraryName));
        }

        return sb.toString();
    }

    public String encodeResourceRequestPath(FacesContext context, String libraryName, String resourceName, Object resourceData,
        String resourceVersion) {
        String encodedDataString = null;
        boolean dataIsSerialized = false;
        if (resourceData != null) {
            if (resourceData instanceof byte[]) {
                encodedDataString = ResourceUtils.encodeBytesData((byte[]) resourceData);
            } else {
                encodedDataString = ResourceUtils.encodeObjectData(resourceData);
                dataIsSerialized = true;
            }
        }

        return ResourceHandlerImpl.RICHFACES_RESOURCE_IDENTIFIER
            + encodeResource(libraryName, resourceName, encodedDataString, dataIsSerialized, resourceVersion);
    }

    public String encodeJSFMapping(FacesContext context, String resourcePath) {
        return ResourceUtils.encodeJSFURL(context, resourcePath);
    }

    public ResourceRequestData decodeResource(FacesContext context, String requestPath) {
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        DefaultCodecResourceRequestData data = new DefaultCodecResourceRequestData(this);
        data.setResourceName(requestPath);
        data.setLibraryName(params.get(LIBRARY_NAME_PARAM));
        data.setVersion(params.get(VERSION_PARAM));

        String objectDataString = params.get(DATA_OBJECT_PARAM);
        if (objectDataString != null) {
            data.setDataString(objectDataString);
            data.setDataSerialized(true);
        } else {
            data.setDataString(params.get(DATA_BYTES_ARRAY_PARAM));
        }
        return data;
    }
}
