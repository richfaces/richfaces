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
import org.richfaces.component.AbstractPanelMenuGroup;
import org.richfaces.component.AbstractPanelMenuItem;
import org.richfaces.component.html.HtmlPanelMenuGroup;
import org.richfaces.renderkit.RenderKitUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.richfaces.renderkit.HtmlConstants.*;
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
    private static final String CSS_CLASS_PREFIX = "rf-pm-gr";

    //TODO nick - shouldn't this be rf-pm-gr-top?
    private static final String TOP_CSS_CLASS_PREFIX = "rf-pm-top-gr";

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        AbstractPanelMenuGroup menuGroup = (AbstractPanelMenuGroup) component;

        Map<String, String> requestMap =
              context.getExternalContext().getRequestParameterMap();

        // Don't overwrite the value unless you have to!
        //TODO! nick - ":expanded" suffix is not necessary
        String newValue = requestMap.get(component.getClientId() + ":expanded");
        if (newValue != null) {
            menuGroup.setSubmittedExpanded(newValue);
        }

        String clientId = component.getClientId(context);
        if (requestMap.get(clientId) != null) {
            context.getPartialViewContext().getRenderIds().add(clientId);

            //TODO nick - this should be done on encode, not on decode
            PanelMenuRenderer.addOnCompleteParam(clientId);
        }
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(writer, context, component);

        HtmlPanelMenuGroup menuGroup = (HtmlPanelMenuGroup) component;

        writer.startElement(INPUT_ELEM, component);

        //TODO nick - there is no need to encode this input - group state can be extracted from class
        final String expanded = component.getClientId(context) + ":expanded";
        writer.writeAttribute(ID_ATTRIBUTE, expanded, null);
        writer.writeAttribute(NAME_ATTRIBUTE, expanded, null);
        writer.writeAttribute(TYPE_ATTR, INPUT_TYPE_HIDDEN, null);
        writer.writeAttribute(VALUE_ATTRIBUTE, String.valueOf(menuGroup.isExpanded()), null);
        writer.endElement(INPUT_ELEM);

        encodeHeader(writer, context, menuGroup);
        encodeContentBegin(writer, context, menuGroup);
    }

    private void encodeHeader(ResponseWriter writer, FacesContext context, HtmlPanelMenuGroup menuGroup) throws IOException {
        writer.startElement(DIV_ELEM, null);
        writer.writeAttribute(ID_ATTRIBUTE, menuGroup.getClientId(context) + ":hdr", null);
        writer.writeAttribute(CLASS_ATTRIBUTE, getCssClass(menuGroup, "-hdr"), null);
        encodeHeaderGroup(writer, context, menuGroup, getCssClass(menuGroup, ""));
        writer.endElement(DIV_ELEM);
    }

    private void encodeHeaderGroup(ResponseWriter writer, FacesContext context, HtmlPanelMenuGroup menuItem, String classPrefix) throws IOException {
        writer.startElement(TABLE_ELEMENT, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, classPrefix + "-gr", null);
        writer.startElement(TBODY_ELEMENT, null);
        writer.startElement(TR_ELEMENT, null);

        encodeHeaderGroupIconLeft(writer, context, menuItem, classPrefix);

        writer.startElement(TD_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, classPrefix + "-lbl", null);

        UIComponent headerFacet = menuItem.getFacet("label");
        if (headerFacet != null && headerFacet.isRendered()) {
            headerFacet.encodeAll(context);
        } else {
            Object label = menuItem.getLabel();
            if (label != null && !label.equals("")) {
                writer.writeText(label, null);
            }
        }

        writer.endElement(TD_ELEM);

        encodeHeaderGroupIconRight(writer, context, menuItem, classPrefix);

        writer.endElement(TR_ELEMENT);
        writer.endElement(TBODY_ELEMENT);
        writer.endElement(TABLE_ELEMENT);
    }

    private void encodeHeaderGroupIconLeft(ResponseWriter writer, FacesContext context, HtmlPanelMenuGroup menuGroup, String classPrefix) throws IOException {
        String iconCollapsed = menuGroup.isDisabled() ? menuGroup.getIconLeftDisabled() : menuGroup.getIconLeftCollapsed();
        String iconExpanded = menuGroup.isDisabled() ? menuGroup.getIconLeftDisabled() : menuGroup.getIconLeftExpanded();

        encodeTdIcon(writer, context, classPrefix + "-ico", menuGroup.isExpanded(), iconCollapsed, iconExpanded);
    }

    private void encodeHeaderGroupIconRight(ResponseWriter writer, FacesContext context, HtmlPanelMenuGroup menuItem, String classPrefix) throws IOException {
        String iconCollapsed = menuItem.isDisabled() ? menuItem.getIconRightDisabled() : menuItem.getIconRightCollapsed();
        String iconExpanded = menuItem.isDisabled() ? menuItem.getIconRightDisabled() : menuItem.getIconRightExpanded();

        //TODO nick - should this be "-ico-exp"? also why expanded icon state is connected with right icon alignment?
        encodeTdIcon(writer, context, classPrefix + "-exp-ico", menuItem.isExpanded(), iconCollapsed, iconExpanded);
    }

    private void encodeTdIcon(ResponseWriter writer, FacesContext context, String cssClass, boolean isExpanded, String attrIconCollapsedValue, String attrIconExpandedValue) throws IOException {
        writer.startElement(TD_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, cssClass, null);

        encodeIdIcon(writer, context, isExpanded, attrIconCollapsedValue, "rf-pm-ico-colps");
        encodeIdIcon(writer, context, !isExpanded, attrIconExpandedValue, "rf-pm-ico-exp");

        writer.endElement(TD_ELEM);
    }

    private void encodeIdIcon(ResponseWriter writer, FacesContext context, boolean isExpanded, String attrIconValue, String styleClass) throws IOException {
        if (attrIconValue == null || attrIconValue.trim().length() <= 0) {
            encodeDivIcon(writer, isExpanded, PanelMenuIcons.none, styleClass);
        } else {
            PanelMenuIcons icon = getIcon(attrIconValue);
            if (icon != null) {
                encodeDivIcon(writer, isExpanded, icon, styleClass);
            } else {
                encodeImage(writer, context, attrIconValue);
            }
        }
    }

    private PanelMenuIcons getIcon(String attrIconCollapsedValue) {
        if (attrIconCollapsedValue == null) {
            return null;
        }

        try {
            return PanelMenuIcons.valueOf(attrIconCollapsedValue);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void encodeDivIcon(ResponseWriter writer, boolean isDisplay, PanelMenuIcons icon, String styleClass) throws IOException {
        writer.startElement(DIV_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, concatClasses(styleClass, icon.cssClass()), null);
        writer.writeAttribute(STYLE_ATTRIBUTE, styleElement("display", isDisplay ? "none" : "block"), null);
        writer.endElement(DIV_ELEM);
    }

    private void encodeImage(ResponseWriter writer, FacesContext context, String attrIconValue) throws IOException {
        writer.startElement(IMG_ELEMENT, null);
        writer.writeAttribute(ALT_ATTRIBUTE, "", null);
        writer.writeURIAttribute(SRC_ATTRIBUTE, RenderKitUtils.getResourceURL(attrIconValue, context), null);
        writer.endElement(IMG_ELEMENT);
    }


    public String getCssClass(AbstractPanelMenuItem item, String postfix) {
        return (item.isTopItem() ? TOP_CSS_CLASS_PREFIX : CSS_CLASS_PREFIX) + postfix;
    }

    private void encodeContentBegin(ResponseWriter writer, FacesContext context, HtmlPanelMenuGroup menuGroup) throws IOException {
        writer.startElement(DIV_ELEM, null);
        writer.writeAttribute(ID_ATTRIBUTE, menuGroup.getClientId(context) + ":cnt", null);
        writer.writeAttribute(CLASS_ATTRIBUTE, concatClasses(getCssClass(menuGroup, "-cnt"), menuGroup.isExpanded() ? "rf-pm-exp" : "rf-pm-colps"), null);

        writeJavaScript(writer, context, menuGroup);
    }

    private void encodeContentEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        writer.endElement(DIV_ELEM);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        AbstractPanelMenuItem menuItem = (AbstractPanelMenuItem) component;

        return concatClasses(getCssClass(menuItem, ""),
            attributeAsString(component, "styleClass"),
            menuItem.isDisabled() ? getCssClass(menuItem, "-dis") : "",
            menuItem.isDisabled() ? attributeAsString(component, "disabledClass") : "");
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
        //TODO nick - non-default values should not be rendered
        options.put("ajax", getAjaxOptions(context, panelMenuGroup));
        options.put("name", panelMenuGroup.getName());
        options.put("mode", panelMenuGroup.getMode());
        options.put("disabled", panelMenuGroup.isDisabled());
        options.put("expandEvent", panelMenuGroup.getExpandEvent());
        options.put("collapseEvent", panelMenuGroup.getCollapseEvent());
        options.put("expandSingle", panelMenuGroup.isExpandSingle());
        options.put("bubbleSelection", panelMenuGroup.isBubbleSelection());
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

        writer.endElement(DIV_ELEM);
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractPanelMenuGroup.class;
    }
}

