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
package org.richfaces.renderkit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.richfaces.renderkit.RenderKitUtils.addToScriptHash;
import static org.richfaces.renderkit.RenderKitUtils.toScriptArgs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.ScriptUtils;
import org.junit.Test;
import org.richfaces.renderkit.RenderKitUtils.ScriptHashVariableWrapper;

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

    private static String dehydrate(String s) {
        return s.replaceAll("\\s+", "");
    }

    @Test
    public void testToScriptArgs() throws Exception {
        assertEquals("", toScriptArgs());
        assertEquals("", toScriptArgs((Object) null));
        assertEquals("", toScriptArgs((Object[]) null));

        assertEquals("\"test\"", toScriptArgs("test"));
        assertEquals("[5,8]", dehydrate(toScriptArgs(Arrays.asList(5, 8))));
        assertEquals("{\"a\":true}", dehydrate(toScriptArgs(Collections.singletonMap("a", true))));

        assertEquals("\"test\"", toScriptArgs("test", null));
        assertEquals("null,\"test\"", toScriptArgs(null, "test"));

        assertEquals("\"test\"", toScriptArgs("test", Collections.emptyList()));
        assertEquals("[],\"test\"", dehydrate(toScriptArgs(Collections.emptyList(), "test")));

        assertEquals("\"test\"", toScriptArgs("test", Collections.emptyMap()));
        assertEquals("{},\"test\"", dehydrate(toScriptArgs(Collections.emptyMap(), "test")));

        assertEquals("\"test\"", toScriptArgs("test", ""));
        assertEquals("\"\",\"test\"", dehydrate(toScriptArgs("", "test")));

        assertEquals("1,2,3", toScriptArgs(1, 2, 3, null));
        assertEquals("1,2,null,3", toScriptArgs(1, 2, null, 3));
    }

    @Test
    public void testScriptHashVariableWrapper() throws Exception {
        assertEquals("abc", ScriptHashVariableWrapper.noop.wrap("abc"));

        Object eventHandler = ScriptHashVariableWrapper.eventHandler.wrap("abc");

        assertTrue(eventHandler instanceof JSFunctionDefinition);

        JSFunctionDefinition handlerFunction = (JSFunctionDefinition) eventHandler;
        assertEquals("function(event){abc}", dehydrate(handlerFunction.toScript()));

        Object arrayObject = ScriptHashVariableWrapper.asArray.wrap("header, footer");
        assertEquals("[\"header\",\"footer\"]", dehydrate(ScriptUtils.toScript(arrayObject)));
    }

    @Test
    public void testAddToScriptHash() throws Exception {
        Map<String, Object> hash = new HashMap<String, Object>();

        addToScriptHash(hash, "x", "y", null, null);
        assertEquals("y", hash.get("x"));
        addToScriptHash(hash, "y", "", null, null);
        assertNull(hash.get("y"));
        assertFalse(hash.containsKey("y"));
        addToScriptHash(hash, "y1", null, null, null);
        assertNull(hash.get("y1"));
        assertFalse(hash.containsKey("y1"));
        addToScriptHash(hash, "st", "server", "", null);
        assertEquals("server", hash.get("st"));
        addToScriptHash(hash, "st1", "ajax", "ajax", null);
        assertNull(hash.get("st1"));
        assertFalse(hash.containsKey("st1"));
        addToScriptHash(hash, "st2", "", "ajax", null);
        assertNull(hash.get("st2"));
        assertFalse(hash.containsKey("st2"));
        addToScriptHash(hash, "null", null, "server", null);
        assertNull(hash.get("null"));
        assertFalse(hash.containsKey("null"));
        addToScriptHash(hash, "b", false, null, null);
        assertNull(hash.get("b"));
        assertFalse(hash.containsKey("b"));
        addToScriptHash(hash, "b1", true, null, null);
        assertEquals(Boolean.TRUE, hash.get("b1"));
        addToScriptHash(hash, "b2", true, "true", null);
        assertNull(hash.get("b2"));
        assertFalse(hash.containsKey("b2"));
        addToScriptHash(hash, "b3", false, "true", null);
        assertEquals(Boolean.FALSE, hash.get("b3"));
        addToScriptHash(hash, "b4", true, "false", null);
        assertEquals(Boolean.TRUE, hash.get("b4"));
        addToScriptHash(hash, "b5", false, "false", null);
        assertNull(hash.get("b5"));
        assertFalse(hash.containsKey("b5"));
        addToScriptHash(hash, "i", Integer.valueOf(0), null, null);
        assertEquals(Integer.valueOf(0), hash.get("i"));
        addToScriptHash(hash, "i1", Integer.valueOf(0), "0", null);
        assertNull(hash.get("i1"));
        assertFalse(hash.containsKey("i1"));
        addToScriptHash(hash, "i2", Integer.valueOf(0), "1", null);
        assertEquals(Integer.valueOf(0), hash.get("i2"));
        addToScriptHash(hash, "i3", Integer.MIN_VALUE, null, null);
        assertNull(hash.get("i3"));
        assertFalse(hash.containsKey("i3"));
        addToScriptHash(hash, "i4", Integer.MIN_VALUE, "0", null);
        assertNull(hash.get("i4"));
        assertFalse(hash.containsKey("i4"));
        addToScriptHash(hash, "plain", "test", null, ScriptHashVariableWrapper.noop);
        assertEquals("test", hash.get("plain"));
        addToScriptHash(hash, "plain1", "newtest", "blank", ScriptHashVariableWrapper.noop);
        assertEquals("newtest", hash.get("plain1"));
        addToScriptHash(hash, "onclick", "alert(1)", null, ScriptHashVariableWrapper.eventHandler);
        assertTrue(hash.get("onclick") instanceof JSFunctionDefinition);
        addToScriptHash(hash, "onclick1", "alert(1)", "no-val", ScriptHashVariableWrapper.eventHandler);
        assertTrue(hash.get("onclick1") instanceof JSFunctionDefinition);
    }

    @Test
    public void testAsArray() {
        assertNull(RenderKitUtils.asArray(null));
    }

    @Test
    public void testAsArray1() {
        String[] strings = new String[] { "a", "b" };
        String[] array = RenderKitUtils.asArray(strings);

        assertSame(strings, array);
    }

    @Test
    public void testAsArray2() {
        Object[] objects = new Object[] { Integer.valueOf(12), null, Integer.valueOf(22), Integer.valueOf(42) };
        String[] array = RenderKitUtils.asArray(objects);
        String[] etalon = new String[] { "12", null, "22", "42" };

        assertTrue(Arrays.equals(etalon, array));
    }

    @Test
    public void testAsArray3() {
        ArrayList<Integer> list = new ArrayList<Integer>();

        list.add(new Integer(12));
        list.add(null);
        list.add(new Integer(22));
        list.add(new Integer(42));

        String[] array = RenderKitUtils.asArray(list);
        String[] etalon = new String[] { "12", null, "22", "42" };

        assertTrue(Arrays.equals(etalon, array));
    }

    @Test
    public void testAsArray31() {
        Set<Integer> set = new TreeSet<Integer>();

        set.add(new Integer(12));
        set.add(new Integer(22));
        set.add(new Integer(42));

        String[] array = RenderKitUtils.asArray(set);
        String[] etalon = new String[] { "12", "22", "42" };

        assertTrue(Arrays.equals(etalon, array));
    }

    @Test
    public void testAsArray4() {
        String string = " a , \t\n b  \n , c ";
        String[] strings = RenderKitUtils.asArray(string);
        String[] etalon = new String[] { "a", "b", "c" };

        assertTrue(Arrays.equals(etalon, strings));
    }
}
