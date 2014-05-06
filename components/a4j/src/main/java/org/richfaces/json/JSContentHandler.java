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
package org.richfaces.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import org.ajax4jsf.Messages;
import org.ajax4jsf.javascript.JSEncoder;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * @author shura SAX content handler for serialise events as JavaScript function.
 */
public class JSContentHandler implements ContentHandler, LexicalHandler {
    public static final String DEFAULT_ENCODING = "ISO-8859-1";
    private static final char C_COMMA = ',';
    private static final char C_GT = '>';
    private static final char C_LT = '<';
    private static final char C_NSSEP = ':';
    private static final char C_QUOTE = '\'';
    private static final char C_SPACE = ' ';
    private static final JSEncoder ENCODER = new JSEncoder();
    private static final char[] S_TEXT_START = "new T(".toCharArray();
    private static final char[] S_TEXT_END = ")".toCharArray();
    private static final char[] S_PROCINSTR_START = "<?".toCharArray();
    private static final char[] S_PROCINSTR_END = "?>".toCharArray();
    private static final char[] S_OBJECT_START = "{".toCharArray();
    private static final char[] S_OBJECT_END = "}".toCharArray();
    private static final char[] S_EOL = System.getProperty("line.separator").toCharArray();
    private static final char[] S_ELEMENT_START = "new E(".toCharArray();
    private static final char[] S_ELEMENT_END_START_TAG = ",[".toCharArray();
    private static final char[] S_ELEMENT_END = "])".toCharArray();
    private static final char[] S_ELEMENT_CLOSE = ")".toCharArray();
    private static final char[] S_DOCUMENT_START = "(".toCharArray();
    private static final char[] S_DOCUMENT_ENF = ");".toCharArray();
    private static final char[] S_DOCUMENT_3 = "\"?>".toCharArray();
    private static final char[] S_COMMENT_START = "new C('".toCharArray();
    private static final char[] S_COMMENT_END = "')".toCharArray();
    private static final char[] S_CDATA_START = "new D('".toCharArray();
    private static final char[] S_CDATA_END = "')".toCharArray();
    private static final char[] S_ATTRIBUTES_START = ",{".toCharArray();
    private static final char[] S_ATTRIBUTES_END = "}".toCharArray();
    private static final boolean DEBUG = false;
    // protected DocType doctype = null;
    protected char[] indentBuffer;
    protected int level;
    protected Writer outputWriter;
    private boolean hangingElement = false;
    /**
     * True if we are processing the prolog.
     */
    private boolean beforeDocumentStart = true;
    /**
     * True if we are processing the DTD.
     */
    private boolean processingDtd = false;
    /**
     * True if we are processing the DTD.
     */
    private boolean processingCdata = false;

    /* ====================================================================== */
    /**
     * The <code>DocType</code> instance representing the document.
     */
    private Locator locator;

    public JSContentHandler() {
    }

    /**
     * @param outputWriter
     */
    public JSContentHandler(Writer outputWriter) {
        this.outputWriter = outputWriter;
    }

    /**
     * @throws java.io.IOException
     */
    public void close() throws IOException {
        outputWriter.close();
    }

