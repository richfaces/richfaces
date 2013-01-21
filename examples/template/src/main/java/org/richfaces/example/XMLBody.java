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
package org.richfaces.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class must read XML file from input stream and can extract body of root element for include into target in generation.
 *
 * @author shura
 *
 */
public class XMLBody {
    private Document xmlDocument;
    private Element rootElement;

    /**
     * Load XML document and parse it into DOM.
     *
     * @param input
     * @throws ParsingException
     */
    public void loadXML(InputStream input) throws ParsingException {
        loadXML(input, false);
    }

    /**
     * Load XML document and parse it into DOM.
     *
     * @param input
     * @throws ParsingException
     */
    public void loadXML(InputStream input, boolean namespaceAware) throws ParsingException {
        try {
            // Create Document Builder Factory
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setIgnoringElementContentWhitespace(true);
            docFactory.setValidating(false);
            docFactory.setNamespaceAware(namespaceAware);
            // Create Document Builder
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // docBuilder.
            // docBuilder.isValidating();

            // Disable loading of external Entityes
            docBuilder.setEntityResolver(new EntityResolver() {
                // Dummi resolver - alvays do nothing
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return new InputSource(new StringReader(""));
                }
            });

            // open and parse XML-file
            xmlDocument = docBuilder.parse(input);

            // Get Root xmlElement
            rootElement = xmlDocument.getDocumentElement();
        } catch (Exception e) {
            throw new ParsingException("Error load XML ", e);
        }
    }

    /**
     * Check name of root element is as expected.
     *
     * @param name
     * @return
     */
    public boolean isRootName(String name) {
        return rootElement.getNodeName().equals(name);
    }

    public String getDoctype() {
        DocumentType doctype = xmlDocument.getDoctype();
        if (null != doctype) {
            return doctype.getName();
        }
        return null;
    }

    public String getPiblicId() {
        DocumentType doctype = xmlDocument.getDoctype();
        if (null != doctype) {
            return doctype.getPublicId();
        }
        return null;
    }

    public String getRootTypeName() {
        return rootElement.getSchemaTypeInfo().getTypeName();
    }

    public String getContent() throws ParsingException {
        NodeList childNodes = rootElement.getChildNodes();
        return serializeNodes(childNodes);
    }

    private String serializeNodes(NodeList childNodes) throws ParsingException {
        try {
            return new XMLBodySerializer().serialize(childNodes, xmlDocument);
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public String getContent(String xpath) throws ParsingException {
        return serializeNodes(getByXpath(xpath));
    }

    public NodeList getByXpath(String xpath) throws ParsingException {
        XPath path = XPathFactory.newInstance().newXPath();
        NodeList childNodes;
        try {
            childNodes = (NodeList) path.evaluate(xpath, xmlDocument, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new ParsingException("Error evaluate xpath", e);
        }
        return childNodes;
    }

    public NodeList getByXpathUnique(String xpath, String keyXPath, Set<String> keySet) throws ParsingException {
        if (keyXPath == null) {
            return getByXpath(xpath);
        } else {
            XPath path = XPathFactory.newInstance().newXPath();
            NodeList childNodes;
            try {
                childNodes = getByXpath(xpath);

                List<Node> nodeSet = new ArrayList<Node>();

                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i).cloneNode(true);

                    String key = serializeNodes((NodeList) path.evaluate(keyXPath, node, XPathConstants.NODESET));
                    if (!keySet.contains(key)) {
                        keySet.add(key);
                        nodeSet.add(node);
                    }
                }
                return new ArrayNodeList(nodeSet.toArray(new Node[nodeSet.size()]));
            } catch (XPathExpressionException e) {
                throw new ParsingException("Error evaluate xpath", e);
            }
        }
    }

    public String getContentUnique(String xpath, String keyXPath, Set<String> keySet) throws ParsingException {
        return serializeNodes(getByXpathUnique(xpath, keyXPath, keySet));
    }
}

class ArrayNodeList implements NodeList {
    private Node[] nodes;

    public ArrayNodeList(Node[] nodes) {
        super();
        this.nodes = nodes;
    }

    public int getLength() {
        return nodes.length;
    }

    public Node item(int index) {
        if (index < nodes.length) {
            return this.nodes[index];
        }

        return null;
    }
}
