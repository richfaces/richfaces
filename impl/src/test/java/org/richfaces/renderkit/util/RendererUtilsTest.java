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

import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.richfaces.renderkit.util.RendererUtils;
import org.richfaces.renderkit.util.RendererUtils.ScriptHashVariableWrapper;

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

    public void testScriptHashVariableWrapper() throws Exception {
        assertEquals("abc", RendererUtils.ScriptHashVariableWrapper.DEFAULT.wrap("abc"));

        Object eventHandler = RendererUtils.ScriptHashVariableWrapper.EVENT_HANDLER.wrap("abc");

        assertTrue(eventHandler instanceof JSFunctionDefinition);

        JSFunctionDefinition handlerFunction = (JSFunctionDefinition) eventHandler;

        assertEquals("function(event){abc}", handlerFunction.toScript().replaceAll("\\s", ""));
    }

    public void testAddToScriptHash() throws Exception {
        Map<String, Object> hash = new HashMap<String, Object>();
        RendererUtils utils = RendererUtils.getInstance();

        utils.addToScriptHash(hash, "x", "y", null, null);
        assertEquals("y", hash.get("x"));
        utils.addToScriptHash(hash, "y", "", null, null);
        assertNull(hash.get("y"));
        assertFalse(hash.containsKey("y"));
        utils.addToScriptHash(hash, "y1", null, null, null);
        assertNull(hash.get("y1"));
        assertFalse(hash.containsKey("y1"));
        utils.addToScriptHash(hash, "st", "server", "", null);
        assertEquals("server", hash.get("st"));
        utils.addToScriptHash(hash, "st1", "ajax", "ajax", null);
        assertNull(hash.get("st1"));
        assertFalse(hash.containsKey("st1"));
        utils.addToScriptHash(hash, "st2", "", "ajax", null);
        assertNull(hash.get("st2"));
        assertFalse(hash.containsKey("st2"));
        utils.addToScriptHash(hash, "null", null, "server", null);
        assertNull(hash.get("null"));
        assertFalse(hash.containsKey("null"));
        utils.addToScriptHash(hash, "b", false, null, null);
        assertNull(hash.get("b"));
        assertFalse(hash.containsKey("b"));
        utils.addToScriptHash(hash, "b1", true, null, null);
        assertEquals(Boolean.TRUE, hash.get("b1"));
        utils.addToScriptHash(hash, "b2", true, "true", null);
        assertNull(hash.get("b2"));
        assertFalse(hash.containsKey("b2"));
        utils.addToScriptHash(hash, "b3", false, "true", null);
        assertEquals(Boolean.FALSE, hash.get("b3"));
        utils.addToScriptHash(hash, "b4", true, "false", null);
        assertEquals(Boolean.TRUE, hash.get("b4"));
        utils.addToScriptHash(hash, "b5", false, "false", null);
        assertNull(hash.get("b5"));
        assertFalse(hash.containsKey("b5"));
        utils.addToScriptHash(hash, "i", Integer.valueOf(0), null, null);
        assertEquals(Integer.valueOf(0), hash.get("i"));
        utils.addToScriptHash(hash, "i1", Integer.valueOf(0), "0", null);
        assertNull(hash.get("i1"));
        assertFalse(hash.containsKey("i1"));
        utils.addToScriptHash(hash, "i2", Integer.valueOf(0), "1", null);
        assertEquals(Integer.valueOf(0), hash.get("i2"));
        utils.addToScriptHash(hash, "i3", Integer.MIN_VALUE, null, null);
        assertNull(hash.get("i3"));
        assertFalse(hash.containsKey("i3"));
        utils.addToScriptHash(hash, "i4", Integer.MIN_VALUE, "0", null);
        assertNull(hash.get("i4"));
        assertFalse(hash.containsKey("i4"));
        utils.addToScriptHash(hash, "plain", "test", null, ScriptHashVariableWrapper.DEFAULT);
        assertEquals("test", hash.get("plain"));
        utils.addToScriptHash(hash, "plain1", "newtest", "blank", ScriptHashVariableWrapper.DEFAULT);
        assertEquals("newtest", hash.get("plain1"));
        utils.addToScriptHash(hash, "onclick", "alert(1)", null, ScriptHashVariableWrapper.EVENT_HANDLER);
        assertTrue(hash.get("onclick") instanceof JSFunctionDefinition);
        utils.addToScriptHash(hash, "onclick1", "alert(1)", "no-val", ScriptHashVariableWrapper.EVENT_HANDLER);
        assertTrue(hash.get("onclick1") instanceof JSFunctionDefinition);
    }
}
