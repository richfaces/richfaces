/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.cache.lru;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Assert;

/**
 * @author Nick Belaevski
 *
 */
public class CacheMapTest extends TestCase {
    private void addToCacheMap(CacheMap cacheMap, String key, String value) {
        CacheEntry entry = new CacheEntry(key, value, null);
        cacheMap.put(key, entry);
    }

    public void testLRUEviction() throws Exception {
        CacheMap cache = new CacheMap(3);
        addToCacheMap(cache, "key1", "value1");
        addToCacheMap(cache, "key2", "value2");
        addToCacheMap(cache, "key3", "value3");
        Assert.assertEquals("value1", cache.get("key1").getValue());
        Assert.assertEquals("value2", cache.get("key2").getValue());
        Assert.assertEquals("value3", cache.get("key3").getValue());
        cache.get("key1");
        cache.get("key3");
        addToCacheMap(cache, "key4", "value4");
        Assert.assertEquals("value1", cache.get("key1").getValue());
        Assert.assertNull(cache.get("key2"));
        Assert.assertEquals("value3", cache.get("key3").getValue());
        Assert.assertEquals("value4", cache.get("key4").getValue());
    }

    public void testExpirationQueue() throws Exception {
        CacheMap cacheMap = new CacheMap();

        Assert.assertTrue(cacheMap.getExpirationQueue().isEmpty());

        CacheEntry cacheEntry = new CacheEntry("key", "value", new Date(System.currentTimeMillis() + 1000));
        cacheMap.put("key", cacheEntry);
        Assert.assertNotNull(cacheMap.get("key"));
        Assert.assertSame(cacheEntry, cacheMap.get("key"));
        Assert.assertFalse(cacheMap.getExpirationQueue().isEmpty());
        cacheMap.clear();
        Assert.assertTrue(cacheMap.getExpirationQueue().isEmpty());
        cacheMap.put("key2", new CacheEntry("key2", "value2", new Date(System.currentTimeMillis() + 1000)));
        Assert.assertNotNull(cacheMap.get("key2"));
        cacheMap.remove("key2");
        Assert.assertTrue(cacheMap.getExpirationQueue().isEmpty());
    }
}
