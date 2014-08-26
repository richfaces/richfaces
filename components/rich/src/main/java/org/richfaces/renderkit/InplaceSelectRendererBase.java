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
import java.util.List;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractInplaceSelect;
import org.richfaces.component.InplaceComponent;
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
        @ResourceDependency(library = "org.richfaces", name = "jquery.position.js"), @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-selection.js"),
        @ResourceDependency(library = "org.richfaces", name = "inputBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "popup.js"),
        @ResourceDependency(library = "org.richfaces", name = "list.js"),
        @ResourceDependency(library = "org.richfaces", name = "popupList.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceInput.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceSelect.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceSelect.ecss") })
public class InplaceSelectRendererBase extends InplaceInputRendererBase {
    public static final String ITEM_CSS = "rf-is-opt";

    public List<ClientSelectItem> getConvertedSelectItems(FacesContext facesContext, UIComponent component) {
        return SelectHelper.getConvertedSelectItems(facesContext, component);
    }

    public void encodeItems(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems)
            throws IOException {
        SelectHelper.encodeItems(facesContext, component, clientSelectItems, HtmlConstants.SPAN_ELEM, ITEM_CSS);
    }

    public void renderListHandlers(FacesContext facesContext, UIComponent component) throws IOException {
        RenderKitUtils.renderPassThroughAttributesOptimized(facesContext, component,
                SelectHelper.SELECT_LIST_HANDLER_ATTRIBUTES);
    }

    @Override
    public void renderInputHandlers(FacesContext facesContext, UIComponent component) throws IOException {
        RenderKitUtils.renderPassThroughAttributesOptimized(facesContext, component, INPLACE_INPUT_HANDLER_ATTRIBUTES);
    }

    public String getSelectInputLabel(FacesContext facesContext, UIComponent component) {
        return SelectHelper.getSelectInputLabel(facesContext, component);
    }

    public String getListWidth(UIComponent component) {
        AbstractInplaceSelect select = (AbstractInplaceSelect) component;
        String width = getListWidth(select);
        return (width != null && width.trim().length() != 0) ? ("width: " + width) : "";
    }

    protected String getListWidth(AbstractInplaceSelect select) {
        String width = HtmlDimensions.formatSize(select.getListWidth());
        if (width == null || width.length() == 0) {
            width = "200px";
        }
        return width;
    }

    protected String getListHeight(AbstractInplaceSelect select) {
        String height = HtmlDimensions.formatSize(select.getListHeight());
        if (height == null || height.length() == 0) {
            height = "100px";
        }
        return height;
    }

    public String getListHeight(UIComponent component) {
        AbstractInplaceSelect select = (AbstractInplaceSelect) component;
        String height = getListHeight(select);
        return (height != null && height.trim().length() != 0) ? ("height: " + height) : "";
    }

    public String getSelectLabel(FacesContext facesContext, UIComponent component) {
        AbstractInplaceSelect select = (AbstractInplaceSelect) component;
        String label = getSelectInputLabel(facesContext, select);
        if (!select.isDisabled() && (label == null)) {
            label = select.getDefaultLabel();
        }
        return label;
    }

    public String getContainerStyleClasses(UIComponent component) {
        InplaceComponent inplaceComponent = (InplaceComponent) component;
        String style = concatClasses("rf-is", component.getAttributes().get("styleClass"));
        if (inplaceComponent.isDisabled()) {
            style = concatClasses(style, getDisableStateCss(inplaceComponent));
        }
        style = concatClasses(style, component.getAttributes().get("placeHolderStyleClass"));
        return style;
    }

    public String getEditStateCss(InplaceComponent component) {
        String css = component.getActiveClass();
        return concatClasses("rf-is-act", css);
    }

    public String getChangedStateCss(InplaceComponent component) {
        String css = component.getChangedClass();
        return concatClasses("rf-is-chng", css);
    }

    public String getDisableStateCss(InplaceComponent component) {
        String css = component.getDisabledClass();
        return concatClasses("rf-is-dis", css);
    }

    public String getDisabledCss(UIComponent component) {
        AbstractInplaceSelect inplaceSelect = (AbstractInplaceSelect) component;
        return getDisableStateCss(inplaceSelect);
    }

    public String getEditCss(InplaceComponent component) {
        return "rf-is-fld-cntr";
    }

    public String getNoneCss(InplaceComponent component) {
        return "rf-is-none";
    }

    public String getListCss(UIComponent component) {
        AbstractInplaceSelect inplaceSelect = (AbstractInplaceSelect) component;
        String css = inplaceSelect.getListClass();
        css = (css != null) ? concatClasses("rf-is-lst-cord", css) : "rf-is-lst-cord";
        return css;
    }

    protected String getInputWidthStyle(UIComponent component) {
        String value = ((AbstractInplaceSelect) component).getInputWidth();
        if (value == null || "".equals(value)) {
            return "";
        } else {
            return "width: " + HtmlDimensions.formatSize(value) + ";";
        }
    }
}
