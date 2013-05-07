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
 * Strategy for rendering Focus in {@link AbstractFocus.Mode#VIEW}
 */
public class ViewFocusRenderStrategy extends AbstractFocusRenderStrategy {

    @Override
    public void postAddToView(FacesContext context, AbstractFocus component) {
        FocusRendererUtils.markViewWithFocus(context.getViewRoot());
    }

    @Override
    public boolean shouldApply(FacesContext context, AbstractFocus component) {

        if (FocusRendererUtils.isFocusEnforced(context)) {
            return false;
        }

        if (!context.isPostback()) {
            return true;
        } else {
            UIForm form = (UIForm) RENDERER_UTILS.getSubmittedForm(context);

            if (!FocusRendererUtils.hasFormFocus(form)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getFocusCandidatesAsString(FacesContext context, AbstractFocus component) {

        if (!context.isPostback()) {
            // candidate will be selected on client-side
            return null;
        } else {
            UIForm form = (UIForm) RENDERER_UTILS.getSubmittedForm(context);

            return getFocusCandidatesAsString(context, component, form);
        }
    }
}