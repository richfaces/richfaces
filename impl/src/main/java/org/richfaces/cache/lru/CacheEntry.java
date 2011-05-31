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

/**
 * User: akolonitsky Date: Oct 13, 2009
 */
public final class CacheEntry implements Comparable<CacheEntry> {
    private Date expired;
    private Object key;
    private Object value;

    public CacheEntry(Object key, Object value, Date expired) {
        super();
        this.key = key;
        this.value = value;
        this.expired = (expired == null) ? null : (Date) expired.clone();
    }

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public boolean isExpired() {
        return (expired != null) && (System.currentTimeMillis() >= expired.getTime());
    }

    public Date getExpired() {
        return (expired == null) ? null : (Date) expired.clone();
    }

    // due to PriorityQueue bug in JDK 1.5 comparator should return 0 ONLY if entries are equal
    public int compareTo(CacheEntry o) {

        // reverse sort order
        int result = expired.compareTo(o.expired);

        if (result == 0) {
            result = key.toString().compareTo(o.key.toString());
        }

        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((key == null) ? 0 : key.hashCode());

        return result;
    }

    // see compareTo()
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        CacheEntry other = (CacheEntry) obj;

        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "<CacheEntry> " + ((expired == null) ? key.toString() : key + ": " + expired);
    }
}
