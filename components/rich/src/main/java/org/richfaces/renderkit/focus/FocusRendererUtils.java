package org.richfaces.renderkit.focus;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.richfaces.focus.FocusManager;



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
