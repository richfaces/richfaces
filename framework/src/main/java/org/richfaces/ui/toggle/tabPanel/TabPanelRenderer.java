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
package org.richfaces.ui.toggle.tabPanel;

import static org.richfaces.renderkit.RenderKitUtils.renderPassThroughAttributes;
import static org.richfaces.ui.common.HtmlConstants.CLASS_ATTRIBUTE;
import static org.richfaces.ui.common.HtmlConstants.DIV_ELEM;
import static org.richfaces.ui.common.HtmlConstants.ID_ATTRIBUTE;
import static org.richfaces.ui.common.HtmlConstants.SPAN_ELEM;
import static org.richfaces.ui.common.HtmlConstants.STYLE_ATTRIBUTE;
import static org.richfaces.ui.common.HtmlConstants.TBODY_ELEMENT;
import static org.richfaces.ui.common.HtmlConstants.TD_ELEM;
import static org.richfaces.ui.common.HtmlConstants.TR_ELEMENT;
import static org.richfaces.ui.toggle.AbstractTogglePanelTitledItem.HeaderStates.active;
import static org.richfaces.ui.toggle.AbstractTogglePanelTitledItem.HeaderStates.disabled;
import static org.richfaces.ui.toggle.AbstractTogglePanelTitledItem.HeaderStates.inactive;

import java.io.IOException;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.javascript.JSObject;
import org.richfaces.renderkit.RenderKitUtils;
import org.richfaces.ui.common.HtmlConstants;
import org.richfaces.ui.toggle.AbstractTogglePanelItemInterface;
import org.richfaces.ui.toggle.AbstractTogglePanelTitledItem;
import org.richfaces.ui.toggle.TogglePanelVisitCallback;
import org.richfaces.ui.toggle.TogglePanelVisitState;
import org.richfaces.ui.toggle.togglePanel.AbstractTogglePanel;
import org.richfaces.ui.toggle.togglePanel.TogglePanelRenderer;
import org.richfaces.util.HtmlUtil;

