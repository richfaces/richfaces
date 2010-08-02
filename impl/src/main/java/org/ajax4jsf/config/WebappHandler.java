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

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author asmirnov
 */
public class WebappHandler extends DefaultHandler {
    static final String FILTER_CLASS_ELEMENT = "filter-class";
    static final String FILTER_ELEMENT = "filter";
    static final String FILTER_MAPPING_ELEMENT = "filter-mapping";
    static final String FILTER_NAME_ELEMENT = "filter-name";
    static final String SERVLET_CLASS_ELEMENT = "servlet-class";
    static final String SERVLET_ELEMENT = "servlet";
    static final String SERVLET_MAPPING_ELEMENT = "servlet-mapping";
    static final String SERVLET_NAME_ELEMENT = "servlet-name";
    static final String URL_PATTERN_ELEMENT = "url-pattern";
    private List<ServletBean> servlets = new ArrayList<ServletBean>();
    private List<ServletMapping> servletMappings = new ArrayList<ServletMapping>();
    private List<FilterBean> filters = new ArrayList<FilterBean>();
    private List<FilterMapping> filterMappings = new ArrayList<FilterMapping>();
    private ServletBean facesServlet;
    private XMLReader xmlReader;

    public WebappHandler(XMLReader reader) {
        this.xmlReader = reader;
    }

    public List<FilterBean> getFilters() {
        return filters;
    }

    public List<FilterMapping> getFilterMappings() {
        return filterMappings;
    }

    public List<ServletBean> getServlets() {
        return servlets;
    }

    public List<ServletMapping> getServletMappings() {
        return servletMappings;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        if (SERVLET_ELEMENT.equals(localName)) {
            xmlReader.setContentHandler(new ServletHandler());
        } else if (SERVLET_MAPPING_ELEMENT.equals(localName)) {
            xmlReader.setContentHandler(new ServletMappingHandler());
        } else if (FILTER_ELEMENT.equals(localName)) {
            xmlReader.setContentHandler(new FilterHandler());
        } else if (FILTER_MAPPING_ELEMENT.equals(localName)) {
            xmlReader.setContentHandler(new FilterMappingHandler());
        }
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {

        // Do nothing, to avoid network requests for external DTD/Schema
        return new InputSource(new StringReader(""));
    }

    /**
     * @author asmirnov
     */
    public class FilterHandler extends WebappElementHandler {
        private StringBuilder filterName = new StringBuilder();
        private StringBuilder filterClass = new StringBuilder();

        public FilterHandler() {
            super(WebappHandler.this);
        }

        @Override
        protected ContentHandler getNextHandler(String uri, String localName, Attributes attributes) {

            // TODO Auto-generated method stub
            ContentHandler handler = null;

            if (FILTER_CLASS_ELEMENT.equals(localName)) {
                handler = new StringContentHandler(getReader(), this, filterClass);
            } else if (FILTER_NAME_ELEMENT.equals(localName)) {
                handler = new StringContentHandler(getReader(), this, filterName);
            }

            return handler;
        }

        @Override
        protected void endLastElement() {
            filters.add(new FilterBean(filterName.toString().trim(), filterClass.toString().trim()));
        }
    }

    /**
     * @author asmirnov
     */
    public class FilterMappingHandler extends WebappElementHandler {
        private StringBuilder servletName = new StringBuilder();
        private StringBuilder urlPattern = new StringBuilder();
        private StringBuilder filterName = new StringBuilder();

        public FilterMappingHandler() {
            super(WebappHandler.this);
        }

        @Override
        protected ContentHandler getNextHandler(String uri, String localName, Attributes attributes) {
            ContentHandler nextHandler = null;

            if (SERVLET_NAME_ELEMENT.equals(localName)) {
                nextHandler = new StringContentHandler(getReader(), this, servletName);
            } else if (URL_PATTERN_ELEMENT.equals(localName)) {
                nextHandler = new StringContentHandler(getReader(), this, urlPattern);
            } else if (FILTER_NAME_ELEMENT.equals(localName)) {
                nextHandler = new StringContentHandler(getReader(), this, filterName);
            }

            return nextHandler;
        }

        @Override
        protected void endLastElement() {
            filterMappings.add(new FilterMapping(filterName.toString().trim(), servletName.toString().trim(),
                urlPattern.toString().trim()));
        }
    }

    final class ServletHandler extends WebappElementHandler {
        private StringBuilder servletName = new StringBuilder();
        private StringBuilder servletClass = new StringBuilder();

        public ServletHandler() {
            super(WebappHandler.this);
        }

        @Override
        protected ContentHandler getNextHandler(String uri, String localName, Attributes attributes) {
            ContentHandler nextHandler = null;

            if (SERVLET_NAME_ELEMENT.equals(localName)) {
                nextHandler = new StringContentHandler(getReader(), this, servletName);
            } else if (SERVLET_CLASS_ELEMENT.equals(localName)) {
                nextHandler = new StringContentHandler(getReader(), this, servletClass);
            }

            return nextHandler;
        }

        @Override
        protected void endLastElement() {
            servlets.add(new ServletBean(servletName.toString().trim(), servletClass.toString().trim()));
        }
    }

    final class ServletMappingHandler extends WebappElementHandler {
        private StringBuilder servletName = new StringBuilder();
        private StringBuilder urlPattern = new StringBuilder();

        public ServletMappingHandler() {
            super(WebappHandler.this);
        }

        @Override
        protected ContentHandler getNextHandler(String uri, String localName, Attributes attributes) {
            ContentHandler nextHandler = null;

            if (SERVLET_NAME_ELEMENT.equals(localName)) {
                nextHandler = new StringContentHandler(getReader(), this, servletName);
            } else if (URL_PATTERN_ELEMENT.equals(localName)) {
                nextHandler = new StringContentHandler(getReader(), this, urlPattern);
            }

            return nextHandler;
        }

        @Override
        protected void endLastElement() {
            servletMappings.add(new ServletMapping(servletName.toString().trim(), urlPattern.toString().trim()));
        }
    }

    private abstract class WebappElementHandler extends StateHandler {
        public WebappElementHandler(ContentHandler parentHandler) {
            super(parentHandler);
        }

        @Override
        protected XMLReader getReader() {
            return xmlReader;
        }
    }
}
