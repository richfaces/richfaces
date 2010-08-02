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
import java.util.List;

/**
 * @author asmirnov
 */
public class FacesConfigHandler extends DefaultHandler {
    private static final String APPLICATION_ELEMENT = "application";
    private static final String APP_EXTENSION_ELEMENT = "application-extension";
    private static final String BRIDGE_NS = "http://www.apache.org/myfaces/xml/ns/bridge/bridge-extension";
    private static final String EXCLUDED_ATTRIBUTES_ELEMENT = "excluded-attributes";
    private static final String EXCLUDED_ATTRIBUTE_ELEMENT = "excluded-attribute";
    private static final String JEE_NS = "http://java.sun.com/xml/ns/javaee";
    private List<String> excludedAttributes;
    private XMLReader reader;

    public FacesConfigHandler(XMLReader reader, List<String> excludedAttributes) {
        this.reader = reader;
        this.excludedAttributes = excludedAttributes;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        if (APPLICATION_ELEMENT.equals(localName) && JEE_NS.equals(uri)) {
            reader.setContentHandler(new ApplicationHandler());
        }
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {

        // Do nothing, to avoid network requests to external DTD/Schema
        return new InputSource(new StringReader(""));
    }

    /**
     * @author asmirnov
     */
    private class ApplicationExtensionHandler extends FacesConfigElementHandler {
        public ApplicationExtensionHandler(ContentHandler parent) {
            super(parent);
        }

        @Override
        protected ContentHandler getNextHandler(String uri, String localName, Attributes attributes) {
            if (EXCLUDED_ATTRIBUTES_ELEMENT.equals(localName) && BRIDGE_NS.equals(uri)) {
                return new ExcludedAttributesHandler(this);
            } else {
                return null;
            }
        }
    }

    /**
     * @author asmirnov
     */
    private class ApplicationHandler extends FacesConfigElementHandler {
        public ApplicationHandler() {
            super(FacesConfigHandler.this);
        }

        @Override
        protected ContentHandler getNextHandler(String uri, String localName, Attributes attributes) {
            if (APP_EXTENSION_ELEMENT.equals(localName) && JEE_NS.equals(uri)) {
                return new ApplicationExtensionHandler(this);
            } else {
                return null;
            }
        }
    }

    /**
     * @author asmirnov
     */
    private class ExcludedAttributeHandler extends StringContentHandler {
        public ExcludedAttributeHandler(ContentHandler parent) {
            super(reader, parent, new StringBuilder());
        }

        @Override
        protected void endLastElement() {
            excludedAttributes.add(getResult().toString());
        }
    }

    /**
     * @author asmirnov
     */
    private class ExcludedAttributesHandler extends FacesConfigElementHandler {
        public ExcludedAttributesHandler(ContentHandler parent) {
            super(parent);
        }

        @Override
        protected ContentHandler getNextHandler(String uri, String localName, Attributes attributes) {
            if (EXCLUDED_ATTRIBUTE_ELEMENT.equals(localName) && BRIDGE_NS.equals(uri)) {
                return new ExcludedAttributeHandler(this);
            } else {
                return null;
            }
        }
    }

    private abstract class FacesConfigElementHandler extends StateHandler {
        public FacesConfigElementHandler(ContentHandler parentHandler) {
            super(parentHandler);
        }

        @Override
        protected XMLReader getReader() {
            return reader;
        }
    }
}
