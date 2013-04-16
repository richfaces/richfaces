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

import java.io.IOException;

import javax.faces.context.PartialResponseWriter;

/**
 * <p>
 * This {@link PartialResponseWriter} wrapper doesn't end the document when {@link #endDocument()} is called to allow write own
 * partial-response extensions at the end of the document.
 * </p>
 *
 * <p>
 * The method {@link #finallyEndDocument()} needs to be called to actually end the document.
 * </p>
 *
 * @author Lukas Fryc
 */
public class DeferredEndingPartialResponseWriter extends PartialResponseWriterWrapper {

    private boolean redirected = false;
    private boolean error = false;

    public DeferredEndingPartialResponseWriter(PartialResponseWriter wrapped) {
        super(wrapped);
    }

    /**
     * The invocation will be blocked, you need to call {@link #finallyEndDocument()} to actually end the document.
     */
    @Override
    public void endDocument() throws IOException {
        if (shouldEndDocumentPrematurely()) {
            super.endDocument();
        }
    }

    /**
     * Will finally end the document since {@link #endDocument()} was blocked to allow to write partial-response extensions.
     *
     * @throws IOException if an input/output error occurs
     */
    void finallyEndDocument() throws IOException {
        if (!shouldEndDocumentPrematurely()) {
            super.endDocument();
        }
    }

    /**
     * Decides whether the document can be finished before {@link #finallyEndDocument()} is called.
     */
    private boolean shouldEndDocumentPrematurely() {
        return redirected || error;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.context.PartialResponseWriterWrapper#redirect(java.lang.String)
     */
    @Override
    public void redirect(String url) throws IOException {
        this.redirected = true;
        super.redirect(url);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.context.PartialResponseWriterWrapper#startError(java.lang.String)
     */
    @Override
    public void startError(String errorName) throws IOException {
        this.error = true;
        super.startError(errorName);
    }
}
