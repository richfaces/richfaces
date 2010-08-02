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

import junit.framework.TestCase;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author asmirnov
 *
 */
public class StateHandlerTest extends TestCase {
    private static final String BAR = "bar";
    private static final String NS = "http://foo.com/";
    private static final String PREFIX = "foo:";
    private XMLReader reader;

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        reader = new MockXmlReader();
    }

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testReturnBack() throws Exception {
        ContentHandler parentHandler = new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String name, Attributes attributes)
                    throws SAXException {
                throw new SAXException();
            }
            @Override
            public void endElement(String uri, String localName, String name) throws SAXException {
                throw new SAXException();
            }
        };
        StateHandler handler = new StateHandler(parentHandler) {
            @Override
            protected XMLReader getReader() {
                return reader;
            }
        };

        reader.setContentHandler(handler);
        handler.startElement(NS, BAR, PREFIX + BAR, null);
        handler.startElement(NS, BAR, PREFIX + BAR, null);
        handler.endElement(NS, BAR, PREFIX + BAR);
        assertSame(handler, reader.getContentHandler());
        handler.endElement(NS, BAR, PREFIX + BAR);
        assertSame(handler, reader.getContentHandler());
        handler.endElement(NS, BAR, PREFIX + BAR);
        assertSame(parentHandler, reader.getContentHandler());
    }
}
