package org.richfaces.renderkit.focus;

import java.util.Iterator;
import java.util.Map;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractFocus;
import org.richfaces.util.RendererUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;

public abstract class AbstractFocusRenderStrategy implements FocusRenderStrategy {

    protected final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();

    protected String getFocusCandidatesAsString(FacesContext context, AbstractFocus component, UIForm form) {
        String[] focusCandidates = getFocusCandidates(context, component, form);

        if (focusCandidates.length == 0) {
            return form.getClientId(context);
        }

        String focusCandidatesAsString = Joiner.on(' ').join(focusCandidates);

        return focusCandidatesAsString;
    }

    /**
     * Return the list of candidate which could be focused on the end of request based on {@link AbstractFocus} component
     * settings.
     */
    private String[] getFocusCandidates(FacesContext context, AbstractFocus component, UIForm form) {
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
         Iterator<String> clientIdsWithMessages = Iterators.filter(context.getClientIdsWithMessages(), new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input != null;
            }
        });
        return Iterators.toArray(clientIdsWithMessages, String.class);
    }

    /**
     * Determines the submitted focused component
     *
     * @return submitted focused component; or null
     */
    private UIComponent getSubmittedFocus(FacesContext context, AbstractFocus component) {
        String focusClientIds = getFocusClientIds(context, component);

        if (focusClientIds == null) {
            return null;
        }

        UIComponent focusedComponent = getFocusedComponent(context, component, focusClientIds);
        return focusedComponent;
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
        String inputFocus = requestParameterMap.get("org.richfaces.focus");
        return inputFocus;
    }
}
