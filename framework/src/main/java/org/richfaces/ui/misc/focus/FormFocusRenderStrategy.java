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

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

/**
 * Strategy for rendering Focus in {@link AbstractFocus.Mode#FORM}
 */
public class FormFocusRenderStrategy extends AbstractFocusRenderStrategy {

    @Override
    public void postAddToView(FacesContext context, AbstractFocus component) {
        UIForm form = (UIForm) RENDERER_UTILS.getNestingForm(component);
        FocusRendererUtils.markFormWithFocus(form);
    }

    @Override
    public boolean shouldApply(FacesContext context, AbstractFocus component) {

        if (FocusRendererUtils.isFocusEnforced(context)) {
            return false;
        }

        if (component.isDelayed()) {
            return false;
        }

        UIForm form = (UIForm) RENDERER_UTILS.getNestingForm(component);

        if (!context.isPostback()) {
            if (FocusRendererUtils.hasViewFocus(context.getViewRoot())) {
                return false;
            } else if (isSomeAnotherFormFocusRendered(context, component)) {
                return false;
            } else {
                FocusRendererUtils.markFirstFormFocusRendered(context, component);
                return true;
            }
        } else {
            return RENDERER_UTILS.isFormSubmitted(context, form);
        }
    }

    private boolean isSomeAnotherFormFocusRendered(FacesContext context, AbstractFocus component) {
        String firstFormFocusRendered = FocusRendererUtils.getFirstFormFocusRendered(context);

        if (firstFormFocusRendered == null) {
            return false;
        }

        return !firstFormFocusRendered.equals(component.getClientId(context));
    }

    @Override
    public String getFocusCandidatesAsString(FacesContext context, AbstractFocus component) {
        UIForm form = (UIForm) RENDERER_UTILS.getNestingForm(component);

        if (!context.isPostback()) {
            return form.getClientId(context);
        }

        if (RENDERER_UTILS.isFormSubmitted(context, form)) {
            return getFocusCandidatesAsString(context, component, form);
        }

        return null;
    }
}