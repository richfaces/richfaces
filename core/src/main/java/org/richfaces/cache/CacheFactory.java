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
