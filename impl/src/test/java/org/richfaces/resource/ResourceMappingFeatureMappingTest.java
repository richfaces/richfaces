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
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingCompressedStages;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingEnabled;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingFile;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingPackedStages;

import java.util.Arrays;
import java.util.List;

import javax.faces.application.ProjectStage;

import org.jboss.test.faces.mockito.runner.FacesMockitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
@RunWith(FacesMockitoRunner.class)
public class ResourceMappingFeatureMappingTest extends AbstractResourceMappingTest {

    private static final String DEFAULT = ResourceMappingConfiguration.DEFAULT_STATIC_RESOURCE_MAPPING_LOCATION;
    private static final String STATIC = "META-INF/richfaces/staticResourceMapping/Static.properties";
    private static final String PACKED = "META-INF/richfaces/staticResourceMapping/Packed.properties";
    private static final String COMPRESSED = "META-INF/richfaces/staticResourceMapping/Compressed.properties";
    private static final String PACKED_COMPRESSED = "META-INF/richfaces/staticResourceMapping/PackedCompressed.properties";

    @Test
    public void testDefaultMappingFilesWhenResourceMappingDisabled() {
        configure(resourceMappingEnabled, false);
        testMappingFile(null, DEFAULT);
    }

    @Test
    public void testDefaultMappingFileWhenResourceMappingEnabled() {
        configure(resourceMappingEnabled, true);
        testMappingFile(null, STATIC, DEFAULT);
    }

    @Test
    public void testCustomMappingFileWhenResourceMappingDisabled() {
        configure(resourceMappingEnabled, false);
        testMappingFile("some_path", DEFAULT, "some_path");
    }

    @Test
    public void testCustomMappingFileWhenResourceMappingEnabled() {
        configure(resourceMappingEnabled, true);
        testMappingFile("some_path", STATIC, DEFAULT, "some_path");
    }

    @Test
    public void testMultipleMappingFiles() {
        configure(resourceMappingEnabled, true);
        testMappingFile("some_path,another_path", STATIC, DEFAULT, "some_path", "another_path");
    }

    @Test
    public void testCustomStagesWhenResourceMappingDisabled() {
        configure(resourceMappingEnabled, false);
        when(application.getProjectStage()).thenReturn(ProjectStage.Development);

        configure(resourceMappingPackedStages, "All");
        configure(resourceMappingCompressedStages, "All");
        testMappingFile(null, DEFAULT);
    }

    @Test
    public void testCustomStagesWhenResourceMappingEnabled() {
        configure(resourceMappingEnabled, true);
        when(application.getProjectStage()).thenReturn(ProjectStage.Development);

        configure(resourceMappingPackedStages, "None");
        configure(resourceMappingCompressedStages, "None");
        testMappingFile(null, STATIC, DEFAULT);

        configure(resourceMappingPackedStages, "All");
        configure(resourceMappingCompressedStages, "All");
        testMappingFile(null, PACKED_COMPRESSED, DEFAULT);

        configure(resourceMappingPackedStages, "Development");
        configure(resourceMappingCompressedStages, "Production");
        testMappingFile(null, PACKED, DEFAULT);

        configure(resourceMappingPackedStages, "Production");
        configure(resourceMappingCompressedStages, "Development");
        testMappingFile(null, COMPRESSED, DEFAULT);

        configure(resourceMappingPackedStages, "Development,Production");
        configure(resourceMappingCompressedStages, "Production,Development");
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
