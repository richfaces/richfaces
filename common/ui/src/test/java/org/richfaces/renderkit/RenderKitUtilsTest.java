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
package org.richfaces.renderkit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Nick Belaevski
 * 
 */
public class RenderKitUtilsTest {

    @Test
    public void testEscape() throws Exception {
        assertEquals("", RenderKitUtils.escape(""));
        assertEquals("abcd", RenderKitUtils.escape("abcd"));
        assertEquals("\\'abcd", RenderKitUtils.escape("'abcd"));
        assertEquals("abcd\\'", RenderKitUtils.escape("abcd'"));
        assertEquals("ab\\'cd", RenderKitUtils.escape("ab'cd"));
        assertEquals("ab\\'\\'cd", RenderKitUtils.escape("ab''cd"));
        assertEquals("ab\\'c\\'d", RenderKitUtils.escape("ab'c'd"));
    }

    @Test
    public void testChain() throws Exception {
        StringBuilder sb = new StringBuilder();

        assertFalse(RenderKitUtils.chain(sb, "", false));
        assertTrue(sb.length() == 0);
        assertFalse(RenderKitUtils.chain(sb, null, false));
        assertTrue(sb.length() == 0);

        assertFalse(RenderKitUtils.chain(sb, "test", false));
        assertEquals("test", sb.toString());

        assertFalse(RenderKitUtils.chain(sb, "", false));
        assertEquals("test", sb.toString());

        assertTrue(RenderKitUtils.chain(sb, "another'object", false));
        assertEquals("'test','another\\'object'", sb.toString());

        assertTrue(RenderKitUtils.chain(sb, "one more", true));
        assertEquals("'test','another\\'object','one more'", sb.toString());
    }

    @Test
    public void testShouldRenderAttribute() throws Exception {
        assertFalse(RenderKitUtils.shouldRenderAttribute(null));

        assertFalse(RenderKitUtils.shouldRenderAttribute(""));
        assertTrue(RenderKitUtils.shouldRenderAttribute("test"));

        assertTrue(RenderKitUtils.shouldRenderAttribute(new Object()));

        assertTrue(RenderKitUtils.shouldRenderAttribute(Long.valueOf(10)));
        assertTrue(RenderKitUtils.shouldRenderAttribute(Long.valueOf(0)));
        assertFalse(RenderKitUtils.shouldRenderAttribute(Long.MIN_VALUE));

        assertTrue(RenderKitUtils.shouldRenderAttribute(Integer.valueOf(10)));
        assertTrue(RenderKitUtils.shouldRenderAttribute(Integer.valueOf(0)));
        assertFalse(RenderKitUtils.shouldRenderAttribute(Integer.MIN_VALUE));

        assertTrue(RenderKitUtils.shouldRenderAttribute(Short.valueOf((short) 10)));
        assertTrue(RenderKitUtils.shouldRenderAttribute(Short.valueOf((short) 0)));
        assertFalse(RenderKitUtils.shouldRenderAttribute(Short.MIN_VALUE));

        assertTrue(RenderKitUtils.shouldRenderAttribute(Character.valueOf('a')));
        assertTrue(RenderKitUtils.shouldRenderAttribute(Character.valueOf((char) 13)));
        assertFalse(RenderKitUtils.shouldRenderAttribute(Character.MIN_VALUE));

        assertTrue(RenderKitUtils.shouldRenderAttribute(Byte.valueOf((byte) 10)));
        assertTrue(RenderKitUtils.shouldRenderAttribute(Byte.valueOf((byte) 0)));
        assertFalse(RenderKitUtils.shouldRenderAttribute(Byte.MIN_VALUE));

        assertTrue(RenderKitUtils.shouldRenderAttribute(Double.valueOf(10)));
        assertTrue(RenderKitUtils.shouldRenderAttribute(Double.valueOf(0)));
        assertFalse(RenderKitUtils.shouldRenderAttribute(Double.MIN_VALUE));

        assertTrue(RenderKitUtils.shouldRenderAttribute(Float.valueOf(10)));
        assertTrue(RenderKitUtils.shouldRenderAttribute(Float.valueOf(0)));
        assertFalse(RenderKitUtils.shouldRenderAttribute(Float.MIN_VALUE));

        assertTrue(RenderKitUtils.shouldRenderAttribute(Boolean.TRUE));
        assertFalse(RenderKitUtils.shouldRenderAttribute(Boolean.FALSE));
    }

}