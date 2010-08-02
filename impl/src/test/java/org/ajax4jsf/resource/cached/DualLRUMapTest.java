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



package org.ajax4jsf.resource.cached;

import junit.framework.TestCase;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public class DualLRUMapTest extends TestCase {
    public void testBasic() throws Exception {
        DualLRUMap<String, String> map = new DualLRUMap<String, String>(16);

        map.put("1", "a");
        map.put("2", "b");
        map.put("3", "c");
        assertEquals("a", map.get("1"));
        assertEquals("b", map.get("2"));
        assertEquals("c", map.get("3"));
        assertEquals("1", map.getKey("a"));
        assertEquals("2", map.getKey("b"));
        assertEquals("3", map.getKey("c"));
        assertTrue(map.containsKey("1"));
        assertTrue(map.containsValue("a"));
        assertTrue(map.containsKey("2"));
        assertTrue(map.containsValue("b"));
        assertTrue(map.containsKey("3"));
        assertTrue(map.containsValue("c"));
        map.remove("2");
        assertNull(map.get("2"));
        assertNull(map.getKey("b"));
        assertFalse(map.containsKey("2"));
        assertFalse(map.containsValue("b"));
    }

    public void testRemoveEldest() throws Exception {
        DualLRUMap<String, String> map = new DualLRUMap<String, String>(2);

        map.put("1", "a");
        map.put("2", "b");
        assertNotNull(map.get("2"));
        assertNotNull(map.get("1"));
        map.put("3", "c");
        assertNull(map.get("2"));
        assertNull(map.getKey("b"));
    }
}
