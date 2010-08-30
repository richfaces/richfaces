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

package org.ajax4jsf.webapp;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

import javax.faces.application.ViewExpiredException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajax4jsf.Messages;
import org.ajax4jsf.application.AjaxViewHandler;
import org.ajax4jsf.context.ContextInitParameters;
import org.ajax4jsf.renderkit.AjaxContainerRenderer;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

public abstract class BaseXMLFilter {
    public static final String AJAX_EXPIRED = "Ajax-Expired";
    public static final String APPLICATION_XHTML_XML = "application/xhtml+xml";
    public static final String APPLICATION_SCOPE_KEY = BaseXMLFilter.class.getName();
    public static final String TEXT_HTML = "text/html";

    private static final String FORCENOTRF_PARAMETER = "forcenotrf";
    private static final String FORCEXML_PARAMETER = "forceparser";
    private static final String INIT_PARAMETER_PREFIX = "org.ajax4jsf.xmlfilter.";
    private static final String MIME_TYPE_PARAMETER = "mime-type";
    private static final String NAMESPACE_PARAMETER = "namespace";
    private static final String PUBLICID_PARAMETER = "publicid";
    private static final String SYSTEMID_PARAMETER = "systemid";
    private static final Logger LOG = RichfacesLogger.WEBAPP.getLogger();

    private String mimetype = "text/xml";
    private String namespace = "http://www.w3.org/1999/xhtml";
    private String publicid = "-//W3C//DTD XHTML 1.0 Transitional//EN";
    private String systemid = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
    private boolean handleViewExpiredOnClient = false;
    private boolean forcexml = false;
    private boolean forceNotRf = true;
    private BaseFilter filter;

    public void setFilter(BaseFilter filter) {
        this.filter = filter;
    }

