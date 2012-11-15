package org.richfaces.renderkit;

import java.util.Iterator;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractFocus;
import org.richfaces.renderkit.util.RendererUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;

@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "com.jqueryui", name = "jquery.ui.core.js"),
        @ResourceDependency(library = "org.richfaces", name = "focus.js") })
public class FocusRendererBase extends RendererBase {

    public static final String RENDERER_TYPE = "org.richfaces.FocusRenderer";

    private static final String FOCUS_PROCESSED_ATTRIBUTE = FocusRendererBase.class.getName() + ".FOCUS_PROCESSED_ATTRIBUTE";

    private final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();

    /**
     * Determines whether the currently rendered Focus should be rendered or not based on if request is postback and if Focus
     * belongs to form which has been submitted.
     */
    public boolean shouldRender(FacesContext context, AbstractFocus component) {
        UIForm form = (UIForm) RENDERER_UTILS.getNestingForm(context, component);

        if (!context.isPostback()) {
            Boolean shouldProcess = (Boolean) context.getAttributes().get(FOCUS_PROCESSED_ATTRIBUTE);
            if (Boolean.TRUE.equals(shouldProcess)) {
                return false;
            } else {
                context.getAttributes().put(FOCUS_PROCESSED_ATTRIBUTE, Boolean.TRUE);
                return true;
            }
        } else {
            return isFormSubmitted(context, form);
        }
    }

    /**
     * Get space-separated list of clientIds as component candidates to be focused on client.
     */
    public String getFocusCandidatesAsString(FacesContext context, AbstractFocus component) {

        UIForm form = (UIForm) RENDERER_UTILS.getNestingForm(context, component);

        if (!context.isPostback()) {
            return form.getClientId(context);
        }

        if (isFormSubmitted(context, form)) {
            String[] focusCandidates = getFocusCandidates(context, component, form);

            if (focusCandidates.length == 0) {
                return form.getClientId(context);
            }

            String focusCandidatesAsString = Joiner.on(' ').join(focusCandidates);

            return focusCandidatesAsString;
        }

        return null;
    }

    /**
     * Determines whenever given form has been submitted
     */
    private boolean isFormSubmitted(FacesContext context, UIForm form) {
        if (form != null) {
            String clientId = form.getClientId(context);
            String formRequestParam = context.getExternalContext().getRequestParameterMap().get(clientId);
            boolean isSubmitted = clientId.equals(formRequestParam);
            return isSubmitted;
        }
        return false;
    }

    /**
     * <p>
     * Returns clientIds of component candidates to be focused.
     * </p>
     */
    public String[] getFocusCandidates(FacesContext context, AbstractFocus component, UIForm form) {
        UIComponent submittedFocus = getSubmittedFocus(context, component);

        if (component.isPreserve() && submittedFocus != null) {
            return new String[] { submittedFocus.getClientId(context) };
        }

        if (component.isValidationAware()) {
            return getClientIdsWithMessages(context, form);
        }

        return new String[] { form.getClientId(context) };
    }

    /**
     * Returns clientIds of component for which validation has failed and adds form's clientId at the end (as a fallback if
     * there is no invalid component or none of invalid components won't be focusable on the client).
     */
    private String[] getClientIdsWithMessages(FacesContext context, UIForm form) {
        Iterator<String> clientIdsWithMessages = context.getClientIdsWithMessages();
        return Iterators.toArray(clientIdsWithMessages, String.class);
    }

    /**
     * Finds a currently focused component by the space-separated list of clientIds which are candidates to be real
     * {@link EditableValueHolder} components.
     */
    private UIComponent getFocusedComponent(FacesContext context, AbstractFocus component, String focusClientIds) {
        Iterable<String> clientIds = Splitter.on(' ').split(focusClientIds);

        for (String clientId : clientIds) {
            UIComponent focusedComponent = RENDERER_UTILS.findComponentFor(component, clientId);
            if (focusedComponent instanceof EditableValueHolder) {
                return focusedComponent;
            }
        }

        return null;
    }

    /**
     * Returns the comma-separated list of clientIds which are candidates to be real components.
     */
    private String getFocusClientIds(FacesContext context, AbstractFocus component) {
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String clientId = component.getClientId(context);
        String inputFocus = requestParameterMap.get(clientId + "InputFocus");
        return inputFocus;
    }

    private UIComponent getSubmittedFocus(FacesContext context, AbstractFocus component) {
        String focusClientIds = getFocusClientIds(context, component);

        if (focusClientIds == null) {
            return null;
        }

        UIComponent focusedComponent = getFocusedComponent(context, component, focusClientIds);
        return focusedComponent;
    }
}
