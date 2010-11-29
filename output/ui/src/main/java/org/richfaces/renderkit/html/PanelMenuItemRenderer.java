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
import org.richfaces.component.AbstractPanelMenuItem;
import org.richfaces.component.html.HtmlPanelMenuItem;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RenderKitUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.richfaces.renderkit.html.TogglePanelRenderer.addEventOption;
import static org.richfaces.renderkit.html.TogglePanelRenderer.getAjaxOptions;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
public class PanelMenuItemRenderer extends DivPanelRenderer {

    public static final String UNSELECT = "unselect";
    public static final String SELECT = "select";
    public static final String BEFORE_SELECT = "beforeselect";
    
    private static final String CSS_CLASS_PREFIX = "rf-pm-itm";

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(writer, context, component);

        encodeHeaderGroup(writer, context, (AbstractPanelMenuItem) component, CSS_CLASS_PREFIX);
    }

    static void encodeHeaderGroup(ResponseWriter writer, FacesContext context, AbstractPanelMenuItem menuItem, String classPrefix) throws IOException {
        writer.startElement("table", null);
        writer.writeAttribute("class", classPrefix + "-gr", null);
        writer.startElement("tr", null);

        encodeTdIcon(writer, context, classPrefix, menuItem.getIcon());

        writer.startElement("td", null);
        writer.writeAttribute("class", classPrefix + "-lbl", null);
        writer.writeText(menuItem.getLabel(), null);
        writer.endElement("td");

        writer.startElement("td", null);
        writer.writeAttribute("class", HtmlUtil.concatClasses(classPrefix + "-exp-ico", "rf-pm-triangle-down"), null);
        writer.endElement("td");

        writer.endElement("tr");
        writer.endElement("table");
    }

    private static void encodeTdIcon(ResponseWriter writer, FacesContext context, String classPrefix, String attrIconValue) throws IOException {
        writer.startElement("td", null);
        try {
            AbstractPanelMenuItem.Icons icon = AbstractPanelMenuItem.Icons.valueOf(attrIconValue);
            writer.writeAttribute("class", HtmlUtil.concatClasses(classPrefix + "-ico", icon.cssClass()), null);
        } catch (IllegalArgumentException e) {
            writer.writeAttribute("class", classPrefix + "-ico", null);
            if(attrIconValue != null && attrIconValue.trim().length() != 0) {
                writer.startElement(HtmlConstants.IMG_ELEMENT, null);
                writer.writeAttribute(HtmlConstants.ALT_ATTRIBUTE, "", null);
                writer.writeURIAttribute(HtmlConstants.SRC_ATTRIBUTE, RenderKitUtils.getResourceURL(attrIconValue, context), null);
                writer.endElement(HtmlConstants.IMG_ELEMENT);
            }
        }

        writer.endElement("td");
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return concatClasses(CSS_CLASS_PREFIX, attributeAsString(component, "styleClass"));
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.PanelMenuItem",
            component.getClientId(), getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        HtmlPanelMenuItem panelMenuItem = (HtmlPanelMenuItem) component;

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("ajax", getAjaxOptions(context, panelMenuItem));
        options.put("disabled", panelMenuItem.isDisabled());
        options.put("mode", panelMenuItem.getMode());
        options.put("name", panelMenuItem.getName());

        addEventOption(context, panelMenuItem, options, UNSELECT);
        addEventOption(context, panelMenuItem, options, SELECT);
        addEventOption(context, panelMenuItem, options, BEFORE_SELECT);

        return options;
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeEnd(writer, context, component);
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractPanelMenuItem.class;
    }
}

