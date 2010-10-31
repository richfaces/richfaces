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

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.javascript.JSObject;
import org.richfaces.component.AbstractPanelMenuGroup;
import org.richfaces.component.html.HtmlPanelMenuGroup;
import org.richfaces.renderkit.HtmlConstants;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.richfaces.component.util.HtmlUtil.concatClasses;
import static org.richfaces.renderkit.html.TogglePanelRenderer.addEventOption;
import static org.richfaces.renderkit.html.TogglePanelRenderer.getAjaxOptions;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
public class PanelMenuGroupRenderer extends DivPanelRenderer {

    public static final String COLLAPSE = "collapse";
    public static final String EXPAND = "expand";
    public static final String SWITCH = "switch";
    public static final String BEFORE_COLLAPSE = "beforecollapse";
    public static final String BEFORE_EXPAND = "beforeexpand";
    public static final String BEFORE_SWITCH = "beforeswitch";


    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        AbstractPanelMenuGroup menuGroup = (AbstractPanelMenuGroup) component;

        Map<String, String> requestMap =
              context.getExternalContext().getRequestParameterMap();

        // Don't overwrite the value unless you have to!
        String newValue = requestMap.get(component.getClientId() + ":expanded");
        if (newValue != null) {
            menuGroup.setSubmittedExpanded(newValue);
        }

        String compClientId = component.getClientId(context);
        String clientId = requestMap.get(compClientId);
        if (clientId != null && clientId.equals(compClientId)) {
            context.getPartialViewContext().getRenderIds().add(clientId);

            //TODO nick - this should be done on encode, not on decode
            addOnCompleteParam(clientId);
        }
    }

    protected static void addOnCompleteParam(String itemId) {
        AjaxContext.getCurrentInstance().appendOncomplete(new StringBuilder()
            .append("RichFaces.$('").append(itemId).append("').onCompleteHandler();").toString());
    }

    

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(writer, context, component);

        HtmlPanelMenuGroup menuGroup = (HtmlPanelMenuGroup) component;

        writer.startElement(HtmlConstants.INPUT_ELEM, component);

        final String expanded = component.getClientId(context) + ":expanded";
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, expanded, null);
        writer.writeAttribute(HtmlConstants.NAME_ATTRIBUTE, expanded, null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.INPUT_TYPE_HIDDEN, null);
        writer.writeAttribute(HtmlConstants.VALUE_ATTRIBUTE, String.valueOf(menuGroup.isExpanded()), null);
        writer.endElement(HtmlConstants.INPUT_ELEM);

        encodeHeader(writer, context, menuGroup);
        encodeContentBegin(writer, context, menuGroup);
    }

    private void encodeHeader(ResponseWriter writer, FacesContext context, HtmlPanelMenuGroup menuGroup) throws IOException {
        writer.startElement("div", null);
        writer.writeAttribute("id", menuGroup.getClientId(context) + ":hdr", null);
        writer.writeAttribute("class", "rf-pm-gr-hdr", null);
        writer.writeText(menuGroup.getLabel(), null);
        writer.endElement("div");
    }

    private void encodeContentBegin(ResponseWriter writer, FacesContext context, HtmlPanelMenuGroup menuGroup) throws IOException {
        writer.startElement("div", null);
        writer.writeAttribute("id", menuGroup.getClientId(context) + ":cnt", null);
        writer.writeAttribute("class", concatClasses("rf-pm-gr-cnt", menuGroup.isExpanded() ? "rf-pm-exp" : "rf-pm-colps"), null);
    }

    private void encodeContentEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        writer.endElement("div");
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return concatClasses("rf-pm-gr", attributeAsString(component, "styleClass"));
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.PanelMenuGroup",
            component.getClientId(), getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        HtmlPanelMenuGroup panelMenuGroup = (HtmlPanelMenuGroup) component;

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("ajax", getAjaxOptions(context, panelMenuGroup));
        options.put("name", panelMenuGroup.getName());
        options.put("mode", panelMenuGroup.getMode());
        options.put("expandEvent", panelMenuGroup.getExpandEvent());
        options.put("collapseEvent", panelMenuGroup.getCollapseEvent());
        options.put("expandSingle", panelMenuGroup.isExpandSingle());
        options.put("bubbleSelection", panelMenuGroup.getBubbleSelection());
        options.put("expanded", panelMenuGroup.isExpanded());

        addEventOption(context, panelMenuGroup, options, COLLAPSE);
        addEventOption(context, panelMenuGroup, options, EXPAND);
        addEventOption(context, panelMenuGroup, options, SWITCH);
        addEventOption(context, panelMenuGroup, options, BEFORE_COLLAPSE);
        addEventOption(context, panelMenuGroup, options, BEFORE_EXPAND);
        addEventOption(context, panelMenuGroup, options, BEFORE_SWITCH);

        return options;
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        encodeContentEnd(writer, context, component);

        super.doEncodeEnd(writer, context, component);
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractPanelMenuGroup.class;
    }
}

