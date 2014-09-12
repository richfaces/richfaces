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

import java.util.Date;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class EhCacheCache implements Cache {
    private net.sf.ehcache.Ehcache cache;
    private boolean preconfiguredCache;

    public EhCacheCache(Ehcache cache, boolean preconfiguredCache) {
        super();
        this.cache = cache;
        this.preconfiguredCache = preconfiguredCache;
    }

    public void start() {
        if (!preconfiguredCache) {
            cache.initialise();
            cache.bootstrap();
        }
    }

    public void stop() {
        if (!preconfiguredCache) {
            cache.dispose();
        }
    }

    public Object get(Object key) {
        Element element = cache.get(key);

        if (element != null) {
            return element.getObjectValue();
        }

        return null;
    }

    public void put(Object key, Object value, Date expired) {
        int ttl = 0;

        if (expired != null) {
            ttl = (int) (expired.getTime() - System.currentTimeMillis()) / 1000;
        }

        Element element = new Element(key, value, 0, ttl);

        cache.putQuiet(element);
    }
}
