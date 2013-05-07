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

package org.richfaces.ui.validation.validator;

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
import org.richfaces.ui.validation.ValidatorRendererTestBase;
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
