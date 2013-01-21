package org.richfaces.renderkit.focus;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractFocus;

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