/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.richfaces.renderkit.ForEachLoop.Status;

/**
 * @author Lukas Fryc
 */
public class ForEachLoopTest {

    private Integer[] array = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };

    @Test
    public void testArrayIterationStatus() {

        ForEachLoop<Integer> loop = ForEachLoop.getInstance(array);

        Set<Integer> iterated = asSet();

        for (int i : loop) {
            @SuppressWarnings("rawtypes")
            Status status = loop.getStatus();

            assertEquals("current [" + i + "]", i, status.getCurrent());
            assertEquals("count [" + i + "]", i, status.getCount());
            assertEquals("index [" + i + "]", i - 1, status.getIndex());

            assertEquals("isFirst [" + i + "]", i == 1, status.isFirst());
            assertEquals("isLast [" + i + "]", i == 15, status.isLast());

            assertNull("begin [" + i + "]", status.getBegin());
            assertNull("end [" + i + "]", status.getEnd());
            assertNull("step [" + i + "]", status.getStep());

            iterated.add(i);
        }

        assertEquals("iterated", asSet(array), iterated);
    }

    @Test
    public void testCollectionIterationStatus() {

        ForEachLoop<Integer> loop = ForEachLoop.getInstance(new LinkedList<Integer>(Arrays.asList(array)));

        Set<Integer> iterated = asSet();

        for (int i : loop) {
            @SuppressWarnings("rawtypes")
            Status status = loop.getStatus();

            assertEquals("current [" + i + "]", i, status.getCurrent());
            assertEquals("count [" + i + "]", i, status.getCount());
            assertEquals("index [" + i + "]", i - 1, status.getIndex());

            assertEquals("isFirst [" + i + "]", i == 1, status.isFirst());
            assertEquals("isLast [" + i + "]", i == 15, status.isLast());

            assertNull("begin [" + i + "]", status.getBegin());
            assertNull("end [" + i + "]", status.getEnd());
            assertNull("step [" + i + "]", status.getStep());

            iterated.add(i);
        }

        assertEquals("iterated", asSet(array), iterated);
    }

    @Test
    public void testIteratorIterationStatus() {

        ForEachLoop<Integer> loop = ForEachLoop.getInstance(new LinkedList<Integer>(Arrays.asList(array)).iterator());

        Set<Integer> iterated = asSet();

        for (int i : loop) {
            @SuppressWarnings("rawtypes")
            Status status = loop.getStatus();

            assertEquals("current [" + i + "]", i, status.getCurrent());
            assertEquals("count [" + i + "]", i, status.getCount());
            assertEquals("index [" + i + "]", i - 1, status.getIndex());

            assertEquals("isFirst [" + i + "]", i == 1, status.isFirst());
            assertEquals("isLast [" + i + "]", i == 15, status.isLast());

            assertNull("begin [" + i + "]", status.getBegin());
            assertNull("end [" + i + "]", status.getEnd());
            assertNull("step [" + i + "]", status.getStep());

            iterated.add(i);
        }

        assertEquals("iterated", asSet(array), iterated);
    }

    @Test
    public void testBegin() {

        ForEachLoop<Integer> loop = ForEachLoop.getInstance(array);
        loop.setBegin(5);

        Set<Integer> iterated = asSet();

        for (int i : loop) {
            @SuppressWarnings("rawtypes")
            Status status = loop.getStatus();

            assertEquals("current [" + i + "]", i, status.getCurrent());
            assertEquals("count [" + i + "]", i - 5, status.getCount());
            assertEquals("index [" + i + "]", i - 1, status.getIndex());

            assertEquals("isFirst [" + i + "]", i == 6, status.isFirst());
            assertEquals("isLast [" + i + "]", i == 15, status.isLast());

            assertEquals("begin [" + i + "]", (Object) 5, status.getBegin());
            assertNull("end [" + i + "]", status.getEnd());
            assertNull("step [" + i + "]", status.getStep());

            iterated.add(i);
        }

        assertEquals("iterated", asSet(6, 7, 8, 9, 10, 11, 12, 13, 14, 15), iterated);
    }

    @Test
    public void testBeginTooHigh() {

        ForEachLoop<Integer> loop = ForEachLoop.getInstance(array);
        loop.setBegin(16);

        for (int i : loop) {
            fail("the loop should not be entered");
        }
    }

    @Test
    public void testEnd() {

        ForEachLoop<Integer> loop = ForEachLoop.getInstance(array);
        loop.setEnd(2);

        Set<Integer> iterated = asSet();

        for (int i : loop) {
            @SuppressWarnings("rawtypes")
            Status status = loop.getStatus();

            assertEquals("current [" + i + "]", i, status.getCurrent());
            assertEquals("count [" + i + "]", i, status.getCount());
            assertEquals("index [" + i + "]", i - 1, status.getIndex());

            assertEquals("isFirst [" + i + "]", i == 1, status.isFirst());
            assertEquals("isLast [" + i + "]", i == 3, status.isLast());

            assertNull("begin [" + i + "]", status.getBegin());
            assertEquals("end [" + i + "]", (Object) 2, status.getEnd());
            assertNull("step [" + i + "]", status.getStep());

            iterated.add(i);
        }

        assertEquals(asSet(1, 2, 3), iterated);
    }

    @Test
    public void testStep2() {

        ForEachLoop<Integer> loop = ForEachLoop.getInstance(array);
        loop.setStep(2);

        Set<Integer> iterated = asSet();

        for (int i : loop) {
            @SuppressWarnings("rawtypes")
            Status status = loop.getStatus();

            assertEquals("current [" + i + "]", i, status.getCurrent());
            assertEquals("count [" + i + "]", (i + 1) / 2, status.getCount());
            assertEquals("index [" + i + "]", i - 1, status.getIndex());

            assertEquals("isFirst [" + i + "]", i == 1, status.isFirst());
            assertEquals("isLast [" + i + "]", i == 15, status.isLast());

            assertNull("begin [" + i + "]", status.getBegin());
            assertNull("end [" + i + "]", status.getEnd());
            assertEquals("step [" + i + "]", (Object) 2, status.getStep());

            iterated.add(i);
        }

        assertEquals(asSet(1, 3, 5, 7, 9, 11, 13, 15), iterated);
    }

    @Test
    public void testStep5() {

        ForEachLoop<Integer> loop = ForEachLoop.getInstance(array);
        loop.setStep(5);

        Set<Integer> iterated = asSet();

        for (int i : loop) {
            @SuppressWarnings("rawtypes")
            Status status = loop.getStatus();

            assertEquals("current [" + i + "]", i, status.getCurrent());
            assertEquals("count [" + i + "]", (i / 5) + 1, status.getCount());
            assertEquals("index [" + i + "]", i - 1, status.getIndex());

            assertEquals("isFirst [" + i + "]", i == 1, status.isFirst());
            assertEquals("isLast [" + i + "]", i == 11, status.isLast());

            assertNull("begin [" + i + "]", status.getBegin());
            assertNull("end [" + i + "]", status.getEnd());
            assertEquals("step [" + i + "]", (Object) 5, status.getStep());

            iterated.add(i);
        }

        assertEquals(asSet(1, 6, 11), iterated);
    }

    @Test
    public void testBeginEndStep() {

        ForEachLoop<Integer> loop = ForEachLoop.getInstance(array);
        loop.setBegin(5);
        loop.setEnd(15);
        loop.setStep(5);

        Set<Integer> iterated = asSet();

        for (int i : loop) {
            @SuppressWarnings("rawtypes")
            Status status = loop.getStatus();

            assertEquals("current [" + i + "]", i, status.getCurrent());
            assertEquals("count [" + i + "]", (i / 5), status.getCount());
            assertEquals("index [" + i + "]", i - 1, status.getIndex());

            assertEquals("isFirst [" + i + "]", i == 6, status.isFirst());
            assertEquals("isLast [" + i + "]", i == 11, status.isLast());

            assertEquals("begin [" + i + "]", (Object) 5, status.getBegin());
            assertEquals("end [" + i + "]", (Object) 15, status.getEnd());
            assertEquals("step [" + i + "]", (Object) 5, status.getStep());

            iterated.add(i);
        }

        assertEquals(asSet(6, 11), iterated);
    }

    private Set<Integer> asSet(Integer... integers) {
        return new TreeSet<Integer>(Arrays.asList(integers));
    }

}
