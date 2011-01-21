/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

package org.richfaces.component.behavior;

import javax.el.ExpressionFactory;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.behavior.ClientBehavior;
import org.richfaces.cdk.annotations.*;
import org.richfaces.component.AbstractTogglePanel;
import org.richfaces.renderkit.util.RendererUtils;

/**
 * @author akolonitsky
 *
 */

@JsfBehavior(
        id = "org.richfaces.component.behavior.ToggleControl",
        tag = @Tag(name = "toggleControl", handler = "org.richfaces.view.facelets.html.CustomBehaviorHandler", type = TagType.Facelets),
        renderer = @JsfBehaviorRenderer(type = "org.richfaces.component.behavior.ToggleControl")
)
public class ToggleControl extends ClientBehavior {

    public static final String BEHAVIOR_ID = "org.richfaces.component.behavior.ToggleControl";

    private static final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();

    private enum PropertyKeys {
        event,
        targetItem,
        targetPanel,
        disableDefault
    }

    @Attribute
    public String getEvent() {
        return (String) getStateHelper().eval(PropertyKeys.event);
    }

    public void setEvent(String eventName) {
        getStateHelper().eval(PropertyKeys.event, eventName);
    }

    @Attribute
    public String getTargetItem() {
        return (String) getStateHelper().eval(PropertyKeys.targetItem, AbstractTogglePanel.META_NAME_NEXT);
    }

    public void setTargetItem(String target) {
        getStateHelper().put(PropertyKeys.targetItem, target);
    }

    @Attribute
    public String getTargetPanel() {
        return (String) getStateHelper().eval(PropertyKeys.targetPanel);
    }

    public void setTargetPanel(String selector) {
        getStateHelper().put(PropertyKeys.targetPanel, selector);
    }

    @Attribute
    public void setDisableDefault(Boolean disableDefault) {
        getStateHelper().put(PropertyKeys.disableDefault, disableDefault);
    }

    public Boolean getDisableDefault() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.disableDefault, true)));
    }

    public String getPanelId(ClientBehaviorContext behaviorContext) throws FacesException {
        return getPanel(behaviorContext.getComponent()).getClientId(behaviorContext.getFacesContext());
    }

    public AbstractTogglePanel getPanel(UIComponent comp) throws FacesException {
        String target = this.getTargetPanel();

        if (target != null) {

            UIComponent targetComponent = RENDERER_UTILS.findComponentFor(comp, target);

            if (null != targetComponent) {
                return (AbstractTogglePanel) targetComponent;
            } else {
                throw new FacesException("Parent panel for control (id="
                        + comp.getClientId(getFacesContext()) + ") has not been found.");
            }
        } else {
            return getEnclosedPanel(comp);
        }
    }

    public static AbstractTogglePanel getEnclosedPanel(UIComponent comp) {
        if (comp == null) {
            return null;
        }

        UIComponent control = comp;
        while (control != null) {
            if (control instanceof AbstractTogglePanel) {
                return (AbstractTogglePanel) control;
            }

            control = control.getParent();
        }
        
        throw new FacesException("Parent panel for control (id="
                + comp.getClientId(FacesContext.getCurrentInstance()) + ") has not been found.");
    }

    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }

    @Override
    public void setLiteralAttribute(String name, Object value) {
        if (compare(PropertyKeys.targetItem, name)) {
            setTargetItem((String) value);
        } else if (compare(PropertyKeys.targetPanel, name)) {
            setTargetPanel((String) value);
        } else if (compare(PropertyKeys.disableDefault, name)) {
            ExpressionFactory expFactory = getFacesContext().getApplication().getExpressionFactory();
            setDisableDefault((Boolean)expFactory.coerceToType(value, Boolean.class));
        }
    }
}
