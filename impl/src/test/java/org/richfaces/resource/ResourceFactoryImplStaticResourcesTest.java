package org.richfaces.resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingEnabled;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingFile;
import static org.richfaces.application.CoreConfiguration.Items.resourceMappingLocation;
import static org.richfaces.resource.ResourceMappingFeature.DEFAULT_LOCATION;

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

@RunWith(FacesMockitoRunner.class)
public class ResourceFactoryImplStaticResourcesTest extends AbstractResourceMappingTest {

    private static final String RESOURCE = "name";
    private static final String LIBRARY = "library";
    private static final String PATH = "path";

    @Mock
    ExpressionFactory expressionFactory;

    ResourceFactory resourceFactory;

    @Override
    public void setUp() {

        super.setUp();

        setupExpressionFactory();

        configure(resourceMappingEnabled, true);
        resourceFactory = new ResourceFactoryImpl(null);
    }

    @Test
    public void testDefaultMappingFile() {
        testResource(LIBRARY, RESOURCE, DEFAULT_LOCATION + PATH);
    }

    @Test
    public void testCustomMappingFile() {
        String packagePath = this.getClass().getPackage().getName().replace('.', '/');
        String customMappingFile = packagePath + "/mapping-test.properties";
        configure(resourceMappingFile, customMappingFile);

        testResource(LIBRARY, RESOURCE, DEFAULT_LOCATION + "customPath");
    }

    @Test
    public void testExternalHttpResource() {
        testResource(LIBRARY, "externalHttpResource", "http://some_resource");
    }

    @Test
    public void testExternalHttpsResource() {
        testResource(LIBRARY, "externalHttpsResource", "https://some_resource");
    }

    @Test
    public void testCustomLocation() {
        String customLocation = "customLocation";
        configure(resourceMappingLocation, customLocation);

        testResource(LIBRARY, RESOURCE, customLocation + PATH);
    }

    private void testResource(String library, String name, String expectedRequestPath) {
        Resource resource = resourceFactory.createResource(name, library, null);

        assertEquals(library, resource.getLibraryName());
        assertEquals(name, resource.getResourceName());
        assertEquals(expectedRequestPath, resource.getRequestPath());
    }

    private void setupExpressionFactory() {
        when(application.getExpressionFactory()).thenReturn(expressionFactory);

        when(
                expressionFactory.createValueExpression(Mockito.any(ELContext.class), Mockito.anyString(),
                        Mockito.any(Class.class))).thenAnswer(new Answer<ValueExpression>() {
            @Override
            public ValueExpression answer(InvocationOnMock invocation) throws Throwable {
                String resourceLocation = (String) requestMap.get(ExternalStaticResource.STATIC_RESOURCE_LOCATION_VARIABLE);
                final String expression = (String) invocation.getArguments()[1];
                ValueExpression valueExpression = mock(ValueExpression.class);
                when(valueExpression.getValue(elContext)).thenReturn(expression + resourceLocation);
                return valueExpression;
            }
        });
    }
}
