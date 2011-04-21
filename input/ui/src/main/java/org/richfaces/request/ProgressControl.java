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
package org.richfaces.request;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletInputStream;

/**
 * @author Nick Belaevski
 * 
 */
public final class ProgressControl {

    private static final String UPLOAD_PROGRESS_PREFIX = "_richfaces_upload_percents";
    
    private Map<String, Object> contextMap;
    
    private String attributeName;

    private long totalBytesRead = 0;
    
    private long length;
    
    private byte lastUpdatedPercentValue;
    
    public ProgressControl(Map<String, Object> contextMap, String uploadId, long length) {
        this.contextMap = contextMap;
        this.attributeName = getContextAttributeName(uploadId);
        this.length = length;
    }

    public static byte getProgress(FacesContext context, String uploadId) {
        Byte progress = (Byte) context.getExternalContext().getSessionMap().get(getContextAttributeName(uploadId));

        if (progress != null) {
            return progress.byteValue();
        }
        
        return 0;
    }

    private static String getContextAttributeName(String uploadId) {
        return UPLOAD_PROGRESS_PREFIX + uploadId;
    }

    void clearProgress() {
        contextMap.remove(attributeName);
    }
    
    public void advance(long bytesRead) {
        totalBytesRead += bytesRead;

        byte percent;
        if (length != 0) {
            percent = (byte) Math.floor( ((double)totalBytesRead) / length * 100);
        } else {
            percent = 100;
        }
        
        if (percent > lastUpdatedPercentValue) {
            lastUpdatedPercentValue = percent;
            contextMap.put(attributeName, lastUpdatedPercentValue);
        }
    }
    
    public ServletInputStream wrapStream(ServletInputStream inputStream) {
        return new ProgressServletInputStream(inputStream, this);
    }
}
