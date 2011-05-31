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
package org.richfaces.cache;

import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * CacheFactory is a service provider specific interface. Service provider should implement CacheFactory to provide the
 * functionality to create a new implementation specific Cache object.
 */
public interface CacheFactory {
    /**
     * creates a new implementation specific Cache object using the env parameters.
     *
     * @param env implementation specific environment parameters passed to the CacheFactory.
     * @param cacheLoader implementation of the {@link CacheLoader} to use
     * @param cacheConfigurationloader TODO
     * @return an implementation specific Cache object.
     * @throws CacheException if any error occurs.
     */
    Cache createCache(FacesContext facesContext, String cacheName, Map<?, ?> env);

    void destroy();
}
