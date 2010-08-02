/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.ajax4jsf.config;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringReader;

/**
 * @author asmirnov
 */
public abstract class StateHandler extends DefaultHandler {
    private int depth = 0;
    private final ContentHandler parentHandler;

    public StateHandler(ContentHandler parentHandler) {
        super();
        this.parentHandler = parentHandler;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        ContentHandler nextHandler = getNextHandler(uri, localName, attributes);

        if (null == nextHandler) {
            depth++;
        } else {

            // TODO nick - setup all four handlers here?
            getReader().setContentHandler(nextHandler);
        }
    }

    protected ContentHandler getNextHandler(String uri, String localName, Attributes attributes) {
        return null;
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (depth-- == 0) {
            if (null != parentHandler) {

                // TODO nick - see to-do in startElement method
                getReader().setContentHandler(parentHandler);
            }

            endLastElement();
        }
    }

    protected void endLastElement() {

        // Do nothing
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {

        // Do nothing, to avoid network requests to external DTD/Schema
        return new InputSource(new StringReader(""));
    }

    protected abstract XMLReader getReader();
}
