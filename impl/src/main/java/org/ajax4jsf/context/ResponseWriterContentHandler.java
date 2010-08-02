/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

package org.ajax4jsf.context;

import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import javax.faces.FacesException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class ResponseWriterContentHandler implements ContentHandler {
    private String linkClass;
    private Node node;

    public ResponseWriterContentHandler(String linkClass) {
        super();

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            node = document.createElement("head");
            document.appendChild(node);
        } catch (ParserConfigurationException e) {
            throw new FacesException(e.getLocalizedMessage(), e);
        }

        this.linkClass = linkClass;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        node.appendChild(node.getOwnerDocument().createTextNode(new String(ch, start, length)));
    }

    public void endDocument() throws SAXException {
    }

    public void endElement(String uri, String localName, String name) throws SAXException {
        node = node.getParentNode();
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        throw new UnsupportedOperationException();
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    public void processingInstruction(String target, String data) throws SAXException {
        throw new UnsupportedOperationException();
    }

    public void setDocumentLocator(Locator locator) {
        throw new UnsupportedOperationException();
    }

    public void skippedEntity(String name) throws SAXException {
        throw new UnsupportedOperationException();
    }

    public void startDocument() throws SAXException {
    }

    public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
        Document document = node.getOwnerDocument();
        Element element = document.createElement(localName);
        int length = atts.getLength();

        for (int i = 0; i < length; i++) {
            element.setAttribute(atts.getLocalName(i), atts.getValue(i));
        }

        if (HTML.LINK_ELEMENT.equals(localName)) {
            element.setAttribute(HTML.CLASS_ATTRIBUTE, linkClass);
        }

        node = node.appendChild(element);
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        throw new UnsupportedOperationException();
    }

    public Node[] getNodes() {
        NodeList childNodes = node.getChildNodes();
        Node[] list = new Node[childNodes.getLength()];

        for (int i = 0; i < list.length; i++) {
            list[i] = childNodes.item(i);
        }

        return list;
    }
}
