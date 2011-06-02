package org.richfaces.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
        String string = "E:/projs/richafces4/framework/trunk/impl/src/test/java/org/ajax4jsf/cache/extended.xcss";
        parser.parse(string);
    }
}

class Handler extends DefaultHandler {
    private static final String TEMPLATE = "template";
    private static final String SELECTOR = "selector";
    private static final String STYLE = "style";
    private static final String RESOURCE = "resource";
    private static final String ATTRIBBUTE = "attribute";
    private static final String VERBATIM = "verbatim";
    private static final String IMPORT = "importResource";
    private StringBuilder ecssContent;
    private boolean hasAttribbute = false;
    private boolean verbatim = false;;

    /**
     * Receive notification of the start of an element.
     *
     * @param namespaceURI - The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName - The local name (without prefix), or the empty string if Namespace processing is not being performed.
     * @param qName - The qualified name (with prefix), or the empty string if qualified names are not available.
     * @param atts - The attributes attached to the element. If there are no attributes, it shall be an empty Attributes object.
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
            ecssContent.append("']\"}\r\n");
        }
        if (VERBATIM.equals(localName)) {
            verbatim = true;
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
            if (null != name) {
                if (skin != null) {
                    ecssContent.append("\t");
                    ecssContent.append(name);
                    ecssContent.append(":");
                    ecssContent.append("'#{richSkin.");
                    ecssContent.append(skin);
                    ecssContent.append("}'");
                } else if (value != null) {
                    ecssContent.append("\t");
                    ecssContent.append(name);
                    ecssContent.append(":");
                    ecssContent.append(value);
                } else {
                    ecssContent.append("\t");
                    ecssContent.append(name);
                    ecssContent.append(":");
                }
            }
        }
        if (RESOURCE.equals(localName)) {
            String value = atts.getValue("f:key");
            if (null != value) {
                ecssContent.append("\"url(#{resource['");
                ecssContent.append(value);
            }
        }
        if (ATTRIBBUTE.equals(localName)) {
            if (!hasAttribbute) {
                ecssContent.append("?");
                hasAttribbute = true;
            }
            String name = atts.getValue("name");
            String skin = atts.getValue("skin");
            String value = atts.getValue("value");
            if (null != name) {
                if (skin != null) {
                    ecssContent.append(name);
                    ecssContent.append("=");
                    ecssContent.append("Skin.");
                    ecssContent.append(skin);
                    ecssContent.append("&");
                } else if (value != null) {
                    ecssContent.append(name);
                    ecssContent.append("=");
                    try {
                        ecssContent.append(URLEncoder.encode(value, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    ecssContent.append("&");
                } else {
                    // ERROR
                }
            }
        }
    }

    /**
     * Receive notification of character data inside an element.
     *
     * @param ch - The characters.
     * @param start - The start position in the character array.
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
     *        processing is not being performed.
     * @param localName - The local name (without prefix), or the empty string if Namespace processing is not being performed.
     * @param qName - The qualified name (with prefix), or the empty string if qualified names are not available.
     * @throws SAXException - Any SAX exception, possibly wrapping another exception.
     */
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (TEMPLATE.equals(localName)) {
            System.out.println(ecssContent.toString().trim());
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
                ecssContent.setLength(ecssContent.length() - 1);
            }
            ecssContent.append("']})\"");
            hasAttribbute = false;
        }
        if (ATTRIBBUTE.equals(localName)) {
            // Do nothing.
        }
    }
}

class CreateParser {
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
     *
     * @throws javax.xml.parsers.ParserConfigurationException
     *
     * @throws org.xml.sax.SAXException
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