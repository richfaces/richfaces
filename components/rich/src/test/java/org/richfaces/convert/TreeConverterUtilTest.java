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
package org.richfaces.convert;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.richfaces.convert.TreeConverterUtil.escape;
import static org.richfaces.convert.TreeConverterUtil.unescape;

import java.util.HashMap;

import javax.faces.context.FacesContext;

import org.jboss.test.faces.mock.Environment;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Nick Belaevski
 *
 */
@RunWith(MockTestRunner.class)
public class TreeConverterUtilTest {
    @Mock
    @Environment({ Environment.Feature.EXTERNAL_CONTEXT })
    private MockFacesEnvironment environment;

    @Before
    public void setUp() throws Exception {
        environment.resetToNice();
        FacesContext facesContext = environment.getFacesContext();
        expect(facesContext.getAttributes()).andStubReturn(new HashMap<Object, Object>());
        environment.replay();
    }

    @Test
    public void testUnescape() throws Exception {
        assertEquals("", unescape(""));
        assertEquals("a", unescape("a"));
        assertEquals("abc", unescape("abc"));

        assertEquals("_", unescape("__"));
        assertEquals("__", unescape("____"));

        assertEquals("_a", unescape("__a"));
        assertEquals("_abc", unescape("__abc"));

        assertEquals("a_", unescape("a__"));
        assertEquals("abc_", unescape("abc__"));

        assertEquals("some_test", unescape("some__test"));

        assertEquals("\u0000", unescape("_x00"));
        assertEquals("\u0001", unescape("_x01"));
        assertEquals("\u0010", unescape("_x10"));
        assertEquals("\u0077", unescape("_x77"));
        assertEquals("\u00A9", unescape("_xa9"));
        assertEquals("\u00FF", unescape("_xFF"));
        assertEquals("\u00FF", unescape("_xff"));

        assertEquals(".", unescape("_x2e"));

        assertEquals("\u0000", unescape("_u0000"));
        assertEquals("\u0001", unescape("_u0001"));
        assertEquals("\uE1A4", unescape("_ue1a4"));
        assertEquals("\uF000", unescape("_uF000"));
        assertEquals("\uFFFF", unescape("_uFFFF"));

        assertEquals(".", unescape("_u002E"));

        assertEquals("Embedded_underscores\u0312unicode\u0045etc", unescape("Embedded__underscores_u0312unicode_x45etc"));

        try {
            unescape("_");

            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            unescape("some _a string");

            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            unescape("_x");

            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            unescape("_x0");

            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            unescape("_u");

            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            unescape("_u-1aaa");

            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            unescape("_u1zaa");

            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            unescape("embedded _x 00 into string");

            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            unescape("embedded _x0 into string");

            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            unescape("embedded _u012 into string");

            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testEscape() throws Exception {
        assertEquals("", escape(""));
        assertEquals("__a__b__", escape("_a_b_"));
        assertEquals("_x3a0_x3a1", escape(":0:1"));
        assertEquals("testTEST05", escape("testTEST05"));
        assertEquals("a_xa9c", escape("a\u00a9c"));
        assertEquals("a_u037ec", escape("a\u037ec"));
        assertEquals("a_ue1acd", escape("a\ue1acd"));
    }
}
