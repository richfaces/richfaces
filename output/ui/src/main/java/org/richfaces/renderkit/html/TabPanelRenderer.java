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
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractTabPanel;
import org.richfaces.component.AbstractTogglePanel;
import org.richfaces.component.AbstractTogglePanelItem;
import org.richfaces.component.AbstractTogglePanelTitledItem;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RenderKitUtils;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

import static org.richfaces.component.AbstractTogglePanelTitledItem.HeaderStates.*;
import static org.richfaces.renderkit.HtmlConstants.*;
import static org.richfaces.renderkit.RenderKitUtils.renderPassThroughAttributes;

/**
 * @author akolonitsky
 * @since 2010-08-24
 */
@ResourceDependencies( { // TODO review
    @ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-event.js"),
    @ResourceDependency(name = "richfaces-base-component.js"),
    @ResourceDependency(library = "org.richfaces", name = "tabPanel.ecss"),
    @ResourceDependency(library = "org.richfaces", name = "togglePanel.js"),
    @ResourceDependency(library = "org.richfaces", name = "tabPanel.js")
})
@JsfRenderer(type = "org.richfaces.TabPanelRenderer", family = AbstractTabPanel.COMPONENT_FAMILY)
public class TabPanelRenderer extends TogglePanelRenderer {

    private static final RenderKitUtils.Attributes HEADER_ATTRIBUTES = RenderKitUtils.attributes()
        .generic("onclick", "onheaderclick", "headerclick")
        .generic("ondblclick", "onheaderdblclick", "headerdblclick")
        .generic("onmousedown", "onheadermousedown", "headermousedown")
        .generic("onmousemove", "onheadermousemove", "headermousemove")
        .generic("onmouseup", "onheadermouseup", "headermouseup");

    private static final String DIV = DIV_ELEM;
    private static final String STYLE = STYLE_ATTRIBUTE;
    private static final String CLASS = CLASS_ATTRIBUTE;

    @Override
    protected void doEncodeBegin(ResponseWriter w, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(w, context, component);
        writeJavaScript(w, context, component);

        writeTabsLine(w, context, component);
        writeTabsLineSeparator(w);
    }

    private void writeTabsLineSeparator(ResponseWriter writer) throws IOException {
        writer.write("<div class='rf-tb-hdr_brd'></div>");
    }

    private void writeTabsLine(ResponseWriter w, FacesContext context, UIComponent comp) throws IOException {
        w.startElement(DIV, comp);
        w.writeAttribute(CLASS, "rf-tb-hdr-tabline-vis", null);

        w.startElement("table", comp);
        w.writeAttribute(CLASS_ATTRIBUTE, "rf-tb-hdr-tabs", null);
        w.writeAttribute("cellspacing", "0", null);
        w.startElement(TBODY_ELEMENT, comp);
        w.startElement(TR_ELEMENT, comp);

        writeTopTabFirstSpacer(w, comp);

        for (AbstractTogglePanelItem item : ((AbstractTogglePanel) comp).getRenderedItems()) {
            AbstractTogglePanelTitledItem tab = (AbstractTogglePanelTitledItem) item;
            writeTopTabHeader(context, w, tab);
            writeTopTabSpacer(w, comp);
        }
        
        writeTopTabLastSpacer(w, comp);

        w.endElement(TR_ELEMENT);
        w.endElement(TBODY_ELEMENT);
        w.endElement("table");

        writeTopTabsControl(w, comp, "rf-tb-hdr-scrl_l rf-tb-hdn", "\u00AB");
        writeTopTabsControl(w, comp, "rf-tb-hdr-tablst rf-tb-hdn", "\u2193");
        writeTopTabsControl(w, comp, "rf-tb-hdr-scrl_r rf-tb-hdn", "\u00BB");

        w.endElement(DIV_ELEM);
    }

    @Override
    protected String getStyle(UIComponent component) {
        return concatStyles(attributeAsString(component, "style"), "width: 100%;");
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return HtmlUtil.concatClasses("rf-tbp", attributeAsString(component, "styleClass"));
    }

    private void writeTopTabHeader(FacesContext context, ResponseWriter writer, AbstractTogglePanelTitledItem tab) throws IOException {
        boolean isActive = tab.isActive();
        boolean isDisabled = tab.isDisabled();
        
        encodeTabHeader(context, tab, writer, inactive, !isActive && !isDisabled);
        encodeTabHeader(context, tab, writer, active, isActive && !isDisabled);
        encodeTabHeader(context, tab, writer, disabled, isDisabled);
        
    }    

    private void encodeTabHeader(FacesContext context, AbstractTogglePanelTitledItem tab, ResponseWriter writer,
                              AbstractTogglePanelTitledItem.HeaderStates state, Boolean isDisplay) throws IOException {

        
        writer.startElement(TD_ELEM, tab);
        writer.writeAttribute(ID_ATTRIBUTE, tab.getClientId() + ":header:" + state.toString(), null);
        renderPassThroughAttributes(context, tab, HEADER_ATTRIBUTES);
        writer.writeAttribute(CLASS_ATTRIBUTE, concatClasses("rf-tb-hdr rf-tb-hdr-" + state.abbreviation(),
                attributeAsString(tab, "headerClass"), attributeAsString(tab, state.headerClass())), null);
        writer.writeAttribute(STYLE_ATTRIBUTE, concatStyles(isDisplay ? "" : "display : none", attributeAsString(tab, "headerStyle")), null);

        writer.startElement("span", tab);
        writer.writeAttribute(CLASS_ATTRIBUTE, "rf-tb-lbl", null);

        UIComponent headerFacet = tab.getHeaderFacet(state);
        if (headerFacet != null && headerFacet.isRendered()) {
            headerFacet.encodeAll(context);
        } else {
            Object headerText = tab.getAttributes().get("header");
            if (headerText != null && !headerText.equals("")) {
                writer.writeText(headerText, null);
            }
        }

        writer.endElement("span");

        writer.endElement(TD_ELEM);
    }



    private void writeTopTabsControl(ResponseWriter w, UIComponent comp, String styles, String text) throws IOException {
        w.startElement(DIV_ELEM, comp);
        w.writeAttribute(CLASS_ATTRIBUTE, styles, null);
        w.writeText(text, null);
        w.endElement(DIV_ELEM);
    }

    private void writeTopTabFirstSpacer(ResponseWriter w, UIComponent comp) throws IOException {
        writeTopTabSpacer(w, comp, "padding-left: 5px;", "rf-tb-hdr-spcr");
    }

    private void writeTopTabSpacer(ResponseWriter w, UIComponent comp) throws IOException {
        writeTopTabSpacer(w, comp, "", "rf-tb-hdr-spcr rf-tb-hortab-tabspcr_wdh");
    }

    private void writeTopTabLastSpacer(ResponseWriter w, UIComponent comp) throws IOException {
        writeTopTabSpacer(w, comp, "padding-right: 5px; width: 100%;", "rf-tb-hdr-spcr");
    }

    private void writeTopTabSpacer(ResponseWriter w, UIComponent comp, String style, String classStyle) throws IOException {
        w.startElement(TD_ELEM, comp);
        w.writeAttribute(STYLE, style, null);
        w.writeAttribute(CLASS, classStyle, null);
        w.write("<br />");
        w.endElement(TD_ELEM);
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.TabPanel",
            component.getClientId(), getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        Map<String, Object> options = super.getScriptObjectOptions(context, component);
        options.put("isKeepHeight", attributeAsString(component, "height").length() > 0);

        return options;
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractTabPanel.class;
    }
}
