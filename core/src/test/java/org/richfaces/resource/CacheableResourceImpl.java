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
