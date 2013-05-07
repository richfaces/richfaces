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

import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehaviorContext;

import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.services.ServiceTracker;
import org.richfaces.ui.validation.ValidatorRendererTestBase;

@RunWith(MockTestRunner.class)
public class RendererBuildScriptTest extends ValidatorRendererTestBase {
    private static final String FUNCTION_NAME = "inputValidate";
    private static final String SOURCE_ID = "clientValidator";
    @Mock
    private UIViewRoot viewRoot;
    @Mock
    private ComponentValidatorScript validatorScript;
    @Mock
    private JavaScriptService scriptService;

    @Before
    public void setUpResource() {
        expect(factory.getInstance(JavaScriptService.class)).andReturn(scriptService);
        ServiceTracker.setFactory(factory);
    }

    @After
    public void cleanUpResource() {
        ServiceTracker.release();
    }

    @Test
    public void buildAndStoreScript() throws Exception {
        ClientValidatorRenderer renderer = new ClientValidatorRenderer() {
            ComponentValidatorScript createValidatorScript(ClientBehaviorContext behaviorContext,
                ClientValidatorBehavior behavior) {
                return validatorScript;
            }

            ;
        };
        setupBehaviorContext(input);
        expect(behaviorContext.getSourceId()).andStubReturn(SOURCE_ID);
        expect(validatorScript.createCallScript(FUNCTION_NAME, SOURCE_ID)).andReturn(FUNCTION_NAME);
        expect(input.getClientId(environment.getFacesContext())).andReturn(FUNCTION_NAME);
        expect(scriptService.addScript(environment.getFacesContext(), validatorScript)).andReturn(validatorScript);
        controller.replay();
        assertEquals(FUNCTION_NAME, renderer.buildAndStoreValidatorScript(behaviorContext, mockBehavior));
        controller.verify();
    }
}
