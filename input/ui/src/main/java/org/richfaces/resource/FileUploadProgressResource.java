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

import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.context.FileUploadPartialViewContextFactory;
import org.richfaces.request.MultipartRequest;

/**
 * @author Nick Belaevski
 * 
 */
public class FileUploadProgressResource extends AbstractJSONResource {

    @Override
    protected Object getData(FacesContext context) {
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        Map<String, Number> percentMap = (Map<String, Number>) sessionMap.get(MultipartRequest.PERCENT_BEAN_NAME);

        Number result = null;
        
        if (percentMap != null) {
            String uploadId = context.getExternalContext().getRequestParameterMap().get(FileUploadPartialViewContextFactory.UID_ALT_KEY);
            result = (Number) percentMap.get(uploadId);
        }
        
        return result != null ? result : 0;
    }

}
