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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingCompressedStages;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingEnabled;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingFile;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingLocation;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingPackedStages;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.ProjectStage;

import org.jboss.test.faces.mockito.runner.FacesMockitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
@RunWith(FacesMockitoRunner.class)
public class ResourceMappingFeatureTest extends AbstractResourceMappingTest {

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenResourceMappingDisabled1() {
        ResourceMappingFeature.getLocation();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenResourceMappingDisabled2() {
        ResourceMappingFeature.getMappingFile();
    }

    @Test
    public void testDefaultMappingFile() {
        testMappingFile(null, "META-INF/richfaces/staticResourceMapping/Static.properties");
    }

    @Test
    public void testCustomMappingFile() {
        testMappingFile("some_path", "some_path");
    }

    @Test
    public void testDefaultLocation() {
        testLocation(null, ResourceMappingFeature.DEFAULT_LOCATION);
    }

    @Test
    public void testCustomLocation() {
        testLocation("some_expression", "some_expression");
    }

    @Test
    public void testCustomStages() {
        when(application.getProjectStage()).thenReturn(ProjectStage.Development);

        configure(resourceMappingPackedStages, "None");
        configure(resourceMappingCompressedStages, "None");
        testMappingFile(null, "META-INF/richfaces/staticResourceMapping/Static.properties");

        configure(resourceMappingPackedStages, "All");
        configure(resourceMappingCompressedStages, "All");
        testMappingFile(null, "META-INF/richfaces/staticResourceMapping/PackedCompressed.properties");

        configure(resourceMappingPackedStages, "Development");
        configure(resourceMappingCompressedStages, "Production");
        testMappingFile(null, "META-INF/richfaces/staticResourceMapping/Packed.properties");

        configure(resourceMappingPackedStages, "Production");
        configure(resourceMappingCompressedStages, "Development");
        testMappingFile(null, "META-INF/richfaces/staticResourceMapping/Compressed.properties");

        configure(resourceMappingPackedStages, "Development,Production");
        configure(resourceMappingCompressedStages, "Production,Development");
        testMappingFile(null, "META-INF/richfaces/staticResourceMapping/PackedCompressed.properties");
    }

    private void testMappingFile(String configuredValue, String expectedResolvedMappingFile) {
        // given
        configure(resourceMappingEnabled, true);
        configure(resourceMappingFile, configuredValue);

        // when
        String mappingFile = ResourceMappingFeature.getMappingFile();

        // then
        assertEquals(expectedResolvedMappingFile, mappingFile);
    }

    private void testLocation(String configuredValue, String expectedResolvedLocationExpression) {
        String expectedResolvedLocation = "evaluated_location";
        ExpressionFactory expressionFactory = mock(ExpressionFactory.class);
        ValueExpression valueExpression = mock(ValueExpression.class);

        // given
        configure(resourceMappingEnabled, true);
        configure(resourceMappingLocation, (String) expectedResolvedLocationExpression);
        when(application.getExpressionFactory()).thenReturn(expressionFactory);
        when(expressionFactory.createValueExpression(elContext, expectedResolvedLocationExpression, Object.class)).thenReturn(
                valueExpression);
        when(valueExpression.getValue(elContext)).thenReturn(expectedResolvedLocation);

        // when
        String location = ResourceMappingFeature.getLocation();

        // then
        assertEquals(expectedResolvedLocation, location);
    }
}
