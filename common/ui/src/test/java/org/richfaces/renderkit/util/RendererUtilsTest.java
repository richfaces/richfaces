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
package org.richfaces.renderkit.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author Nick Belaevski
 * @since 3.3.2
 */
public class RendererUtilsTest extends TestCase {
    public void testIsEmpty() throws Exception {
        RendererUtils utils = RendererUtils.getInstance();

        assertTrue(utils.isEmpty(null));
        assertFalse(utils.isEmpty(new Object()));
        assertFalse(utils.isEmpty(Boolean.FALSE));
        assertFalse(utils.isEmpty(Long.valueOf(0)));
        assertFalse(utils.isEmpty(Integer.valueOf(0)));
        assertFalse(utils.isEmpty(Short.valueOf((short) 0)));
        assertFalse(utils.isEmpty(Byte.valueOf((byte) 0)));
        assertTrue(utils.isEmpty(""));
        assertFalse(utils.isEmpty("s"));
        assertTrue(utils.isEmpty(new ArrayList<Object>()));
        assertTrue(utils.isEmpty(Collections.EMPTY_LIST));

        List<Object> testList = new ArrayList<Object>();

        testList.add("x");
        assertFalse(utils.isEmpty(testList));
        assertTrue(utils.isEmpty(new HashMap<String, Object>()));
        assertTrue(utils.isEmpty(Collections.EMPTY_MAP));

        Map<String, Object> testMap = new HashMap<String, Object>();

        testMap.put("x", "y");
        assertFalse(utils.isEmpty(testMap));
        assertTrue(utils.isEmpty(new Object[0]));
        assertTrue(utils.isEmpty(new int[0]));
        assertFalse(utils.isEmpty(new Object[1]));
        assertFalse(utils.isEmpty(new int[1]));
    }
}
