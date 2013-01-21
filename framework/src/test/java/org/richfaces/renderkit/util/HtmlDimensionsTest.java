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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author amarkhel
 *
 */
public class HtmlDimensionsTest {
    @Test
    public void testFormatSize() {
        assertEquals("100px", HtmlDimensions.formatSize("100"));
        assertEquals("100px", HtmlDimensions.formatSize("100  "));
        assertEquals("100px", HtmlDimensions.formatSize("   100"));
        assertEquals("100px", HtmlDimensions.formatSize("   100   "));
        assertEquals("t100px", HtmlDimensions.formatSize("t100"));
        assertEquals("r100px", HtmlDimensions.formatSize("r100  "));
        assertEquals("100px  ", HtmlDimensions.formatSize("100px  "));
        assertEquals("  100px", HtmlDimensions.formatSize("  100px"));
        assertEquals("  100px ", HtmlDimensions.formatSize("  100px "));
        assertEquals("100px", HtmlDimensions.formatSize("100px"));
        assertEquals("100 px", HtmlDimensions.formatSize("100 px"));
        assertEquals("99%", HtmlDimensions.formatSize("99%"));
        assertEquals("99 %", HtmlDimensions.formatSize("99 %"));
        assertEquals("100em", HtmlDimensions.formatSize("100em"));
        assertEquals("size", HtmlDimensions.formatSize("size"));
        assertEquals("", HtmlDimensions.formatSize(""));
        assertEquals("   ", HtmlDimensions.formatSize("   "));
        assertEquals(null, HtmlDimensions.formatSize(null));
    }
}
