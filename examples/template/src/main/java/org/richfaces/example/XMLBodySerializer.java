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

import java.io.StringWriter;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;

/**
 * @author Maksim Kaszynski
 *
 */
public class XMLBodySerializer {
    public String serialize(NodeList childNodes, Document xmlDocument) throws ParsingException {
        try {
            StringWriter out;
            DocumentFragment fragment = xmlDocument.createDocumentFragment();
            for (int i = 0; i < childNodes.getLength(); i++) {
                fragment.appendChild(childNodes.item(i).cloneNode(true));
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setErrorListener(new ErrorListener() {
                public void error(TransformerException exception) throws TransformerException {
                }

                public void fatalError(TransformerException exception) throws TransformerException {
                }

                public void warning(TransformerException exception) throws TransformerException {
                }
            });
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            out = new StringWriter();
            StreamResult result = new StreamResult(out);
            transformer.transform(new DOMSource(fragment), result);
            return out.toString();
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
