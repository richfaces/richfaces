/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.IterationStateHolder;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Nick Belaevski
 *
 */
@RunWith(MockTestRunner.class)
public class SavedStateTest {
    @Mock
    private FacesContext facesContext;
    private IterationStateHolder iterationStateHolder;
    private String iterationState;

    @Before
    public void setUp() throws Exception {
        iterationStateHolder = new IterationStateHolder() {
            public void setIterationState(Object state) {
                iterationState = (String) state;
            }

            public Object getIterationState() {
                return iterationState;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        iterationStateHolder = null;
    }

    private void checkDefaultState(SavedState state) {

        assertTrue(state.isValid());
        assertFalse(state.isLocalValueSet());
        assertFalse(state.isSubmitted());

        assertNull(state.getIterationState());
        assertNull(state.getSubmittedValue());
        assertNull(state.getValue());
    }

    @Test
    public void testDefaultValue() throws Exception {
        SavedState state = new SavedState();
        checkDefaultState(state);

        SavedState inputState = new SavedState(new UIInput());
        checkDefaultState(inputState);

        SavedState formState = new SavedState(new UIForm());
        checkDefaultState(formState);

        SavedState iterationState = new SavedState(iterationStateHolder);
        checkDefaultState(iterationState);
    }

    @Test
    public void testIterationStateHolderConstructor() throws Exception {
        this.iterationState = "some state";
        SavedState iterationState = new SavedState(iterationStateHolder);

        assertEquals("some state", iterationState.getIterationState());

        assertFalse(iterationState.isSubmitted());
        assertTrue(iterationState.isValid());
        assertNull(iterationState.getSubmittedValue());
        assertNull(iterationState.getValue());
        assertFalse(iterationState.isLocalValueSet());
    }

    @Test
    public void testIterationStateApply() throws Exception {
        SavedState iterationState = new SavedState();
        iterationState.setIterationState("some state");
        iterationState.apply(iterationStateHolder);

        assertEquals("some state", this.iterationState);
    }

    @Test
    public void testFormConstructor() {
        UIForm form = new UIForm();
        form.setSubmitted(true);

        SavedState formState = new SavedState(form);

        assertTrue(formState.isSubmitted());

        assertTrue(formState.isValid());
        assertNull(formState.getSubmittedValue());
        assertNull(formState.getValue());
        assertFalse(formState.isLocalValueSet());
        assertNull(formState.getIterationState());
    }

    @Test
    public void testFormApply() {
        SavedState formState = new SavedState();
        formState.setSubmitted(true);

        UIForm form = new UIForm();
        formState.apply(form);

        assertTrue(form.isSubmitted());
    }

    @Test
    public void testInputConstructor() {
        UIInput input = new UIInput();

        input.setValid(false);
        input.setSubmittedValue("submitted");
        input.setValue("value");
        input.setLocalValueSet(true);

        SavedState inputState = new SavedState(input);

        assertFalse(inputState.isValid());
        assertEquals("submitted", inputState.getSubmittedValue());
        assertEquals("value", inputState.getValue());
        assertTrue(inputState.isLocalValueSet());

        assertFalse(inputState.isSubmitted());
        assertNull(inputState.getIterationState());
    }

    @Test
    public void testInputApply() {
        SavedState state = new SavedState();
        state.setValid(false);
        state.setSubmittedValue("submitted");
        state.setValue("value");
        state.setLocalValueSet(true);

        UIInput input = new UIInput();
        state.apply(input);

        assertFalse(input.isValid());
        assertEquals("submitted", input.getSubmittedValue());
        assertEquals("value", input.getValue());
        assertTrue(input.isLocalValueSet());
    }

    @Test
    public void testTransient() throws Exception {
        SavedState defaultState = new SavedState();

        assertTrue(defaultState.isTransient());

        SavedState state = new SavedState();
        state.setIterationState("something");
        assertFalse(state.isTransient());

        state = new SavedState();
        state.setLocalValueSet(true);
        assertFalse(state.isTransient());

        state = new SavedState();
        state.setSubmitted(true);
        assertFalse(state.isTransient());

        state = new SavedState();
        state.setSubmittedValue("submitted");
        assertFalse(state.isTransient());

        state = new SavedState();
        state.setValid(false);
        assertFalse(state.isTransient());

        state = new SavedState();
        state.setValue(Integer.MAX_VALUE);
        assertFalse(state.isTransient());
    }
}
