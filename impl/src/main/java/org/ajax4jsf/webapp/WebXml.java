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

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.ajax4jsf.config.WebXMLParser;
import org.richfaces.VersionBean;
import org.richfaces.VersionBean.Version;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * Parse at startup application web.xml and store servlet and filter mappings.
 * at runtime, used for convert resource key to uri, and vice versa.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:58:59 $
 */
public class WebXml extends WebXMLParser implements Serializable {
    public static final String CONTEXT_ATTRIBUTE = WebXml.class.getName();
    public static final String GLOBAL_RESOURCE_URI_PREFIX = "a4j/g";
    public static final String GLOBAL_RESOURCE_URI_PREFIX_PARAM = "org.ajax4jsf.GLOBAL_RESOURCE_URI_PREFIX";
    public static final String RESOURCE_URI_PREFIX = "a4j";
    public static final String RESOURCE_URI_PREFIX_PARAM = "org.ajax4jsf.RESOURCE_URI_PREFIX";
    public static final String SESSION_RESOURCE_URI_PREFIX = "a4j/s";
    public static final String SESSION_RESOURCE_URI_PREFIX_PARAM = "org.ajax4jsf.SESSION_RESOURCE_URI_PREFIX";
    public static final String GLOBAL_RESOURCE_URI_PREFIX_VERSIONED;
    public static final String RESOURCE_URI_PREFIX_VERSIONED;
    public static final String SESSION_RESOURCE_URI_PREFIX_VERSIONED;

    private static final long serialVersionUID = -9042908418843695017L;
    private static final Logger LOG = RichfacesLogger.WEBAPP.getLogger();

    static {
        VersionBean versionBean = new VersionBean();
        Version version = versionBean.getVersion();
        String suffix = "/" + version.getResourceVersion();

        // that's to prevent static compile-time linkage to constant values
        RESOURCE_URI_PREFIX_VERSIONED = RESOURCE_URI_PREFIX + suffix;
        GLOBAL_RESOURCE_URI_PREFIX_VERSIONED = GLOBAL_RESOURCE_URI_PREFIX + suffix;
        SESSION_RESOURCE_URI_PREFIX_VERSIONED = SESSION_RESOURCE_URI_PREFIX + suffix;
    }

    protected boolean prefixMapping = false;

    /**
     * Prefix for resources handled by Chameleon framework.
     */
    String resourcePrefix = RESOURCE_URI_PREFIX;
    String globalResourcePrefix;
    String sessionResourcePrefix;

    public static WebXml getInstance() {
        return getInstance(FacesContext.getCurrentInstance());
    }

    public static WebXml getInstance(FacesContext context) {
        return (WebXml) context.getExternalContext().getApplicationMap().get(WebXml.CONTEXT_ATTRIBUTE);
    }

    @Override
    public void init(ServletContext context, String filterName) throws ServletException {
        super.init(context, filterName);
        setupResourcePrefixes(context);

        // Store Instance to context attribute.
        context.setAttribute(CONTEXT_ATTRIBUTE, this);
    }

    /**
     * Convert {@link org.ajax4jsf.resource.InternetResource } key to real URL
     * for handle by chameleon filter, depend of mapping in WEB.XML . For prefix
     * or * mapping, prepend servlet prefix and default Resource prefix to key.
     * For suffix mapping, prepend with resource prefix and append default faces
     * suffix to URL ( before request param ). After conversion, call
     * {@link javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext, java.lang.String)}
     * and
     * {@link javax.faces.context.ExternalContext#encodeResourceURL(java.lang.String)} .
     *
     * @param context
     * @param url
     * @return
     */
    public String getFacesResourceURL(FacesContext context, String url, boolean isGlobal) {
        StringBuffer buf = new StringBuffer();

        buf.append(isGlobal ? getGlobalResourcePrefix() : getSessionResourcePrefix()).append(url);

        // Insert suffix mapping
        if (isPrefixMapping()) {
            buf.insert(0, getFacesFilterPrefix());
        } else {
            int index = buf.indexOf("?");

            if (index >= 0) {
                buf.insert(index, getFacesFilterSuffix());
            } else {
                buf.append(getFacesFilterSuffix());
            }
        }

        String resourceURL = context.getApplication().getViewHandler().getResourceURL(context, buf.toString());

        return resourceURL;
    }

    @Deprecated
    public String getFacesResourceURL(FacesContext context, String url) {
        return getFacesResourceURL(context, url, false);
    }

