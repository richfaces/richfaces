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

import java.util.Iterator;
import java.util.Map.Entry;

import junit.framework.TestCase;

/**
 * @author asmirnov
 *
 */
public class LRUMapTest extends TestCase {
    /**
     * @param name
     */
    public LRUMapTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link org.richfaces.util.LRUMap#LRUMap(int)}.
     */
    public void testLRUMap() {
        LRUMap map = new LRUMap(5);

        for (int i = 0; i < 10; i++) {
            map.put(new Integer(i), "Val" + (new Integer(i)));
        }

        assertEquals(map.size(), 5);
    }

    /**
     * Test method for {@link org.richfaces.util.LRUMap#removeEldestEntry(java.util.Map.Entry)}.
     */
    public void testRemoveEldestEntryEntry() {
        LRUMap map = new LRUMap(5) {
            protected boolean removeEldestEntry(Entry arg0) {
                boolean eldestEntry = super.removeEldestEntry(arg0);

                assertTrue(eldestEntry ^ size() <= 5);

                return false;
            }
        };

        for (int i = 0; i < 10; i++) {
            map.put(new Integer(i), "Val" + (new Integer(i)));
        }
    }

    /**
     * Test method for {@link java.util.HashMap#put(K, V)}.
     */
    public void testPut() {
        LRUMap map = new LRUMap(5);

        for (int i = 0; i < 10; i++) {
            map.put(new Integer(i), "Val" + (new Integer(i)));
        }

        assertEquals(map.size(), 5);

        Iterator iterator = map.values().iterator();

        for (int i = 5; i < 10; i++) {
            assertTrue(iterator.hasNext());
            assertEquals("Val" + (new Integer(i)), iterator.next());
        }

        assertFalse(iterator.hasNext());
    }
}
