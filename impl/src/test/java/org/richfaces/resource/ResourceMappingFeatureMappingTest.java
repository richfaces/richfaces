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
package org.richfaces.resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.richfaces.application.CoreConfiguration.Items.resourceOptimizationCompressionStages;
import static org.richfaces.application.CoreConfiguration.Items.resourceOptimizationEnabled;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingFile;
import static org.richfaces.application.CoreConfiguration.Items.resourceOptimizationPackagingStages;

import java.util.Arrays;
import java.util.List;

import javax.faces.application.ProjectStage;

import org.jboss.test.faces.mockito.runner.FacesMockitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
@RunWith(FacesMockitoRunner.class)
public class ResourceMappingFeatureMappingTest extends AbstractResourceMappingTest {

    private static final String DEFAULT = ResourceMappingConfiguration.DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION;
    private static final String STATIC = "META-INF/richfaces/staticResourceMapping/Static.properties";
    private static final String PACKED = "META-INF/richfaces/staticResourceMapping/Packed.properties";
    private static final String COMPRESSED = "META-INF/richfaces/staticResourceMapping/Compressed.properties";
    private static final String PACKED_COMPRESSED = "META-INF/richfaces/staticResourceMapping/PackedCompressed.properties";

    @Test
    public void testDefaultMappingFilesWhenResourceLoadingOptimizationDisabled() {
        configure(resourceOptimizationEnabled, false);
        testMappingFile(null, DEFAULT);
    }

    @Test
    public void testDefaultMappingFileWhenResourceLoadingOptimizationEnabled() {
        configure(resourceOptimizationEnabled, true);
        testMappingFile(null, STATIC, DEFAULT);
    }

    @Test
    public void testCustomMappingFileWhenResourceLoadingOptimizationDisabled() {
        configure(resourceOptimizationEnabled, false);
        testMappingFile("some_path", DEFAULT, "some_path");
    }

    @Test
    public void testCustomMappingFileWhenResourceLoadingOptimizationEnabled() {
        configure(resourceOptimizationEnabled, true);
        testMappingFile("some_path", STATIC, DEFAULT, "some_path");
    }

    @Test
    public void testMultipleMappingFiles() {
        configure(resourceOptimizationEnabled, true);
        testMappingFile("some_path,another_path", STATIC, DEFAULT, "some_path", "another_path");
    }

    @Test
    public void testCustomStagesWhenResourceLoadingOptimizationDisabled() {
        configure(resourceOptimizationEnabled, false);
        when(application.getProjectStage()).thenReturn(ProjectStage.Development);

        configure(resourceOptimizationPackagingStages, "All");
        configure(resourceOptimizationCompressionStages, "All");
        testMappingFile(null, DEFAULT);
    }

    @Test
    public void testCustomStagesWhenResourceLoadingOptimizationEnabled() {
        configure(resourceOptimizationEnabled, true);
        when(application.getProjectStage()).thenReturn(ProjectStage.Development);

        configure(resourceOptimizationPackagingStages, "None");
        configure(resourceOptimizationCompressionStages, "None");
        testMappingFile(null, STATIC, DEFAULT);

        configure(resourceOptimizationPackagingStages, "All");
        configure(resourceOptimizationCompressionStages, "All");
        testMappingFile(null, PACKED_COMPRESSED, DEFAULT);

        configure(resourceOptimizationPackagingStages, "Development");
        configure(resourceOptimizationCompressionStages, "Production");
        testMappingFile(null, PACKED, DEFAULT);

        configure(resourceOptimizationPackagingStages, "Production");
        configure(resourceOptimizationCompressionStages, "Development");
        testMappingFile(null, COMPRESSED, DEFAULT);

        configure(resourceOptimizationPackagingStages, "Development,Production");
        configure(resourceOptimizationCompressionStages, "Production,Development");
        testMappingFile(null, PACKED_COMPRESSED, DEFAULT);
    }

    private void testMappingFile(String configuredValue, String... expectedResolvedMappingFiles) {
        // given
        configure(resourceMappingFile, configuredValue);

        // when
        List<String> mappingFiles = ResourceMappingFeature.getMappingFiles();

        // then
        assertEquals(Arrays.asList(expectedResolvedMappingFiles), mappingFiles);
    }
}
