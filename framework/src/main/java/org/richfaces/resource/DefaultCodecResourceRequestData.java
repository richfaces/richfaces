/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import org.richfaces.util.Util;

/**
 * @author Nick Belaevski
 *
 */
public final class DefaultCodecResourceRequestData implements ResourceRequestData {
    private DefaultResourceCodec defaultResourceCodec;
    private String resourceName;
    private String libraryName;
    private String version;
    private String dataString;
    private boolean dataSerialized;
    // lazy evaluated
    private String resourceKey = null;
    // lazy evaluated
    private Object data = null;

    public DefaultCodecResourceRequestData(DefaultResourceCodec defaultResourceCodec) {
        super();
        this.defaultResourceCodec = defaultResourceCodec;
    }

    protected void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }

    protected void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getLibraryName() {
        return libraryName;
    }

    protected void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getResourceKey() {
        if (resourceKey == null) {
            resourceKey = defaultResourceCodec.encodeResource(this);
        }
        return resourceKey;
    }

    protected String getDataString() {
        return dataString;
    }

    protected void setDataString(String dataString) {
        this.dataString = dataString;
    }

    protected void setDataSerialized(boolean dataSerialized) {
        this.dataSerialized = dataSerialized;
    }

    protected boolean isDataSerialized() {
        return dataSerialized;
    }

    public Object getData() {
        if (data == null && dataString != null) {
            if (isDataSerialized()) {
                data = Util.decodeObjectData(dataString);
            } else {
                data = Util.decodeBytesData(dataString);
            }
        }

        return data;
    }
}
