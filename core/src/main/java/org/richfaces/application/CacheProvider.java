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
package org.richfaces.application;

import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.cache.Cache;
import org.richfaces.cache.CacheManager;
import org.richfaces.resource.ResourceHandlerImpl;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class CacheProvider implements Initializable, Cache {
    private Cache instance;
    private CacheManager cacheManager;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param key
     * @return
     * @see org.richfaces.cache.Cache#get(java.lang.Object)
     */
    public Object get(Object key) {
        return this.instance.get(key);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param key
     * @param value
     * @param expired
     * @see org.richfaces.cache.Cache#put(java.lang.Object, java.lang.Object, java.util.Date)
     */
    public void put(Object key, Object value, Date expired) {
        this.instance.put(key, value, expired);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @see org.richfaces.cache.Cache#start()
     */
    public void start() {
        this.instance.start();
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @see org.richfaces.cache.Cache#stop()
     */
    public void stop() {
        this.instance.stop();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.jsr330.Initializable#destroy()
     */
    public void release() {
        cacheManager.destroy();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.jsr330.Initializable#init(org.richfaces.jsr330.Binders)
     */
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        cacheManager = new CacheManager();
        Map<?, ?> envMap = facesContext.getExternalContext().getInitParameterMap();
        instance = cacheManager.createCache(facesContext, ResourceHandlerImpl.RESOURCE_CACHE_NAME, envMap);
    }

    public Cache get() {
        return instance;
    }
}
