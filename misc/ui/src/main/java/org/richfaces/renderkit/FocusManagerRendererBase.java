package org.richfaces.renderkit;

import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractFocusManager;
import org.richfaces.renderkit.util.RendererUtils;

import com.google.common.base.Splitter;

@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "focusManager.js") })
public class FocusManagerRendererBase extends RendererBase {

    private static final String CURRENT_FOCUS = AbstractFocusManager.COMPONENT_TYPE + ".currentFocus";

    private final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();

    protected void doDecode(FacesContext context, UIComponent component) {
        AbstractFocusManager focusManager = (AbstractFocusManager) component;

        String focusClientIds = getFocusClientIds(context, focusManager);
        if (focusClientIds != null) {
            focusManager.setSubmittedValue(focusClientIds);
        }

        UIComponent focusedComponent = getFocusedComponent(context, focusManager, focusClientIds);
        if (focusedComponent != null) {
            context.getAttributes().put(CURRENT_FOCUS, focusedComponent);
        }
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
}
