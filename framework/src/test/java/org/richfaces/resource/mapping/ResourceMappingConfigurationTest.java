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
package org.richfaces.resource.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingFile;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingLocation;
import static org.richfaces.application.CoreConfiguration.Items.resourceOptimizationEnabled;

import java.util.Arrays;
import java.util.List;

import org.jboss.test.faces.mockito.runner.FacesMockitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
@RunWith(FacesMockitoRunner.class)
public class ResourceMappingConfigurationTest extends AbstractResourceMappingTest {

    @Test
    public void testMappingDisabled() {
        // when
        configure(resourceOptimizationEnabled, (Boolean) false);

        // then
        boolean enabled = ResourceLoadingOptimizationConfiguration.isEnabled();

        // verify
        assertFalse(enabled);
    }

    @Test
    public void testMappingEnabled() {
        // when
        configure(resourceOptimizationEnabled, true);

        // then
        boolean enabled = ResourceLoadingOptimizationConfiguration.isEnabled();

        // verify
        assertTrue(enabled);
    }

    @Test
    public void testLocationIsNull() {
        // when
        configure(resourceMappingLocation, (String) null);

        // then
        String location = PropertiesMappingConfiguration.getLocation();

        // verify
        assertNull(location);
    }

    @Test
    public void testLocationConfiguredWithResourceMappingLocationSwitch() {
        // when
        String expected = "some_location";
        configure(resourceMappingLocation, expected);

        // then
        String location = PropertiesMappingConfiguration.getLocation();

        // verify
        assertEquals(expected, location);
    }

    @Test
    public void testMappingFileIsNull() {
        // when
        configure(resourceMappingFile, (String) null);

        // then
        List<String> mappingFiles = PropertiesMappingConfiguration.getMappingFiles();

        // verify
        assertEquals(Arrays.asList(PropertiesMappingConfiguration.DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION), mappingFiles);
    }

    @Test
    public void testMappingFileConfiguredUsingStaticResourceLocation() {
        // when
        configure(resourceMappingFile, (String) null);

        // then
        List<String> mappingFiles = PropertiesMappingConfiguration.getMappingFiles();

        // verify
        assertEquals(Arrays.asList(PropertiesMappingConfiguration.DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION), mappingFiles);
    }

    @Test
    public void testMappingFileConfiguredWithResourceMappingFileSwitch() {
        // when
        String expected = "some_file";
        configure(resourceMappingFile, expected);

        // then
        List<String> mappingFiles = PropertiesMappingConfiguration.getMappingFiles();

        // verify
        assertEquals(Arrays.asList(PropertiesMappingConfiguration.DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION, expected), mappingFiles);
    }
}
