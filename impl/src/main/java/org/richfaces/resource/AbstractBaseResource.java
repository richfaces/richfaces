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

package org.richfaces.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import org.richfaces.application.ServiceTracker;
import org.richfaces.application.Uptime;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.util.Util;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public abstract class AbstractBaseResource extends Resource {
    public static final String URL_PROTOCOL = "jsfresource";
    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();
    private Date lastModified = null;

    protected AbstractBaseResource() {
        super();
    }

    protected int getContentLength(FacesContext context) {
        return -1;
    }

    /**
     * TODO optimize/review?
     *
     * @return Returns the lastModified.
     */
    protected Date getLastModified(FacesContext context) {
        if (lastModified == null) {
            lastModified = getLastModifiedBySource();
        }

        // TODO - originally lastModified was set during resource creation.
        // as resources can be managed beans this approach does not seem good
        if (lastModified == null) {
            lastModified = ServiceTracker.getService(Uptime.class).getStartTime();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(
                    MessageFormat.format(
                        "Using resource handler start time as last modified date: {0,date,dd MMM yyyy HH:mm:ss zzz}",
                        lastModified));
            }
        }

        return lastModified;
    }

    private Date getLastModifiedBySource() {
        ClassLoader classLoader = getClassLoader();

        if (classLoader == null) {
            return null;
        }

        URL classResource = classLoader.getResource(getClass().getName().replace('.', '/') + ".class");

        if (classResource != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(MessageFormat.format("Located source for the resource class: {0}", classResource));
            }

            try {
                URLConnection connection = classResource.openConnection();

                connection.setUseCaches(false);

                long classLastModifiedDate = connection.getLastModified();

                if (classLastModifiedDate > 0) {
                    lastModified = new Date(classLastModifiedDate);

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(MessageFormat.format("Last source modification date is: {0,date}", lastModified));
                    }
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Source for the resource class has not been located");
            }
        }

        return null;
    }

    protected ClassLoader getClassLoader() {
        Class<? extends AbstractBaseResource> thisClass = getClass();
        ClassLoader classLoader = thisClass.getClassLoader();

        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        return classLoader;
    }

    private String getResourceVersion() {
        if (this instanceof VersionedResource) {
            return ((VersionedResource) this).getVersion();
        }
        
        return null;
    }
    
    @Override
    public String getRequestPath() {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceCodec resourceCodec = ServiceTracker.getService(context, ResourceCodec.class);
        String libraryName = getLibraryName();
        String resourceName = getResourceName();
        Object resourceData = Util.saveResourceState(context, this);
        String resourceVersion = getResourceVersion();
        String resourceUri = resourceCodec.encodeResourceRequestPath(context, libraryName, resourceName, 
            resourceData, resourceVersion);
        
        resourceUri = resourceCodec.encodeJSFMapping(context, resourceUri);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MessageFormat.format("Request path for {0} resource is: {1}", String.valueOf(resourceName),
                String.valueOf(resourceUri)));
        }

        return resourceUri;
    }

    boolean isResourceRequest() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        return facesContext.getApplication().getResourceHandler().isResourceRequest(facesContext);
    }

    long getCurrentTime() {
        return System.currentTimeMillis();
    }
    
    protected void addNoCacheResponseHeaders(FacesContext facesContext, Map<String, String> headers) {
        headers.put("Expires", "0");
        headers.put("Cache-Control", "max-age=0, no-store, no-cache");
        headers.put("Pragma", "no-cache");
    }
    
    protected void addCacheControlResponseHeaders(FacesContext facesContext, Map<String, String> headers) {
        addNoCacheResponseHeaders(facesContext, headers);
    }
    
    @Override
    public Map<String, String> getResponseHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (isResourceRequest()) {
            int contentLength = getContentLength(facesContext);

            if (contentLength >= 0) {
                headers.put("Content-Length", String.valueOf(contentLength));
            }

            String contentType = getContentType();

            if (contentType != null) {
                headers.put("Content-Type", contentType);
            }

            Date lastModified = getLastModified(facesContext);

            if (lastModified != null) {
                headers.put("Last-Modified", Util.formatHttpDate(lastModified));
            }

            headers.put("Date", Util.formatHttpDate(getCurrentTime()));

            addCacheControlResponseHeaders(facesContext, headers);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Created set of response headers");

                // TODO - security - can we log header values?
                for (Entry<String, String> entry : headers.entrySet()) {
                    LOGGER.debug(MessageFormat.format("\t{0}={1}", entry.getKey(), entry.getValue()));
                }
            }
        }

        return headers;
    }

    @Override
    public URL getURL() {
        try {
            return new URL(URL_PROTOCOL, null, -1, getResourceName(), new MyURLStreamHandler());
        } catch (MalformedURLException e) {
            throw new FacesException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        return true;
    }
    
    private class MyURLConnection extends URLConnection {
        MyURLConnection(URL u) {
            super(u);
        }

        @Override
        public void connect() throws IOException {
        }

        @Override
        public int getContentLength() {
            FacesContext facesContext = FacesContext.getCurrentInstance();

            return AbstractBaseResource.this.getContentLength(facesContext);
        }

        @Override
        public String getContentType() {
            return AbstractBaseResource.this.getContentType();
        }

        @Override
        public long getExpiration() {
            return 0;
        }

        @Override
        public long getLastModified() {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Date date = AbstractBaseResource.this.getLastModified(facesContext);

            if (date != null) {
                return date.getTime();
            }

            return 0;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return AbstractBaseResource.this.getInputStream();
        }
    }

    private class MyURLStreamHandler extends URLStreamHandler {
        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            return new MyURLConnection(u);
        }
    }

}
