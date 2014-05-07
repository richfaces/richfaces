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
package org.ajax4jsf.io;

import java.io.IOException;

import javax.faces.component.UIComponent;

/**
 * @author shura Class to implement state pattern for <code>ResponseWriter</code> Real states must extend this. By default,
 *         block any events, ignore output.
 */
class XMLResponseWriterState {

    // private ResponseWriter writer;

    /**
     * @throws java.io.IOException
     */
    void endDocument() throws IOException {
        throw new IOException("Illegal state for this method");
    }

    /**
     * @param name
     * @throws java.io.IOException
     */
    void endElement(String name) throws IOException {
        throw new IOException("Illegal state for this method");
    }

    /**
     * @throws java.io.IOException
     */
    void startDocument() throws IOException {
        throw new IOException("Illegal state for this method");
    }

    /**
     * @param name
     * @param component
     * @throws java.io.IOException
     */
    void startElement(String name, UIComponent component) throws IOException {
        throw new IOException("Illegal state for this method");
    }

    /**
     * @param name
     * @param value
     * @param property
     * @throws java.io.IOException
     */
    void writeAttribute(String name, Object value, String property) throws IOException {
        throw new IOException("Illegal state for this method");
    }

    /**
     * @param comment
     * @throws java.io.IOException
     */
    void writeComment(Object comment) throws IOException {
        throw new IOException("Illegal state for this method");
    }

    /**
     * @param text
     * @param off
     * @param len
     * @throws java.io.IOException
     */
    void writeText(char[] text, int off, int len) throws IOException {
        throw new IOException("Illegal state for this method");
    }

    /**
     * @param text
     * @param property
     * @throws java.io.IOException
     */
    void writeText(Object text, String property) throws IOException {
        throw new IOException("Illegal state for this method");
    }

    /**
     * @param name
     * @param value
     * @param property
     * @throws java.io.IOException
     */
    void writeURIAttribute(String name, Object value, String property) throws IOException {
        throw new IOException("Illegal state for this method");
    }

    /**
     * Main hook for realise <code>Writer</code>. In document writed as comment, outside of document do nothing ....
     *
     * @param cbuf
     * @param off
     * @param len
     * @throws IOException
     */
    void write(char[] cbuf, int off, int len) throws IOException {

        // DO NOTHING
    }
}
