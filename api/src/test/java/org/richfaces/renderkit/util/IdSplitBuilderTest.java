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
package org.richfaces.renderkit.util;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.richfaces.renderkit.util.IdSplitBuilder;


/**
 * @author Nick Belaevski
 *
 */
public class IdSplitBuilderTest {

    private static String[] asArray(String... strings) {
        return strings;
    }

    @Test
    public void testEmptyString() throws Exception {
        assertArrayEquals(asArray(), IdSplitBuilder.split(""));
        assertArrayEquals(asArray(), IdSplitBuilder.split(" \r\t\n  "));
    }

    @Test
    public void testOneStrings() throws Exception {
        assertArrayEquals(asArray("test"), IdSplitBuilder.split("test"));
        assertArrayEquals(asArray("some:id"), IdSplitBuilder.split("some:id"));
        assertArrayEquals(asArray("table:[1]"), IdSplitBuilder.split("table:[1]"));
        assertArrayEquals(asArray("table:[1, 2]"), IdSplitBuilder.split("table:[1, 2]"));
        assertArrayEquals(asArray("table:[1, 2]:nestedTable:[*]"), IdSplitBuilder.split("table:[1, 2]:nestedTable:[*]"));
        assertArrayEquals(asArray("table:[1, 2]:[*]:group"), IdSplitBuilder.split("table:[1, 2]:[*]:group"));
        assertArrayEquals(asArray("table:[1 2]:nestedTable:[*]"), IdSplitBuilder.split("table:[1 2]:nestedTable:[*]"));
        assertArrayEquals(asArray("table:[1 2]:[*]:group"), IdSplitBuilder.split("table:[1 2]:[*]:group"));
    }

    @Test
    public void testTwoStrings() throws Exception {
        assertArrayEquals(asArray("test", "abc"), IdSplitBuilder.split("test abc"));
        assertArrayEquals(asArray("some:id", "form:table"), IdSplitBuilder.split("some:id form:table"));
        assertArrayEquals(asArray("test", "abc"), IdSplitBuilder.split("test, abc"));
        assertArrayEquals(asArray("some:id", "form:table"), IdSplitBuilder.split("some:id, form:table"));

        assertArrayEquals(asArray("test:[1 2 3]:abc", "form:[2]"), IdSplitBuilder.split("test:[1 2 3]:abc form:[2]"));
        assertArrayEquals(asArray("[1  2]:some", "[3\t4]:id"), IdSplitBuilder.split("  [1  2]:some   [3\t4]:id  "));
    }

    @Test
    public void testSeveralStrings() throws Exception {
        assertArrayEquals(asArray("test", "abc", "def", "ghi"), IdSplitBuilder.split("test abc def ghi"));
        assertArrayEquals(asArray("test:[1  2]abc", "def", "ghi"), IdSplitBuilder.split("test:[1  2]abc def ghi"));
        assertArrayEquals(asArray("[1  2]abc", "[3, 4]def", "ghi[5 6 7]"),
            IdSplitBuilder.split("[1  2]abc [3, 4]def ghi[5 6 7]"));

        assertArrayEquals(
            asArray("test:[1  2]:abc", "table", "form:table:[  *  ]:child", "extTable:[ 0, 3 ]:child:[1 8]:@header"),
            IdSplitBuilder.split("  test:[1  2]:abc, table," +
            		"  form:table:[  *  ]:child, extTable:[ 0, 3 ]:child:[1 8]:@header"  ));
    }
}
