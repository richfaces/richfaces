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

package org.richfaces.renderkit.html;

import org.ajax4jsf.javascript.JSObject;
import org.richfaces.component.AbstractCollapsiblePanel;
import org.richfaces.component.AbstractTogglePanel;
import org.richfaces.component.AbstractTogglePanelTitledItem;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.richfaces.component.AbstractCollapsiblePanel.States.*;
import static org.richfaces.component.util.HtmlUtil.concatClasses;
import static org.richfaces.component.util.HtmlUtil.concatStyles;

/**
 * @author akolonitsky
 * @since 2010-08-27
 */
@ResourceDependencies( {
    @ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-event.js"),
    @ResourceDependency(name = "richfaces-base-component.js"),
    @ResourceDependency(library = "org.richfaces", name = "TogglePanel.js"),
    @ResourceDependency(library = "org.richfaces", name = "TogglePanelItem.js"),
    @ResourceDependency(library = "org.richfaces", name = "CollapsiblePanel.js"),
    @ResourceDependency(library = "org.richfaces", name = "CollapsiblePanelItem.js"),
    @ResourceDependency(library = "org.richfaces", name = "collapsiblePanel.ecss") })
public class CollapsiblePanelRenderer extends TogglePanelRenderer {

    public static final String SWITCH = "switch";
    public static final String BEFORE_SWITCH = "beforeswitch";

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        AbstractTogglePanel panel = (AbstractTogglePanel) component;

        Map<String, String> requestMap =
              context.getExternalContext().getRequestParameterMap();

        // Don't overwrite the value unless you have to!
        String newValue = requestMap.get(getValueRequestParamName(context, component));
        if (newValue != null) {
            panel.setSubmittedActiveItem(newValue);
        }

        String compClientId = component.getClientId(context);
        String clientId = requestMap.get(compClientId);
        if (clientId != null && clientId.equals(compClientId)) {
            String itemClientId = clientId + (Boolean.parseBoolean(newValue) ? ":content" : ":empty");
            context.getPartialViewContext().getRenderIds().add(itemClientId);

            //TODO nick - this should be done on encode, not on decode
            addOnCompleteParam(newValue, panel.getClientId());
        }
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent comp) throws IOException {
        super.doEncodeBegin(writer, context, comp);

        encodeHeader(context, comp, writer);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return concatClasses("rf-cp", super.getStyleClass(component));
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.CollapsiblePanel",
            component.getClientId(), getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        AbstractTogglePanel panel = (AbstractTogglePanel) component;

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("activeItem", panel.getActiveItem());
        options.put("ajax", getAjaxOptions(context, panel));
        options.put("switchMode", panel.getSwitchType());

        TogglePanelRenderer.addEventOption(context, panel, options, SWITCH);
        TogglePanelRenderer.addEventOption(context, panel, options, BEFORE_SWITCH);

        return options;
    }

    private void encodeHeader(FacesContext context, UIComponent component, ResponseWriter writer) throws IOException {
        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(context) + ":header", null);
        writer.writeAttribute("class", concatClasses("rf-cp-hr", attributeAsString(component, "headerClass")), null);

        AbstractCollapsiblePanel panel = (AbstractCollapsiblePanel) component;
        encodeHeader(context, component, writer, expanded, panel.isExpanded());
        encodeHeader(context, component, writer, collapsed, !panel.isExpanded());

        writer.endElement("div");
    }
    
    private void encodeHeader(FacesContext context, UIComponent component, ResponseWriter responseWriter, AbstractCollapsiblePanel.States state, boolean isVisible) throws IOException {
        responseWriter.startElement("div", component);
        responseWriter.writeAttribute("class", "rf-cp-hr-" + state, null);
        responseWriter.writeAttribute("style", concatStyles(styleElement("display", isVisible ? "" : "none"), attributeAsString(component, "headerClass")), null);

        UIComponent header = AbstractTogglePanelTitledItem.getHeaderFacet(component, state);
        if (header != null && header.isRendered()) {
            header.encodeAll(context);
        } else {
            String headerText = (String) component.getAttributes().get("header");
            if (headerText != null && !headerText.isEmpty()) {
                responseWriter.writeText(headerText, null);
            }
        }
        
        responseWriter.endElement("div");
    }

    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component)
        throws IOException {

        AbstractCollapsiblePanel panel = (AbstractCollapsiblePanel) component;

        encodeContentChild(writer, context, component, panel);
        encodeEmptyChild(writer, context, component, panel);
    }

    private void encodeContentChild(ResponseWriter writer, FacesContext context, UIComponent component, AbstractCollapsiblePanel panel) throws IOException {
        if (panel.isExpanded()) {
            encodeContent(writer, context, component, true);
        } else {
            switch (panel.getSwitchType()) {
                case client:
                    encodeContent(writer, context, component, false);
                    break;

                case ajax:
                    context.getResponseWriter().write(getPlaceHolder(panel.getClientId() + ":content"));
                    break;

                case server:
                    // Do nothing.
                    break;

                default:
                    throw new IllegalStateException("Unknown switch type : " + panel.getSwitchType());
            }
        }
    }

    private void encodeEmptyChild(ResponseWriter writer, FacesContext context, UIComponent component, AbstractCollapsiblePanel panel) throws IOException {
        if (!panel.isExpanded()) {
            encodeEmptyDiv(writer, context, component, true);
        } else {
            switch (panel.getSwitchType()) {
                case client:
                    encodeEmptyDiv(writer, context, component, false);
                    break;

                case ajax:
                    context.getResponseWriter().write(getPlaceHolder(panel.getClientId() + ":empty"));
                    break;

                case server:
                    // Do nothing.
                    break;

                default:
                    throw new IllegalStateException("Unknown switch type : " + panel.getSwitchType());
            }
        }
    }

    private String getPlaceHolder(String id) {
        return "<div id=\"" + id + "\" style=\"display: none\" ></div>";
    }

    private void encodeContent(ResponseWriter writer, FacesContext context, UIComponent component, boolean visible) throws IOException {
        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId() + ":content", null);
        writer.writeAttribute("class", concatClasses("rf-cp-b", attributeAsString(component, "bodyClass")), null);
        writer.writeAttribute("style", concatStyles(visible ? "" : "none", attributeAsString(component, "style")), null);

        renderChildren(context, component);

        writer.endElement("div");
    }

    private void encodeEmptyDiv(ResponseWriter writer, FacesContext context, UIComponent component, boolean visible) throws IOException {
        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId() + ":empty", null);
        writer.writeAttribute("class", "rf-cp-empty", null);
        writer.writeAttribute("style", styleElement("display", visible ? "" : "none"), null);
        writer.endElement("div");
    }

    @Override
    protected List<JSObject> getChildrenScriptObjects(FacesContext context, UIComponent component) {
        return null;
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractCollapsiblePanel.class;
    }
}

