/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.NoSuchElementException;

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
public class IdSplitIteratorTest {
    @Mock
    @Environment({ Environment.Feature.EXTERNAL_CONTEXT })
    private MockFacesEnvironment environment;

    @Before
    public void setUp() throws Exception {
        environment.resetToNice();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        expect(facesContext.getAttributes()).andStubReturn(new HashMap<Object, Object>());
        environment.replay();
    }

    @Test
    public void testEmptyString() throws Exception {
        IdSplitIterator iterator = new IdSplitIterator("");

        assertFalse(iterator.hasNext());

        try {
            iterator.next();
            fail();
        } catch (NoSuchElementException e) {
            // ignore
        }

        assertNull(iterator.getSubtreeId());
    }

    @Test
    public void testSimpleString() throws Exception {
        IdSplitIterator iterator = new IdSplitIterator("id");

        assertTrue(iterator.hasNext());

        assertEquals("id", iterator.next());
        assertNull(iterator.getSubtreeId());

        assertFalse(iterator.hasNext());

        try {
            iterator.next();
            fail();
        } catch (NoSuchElementException e) {
            // ignore
        }

        assertNull(iterator.getSubtreeId());
    }

    @Test
    public void testTwoSegmentsString() throws Exception {
        IdSplitIterator iterator = new IdSplitIterator("form:table");

        assertTrue(iterator.hasNext());

        assertEquals("table", iterator.next());
        assertEquals("form", iterator.getSubtreeId());

        assertTrue(iterator.hasNext());
        assertEquals("form", iterator.next());
        assertNull(iterator.getSubtreeId());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testThreeSegmentsString() throws Exception {
        IdSplitIterator iterator = new IdSplitIterator("form:table:cell");

        assertTrue(iterator.hasNext());

        assertEquals("cell", iterator.next());
        assertEquals("form:table", iterator.getSubtreeId());

        assertTrue(iterator.hasNext());

        assertEquals("table", iterator.next());
        assertEquals("form", iterator.getSubtreeId());

        assertTrue(iterator.hasNext());
        assertEquals("form", iterator.next());
        assertNull(iterator.getSubtreeId());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testBadStrings() throws Exception {
        IdSplitIterator iterator;

        iterator = new IdSplitIterator(":");
        assertFalse(iterator.hasNext());

        iterator = new IdSplitIterator("test:");
        assertFalse(iterator.hasNext());

        iterator = new IdSplitIterator(":test");
        assertTrue(iterator.hasNext());
        assertEquals("test", iterator.next());
        assertEquals("", iterator.getSubtreeId());
        assertFalse(iterator.hasNext());

        iterator = new IdSplitIterator("::");
        assertFalse(iterator.hasNext());
    }
}
