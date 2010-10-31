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
public class PanelMenuItemRenderer extends DivPanelRenderer {

    public static final String UNSELECT = "unselect";
    public static final String SELECT = "select";
    public static final String BEFORE_SELECT = "beforeselect";

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(writer, context, component);

        writer.writeText(((AbstractPanelMenuItem) component).getLabel(), null);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return concatClasses("rf-pm-itm", attributeAsString(component, "styleClass"));
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

