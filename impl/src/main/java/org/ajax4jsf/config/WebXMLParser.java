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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author asmirnov
 */
@Deprecated
public class WebXMLParser {
    public static final String WEB_XML = "/WEB-INF/web.xml";
    static final String FACES_SERVLET_CLASS = "javax.faces.webapp.FacesServlet";
    
    private String facesFilterPrefix = null;

    /**
     * Suffix for Resource-Ajax filter , in common must be same as for
     * {@link javax.faces.webapp.FacesServlet}
     */
    private String facesFilterSuffix = null;
    private String facesServletPrefix = null;
    private String facesServletSuffix = null;
    private List<String> facesServletMappings;
    private List<String> filterMappings;

    public void init(InputStream webXml, String filterName) throws ParsingException {
        try {

            // Prepare SAX parser for a web-xml.
            SAXParserFactory factory = SAXParserFactory.newInstance();

            factory.setValidating(false);
            factory.setNamespaceAware(true);

            SAXParser parser = factory.newSAXParser();

            // WL 10.3 parser returns new instance of XMLReader everytime
            XMLReader reader = parser.getXMLReader();

            // Parse web.xml with state-avare content handler.
            WebappHandler webappHandler = new WebappHandler(reader);

            reader.setContentHandler(webappHandler);
            reader.setEntityResolver(webappHandler);
            reader.setErrorHandler(webappHandler);
            reader.setDTDHandler(webappHandler);
            reader.parse(new InputSource(webXml));

            // Calculate Faces Servlet and filter mappings.
            for (ServletBean servlet : webappHandler.getServlets()) {
                if (FACES_SERVLET_CLASS.equals(servlet.getClassName())) {
                    facesServletMappings = new ArrayList<String>();

                    for (ServletMapping mapping : webappHandler.getServletMappings()) {
                        if (servlet.getName().equals(mapping.getServletName())) {
                            facesServletMappings.add(mapping.getUrlPattern());

                            String asPrefix = getAsPrefix(mapping.getUrlPattern());

                            if (null != asPrefix) {
                                facesServletPrefix = asPrefix;
                            }

                            String asSuffix = getAsSuffix(mapping.getUrlPattern());

                            if (null != asSuffix) {
                                facesServletSuffix = asSuffix;
                            }
                        }
                    }
                }
            }

            // Find named filter configuration.
            // Got filter, check it's mapping.
            filterMappings = new ArrayList<String>();

            for (FilterMapping mapping : webappHandler.getFilterMappings()) {
                if (mapping.getFilterName().equals(filterName)) {
                    if ((null != mapping.getUrlPattern()) && !"".equals(mapping.getUrlPattern())) {
                        filterMappings.add(mapping.getUrlPattern());

                        String asPrefix = getAsPrefix(mapping.getUrlPattern());

                        if (null != asPrefix) {
                            facesFilterPrefix = asPrefix;
                        }

                        String asSuffix = getAsSuffix(mapping.getUrlPattern());

                        if (null != asSuffix) {
                            facesFilterSuffix = asSuffix;
                        }
                    } else if ((null != mapping.getServletName()) && !"".equals(mapping.getServletName())) {
                        for (ServletMapping servletMapping : webappHandler.getServletMappings()) {
                            if (mapping.getServletName().equals(servletMapping.getServletName())) {
                                filterMappings.add(servletMapping.getUrlPattern());

                                String asPrefix = getAsPrefix(servletMapping.getUrlPattern());

                                if (null != asPrefix) {
                                    facesFilterPrefix = asPrefix;
                                }

                                String asSuffix = getAsSuffix(servletMapping.getUrlPattern());

                                if (null != asSuffix) {
                                    facesFilterSuffix = asSuffix;
                                }
                            }
                        }
                    }
                }
            }

            if (0 == filterMappings.size()) {
                throw new ParsingException("No filter mapping set for a filter " + filterName);
            }
        } catch (IOException e) {
            throw new ParsingException("Error read web application config", e);
        } catch (ParserConfigurationException e) {
            throw new ParsingException("SAX Parser configuration error", e);
        } catch (SAXException e) {
            throw new ParsingException("Error parsing XML for the web application config", e);
        }
    }

    private String getAsSuffix(String urlPattern) {
        String suffix = null;

        if (urlPattern.startsWith("*")) {
            suffix = urlPattern.substring(1);
        }

        return suffix;
    }

    private String getAsPrefix(String urlPattern) {
        String preffix = null;

        if (urlPattern.endsWith("*")) {
            int cut = urlPattern.endsWith("/*") ? 2 : 1;

            preffix = urlPattern.substring(0, urlPattern.length() - cut);
        }

        return preffix;
    }

    public List<String> getFacesServletMappings() {
        return facesServletMappings;
    }

    /**
     * @return the facesFilterPrefix
     */
    public String getFacesFilterPrefix() {
        return facesFilterPrefix;
    }

    /**
     * @return the facesFilterSuffix
     */
    public String getFacesFilterSuffix() {
        return facesFilterSuffix;
    }

    /**
     * @return the facesServletPrefix
     */
    public String getFacesServletPrefix() {
        return facesServletPrefix;
    }

    /**
     * @return the facesServletSuffix
     */
    public String getFacesServletSuffix() {
        return facesServletSuffix;
    }

    public void init(ServletContext portletContext, String filterName) throws ServletException {
        InputStream inputStream = portletContext.getResourceAsStream(WEB_XML);

        if (null != inputStream) {
            try {
                init(inputStream, filterName);
            } catch (ParsingException e1) {
                throw new ServletException("Error parse web application config", e1);
            }

            try {
                inputStream.close();
            } catch (IOException e) {

                // Do nothing, ignore
            }
        }
    }
}