/**
 * @author akolonitsky
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "toggle/togglePanel/togglePanel.js"),
        @ResourceDependency(library = "org.richfaces", name = "toggle/tabPanel/tabPanel.js"),
        @ResourceDependency(library = "org.richfaces", name = "toggle/tabPanel/tabPanel.ecss") })
@JsfRenderer(type = "org.richfaces.ui.TabPanelRenderer", family = AbstractTabPanel.COMPONENT_FAMILY)
public class TabPanelRenderer extends TogglePanelRenderer {
    private static final RenderKitUtils.Attributes HEADER_ATTRIBUTES = RenderKitUtils.attributes()
            .generic("onclick", "onheaderclick", "headerclick").generic("ondblclick", "onheaderdblclick", "headerdblclick")
            .generic("onmousedown", "onheadermousedown", "headermousedown")
            .generic("onmousemove", "onheadermousemove", "headermousemove")
            .generic("onmouseup", "onheadermouseup", "headermouseup");
    private static final String DIV = DIV_ELEM;
    private static final String STYLE = STYLE_ATTRIBUTE;
    private static final String CLASS = CLASS_ATTRIBUTE;

    @Override
    protected boolean isSubmitted(FacesContext context, AbstractTogglePanel panel) {
        String activePanelName = panel.getSubmittedActiveItem();
        String clientId = panel.getClientIdByName(activePanelName);
        if (clientId == null) {
            return false;
        }
        Map<String, String> parameterMap = context.getExternalContext().getRequestParameterMap();
        return parameterMap.get(clientId) != null;
    }

    @Override
    protected void doEncodeBegin(ResponseWriter w, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(w, context, component);

        AbstractTabPanel tabPanel = (AbstractTabPanel) component;
        if (tabPanel.isHeaderPositionedTop()) {
            writeTabsLine(w, context, component);
            writeTabsLineSeparator(w, component);
        }
    }

    private void writeTabsLineSeparator(ResponseWriter writer, UIComponent component) throws IOException {
        writer.startElement(DIV, component);
        writer.writeAttribute(CLASS_ATTRIBUTE, "rf-tab-hdr-brd", null);
        writer.endElement(DIV);
    }

    private void writeTabsLine(ResponseWriter w, FacesContext context, UIComponent comp) throws IOException {
        w.startElement(DIV, comp);
        AbstractTabPanel tabPanel = (AbstractTabPanel) comp;
        if (tabPanel.isHeaderPositionedTop()) {
            w.writeAttribute(CLASS, "rf-tab-hdr-tabline-vis rf-tab-hdr-tabline-top", null);
        } else {
            w.writeAttribute(CLASS, "rf-tab-hdr-tabline-vis rf-tab-hdr-tabline-btm", null);
        }
        w.startElement("table", comp);
        w.writeAttribute(CLASS_ATTRIBUTE, "rf-tab-hdr-tabs", null);
        w.writeAttribute("cellspacing", "0", null);
        w.startElement(TBODY_ELEMENT, comp);
        w.startElement(TR_ELEMENT, comp);

        writeTopTabFirstSpacer(w, comp);

        writeTabLine(w, context, tabPanel);

        writeTopTabLastSpacer(w, comp);

        w.endElement(TR_ELEMENT);
        w.endElement(TBODY_ELEMENT);
        w.endElement("table");

        writeTopTabsControl(w, comp, "rf-tab-hdr-scrl-lft rf-tab-hdn", "\u00AB");
        writeTopTabsControl(w, comp, "rf-tab-hdr-tablst rf-tab-hdn", "\u2193");
        writeTopTabsControl(w, comp, "rf-tab-hdr-scrl-rgh rf-tab-hdn", "\u00BB");

        w.endElement(DIV_ELEM);
    }

    private void writeTabLine(final ResponseWriter w, final FacesContext context, final AbstractTabPanel panel) throws IOException {
        panel.visitTogglePanelItems(panel, new TogglePanelVisitCallback() {
            @Override
            public VisitResult visit(FacesContext context, TogglePanelVisitState visitState) {
                AbstractTogglePanelItemInterface item = visitState.getItem();
                if (item.isRendered() && item instanceof AbstractTab) {
                    try {
                        AbstractTab tab = (AbstractTab) item;
                        writeTopTabHeader(context, w, tab);
                        writeTopTabSpacer(w, panel);
                    } catch (IOException e) {
                        throw new FacesException(e);
                    }
                }
                return VisitResult.ACCEPT;
            }
        });
    }

    @Override
    protected String getStyle(UIComponent component) {
        return attributeAsString(component, "style");
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return HtmlUtil.concatClasses("rf-tbp", attributeAsString(component, "styleClass"));
    }

    private void writeTopTabHeader(FacesContext context, ResponseWriter writer, AbstractTab tab) throws IOException {
        boolean isActive = tab.isActive();
        boolean isDisabled = tab.isDisabled();
        // TODO: Ilya, review. Much HTML because we always encoding all states. Need to optimize somehow.
        encodeTabHeader(context, tab, writer, inactive, !isActive && !isDisabled);
        encodeTabHeader(context, tab, writer, active, isActive && !isDisabled);
        encodeTabHeader(context, tab, writer, disabled, isDisabled);
    }

    private String positionAbbreviation(AbstractTab tab) {
        if (tab.getParentPanel().isHeaderPositionedTop()) {
            return "top";
        } else {
            return "btm";
        }
    }

    private void encodeTabHeader(FacesContext context, AbstractTab tab, ResponseWriter writer,
            AbstractTogglePanelTitledItem.HeaderStates state, Boolean isDisplay) throws IOException {

        String headerStateClass = "rf-tab-hdr-" + state.abbreviation();
        String headerPositionClass = "rf-tab-hdr-" + positionAbbreviation(tab);

        writer.startElement(TD_ELEM, tab);
        writer.writeAttribute(ID_ATTRIBUTE, tab.getClientId(context) + ":header:" + state.toString(), null);
        renderPassThroughAttributes(context, tab, HEADER_ATTRIBUTES);
        writer.writeAttribute(
                CLASS_ATTRIBUTE,
                concatClasses("rf-tab-hdr", headerStateClass, headerPositionClass,
                        attributeAsString(tab, "headerClass"), attributeAsString(tab, state.headerClass())), null);
        writer.writeAttribute(STYLE_ATTRIBUTE,
                concatStyles(isDisplay ? "" : "display : none", attributeAsString(tab, "headerStyle")), null);

        writer.startElement(SPAN_ELEM, tab);
        writer.writeAttribute(CLASS_ATTRIBUTE, "rf-tab-lbl", null);

        UIComponent headerFacet = tab.getHeaderFacet(state);
        if (headerFacet != null && headerFacet.isRendered()) {
            headerFacet.encodeAll(context);
        } else {
            Object headerText = tab.getAttributes().get("header");
            if (headerText != null && !headerText.equals("")) {
                writer.writeText(headerText, null);
            }
        }

        writer.endElement(SPAN_ELEM);

        writer.endElement(TD_ELEM);
    }

    private void writeTopTabsControl(ResponseWriter w, UIComponent comp, String styles, String text) throws IOException {
        w.startElement(DIV_ELEM, comp);
        w.writeAttribute(CLASS_ATTRIBUTE, styles, null);
        w.writeText(text, null);
        w.endElement(DIV_ELEM);
    }

    private void writeTopTabFirstSpacer(ResponseWriter w, UIComponent comp) throws IOException {
        AbstractTabPanel tabPanel = (AbstractTabPanel) comp;
        if (tabPanel.isHeaderAlignedLeft()) {
            writeTopTabSpacer(w, comp, "padding-left: 5px;", "rf-tab-hdr-spcr");
        } else {
            writeTopTabSpacer(w, comp, "padding-left: 5px; width:100%", "rf-tab-hdr-spcr");
        }
    }

    private void writeTopTabSpacer(ResponseWriter w, UIComponent comp) throws IOException {
        writeTopTabSpacer(w, comp, "", "rf-tab-hdr-spcr rf-tab-hortab-tabspcr-wdh");
    }

    private void writeTopTabLastSpacer(ResponseWriter w, UIComponent comp) throws IOException {
        AbstractTabPanel tabPanel = (AbstractTabPanel) comp;
        if (tabPanel.isHeaderAlignedLeft()) {
            writeTopTabSpacer(w, comp, "padding-right: 5px; width: 100%;", "rf-tab-hdr-spcr");
        } else {
            writeTopTabSpacer(w, comp, "padding-right: 5px;", "rf-tab-hdr-spcr");
        }
    }

    private void writeTopTabSpacer(ResponseWriter w, UIComponent comp, String style, String styleClass) throws IOException {
        w.startElement(TD_ELEM, comp);
        w.writeAttribute(STYLE, style, null);
        w.writeAttribute(CLASS, styleClass, null);
        w.write("<br />");
        w.endElement(TD_ELEM);
    }

    @Override
    protected void doEncodeEnd(final ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        AbstractTabPanel panel = (AbstractTabPanel) component;
        if (!panel.isHeaderPositionedTop()) {
            writeTabsLineSeparator(writer, component);
            writeTabsLine(writer, context, component);
        }
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.TabPanel", component.getClientId(context), getScriptObjectOptions(context, component));
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
