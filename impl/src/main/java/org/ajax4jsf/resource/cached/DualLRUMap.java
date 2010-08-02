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

package org.ajax4jsf.resource.cached;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

class DualLRUMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = -313747679711995782L;
    private int capacity;
    private Map<V, K> reverseMap;

    public DualLRUMap(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
        this.reverseMap = new HashMap<V, K>(capacity, 0.75f);
    }

    @Override
    public V put(K key, V value) {
        V v = super.put(key, value);

        reverseMap.put(value, key);

        return v;
    }

    @Override
    public V remove(Object key) {
        V value = super.remove(key);

        if (value != null) {
            reverseMap.remove(value);
        }

        return value;
    }

    public K getKey(Object value) {
        K key = reverseMap.get(value);

        if (key != null) {

            // update LRU
            get(key);
        }

        return key;
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        boolean remove = size() > capacity;

        if (remove) {
            reverseMap.remove(eldest.getValue());
        }

        return remove;
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    }

    @Override
    public Set<K> keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    @Override
    public Collection<V> values() {
        return Collections.unmodifiableCollection(super.values());
    }
}
