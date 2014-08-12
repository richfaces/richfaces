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
import java.io.Writer;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Realization of Faces <code>ResponseWriter</code> for Cocoon Environment. Use ONLY Markup-specific calls , send it as
 * SAX events to <code>XMLConsumer</code> Use "State" pattern for control of events flow. TODO - implement namespace
 * capabilites
 *
 * @author shura
 */
public class SAXResponseWriter extends ResponseWriter {
    /**
     * As we in XML framework, only UTF-8 supported for <code>CHARTER_ENCODING</code>
     */
    private static final String CHARTER_ENCODING = "UTF-8";
    /**
     * As we in XML framework, only xml supported for <code>CONTENT_TYPE</code>
     */
    private static final String CONTENT_TYPE = "text/xml";
    private String namespaceURI = "http://www.w3.org/1999/xhtml";
    private LexicalHandler xmlLexicalHandler = null;
    private AttributesImpl attributes;
    private XMLResponseWriterState cdataState;
    private String element;
    /**
     * State after startElement. Collect Attributes for SAX startElement <code>elementState</code>
     */
    private XMLResponseWriterState elementState;
    /**
     * State in normal document <code>inDocumentState</code>
     */
    private XMLResponseWriterState inDocumentState;
    /**
     * Before StartDocument or after EndDocument <code>notDocumentState</code>
     */
    private XMLResponseWriterState notDocumentState;
    private XMLResponseWriterState state;
    /**
     * Hold Cocoon Generator XML <code>consumer</code>
     */
    private ContentHandler xmlConsumer;

    /**
     * @param consumer - SAX events receiver for Cocoon pipeline.
     */
    public SAXResponseWriter(ContentHandler consumer) {
        super();
        this.xmlConsumer = consumer;

        if (consumer instanceof LexicalHandler) {
            xmlLexicalHandler = (LexicalHandler) consumer;
        }

        // Initialise states. May be must implemented in static block ?
        this.notDocumentState = new NotDocumentState();

        // inside document. allow any events exclude attributes and
        // startDocument.
        this.inDocumentState = new InDocumentState();
        this.cdataState = new CDATAState();

        // In element, collect attributes ...
        this.elementState = new ElementState();
        this.state = notDocumentState;
    }

    public ContentHandler getXmlConsumer() {
        return xmlConsumer;
    }

