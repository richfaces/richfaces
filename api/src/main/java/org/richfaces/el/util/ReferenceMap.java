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

package org.richfaces.el.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created 17.03.2008
 *
 * @author Nick Belaevski
 * @since 3.2
 */
public class ReferenceMap<K, V> implements Map<K, V> {
    private ReferenceQueue<V> queue = new ReferenceQueue<V>();
    private Map<K, Reference<V>> map;

    public ReferenceMap() {
        this(Collections.synchronizedMap(new HashMap<K, Reference<V>>()));
    }

    public ReferenceMap(Map<K, Reference<V>> map) {
        super();
        this.map = map;
    }

    private void purge() {
        Reference<? extends V> reference = null;

        while ((reference = queue.poll()) != null) {
            ReferenceMapSoftReference<?, ?> entry = (ReferenceMapSoftReference<?, ?>) reference;

            entry.clear();
            map.remove(entry.getKey());
        }
    }

    public void clear() {
        map.clear();

        Reference<? extends V> reference = null;

        while ((reference = queue.poll()) != null) {

            // release queue entries
            reference.clear();
        }
    }

    public boolean containsKey(Object key) {
        purge();

        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public V get(Object key) {
        purge();

        Reference<V> reference = map.get(key);

        if (reference != null) {
            return reference.get();
        }

        return null;
    }

    public boolean isEmpty() {
        purge();

        return map.isEmpty();
    }

    public Set<K> keySet() {
        purge();

        return map.keySet();
    }

    private V doPut(K key, V value) {
        Reference<V> reference = map.put(key, new ReferenceMapSoftReference<K, V>(key, value, queue));

        if (reference != null) {
            return reference.get();
        }

        return null;
    }

    public V put(K key, V value) {
        purge();

        V v = doPut(key, value);

        purge();

        return v;
    }

    public void putAll(Map<? extends K, ? extends V> t) {
        purge();

        for (Map.Entry<? extends K, ? extends V> entry : t.entrySet()) {
            doPut(entry.getKey(), entry.getValue());
        }

        purge();
    }

    public V remove(Object key) {
        purge();

        Reference<V> reference = map.remove(key);

        if (reference != null) {
            return reference.get();
        }

        return null;
    }

    public int size() {
        purge();

        return map.size();
    }

    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    protected static class ReferenceMapSoftReference<K, V> extends SoftReference<V> {
        private K key;

        public ReferenceMapSoftReference(K key, V value, ReferenceQueue<? super V> queue) {
            super(value, queue);
            this.key = key;
        }

        public K getKey() {
            return key;
        }
    }
}
