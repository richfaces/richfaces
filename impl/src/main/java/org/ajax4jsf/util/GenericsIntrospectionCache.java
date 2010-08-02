/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

package org.ajax4jsf.util;

import org.richfaces.util.ReferenceMap;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.beans.BeanInfo;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Map;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
final class GenericsIntrospectionCache {
    private static final String CACHE_SIZE_PARAMETER = "org.richfaces.GenericsIntrospectionCacheSize";
    private static final int DEFAULT_CACHE_SIZE = 256;
    private static final String INSTANCE_ATTRIBUTE_NAME = GenericsIntrospectionCache.class.getName();
    private Map<Class<?>, GenericsCacheEntry> genericsCache;

    private GenericsIntrospectionCache(int cacheSize) {
        genericsCache = new ReferenceMap<Class<?>,
            GenericsCacheEntry>(Collections.synchronizedMap(new LRUMap<Class<?>,
            Reference<GenericsCacheEntry>>(cacheSize)));
    }

    private static int getSize(ExternalContext externalContext) {
        int cacheSize = DEFAULT_CACHE_SIZE;
        String cacheSizeParameter = externalContext.getInitParameter(CACHE_SIZE_PARAMETER);

        if (cacheSizeParameter != null && cacheSizeParameter.length() != 0) {
            try {
                cacheSize = Integer.valueOf(cacheSizeParameter);
            } catch (NumberFormatException e) {
                externalContext.log("Error converting " + CACHE_SIZE_PARAMETER + " init parameter to int: "
                    + e.getMessage(), e);
            }
        }

        return cacheSize;
    }

    public GenericsCacheEntry getGenericCacheEntry(Class<?> beanClass) {
        GenericsCacheEntry cacheEntry = genericsCache.get(beanClass);

        if (cacheEntry == null) {
            cacheEntry = new GenericsCacheEntry();
            genericsCache.put(beanClass, cacheEntry);
        }

        return cacheEntry;
    }

    static GenericsIntrospectionCache getInstance(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        Map<String, Object> applicationMap = externalContext.getApplicationMap();
        GenericsIntrospectionCache instance;

        synchronized (applicationMap) {
            instance = (GenericsIntrospectionCache) applicationMap.get(INSTANCE_ATTRIBUTE_NAME);

            if (instance == null) {
                instance = new GenericsIntrospectionCache(getSize(externalContext));
                applicationMap.put(INSTANCE_ATTRIBUTE_NAME, instance);
            }
        }

        return instance;
    }

    static final class GenericsCacheEntry {
        SoftReference<BeanInfo> beanInfoReference;
        Map<String, Class<?>> genericPropertiesClasses;
    }
}
