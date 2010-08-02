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

import org.ajax4jsf.Messages;
import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.renderkit.AjaxContainerRenderer;
import org.ajax4jsf.request.MultipartRequest;
import org.ajax4jsf.resource.InternetResourceService;
import org.richfaces.component.FileUploadConstants;
import org.richfaces.log.RichfacesLogger;
import org.slf4j.Logger;

import javax.faces.application.ViewHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Base class for request processing filters, with convert Htmp content to XML
 * for ajax requests, and serve request to application off-page resources
 *
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:58:21 $
 */
public abstract class BaseFilter implements Filter {
    public static final String ABSOLUTE_TAGS_PARAMETER = "absolute-attributes";
    public static final String AJAX_PUSH_KEY_HEADER = "Ajax-Push-Key";
    public static final String AJAX_PUSH_READY = "READY";
    public static final String AJAX_PUSH_STATUS_HEADER = "Ajax-Push-Status";
    public static final String CACHEABLE_PREFIX = "/cache";
    public static final String DATA_PARAMETER = "DATA";
    public static final String DEFAULT_SERVLET_PATH = "/resource";

    // private static final Pattern rendererPattern =
    // Pattern.compile(RENDERER_PREFIX+"/([^/]+)/([^/]+)/([^/]+)/(.*)");
    // private static final Pattern builderPattern =
    // Pattern.compile(CACHEABLE_PREFIX+"/(.*)");
    public static final String FILTER_PERFORMED = "com.exade.vcp.Filter.done";

    /**
     * Multipart request start
     */
    public static final String MULTIPART = "multipart/";

    // private WebXml webXml;
    // private String xsl;
    // private Templates xslTemplates;

    public static final String RENDERER_PREFIX = "/renderer";
    public static final String RESPONSE_WRAPPER_ATTRIBUTE = "com.exade.vcp.Filter.ResponseWrapper";
    public static final String REWRITEID_PARAMETER = "rewriteid";

    /**
     * Request parameter that allow to send HTTP error instead of html message
     */
    public static final String SEND_HTTP_ERROR = "_richfaces_send_http_error";
    public static final String STYLESHEET_PARAMETER = "xsl";
    public static final boolean DEBUG = true;

    private static final String FUNCTION_NAME_PARAMETER = "function";
    private static final String MYFACES_DOFILTER_CALLED =
        "org.apache.myfaces.component.html.util.ExtensionFilter.doFilterCalled";

    private static final Logger LOG = RichfacesLogger.WEBAPP.getLogger();
    private static final long serialVersionUID = -2295534611886142935L;
    private static final String MYFACES_FILTER_CHECKED = BaseFilter.class.getName() + ":MyFacesFilterChecked";
    private static final Pattern AMPERSAND = Pattern.compile("&+");

    protected InternetResourceService resourceService = null;
    protected BaseXMLFilter xmlFilter = null;
    protected PollEventsManager eventsManager;

    private String function = "alert('Data received');JSHttpRequest.dataReady";

    /**
     * The maximum size of a file upload request. 0 means no limit.
     */
    private int maxRequestSize = 0;
    private boolean rewriteid = false;
    private boolean myfacesMessagePrinted = false;

    /**
     * Flag indicating whether a temporary file should be used to cache the
     * uploaded file
     */
    private boolean createTempFiles = false;
    private String attributesNames;
    private FilterConfig filterConfig;

    /**
     * Initialize the filter.
     */
    public void init(FilterConfig config) throws ServletException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Init ajax4jsf filter with nane: " + config.getFilterName());

            Enumeration<String> parameterNames = config.getInitParameterNames();
            StringBuffer parameters = new StringBuffer("Init parameters :\n");

            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();

