/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.ui.misc.focus;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSLiteral;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.renderkit.RendererBase;
import org.richfaces.services.ServiceTracker;

@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "common/richfaces-base-component.js"),
        @ResourceDependency(library = "com.jqueryui", name = "jquery.ui.core.js"),
        @ResourceDependency(library = "org.richfaces", name = "misc/focus/focus.js") })
public class FocusRendererBase extends RendererBase implements FocusRendererInterface {

    public static final String RENDERER_TYPE = "org.richfaces.ui.FocusRenderer";

    private final FormFocusRenderStrategy FORM_RENDERING_STRATEGY = new FormFocusRenderStrategy();
    private final ViewFocusRenderStrategy VIEW_RENDERING_STRATEGY = new ViewFocusRenderStrategy();

    /**
     * Determines whether the currently rendered Focus should be rendered or not based on if request is postback and if Focus
     * belongs to form which has been submitted.
     */
    public boolean shouldApply(FacesContext context, AbstractFocus component) {
        return getStrategy(component).shouldApply(context, component);
    }

    /**
     * Get space-separated list of clientIds as component candidates to be focused on client.
     */
    public String getFocusCandidatesAsString(FacesContext context, AbstractFocus component) {
        return getStrategy(component).getFocusCandidatesAsString(context, component);
    }

    @Override
    public void postAddToView(FacesContext context, AbstractFocus component) {
        getStrategy(component).postAddToView(context, component);
    }

    /**
     * This method ensures that component which should be focused will be present in the page in the time of running script (oncomplete).
     */
    public void renderOncompleteScript(FacesContext context, String script) {
        JavaScriptService javaScriptService = ServiceTracker.getService(JavaScriptService.class);
        javaScriptService.addScript(context, new JSLiteral(script));
    }

    private FocusRenderStrategy getStrategy(AbstractFocus component) {
        switch (component.getMode()) {
            case FORM:
                return FORM_RENDERING_STRATEGY;
            case VIEW:
                return VIEW_RENDERING_STRATEGY;
            default:
                throw new UnsupportedOperationException("Retrieving focus candidates in " + component.getMode()
                        + " mode is not supported");
        }
    }
}
