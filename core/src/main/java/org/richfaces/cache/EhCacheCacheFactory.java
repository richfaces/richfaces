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

/**
 *
 */
package org.richfaces.cache;

import static org.richfaces.application.configuration.ConfigurationServiceHelper.getIntConfigurationValue;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.richfaces.application.CoreConfiguration;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class EhCacheCacheFactory implements CacheFactory {
    private static final Logger LOG = RichfacesLogger.CACHE.getLogger();
    private CacheManager cacheManager;

    public EhCacheCacheFactory() {
        super();

        URL configUrl = null;

        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        if (ccl != null) {
            configUrl = ccl.getResource("ehcache.xml");
        }

        if (configUrl != null) {
            LOG.info(MessageFormat.format("Using cache configuration: {0}", configUrl.toExternalForm()));
        } else {
            configUrl = EhCacheCacheFactory.class.getResource("ehcache-failsafe-richfaces.xml");
            LOG.info(MessageFormat.format("Using default cache configuration: {0}", configUrl.toExternalForm()));
        }

        cacheManager = CacheManager.create(configUrl);
    }

    public Cache createCache(FacesContext facesContext, String cacheName, Map<?, ?> env) {
        LOG.info("Creating EhCache cache instance");

        int maxCacheSize = getIntConfigurationValue(facesContext, CoreConfiguration.Items.resourcesCacheSize);
        boolean preconfiguredCache = false;

        Ehcache ehcache = cacheManager.getEhcache(cacheName);
        if (ehcache == null) {
            ehcache = new net.sf.ehcache.Cache(cacheName, maxCacheSize, false, true, 0, 0);
        } else {
            preconfiguredCache = true;

            if (ehcache.getCacheConfiguration().getMaxElementsInMemory() <= 0) {
                LOG.info(MessageFormat.format("Maximum cache size hasn''t been set, resetting to {0} max items", maxCacheSize));

                ehcache.getCacheConfiguration().setMaxElementsInMemory(maxCacheSize);
            }
        }

        return new EhCacheCache(ehcache, preconfiguredCache);
    }

    public void destroy() {
        cacheManager.shutdown();
        cacheManager = null;
    }
}
