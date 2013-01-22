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
package org.richfaces.resource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

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
