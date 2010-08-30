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
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.ajax4jsf.Messages;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.util.Util;

/**
 * Base class for all Html page resources - images, scripts, styles etc. Realise
 * as "brige" pattern - different subclasses for different resource source
 * (static,jar,soft generator ) and customaized with differernt renderers for
 * image, script, style ( linked ir inline ). must operate with different
 * sources - application context, classpath, software generator.
 *
 * @author shura (latest modification by $Author: nick_belaevski $)
 * @version $Revision: 1.1.2.2 $ $Date: 2007/01/11 16:52:15 $
 */
public abstract class InternetResourceBase implements InternetResource {
    static final int BUFFER_SIZE = 1024;
    private static final Logger LOG = RichfacesLogger.RESOURCE.getLogger();

    // Hours
    private int contentLength = -1;
    private Date lastModified = new Date(System.currentTimeMillis());
    private long expired = Long.MIN_VALUE;
    private boolean cacheable = true;
    private ResourceRenderer renderer = null;
    private boolean sessionAware = true;
    private Map<Object, Object> customProperties = new HashMap<Object, Object>();
    private String key;

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.chameleon.resource.InternetResource#getContentLength()
     */
    public int getContentLength(ResourceContext resourceContext) {
        return contentLength;
    }

    /**
     * @param contentLength The contentLength to set.
     */
    protected void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.chameleon.resource.InternetResource#getExpired()
     */
    public long getExpired(ResourceContext resourceContext) {
        if (expired == Long.MIN_VALUE) {
            expired = -1;

            String defaultExpireParameter = resourceContext.getInitParameter(InternetResource.DEFAULT_EXPITE_PARAMETER);

            if (null != defaultExpireParameter) {
                expired = Long.parseLong(defaultExpireParameter) * 1000L;
            }
        }

        return expired;
    }

    /**
     * @param expired The expired to set.
     */
    public void setExpired(long expired) {
        this.expired = expired;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.chameleon.resource.InternetResource#getLastModified()
     */
    public Date getLastModified(ResourceContext resourceContext) {
        if (null != lastModified) {
            return lastModified;
        } else {
            return new Date(System.currentTimeMillis());
        }
    }

    /**
     * @param lastModified The lastModified to set.
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.chameleon.resource.InternetResource#isCacheable()
     */
    public boolean isCacheable(ResourceContext resourceContext) {
        return cacheable;
    }

    /**
     * @param cacheable The cacheable to set.
     */
    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    /**
     * @return Returns the sessionAware.
     */
    public boolean isSessionAware() {
        return requireFacesContext();
    }

    /**
     * @param sessionAware The sessionAware to set.
     */
    public void setSessionAware(boolean sessionAware) {
        this.sessionAware = sessionAware;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.chameleon.resource.InternetResource#getContentType()
     */
    public String getContentType(ResourceContext resourceContext) {
        return getRenderer(resourceContext).getContentType();
    }

    /**
     * @return Returns the renderer.
     */
    public ResourceRenderer getRenderer(ResourceContext resourceContext) {
        return renderer;
    }

    /**
     * @param renderer The renderer to set.
     */
    public void setRenderer(ResourceRenderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Key for wich resource registered in resource builder.
     *
     * @return Returns the key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Set registration key ( by {@link InternetResourceBuilder} for this
     * resource.
     *
     * @param key The key to set.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.InternetResource#getUri(javax.faces.context.FacesContext,
     *      java.lang.Object)
     */
    public String getUri(final FacesContext context, Object data) {

        /*
         * return InternetResourceBuilder.getInstance().getUri(this, context,
         *       getDataToStore(context, data));
         */
        return null;
    }

    /**
     * Template method to construct resource-specific data ( to store in
     * resource URI ). If resource must store any data in session or other
     * cache, it must be done here. Since lightweight realisation, information
     * for restore such data muct be returned by this method.
     *
     * @param context -
     *                current faces context.
     * @param data    -
     *                data for build information, usually current
     *                {@link javax.faces.component.UIComponent}
     * @return - any {@link java.io.Serializable} object, or null.
     */
    protected Object getDataToStore(FacesContext context, Object data) {
        return getRenderer(new FacesResourceContext(context)).getData(this, context, data);
    }

    /**
     * Restore data object from resource URI request parameter ( same object as
     * returned in getDataToStore method )
     *
     * @param context
     * @return restored data, or null.
     */
    protected Object restoreData(ResourceContext context) {
        Object data = context.getResourceData();

        if (data instanceof byte[]) {
            byte[] objectArray = (byte[]) data;

            data = deserializeData(objectArray);
        }

        return data;
    }

    /**
     * Deserialize parameters object from byte array. By default, used Java
     * de-serialisation from ObjectOutputStream , but implementations can
     * override this method ( togewer with getDataToStore ) for implement short
     * version.
     *
     * @param objectArray
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected Object deserializeData(byte[] objectArray) {
        return objectArray;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.chameleon.resource.InternetResource#getResourceAsStream()
     */
    public InputStream getResourceAsStream(ResourceContext context) {
        throw new UnsupportedOperationException(Messages.getMessage(Messages.METHOD_NOT_IMPLEMENTED,
            "getResourceAsStream"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.InternetResource#getProperty(java.lang.Object)
     */
    public Object getProperty(Object key) {

        // TODO Auto-generated method stub
        return customProperties.get(key);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.InternetResource#setProperty(java.lang.Object,
     *      java.lang.Object)
     */
    public void setProperty(Object key, Object value) {
        customProperties.put(key, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.InternetResource#encode(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encode(FacesContext context, Object data) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.ENCODE_HTML_INFO, getKey()));
        }

        getRenderer(new FacesResourceContext(context)).encode(this, context, data);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.InternetResource#encode(javax.faces.context.FacesContext,
     *      java.lang.Object, java.util.Map)
     */
    public void encode(FacesContext context, Object data, Map<String, Object> attributes) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.ENCODE_HTML_INFO_2, getKey(), attributes));
        }

        getRenderer(new FacesResourceContext(context)).encodeBegin(this, context, data, attributes);
        getRenderer(new FacesResourceContext(context)).encodeEnd(this, context, data);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.InternetResource#encodeBegin(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent, java.util.Map)
     */
    public void encodeBegin(FacesContext context, Object component, Map<String, Object> attrs) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.ENCODE_BEGIN_HTML_INFO, getKey(), attrs));
        }

