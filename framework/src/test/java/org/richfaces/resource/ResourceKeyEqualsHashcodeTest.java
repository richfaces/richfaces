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
package org.richfaces.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public class ResourceKeyEqualsHashcodeTest {

    private static final String NAME = "sample1";

    @Test
    public void testLibsNull() {
        assertKeysEqual(resourceKey(NAME, null), resourceKey(NAME, null));
    }

    @Test
    public void testLibsEmptyAndNull() {
        assertKeysEqual(resourceKey(NAME, ""), resourceKey(NAME, null));
    }

    @Test
    public void testLibsNonEmptyAndNull() {
        assertKeysNotEqual(resourceKey(NAME, "lib1"), resourceKey(NAME, null));
    }

    @Test
    public void testLibsEmpty() {
        assertKeysEqual(resourceKey(NAME, ""), resourceKey(NAME, ""));
    }

    @Test
    public void testLibsNotEqual() {
        assertKeysNotEqual(resourceKey(NAME, "lib1"), resourceKey(NAME, "lib2"));
    }

    @Test
    public void testNamesEquals() {
        assertKeysEqual(resourceKey(NAME, NAME), resourceKey(NAME, NAME));
    }

    @Test
    public void testNamesNull() {
        assertKeysEqual(resourceKey(null, NAME), resourceKey(null, NAME));
    }

    @Test
    public void testNamesNotEqual() {
        assertKeysNotEqual(resourceKey("name1", NAME), resourceKey("name2", NAME));
    }

    @Test
    public void testNamesOneNull() {
        assertKeysNotEqual(resourceKey(null, NAME), resourceKey("name2", NAME));
    }

    public void assertKeysEqual(ResourceKey key1, ResourceKey key2) {
        assertTrue(key1.equals(key2));
        assertTrue(key2.equals(key1));
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    public void assertKeysNotEqual(ResourceKey key1, ResourceKey key2) {
        assertFalse(key1.equals(key2));
        assertFalse(key2.equals(key1));
        assertTrue(key1.hashCode() != key2.hashCode());
    }

    private ResourceKey resourceKey(String resourceName, String resourceLibrary) {
        return new ResourceKey(resourceName, resourceLibrary);
    }
}
