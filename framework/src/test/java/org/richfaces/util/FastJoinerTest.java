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
package org.richfaces.util;

import static org.junit.Assert.assertEquals;
import static org.richfaces.util.FastJoiner.on;

import org.junit.Test;

import com.google.common.base.Joiner;

/**
 * @author Nick Belaevski
 *
 */
public class FastJoinerTest {
    private static final int PERFORMANCE_TEST_STEPS = 100000;

    @Test
    public void testDotCharJoin() throws Exception {
        FastJoiner joiner = on('.');
        assertEquals("", joiner.join(null, null));

        assertEquals("test", joiner.join("test", null));
        assertEquals("test.", joiner.join("test", ""));

        assertEquals("another test", joiner.join(null, "another test"));
        assertEquals(".another test", joiner.join("", "another test"));

        assertEquals("join.them", joiner.join("join", "them"));
    }

    @Test
    public void testDotCharVarargsJoin() throws Exception {
        FastJoiner joiner = on('.');

        assertEquals("", joiner.join(null, null, null));

        assertEquals("test", joiner.join("test", null, null));
        assertEquals("test", joiner.join(null, "test", null));
        assertEquals("test", joiner.join(null, null, "test"));

        assertEquals("test..abc", joiner.join("test", "", "abc"));
        assertEquals("test..abc", joiner.join(null, "test", "", "abc"));

        assertEquals(".test.abc", joiner.join("", "test", "abc"));
        assertEquals(".test.abc", joiner.join(null, "", "test", "abc"));

        assertEquals("join.them.all", joiner.join("join", "them", "all"));
    }

    @Test
    public void testXYStringJoin() throws Exception {
        FastJoiner joiner = on("-xy+");

        assertEquals("", joiner.join(null, null));

        assertEquals("test", joiner.join("test", null));
        assertEquals("test-xy+", joiner.join("test", ""));

        assertEquals("another test", joiner.join(null, "another test"));
        assertEquals("-xy+another test", joiner.join("", "another test"));

        assertEquals("join-xy+them", joiner.join("join", "them"));
    }

    @Test
    public void testEmptyStringJoin() throws Exception {
        FastJoiner joiner = on("");

        assertEquals("", joiner.join(null, null));

        assertEquals("test", joiner.join("test", null));
        assertEquals("test", joiner.join("test", ""));

        assertEquals("another test", joiner.join(null, "another test"));
        assertEquals("another test", joiner.join("", "another test"));

        assertEquals("jointhem", joiner.join("join", "them"));
    }

    @Test
    public void testEmptyStringVarargsJoin() throws Exception {
        FastJoiner joiner = on("");

        assertEquals("", joiner.join(null, null, null));

        assertEquals("test", joiner.join(null, "test", null, null));
        assertEquals("test", joiner.join("test", null, null));
        assertEquals("test", joiner.join("test", "", null));
        assertEquals("test", joiner.join("", "test", null));
        assertEquals("test", joiner.join(null, "", "test", null));

        assertEquals("jointhemall", joiner.join("join", "them", "all"));
    }

    @Test
    public void testGuavaJoinerPerformance() throws Exception {
        Joiner joiner = Joiner.on("-separator-").skipNulls();

        for (int i = 0; i < PERFORMANCE_TEST_STEPS; i++) {
            joiner.join("big:table:id:string", "cell:id");
        }
    }

    @Test
    public void testFastJoinerPerformance() throws Exception {
        FastJoiner joiner = FastJoiner.on("-separator-");

        for (int i = 0; i < PERFORMANCE_TEST_STEPS; i++) {
            joiner.join("big:table:id:string", "cell:id");
        }
    }
}
