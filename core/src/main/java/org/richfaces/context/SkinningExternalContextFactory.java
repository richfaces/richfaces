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
package org.richfaces.context;

import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextFactory;
import javax.faces.context.ExternalContextWrapper;

/**
 * @author Nick Belaevski
 *
 */
public class SkinningExternalContextFactory extends ExternalContextFactory implements FacesWrapper<ExternalContextFactory> {
    private ExternalContextFactory factory;

    private static final class ExternalContextWrapperImpl extends ExternalContextWrapper {
        private ExternalContext externalContext;

        public ExternalContextWrapperImpl(ExternalContext externalContext) {
            super();
            this.externalContext = externalContext;
        }

        @Override
        public String getMimeType(String file) {
            String mimeType;

            if (file != null && file.endsWith(".ecss")) {
                mimeType = "text/vnd.richfaces.css";
            } else {
                mimeType = super.getMimeType(file);
            }

            return mimeType;
        }

        @Override
        public ExternalContext getWrapped() {
            return externalContext;
        }

        @Override
        public boolean isSecure() {
            return getWrapped().isSecure();
        }
    }

    public SkinningExternalContextFactory(ExternalContextFactory factory) {
        super();
        this.factory = factory;
    }

    @Override
    public ExternalContextFactory getWrapped() {
        return factory;
    }

    @Override
    public ExternalContext getExternalContext(Object context, Object request, Object response) throws FacesException {
        ExternalContext externalContext = factory.getExternalContext(context, request, response);

        return wrap(externalContext);
    }

    private ExternalContext wrap(ExternalContext externalContext) {
        return new ExternalContextWrapperImpl(externalContext);
    }
}
