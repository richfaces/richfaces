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
package org.richfaces.util;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * Last Recent Used Map cache. See {@link LinkedHashMap} for details.
 *
 * @author asmirnov
 */
public class LRUMap<K, V> extends LinkedHashMap<K, V> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7232885382582796665L;
    private int capacity;

    /**
     * Default capacity constructor
     */
    public LRUMap() {
        this(1000);
    }

    /**
     * @param capacity - maximal cache capacity.
     */
    public LRUMap(int capacity) {
        super(capacity, 1.0f, true);
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Entry<K, V> entry) {

        // Remove last entry if size exceeded.
        return size() > capacity;
    }
}
