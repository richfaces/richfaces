/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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
package org.ajax4jsf.renderkit;

import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.render.FacesBehaviorRenderer;
import javax.faces.render.RenderKitFactory;

import org.ajax4jsf.component.AjaxClientBehavior;
import org.ajax4jsf.component.behavior.AjaxBehavior;
import org.ajax4jsf.javascript.JSReference;
import org.richfaces.event.BypassUpdatesAjaxBehaviorEvent;
import org.richfaces.renderkit.AjaxConstants;
import org.richfaces.renderkit.AjaxFunction;
import org.richfaces.renderkit.AjaxOptions;
import org.richfaces.renderkit.util.AjaxRendererUtils;
import org.richfaces.renderkit.util.RendererUtils;

import com.google.common.base.Strings;

/**
 * @author Anton Belevich
 *
 */
@FacesBehaviorRenderer(rendererType = "org.ajax4jsf.behavior.Ajax", renderKitId = RenderKitFactory.HTML_BASIC_RENDER_KIT)
@ResourceDependencies({
        @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib")
})
public class AjaxBehaviorRenderer extends ClientBehaviorRenderer {
    private final RendererUtils utils = RendererUtils.getInstance();

    @Override
    public void decode(FacesContext context, UIComponent component, ClientBehavior behavior) {
        if (null == context || null == component || null == behavior) {
            throw new NullPointerException();
        }

        if (!(behavior instanceof AjaxBehavior)) {
            throw new IllegalArgumentException("org.ajax4jsf.component.behavior.AjaxBehavior required: " + behavior);
        }

        AjaxBehavior ajaxBehavior = (AjaxBehavior) behavior;

        if (ajaxBehavior.isDisabled()) {
            return;
        }

        component.queueEvent(createEvent(component, ajaxBehavior));

        if (isBypassUpdates(component, ajaxBehavior)) {
            component.queueEvent(new BypassUpdatesAjaxBehaviorEvent(component, ajaxBehavior));
        }
    }

    private AjaxBehaviorEvent createEvent(UIComponent component, AjaxBehavior ajaxBehavior) {
        AjaxBehaviorEvent event = new AjaxBehaviorEvent(component, ajaxBehavior);
        PhaseId phaseId;

        if (isImmediate(component, ajaxBehavior)) {
            phaseId = PhaseId.APPLY_REQUEST_VALUES;
        } else if (isBypassUpdates(component, ajaxBehavior)) {
            phaseId = PhaseId.PROCESS_VALIDATIONS;
        } else {
            phaseId = PhaseId.INVOKE_APPLICATION;
        }

        event.setPhaseId(phaseId);

        return event;
    }

    private boolean isImmediate(UIComponent component, AjaxBehavior ajaxBehavior) {
        boolean immediate = ajaxBehavior.isImmediate();
        if (!immediate) {
            if (component instanceof EditableValueHolder) {
                immediate = ((EditableValueHolder) component).isImmediate();
            } else if (component instanceof ActionSource) {
                immediate = ((ActionSource) component).isImmediate();
            }
        }

        return immediate;
    }

    private boolean isBypassUpdates(UIComponent component, AjaxBehavior ajaxBehavior) {
        boolean bypassUpdates = ajaxBehavior.isBypassUpdates();
        if (!bypassUpdates) {
            bypassUpdates = utils.isBooleanAttribute(component, "bypassUpdates");
        }

        return bypassUpdates;
    }

    @Override
    public String getScript(ClientBehaviorContext behaviorContext, ClientBehavior behavior) {
        String script = null;
        if (behavior instanceof AjaxBehavior && !((AjaxBehavior) behavior).isDisabled()) {
            script = buildAjaxCommand(behaviorContext, (AjaxBehavior) behavior);
        }

        return script;
    }

    public String buildAjaxCommand(ClientBehaviorContext bContext, AjaxBehavior behavior) {
        return buildAjaxFunction(bContext, behavior).toString();
    }

    public AjaxFunction buildAjaxFunction(ClientBehaviorContext behaviorContext, AjaxClientBehavior behavior) {
        Object source;

        AjaxOptions options = buildAjaxOptions(behaviorContext, behavior);

        if (behaviorContext.getSourceId() != null) {
            source = behaviorContext.getSourceId();
        } else {
            source = JSReference.THIS;

            FacesContext facesContext = behaviorContext.getFacesContext();
            UIComponent component = behaviorContext.getComponent();

            options.setAjaxComponent(component.getClientId(facesContext));
            options.set("sourceId", source);
        }

        if (behavior.isResetValues()) {
            options.setParameter(AjaxConstants.RESET_VALUES_PARAMETER, true);
        }

        return new AjaxFunction(source, options);
    }



    private AjaxOptions buildAjaxOptions(ClientBehaviorContext behaviorContext, AjaxClientBehavior ajaxBehavior) {
        FacesContext facesContext = behaviorContext.getFacesContext();
        UIComponent component = behaviorContext.getComponent();

        AjaxOptions ajaxOptions = new AjaxOptions();

        Map<String, Object> parametersMap = RendererUtils.getInstance().createParametersMap(facesContext, component);
        ajaxOptions.addParameters(parametersMap);

        String ajaxStatusName = ajaxBehavior.getStatus();
        if (Strings.isNullOrEmpty(ajaxStatusName)) {
            ajaxStatusName = AjaxRendererUtils.getAjaxStatus(component);
        }
        if (!Strings.isNullOrEmpty(ajaxStatusName)) {
            ajaxOptions.set(AjaxRendererUtils.STATUS_ATTR_NAME, ajaxStatusName);
        }

        appenAjaxBehaviorOptions(behaviorContext, ajaxBehavior, ajaxOptions);

        return ajaxOptions;
    }

    private void appenAjaxBehaviorOptions(ClientBehaviorContext behaviorContext, AjaxClientBehavior behavior,
        AjaxOptions ajaxOptions) {
        ajaxOptions.setParameter(AjaxConstants.BEHAVIOR_EVENT_PARAMETER, behaviorContext.getEventName());
        ajaxOptions.setBeforesubmitHandler(behavior.getOnbeforesubmit());

        for (BehaviorOptionsData optionsData : BehaviorOptionsData.values()) {
            String optionValue = optionsData.getAttributeValue(behavior);

            if (!Strings.isNullOrEmpty(optionValue)) {
                ajaxOptions.set(optionsData.toString(), optionValue);
            }
        }
    }

    private static enum BehaviorOptionsData {
        begin {
            @Override
            public String getAttributeValue(AjaxClientBehavior behavior) {
                return behavior.getOnbegin();
            }
        },
        error {
            @Override
            public String getAttributeValue(AjaxClientBehavior behavior) {
                return behavior.getOnerror();
            }
        },
        queueId {
            @Override
            public String getAttributeValue(AjaxClientBehavior behavior) {
                return behavior.getQueueId();
            }
        };

        public abstract String getAttributeValue(AjaxClientBehavior behavior);
    }
}