                parameters.append(name).append(" : '").append(config.getInitParameter(name)).append('\n');
            }

            LOG.debug(parameters.toString());

            // log.debug("Stack Trace", new Exception());
        }

        // Save config
        filterConfig = config;
        setFunction((String) nz(filterConfig.getInitParameter(FUNCTION_NAME_PARAMETER), getFunction()));
        setAttributesNames(filterConfig.getInitParameter(ABSOLUTE_TAGS_PARAMETER));
        xmlFilter.init(config);

        if ("true".equalsIgnoreCase(filterConfig.getInitParameter(REWRITEID_PARAMETER))) {
            this.setRewriteid(true);
        }

        resourceService = new InternetResourceService();

        // Caching initialization.
        resourceService.init(filterConfig);
        eventsManager = new PollEventsManager();
        eventsManager.init(filterConfig.getServletContext());

        String param = filterConfig.getInitParameter("createTempFiles");

        if (param != null) {
            this.createTempFiles = Boolean.parseBoolean(param);
        } else {
            this.createTempFiles = true;
        }

        param = filterConfig.getInitParameter("maxRequestSize");

        if (param != null) {
            this.maxRequestSize = Integer.parseInt(param);
        }
    }

    private Map<String, String> parseQueryString(String queryString) {
        if (queryString != null) {
            Map<String, String> parameters = new HashMap<String, String>();
            String[] nvPairs = AMPERSAND.split(queryString);

            for (String nvPair : nvPairs) {
                if (nvPair.length() == 0) {
                    continue;
                }

                int eqIdx = nvPair.indexOf('=');

                if (eqIdx >= 0) {
                    try {
                        String name = URLDecoder.decode(nvPair.substring(0, eqIdx), "UTF-8");

                        if (!parameters.containsKey(name)) {
                            String value = URLDecoder.decode(nvPair.substring(eqIdx + 1), "UTF-8");

                            parameters.put(name, value);
                        }
                    } catch (UnsupportedEncodingException e) {

                        // log warning and skip this parameter
                        LOG.warn(e.getLocalizedMessage(), e);
                    }
                }
            }

            return parameters;
        } else {
            return Collections.EMPTY_MAP;
        }
    }

    private boolean isMultipartRequest(HttpServletRequest request) {
        if (!"post".equals(request.getMethod().toLowerCase())) {
            return false;
        }

        String contentType = request.getContentType();

        if (contentType == null) {
            return false;
        }

        if (contentType.toLowerCase().startsWith(MULTIPART)) {
            return true;
        }

        return false;
    }

    private boolean isFileSizeRestricted(ServletRequest request, int maxSize) {
        if ((maxSize != 0) && (request.getContentLength() > maxSize)) {
            return true;
        }

        return false;
    }

    private boolean checkFileCount(HttpServletRequest request, String idParameter) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            Map<String, Integer> map = (Map<String,
                Integer>) session.getAttribute(FileUploadConstants.UPLOADED_COUNTER);

            if (map != null) {
                String id = idParameter;

                if (id != null) {
                    Integer i = map.get(id);

                    if ((i != null) && (i == 0)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void printResponse(ServletResponse response, String message) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.setContentType("text/html");

        PrintWriter writer = httpResponse.getWriter();

        writer.write(message);
        writer.close();
    }

    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        // check ajax request parameter
        // TODO - check for JSF page.
        if (true) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getMessage(Messages.FILTER_XML_OUTPUT));
            }

            // Execute the rest of the filter chain, including the
            // JSP
            xmlFilter.doXmlFilter(chain, request, response);
        } else {

            // normal request, execute chain ...
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getMessage(Messages.FILTER_NO_XML_CHAIN));
            }

            chain.doFilter(request, response);
        }
    }

    private void checkMyFacesExtensionsFilter(HttpServletRequest request) {
        if (request.getAttribute(MYFACES_FILTER_CHECKED) == null) {
            if (request.getAttribute(MYFACES_DOFILTER_CALLED) != null) {
                if (!this.myfacesMessagePrinted) {
                    LOG.warn(
                        "MyFaces Extensions Filter should be configured to execute *AFTER* RichFaces filter. Refer to "
                            + "SRV.6.2.4 section of Servlets specification on how to achieve that.");
                    this.myfacesMessagePrinted = true;
                }
            }

            request.setAttribute(MYFACES_FILTER_CHECKED, Boolean.TRUE);
        }
    }

    /**
     * Method catches upload files request. Request parameter
     * <b>org.ajax4jsf.Filter.UPLOAD_FILES_ID</b> indicates if request
     * generated by rich-upload component. If it was detected custom parsing
     * request should be done. Processing information about percent of
     * completion and file size will be put into session scope.
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    protected void processUploadsAndHandleRequest(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Map<String, String> queryParamMap = parseQueryString(httpRequest.getQueryString());
        String uid = queryParamMap.get(FileUploadConstants.UPLOAD_FILES_ID);

        if (uid != null) {
            if (isMultipartRequest(httpRequest)) {
                MultipartRequest multipartRequest = new MultipartRequest(httpRequest, createTempFiles, maxRequestSize,
                    uid);
                Object oldAttributeValue =
                    httpRequest.getAttribute(FileUploadConstants.FILE_UPLOAD_REQUEST_ATTRIBUTE_NAME);

                httpRequest.setAttribute(FileUploadConstants.FILE_UPLOAD_REQUEST_ATTRIBUTE_NAME, multipartRequest);

                try {
                    if (isFileSizeRestricted(request, maxRequestSize)) {
                        boolean sendError = Boolean.parseBoolean(queryParamMap.get(SEND_HTTP_ERROR));

                        if (sendError) {
                            response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
                            System.err.println(
                                "ERROR " + HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE
                                    + "request entity is larger than the server is willing or able to process.");

                            return;
                        } else {
                            printResponse(response, "<html id=\"_richfaces_file_upload_size_restricted\"></html>");
                        }
                    } else if (!checkFileCount(httpRequest, queryParamMap.get("id"))) {
                        printResponse(response, "<html id=\"_richfaces_file_upload_forbidden\"></html>");
                    } else {
                        handleRequest(multipartRequest, getHttpServletResponse(response, multipartRequest), chain);

                        if (!multipartRequest.isDone()) {
                            printResponse(response, "<html id=\"_richfaces_file_upload_stopped\"></html>");
                        }
                    }
                } finally {
                    httpRequest.setAttribute(FileUploadConstants.FILE_UPLOAD_REQUEST_ATTRIBUTE_NAME, oldAttributeValue);
                    multipartRequest.clearRequestData();
                }
            } else {
                handleRequest(request, response, chain);
            }
        } else {
            handleRequest(request, response, chain);
        }
    }

    private HttpServletResponse getHttpServletResponse(final HttpServletResponse response,
                                                       MultipartRequest multipartRequest) {
        if (multipartRequest.isFormUpload()) {
            return response;
        }

        return new HttpServletResponseWrapper((HttpServletResponse) response) {
            @Override
            public void setContentType(String type) {
                super.setContentType(BaseXMLFilter.TEXT_HTML + ";charset=UTF-8");
            }
        };
    }

    /**
     * @param httpServletRequest
     * @throws UnsupportedEncodingException
     */
    protected void setupRequestEncoding(HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String contentType = httpServletRequest.getHeader("Content-Type");
        String characterEncoding = lookupCharacterEncoding(contentType);

        if (characterEncoding == null) {
            HttpSession session = httpServletRequest.getSession(false);

            if (session != null) {
                characterEncoding = (String) session.getAttribute(ViewHandler.CHARACTER_ENCODING_KEY);
            }

            if (characterEncoding != null) {
                httpServletRequest.setCharacterEncoding(characterEncoding);
            }
        }
    }

    /**
     * Detect request encoding from Content-Type header
     *
     * @param contentType
     * @return - charset, if present.
     */
    private String lookupCharacterEncoding(String contentType) {
        String characterEncoding = null;

        if (contentType != null) {
            int charsetFind = contentType.indexOf("charset=");

            if (charsetFind != -1) {
                if (charsetFind == 0) {

                    // charset at beginning of Content-Type, curious
                    characterEncoding = contentType.substring(8);
                } else {
                    char charBefore = contentType.charAt(charsetFind - 1);

                    if ((charBefore == ';') || Character.isWhitespace(charBefore)) {

                        // Correct charset after mime type
                        characterEncoding = contentType.substring(charsetFind + 8);
                    }
                }

                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.getMessage(Messages.CONTENT_TYPE_ENCODING, characterEncoding));
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.getMessage(Messages.CONTENT_TYPE_NO_ENCODING, contentType));
                }
            }
        }

        return characterEncoding;
    }

    /**
     * @param def
     * @param param
     * @return
     */
    private Object nz(Object param, Object def) {
        return (param != null) ? param : def;
    }

    /**
     * Execute the filter.
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        long startTimeMills = 0;

        // Detect case of request - normal, AJAX, AJAX - JavaScript
        // TODO - detect first processing in filter.
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (LOG.isDebugEnabled()) {
            startTimeMills = System.currentTimeMillis();
            LOG.debug(Messages.getMessage(Messages.FILTER_START_INFO, new Date(startTimeMills),
                httpServletRequest.getRequestURI()));
        }

        if (request.getAttribute(FILTER_PERFORMED) != Boolean.TRUE) {

            // mark - and not processing same request twice.
            try {
                request.setAttribute(FILTER_PERFORMED, Boolean.TRUE);

                String ajaxPushHeader = httpServletRequest.getHeader(AJAX_PUSH_KEY_HEADER);

                // check for a push check request.
                if (httpServletRequest.getMethod().equals("HEAD") && (null != ajaxPushHeader)) {
                    PushEventsCounter listener = eventsManager.getListener(ajaxPushHeader);

                    // To avoid XmlHttpRequest parsing exceptions.
                    httpServletResponse.setContentType("text/plain");

                    if (listener.isPerformed()) {
                        listener.processed();
                        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                        httpServletResponse.setHeader(AJAX_PUSH_STATUS_HEADER, AJAX_PUSH_READY);

                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Occurs event for a id " + ajaxPushHeader);
                        }
                    } else {

                        // Response code - 'No content'
                        httpServletResponse.setStatus(HttpServletResponse.SC_ACCEPTED);

                        if (LOG.isDebugEnabled()) {
                            LOG.debug("No event for a id " + ajaxPushHeader);
                        }
                    }

                    httpServletResponse.setContentLength(0);
                } else {

                    // check for resource request
                    // if (!getResourceService().serviceResource(httpServletRequest,
                    // httpServletResponse)) {
                    // Not request to resource - perform filtering.
                    // first stage - detect/set encoding of request. Same as in
                    // Myfaces External Context.
                    setupRequestEncoding(httpServletRequest);
                }

                checkMyFacesExtensionsFilter(httpServletRequest);
                processUploadsAndHandleRequest(httpServletRequest, httpServletResponse, chain);

                // }
            } finally {

                // Remove filter marker from response, to enable sequence calls ( for example, forward to error page )
                request.removeAttribute(FILTER_PERFORMED);

                Object ajaxContext = request.getAttribute(AjaxContext.AJAX_CONTEXT_KEY);

                if ((null != ajaxContext) && (ajaxContext instanceof AjaxContext)) {
                    ((AjaxContext) ajaxContext).release();
                    request.removeAttribute(AjaxContext.AJAX_CONTEXT_KEY);
                }
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getMessage(Messages.FILTER_NO_XML_CHAIN_2));
            }

            chain.doFilter(request, response);
        }

        if (LOG.isDebugEnabled()) {
            startTimeMills = System.currentTimeMillis() - startTimeMills;
            LOG.debug(Messages.getMessage(Messages.FILTER_STOP_INFO, "" + startTimeMills,
                httpServletRequest.getRequestURI()));
        }
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
     * Destroy the filter.
     */
    public void destroy() {
    }

    /**
     * @return Returns the servletContext.
     */
    ServletContext getServletContext() {
        return filterConfig.getServletContext();
    }

    /**
     * @return the resourceService
     * @throws ServletException
     */
    protected synchronized InternetResourceService getResourceService() throws ServletException {

        // if (resourceService == null) {
        // resourceService = new InternetResourceService();
        // // Caching initialization.
        // resourceService.init(filterConfig);
        //
        // }
        return resourceService;
    }

    /**
     * @param function The function to set.
     */
    protected void setFunction(String function) {
        this.function = function;
    }

    /**
     * @return Returns the function.
     */
    protected String getFunction() {
        return function;
    }

    /**
     * @param rewriteid The rewriteid to set.
     */
    protected void setRewriteid(boolean rewriteid) {
        this.rewriteid = rewriteid;
    }

    /**
     * @return Returns the rewriteid.
     */
    protected boolean isRewriteid() {
        return rewriteid;
    }

    /**
     * @param attributesNames The attributesNames to set.
     */
    protected void setAttributesNames(String attributesNames) {
        this.attributesNames = attributesNames;
    }

    /**
     * @return Returns the attributesNames.
     */
    protected String getAttributesNames() {
        return attributesNames;
    }
}
