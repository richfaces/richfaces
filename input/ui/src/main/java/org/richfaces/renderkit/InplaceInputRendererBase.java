/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.InplaceComponent;
import org.richfaces.component.InplaceState;

/**
 * @author Anton Belevich
 * 
 */
@ResourceDependencies({
        @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(name = "jquery.js"),
        @ResourceDependency(name = "richfaces.js"),
        @ResourceDependency(name = "richfaces-event.js"),
        @ResourceDependency(name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "inputBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceInput.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceInput.ecss") })
public class InplaceInputRendererBase extends InputRendererBase {

    public static final String OPTIONS_EDIT_EVENT = "editEvent";
    
    public static final String OPTIONS_STATE = "state";

    public static final String OPTIONS_EDIT_CONTAINER = "editContainer";

    public static final String OPTIONS_INPUT = "input";

    public static final String OPTIONS_FOCUS = "focusElement";

    public static final String OPTIONS_BUTTON_OK = "okbtn";

    public static final String OPTIONS_LABEL = "label";

    public static final String OPTIONS_DEFAULT_LABEL = "defaultLabel";

    public static final String OPTIONS_BUTTON_CANCEL = "cancelbtn";

    public static final String OPTIONS_SHOWCONTROLS = "showControls";

    public static final String OPTIONS_NONE_CSS = "noneCss";

    public static final String OPTIONS_CHANGED_CSS = "changedCss";
    
    public static final String OPTIONS_EDIT_CSS = "editCss";

    public static final String OPTIONS_INITIAL_VALUE = "initialValue";

    public static final String OPTIONS_SAVE_ON_BLUR = "saveOnBlur";
    
    
    
        
    //TODO: anton - move to RenderUtils (we use the same in the calendar base renderer) ? 
    protected static final Map<String, ComponentAttribute> INPLACE_INPUT_HANDLER_ATTRIBUTES = Collections
            .unmodifiableMap(ComponentAttribute.createMap(
                    new ComponentAttribute(HtmlConstants.ONCLICK_ATTRIBUTE)
                            .setEventNames("inputclick")
                            .setComponentAttributeName("oninputclick"),
                    new ComponentAttribute(HtmlConstants.ONDBLCLICK_ATTRIBUTE)
                            .setEventNames("inputdblclick")
                            .setComponentAttributeName("oninputdblclick"),
                    new ComponentAttribute(HtmlConstants.ONMOUSEDOWN_ATTRIBUTE)
                            .setEventNames("inputmousedown")
                            .setComponentAttributeName("oninputmousedown"),
                    new ComponentAttribute(HtmlConstants.ONMOUSEUP_ATTRIBUTE)
                            .setEventNames("inputmouseup")
                            .setComponentAttributeName("oninputmouseup"),
                    new ComponentAttribute(HtmlConstants.ONMOUSEOVER_ATTRIBUTE)
                            .setEventNames("inputmouseover")
                            .setComponentAttributeName("oninputmouseover"),
                    new ComponentAttribute(HtmlConstants.ONMOUSEMOVE_ATTRIBUTE)
                            .setEventNames("inputmousemove")
                            .setComponentAttributeName("oninputmousemove"),
                    new ComponentAttribute(HtmlConstants.ONMOUSEOUT_ATTRIBUTE)
                            .setEventNames("inputmouseout")
                            .setComponentAttributeName("oninputmouseout"),
                    new ComponentAttribute(HtmlConstants.ONKEYPRESS_ATTRIBUTE)
                            .setEventNames("inputkeypress")
                            .setComponentAttributeName("oninputkeypress"),
                    new ComponentAttribute(HtmlConstants.ONKEYDOWN_ATTRIBUTE)
                            .setEventNames("inputkeydown")
                            .setComponentAttributeName("oninputkeydown"),
                    new ComponentAttribute(HtmlConstants.ONKEYUP_ATTRIBUTE)
                            .setEventNames("inputkeyup")
                            .setComponentAttributeName("oninputkeyup"),
                    new ComponentAttribute(HtmlConstants.ONSELECT_ATTRIBUTE)
                            .setEventNames("inputselect").setComponentAttributeName(
                                    "oninputselect")));

    public void renderInputHandlers(FacesContext facesContext,
            UIComponent component) throws IOException {
        RenderKitUtils.renderPassThroughAttributesOptimized(facesContext,
                component, INPLACE_INPUT_HANDLER_ATTRIBUTES);
    }

    public InplaceState getInplaceState(UIComponent component) {
        return ((InplaceComponent) component).getState();
    }

    public String getValue(FacesContext facesContext, UIComponent component) throws IOException {
        String value = getInputValue(facesContext, component);
        if (!isDisable(getInplaceState(component)) && (value == null || "".equals(value)) ) {
            value = ((InplaceComponent) component).getDefaultLabel();
        }
        return value;
    }

    public String getResourcePath(FacesContext context, String library, String resourceName) {
        return RenderKitUtils.getResourcePath(context, library, resourceName);
    }

    public String getStateStyleClass(UIComponent component, InplaceState inplaceState) {
        InplaceComponent inplaceComponent = (InplaceComponent)component; 
        String style = getReadyStateCss(inplaceComponent);
        switch (inplaceState) {
            case edit:
                style = concatClasses(style, getEditStateCss(inplaceComponent));
                break;
    
            case changed: 
                style = concatClasses(style, getChangedStateCss(inplaceComponent));
                break;
    
            case disable:
                style = getDisableStateCss(inplaceComponent);
                break;
    
            default:
                break;
        }

        return style;
    }

    public boolean isDisable(InplaceState currentState) {
        return (InplaceState.disable == currentState); 
    }
    
    public String getEditStyleClass(UIComponent component,  InplaceState inplaceState) {
        InplaceComponent inplaceComponent = (InplaceComponent)component;
        return (InplaceState.edit != inplaceState) ? concatClasses(getEditCss(inplaceComponent), getNoneCss(inplaceComponent)) : getEditCss(inplaceComponent);
    }
  
    public String getReadyStateCss(InplaceComponent component) {
        String css = component.getReadyStateClass();
        return concatClasses("rf-ii-d-s", css);
    }

    public String getEditStateCss(InplaceComponent component) {
        String css = component.getEditStateClass();
        return concatClasses("rf-ii-e-s", css);
    }

    public String getChangedStateCss(InplaceComponent component) {
        String css = component.getChangedStateClass();
        return concatClasses("rf-ii-c-s", css);
    }

    public String getDisableStateCss(InplaceComponent component) {
        String css = component.getDisabledStateClass();
        return concatClasses("rf-ii-dis-s", css);
    }
    
    public String getEditCss(InplaceComponent component) {
        String css = component.getEditClass();
        return concatClasses("rf-ii-edit", css);
    }

    public String getNoneCss(InplaceComponent component) {
        String css = component.getNoneClass();
        return concatClasses("rf-ii-none", css);
    }
}
