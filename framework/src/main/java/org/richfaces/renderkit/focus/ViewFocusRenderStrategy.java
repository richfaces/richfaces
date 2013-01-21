package org.richfaces.renderkit.focus;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractFocus;

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