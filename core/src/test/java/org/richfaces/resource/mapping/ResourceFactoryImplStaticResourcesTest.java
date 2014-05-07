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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingFile;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingLocation;
import static org.richfaces.application.CoreConfiguration.Items.resourceOptimizationEnabled;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Resource;

import org.jboss.test.faces.mockito.runner.FacesMockitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.richfaces.resource.ResourceFactory;
import org.richfaces.resource.ResourceFactoryImpl;

/**
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
@RunWith(FacesMockitoRunner.class)
public class ResourceFactoryImplStaticResourcesTest extends AbstractResourceMappingTest {

    private static final String RESOURCE = "name";
    private static final String LIBRARY = "library";

    @Mock
    ExpressionFactory expressionFactory;

    private String configuredLocation;

    private Resource resourceInDefaultMapping = resource(LIBRARY, RESOURCE, location("defaultMappingFilePath"));
    private Resource resourceInFirstCustomMapping = resource(LIBRARY, RESOURCE, location("firstCustomMappingFilePath"));
    private Resource resourceInSecondCustomMapping = resource(LIBRARY, RESOURCE, location("secondCustomMappingFilePath"));

    private Resource onlyInDefaultMappingFile = resource(LIBRARY, "onlyInDefaultMappingFile",
            location("onlyInDefaultMappingFilePath"));
    private Resource onlyInFeatureSpecificMappingFile = resource(LIBRARY, "onlyInFeatureSpecificMappingFile",
            location("onlyInFeatureSpecificMappingFilePath"));
    private Resource onlyInFirstCustomMappingFile = resource(LIBRARY, "onlyInFirstCustomMappingFile",
            location("onlyInFirstCustomMappingFilePath"));
    private Resource onlyInSecondCustomMappingFile = resource(LIBRARY, "onlyInSecondCustomMappingFile",
            location("onlyInSecondCustomMappingFilePath"));

    private Resource externalHttpResource = resource(LIBRARY, "externalHttpResource", externalLocation("http://some_resource"));
    private Resource externalHttpsResource = resource(LIBRARY, "externalHttpsResource",
            externalLocation("https://some_resource"));

    private Resource noLibraryResource = resource(null, "noLibraryResource", location("noLibraryResourcePath"));

    @Override
    public void setUp() {

        super.setUp();

        setupExpressionFactory();
        configuredLocation = ResourceMappingConfiguration.DEFAULT_LOCATION;
    }

    @Test
    public void testDefaultMappingFile() {
        configure(resourceOptimizationEnabled, false);

        verifyResourcesPresent(resourceInDefaultMapping);
        verifyResourcesInDefaultMappingFilePresent();
        verifyResourceNotPresent(onlyInFeatureSpecificMappingFile, onlyInFirstCustomMappingFile, onlyInSecondCustomMappingFile);
    }

    @Test
    public void testFeatureSpecificMappingFile() {
        configure(resourceOptimizationEnabled, true);

        verifyResourcesPresent(resourceInDefaultMapping, onlyInFeatureSpecificMappingFile);
        verifyResourcesInDefaultMappingFilePresent();
        verifyResourceNotPresent(onlyInFirstCustomMappingFile, onlyInSecondCustomMappingFile);
    }

    @Test
    public void testFirstCustomMappingFile() {
        configure(resourceOptimizationEnabled, true);
        configureCustomMappingFiles("mapping-test1.properties");

        verifyResourcesPresent(resourceInFirstCustomMapping, onlyInFeatureSpecificMappingFile, onlyInFirstCustomMappingFile);
        verifyResourcesInDefaultMappingFilePresent();
        verifyResourceNotPresent(onlyInSecondCustomMappingFile);
    }

    @Test
    public void testMultipleCustomMappingFile() {
        configure(resourceOptimizationEnabled, true);
        configureCustomMappingFiles("mapping-test1.properties", "mapping-test2.properties");

        verifyResourcesPresent(resourceInSecondCustomMapping, onlyInFeatureSpecificMappingFile, onlyInFirstCustomMappingFile,
                onlyInSecondCustomMappingFile);
        verifyResourcesInDefaultMappingFilePresent();
    }

    @Test
    public void testCustomLocation() {
        configuredLocation = "customLocation";
        configure(resourceMappingLocation, configuredLocation);

        testFeatureSpecificMappingFile();
    }

    private void verifyResourcesPresent(Resource... expectedResources) {
        for (Resource expectedResource : expectedResources) {
            ResourceFactory resourceFactory = new ResourceFactoryImpl(null);
            Resource resource = resourceFactory.createResource(expectedResource.getResourceName(),
                    expectedResource.getLibraryName(), null);

            assertNotNull(
                    "resource is not present: " + expectedResource.getLibraryName() + ":" + expectedResource.getResourceName(),
                    resource);
            assertThat(resource.getRequestPath(), equalTo(expectedResource.getRequestPath()));
        }
    }

    private void verifyResourceNotPresent(Resource... nonPresentResources) {
        for (Resource notPresentResource : nonPresentResources) {
            ResourceFactory resourceFactory = new ResourceFactoryImpl(null);
            Resource resource = resourceFactory.createResource(notPresentResource.getResourceName(),
                    notPresentResource.getLibraryName(), null);

            assertNull(resource);
        }
    }

    private void verifyResourcesInDefaultMappingFilePresent() {
        verifyResourcesPresent(onlyInDefaultMappingFile, externalHttpResource, externalHttpsResource, noLibraryResource);
    }

    private void configureCustomMappingFiles(String... customMappingFiles) {
        String packagePath = this.getClass().getPackage().getName().replace('.', '/');
        StringBuffer mappingFiles = new StringBuffer();
        for (String mappingFile : customMappingFiles) {
            if (mappingFiles.length() > 0) {
                mappingFiles.append(',');
            }
            mappingFiles.append(packagePath);
            mappingFiles.append('/');
            mappingFiles.append(mappingFile);
        }
        configure(resourceMappingFile, mappingFiles.toString());
    }

    private interface Location {
        String getRequestPath();
    }

    private Resource resource(String library, String name, final Location location) {
        Resource resource = Mockito.mock(Resource.class);
        when(resource.getLibraryName()).thenReturn(library);
        when(resource.getResourceName()).thenReturn(name);
        when(resource.getRequestPath()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return location.getRequestPath();
            }
        });
        return resource;
    }

    private Location location(final String location) {
        return new Location() {
            @Override
            public String getRequestPath() {
                return configuredLocation + location;
            }
        };
    }

    private Location externalLocation(final String location) {
        return new Location() {
            @Override
            public String getRequestPath() {
                return location;
            }
        };
    }

    private void setupExpressionFactory() {
        when(application.getExpressionFactory()).thenReturn(expressionFactory);

        when(
                expressionFactory.createValueExpression(Mockito.any(ELContext.class), Mockito.anyString(),
                        Mockito.any(Class.class))).thenAnswer(new Answer<ValueExpression>() {
            @Override
            public ValueExpression answer(InvocationOnMock invocation) throws Throwable {
                final String expression = (String) invocation.getArguments()[1];
                ValueExpression valueExpression = mock(ValueExpression.class);
                when(valueExpression.getValue(elContext)).thenReturn(expression);
                return valueExpression;
            }
        });
    }
}
