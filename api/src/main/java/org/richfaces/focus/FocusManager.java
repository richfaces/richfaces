package org.richfaces.focus;

import javax.faces.event.PhaseId;

/**
 * Service for managing Focus of components on the page.
 */
public interface FocusManager {

    String FOCUS_CONTEXT_ATTRIBUTE = FocusManager.class.getName() + ".FOCUS";

    /**
     * <p>
     * Enforces to focus given component.
     * </p>
     *
     * <p>
     * In order to ensure the focus will be given to component, this method must be used before {@link PhaseId#RENDER_RESPONSE}
     * phase takes place.
     * </p>
     *
     * @param componentId ID of the component to be focused; or null if focus should not be enforced
     */
    void focus(String componentId);
}
