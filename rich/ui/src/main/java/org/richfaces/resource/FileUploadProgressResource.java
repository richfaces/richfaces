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

import javax.faces.context.FacesContext;

import org.richfaces.request.ProgressControl;

/**
 * @author Nick Belaevski
 *
 */
public class FileUploadProgressResource extends AbstractJSONResource {
    private static final Object UID_ALT_KEY = "rf_fu_uid_alt";

    @Override
    protected Object getData(FacesContext context) {
        String uploadId = context.getExternalContext().getRequestParameterMap().get(UID_ALT_KEY);
        return ProgressControl.getProgress(context, uploadId);
    }
}
