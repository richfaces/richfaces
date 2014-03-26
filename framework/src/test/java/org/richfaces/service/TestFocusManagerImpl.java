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

package org.richfaces.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.ajax4jsf.javascript.JSLiteral;
import org.jboss.test.faces.mockito.runner.FacesMockitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.richfaces.ui.misc.focus.FocusManager;
import org.richfaces.ui.misc.focus.FocusManagerImpl;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.ui.misc.focus.FocusRendererUtils;
import org.richfaces.services.ServicesFactory;
import org.richfaces.test.AbstractServicesTest;

@RunWith(FacesMockitoRunner.class)
public class TestFocusManagerImpl extends AbstractServicesTest {

    @Inject
    FacesContext facesContext;

    @Mock
    UIComponent viewRoot;

    @Mock
    UIComponent component;

    @Spy
    Map<Object, Object> attributes = new HashMap<Object, Object>();

    @Mock
    JavaScriptService javaScriptService;

    @Before
    public void setUp() {
        when(facesContext.getAttributes()).thenReturn(attributes);

        viewRoot.pushComponentToEL(facesContext, viewRoot);

    }

    @Test
    public void test() {
        // having
        FocusManager focusManager = new FocusManagerImpl();

        // when
        focusManager.focus(null);

        // then
        verify(attributes).put(FocusManager.FOCUS_CONTEXT_ATTRIBUTE, null);
        assertFalse(FocusRendererUtils.isFocusEnforced(facesContext));

        verifyZeroInteractions(javaScriptService);
    }

    @Test
    public void test2() {
        // having
        FocusManager focusManager = new FocusManagerImpl();
        String componentId = "someComponentId";
        String clientId = "someClientId";

        when(component.getClientId(facesContext)).thenReturn(clientId);
        when(viewRoot.findComponent(componentId)).thenReturn(component);

        // when
        focusManager.focus(componentId);

        // then
        // verify(attributes).put(FocusManager.FOCUS_CONTEXT_ATTRIBUTE, clientId);
        assertEquals(clientId, attributes.get(FocusManager.FOCUS_CONTEXT_ATTRIBUTE));
        assertTrue(attributes.containsKey(FocusManager.FOCUS_CONTEXT_ATTRIBUTE));
        assertTrue(FocusRendererUtils.isFocusEnforced(facesContext));

        verify(javaScriptService).addPageReadyScript(Mockito.any(FacesContext.class), Mockito.any(JSLiteral.class));
    }

    @Override
    protected void configureServices(ServicesFactory injector) {
        injector.setInstance(JavaScriptService.class, javaScriptService);
    }
}
