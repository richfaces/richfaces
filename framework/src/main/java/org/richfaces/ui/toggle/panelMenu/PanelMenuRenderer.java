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
package org.richfaces.ui.toggle.panelMenu;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.javascript.JSObject;
import org.richfaces.ui.common.DivPanelRenderer;
import org.richfaces.ui.common.HtmlConstants;
import org.richfaces.util.HtmlUtil;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.richfaces.ui.toggle.togglePanel.TogglePanelRenderer.getAjaxOptions;
import static org.richfaces.ui.toggle.togglePanel.TogglePanelRenderer.getValueRequestParamName;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
@JsfRenderer(type = "org.richfaces.ui.PanelMenuRenderer", family = AbstractPanelMenu.COMPONENT_FAMILY)
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library="org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "toggle/panelMenu/panelMenu.js"),
        @ResourceDependency(library = "org.richfaces", name = "toggle/panelMenu/panelMenuItem.js"),
        @ResourceDependency(library = "org.richfaces", name = "toggle/panelMenu/panelMenuGroup.js"),
        @ResourceDependency(library = "org.richfaces", name = "toggle/icons.ecss"),
        @ResourceDependency(library = "org.richfaces", name = "toggle/panelMenu/panelMenu.ecss") })
public class PanelMenuRenderer extends DivPanelRenderer {
    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        AbstractPanelMenu panelMenu = (AbstractPanelMenu) component;

        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();

        // Don't overwrite the value unless you have to!
        String newValue = requestMap.get(getValueRequestParamName(context, component));
        if (newValue != null) {
            panelMenu.setSubmittedActiveItem(newValue);
        }
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(writer, context, component);

        AbstractPanelMenu panelMenu = (AbstractPanelMenu) component;

        writer.startElement(HtmlConstants.INPUT_ELEM, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, getValueRequestParamName(context, component), null);
        writer.writeAttribute(HtmlConstants.NAME_ATTRIBUTE, getValueRequestParamName(context, component), null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.INPUT_TYPE_HIDDEN, null);
        writer.writeAttribute(HtmlConstants.VALUE_ATTRIBUTE, panelMenu.getActiveItem(), null);
        writer.endElement(HtmlConstants.INPUT_ELEM);

        writeJavaScript(writer, context, component);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return concatClasses("rf-pm", attributeAsString(component, "styleClass"));
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.PanelMenu", component.getClientId(context),
            getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        AbstractPanelMenu panelMenu = (AbstractPanelMenu) component;

        Map<String, Object> options = new HashMap<String, Object>();
        // TODO nick - only options with non-default values should be rendered
        options.put("ajax", getAjaxOptions(context, panelMenu));
        options.put("disabled", panelMenu.isDisabled());
        options.put("expandSingle", panelMenu.isExpandSingle());
        options.put("bubbleSelection", panelMenu.isBubbleSelection());
        return options;
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractPanelMenu.class;
    }

    @Override
    protected String getStyle(UIComponent component) {
        return HtmlUtil.concatStyles(super.getStyle(component), attributeAsStyle(component, "width"));
    }
}
