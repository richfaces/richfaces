/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

package org.richfaces.javascript;

import com.google.common.collect.Iterables;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.richfaces.resource.ResourceKey;

import javax.faces.FacesException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServiceConfigParserTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParseConfig() {
        Map<Class<?>, LibraryFunction> parseConfig = ClientServiceConfigParser.parseConfig("csv.xml");
        assertEquals(2, parseConfig.size());
        assertTrue(parseConfig.containsKey(String.class));
        LibraryFunction libraryFunction = parseConfig.get(String.class);
        assertEquals("stringConverter", libraryFunction.getName());
        ResourceKey resource = Iterables.getOnlyElement(libraryFunction.getResources());
        assertEquals("csv.js", resource.getResourceName());
        assertEquals("org.richfaces", resource.getLibraryName());
    }

    @Test(expected = FacesException.class)
    @Ignore("parser log errors instead of exception")
    public void testParseBadConfig() {
        Map<Class<?>, LibraryFunction> parseConfig = ClientServiceConfigParser.parseConfig("badcsv.xml");
    }

    @Test()
    public void testParseNoConfig() {
        Map<Class<?>, LibraryFunction> parseConfig = ClientServiceConfigParser.parseConfig("non-exists-csv.xml");
        assertEquals(0, parseConfig.size());
    }
}
