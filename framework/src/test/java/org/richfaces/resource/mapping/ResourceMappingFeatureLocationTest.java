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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.richfaces.configuration.CoreConfiguration.Items.resourceMappingLocation;
import static org.richfaces.configuration.CoreConfiguration.Items.resourceOptimizationEnabled;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import org.jboss.test.faces.mockito.runner.FacesMockitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.services.ServiceTracker;

/**
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
@RunWith(FacesMockitoRunner.class)
public class ResourceMappingFeatureLocationTest extends AbstractResourceMappingTest {

    @Test
    public void shouldThrowExceptionWhenResourceLoadingOptimizationDisabled() {
        configure(resourceOptimizationEnabled, false);
        testLocation(null, ResourceMappingConfiguration.DEFAULT_LOCATION);
    }

    @Test
    public void testDefaultLocationWhenResourceLoadingOptimizationEnabled() {
        configure(resourceOptimizationEnabled, true);
        testLocation(null, ResourceMappingConfiguration.DEFAULT_LOCATION);
    }

    @Test
    public void testCustomLocationWhenResourceLoadingOptimizationDisabled() {
        configure(resourceOptimizationEnabled, false);
        testLocation("some_expression", "some_expression");
    }

    @Test
    public void testCustomLocationWhenResourceLoadingOptimizationEnabled() {
        configure(resourceOptimizationEnabled, true);
        testLocation("some_expression", "some_expression");
    }

    private void testLocation(String configuredValue, String expectedResolvedLocationExpression) {
        String expectedResolvedLocation = "evaluated_location";
        ExpressionFactory expressionFactory = mock(ExpressionFactory.class);
        ValueExpression valueExpression = mock(ValueExpression.class);

        // given
        configure(resourceMappingLocation, (String) expectedResolvedLocationExpression);
        when(application.getExpressionFactory()).thenReturn(expressionFactory);
        when(expressionFactory.createValueExpression(elContext, expectedResolvedLocationExpression, Object.class)).thenReturn(
                valueExpression);
        when(valueExpression.getValue(elContext)).thenReturn(expectedResolvedLocation);

        // when
        String location = ServiceTracker.getService(ResourceMappingConfiguration.class).getLocation();

        // then
        assertEquals(expectedResolvedLocation, location);
    }
}
