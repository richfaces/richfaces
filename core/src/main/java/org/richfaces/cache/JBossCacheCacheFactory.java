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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.ajax4jsf.resource.util.URLToStreamHelper;
import org.jboss.cache.Cache;
import org.jboss.cache.DefaultCacheFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.EvictionAlgorithmConfig;
import org.jboss.cache.config.EvictionConfig;
import org.jboss.cache.config.EvictionRegionConfig;
import org.jboss.cache.eviction.EvictionAlgorithmConfigBase;
import org.jboss.cache.eviction.ExpirationAlgorithmConfig;
import org.richfaces.application.CoreConfiguration;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class JBossCacheCacheFactory implements CacheFactory {
    private static final Logger LOG = RichfacesLogger.CACHE.getLogger();
    private org.jboss.cache.CacheFactory<String, Object> cacheFactory;

    public JBossCacheCacheFactory() {
        super();
        cacheFactory = new DefaultCacheFactory<String, Object>();
    }

    private void setupMaxSizeEviction(FacesContext facesContext, Cache<String, Object> cache) {
        EvictionConfig evictionConfig = cache.getConfiguration().getEvictionConfig();
        EvictionAlgorithmConfig evictionAlgorithmConfig = evictionConfig.getDefaultEvictionRegionConfig()
            .getEvictionAlgorithmConfig();

        if (evictionAlgorithmConfig instanceof EvictionAlgorithmConfigBase) {
            EvictionAlgorithmConfigBase baseEvicitonConfig = (EvictionAlgorithmConfigBase) evictionAlgorithmConfig;
            if (baseEvicitonConfig.getMaxNodes() <= 0) {
                int maxCacheSize = getIntConfigurationValue(facesContext, CoreConfiguration.Items.resourcesCacheSize);
                LOG.info(MessageFormat.format("Maximum cache size hasn''t been set, resetting to {0} max items", maxCacheSize));
                baseEvicitonConfig.setMaxNodes(maxCacheSize);
            }
        }
    }

    public org.richfaces.cache.Cache createCache(FacesContext facesContext, String cacheName, Map<?, ?> env) {
        // TODO - handle cache name
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Cache<String, Object> cache = null;
        URL cacheConfigurationURL = null;

        if (contextClassLoader != null) {
            cacheConfigurationURL = contextClassLoader.getResource("jboss-cache.xml");
        }

        if (cacheConfigurationURL != null) {
            InputStream stream = null;

            try {
                stream = URLToStreamHelper.urlToStream(cacheConfigurationURL);
                cache = cacheFactory.createCache(stream);
                setupMaxSizeEviction(facesContext, cache);
            } catch (IOException e) {
                throw new FacesException(e.getLocalizedMessage(), e);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        LOG.debug(e.getMessage(), e);
                    }
                }
            }
        } else {
            Configuration configuration = new Configuration();
            EvictionRegionConfig evictionRegionConfig = new EvictionRegionConfig(Fqn.root());
            ExpirationAlgorithmConfig expirationAlgorithm = new ExpirationAlgorithmConfig();
            int maxCacheSize = getIntConfigurationValue(facesContext, CoreConfiguration.Items.resourcesCacheSize);
            expirationAlgorithm.setMaxNodes(maxCacheSize);

            evictionRegionConfig.setEvictionAlgorithmConfig(expirationAlgorithm);

            EvictionConfig evictionConfig = new EvictionConfig(evictionRegionConfig);

            evictionConfig.setWakeupInterval(1000);
            configuration.setEvictionConfig(evictionConfig);
            cache = cacheFactory.createCache(configuration);
        }

        return new JBossCacheCache(cache);
    }

    public void destroy() {
        cacheFactory = null;
    }
}
