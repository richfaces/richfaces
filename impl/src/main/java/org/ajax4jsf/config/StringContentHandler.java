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

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author asmirnov
 */
class StringContentHandler extends StateHandler {
    private final XMLReader reader;
    private StringBuilder result;

    public StringContentHandler(XMLReader reader, ContentHandler parentHandler, StringBuilder result) {
        super(parentHandler);
        this.reader = reader;
        this.result = result;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        result.append(ch, start, length);
    }

    protected StringBuilder getResult() {
        return result;
    }

    @Override
    protected XMLReader getReader() {
        return reader;
    }
}
