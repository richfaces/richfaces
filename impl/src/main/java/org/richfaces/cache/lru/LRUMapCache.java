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

import java.util.Date;

import org.richfaces.cache.Cache;

/**
 * @author Nick - mailto:nbelaevski@exadel.com
 * @since 3.1
 */
public class LRUMapCache implements Cache {
    private CacheMap map;

    public LRUMapCache() {
        this.map = new CacheMap();
    }

    public LRUMapCache(int capacity) {
        this.map = new CacheMap(capacity);
    }

    public synchronized Object get(Object key) {
        this.map.purge();

        CacheEntry entry = this.map.get(key);

        if (entry != null) {
            return entry.getValue();
        }

        return null;
    }

    public synchronized void put(Object key, Object value, Date expired) {
        this.map.purge();

        CacheEntry cacheEntry = new CacheEntry(key, value, expired);

        this.map.put(key, cacheEntry);
    }

    public void start() {
    }

    public void stop() {
        this.map.clear();
    }
}
