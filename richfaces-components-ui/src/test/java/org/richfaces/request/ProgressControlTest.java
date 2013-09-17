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
package org.richfaces.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski
 *
 */
public class ProgressControlTest {
    private static final String UPLOAD_ID = "testId";
    private static final String ATTRIBUTE_NAME = ProgressControl.getContextAttributeName(UPLOAD_ID);

    @Test
    public void testAdvance() throws Exception {
        Map<String, Object> contextMap = Maps.newHashMap();
        ProgressControl control = new ProgressControl(UPLOAD_ID, 400);
        control.setContextMap(contextMap);

        assertNull(contextMap.get(ATTRIBUTE_NAME));
        control.advance(1);
        assertNull(contextMap.get(ATTRIBUTE_NAME));

        control.advance(3);
        assertEquals((byte) 1, contextMap.get(ATTRIBUTE_NAME));

        control.advance(196);
        assertEquals((byte) 50, contextMap.get(ATTRIBUTE_NAME));

        control.advance(200);
        assertEquals((byte) 100, contextMap.get(ATTRIBUTE_NAME));
    }

    @Test
    public void testClearProgress() throws Exception {
        Map<String, Object> contextMap = Maps.newHashMap();
        ProgressControl control = new ProgressControl(UPLOAD_ID, 100);
        control.setContextMap(contextMap);

        assertNull(contextMap.get(ATTRIBUTE_NAME));
        control.advance(50);
        assertNotNull(contextMap.get(ATTRIBUTE_NAME));
        control.clearProgress();
        assertNull(contextMap.get(ATTRIBUTE_NAME));
    }
}
