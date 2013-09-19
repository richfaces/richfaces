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
package org.richfaces.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Nick Belaevski
 *
 */
public class SequenceIterationStatusTest {
    @Test
    public void testBeginEnd() throws Exception {
        SequenceIterationStatus s1 = new SequenceIterationStatus(5, 10, 5, 100);
        assertEquals(Integer.valueOf(5), s1.getBegin());
        assertEquals(Integer.valueOf(10), s1.getEnd());

        SequenceIterationStatus s2 = new SequenceIterationStatus(null, null, 5, 100);
        assertNull(s2.getBegin());
        assertEquals(Integer.valueOf(99), s2.getEnd());

        SequenceIterationStatus s3 = new SequenceIterationStatus(null, null, 5, null);
        assertNull(s3.getBegin());
        assertEquals(Integer.valueOf(Integer.MAX_VALUE), s3.getEnd());
    }

    @Test
    public void testFirst() throws Exception {
        SequenceIterationStatus s1 = new SequenceIterationStatus(0, 10, 0, 100);
        assertTrue(s1.isFirst());
        assertFalse(s1.isLast());

        SequenceIterationStatus s2 = new SequenceIterationStatus(0, 10, 1, 100);
        assertFalse(s2.isFirst());
        assertFalse(s2.isLast());

        SequenceIterationStatus s3 = new SequenceIterationStatus(0, 0, 0, 100);
        assertTrue(s3.isFirst());
        assertTrue(s3.isLast());

        SequenceIterationStatus s4 = new SequenceIterationStatus(5, 10, 5, 100);
        assertTrue(s4.isFirst());
        assertFalse(s4.isLast());

        SequenceIterationStatus s5 = new SequenceIterationStatus(5, 10, 6, 100);
        assertFalse(s5.isFirst());
        assertFalse(s5.isLast());

        SequenceIterationStatus s6 = new SequenceIterationStatus(null, 10, 0, 100);
        assertTrue(s6.isFirst());
        assertFalse(s6.isLast());
    }

    @Test
    public void testLast() throws Exception {
        SequenceIterationStatus s1 = new SequenceIterationStatus(0, 9, 9, 100);
        assertTrue(s1.isLast());
        assertFalse(s1.isFirst());

        SequenceIterationStatus s2 = new SequenceIterationStatus(0, 9, 8, 100);
        assertFalse(s2.isLast());
        assertFalse(s2.isFirst());

        SequenceIterationStatus s3 = new SequenceIterationStatus(0, 100, 9, 10);
        assertTrue(s3.isLast());
        assertFalse(s3.isFirst());

        SequenceIterationStatus s4 = new SequenceIterationStatus(0, 100, 8, 10);
        assertFalse(s4.isLast());
        assertFalse(s4.isFirst());

        SequenceIterationStatus s5 = new SequenceIterationStatus(0, null, 9, 10);
        assertTrue(s5.isLast());
        assertFalse(s5.isFirst());

        SequenceIterationStatus s6 = new SequenceIterationStatus(0, null, 8, 10);
        assertFalse(s6.isLast());
        assertFalse(s6.isFirst());
    }

    @Test
    public void testIsEvenOdd() throws Exception {
        SequenceIterationStatus s1 = new SequenceIterationStatus(0, 100, 0, 10);
        assertTrue(s1.isOdd());
        assertFalse(s1.isEven());

        SequenceIterationStatus s2 = new SequenceIterationStatus(0, 100, 1, 10);
        assertTrue(s2.isEven());
        assertFalse(s2.isOdd());

        SequenceIterationStatus s3 = new SequenceIterationStatus(5, 100, 7, 10);
        assertTrue(s3.isOdd());
        assertFalse(s3.isEven());

        SequenceIterationStatus s4 = new SequenceIterationStatus(5, 100, 6, 10);
        assertTrue(s4.isEven());
        assertFalse(s4.isOdd());
    }

    @Test
    public void testGetRowCount() throws Exception {
        SequenceIterationStatus s1 = new SequenceIterationStatus(0, 100, 0, 200);
        assertEquals(Integer.valueOf(200), s1.getRowCount());

        SequenceIterationStatus s2 = new SequenceIterationStatus(0, 400, 100, 150);
        assertEquals(Integer.valueOf(150), s2.getRowCount());
    }

    @Test
    public void testGetIndex() throws Exception {
        SequenceIterationStatus s1 = new SequenceIterationStatus(0, 100, 30, 200);
        assertTrue(30 == s1.getIndex());

        SequenceIterationStatus s2 = new SequenceIterationStatus(0, 400, 100, 150);
        assertTrue(100 == s2.getIndex());
    }

    @Test
    public void testGetCount() throws Exception {
        SequenceIterationStatus s1 = new SequenceIterationStatus(0, 100, 0, 10);
        assertTrue(s1.getCount() == 1);

        SequenceIterationStatus s2 = new SequenceIterationStatus(0, 100, 1, 10);
        assertTrue(s2.getCount() == 2);

        SequenceIterationStatus s3 = new SequenceIterationStatus(5, 100, 7, 10);
        assertTrue(s3.getCount() == 3);

        SequenceIterationStatus s4 = new SequenceIterationStatus(5, 100, 8, 10);
        assertTrue(s4.getCount() == 4);
    }
}
