package org.richfaces.renderkit.focus;

import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

public final class FocusRendererUtils {

    private static final String VIEW_FOCUS = FocusRendererUtils.class.getName() + ".VIEW_FOCUS";
    private static final String FORM_FOCUS = FocusRendererUtils.class.getName() + ".FORM_FOCUS";
    private static final String FIRST_FORM_FOCUS_PROCESSED = FocusRendererUtils.class.getName() + ".FIRST_FORM_FOCUS_PROCESSED";

    public static void markViewWithFocus(UIViewRoot viewRoot) {
        viewRoot.getAttributes().put(VIEW_FOCUS, VIEW_FOCUS);
    }

    public static boolean hasViewFocus(UIViewRoot viewRoot) {
        return VIEW_FOCUS.equals(viewRoot.getAttributes().get(VIEW_FOCUS));
    }

    public static void markFormWithFocus(UIForm form) {
        form.getAttributes().put(FORM_FOCUS, FORM_FOCUS);

    }

    public static boolean hasFormFocus(UIForm form) {
        return FORM_FOCUS.equals(form.getAttributes().get(FORM_FOCUS));

    }

    public static void markFirstFormFocusRendered(FacesContext context) {
        context.getAttributes().put(FIRST_FORM_FOCUS_PROCESSED, FIRST_FORM_FOCUS_PROCESSED);
    }

    public static boolean isFirstFormFocusRendered(FacesContext context) {
        return FIRST_FORM_FOCUS_PROCESSED.equals(context.getAttributes().get(FIRST_FORM_FOCUS_PROCESSED));
    }
}
