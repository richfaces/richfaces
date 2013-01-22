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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
@DynamicResource
public class CacheableResourceImpl extends AbstractCacheableResource {
    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.resource.AbstractBaseResource#getInputStream()
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(getContentToEcho());
    }

    private byte[] getContentToEcho() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, String> headers = externalContext.getRequestHeaderMap();
        Object content = headers.get(ResourceHandlerImplTest.ECHO_HEADER);

        try {
            return content.toString().getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new FacesException(e.getMessage(), e);
        }
    }

    @Override
    protected int getContentLength(FacesContext context) {
        return getContentToEcho().length;
    }

    @Override
    public String getContentType() {
        return "text/plain";
    }

    @Override
    long getCurrentTime() {
        return ResourceHandlerImplTest.currentTime.getTime();
    }

    @Override
    protected Date getLastModified(FacesContext context) {
        return ResourceHandlerImplTest.lastModified;
    }

    @Override
    public Date getExpires(FacesContext context) {
        return ResourceHandlerImplTest.expires;
    }
}
