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
package org.richfaces.ehcache;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.richfaces.cache.Cache;
import org.richfaces.cache.CacheManager;

@ManagedBean
@ViewScoped
public class EhCacheBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private FacesContext facesContext;
    private Cache cache;
    private String cacheValue;

    @PostConstruct
    public void init() {
        // create cache instance
        facesContext = FacesContext.getCurrentInstance();
        CacheManager cacheManager = new CacheManager();
        Map<?, ?> initParameterMap = facesContext.getExternalContext().getInitParameterMap();
        cache = cacheManager.createCache(facesContext, "test-cache", initParameterMap);
        // insert value into cache and put it into cacheValue for later retrieval
        cache.put("x", "value-x", null);
        cacheValue = cache.get("x").toString();
    }

    public String getCacheValue() {
        return cacheValue;
    }
}
