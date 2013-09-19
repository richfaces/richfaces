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
package org.richfaces.json;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import junit.framework.TestCase;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public class SerializationTest extends TestCase {
    private <T> T saveRestore(T t) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(t);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        T restoredT = (T) t.getClass().cast(ois.readObject());

        return restoredT;
    }

    public void testJSONObject() throws Exception {
        JSONObject restoredObject = saveRestore(new JSONObject("{a:null, b:'abc', c: {d: 'e', f: 'g'}, h: ['i', 'j']}"));

        assertSame(JSONObject.NULL, restoredObject.get("a"));
        assertEquals("abc", restoredObject.get("b"));

        JSONObject nestedObject = (JSONObject) restoredObject.get("c");

        assertEquals("e", nestedObject.get("d"));
        assertEquals("g", nestedObject.get("f"));

        JSONArray array = (JSONArray) restoredObject.get("h");

        assertEquals(2, array.length());
        assertEquals("i", array.get(0));
        assertEquals("j", array.get(1));
    }

    public void testJSONCollection() throws Exception {
        JSONCollection restored = saveRestore(new JSONCollection("[1, '2', null, [3, 4], {a: 'b', c: 'd'}]"));
        Iterator iterator = restored.iterator();

        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals("2", iterator.next());
        assertSame(JSONObject.NULL, iterator.next());

        JSONCollection nestedCollection = (JSONCollection) iterator.next();
        Iterator nestedIterator = nestedCollection.iterator();

        assertEquals(Integer.valueOf(3), nestedIterator.next());
        assertEquals(Integer.valueOf(4), nestedIterator.next());
        assertFalse(nestedIterator.hasNext());

        JSONMap nestedMap = (JSONMap) iterator.next();

        assertEquals("b", nestedMap.get("a"));
        assertEquals("d", nestedMap.get("c"));
        assertEquals(2, nestedMap.size());
        assertFalse(iterator.hasNext());
    }

    public void testJSONMap() throws Exception {
        JSONMap restored = saveRestore(new JSONMap("{a: 'b', c: 3, d: null, e: [5, 'v'], f: {x: 'y', z: 2}}"));

        assertEquals("b", restored.get("a"));
        assertEquals(Integer.valueOf(3), restored.get("c"));
        assertSame(JSONObject.NULL, restored.get("d"));

        JSONCollection nestedCollection = (JSONCollection) restored.get("e");
        Iterator nestedIterator = nestedCollection.iterator();

        assertEquals(Integer.valueOf(5), nestedIterator.next());
        assertEquals("v", nestedIterator.next());
        assertFalse(nestedIterator.hasNext());

        JSONMap nestedObject = (JSONMap) restored.get("f");

        assertEquals("y", nestedObject.get("x"));
        assertEquals(Integer.valueOf(2), nestedObject.get("z"));
        assertEquals(2, nestedObject.size());
        assertEquals(5, restored.size());
    }
}
