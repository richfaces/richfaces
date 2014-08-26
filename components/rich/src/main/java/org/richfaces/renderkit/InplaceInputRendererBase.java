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

import org.richfaces.component.AbstractInplaceInput;
import org.richfaces.component.InplaceComponent;
import org.richfaces.component.InplaceState;
import org.richfaces.renderkit.util.HtmlDimensions;

/**
 * @author Anton Belevich
 *
 */
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "inputBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceInput.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceInput.ecss") })
public class InplaceInputRendererBase extends InputRendererBase {
    // TODO: anton - move to RenderUtils (we use the same in the calendar base renderer) ?
    protected static final Map<String, ComponentAttribute> INPLACE_INPUT_HANDLER_ATTRIBUTES = Collections
        .unmodifiableMap(ComponentAttribute.createMap(
            new ComponentAttribute(HtmlConstants.ONCLICK_ATTRIBUTE).setEventNames("inputclick").setComponentAttributeName(
                "oninputclick"),
            new ComponentAttribute(HtmlConstants.ONDBLCLICK_ATTRIBUTE).setEventNames("inputdblclick")
                .setComponentAttributeName("oninputdblclick"),
            new ComponentAttribute(HtmlConstants.ONMOUSEDOWN_ATTRIBUTE).setEventNames("inputmousedown")
                .setComponentAttributeName("oninputmousedown"),
            new ComponentAttribute(HtmlConstants.ONMOUSEUP_ATTRIBUTE).setEventNames("inputmouseup").setComponentAttributeName(
                "oninputmouseup"),
            new ComponentAttribute(HtmlConstants.ONMOUSEOVER_ATTRIBUTE).setEventNames("inputmouseover")
                .setComponentAttributeName("oninputmouseover"),
            new ComponentAttribute(HtmlConstants.ONMOUSEMOVE_ATTRIBUTE).setEventNames("inputmousemove")
                .setComponentAttributeName("oninputmousemove"),
            new ComponentAttribute(HtmlConstants.ONMOUSEOUT_ATTRIBUTE).setEventNames("inputmouseout")
                .setComponentAttributeName("oninputmouseout"),
            new ComponentAttribute(HtmlConstants.ONKEYPRESS_ATTRIBUTE).setEventNames("inputkeypress")
                .setComponentAttributeName("oninputkeypress"),
            new ComponentAttribute(HtmlConstants.ONKEYDOWN_ATTRIBUTE).setEventNames("inputkeydown").setComponentAttributeName(
                "oninputkeydown"),
            new ComponentAttribute(HtmlConstants.ONKEYUP_ATTRIBUTE).setEventNames("inputkeyup").setComponentAttributeName(
                "oninputkeyup"), new ComponentAttribute(HtmlConstants.ONSELECT_ATTRIBUTE).setEventNames("inputselect")
                .setComponentAttributeName("oninputselect")));

    public void renderInputHandlers(FacesContext facesContext, UIComponent component) throws IOException {
        RenderKitUtils.renderPassThroughAttributesOptimized(facesContext, component, INPLACE_INPUT_HANDLER_ATTRIBUTES);
    }

    public InplaceState getInplaceState(UIComponent component) {
        InplaceState state = ((InplaceComponent) component).getState();
        if (state == null) {
            state = InplaceState.ready;
        }
        return state;
    }

    public String getValue(FacesContext facesContext, UIComponent component) throws IOException {
        AbstractInplaceInput inplaceInput = (AbstractInplaceInput) component;
        String value = getInputValue(facesContext, inplaceInput);
        if (!inplaceInput.isDisabled() && (value == null || "".equals(value))) {
            value = inplaceInput.getDefaultLabel();
        }
        return value;
    }

    public String getStateStyleClass(UIComponent component, InplaceState inplaceState) {
        InplaceComponent inplaceComponent = (InplaceComponent) component;
        String style = getReadyStateCss(inplaceComponent);
        if (!inplaceComponent.isDisabled()) {
            switch (inplaceState) {
                case edit:
                    style = concatClasses(style, getEditStateCss(inplaceComponent));
                    break;
                case changed:
                    style = concatClasses(style, getChangedStateCss(inplaceComponent));
                    break;
                default:
                    break;
            }
        } else {
            style = concatClasses(style, getDisableStateCss(inplaceComponent));
        }
        return style;
    }

    public String getContainerStyleClasses(UIComponent component) {
        InplaceComponent inplaceComponent = (InplaceComponent) component;
        String style = "rf-ii";
        if (inplaceComponent.isDisabled()) {
            style = concatClasses(style, getDisableStateCss(inplaceComponent));
        }
        style = concatClasses(style, component.getAttributes().get("placeHolderStyleClass"));
        return style;
    }

    public String getEditStyleClass(UIComponent component, InplaceState inplaceState) {
        InplaceComponent inplaceComponent = (InplaceComponent) component;
        return (InplaceState.edit != inplaceState) ? concatClasses(getEditCss(inplaceComponent), getNoneCss(inplaceComponent))
            : getEditCss(inplaceComponent);
    }

    public String getReadyStateCss(InplaceComponent component) {
        return "rf-ii";
    }

    public String getEditStateCss(InplaceComponent component) {
        String css = component.getActiveClass();
        return concatClasses("rf-ii-act", css);
    }

    public String getChangedStateCss(InplaceComponent component) {
        String css = component.getChangedClass();
        return concatClasses("rf-ii-chng", css);
    }

    public String getDisableStateCss(InplaceComponent component) {
        String css = component.getDisabledClass();
        return concatClasses("rf-ii-dis", css);
    }

    public String getEditCss(InplaceComponent component) {
        return "rf-ii-fld-cntr";
    }

    public String getNoneCss(InplaceComponent component) {
        return "rf-ii-none";
    }

    protected String getInputWidth(UIComponent component) {
        String value = ((AbstractInplaceInput) component).getInputWidth();
        if (value == null || "".equals(value)) {
            value = "100%";
        }
        return HtmlDimensions.formatSize(value);
    }

    protected String getEditEvent(UIComponent component) {
        String value = ((InplaceComponent) component).getEditEvent();
        if (value == null || "".equals(value)) {
            value = "click";
        }
        return value;
    }
}
