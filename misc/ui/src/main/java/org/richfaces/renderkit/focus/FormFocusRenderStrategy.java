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
    public boolean shouldRender(FacesContext context, AbstractFocus component) {
        UIForm form = (UIForm) RENDERER_UTILS.getNestingForm(component);

        if (!context.isPostback()) {
            if (FocusRendererUtils.hasViewFocus(context.getViewRoot()) || FocusRendererUtils.isFirstFormFocusRendered(context)) {
                return false;
            } else {
                FocusRendererUtils.markFirstFormFocusRendered(context);
                return true;
            }
        } else {
            return RENDERER_UTILS.isFormSubmitted(context, form);
        }
    }

    @Override
    public String getFocusCandidatesAsString(FacesContext context, AbstractFocus component) {
        UIForm form = (UIForm) RENDERER_UTILS.getNestingForm(context, component);

        if (!context.isPostback()) {
            return form.getClientId(context);
        }

        if (RENDERER_UTILS.isFormSubmitted(context, form)) {
            return getFocusCandidatesAsString(context, component, form);
        }

        return null;
    }
}