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

import java.util.PriorityQueue;

import org.richfaces.util.LRUMap;

/**
 * User: akolonitsky Date: Oct 13, 2009
 */
public final class CacheMap extends LRUMap<Object, CacheEntry> {
    private static final long serialVersionUID = -5422668357346537621L;
    private PriorityQueue<CacheEntry> expirationQueue = new PriorityQueue<CacheEntry>();

    public CacheMap() {
        super();
    }

    public CacheMap(int capacity) {
        super(capacity);
    }

    public PriorityQueue<CacheEntry> getExpirationQueue() {
        return expirationQueue;
    }

    @Override
    public CacheEntry put(Object key, CacheEntry value) {
        CacheEntry entry = super.put(key, value);

        if (entry != null) {

            // prolong
            expirationQueue.remove(entry);
        }

        if (value.getExpired() != null) {
            expirationQueue.add(value);
        }

        return entry;
    }

    @Override
    public CacheEntry remove(Object key) {
        CacheEntry entry = super.remove(key);

        if (entry != null) {
            expirationQueue.remove(entry);
        }

        return entry;
    }

    @Override
    public void clear() {
        super.clear();
        expirationQueue.clear();
    }

    public void purge() {
        CacheEntry queueEntry = expirationQueue.peek();

        while ((queueEntry != null) && queueEntry.isExpired()) {
            super.remove(queueEntry.getKey());
            expirationQueue.remove();
            queueEntry = expirationQueue.peek();
        }
    }
}
