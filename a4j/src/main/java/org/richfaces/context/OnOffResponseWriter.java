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
package org.richfaces.context;

import java.io.IOException;
import java.io.Writer;

import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;

/**
 * @author Nick Belaevski
 *
 */
public class OnOffResponseWriter extends ResponseWriterWrapper {
    private static final Writer NO_OP_WRITER = new Writer() {
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            // do nothing
        }

        public void write(char[] cbuf) throws IOException {
            // do nothing
        }

        ;

        public void write(int c) throws IOException {
            // do nothing
        }

        ;

        public void write(String str) throws IOException {
            // do nothing
        }

        ;

        public void write(String str, int off, int len) throws IOException {
            // do nothing
        }

        ;

        @Override
        public void flush() throws IOException {
            // do nothing
        }

        @Override
        public void close() throws IOException {
            // do nothing
        }
    };
    private boolean switchedOn = false;
    private ResponseWriter wrappedWriter;
    private ResponseWriter stubWriter;

    public OnOffResponseWriter(ResponseWriter wrappedWriter) {
        super();
        this.wrappedWriter = wrappedWriter;
    }

    @Override
    public ResponseWriter getWrapped() {
        if (!switchedOn) {
            if (stubWriter == null) {
                stubWriter = wrappedWriter.cloneWithWriter(NO_OP_WRITER);
            }

            return stubWriter;
        } else {
            return wrappedWriter;
        }
    }

    public void setSwitchedOn(boolean newState) {
        switchedOn = newState;
    }

    public boolean isSwitchedOn() {
        return switchedOn;
    }
}
