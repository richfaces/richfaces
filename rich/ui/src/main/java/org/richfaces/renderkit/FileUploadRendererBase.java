/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractFileUpload;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.richfaces.request.MultipartRequest;

/**
 * @author Konstantin Mishin
 * @author Nick Belaevski
 * @author Lukas Fryc
 * @author Simone Cinti
 *
 */

public class FileUploadRendererBase extends RendererBase {

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        final AbstractFileUpload fileUpload = (AbstractFileUpload) component;
        final ExternalContext externalContext = context.getExternalContext();

        MultipartRequest multipartRequest = (MultipartRequest) externalContext.getRequestMap().get(
                MultipartRequest.REQUEST_ATTRIBUTE_NAME);

        if (multipartRequest != null) {

            String clientId = component.getClientId(context);
            for (UploadedFile file : multipartRequest.getUploadedFiles()) {
//                if (fileUpload.acceptsFile(file)) {
//                    fileUpload.queueEvent(new FileUploadEvent(fileUpload, file));
//                }

                if (clientId.equals(file.getParameterName())) {
                    component.queueEvent(new FileUploadEvent(component, file));
                    break;
                }

            }
        }
    }
}