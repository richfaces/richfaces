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

package org.richfaces.ui.misc.focus;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

public final class FocusRendererUtils {

    private static final String VIEW_FOCUS = FocusRendererUtils.class.getName() + ".VIEW_FOCUS";
    private static final String FORM_FOCUS = FocusRendererUtils.class.getName() + ".FORM_FOCUS";
    private static final String FIRST_FORM_FOCUS_PROCESSED = FocusRendererUtils.class.getName() + ".FIRST_FORM_FOCUS_PROCESSED";

    /**
     * Marks context of {@link UIViewRoot} with information that view focus is present
     */
    public static void markViewWithFocus(UIViewRoot viewRoot) {
        viewRoot.getAttributes().put(VIEW_FOCUS, VIEW_FOCUS);
    }

    /**
     * Determines whether view focus is present based on context attribute
     */
    public static boolean hasViewFocus(UIViewRoot viewRoot) {
        return VIEW_FOCUS.equals(viewRoot.getAttributes().get(VIEW_FOCUS));
    }

    /**
     * Marks given form that it contains focus
     */
    public static void markFormWithFocus(UIForm form) {
        form.getAttributes().put(FORM_FOCUS, FORM_FOCUS);

    }

    /**
     * Determines if given form contains focus
     */
    public static boolean hasFormFocus(UIForm form) {
        return FORM_FOCUS.equals(form.getAttributes().get(FORM_FOCUS));

    }

    /**
     * Marks context with information that one form has already focus applied
     */
    public static void markFirstFormFocusRendered(FacesContext context, UIComponent focusComponent) {
        context.getAttributes().put(FIRST_FORM_FOCUS_PROCESSED, focusComponent.getClientId(context));
    }

    /**
     * Determines whether there is form which has already focus rendered
     */
    public static String getFirstFormFocusRendered(FacesContext context) {
        return (String) context.getAttributes().get(FIRST_FORM_FOCUS_PROCESSED);
    }

    /**
     * Determines if focus has been enforced using {@link FocusManager}.
     */
    public static boolean isFocusEnforced(FacesContext context) {
        return context.getAttributes().get(FocusManager.FOCUS_CONTEXT_ATTRIBUTE) != null;
    }
}
