package org.richfaces.renderkit.html;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.convert.NumberConverter;
import javax.faces.validator.RegexValidator;

import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.javascript.ClientScriptService;
import org.richfaces.javascript.LibraryFunction;
import org.richfaces.javascript.ScriptNotFoundException;
import org.richfaces.services.ServiceTracker;
import org.richfaces.validator.ConverterDescriptor;
import org.richfaces.validator.ValidatorDescriptor;

import com.google.common.collect.Iterables;

@RunWith(MockTestRunner.class)
public class RendererGetClientSideScriptTest extends ValidatorRendererTestBase {
    @Mock
    private ValidatorDescriptor descriptor;
    @Mock
    private ConverterDescriptor converterDescriptor;
    @Mock
    private ClientScriptService scriptService;
    @Mock
    private LibraryFunction script;
    private Collection<ValidatorDescriptor> descriptors;

    @Before
    public void setupService() {
        expect(factory.getInstance(ClientScriptService.class)).andReturn(scriptService);
        ServiceTracker.setFactory(factory);
        descriptors = Collections.singleton(descriptor);
    }

    @After
    public void releaseService() {
        ServiceTracker.release();
    }

    @Test()
    public void testGetClientSideScriptNotExists() throws Throwable {
        expect((Class) descriptor.getImplementationClass()).andReturn(RegexValidator.class);
        expect(scriptService.getScript(environment.getFacesContext(), RegexValidator.class)).andThrow(
            new ScriptNotFoundException());

        controller.replay();
        Collection<? extends LibraryScriptFunction> clientSideValidatorScript = renderer.getClientSideValidatorScript(
            environment.getFacesContext(), descriptors);
        assertTrue(clientSideValidatorScript.isEmpty());
        controller.verify();
    }

    @Test(expected = ScriptNotFoundException.class)
    public void testGetClientSideConverterScriptNotExists() throws Throwable {
        expect((Class) converterDescriptor.getImplementationClass()).andReturn(NumberConverter.class);
        expect(scriptService.getScript(environment.getFacesContext(), NumberConverter.class)).andThrow(
            new ScriptNotFoundException());
        controller.replay();
        renderer.getClientSideConverterScript(environment.getFacesContext(), converterDescriptor);
        controller.verify();
    }

    @Test
    public void testGetClientSideValidatorScriptFromDescription() throws Exception {
        expect((Class) descriptor.getImplementationClass()).andReturn(RegexValidator.class);
        expect(descriptor.getMessage()).andReturn(VALIDATOR_MESSAGE);
        expect((Map<String, Object>) descriptor.getAdditionalParameters()).andReturn((Map<String, Object>) VALIDATOR_PARAMS);
        expect(scriptService.getScript(environment.getFacesContext(), RegexValidator.class)).andReturn(script);
        expect(script.getName()).andReturn(REGEX_VALIDATOR).atLeastOnce();
        expect(script.getResources()).andReturn(CLIENT_VALIDATOR_LIBRARY);
        controller.replay();
        Collection<? extends LibraryScriptFunction> clientSideScripts = renderer.getClientSideValidatorScript(
            environment.getFacesContext(), descriptors);
        LibraryScriptFunction clientSideScript = (LibraryScriptFunction) Iterables.getOnlyElement(clientSideScripts);
        assertEquals(VALIDATOR_MESSAGE, clientSideScript.getMessage());
        assertEquals(VALIDATOR_PARAMS, clientSideScript.getParameters());
        assertEquals(CLIENT_VALIDATOR_LIBRARY, clientSideScript.getResources());
        controller.verify();
    }

    @Test
    public void testGetClientSideConverterScriptFromDescription() throws Exception {
        expect((Class) converterDescriptor.getImplementationClass()).andReturn(NumberConverter.class);
        expect(converterDescriptor.getMessage()).andReturn(VALIDATOR_MESSAGE);
        expect((Map<String, Object>) converterDescriptor.getAdditionalParameters()).andReturn(
            (Map<String, Object>) VALIDATOR_PARAMS);
        expect(scriptService.getScript(environment.getFacesContext(), NumberConverter.class)).andReturn(script);
        expect(script.getName()).andReturn(REGEX_VALIDATOR).atLeastOnce();
        expect(script.getResources()).andReturn(CLIENT_VALIDATOR_LIBRARY);
        controller.replay();
        LibraryScriptFunction clientSideScript = (LibraryScriptFunction) renderer.getClientSideConverterScript(
            environment.getFacesContext(), converterDescriptor);
        assertEquals(VALIDATOR_MESSAGE, clientSideScript.getMessage());
        assertEquals(VALIDATOR_PARAMS, clientSideScript.getParameters());
        assertEquals(CLIENT_VALIDATOR_LIBRARY, clientSideScript.getResources());
        controller.verify();
    }
}
