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
package org.richfaces.ui.util.renderkit;

import org.jboss.test.faces.mockito.runner.FacesMockitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.util.RendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Nick Belaevski
 * @since 3.3.2
 */
@RunWith(FacesMockitoRunner.class)
public class RendererUtilsTest {

    private RendererUtils utils = RendererUtils.getInstance();

    @Inject
    private FacesContext facesContext;

    @Inject
    private ExternalContext externalContext;

    @Test
    public void testIsEmpty() throws Exception {
        assertTrue(utils.isEmpty(null));
        assertFalse(utils.isEmpty(new Object()));
        assertFalse(utils.isEmpty(Boolean.FALSE));
        assertFalse(utils.isEmpty(Long.valueOf(0)));
        assertFalse(utils.isEmpty(Integer.valueOf(0)));
        assertFalse(utils.isEmpty(Short.valueOf((short) 0)));
        assertFalse(utils.isEmpty(Byte.valueOf((byte) 0)));
        assertTrue(utils.isEmpty(""));
        assertFalse(utils.isEmpty("s"));
        assertTrue(utils.isEmpty(new ArrayList<Object>()));
        assertTrue(utils.isEmpty(Collections.EMPTY_LIST));

        List<Object> testList = new ArrayList<Object>();

        testList.add("x");
        assertFalse(utils.isEmpty(testList));
        assertTrue(utils.isEmpty(new HashMap<String, Object>()));
        assertTrue(utils.isEmpty(Collections.EMPTY_MAP));

        Map<String, Object> testMap = new HashMap<String, Object>();

        testMap.put("x", "y");
        assertFalse(utils.isEmpty(testMap));
        assertTrue(utils.isEmpty(new Object[0]));
        assertTrue(utils.isEmpty(new int[0]));
        assertFalse(utils.isEmpty(new Object[1]));
        assertFalse(utils.isEmpty(new int[1]));
    }

    @Test
    public void when_getNestingForm_provided_with_form_then_it_is_returned_as_result() {
        // given
        RendererUtils rendererUtils = RendererUtils.getInstance();
        UIComponent form = mock(UIForm.class);

        // when
        UIComponent result = rendererUtils.getNestingForm(form);

        // then
        assertEquals(form, result);
    }

    @Test
    public void when_getNestingForm_is_provided_with_component_which_has_form_ancestor_then_it_is_returned() {
        // given
        UIComponent form = mock(UIForm.class);
        UIComponent component1 = mock(UIComponent.class);
        UIComponent component2 = mock(UIComponent.class);
        when(component1.getParent()).thenReturn(component2);
        when(component2.getParent()).thenReturn(form);

        // when
        UIComponent result = utils.getNestingForm(component1);

        // then
        assertEquals(form, result);
    }

    @Test
    public void when_getNestingForm_is_provided_with_component_which_hasnt_form_ancestor_then_null_is_returned() {
        // given
        UIComponent viewRoot = mock(UIViewRoot.class);
        UIComponent component1 = mock(UIComponent.class);
        UIComponent component2 = mock(UIComponent.class);
        when(component1.getParent()).thenReturn(component2);
        when(component2.getParent()).thenReturn(viewRoot);

        // when
        UIComponent result = utils.getNestingForm(component1);

        // then
        assertNull(result);
    }

    @Test
    public void when_getSubmittedForm_is_called_on_initial_request_then_null_is_returned() {
        // given
        initializeRequestParameterMap(new HashMap<String, String>() {
        });

        when(facesContext.isPostback()).thenReturn(false);

        // when
        UIComponent result = utils.getSubmittedForm(facesContext);

        // then
        assertNull(result);
    }

    @Test
    public void when_getSubmittedForm_is_called_on_postback_and_no_matching_component_is_found_then_null_is_returned() {
        // given
        initializeRequestParameterMap(new HashMap<String, String>() {
            {
                put("same_but_no_clientId", "same_but_no_clientId");
            }
        });

        UIViewRoot viewRoot = mock(UIViewRoot.class);
        when(facesContext.getViewRoot()).thenReturn(viewRoot);
        when(viewRoot.getFacetsAndChildren()).thenReturn(Arrays.asList(new UIComponent[] {}).iterator());
        when(facesContext.isPostback()).thenReturn(true);

        // when
        UIComponent result = utils.getSubmittedForm(facesContext);

        // then
        assertNull(result);
    }

    @Test
    public void when_getSubmittedForm_is_called_with_form_details_provided_then_form_is_returned() {
        // given
        initializeRequestParameterMap(new HashMap<String, String>() {
            {
                put("form", "form");
            }
        });
        UIForm form = mock(UIForm.class);
        UIViewRoot viewRoot = mock(UIViewRoot.class);

        when(facesContext.getViewRoot()).thenReturn(viewRoot);
        when(facesContext.getViewRoot().findComponent("form")).thenReturn(form);
        when(facesContext.isPostback()).thenReturn(true);

        // when
        UIComponent result = utils.getSubmittedForm(facesContext);

        // then
        assertEquals(form, result);
    }

    @Test
    public void when_getSubmittedForm_is_called_with_component_in_form_details_provided_then_form_is_returned() {
        // given
        initializeRequestParameterMap(new HashMap<String, String>() {
            {
                put("component", "component");
            }
        });
        UIForm form = mock(UIForm.class);
        UIComponent component = mock(UIComponent.class);
        UIViewRoot viewRoot = mock(UIViewRoot.class);

        when(facesContext.getViewRoot()).thenReturn(viewRoot);
        when(facesContext.getViewRoot().findComponent("component")).thenReturn(component);
        when(component.getParent()).thenReturn(form);
        when(facesContext.isPostback()).thenReturn(true);

        // when
        UIComponent result = utils.getSubmittedForm(facesContext);

        // then
        assertEquals(form, result);
    }

    @Test
    public void when_isFormSubbmited_is_called_with_form() {
        // given
        initializeRequestParameterMap(new HashMap<String, String>() {
            {
                put("form", "form");
            }
        });
        UIForm form = mock(UIForm.class);

        when(form.getClientId(facesContext)).thenReturn("form");

        // when
        boolean result = utils.isFormSubmitted(facesContext, form);

        // then
        assertTrue(result);
    }

    @Test
    public void when_isFormSubbmited_is_called_with_form_which_does_not_have_request_parameter_equal_to_its_clientId_then_false_is_returned() {
        // given
        initializeRequestParameterMap(new HashMap<String, String>() {
            {
                put("form", "");
            }
        });
        UIForm form = mock(UIForm.class);

        when(form.getClientId(facesContext)).thenReturn("form");

        // when
        boolean result = utils.isFormSubmitted(facesContext, form);

        // then
        assertFalse(result);
    }

    @Test
    public void when_isFormSubbmited_is_called_with_form_which_does_not_have_corresponding_request_parameter_then_false_is_returned() {
        // given
        initializeRequestParameterMap(new HashMap<String, String>() {
        });
        UIForm form = mock(UIForm.class);

        when(form.getClientId(facesContext)).thenReturn("form");

        // when
        boolean result = utils.isFormSubmitted(facesContext, form);

        // then
        assertFalse(result);
    }

    private void initializeRequestParameterMap(Map<String, String> map) {
        when(externalContext.getRequestParameterMap()).thenReturn(map);
    }

}
