package org.richfaces.renderkit.focus;

import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractFocus;

/**
 * The strategy for rendering Focus component
 */
public interface FocusRenderStrategy {

    /**
     * This method will be called once the component is placed into view
     */
    void postAddToView(FacesContext context, AbstractFocus component);

    /**
     * Get space-separated list of clientIds as component candidates to be focused on client.
     */
    String getFocusCandidatesAsString(FacesContext context, AbstractFocus component);

    /**
     * Determines whether the currently rendered Focus should be rendered or not based on if request is postback and if Focus
     * belongs to form which has been submitted.
     */
    boolean shouldApply(FacesContext context, AbstractFocus component);
}