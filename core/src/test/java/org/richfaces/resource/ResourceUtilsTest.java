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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class ResourceUtilsTest {
    @Test
    public void testFormatWeakTag() throws Exception {
        assertNull(ResourceUtils.formatWeakTag(null));
        assertEquals("W/\"123-456789\"", ResourceUtils.formatWeakTag("123-456789"));

        try {
            ResourceUtils.formatWeakTag("123\"456789");
            fail();
        } catch (IllegalArgumentException e) {

            // ok
        }
    }

    @Test
    public void testFormatTag() throws Exception {
        assertNull(ResourceUtils.formatTag(null));
        assertEquals("\"123-456789\"", ResourceUtils.formatTag("123-456789"));

        try {
            ResourceUtils.formatTag("123\"456789");
            fail();
        } catch (IllegalArgumentException e) {

            // ok
        }
    }

    @Test
    public void testMatchTag() throws Exception {
        assertTrue(ResourceUtils.matchTag("\"123-4567890\"", "W/\"123-4567890\""));
        assertTrue(ResourceUtils.matchTag("\"123-4567890\"", "W/\"123-4567890\", \"123-4567891\""));
        assertTrue(ResourceUtils.matchTag("W/\"123-4567891\"", "W/\"123-4567890\", \"123-4567891\""));
        assertFalse(ResourceUtils.matchTag("\"123-4567890\"", "W/\"023-4567890\""));
        assertFalse(ResourceUtils.matchTag("\"123-4567890\"", "W/\"023-4567890\", \"023-4567891\""));
        assertFalse(ResourceUtils.matchTag("W/\"123-4567891\"", "W/\"023-4567890\", \"023-4567891\""));

        try {
            ResourceUtils.matchTag(null, "W/\"123-4567890\", \"123-4567891\"");
            fail();
        } catch (IllegalArgumentException e) {

            // ok
        }

        try {
            ResourceUtils.matchTag("W/\"123-4567890\"", null);
            fail();
        } catch (IllegalArgumentException e) {

            // ok
        }

        try {
            ResourceUtils.matchTag("123\"456789", "W/\"123\"");
            fail();
        } catch (IllegalArgumentException e) {

            // ok
        }
    }
}
