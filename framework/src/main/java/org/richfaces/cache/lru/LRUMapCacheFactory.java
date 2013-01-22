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
package org.richfaces.cache.lru;

import static org.richfaces.application.configuration.ConfigurationServiceHelper.getIntConfigurationValue;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.application.CoreConfiguration;
import org.richfaces.cache.Cache;
import org.richfaces.cache.CacheFactory;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick - mailto:nbelaevski@exadel.com created 01.05.2007
 */
public class LRUMapCacheFactory implements CacheFactory {
    private static final Logger LOG = RichfacesLogger.CACHE.getLogger();

    public Cache createCache(FacesContext facesContext, String cacheName, Map<?, ?> env) {
        // TODO - handle cache name
        LOG.info("Creating LRUMap cache instance using parameters: " + env);

        Integer cacheSize = getIntConfigurationValue(facesContext, CoreConfiguration.Items.lruMapCacheSize);

        if (cacheSize == null) {
            cacheSize = getIntConfigurationValue(facesContext, CoreConfiguration.Items.resourcesCacheSize);
        }

        LOG.info("Creating LRUMap cache instance of " + cacheSize + " items capacity");

        return new LRUMapCache(cacheSize);
    }

    public void destroy() {
    }
}
