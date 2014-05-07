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
package org.richfaces.cache;

import static org.richfaces.application.configuration.ConfigurationServiceHelper.getIntConfigurationValue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.ajax4jsf.resource.util.URLToStreamHelper;
import org.richfaces.application.CoreConfiguration;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.opensymphony.oscache.base.AbstractCacheAdministrator;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * @author Nick - mailto:nbelaevski@exadel.com created 01.05.2007
 */
public class OSCacheCacheFactory implements CacheFactory {
    private static final Logger LOG = RichfacesLogger.CACHE.getLogger();
    private List<GeneralCacheAdministrator> cacheAdministrators = new ArrayList<GeneralCacheAdministrator>(1);

    public OSCacheCacheFactory() throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = OSCacheCacheFactory.class.getClassLoader();
        }

        // try load cache class to check its presence in classpath
        Class.forName(GeneralCacheAdministrator.class.getName(), false, classLoader);
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        URL resource = OSCacheCache.class.getResource("oscache.properties");

        if (resource != null) {
            InputStream stream = URLToStreamHelper.urlToStream(resource);

            try {
                properties.load(stream);
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    LOG.debug(e.getMessage(), e);
                }
            }
        }

        return properties;
    }

    public Cache createCache(FacesContext facesContext, String cacheName, Map<?, ?> env) {
        // TODO - handle cache name
        Properties cacheProperties = new Properties();

        try {
            cacheProperties.putAll(loadProperties());
        } catch (IOException e) {
            throw new FacesException(e.getMessage(), e);
        }

        try {
            cacheProperties.putAll(loadProperties());
        } catch (IOException e) {
            throw new FacesException(e.getMessage(), e);
        }

        cacheProperties.putAll(env);
        LOG.info("Creating OSCache cache instance using parameters: " + cacheProperties);

        String property = cacheProperties.getProperty(AbstractCacheAdministrator.CACHE_CAPACITY_KEY);
        if (property == null) {
            int maxCacheSize = getIntConfigurationValue(facesContext, CoreConfiguration.Items.resourcesCacheSize);
            LOG.info(MessageFormat.format("Maximum cache size hasn''t been set, resetting to {0} max items", maxCacheSize));
            cacheProperties.put(AbstractCacheAdministrator.CACHE_CAPACITY_KEY, Integer.toString(maxCacheSize));
        }

        GeneralCacheAdministrator cacheAdministrator = new GeneralCacheAdministrator(cacheProperties);
        cacheAdministrators.add(cacheAdministrator);
        return new OSCacheCache(cacheAdministrator.getCache());
    }

    public void destroy() {
        for (GeneralCacheAdministrator cacheAdministrator : cacheAdministrators) {
            cacheAdministrator.destroy();
        }

        cacheAdministrators = null;
    }
}
