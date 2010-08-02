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

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.faces.FacesException;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author asmirnov
 */
public class FacesConfig {
    private static final String FACES_CONFIG_CLASS_PATH = "META-INF/faces-config.xml";
    private static final String FACES_CONFIG_WEB_PATH = "/WEB-INF/faces-config.xml";
    private static final Logger LOG = Logger.getLogger(FacesConfig.class.getName());
    private List<String> excludedAttributes = new ArrayList<String>();
    private SAXParserFactory factory;

    public FacesConfig() {
        factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
    }

    public void parse(ServletContext context) {
        try {
            parseClasspath(context);
            parseOptional(context);
            parseDefault(context);
        } catch (ParsingException e) {
            throw new FacesException("Error parsing faces-config", e);
        }
    }

    protected void parseDefault(ServletContext context) throws ParsingException {

        // Parse default faces config.
        InputStream inputStream = context.getResourceAsStream(FACES_CONFIG_WEB_PATH);

        if (null != inputStream) {
            try {
                parse(inputStream);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Can't close input stream for web resource " + FACES_CONFIG_WEB_PATH, e);
                }
            }
        }
    }

    protected void parseOptional(ServletContext context) throws ParsingException {

        // Parse additional faces-config files, if present.
        String facesConfigs = context.getInitParameter(FacesServlet.CONFIG_FILES_ATTR);

        if (null != facesConfigs) {
            String[] configNamesArray = facesConfigs.trim().split("(\\s)*,(\\s)*");

            for (int i = 0; i < configNamesArray.length; i++) {
                String facesConfigPath = configNamesArray[i];
                InputStream inputStream = context.getResourceAsStream(facesConfigPath);

                if (null != inputStream) {
                    try {
                        parse(inputStream);
                    } finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            LOG.log(Level.WARNING, "Can't close input stream for web resource " + facesConfigPath, e);
                        }
                    }
                }
            }
        }
    }

    protected void parseClasspath(ServletContext context) throws ParsingException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (null == classLoader) {
            classLoader = context.getClass().getClassLoader();
        }

        try {

            // Parse all faces-config.xml files in the classpath.
            Enumeration<URL> resources = classLoader.getResources(FACES_CONFIG_CLASS_PATH);

            while (resources.hasMoreElements()) {
                URL resourceURL = (URL) resources.nextElement();

                try {
                    URLConnection connection = resourceURL.openConnection();

                    // To avoid file locking in the Windows environmemt.
                    connection.setUseCaches(false);

                    InputStream inputStream = connection.getInputStream();

                    try {
                        parse(inputStream);
                    } finally {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Can't parse " + resourceURL.toExternalForm(), e);
                }
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Can't get META-INF/faces-config.xml resources", e);
        }
    }

    protected void parse(InputStream facesConfig) throws ParsingException {
        try {
            SAXParser parser = getParser();

            // WL 10.3 parser returns new instance of XMLReader everytime
            XMLReader reader = parser.getXMLReader();
            FacesConfigHandler facesConfigHandler = new FacesConfigHandler(reader, excludedAttributes);

            reader.setContentHandler(facesConfigHandler);
            reader.setEntityResolver(facesConfigHandler);
            reader.setErrorHandler(facesConfigHandler);
            reader.setDTDHandler(facesConfigHandler);
            reader.parse(new InputSource(facesConfig));
        } catch (SAXException e) {
            LOG.log(Level.WARNING, "Exception at faces-config.xml parsing", e);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Exception at faces-config.xml parsing", e);
        }
    }

    protected SAXParser getParser() throws ParsingException {
        try {
            SAXParser parser = factory.newSAXParser();

            return parser;
        } catch (ParserConfigurationException e) {
            throw new ParsingException("SAX Parser configuration error", e);
        } catch (SAXException e) {
            throw new ParsingException("SAX Parser instantiation error", e);
        }
    }

    public List<String> getExcludedAttributes() {
        return excludedAttributes;
    }
}
