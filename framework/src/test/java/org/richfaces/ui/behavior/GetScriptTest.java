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

package org.richfaces.ui.behavior;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

import javax.faces.FacesException;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.render.RenderKit;

import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockTestRunner;
import org.jboss.test.faces.mock.Stub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.ui.ajax.ajax.AjaxBehavior;
import org.richfaces.ui.validation.validator.ClientValidatorRenderer;
import org.richfaces.ui.validation.validator.FormClientValidatorRenderer;

/**
 * <p class="changed_added_4_0">
 * This class tests client validator behavior. as it described at https://community.jboss.org/wiki/ClientSideValidation #
 * Server-side rendering algorithm
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@RunWith(MockTestRunner.class)
public class GetScriptTest extends BehaviorTestBase {
    private static final String CLIENT_VALIDATION_FUNCTION = "rf.csv.v";
    @Stub
    protected UIOutput output;
    @Mock
    protected UICommand command;

    /**
     * <p class="changed_added_4_0">
     * Server-side rendering algorithm .1
     * </p>
     */
    @Test(expected = FacesException.class)
    public void testGetScriptForIllegalComponent() {
        getScript(output);
    }

    /**
     * <p class="changed_added_4_0">
     * Delegate getScript call to ClientValidatorRenderer
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testGetScriptForInput() throws Exception {
        testGetScriptDelegate(ClientValidatorRenderer.RENDERER_TYPE, input);
    }

    /**
     * <p class="changed_added_4_0">
     * Server-side rendering algorithm .3
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testGetScriptForAction() throws Exception {
        testGetScriptDelegate(FormClientValidatorRenderer.RENDERER_TYPE, command);
    }

    @Test
    public void testGetAjaxScript() throws Exception {
        setupRenderer(AjaxBehavior.BEHAVIOR_ID);
        ClientBehaviorContext clientBehaviorContext = setupBehaviorContext(input);
        controller.replay();
        String script = behavior.getAjaxScript(clientBehaviorContext);
        controller.verify();
        assertEquals(CLIENT_VALIDATION_FUNCTION, script);
    }

    private void testGetScriptDelegate(String rendererType, UIComponent component) {
        setupRenderer(rendererType);
        String script = getScript(component);// ajax-only validator.
        assertEquals(CLIENT_VALIDATION_FUNCTION, script);
    }

    private void setupRenderer(String rendererType) {
        RenderKit renderKit = environment.getRenderKit();
        expect(renderKit.getClientBehaviorRenderer(rendererType)).andReturn(behaviorRenderer);
        expect(behaviorRenderer.getScript(behaviorContext, behavior)).andReturn(CLIENT_VALIDATION_FUNCTION);
    }

    private String getScript(UIComponent component) {
        ClientBehaviorContext clientBehaviorContext = setupBehaviorContext(component);
        controller.replay();
        String script = behavior.getScript(clientBehaviorContext);
        controller.verify();
        return script;
    }
}
