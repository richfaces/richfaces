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
import java.util.List;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractInplaceSelect;

/**
 * @author Anton Belevich
 * 
 */

@ResourceDependencies({
        @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(name = "jquery.js"),
        @ResourceDependency(name = "jquery.position.js"),
        @ResourceDependency(name = "richfaces.js"),
        @ResourceDependency(name = "jquery.position.js"),
        @ResourceDependency(name = "richfaces-event.js"),
        @ResourceDependency(name = "richfaces-base-component.js"),
        @ResourceDependency(name = "richfaces-selection.js"),
        @ResourceDependency(library = "org.richfaces", name = "inputBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "popup.js"),
        @ResourceDependency(library = "org.richfaces", name = "popupList.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceInput.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceSelect.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceSelect.ecss") })
public class InplaceSelectRendererBase extends InplaceInputRendererBase {
    
    public static final String OPTIONS_VISIBLE = "visible";

    private static final Map<String, ComponentAttribute> INPLACESELECT_HANDLER_ATTRIBUTES = Collections.unmodifiableMap(ComponentAttribute.createMap(
            new ComponentAttribute(HtmlConstants.ONCHANGE_ATTRIBUTE).setEventNames("change").
                setComponentAttributeName("onchange"),
            new ComponentAttribute(HtmlConstants.ONSELECT_ATTRIBUTE).setEventNames("select").
                setComponentAttributeName("onselect")
    ));

    @Override
    protected String getScriptName() {
        return "new RichFaces.ui.InplaceSelect";
    }

    public List<ClientSelectItem> getConvertedSelectItems(FacesContext facesContext, UIComponent component) {
        return SelectHelper.getConvertedSelectItems(facesContext, component);
    }
    
    public void encodeItems(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems) throws IOException {
        SelectHelper.encodeItems(facesContext, component, clientSelectItems, HtmlConstants.SPAN_ELEM);
    }
    
    @Override
    protected void renderInputHandlers(FacesContext facesContext, UIComponent component) throws IOException {
        RenderKitUtils.renderPassThroughAttributesOptimized(facesContext, component, INPLACESELECT_HANDLER_ATTRIBUTES);
    }
    
    public String getSelectInputLabel(FacesContext facesContext, UIComponent component) {
        return SelectHelper.getSelectInputLabel(facesContext, component);
    }
    
    @Override
    public void addToOptions(FacesContext facesContext, UIComponent component, Map<String, Object> options, Object additional) {
        options.put(PopupConstants.OPTIONS_ITEM_CLASS, "rf-is-opt");
        options.put(PopupConstants.OPTIONS_SELECT_ITEM_CLASS, "rf-is-sel");
        
        String clientId = component.getClientId(facesContext);
        options.put(PopupConstants.OPTIONS_LIST_CORD, clientId + "List");
        options.put(PopupConstants.OPTIONS_LIST_CLASS, component.getAttributes().get("listCss"));
        options.put(SelectHelper.OPTIONS_SELECT_ITEM_VALUE_INPUT, clientId + "selValue");
        options.put(SelectHelper.OPTIONS_LIST_ITEMS, additional);
        options.put(OPTIONS_VISIBLE, component.getAttributes().get("openOnEdit"));
    }

    public String getSelectLabel(FacesContext facesContext, UIComponent component) {
        AbstractInplaceSelect select = (AbstractInplaceSelect) component;
        String label = getSelectInputLabel(facesContext, select);
        if (!isDisable(getInplaceState(component)) && (label == null)) {
            label = select.getDefaultLabel();
        }
        return label;
    }
    
    public String getListStyles(FacesContext facesContext, UIComponent component) {
        AbstractInplaceSelect inplaceSelect = (AbstractInplaceSelect) component;
        return inplaceSelect.isOpenOnEdit() ? "" : "display: none"; 
    }

    public String getReadyStateCss() {
        return "rf-is-d-s";
    }

    public String getEditStateCss() {
        return "rf-is-e-s";
    }

    public String getChangedStateCss() {
        return "rf-is-c-s";
    }

    public String getDisableStateCss() {
    	return "rf-is-dis-s";
    }
    
    public String getEditCss() {
    	return "rf-is-edit";
    }

    public String getNoneCss() {
        return "rf-is-none";
    }
}
