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
package org.richfaces.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Nick Belaevski
 *
 */
public class IdParserTest {

    private IdParser idParser;

    @Before
    public void setUp() throws Exception {
        idParser = new IdParser(':', '@');
    }

    @After
    public void tearDown() throws Exception {
        idParser = null;
    }

    @Test
    public void testIncorrectIds() throws Exception {
        idParser.setId("");

        idParser.findNext();
        assertFalse(idParser.findNext());

        idParser.setId(":test");

        idParser.findNext();
        idParser.findNext();
        assertFalse(idParser.findNext());

        idParser.setId("test:");

        idParser.findNext();
        idParser.findNext();
        assertFalse(idParser.findNext());

        idParser.setId("@head");

        idParser.findNext();
        assertFalse(idParser.findNext());

        idParser.setId("head@");

        idParser.findNext();
        assertFalse(idParser.findNext());
    }

    @Test
    public void testSimpleId() throws Exception {
        idParser.setId("simpleId");

        assertTrue(idParser.findNext());
        assertEquals("simpleId", idParser.getComponentId());
        assertNull(idParser.getMetadataComponentId());

        assertFalse(idParser.findNext());
    }

    @Test
    public void testSubComponentId() throws Exception {
        idParser.setId("table@head");

        assertTrue(idParser.findNext());
        assertEquals("@head", idParser.getMetadataComponentId());
        assertEquals("table", idParser.getComponentId());

        assertFalse(idParser.findNext());
    }

    @Test
    public void testSeries() throws Exception {
        idParser.setId("form:table:0:nestedTable@body");

        assertTrue(idParser.findNext());
        assertEquals("form", idParser.getComponentId());
        assertNull(idParser.getMetadataComponentId());

        assertTrue(idParser.findNext());
        assertEquals("table", idParser.getComponentId());
        assertNull(idParser.getMetadataComponentId());

        assertTrue(idParser.findNext());
        assertEquals("0", idParser.getComponentId());
        assertNull(idParser.getMetadataComponentId());

        assertTrue(idParser.findNext());
        assertEquals("@body", idParser.getMetadataComponentId());
        assertEquals("nestedTable", idParser.getComponentId());

        assertFalse(idParser.findNext());

        idParser.setId("myBigTable@header");

        assertTrue(idParser.findNext());
        assertEquals("@header", idParser.getMetadataComponentId());
        assertEquals("myBigTable", idParser.getComponentId());

        assertFalse(idParser.findNext());

        idParser.setId("tree:0-12-28:node@status:table:10:tab@label");

        assertTrue(idParser.findNext());
        assertEquals("tree", idParser.getComponentId());
        assertNull(idParser.getMetadataComponentId());

        assertTrue(idParser.findNext());
        assertEquals("0-12-28", idParser.getComponentId());
        assertNull(idParser.getMetadataComponentId());

        assertTrue(idParser.findNext());
        assertEquals("@status", idParser.getMetadataComponentId());
        assertEquals("node", idParser.getComponentId());

        assertTrue(idParser.findNext());
        assertEquals("table", idParser.getComponentId());
        assertNull(idParser.getMetadataComponentId());

        assertTrue(idParser.findNext());
        assertEquals("10", idParser.getComponentId());
        assertNull(idParser.getMetadataComponentId());

        assertTrue(idParser.findNext());
        assertEquals("@label", idParser.getMetadataComponentId());
        assertEquals("tab", idParser.getComponentId());

        assertFalse(idParser.findNext());
    }

    @Test
    public void testNestedSubcomponents() throws Exception {
        //TODO - review this test - behavior is not clear for now

        idParser.setId("form:table@head@x-head:child");

        assertTrue(idParser.findNext());
        assertEquals("form", idParser.getComponentId());
        assertNull(idParser.getMetadataComponentId());

        assertTrue(idParser.findNext());
        assertEquals("@head@x-head", idParser.getMetadataComponentId());
        assertEquals("table", idParser.getComponentId());

        assertTrue(idParser.findNext());
        assertEquals("child", idParser.getComponentId());
        assertNull(idParser.getMetadataComponentId());

        assertFalse(idParser.findNext());
    }
}
