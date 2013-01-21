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

import static org.richfaces.application.configuration.ConfigurationServiceHelper.getLongConfigurationValue;
import static org.richfaces.resource.ResourceUtils.millisToSecond;
import static org.richfaces.resource.ResourceUtils.secondToMillis;

import java.util.Date;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.richfaces.application.CoreConfiguration;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.util.Util;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public abstract class AbstractCacheableResource extends AbstractBaseResource implements CacheableResource {
    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();
    private boolean cacheable = true;

    public boolean isCacheable(FacesContext context) {
        return cacheable;
    }

    // TODO add getExpired(FacesContext) for HTTP matching headers?
    private static boolean isUserCopyActual(Date lastModified, Date modifiedCondition) {

        // 1000 ms due to round modification time to seconds.
        return (lastModified.getTime() - modifiedCondition.getTime()) <= 1000;
    }

    @Deprecated
    protected Boolean isMatchesLastModified(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        Map<String, String> requestHeaderMap = externalContext.getRequestHeaderMap();
        String modifiedCondition = requestHeaderMap.get("If-Modified-Since");

        if (modifiedCondition == null) {
            return null;
        }

        return isMatchesLastModified(context, modifiedCondition);
    }

    protected boolean isMatchesLastModified(FacesContext context, String modifiedCondition) {
        Date lastModified = getLastModified(context);

        if (lastModified == null) {
            return false;
        }

        return isUserCopyActual(lastModified, Util.parseHttpDate(modifiedCondition));
    }

    @Deprecated
    protected Boolean isMatchesEntityTag(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        Map<String, String> requestHeaderMap = externalContext.getRequestHeaderMap();
        String matchHeaderValue = requestHeaderMap.get("If-None-Match");

        if (matchHeaderValue == null) {
            return null;
        }

        return isMatchesEntityTag(context, matchHeaderValue);
    }

    protected boolean isMatchesEntityTag(FacesContext context, String matchHeaderValue) {
        String resourceEntityTag = getEntityTag(context);

        if (resourceEntityTag == null) {
            return false;
        }

        return ResourceUtils.matchTag(resourceEntityTag, matchHeaderValue);
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        if (!isCacheable(context)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("User agent cache check: resource is not cacheable");
            }

            return true;
        }

        ExternalContext externalContext = context.getExternalContext();
        Map<String, String> requestHeaderMap = externalContext.getRequestHeaderMap();
        String modifiedCondition = requestHeaderMap.get("If-Modified-Since");
        String matchHeaderValue = requestHeaderMap.get("If-None-Match");

        if ((modifiedCondition == null) && (matchHeaderValue == null)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("User agent cache check: no cache information was provided in request");
            }

            return true;
        }

        if ((matchHeaderValue != null) && !isMatchesEntityTag(context, matchHeaderValue)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("User agent cache check: entity tags don't match");
            }

            return true;
        }

        if ((modifiedCondition != null) && !isMatchesLastModified(context, modifiedCondition)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("User agent cache check: resource was modified since the last request");
            }

            return true;
        }

        return false;
    }

    /**
     * <b>IMPORTANT:</b> this method returned TTL in RF 3.x, now it returns expiration time
     *
     * @return Returns the expired.
     */
    public Date getExpires(FacesContext context) {
        return null;
    }

    public int getTimeToLive(FacesContext context) {
        return 0;
    }

    public String getEntityTag(FacesContext context) {
        int contentLength = getContentLength(context);
        Date lastModified = getLastModified(context);

        if ((contentLength < 0) || (lastModified == null)) {
            return null;
        }

        return ResourceUtils.formatWeakTag(contentLength + "-" + lastModified.getTime());
    }

    @Override
    protected void addCacheControlResponseHeaders(FacesContext facesContext, Map<String, String> headers) {
        if (isCacheable(facesContext)) {
            long currentTime = getCurrentTime();
            String formattedExpireDate;
            long maxAge = getTimeToLive(facesContext);

            if (maxAge > 0) {
                formattedExpireDate = Util.formatHttpDate(currentTime + secondToMillis(maxAge));
            } else {
                Date expired = getExpires(facesContext);

                if (expired != null) {
                    formattedExpireDate = Util.formatHttpDate(expired);
                    maxAge = millisToSecond(expired.getTime() - currentTime);
                } else {
                    maxAge = getLongConfigurationValue(facesContext, CoreConfiguration.Items.resourcesTTL);
                    formattedExpireDate = Util.formatHttpDate(currentTime + secondToMillis(maxAge));
                }
            }

            if (formattedExpireDate != null) {
                headers.put("Expires", formattedExpireDate);
            }

            if (maxAge > 0) {
                headers.put("Cache-Control", "max-age=" + maxAge);
            }

            String entityTag = getEntityTag(facesContext);

            if (entityTag != null) {
                headers.put("ETag", entityTag);
            }
        } else {
            addNoCacheResponseHeaders(facesContext, headers);
        }
    }
}
