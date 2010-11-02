/**
 * 
 */
package org.richfaces.validator;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.validation.constraints.Max;

import org.jboss.test.faces.mock.Environment;
import org.jboss.test.faces.mock.Environment.Feature;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockController;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.ImmutableMap;

/**
 * @author asmirnov
 * 
 */
@RunWith(MockTestRunner.class)
public class ClientScriptServiceTest {

    private static final String TEXT_JAVASCRIPT = "text/javascript";

    private static final String ORG_RICHFACES_CSV = "org.richfaces.csv";

    private static final String RESOURCE_NAME = ValidatorWithFacesResource.class.getSimpleName() + ".js";

    @Mock
    @Environment({ Feature.APPLICATION })
    private MockFacesEnvironment environment;

    @Mock
    private ResourceHandler resourceHandler;

    private MockController controller;

    @Mock
    private Resource resource;

    @Mock
    private LibraryFunction function;

    private ClientScriptServiceImpl serviceImpl;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        expect(environment.getApplication().getResourceHandler()).andStubReturn(resourceHandler);
        Map<Class<?>, LibraryFunction> defaultMapping = ImmutableMap
            .<Class<?>, LibraryFunction> of(Max.class, function);
        serviceImpl = new ClientScriptServiceImpl(defaultMapping);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        controller.verify();
    }

    /**
     * Test method for {@link org.richfaces.validator.ClientScriptServiceImpl#getScript(FacesContext, java.lang.Class)}.
     * 
     * @throws Exception
     */
    @Test
    public void testGetScriptAsJsfResource() throws Exception {
        LibraryFunction script = getScript(resource, ValidatorWithFacesResource.class);
        assertEquals(RESOURCE_NAME, script.getResource().getResourceName());
        assertEquals(ORG_RICHFACES_CSV, script.getResource().getLibrary());
        assertEquals("validatorWithFacesResource", script.getName());
    }

    @Test
    public void testGetScriptFromAnnotation() throws Exception {
        LibraryFunction script = getScript(null, ValidatorWithFacesResource.class);
        assertEquals("baz.js", script.getResource().getResourceName());
        assertEquals("bar", script.getResource().getLibrary());
        assertEquals("foo", script.getName());
    }

    @Test
    public void testGetScriptFromDefaultMapping() throws Exception {
        LibraryFunction script = getScript(null, Max.class);
        assertSame(function, script);
    }

    @Test
    public void testGetScriptOverrideAnnotation() throws Exception {
        Map<Class<?>, LibraryFunction> defaultMapping = ImmutableMap.<Class<?>, LibraryFunction> of(
            ValidatorWithFacesResource.class, function);
        serviceImpl = new ClientScriptServiceImpl(defaultMapping);
        LibraryFunction script = getScript(null, ValidatorWithFacesResource.class);
        assertSame(function, script);
    }

    private LibraryFunction getScript(Resource resource, Class<?> serverSideType) throws ScriptNotFoundException {
        expect(resourceHandler.createResource(serverSideType.getSimpleName() + ".js", ORG_RICHFACES_CSV, TEXT_JAVASCRIPT)).andReturn(resource);
        controller.replay();
        LibraryFunction script = serviceImpl.getScript(environment.getFacesContext(), serverSideType);
        return script;
    }

}