    public void init(FilterConfig config) throws ServletException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("init XML filter service with class " + this.getClass().getName());
        }

        String forceXmlParameter = config.getInitParameter(FORCEXML_PARAMETER);

        if (forceXmlParameter == null) {
            forceXmlParameter = config.getServletContext().getInitParameter(INIT_PARAMETER_PREFIX + FORCEXML_PARAMETER);
        }

        setupForceXml(forceXmlParameter);

        String forceNotRfParameter = config.getInitParameter(FORCENOTRF_PARAMETER);

        if (forceNotRfParameter == null) {
            forceNotRfParameter = config.getServletContext().getInitParameter(INIT_PARAMETER_PREFIX
                + FORCENOTRF_PARAMETER);
        }

        setupForcenotrf(forceNotRfParameter);
        setMimetype((String) nz(config.getInitParameter(MIME_TYPE_PARAMETER), "text/xml"));
        setPublicid((String) nz(config.getInitParameter(PUBLICID_PARAMETER), getPublicid()));
        setSystemid((String) nz(config.getInitParameter(SYSTEMID_PARAMETER), getSystemid()));
        setNamespace((String) nz(config.getInitParameter(NAMESPACE_PARAMETER), getNamespace()));
        handleViewExpiredOnClient = Boolean.parseBoolean(
            config.getServletContext().getInitParameter(ContextInitParameters.HANDLE_VIEW_EXPIRED_ON_CLIENT));
    }

    private Boolean stringToBoolean(String s) {
        if ("false".equalsIgnoreCase(s)) {
            return Boolean.FALSE;
        } else if ("true".equalsIgnoreCase(s)) {
            return Boolean.TRUE;
        }

        return null;
    }

    private void setupForcenotrf(String paramValue) {
        Boolean val = stringToBoolean(paramValue);

        if (val != null) {
            this.forceNotRf = val.booleanValue();
        }
    }

    /**
     * @param forceXmlParameter
     */
    private void setupForceXml(String forceXmlParameter) {
        Boolean val = stringToBoolean(forceXmlParameter);

        if (val != null) {
            this.forcexml = val.booleanValue();
        }
    }

    /**
     * Perform filter chain with xml parsing and transformation. Subclasses must
     * implement concrete HTML to XML parsing, nesseasary transformations and
     * serialization.
     *
     * @param chain
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doXmlFilter(FilterChain chain, HttpServletRequest request, final HttpServletResponse response)
        throws IOException, ServletException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("XML filter service start processing request");
        }

        FilterServletResponseWrapper servletResponseWrapper = getWrapper(response);

        // HACK - to avoid MyFaces <f:view> incompabilites and bypass
        // intermediaty filters
        // in chain, self-rendered region write directly to wrapper stored in
        // request-scope attribute.
        try {
            request.setAttribute(BaseFilter.RESPONSE_WRAPPER_ATTRIBUTE, servletResponseWrapper);
            chain.doFilter(request, servletResponseWrapper);
        } catch (ServletException e) {
            if (handleViewExpiredOnClient && (isViewExpired(e) || isViewExpired(e.getRootCause()))
                && isAjaxRequest(request)) {
                LOG.debug("ViewExpiredException in the filter chain - will be handled on the client", e);

                Writer output = resetResponse(response, servletResponseWrapper, "true");
                String message = Messages.getMessage(Messages.AJAX_VIEW_EXPIRED);

                response.setHeader(AJAX_EXPIRED, message);
                output.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head>" + "<meta name=\""
                    + AjaxContainerRenderer.AJAX_FLAG_HEADER + "\" content=\"true\" />" + "<meta name=\""
                    + AJAX_EXPIRED + "\" content=\"" + message + "\" />" + "</head></html>");
                output.flush();
                response.flushBuffer();

                return;
            } else {
                LOG.error("Exception in the filter chain", e);

                throw e;
            }
        } finally {
            request.removeAttribute(BaseFilter.RESPONSE_WRAPPER_ATTRIBUTE);
        }

        String viewId = (String) request.getAttribute(AjaxViewHandler.VIEW_ID_KEY);
        HtmlParser parser = null;

        // setup response
        // Redirect in AJAX request - convert to special response recognized by
        // client.
        String redirectLocation = servletResponseWrapper.getRedirectLocation();
        String characterEncoding = servletResponseWrapper.getCharacterEncoding();
        Writer output;

        if (null != redirectLocation) {
            if (isAjaxRequest(request)) {

                // Special handling of redirect - client-side script must
                // Check for response and perform redirect by window.location
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Create AJAX redirect response to url: " + redirectLocation);
                }

                output = resetResponse(response, servletResponseWrapper, "redirect");
                response.setHeader(AjaxContainerRenderer.AJAX_LOCATION_HEADER, redirectLocation);

                // For buggy XmlHttpRequest realisations repeat headers in
                // <meta>
                output.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head>" + "<meta name=\""
                    + AjaxContainerRenderer.AJAX_FLAG_HEADER + "\" content=\"redirect\" />" + "<meta name=\""
                    + AjaxContainerRenderer.AJAX_LOCATION_HEADER + "\" content=\"" + redirectLocation
                    + "\" />" + "</head></html>");
                output.flush();
                response.flushBuffer();
            } else {
                response.sendRedirect(redirectLocation);
            }

            return;
        } else {
            if ("true".equals(servletResponseWrapper.getHeaders().get(AjaxContainerRenderer.AJAX_FLAG_HEADER))) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Process response to well-formed XML for AJAX XMLHttpRequest parser");
                }

                // Not caching AJAX request
                response.setHeader("Cache-Control", "no-cache, must-revalidate, max_age=0, no-store");
                response.setHeader("Expires", "0");
                response.setHeader("Pragma", "no-cache");

                // response.setCharacterEncoding(servletResponseWrapper
                // .getCharacterEncoding()); //
                // JSContentHandler.DEFAULT_ENCODING);
                // Set the content-type. For AJAX responses default encoding -
                // UTF8.
                // TODO - for null encoding, setup only Output encoding for
                // filter ?
                String outputEncoding = "UTF-8";
                String contentType = getMimetype() + ";charset=" + outputEncoding;

                response.setContentType(contentType);
                parser = getParser(getMimetype(), true, viewId);

                if (null == parser) {
                    throw new ServletException(Messages.getMessage(Messages.PARSER_NOT_INSTANTIATED_ERROR,
                        contentType));
                }

                output = createOutputWriter(response, outputEncoding);
                parser.setDoctype(getPublicid());
                parser.setInputEncoding(characterEncoding);
                parser.setOutputEncoding(outputEncoding);
                parser.setViewState((String) request.getAttribute(AjaxViewHandler.SERIALIZED_STATE_KEY));
            } else {

                // setup conversion reules for output contentType, send directly
                // if content not
                // supported by tidy.
                String contentType = servletResponseWrapper.getContentType();
                String contentTypeCharset = contentType;

                if (LOG.isDebugEnabled()) {
                    LOG.debug("create HTML/XML parser for content type: " + contentType);
                }

                // if(contentType == null){
                // contentType = request.getContentType();
                // }
                boolean forcenotrf = isForcenotrf();

                if (forcenotrf || !servletResponseWrapper.isError()) {
                    if (forcenotrf) {
                        if (contentTypeCharset != null) {
                            if ((contentTypeCharset.indexOf("charset") < 0) && (null != characterEncoding)) {
                                contentTypeCharset += ";charset=" + characterEncoding;
                            }

                            parser = getParser(contentTypeCharset, false, viewId);

                            if (null == parser) {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("Parser not have support for the such content type, send response as-is");
                                }
                            }
                        }
                    } else {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("No resource inclusions detected, send response as-is");
                        }
                    }
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Servlet error occured, send response as-is");
                    }
                }

                // null or unsupported content type
                if (null == parser) {
                    try {
                        if (servletResponseWrapper.isUseWriter()) {
                            if (contentTypeCharset != null) {
                                response.setContentType(contentTypeCharset);
                            }

                            output = createOutputWriter(response, characterEncoding);
                            servletResponseWrapper.sendContent(output);
                        } else if (servletResponseWrapper.isUseStream()) {
                            if (contentType != null) {
                                response.setContentType(contentType);
                            }

                            ServletOutputStream out = response.getOutputStream();

                            servletResponseWrapper.sendContent(out);
                        }
                    } finally {

                        // reuseWrapper(servletResponseWrapper);
                    }

                    return;
                }

                if (contentTypeCharset != null) {
                    response.setContentType(contentTypeCharset);
                }

                output = createOutputWriter(response, characterEncoding);
                parser.setInputEncoding(characterEncoding);
                parser.setOutputEncoding(characterEncoding);
            }
        }

        try {

            // Process parsing.
            long startTimeMills = System.currentTimeMillis();

            servletResponseWrapper.parseContent(output, parser);

            if (LOG.isDebugEnabled()) {
                startTimeMills = System.currentTimeMillis() - startTimeMills;
                LOG.debug(Messages.getMessage(Messages.PARSING_TIME_INFO, "" + startTimeMills));
            }
        } catch (Exception e) {
            throw new ServletException(Messages.getMessage(Messages.JTIDY_PARSING_ERROR), e);
        } finally {
            reuseParser(parser);
        }
    }

    /**
     * @param response
     * @param servletResponseWrapper
     * @param ajaxResponseType
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private Writer resetResponse(final HttpServletResponse response,
                                 FilterServletResponseWrapper servletResponseWrapper, String ajaxResponseType)
        throws IOException, UnsupportedEncodingException {

        response.reset();

        // Keep cookies.
        for (Cookie cookie : servletResponseWrapper.getCookies()) {
            response.addCookie(cookie);
        }

        // Copy response headers
        Map<String, Object> headers = servletResponseWrapper.getHeaders();

        for (Map.Entry<String, Object> header : headers.entrySet()) {
            response.setHeader(header.getKey(), (String) header.getValue());
        }

        response.setHeader(AjaxContainerRenderer.AJAX_FLAG_HEADER, ajaxResponseType);

        // Not caching AJAX request
        response.setHeader("Cache-Control", "no-cache, must-revalidate, max_age=0, no-store");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType(getMimetype() + ";charset=UTF-8");

        return createOutputWriter(response, "UTF-8");
    }

    /**
     * Check for a {@link ViewExpiredException} in the exception chain.
     *
     * @param e exception from filter chain
     * @return true if any exception in the chain instance of the {@link ViewExpiredException}
     */
    private boolean isViewExpired(Throwable e) {
        while (null != e) {
            if (e instanceof ViewExpiredException) {
                return true;
            } else {
                e = e.getCause();
            }
        }

        return false;
    }

    /**
     * @param response
     * @return
     * @throws ServletException
     */
    protected FilterServletResponseWrapper getWrapper(HttpServletResponse response) throws ServletException {
        return new FilterServletResponseWrapper(response);
    }

    /**
     * @param request
     * @return
     */
    protected boolean isAjaxRequest(ServletRequest request) {
        try {
            return null != request.getParameter(AjaxContainerRenderer.AJAX_PARAMETER_NAME);
        } catch (Exception e) {

            // OCJ 10 - throw exception for static resources.
            return false;
        }
    }

    /**
     * @param response
     * @param characterEncoding
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private Writer createOutputWriter(final HttpServletResponse response, String characterEncoding)
        throws IOException, UnsupportedEncodingException {

        Writer output;

        try {
            output = response.getWriter();
        } catch (IllegalStateException e) {
            if (null != characterEncoding) {
                output = new OutputStreamWriter(response.getOutputStream(), characterEncoding);
            } else {
                output = new OutputStreamWriter(response.getOutputStream());
            }
        }

        return output;
    }

    protected abstract void reuseParser(HtmlParser parser);

    protected abstract HtmlParser getParser(String mimetype, boolean isAjax, String viewId);

    /**
     * @param publicid The publicid to set.
     */
    protected void setPublicid(String publicid) {
        this.publicid = publicid;
    }

    /**
     * @return Returns the publicid.
     */
    public String getPublicid() {
        return publicid;
    }

    /**
     * @param systemid The systemid to set.
     */
    protected void setSystemid(String systemid) {
        this.systemid = systemid;
    }

    /**
     * @return Returns the systemid.
     */
    public String getSystemid() {
        return systemid;
    }

    /**
     * @param namespace The namespace to set.
     */
    protected void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * @return Returns the namespace.
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * @param mimetype The mimetype to set.
     */
    protected void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    /**
     * @return Returns the mimetype.
     */
    public String getMimetype() {
        return mimetype;
    }

    /**
     * @return Returns the forcexml.
     */
    public boolean isForcexml() {
        return this.forcexml;
    }

    public boolean isForcenotrf() {
        return this.forceNotRf;
    }

    /**
     * @param forcexml The forcexml to set.
     */
    protected void setForcexml(boolean forcexml) {
        this.forcexml = forcexml;
    }

    protected void setForcenotrf(boolean forcenotrf) {
        this.forceNotRf = forcenotrf;
    }

    private Object nz(Object param, Object def) {
        return (param != null) ? param : def;
    }
}