    public String getFacesResourceKey(String resourcePath) {
        String workingResourcePath = resourcePath;

        // Remove JSESSIONID - for expired sessions it will merged to path.
        int jsesionidStart = workingResourcePath.lastIndexOf(";jsessionid");

        if (jsesionidStart >= 0) {
            workingResourcePath = workingResourcePath.substring(0, jsesionidStart);
        }

        String resourcePrefix = getResourcePrefix();

        if (isPrefixMapping()) {
            String facesFilterPrefix = getFacesFilterPrefix();

            if (workingResourcePath.startsWith(facesFilterPrefix)) {
                String sessionResourcePrefix = getSessionResourcePrefix();

                if (workingResourcePath.startsWith(sessionResourcePrefix, facesFilterPrefix.length())) {
                    return workingResourcePath.substring(facesFilterPrefix.length() + sessionResourcePrefix.length());
                } else {
                    String globalResourcePrefix = getGlobalResourcePrefix();

                    if (!sessionResourcePrefix.equals(globalResourcePrefix)
                        && workingResourcePath.startsWith(globalResourcePrefix, facesFilterPrefix.length())) {
                        return workingResourcePath.substring(facesFilterPrefix.length()
                            + globalResourcePrefix.length());
                    } else if (!globalResourcePrefix.equals(resourcePrefix)
                        && workingResourcePath.startsWith(resourcePrefix, facesFilterPrefix.length())) {
                        return workingResourcePath.substring(facesFilterPrefix.length() + resourcePrefix.length());
                    }
                }
            }
        } else {
            String sessionResourcePrefix = getSessionResourcePrefix();

            if (workingResourcePath.startsWith(sessionResourcePrefix)) {
                return workingResourcePath.substring(sessionResourcePrefix.length(),
                    workingResourcePath.length() - getFacesFilterSuffix().length());
            } else {
                String globalResourcePrefix = getGlobalResourcePrefix();

                if (!sessionResourcePrefix.equals(globalResourcePrefix)
                    && workingResourcePath.startsWith(globalResourcePrefix)) {
                    return workingResourcePath.substring(globalResourcePrefix.length(),
                        workingResourcePath.length() - getFacesFilterSuffix().length());
                } else if (!globalResourcePrefix.equals(resourcePrefix)
                    && workingResourcePath.startsWith(resourcePrefix)) {
                    return workingResourcePath.substring(resourcePrefix.length(),
                        workingResourcePath.length() - getFacesFilterSuffix().length());
                }
            }
        }

        return null;
    }

    /**
     * Detect request to resource and extract key from request
     *
     * @param request current http request
     * @return resource key, or null for ordinary faces request.
     */
    public String getFacesResourceKey(HttpServletRequest request) {
        String resourcePath = request.getRequestURI().substring(request.getContextPath().length());

        // isPrefixMapping()?request.getPathInfo():request.getServletPath();

        return getFacesResourceKey(resourcePath);
    }

    /**
     * Detect request to {@link javax.faces.webapp.FacesServlet}
     *
     * @param request
     * @return true if request parsed to JSF.
     */
    public boolean isFacesRequest(HttpServletRequest request) {

        // String resourcePath =
        // request.getRequestURI().substring(request.getContextPath().length());
        // //isPrefixMapping()?request.getPathInfo():request.getServletPath();
        // if(isPrefixMapping() ) {
        // if (resourcePath.startsWith(getFacesFilterPrefix())) {
        // return true;
        // }
        // } else if (resourcePath.endsWith(getFacesFilterSuffix())) {
        // return true;
        // }
        // return false;
        return true;
    }

    /**
     * @return Returns the resourcePrefix.
     */
    @Deprecated
    public String getResourcePrefix() {
        return resourcePrefix;
    }

    /**
     * @return
     * @since 3.2.2
     */
    public String getGlobalResourcePrefix() {
        return globalResourcePrefix;
    }

    /**
     * @return
     * @since 3.2.2
     */
    public String getSessionResourcePrefix() {
        return sessionResourcePrefix;
    }

    /**
     * @return Returns the prefixMapping.
     */
    public boolean isPrefixMapping() {
        return prefixMapping;
    }

    /**
     * @param resourcePrefix The resourcePrefix to set.
     */
    @Deprecated
    void setResourcePrefix(String resourcePrefix) {
        this.resourcePrefix = resourcePrefix;
    }

    /**
     * @param resourcePrefix The resourcePrefix to set.
     * @since 3.2.2
     */
    void setGlobalResourcePrefix(String resourcePrefix) {
        globalResourcePrefix = resourcePrefix;
    }

    /**
     * @param resourcePrefix The resourcePrefix to set.
     * @since 3.2.2
     */
    void setSessionResourcePrefix(String resourcePrefix) {
        sessionResourcePrefix = resourcePrefix;
    }

    /**
     * @param context
     */
    void setupResourcePrefixes(ServletContext context) {
        String globalResourcePrefix = (String) context.getInitParameter(GLOBAL_RESOURCE_URI_PREFIX_PARAM);
        String sessionResourcePrefix = (String) context.getInitParameter(SESSION_RESOURCE_URI_PREFIX_PARAM);
        String resourcePrefix = (String) context.getInitParameter(RESOURCE_URI_PREFIX_PARAM);

        if (null != resourcePrefix) {
            if (globalResourcePrefix == null) {

                // TODO overriden
                globalResourcePrefix = resourcePrefix;
            }

            if (sessionResourcePrefix == null) {

                // TODO overriden
                sessionResourcePrefix = resourcePrefix;
            }
        } else {
            resourcePrefix = RESOURCE_URI_PREFIX_VERSIONED;
        }

        if (globalResourcePrefix == null) {

            // TODO overriden
            globalResourcePrefix = GLOBAL_RESOURCE_URI_PREFIX_VERSIONED;
        }

        if (sessionResourcePrefix == null) {

            // TODO overriden
            sessionResourcePrefix = SESSION_RESOURCE_URI_PREFIX_VERSIONED;
        }

        if (null != getFacesFilterPrefix()) {
            prefixMapping = true;

            if (getFacesFilterPrefix().endsWith("/")) {
                setGlobalResourcePrefix(globalResourcePrefix);
                setSessionResourcePrefix(sessionResourcePrefix);
                setResourcePrefix(resourcePrefix);
            } else {
                setGlobalResourcePrefix("/" + globalResourcePrefix);
                setSessionResourcePrefix("/" + sessionResourcePrefix);
                setResourcePrefix("/" + resourcePrefix);
            }
        } else if (null != getFacesFilterSuffix()) {
            prefixMapping = false;
            setResourcePrefix("/" + resourcePrefix);
            setGlobalResourcePrefix("/" + globalResourcePrefix);
            setSessionResourcePrefix("/" + sessionResourcePrefix);
        }
    }
}