    // ContentHandler Methods

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.beforeDocumentStart || (level < 0)) {
            return;
        }

        try {
            if ((level != 0) && !this.closeElement(false) && !this.processingCdata) {
                this.outputWriter.write(C_COMMA);
            }

            if (DEBUG) {
                this.outputWriter.write('[');
                this.outputWriter.write(Integer.toString(level));
                this.outputWriter.write(']');
            }

            if (!this.processingCdata) {
                this.outputWriter.write(S_TEXT_START);
            }

            this.encodeText(ch, start, length);

            if (!this.processingCdata) {
                this.outputWriter.write(S_TEXT_END);
            }
        } catch (IOException e) {
            throw new SAXException("Write error", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument() throws SAXException {
        this.beforeDocumentStart = true;

        if (this.level != 0) {
            throw new SAXException(Messages.getMessage(Messages.OPEN_CLOSE_TAGS_DO_NOT_MATCH_ERROR));
        }

        // Write parameters after parsing and final function )
        try {
            this.outputWriter.write(S_EOL);
            this.outputWriter.flush();
        } catch (IOException e) {
            throw new SAXException("Write error", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.level--;

        if (closeElement(true)) {
            return;
        }

        try {
            this.outputWriter.write(S_ELEMENT_END); // [</]

            if (DEBUG) {
                this.outputWriter.write('[');
                this.outputWriter.write(qName);
                this.outputWriter.write('-');
                this.outputWriter.write(uri);
                this.outputWriter.write(']');
            }
        } catch (IOException e) {
            throw new SAXException("Write error", e);
        }

        // this.outputWriter.write(qual);
        // this.outputWriter.write(C_GT); // [>]
    }

    /**
     * Write the end part of a start element (if necessary).
     *
     * @param endElement Whether this method was called because an element is being closed or not.
     * @return <b>true </b> if this call successfully closed the element (and no further <code>&lt;/element&gt;</code> is
     *         required.
     */
    protected boolean closeElement(boolean endElement) throws SAXException {
        if (!hangingElement) {
            return false;
        }

        try {
            if (endElement) {
                this.outputWriter.write(S_ELEMENT_CLOSE); // [ />]
            } else {
                this.outputWriter.write(S_ELEMENT_END_START_TAG); // [>]
            }
        } catch (IOException e) {
            throw new SAXException("Write error", e);
        }

        this.hangingElement = false;

        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
     */
    public void processingInstruction(String target, String data) throws SAXException {

        // TODO Auto-generated method stub
    }

    /* ====================================================================== */

    /**
     * Receive an object for locating the origin of SAX document events.
     */
    public final void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    /**
     * Return the public identifier for the current document event.
     *
     * @return A <code>String</code> containing the public identifier, or <b>null</b> if none is available.
     */
    public String getPublicId() {
        return (this.locator == null) ? null : this.locator.getPublicId();
    }

    /**
     * Return the system identifier for the current document event.
     *
     * @return A <code>String</code> containing the system identifier, or <b>null</b> if none is available.
     */
    public String getSystemId() {
        return (this.locator == null) ? null : this.locator.getSystemId();
    }

    /**
     * Return the line number where the current document event ends.
     *
     * @return The line number, or -1 if none is available.
     */
    public int getLineNumber() {
        return (this.locator == null) ? -1 : this.locator.getLineNumber();
    }

    /**
     * Return the column number where the current document event ends.
     *
     * @return The column number, or -1 if none is available.
     */
    public int getColumnNumber() {
        return (this.locator == null) ? -1 : this.locator.getColumnNumber();
    }

    /**
     * Return a <code>String</code> describing the current location.
     */
    protected String getLocation() {
        if (this.locator == null) {
            return "";
        }

        StringBuffer buf = new StringBuffer(" (");

        if (this.getSystemId() != null) {
            buf.append(this.getSystemId());
            buf.append(' ');
        }

        buf.append("line " + this.getLineNumber());
        buf.append(" col " + this.getColumnNumber());
        buf.append(')');

        return buf.toString();
    }

    /* ====================================================================== */

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */

    public void skippedEntity(String name) throws SAXException {

        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() throws SAXException {
        this.beforeDocumentStart = false;
        this.processingCdata = false;
        this.level = 0;

        /* We have a document type. */

        // if (this.doctype != null) {
        //
        // String root_name = this.doctype.getName();
        // /* Check the DTD and the root element */
        // if (!root_name.equals(qual)) {
        // throw new SAXException("Root element name \"" + root_name
        // + "\" declared by document type declaration differs "
        // + "from actual root element name \"" + qual + "\"");
        // }

        /* Output the <!DOCTYPE ...> declaration. */

        // this.outputWriter.write(this.doctype.toString());
        // }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
     * org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if (!this.closeElement(false) && (this.level > 0)) {
                this.outputWriter.write(C_COMMA);
            }

            this.outputWriter.write(S_ELEMENT_START); // [<]

            if (DEBUG) {
                this.outputWriter.write('[');
                this.outputWriter.write(uri);
                this.outputWriter.write(']');
            }

            this.outputWriter.write(C_QUOTE);
            this.outputWriter.write(qName);
            this.outputWriter.write(C_QUOTE);

            // if (attributes.getLength() > 0) {
            this.outputWriter.write(S_ATTRIBUTES_START);

            // TODO - implementing namespaces !
            // for (int x = 0; x < namespaces.length; x++) {
            // this.outputWriter.write(S_ELEMENT_4); // [ xmlns]
            // if (namespaces[x][Namespaces.NAMESPACE_PREFIX].length() > 0) {
            // this.outputWriter.write(C_NSSEP); // [:]
            // this.outputWriter.write(namespaces[x][Namespaces.NAMESPACE_PREFIX]);
            // }
            // this.outputWriter.write(S_ELEMENT_1); // [="]
            // this.encode(namespaces[x][Namespaces.NAMESPACE_URI]);
            // this.outputWriter.write(C_QUOTE); // ["]
            // }
            for (int x = 0; x < attributes.getLength(); x++) {
                if (0 != x) {
                    this.outputWriter.write(C_COMMA);
                    this.outputWriter.write(C_SPACE); // [ ]
                }

                if (DEBUG) {
                    this.outputWriter.write('[');
                    this.outputWriter.write(attributes.getURI(x));
                    this.outputWriter.write(']');
                }

                String attrName = attributes.getQName(x);

                // For JavaScript any attributes names illegal ...
                // replate with correct names.
                if (attrName.equalsIgnoreCase("class")) {
                    attrName = "className";
                }

                this.outputWriter.write(C_QUOTE); // [']
                this.outputWriter.write(attrName);
                this.outputWriter.write(C_QUOTE); // [']
                this.outputWriter.write(C_NSSEP); // [:]

                // TODO by nick - fix namespace handling - they shouldn't be encoded
                this.encodeAttributeValue(attributes, x);
            }

            this.outputWriter.write(S_ATTRIBUTES_END);

            // }
            this.level++;
            this.hangingElement = true;
        } catch (IOException e) {
            throw new SAXException("write error", e);
        }
    }

    protected void encodeAttributeValue(Attributes attributes, int idx) throws SAXException, IOException {
        this.outputWriter.write(C_QUOTE); // [']
        this.encode(attributes.getValue(idx));
        this.outputWriter.write(C_QUOTE); // [']
    }

    protected void encodeText(char[] chars, int start, int length) throws SAXException, IOException {
        this.encode(chars, start, length);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
     */
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ext.LexicalHandler#comment(char[], int, int)
     */
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.beforeDocumentStart || (level < 0)) {
            return;
        }

        try {
            if (!this.closeElement(false) && (level != 0)) {
                this.outputWriter.write(C_COMMA);
            }

            if (DEBUG) {
                this.outputWriter.write('[');
                this.outputWriter.write(Integer.toString(level));
                this.outputWriter.write(']');
            }

            // this.outputWriter.write(C_QUOTE);
            this.outputWriter.write(S_COMMENT_START);
            this.encode(ch, start, length);
            this.outputWriter.write(S_COMMENT_END);

            // this.outputWriter.write(C_QUOTE);
        } catch (IOException e) {
            throw new SAXException("Write error", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ext.LexicalHandler#endCDATA()
     */
    public void endCDATA() throws SAXException {
        if (this.beforeDocumentStart || (level < 0)) {
            return;
        }

        try {
            if (this.closeElement(false)) {
                return;
            }

            if (DEBUG) {
                this.outputWriter.write('[');
                this.outputWriter.write(Integer.toString(level));
                this.outputWriter.write(']');
            }

            this.outputWriter.write(S_CDATA_END);
            this.processingCdata = false;
        } catch (IOException e) {
            throw new SAXException("Write error", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ext.LexicalHandler#endDTD()
     */
    public void endDTD() throws SAXException {

        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)
     */
    public void endEntity(String name) throws SAXException {

        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ext.LexicalHandler#startCDATA()
     */
    public void startCDATA() throws SAXException {
        if (this.beforeDocumentStart || (level < 0)) {
            return;
        }

        try {
            if (!this.closeElement(false) && (level != 0)) {
                this.outputWriter.write(C_COMMA);
            }

            if (DEBUG) {
                this.outputWriter.write('[');
                this.outputWriter.write(Integer.toString(level));
                this.outputWriter.write(']');
            }

            this.outputWriter.write(S_CDATA_START);
            this.processingCdata = true;
        } catch (IOException e) {
            throw new SAXException("Write error", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ext.LexicalHandler#startDTD(java.lang.String, java.lang.String, java.lang.String)
     */
    public void startDTD(String name, String publicId, String systemId) throws SAXException {

        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)
     */
    public void startEntity(String name) throws SAXException {

        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.cocoon.components.serializers.EncodingSerializer#writeIndent(int)
     */
    protected void writeIndent(int indent) throws SAXException {
        try {
            this.outputWriter.write("\n".toCharArray(), 0, 1);

            if (indent > 0) {
                this.outputWriter.write(assureIndentBuffer(indent), 0, indent);
            }
        } catch (IOException e) {
            throw new SAXException("Write error", e);
        }
    }

    private char[] assureIndentBuffer(int size) {
        if ((indentBuffer == null) || (indentBuffer.length < size)) {
            indentBuffer = new char[size];
            Arrays.fill(indentBuffer, ' ');
        }

        return indentBuffer;
    }

    /**
     * Encode and write a <code>String</code>
     */
    protected void encode(String data) throws SAXException {
        char[] array = data.toCharArray();

        this.encode(array, 0, array.length);
    }

    /**
     * Encode and write an array of characters.
     */
    protected void encode(char[] data) throws SAXException {
        this.encode(data, 0, data.length);
    }

    /**
     * Encode and write a specific part of an array of characters.
     */
    protected void encode(char[] data, int start, int length) throws SAXException {
        int end = start + length;

        if (data == null) {
            throw new NullPointerException("Null data");
        }

        if ((start < 0) || (start > data.length) || (length < 0) || (end > data.length) || (end < 0)) {
            throw new IndexOutOfBoundsException("Invalid data");
        }

        if (length == 0) {
            return;
        }

        try {
            for (int x = start; x < end; x++) {
                char c = data[x];

                if (JSContentHandler.ENCODER.compile(c)) {
                    continue;
                }

                if (start != x) {
                    this.outputWriter.write(data, start, x - start);
                }

                this.outputWriter.write(JSContentHandler.ENCODER.encode(c));
                start = x + 1;

                continue;
            }

            if (start != end) {
                this.outputWriter.write(data, start, end - start);
            }
        } catch (IOException e) {
            throw new SAXException("Write error", e);
        }
    }

    protected boolean isProcessingCdata() {
        return processingCdata;
    }

    protected boolean isBeforeDocumentStart() {
        return beforeDocumentStart;
    }
}