    /**
     * @return Returns the namespaceURI.
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * @param namespaceURI The namespaceURI to set.
     */
    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.ResponseWriter#getContentType()
     */
    public String getContentType() {
        return CONTENT_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.ResponseWriter#getCharacterEncoding()
     */
    public String getCharacterEncoding() {
        return CHARTER_ENCODING;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.Flushable#flush()
     */
    public void flush() throws IOException {

        // DO NOTHING...
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.ResponseWriter#startDocument()
     */
    public void startDocument() throws IOException {
        state.startDocument();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.ResponseWriter#endDocument()
     */
    public void endDocument() throws IOException {
        state.endDocument();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.ResponseWriter#startElement(java.lang.String, javax.faces.component.UIComponent)
     */
    public void startElement(String name, UIComponent component) throws IOException {
        state.startElement(name, component);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.ResponseWriter#endElement(java.lang.String)
     */
    public void endElement(String name) throws IOException {
        state.endElement(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.ResponseWriter#writeAttribute(java.lang.String, java.lang.Object, java.lang.String)
     */
    public void writeAttribute(String name, Object value, String property) throws IOException {
        state.writeAttribute(name, value, property);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.ResponseWriter#writeURIAttribute(java.lang.String, java.lang.Object, java.lang.String)
     */
    public void writeURIAttribute(String name, Object value, String property) throws IOException {
        state.writeURIAttribute(name, value, property);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.ResponseWriter#writeComment(java.lang.Object)
     */
    public void writeComment(Object comment) throws IOException {
        state.writeComment(comment);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.ResponseWriter#writeText(java.lang.Object, java.lang.String)
     */
    public void writeText(Object text, String property) throws IOException {
        state.writeText(text, property);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.ResponseWriter#writeText(char[], int, int)
     */
    public void writeText(char[] text, int off, int len) throws IOException {
        state.writeText(text, off, len);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.ResponseWriter#cloneWithWriter(java.io.Writer)
     */
    public ResponseWriter cloneWithWriter(Writer writer) {

        // TODO as used XML consumer to get sax Events, we simple return current
        // instance.
        // if will used wrapper to combine XML Consumer with plain Servlet
        // responce writer, must
        // perform real clone ...
        // We can use org.apache.cocoon.xml.SaxBuffer;
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.Writer#write(char[], int, int)
     */
    public void write(char[] cbuf, int off, int len) throws IOException {
        state.writeText(cbuf, off, len);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.Closeable#close()
     */
    public void close() throws IOException {

        // TODO Auto-generated method stub
    }

    /**
     * @author shura
     *         <p/>
     *         CDATA section. allow regular write() functions, write any text.
     */
    private final class CDATAState extends XMLResponseWriterState {
        void flushCDATA() throws IOException {

            // try {
            // xmlConsumer.endCDATA();
            // } catch (SAXException e) {
            // throw new IOException("Exception in endCDATA: "+ e.getMessage());
            // }
        }

        /*
         * (non-Javadoc)
         *
         * @see org.apache.cocoon.components.faces.context.XMLResponseWriterState#endDocument()
         */
        void endDocument() throws IOException {
            flushCDATA();
            state = inDocumentState;
            state.endDocument();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.apache.cocoon.components.faces.context.XMLResponseWriterState#endElement(java.lang.String)
         */
        void endElement(String name) throws IOException {
            flushCDATA();
            state = inDocumentState;
            state.endElement(name);
        }

        /*
         * (non-Javadoc)
         *
         * @see org.apache.cocoon.components.faces.context.XMLResponseWriterState#startElement(java.lang.String,
         * javax.faces.component.UIComponent)
         */
        void startElement(String name, UIComponent component) throws IOException {
            flushCDATA();
            element = name;
            attributes = new AttributesImpl();
            state = elementState;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.apache.cocoon.components.faces.context.XMLResponseWriterState#writeComment(java.lang.Object)
         */
        void writeComment(Object comment) throws IOException {
            flushCDATA();
            state = inDocumentState;
            state.writeComment(comment);
        }

        /*
         * (non-Javadoc)
         *
         * @see org.apache.cocoon.components.faces.context.XMLResponseWriterState#writeText(char[], int, int)
         */
        void writeText(char[] text, int off, int len) throws IOException {
            try {
                xmlConsumer.characters(text, off, len);
            } catch (SAXException e) {
                throw new IOException("Sax exceptions in writeText: " + e.getMessage());
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see org.apache.cocoon.components.faces.context.XMLResponseWriterState#writeText(java.lang.Object, java.lang.String)
         */
        void writeText(Object text, String property) throws IOException {
            writeText(text.toString().toCharArray(), 0, text.toString().length());
        }

        /*
         * (non-Javadoc)
         *
         * @see org.apache.cocoon.components.faces.context.XMLResponseWriterState#write(char[], int, int)
         */
        void write(char[] cbuf, int off, int len) throws IOException {
            writeText(cbuf, off, len);
        }
    }

    /**
     * @author shura State in element declsration. Collect attributes, on any other eventss - generate SAX startElement()
     */
    private final class ElementState extends XMLResponseWriterState {
        /**
         * Generate SAX StartElement event
         *
         * @throws IOException
         */
        void flushElement() throws IOException {
            try {
                xmlConsumer.startElement(getNamespaceURI(), element, element, attributes);
            } catch (SAXException e) {
                throw new IOException("Exception in startElement: " + e.getMessage());
            } finally {
                element = null;
                attributes = null;
            }
        }

        void writeAttribute(String name, Object value, String property) throws IOException {
            attributes.addAttribute(getNamespaceURI(), name, name, "id".equalsIgnoreCase(name) ? "ID" : "CDATA",
                value.toString());
        }

        void writeURIAttribute(String name, Object value, String property) throws IOException {
            String uri = value.toString();

            // TODO - perform encodeActionURL() or ???
            attributes.addAttribute(getNamespaceURI(), name, name, "CDATA", uri);
        }

        /*
         * (non-Javadoc)
         *
         * @see org.apache.cocoon.components.faces.context.XMLResponseWriterState#endElement(java.lang.String)
         */
        void endElement(String name) throws IOException {

            //
            flushElement();
            state = inDocumentState;
            state.endElement(name);
        }

        /*
         * (non-Javadoc)
         *
         * @see org.apache.cocoon.components.faces.context.XMLResponseWriterState#startElement(java.lang.String,
         * javax.faces.component.UIComponent)
         */
        void startElement(String name, UIComponent component) throws IOException {

            //
            flushElement();
            element = name;
            attributes = new AttributesImpl();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.apache.cocoon.components.faces.context.XMLResponseWriterState#writeComment(java.lang.Object)
         */
        void writeComment(Object comment) throws IOException {

            // TODO Auto-generated method stub
            flushElement();
            state = inDocumentState;
            state.writeComment(comment);
        }

        /*
         * (non-Javadoc)
         *
         * @see org.apache.cocoon.components.faces.context.XMLResponseWriterState#writeText(char[], int, int)
         */
        void writeText(char[] text, int off, int len) throws IOException {

            // TODO Auto-generated method stub
            flushElement();
            state = cdataState;
            state.writeText(text, off, len);
        }

        /*
         * (non-Javadoc)
         *
         * @see org.apache.cocoon.components.faces.context.XMLResponseWriterState#writeText(java.lang.Object, java.lang.String)
         */
        void writeText(Object text, String property) throws IOException {

            // TODO Auto-generated method stub
            flushElement();
            state = cdataState;
            state.writeText(text, property);
        }
    }

    /**
     * @author shura
     *         <p/>
     *         State in regular document. Disabled attributes & startDocument.
     */
    private final class InDocumentState extends XMLResponseWriterState {
        void startElement(String name, UIComponent component) {
            element = name;
            attributes = new AttributesImpl();
            state = elementState;
        }

        void writeComment(Object comment) throws IOException {
            String remark = comment.toString();

            try {
                xmlLexicalHandler.comment(remark.toCharArray(), 0, remark.length());
            } catch (SAXException e) {
                throw new IOException("Comment SAX exception :" + e.getMessage());
            }
        }

        void writeText(Object o, String property) throws IOException {
            writeText(o.toString().toCharArray(), 0, o.toString().length());
        }

        void writeText(char[] text, int start, int lenght) throws IOException {

            // try {
            // xmlConsumer.startCDATA();
            // } catch (SAXException e) {
            // throw new IOException("Sax exceptions in writeText: "+
            // e.getMessage());
            // } finally {
            state = cdataState;

            // }
            state.writeText(text, start, lenght);
        }

        void endElement(String name) throws IOException {
            try {
                xmlConsumer.endElement(getNamespaceURI(), name, name);
            } catch (SAXException e) {
                throw new IOException("Sax exceptions in endElement: " + e.getMessage());
            }
        }

        void endDocument() throws IOException {
            try {
                xmlConsumer.endDocument();
            } catch (SAXException e) {
                throw new IOException("Sax exceptions in endDocument" + e.getMessage());
            } finally {

                // after endDocument all events disabled ...
                state = new XMLResponseWriterState();
            }
        }
    }

    // Private classes

    /**
     * @author shura
     *         <p/>
     *         state before startDocument - only allow startDocument.
     */
    private final class NotDocumentState extends XMLResponseWriterState {
        /*
         * (non-Javadoc)
         *
         * @see org.apache.cocoon.components.faces.context.XMLResponseWriterState#startDocument()
         */
        void startDocument() throws IOException {
            try {

                //
                xmlConsumer.startDocument();
            } catch (SAXException e) {

                //
                throw new IOException("StartDocument SAX exception :" + e.getMessage());
            } finally {
                state = inDocumentState;
            }
        }
    }
}
