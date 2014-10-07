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

package org.richfaces.resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class Xcss2EcssConverter {
    private Xcss2EcssConverter() {
    }

    public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException {

        // Create Handler
        Handler handler = new Handler();

        // Create the parser
        CreateParser parser = new CreateParser(handler);

        // Parse the XML file, handler generates the output
        String filename = args[0];
        parser.parse(filename);
    }

    public static class Handler extends DefaultHandler {
        private static final String TEMPLATE = "template";
        private static final String SELECTOR = "selector";
        private static final String STYLE = "style";
        private static final String RESOURCE = "resource";
        private static final String ATTRIBUTE = "attribute";
        private static final String VERBATIM = "verbatim";
        private static final String IMPORT = "importResource";
        private static final String IF = "if";
        private StringBuilder ecssContent;
        private StringBuilder currentCssValue = new StringBuilder();
        private boolean hasAttribbute = false;
        private boolean verbatim = false;
        private List<String> conditions = new ArrayList<String>();
        private PrintStream outputStream;

        public Handler() {
            this.outputStream = System.out;
        }

        public Handler(OutputStream outputStream) {
            this.outputStream = new PrintStream(outputStream);
        }

        /**
         * Receive notification of the start of an element.
         *
         * @param namespaceURI - The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace
         *                     processing is not being performed.
         * @param localName    - The local name (without prefix), or the empty string if Namespace processing is not being performed.
         * @param qName        - The qualified name (with prefix), or the empty string if qualified names are not available.
         * @param atts         - The attributes attached to the element. If there are no attributes, it shall be an empty Attributes object.
         * @throws SAXException - Any SAX exception, possibly wrapping another exception.
         */
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

            if (TEMPLATE.equals(localName)) {
                ecssContent = new StringBuilder();
            }
            if (IMPORT.equals(localName)) {
                String src = atts.getValue("src");
                ecssContent.append("@import url(\"#{resource['");
                ecssContent.append(src);
                ecssContent.append("']}\");\r\n");
            }
            if (VERBATIM.equals(localName)) {
                verbatim = true;
                String skin = atts.getValue("skin");
                if (null != skin) {
                    ecssContent.append(" " + cssValue("#{a4jSkin." + skin + "}") + " ");
                }
            }
            if (SELECTOR.equals(localName)) {
                String value = atts.getValue("name");
                if (null != value) {
                    ecssContent.append(value);
                    ecssContent.append("{\r\n");
                }
            }
            if (STYLE.equals(localName)) {
                // Reset Order's values
                String name = atts.getValue("name");
                String skin = atts.getValue("skin");
                String value = atts.getValue("value");
                String defaultAttr = atts.getValue("default");
                if (null != name) {
                    ecssContent.append("\t");
                    ecssContent.append(name);
                    ecssContent.append(":");
                    if (null != defaultAttr) {
                        if (skin != null) {
                            conditions.add("#{not empty a4jSkin." + skin + "}");
                            ecssContent.append(cssValue("#{a4jSkin." + skin + "}", defaultAttr));
                            conditions.remove(conditions.size() - 1);
                        } else {
                            ecssContent.append(cssValue(defaultAttr, null));
                        }
                    } else if (skin != null) {
                        ecssContent.append(cssValue("#{a4jSkin." + skin + "}", null));
                    } else if (value != null) {
                        ecssContent.append(cssValue(value, null));
                    }
                }
            }
            if (RESOURCE.equals(localName)) {
                String value = atts.getValue("f:key");
                if (null != value) {
                    currentCssValue.append("url(#{resource['");
                    currentCssValue.append(value);
                }
                String skin = atts.getValue("f:skin");
                if (skin != null) {
                    currentCssValue.append("url(#{resource[a4jSkin.");
                    currentCssValue.append(skin);
                }
            }
            if (ATTRIBUTE.equals(localName)) {
                if (!hasAttribbute) {
                    currentCssValue.append("?");
                    hasAttribbute = true;
                }
                String name = atts.getValue("name");
                String skin = atts.getValue("skin");
                String value = atts.getValue("value");
                if (null != name) {
                    if (skin != null) {
                        currentCssValue.append(name);
                        currentCssValue.append("=");
                        currentCssValue.append("Skin.");
                        currentCssValue.append(skin);
                        currentCssValue.append("&");
                    } else if (value != null) {
                        currentCssValue.append(name);
                        currentCssValue.append("=");
                        try {
                            currentCssValue.append(URLEncoder.encode(value, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        currentCssValue.append("&");
                    } else {
                        // ERROR
                    }
                }
            }
            if (IF.equals(localName)) {
                String condition = atts.getValue("when");
                if (condition == null) {
                    condition = "#{not empty a4jSkin." + atts.getValue("skin") + "}";
                }
                conditions.add(condition);
            }
        }

        private String cssValue(String value) {
            return cssValue(value, null);
        }

        private String cssValue(String value, String defaultValue) {
            String cssValue = _cssValue(value, defaultValue);
            // escape cssValue
            if (cssValue != null) {
                if (cssValue.contains("#{")) {
                    if (cssValue.contains("'")) {
                        cssValue = "\"" + cssValue + "\"";
                    } else {
                        cssValue = "'" + cssValue + "'";
                    }
                }
            }
            return cssValue;
        }

        /**
         * Returns unescaped El or literal value.
         */
        private String _cssValue(String value, String defaultValue) {
            if (defaultValue == null || defaultValue.trim().length() == 0) {
                defaultValue = "''";
            }
            if (conditions.size() == 0) {
                return value;
            }
            if (isEl(defaultValue)) {
                defaultValue = defaultValue.substring(2, defaultValue.length() - 2);
            } else if (!defaultValue.contains("'")) {
                defaultValue = "'" + defaultValue + "'";
            }
            if (isEl(value)) {
                //return insertConditionIntoEl(conditions, value, defaultValue);
                int start = value.indexOf("#{");
                int end = value.indexOf("}", start);
                if (start > 0) {
                    //mixed el / constant - like "url(#{resource['test.png']})"
                    return _cssValue(value.substring(0, start), defaultValue)
                            + _cssValue(value.substring(start, value.length()), defaultValue);
                } else if (end < value.length() - 1) {
                    //mixed el / constant - like #{resource['test.png']}10px
                    return _cssValue(value.substring(0, end + 1), defaultValue)
                            + _cssValue(value.substring(end + 1, value.length()), defaultValue);
                } else {
                    //full el - like #{resource['test.png']}
                    String conditionsString = conditionsToString();
                    return "#{" + conditionsString + " ? " + value.substring("#{".length(), value.length() - 1) + " : " + defaultValue + "}";
                }
            } else {
                String conditionsString = conditionsToString();
                return "#{" + conditionsString + " ? '" + value + "' : " + defaultValue + "}";
            }
        }

        private String conditionsToString() {
            if (conditions.size() == 1) {
                return convertCondition(conditions.get(0));
            } else {
                StringBuilder builder = new StringBuilder();
                String sep = "";
                for (String condition : conditions) {
                    builder.append(sep + "(" + convertCondition(condition) + ")");
                    sep = " and ";
                }
                return builder.toString();
            }
        }

        /**
         * If condition is an El, removes '#{' prefix and '}' suffix.
         * If not, adds a ' prefix and suffix
         */
        private String convertCondition(String condition) {
            if (isEl(condition)) {
                return removeElDelimiters(condition);
            } else {
                return toEl(condition);
            }
        }

        /*private String convertValue(String value) {
           return convertCondition(value);
       } */

        private boolean isEl(String value) {
            return value.contains("#{");
        }

        /**
         * Transforms a constant to el constant by adding '.
         * eg test is converted to 'test'
         */
        private String toEl(String value) {
            return "'" + value + "'";
        }

        /**
         * Removes '#{' prefix and '}' suffix.
         * Don't do anything if value doesn't begins with '#{'.
         */
        private String removeElDelimiters(String value) {
            String current = value.trim();
            if (current.startsWith("#{")) {
                current = current.substring(2);
                if (current.endsWith("}")) {
                    current = current.substring(0, current.length() - 1);
                }
            }
            return current;
        }

        /**
         * Receive notification of character data inside an element.
         *
         * @param ch     - The characters.
         * @param start  - The start position in the character array.
         * @param length - The number of characters to use from the character array.
         * @throws SAXException - Any SAX exception, possibly wrapping another exception.
         */
        public void characters(char[] ch, int start, int length) throws SAXException {

            if (verbatim) {
                String strValue = new String(ch, start, length);
                ecssContent.append(strValue);
            }
        }

        /**
         * Receive notification of the end of an element.
         *
         * @param namespaceURI - The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace
         *                     processing is not being performed.
         * @param localName    - The local name (without prefix), or the empty string if Namespace processing is not being performed.
         * @param qName        - The qualified name (with prefix), or the empty string if qualified names are not available.
         * @throws SAXException - Any SAX exception, possibly wrapping another exception.
         */
        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

            if (TEMPLATE.equals(localName)) {
                outputStream.println(ecssContent.toString().trim());
            }
            if (VERBATIM.equals(localName)) {
                verbatim = false;
            }
            if (SELECTOR.equals(localName)) {
                ecssContent.append("}\r\n");
            }
            if (STYLE.equals(localName)) {
                ecssContent.append(";\r\n");
            }
            if (RESOURCE.equals(localName)) {
                if (hasAttribbute) {
                    currentCssValue.setLength(currentCssValue.length() - 1);
                }
                if (currentCssValue.indexOf("'") != -1) {
                    currentCssValue.append("'");
                }
                currentCssValue.append("]})");
                ecssContent.append(cssValue(currentCssValue.toString(), null));
                currentCssValue = new StringBuilder();
                hasAttribbute = false;
            }
            if (ATTRIBUTE.equals(localName)) {
                // Do nothing.
            }
            if (IF.equals(localName)) {
                conditions.remove(conditions.size() - 1);
            }
        }
    }

    public static class CreateParser {
        private DefaultHandler handler;
        private SAXParser saxParser;

        /**
         * Constructor
         *
         * @param handler - DefaultHandler for the SAX parser
         * @throws javax.xml.parsers.ParserConfigurationException
         *
         * @throws org.xml.sax.SAXException
         */
        public CreateParser(DefaultHandler handler) throws SAXException, ParserConfigurationException {
            this.handler = handler;
            create();
        }

        /**
         * Create the SAX parser
         */
        private void create() throws SAXException, ParserConfigurationException {
            // Obtain a new instance of a SAXParserFactory.
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // Specifies that the parser produced by this code will provide support for XML namespaces.
            factory.setNamespaceAware(true);
            // Specifies that the parser produced by this code will validate documents as they are parsed.
            // factory.setValidating(true);
            // Creates a new instance of a SAXParser using the currently configured factory parameters.
            saxParser = factory.newSAXParser();
        }

        /**
         * Parse a File
         *
         * @param file - File
         * @throws IOException
         * @throws SAXException
         */
        public void parse(File file) throws IOException, SAXException {
            saxParser.parse(file, handler);
        }

        /**
         * Parse a URI
         *
         * @param uri - String
         */
        public void parse(String uri) throws IOException, SAXException {
            saxParser.parse(uri, handler);
        }

        /**
         * Parse a Stream
         *
         * @param stream - InputStream
         */
        public void parse(InputStream stream) throws IOException, SAXException {
            saxParser.parse(stream, handler);
        }
    }

    /**
     * FilterReader converting xcss content to ecss content
     * <p/>
     * Can be usefull with ant copy task to convert a bunch of xcss files.
     */
    public static class FilterReader extends java.io.FilterReader {

        public FilterReader(Reader in) {
            super(in);
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                parse(toInputStream(in), baos);
                this.in = new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), "utf-8");
            } catch (IOException e) {
                throw new RuntimeException("Error while reading xcss content : " + e.toString(), e);
            } catch (SAXException e) {
                throw new RuntimeException("Error while reading xml xcss content : " + e.toString(), e);
            } catch (ParserConfigurationException e) {
                throw new RuntimeException("Error while parsing xml xcss content : " + e.toString(), e);
            }
        }

        private InputStream toInputStream(Reader in) throws IOException {
            return toInputStream(toString(in));
        }

        private String toString(Reader in) throws IOException {
            StringBuilder builder = new StringBuilder(1000);
            char[] buf = new char[1024];
            int numRead;
            while ((numRead = in.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                builder.append(readData);
            }
            in.close();
            return builder.toString();
        }

        private InputStream toInputStream(String value) {
            try {
                return new ByteArrayInputStream(value.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("unexpected exception, utf-8 encoding should be supported : " + e, e);
            }
        }

        private void parse(InputStream inputStream, OutputStream outputStream) throws SAXException, ParserConfigurationException, IOException {
            Handler handler = new Handler(outputStream);
            CreateParser parser = new CreateParser(handler);
            parser.parse(inputStream);
        }
    }
}
