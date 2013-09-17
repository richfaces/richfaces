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
import static org.richfaces.configuration.CoreConfiguration.Items.resourceMappingFile;
import static org.richfaces.configuration.CoreConfiguration.Items.resourceMappingLocation;
import static org.richfaces.configuration.CoreConfiguration.Items.resourceOptimizationEnabled;
import static org.richfaces.configuration.CoreConfiguration.Items.staticResourceLocation;

import org.jboss.test.faces.mockito.runner.FacesMockitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.resource.mapping.ResourceLoadingOptimization;
import org.richfaces.resource.mapping.ResourceMappingConfiguration;

/**
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
@SuppressWarnings("deprecation")
@RunWith(FacesMockitoRunner.class)
public class ResourceMappingConfigurationTest extends AbstractResourceMappingTest {

    @Test
    public void testMappingDisabled() {
        // when
        configure(resourceOptimizationEnabled, (Boolean) false);

        // then
        boolean enabled = ResourceLoadingOptimization.isEnabled();

        // verify
        assertFalse(enabled);
    }

    @Test
    public void testMappingEnabled() {
        // when
        configure(resourceOptimizationEnabled, true);

        // then
        boolean enabled = ResourceLoadingOptimization.isEnabled();

        // verify
        assertTrue(enabled);
    }

    @Test
    public void testLocationIsNull() {
        // when
        configure(staticResourceLocation, (String) null);
        configure(resourceMappingLocation, (String) null);

        // then
        String location = ResourceMappingConfiguration.getLocation();

        // verify
        assertNull(location);
    }

    @Test
    public void testLocationConfiguredUsingStaticResourceLocation() {
        // when
        String expected = "some_location";
        configure(staticResourceLocation, expected);
        configure(resourceMappingLocation, (String) null);

        // then
        String location = ResourceMappingConfiguration.getLocation();

        // verify
        assertEquals(expected, location);
    }

    @Test
    public void testLocationConfiguredWithResourceMappingLocationSwitch() {
        // when
        String expected = "some_location";
        configure(staticResourceLocation, (String) null);
        configure(resourceMappingLocation, expected);

        // then
        String location = ResourceMappingConfiguration.getLocation();

        // verify
        assertEquals(expected, location);
    }

    @Test
    public void testLocationStaticResourceLocationHasPrecedence() {
        // when
        String expected = "some_location";
        String wrong = "fail";
        configure(staticResourceLocation, expected);
        configure(resourceMappingLocation, wrong);

        // then
        String location = ResourceMappingConfiguration.getLocation();

        // verify
        assertEquals(expected, location);
    }

    @Test
    public void testMappingFileIsNull() {
        // when
        configure(staticResourceLocation, (String) null);
        configure(resourceMappingFile, (String) null);

        // then
        String mappingFile = ResourceMappingConfiguration.getResourceMappingFile();

        // verify
        assertNull(mappingFile);
    }

    @Test
    public void testMappingFileConfiguredUsingStaticResourceLocation() {
        // when
        configure(staticResourceLocation, "some_location");
        configure(resourceMappingFile, (String) null);

        // then
        String mappingFile = ResourceMappingConfiguration.getResourceMappingFile();

        // verify
        assertEquals(ResourceMappingConfiguration.DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION, mappingFile);
    }

    @Test
    public void testMappingFileConfiguredWithResourceMappingFileSwitch() {
        // when
        String expected = "some_file";
        configure(staticResourceLocation, (String) null);
        configure(resourceMappingFile, expected);

        // then
        String mappingFile = ResourceMappingConfiguration.getResourceMappingFile();

        // verify
        assertEquals(expected, mappingFile);
    }

    @Test
    public void testMappingFileStaticResourceLocationHasPrecedence() {
        // when
        String wrong = "some_file";
        configure(staticResourceLocation, "some_location");
        configure(resourceMappingFile, wrong);

        // then
        String mappingFile = ResourceMappingConfiguration.getResourceMappingFile();

        // verify
        assertEquals(ResourceMappingConfiguration.DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION, mappingFile);
    }
}
