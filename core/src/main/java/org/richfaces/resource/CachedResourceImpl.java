/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.resource;

import static org.richfaces.resource.ResourceUtils.secondToMillis;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import org.ajax4jsf.io.ByteBuffer;
import org.ajax4jsf.io.FastBufferInputStream;
import org.ajax4jsf.io.FastBufferOutputStream;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class CachedResourceImpl extends AbstractCacheableResource {
    // [0..1]
    private static final float CACHE_EXPIRATION_COEFFICIENT = 0.9f;
    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();
    private static final int MAX_AGE_VALUE_GROUP = 2;
    private static final int SMAX_AGE_GROUP = 1;
    private static final Pattern MAX_AGE = Pattern.compile("^(?:(s-maxage)|max-age)=(\\d+)$", Pattern.CASE_INSENSITIVE);
    private ByteBuffer content;
    private String entityTag;
    /**
     * serves only to define server cache entry expiration time only browser cache expiration is controlled by stored HTTP
     * headers value
     */
    private Date expired;
    private Map<String, String> headers;
    private Date lastModified;

    private void initializeFromHeaders() {
        this.entityTag = null;
        this.lastModified = null;
        this.expired = null;

        Date expiredFromHeader = null;

        // TODO what if maxAge = 0 in header?
        int maxAge = 0;

        for (Entry<String, String> headerEntry : headers.entrySet()) {
            String headerKey = headerEntry.getKey().toLowerCase(Locale.US);

            if ("etag".equals(headerKey)) {
                this.entityTag = headerEntry.getValue();
            }

            if ("last-modified".equals(headerKey)) {
                this.lastModified = ResourceUtils.parseHttpDate(headerEntry.getValue());
            }

            if ("expires".equals(headerKey)) {
                expiredFromHeader = ResourceUtils.parseHttpDate(headerEntry.getValue());
            }

            if ("cache-control".equals(headerKey)) {
                String[] values = headerEntry.getValue().split(",");

                for (String value : values) {
                    Matcher matcher = MAX_AGE.matcher(value.trim());

                    if (matcher.find()) {
                        boolean isSMaxAge = matcher.group(SMAX_AGE_GROUP) != null;

                        if (maxAge <= 0 || isSMaxAge) { // s-maxage overrides max-age
                            try {
                                maxAge = (int) secondToMillis(Integer.parseInt(matcher.group(MAX_AGE_VALUE_GROUP)));
                            } catch (NumberFormatException e) {

                                // TODO: handle exception
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        long currentTime = getCurrentTime();

        if (maxAge > 0) {
            this.expired = new Date((long) (currentTime + CACHE_EXPIRATION_COEFFICIENT * maxAge));
        } else if (expiredFromHeader != null) {

            // ttl = expireTime - currentTime
            // CACHE_EXPIRATION_COEFFICIENT * ttl + currentTime
            this.expired = new Date(
                (long) (CACHE_EXPIRATION_COEFFICIENT * expiredFromHeader.getTime() + (1 - CACHE_EXPIRATION_COEFFICIENT)
                    * currentTime));
        } else {

            // TODO throw exception or modify headers?
        }
    }

    long getCurrentTime() {
        return System.currentTimeMillis();
    }

    private static ByteBuffer readContent(InputStream is) throws IOException {
        if (is == null) {
            throw new NullPointerException("Resource input stream is null");
        }

        FastBufferOutputStream os = new FastBufferOutputStream();

        try {
            ResourceUtils.copyStreamContent(is, os);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(e.getMessage(), e);
                }
            }

            try {
                os.close();
            } catch (IOException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(e.getMessage(), e);
                }
            }
        }

        ByteBuffer buffer = os.getFirstBuffer();

        buffer.compact();

        return buffer;
    }

    public void initialize(Resource resource) throws IOException {
        setResourceName(resource.getResourceName());
        setContentType(resource.getContentType());
        this.headers = resource.getResponseHeaders();
        initializeFromHeaders();
        this.content = readContent(resource.getInputStream());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FastBufferInputStream(content);
    }

    @Override
    public String getRequestPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        return new HashMap<String, String>(headers);
    }

    @Override
    public URL getURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCacheable(FacesContext context) {
        return true;
    }

    @Override
    public String getEntityTag(FacesContext context) {
        return entityTag;
    }

    @Override
    protected Date getLastModified(FacesContext context) {
        return lastModified;
    }

    public Date getExpired(FacesContext context) {
        return expired;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Cached resource: {0}", getResourceName());
    }
}
