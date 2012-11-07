package org.richfaces.renderkit;

import java.util.Iterator;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractFocusManager;
import org.richfaces.renderkit.util.RendererUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;

@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
    @ResourceDependency(library = "com.jqueryui", name = "jquery.ui.core.js"),
    @ResourceDependency(library = "org.richfaces", name = "focusManager.js") })
public class FocusManagerRendererBase extends RendererBase {

    private static final String SUBMITTED_FOCUS = AbstractFocusManager.COMPONENT_TYPE + ".submittedFocus";

    private final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();

    /**
     * Decode focused component ID and save it to context
     */
    protected void doDecode(FacesContext context, UIComponent component) {
        AbstractFocusManager focusManager = (AbstractFocusManager) component;

        String focusClientIds = getFocusClientIds(context, focusManager);
        if (focusClientIds != null) {
            focusManager.setSubmittedValue(focusClientIds);
        }

        UIComponent focusedComponent = getFocusedComponent(context, focusManager, focusClientIds);
        if (focusedComponent != null) {
            setSubmittedFocus(context, focusedComponent);
        }

        if (focusManager.isAjaxRendered() && context.getPartialViewContext().isAjaxRequest()) {
            context.getPartialViewContext().getRenderIds().add(component.getClientId(context));
        }
    }

    /**
     * Get space-separated list of clientIds as component candidates to be focused on client.
     */
    protected String getFocusCandidatesAsString(FacesContext context, AbstractFocusManager component) {
        UIForm form = (UIForm) RENDERER_UTILS.getNestingForm(context, component);

        if (!context.isPostback() || form.isSubmitted()) {
            String[] focusCandidates = getFocusCandidates(context, component, form);
            String focusCandidatesAsString = Joiner.on(' ').join(focusCandidates);
            return focusCandidatesAsString;
        }

        return null;
    }

    /**
     * <p>Returns clientIds of component candidates to be focused.</p>
     */
    private String[] getFocusCandidates(FacesContext context, AbstractFocusManager component, UIForm form) {
        UIComponent submittedFocus = getSubmittedFocus(context);

        if (component.isPreserve() && submittedFocus != null) {
            return new String[] { submittedFocus.getClientId(context) };
        }

        if (component.isValidationAware()) {
            return getClientIdsWithMessagesAndFormClientId(context, form);
        }

        return new String[] { form.getClientId(context) };
    }

    /**
     * Returns clientIds of component for which validation has failed and adds form's clientId at the end (as a fallback if
     * there is no invalid component or none of invalid components won't be focusable on the client).
     */
    private String[] getClientIdsWithMessagesAndFormClientId(FacesContext context, UIForm form) {
        Iterator<String> clientIdsWithMessages = context.getClientIdsWithMessages();
        Iterator<String> formClientId = Iterators.forArray(new String[] { form.getClientId(context) });

        Iterator<String> merged = Iterators.concat(clientIdsWithMessages, formClientId);
        return Iterators.toArray(merged, String.class);
    }

    /**
     * Finds a currently focused component by the space-separated list of clientIds which are candidates to be real
     * {@link EditableValueHolder} components.
     */
    private UIComponent getFocusedComponent(FacesContext context, AbstractFocusManager component, String focusClientIds) {
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
    private String getFocusClientIds(FacesContext context, AbstractFocusManager component) {
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String clientId = component.getClientId(context);
        String inputFocus = requestParameterMap.get(clientId + "InputFocus");
        return inputFocus;
    }

    private void setSubmittedFocus(FacesContext context, UIComponent focusedComponent) {
        context.getAttributes().put(SUBMITTED_FOCUS, focusedComponent);
    }

    private UIComponent getSubmittedFocus(FacesContext context) {
        return (UIComponent) context.getAttributes().get(SUBMITTED_FOCUS);
    }
}
