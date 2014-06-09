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
package org.richfaces.demo.iteration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.swing.tree.TreeNode;

import org.richfaces.ui.iteration.tree.model.SwingTreeNodeImpl;
import org.richfaces.util.FastJoiner;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 */
public class TreeNodeParser implements ContentHandler {
    private static final FastJoiner JOINER = FastJoiner.on("");
    private XMLReader reader;
    private List<TreeNode> rootNodes = Lists.newArrayList();
    private SwingTreeNodeImpl<String> currentNode;

    public TreeNodeParser() throws SAXException {
        reader = XMLReaderFactory.createXMLReader();
    }

    public void parse(URL url) throws IOException, SAXException {
        InputStream is = null;
        try {
            is = url.openStream();
            reader.setContentHandler(this);
            reader.parse(new InputSource(is));
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // Swallow
            }
        }
    }

    public List<TreeNode> getRootNodes() {
        return rootNodes;
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        SwingTreeNodeImpl<String> newNode = new SwingTreeNodeImpl<String>();

        if (currentNode == null) {
            rootNodes.add(newNode);
        } else {
            currentNode.addChild(newNode);
        }

        newNode.setData(JOINER.join(newNode.getData(), localName.toLowerCase(Locale.US), " ["));

        currentNode = newNode;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        currentNode.setData(JOINER.join(currentNode.getData(), "]"));
        currentNode = (SwingTreeNodeImpl) currentNode.getParent();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        currentNode.setData(JOINER.join(currentNode.getData(), new String(ch, start, length).trim()));
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    public void processingInstruction(String target, String data) throws SAXException {
    }

    public void skippedEntity(String name) throws SAXException {
    }
}
