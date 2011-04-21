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
package org.richfaces.context;

import java.io.IOException;
import java.io.Writer;

import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContextWrapper;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.request.MultipartRequest;
import org.richfaces.request.MultipartRequest.ResponseState;

/**
 * @author Nick Belaevski
 * 
 */
public class FileUploadFacesContextFactory extends FacesContextFactory implements FacesWrapper<FacesContextFactory> {

    private static final Logger LOGGER = RichfacesLogger.CONTEXT.getLogger();

    private static final class FileUploadFacesContext extends FacesContextWrapper {

        private FacesContext facesContext;

        public FileUploadFacesContext(FacesContext facesContext) {
            super();
            this.facesContext = facesContext;
        }
        
        @Override
        public FacesContext getWrapped() {
            return facesContext;
        }
        
        @Override
        public void release() {
            MultipartRequest multipartRequest = (MultipartRequest) getExternalContext().getRequestMap().get(MultipartRequest.REQUEST_ATTRIBUTE_NAME);

            if (multipartRequest != null) {
                multipartRequest.release();
            }

            super.release();
        }
    }

    private FacesContextFactory wrappedFactory;

    public FileUploadFacesContextFactory(FacesContextFactory wrappedFactory) {
        super();
        this.wrappedFactory = wrappedFactory;
    }
    
    @Override
    public FacesContextFactory getWrapped() {
        return wrappedFactory;
    }

    @Override
    public FacesContext getFacesContext(Object context, Object request, Object response, Lifecycle lifecycle)
        throws FacesException {

        FacesContext facesContext = wrappedFactory.getFacesContext(context, request, response, lifecycle);
        
        MultipartRequest multipartRequest = (MultipartRequest) facesContext.getExternalContext().getRequestMap().get(MultipartRequest.REQUEST_ATTRIBUTE_NAME);
        if (multipartRequest != null) {
            facesContext = new FileUploadFacesContext(facesContext);

            if (multipartRequest.getResponseState() != ResponseState.ok) {
                printResponse(facesContext, multipartRequest);
            }
        }
        
        return facesContext;
    }
    
    
    private void printResponse(FacesContext facesContext, MultipartRequest multipartRequest) {
        facesContext.responseComplete();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseStatus(HttpServletResponse.SC_OK);
        externalContext.setResponseContentType("text/html");
        try {
            Writer writer = externalContext.getResponseOutputWriter();
            writer.write("<html id=\"" + FileUploadExternalContextFactory.UID_KEY + multipartRequest.getUploadId() + ":" + multipartRequest.getResponseState() + "\"/>");
            writer.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