        getRenderer(new FacesResourceContext(context)).encodeBegin(this, context, component, attrs);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.InternetResource#encodeEnd(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent, java.util.Map)
     */
    public void encodeEnd(FacesContext context, Object component) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.ENCODE_END_HTML_INFO, getKey()));
        }

        getRenderer(new FacesResourceContext(context)).encodeEnd(this, context, component);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.InternetResource#send(javax.faces.context.FacesContext,
     *      java.lang.Object)
     */
    public void send(ResourceContext context) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.SEND_CONTENT_INFO, getKey()));
        }

        int total = getRenderer(context).send(this, context);

        // For cacheable resources, store size.
        if (isCacheable(context)) {
            if (context instanceof CachedResourceContext) {
                CachedResourceContext cachedContext = (CachedResourceContext) context;

                cachedContext.getContent().setContentLength(total);
            } else {
                setContentLength(total);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.chameleon.resource.InternetResource#sendHeaders(javax.servlet.http.HttpServletResponse)
     */
    public void sendHeaders(ResourceContext context) {
        boolean cached = context.isCacheEnabled() && isCacheable(context);

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.SET_RESPONSE_HEADERS_INFO, getKey()));
        }

        // context.setHeader("Content-Type",getContentType());
        context.setContentType(getContentType(context));

        Date lastModified = getLastModified(context);

        if (lastModified != null) {
            context.setHeader("Last-Modified", Util.formatHttpDate(lastModified.getTime()));

            // context.setDateHeader("Last-Modified", lastModified.getTime());
        }

        int contentLength = getContentLength(context);

        if (cached) {
            if (contentLength > 0) {
                context.setContentLength(contentLength);
            }

            long expired = getExpired(context);

            if (expired < 0) {
                expired = DEFAULT_EXPIRE;
            }

            context.setHeader("Expires", Util.formatHttpDate(System.currentTimeMillis() + expired));

//          context.setDateHeader("Expires", System.currentTimeMillis()
//                  + expired);
            context.setHeader("Cache-Control", "max-age=" + (expired / 1000L));
        } else {
            if (contentLength > 0) {
                context.setContentLength(contentLength);

//              } else {
//                  context.setHeader("Transfer-Encoding", "chunked");
            }

            context.setHeader("Cache-Control", "max-age=0, no-store, no-cache");
            context.setHeader("Pragma", "no-cache");
            context.setIntHeader("Expires", 0);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.InternetResource#requireFacesContext()
     */
    public boolean requireFacesContext() {

        // by default, send data in ordinary Http request.
        return getRenderer(null).requireFacesContext();
    }
}
