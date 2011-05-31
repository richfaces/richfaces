/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.resource;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.util.Util;

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
            sb.append(Util.encodeURIQueryPart(resourceVersion));
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
            sb.append(Util.encodeURIQueryPart(encodedResourceData));
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
            sb.append(Util.encodeURIQueryPart(libraryName));
        }

        return sb.toString();
    }

    public String encodeResourceRequestPath(FacesContext context, String libraryName, String resourceName, Object resourceData,
        String resourceVersion) {
        String encodedDataString = null;
        boolean dataIsSerialized = false;
        if (resourceData != null) {
            if (resourceData instanceof byte[]) {
                encodedDataString = Util.encodeBytesData((byte[]) resourceData);
            } else {
                encodedDataString = Util.encodeObjectData(resourceData);
                dataIsSerialized = true;
            }
        }

        return ResourceHandlerImpl.RICHFACES_RESOURCE_IDENTIFIER
            + encodeResource(libraryName, resourceName, encodedDataString, dataIsSerialized, resourceVersion);
    }

    public String encodeJSFMapping(FacesContext context, String resourcePath) {
        return Util.encodeJSFURL(context, resourcePath);
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
