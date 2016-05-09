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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;

import org.ajax4jsf.io.ByteBuffer;
import org.ajax4jsf.io.FastBufferInputStream;
import org.ajax4jsf.io.FastBufferOutputStream;

/**
 * @author Nick Belaevski
 *
 */
public class UserResourceWrapperImpl extends BaseResourceWrapper<UserResource> implements ContentProducerResource {
    private static final InputStream EMPTY_STREAM = new ByteArrayInputStream(new byte[0]);

    private static final class FacesContextWrapperImpl extends FacesContextWrapper {
        private FacesContext facesContext;
        private ExternalContextWrapperImpl externalContext;

        private FacesContextWrapperImpl(FacesContext facesContext, ExternalContextWrapperImpl externalContextWrapper) {
            super();

            this.facesContext = facesContext;
            this.externalContext = externalContextWrapper;
        }

        @Override
        public FacesContext getWrapped() {
            return facesContext;
        }

        @Override
        public ExternalContextWrapperImpl getExternalContext() {
            return externalContext;
        }

        public static FacesContextWrapperImpl wrap(Charset charset) {
            FacesContext originalFacesContext = FacesContext.getCurrentInstance();

            ExternalContextWrapperImpl externalContextWrapper = new ExternalContextWrapperImpl(
                originalFacesContext.getExternalContext(), charset);
            FacesContextWrapperImpl facesContextWrapper = new FacesContextWrapperImpl(originalFacesContext,
                externalContextWrapper);

            setCurrentInstance(facesContextWrapper);
            return facesContextWrapper;
        }

        public static void unwrap() {
            FacesContext originalContext = ((FacesContextWrapper) FacesContext.getCurrentInstance()).getWrapped();
            setCurrentInstance(originalContext);
        }

        public InputStream getWrittenDataAsStream() throws IOException {
            return externalContext.getWrittenDataAsStream();
        }
    }

    private static final class ExternalContextWrapperImpl extends ExternalContextWrapper {
        private FastBufferOutputStream stream = null;
        private Writer writer = null;
        private ExternalContext externalContext;
        private Charset charset;

        ExternalContextWrapperImpl(ExternalContext externalContext, Charset charset) {
            super();

            this.externalContext = externalContext;
            this.charset = charset;
        }

        @Override
        public ExternalContext getWrapped() {
            return externalContext;
        }

        @Override
        public FastBufferOutputStream getResponseOutputStream() {
            if (stream == null) {
                stream = new FastBufferOutputStream();
            }

            return stream;
        }

        @Override
        public Writer getResponseOutputWriter() {
            if (writer == null) {
                writer = new OutputStreamWriter(getResponseOutputStream(), charset);
            }

            return writer;
        }

        public InputStream getWrittenDataAsStream() throws IOException {
            flushBuffers();

            if (stream != null) {
                ByteBuffer firstBuffer = stream.getFirstBuffer();
                firstBuffer.compact();
                return new FastBufferInputStream(firstBuffer);
            } else {
                return EMPTY_STREAM;
            }
        }

        private void flushBuffers() throws IOException {
            if (writer != null) {
                writer.flush();
            } else if (stream != null) {
                stream.flush();
            }
        }
    }

    public UserResourceWrapperImpl(UserResource resourceObject, boolean cacheable, boolean versioned) {
        super(resourceObject, cacheable, versioned);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        Charset charset = ResourceUtils.getCharsetFromContentType(getContentType());
        FacesContextWrapperImpl wrappedContext = FacesContextWrapperImpl.wrap(charset);
        try {
            encode(wrappedContext);

            return wrappedContext.getWrittenDataAsStream();
        } finally {
            FacesContextWrapperImpl.unwrap();
        }
    }

    @Override
    protected Map<String, String> getWrappedResourceResponseHeaders() {
        return getWrapped().getResponseHeaders();
    }

    @Override
    public String getContentType() {
        return getWrapped().getContentType();
    }

    @Override
    protected int getContentLength(FacesContext context) {
        return getWrapped().getContentLength();
    }

    @Override
    protected Date getLastModified(FacesContext context) {
        return getWrapped().getLastModified();
    }

    public void encode(FacesContext context) throws IOException {
        getWrapped().encode(context);
    }
}
