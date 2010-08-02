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



package org.richfaces.component.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

/**
 * @author Nick Belaevski
 *         mailto:nbelaevski@exadel.com
 *         created 20.07.2007
 *
 */
public class ComponentUtilTest extends TestCase {
    public void testAsArray() {
        assertNull(ComponentUtil.asArray(null));
    }

    public void testAsArray1() {
        String[] strings = new String[] {"a", "b"};
        String[] array = ComponentUtil.asArray(strings);

        assertSame(strings, array);
    }

    public void testAsArray2() {
        Object[] objects = new Object[] {Integer.valueOf(12), null, Integer.valueOf(22), Integer.valueOf(42)};
        String[] array = ComponentUtil.asArray(objects);
        String[] etalon = new String[] {"12", null, "22", "42"};

        assertTrue(Arrays.equals(etalon, array));
    }

    public void testAsArray3() {
        ArrayList list = new ArrayList();

        list.add(new Integer(12));
        list.add(null);
        list.add(new Integer(22));
        list.add(new Integer(42));

        String[] array = ComponentUtil.asArray(list);
        String[] etalon = new String[] {"12", null, "22", "42"};

        assertTrue(Arrays.equals(etalon, array));
    }

    public void testAsArray31() {
        Set set = new TreeSet();

        set.add(new Integer(12));
        set.add(new Integer(22));
        set.add(new Integer(42));

        String[] array = ComponentUtil.asArray(set);
        String[] etalon = new String[] {"12", "22", "42"};

        assertTrue(Arrays.equals(etalon, array));
    }

    public void testAsArray4() {
        String string = " a , \t\n b  \n , c ";
        String[] strings = ComponentUtil.asArray(string);
        String[] etalon = new String[] {"a", "b", "c"};

        assertTrue(Arrays.equals(etalon, strings));
    }
}
