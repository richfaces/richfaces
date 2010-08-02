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



package org.ajax4jsf.resource;

import java.io.IOException;
import java.io.InputStream;

import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * Interface for work with off-page internet resources ( images, scripts, styles etc. )
 * All instances must work in two phases - render page ( application set HTML code and attributes to reference resource )
 * and in concrete request to resource body.
 * For best performance, preffer to  realise it as "lightweght" pattern.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: nick_belaevski $)
 * @version $Revision: 1.1.2.2 $ $Date: 2007/01/11 16:52:15 $
 *
 */
public interface InternetResource {
    public static final String CODEC_ATTR = "org.ajax4jsf.resource.CODEC";
    public static final String DATA_SEPARATOR = "/DATA/";
    public static final String DEFAULT_EXPITE_PARAMETER = "org.ajax4jsf.DEFAULT_EXPIRE";
    public static final int DEFAULT_TTL = 60 * 60 * 24; // 1 day
    public static final String ENCODE_PASS_PARAMETER = "org.ajax4jsf.ENCRYPT_PASSWORD";
    public static final String ENCODE_URI_PARAMETER = "org.ajax4jsf.ENCRYPT_RESOURCE_DATA";
    public static final String RESOURCE_PROTOCOL = "resource";
    public static final String RESOURCE_URI_PREFIX = "resource://";
    public static final int RESOURCE_URI_PREFIX_LENGTH = RESOURCE_URI_PREFIX.length();
    public static final long DEFAULT_EXPIRE = 1000L * DEFAULT_TTL; // 1 day

    /**
     * @param resourceContext current {@link ResourceContext}
     * @return Returns the contentLength.
     */
    public int getContentLength(ResourceContext resourceContext);

    /**
     * @param resourceContext current {@link ResourceContext}
     * @return Returns the expired.
     */
    public long getExpired(ResourceContext resourceContext);

    /**
     * @param resourceContext current {@link ResourceContext}
     * @return Returns the lastModified.
     */
    public Date getLastModified(ResourceContext resourceContext);

    /**
     * @param resourceContext current {@link ResourceContext}
     * @return Returns the cacheable.
     */
    public boolean isCacheable(ResourceContext resourceContext);

    /**
     * @param resourceContext current {@link ResourceContext}
     * @return Returns the mimeType.
     */
    public String getContentType(ResourceContext resourceContext);

    /**
     * @param context TODO
     * @return input stream with resource
     */
    public InputStream getResourceAsStream(ResourceContext context);

    /**
     * @return string with URI for get resource from  page.
     */
    public String getUri(FacesContext context, Object data);

    /**
     * Encode resource as Markup .
     * @param context
     * @param data
     * @throws IOException
     */
    public void encode(FacesContext context, Object data) throws IOException;

    /**
     * Encode resource as Markup with custom attributes  .
     * @param context
     * @param data
     * @param attributes
     * @throws IOException
     */
    public void encode(FacesContext context, Object data, Map<String, Object> attributes) throws IOException;

    /**
     * Encode start tag, attributes and body (  for inline script or style )
     * @param context
     * @param component
     * @param attrs
     * @throws IOException
     */
    public void encodeBegin(FacesContext context, Object component, Map<String, Object> attrs) throws IOException;

    /**
     * Encode closed tag.
     * @param context
     * @param component
     * @throws IOException
     */
    public void encodeEnd(FacesContext context, Object component) throws IOException;

    /**
     * Send incapsulated resource to client by {@link ResourceContext} .
     * @param context
     */
    public void send(ResourceContext context) throws IOException;

    /**
     * Set response headers based on resource properties ( mime type etc )
     * TODO - send via {@link FacesContext } ???
     * @param response
     */
    public void sendHeaders(ResourceContext response);

    /**
     * Custom properties for resource ( image size etc. )
     * @param key
     * @return property value, or null if not set.
     */
    public Object getProperty(Object key);

    /**
     * Set new value for custom property.
     * @param key
     * @param value
     */
    void setProperty(Object key, Object value);

    /**
     * Set concrete renderer ( for brige pattern ). In renderer, incapsulate encoding functions,
     * content type, resource encoding.
     * @param renderer
     */
    public void setRenderer(ResourceRenderer renderer);

    /**
     * @return true, if resource must be rendered in faces request cicle.
     */
    public boolean requireFacesContext();

    /**
     * Flag for check session-dependencies for resource. In application-wide case,
     * resource URL not prepared by context.getExternalContext().encodeResourceURL(resourceURL)
     * and used application-wide cache for store.
     * @return
     */
    public boolean isSessionAware();

    void setKey(String key);

    String getKey();

    public abstract ResourceRenderer getRenderer(ResourceContext resourceContext);

    public abstract void setSessionAware(boolean sessionAware);

    public abstract void setCacheable(boolean cacheable);

    public abstract void setLastModified(Date lastModified);

    public abstract void setExpired(long expired);
}
