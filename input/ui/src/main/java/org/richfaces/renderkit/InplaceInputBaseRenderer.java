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
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.component.AbstractInplaceInput;
import org.richfaces.component.InplaceState;
import org.richfaces.component.util.HtmlUtil;

/**
 * @author Anton Belevich
 * 
 */
@ResourceDependencies({ 
    @ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"), 
    @ResourceDependency(name = "richfaces-event.js"), 
    @ResourceDependency(library="org.richfaces", name = "inplaceInput.js"), 
    @ResourceDependency(library="org.richfaces", name = "inplaceInput.ecss") })
public class InplaceInputBaseRenderer extends InputRendererBase {
    
    public static final String OPTIONS_EDIT_EVENT = "editEvent";
    
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
    
    public static final String OPTIONS_INITIAL_VALUE = "initialValue";
    
    private static final String READY_STATE_CSS = "rf-ii-d-s";

    private static final String EDIT_STATE_CSS = "rf-ii-e-s";

    private static final String CHANGED_STATE_CSS = "rf-ii-c-s";

    private static final String NONE_CSS = "rf-ii-none";
    
    private static final Map<String, ComponentAttribute> INPLACEINPUT_HANDLER_ATTRIBUTES = Collections
    .unmodifiableMap(ComponentAttribute.createMap(
        new ComponentAttribute(HtmlConstants.ONCLICK_ATTRIBUTE).setEventNames("inputClick").
            setComponentAttributeName("onInputClick"),
        new ComponentAttribute(HtmlConstants.ONDBLCLICK_ATTRIBUTE).setEventNames("inputDblclick").
            setComponentAttributeName("onInputDblclick"),
        new ComponentAttribute(HtmlConstants.ONMOUSEDOWN_ATTRIBUTE).setEventNames("inputMousedown").
            setComponentAttributeName("onInputMousedown"),
        new ComponentAttribute(HtmlConstants.ONMOUSEUP_ATTRIBUTE).setEventNames("inputMouseup").
            setComponentAttributeName("onInputMouseup"),
        new ComponentAttribute(HtmlConstants.ONMOUSEOVER_ATTRIBUTE).setEventNames("inputMouseover").
            setComponentAttributeName("onInputMouseover"),
        new ComponentAttribute(HtmlConstants.ONMOUSEMOVE_ATTRIBUTE).setEventNames("inputMousemove").
            setComponentAttributeName("onInputMousemove"),
        new ComponentAttribute(HtmlConstants.ONMOUSEOUT_ATTRIBUTE).setEventNames("inputMouseout").
            setComponentAttributeName("onInputMouseout"),
        new ComponentAttribute(HtmlConstants.ONKEYPRESS_ATTRIBUTE).setEventNames("inputKeypress").
            setComponentAttributeName("onInputKeypress"),
        new ComponentAttribute(HtmlConstants.ONKEYDOWN_ATTRIBUTE).setEventNames("inputKeydown").
            setComponentAttributeName("onInputKeydown"),
        new ComponentAttribute(HtmlConstants.ONKEYUP_ATTRIBUTE).setEventNames("inputKeyup").
            setComponentAttributeName("onInputKeyup"),
        new ComponentAttribute(HtmlConstants.ONBLUR_ATTRIBUTE).setEventNames("inputBlur").
            setComponentAttributeName("onInputBlur"),
        new ComponentAttribute(HtmlConstants.ONFOCUS_ATTRIBUTE).setEventNames("inputFocus").
            setComponentAttributeName("onInputFocus"),
        new ComponentAttribute(HtmlConstants.ONCHANGE_ATTRIBUTE).setEventNames("change").
            setComponentAttributeName("onchange"),
        new ComponentAttribute(HtmlConstants.ONSELECT_ATTRIBUTE).setEventNames("select").
            setComponentAttributeName("onselect")
    ));

    protected void renderInputHandlers(FacesContext facesContext, UIComponent component) throws IOException {
        RenderKitUtils.renderPassThroughAttributesOptimized(facesContext, component, INPLACEINPUT_HANDLER_ATTRIBUTES);
    }
    
    public InplaceState getInplaceState(UIComponent component) {
        return ((AbstractInplaceInput) component).getState();
    }

    public String getValue(FacesContext facesContext, UIComponent component) throws IOException {
        AbstractInplaceInput inplaceInput = (AbstractInplaceInput)component;        
        String value = getInputValue(facesContext, inplaceInput);
        if(value == null || "".equals(value)) {
            value = inplaceInput.getDefaultLabel();
        }
        return value;
    }
    
    public String getResourcePath(FacesContext context, String resourceName) {
        if (resourceName != null) {
            ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
            Resource resource = resourceHandler.createResource(resourceName);
            return resource.getRequestPath();
        }
        return null;
    }

    public String getReadyStyleClass(UIComponent component, InplaceState inplaceState) {
        return (InplaceState.changed != inplaceState) ? READY_STATE_CSS : HtmlUtil.concatClasses(READY_STATE_CSS, CHANGED_STATE_CSS);
    }

    public String getEditStyleClass(UIComponent component, InplaceState inplaceState) {
        return (InplaceState.edit != inplaceState)? HtmlUtil.concatClasses(EDIT_STATE_CSS, NONE_CSS) : EDIT_STATE_CSS;
    }
    public String getReadyClientId(FacesContext facesContext, UIComponent component, InplaceState inplaceState) {
        String clientId = component.getClientId(facesContext);
        return getId(clientId, InplaceState.ready, inplaceState);
    }

    public String getChangedClientId(FacesContext facesContext, UIComponent component, InplaceState inplaceState) {
        String clientId = component.getClientId(facesContext);
        return getId(clientId, InplaceState.changed, inplaceState);
    }

    private String getId(String clientId, InplaceState expect, InplaceState current) {
        String result = clientId;
        if (expect != current) {
            result = clientId + ":" + expect;
        }
        return result;
    }
    
    public void buildScript(ResponseWriter writer, FacesContext facesContext, UIComponent component) throws IOException {
        AbstractInplaceInput inplaceInput = (AbstractInplaceInput)component;
        JSFunction function = new JSFunction("new RichFaces.ui.InplaceInput");
        function.addParameter(inplaceInput.getClientId(facesContext));
        
        String clientId = inplaceInput.getClientId(facesContext);
        Map<String, Object> options = new HashMap<String, Object>();
        options.put(OPTIONS_EDIT_EVENT, inplaceInput.getEditEvent());
        options.put(OPTIONS_NONE_CSS, NONE_CSS);
        options.put(OPTIONS_CHANGED_CSS, CHANGED_STATE_CSS);
        options.put(OPTIONS_EDIT_CONTAINER, clientId + ":edit");
        options.put(OPTIONS_INPUT, clientId + ":input");
        options.put(OPTIONS_LABEL, clientId + ":label");
        options.put(OPTIONS_FOCUS, clientId + ":focus");
        options.put(OPTIONS_DEFAULT_LABEL, inplaceInput.getDefaultLabel());
        
        boolean showControls = inplaceInput.isShowControls();
        options.put(OPTIONS_SHOWCONTROLS, showControls);
        if(showControls) {
            options.put(OPTIONS_BUTTON_OK, clientId + ":okbtn");
            options.put(OPTIONS_BUTTON_CANCEL, clientId + ":cancelbtn");
        }    
        function.addParameter(options);
        
        writer.write(function.toString());
    }
}
