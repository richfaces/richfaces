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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSObject;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractTogglePanel;
import org.richfaces.component.AbstractTogglePanelItem;
import org.richfaces.component.SwitchType;
import org.richfaces.component.util.HtmlUtil;

/**
 * @author akolonitsky
 * @since -4712-01-01
 */
@ResourceDependencies({
    @ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-event.js"),
    @ResourceDependency(name = "richfaces-base-component.js"),
    @ResourceDependency(library = "org.richfaces", name = "togglePanelItem.js") })
@JsfRenderer(type = "org.richfaces.TogglePanelItemRenderer", family = AbstractTogglePanelItem.COMPONENT_FAMILY)
public class TogglePanelItemRenderer extends DivPanelRenderer {
    
    private static final String LEAVE = "leave";
    private static final String ENTER = "enter";

    private final boolean hideInactiveItems;
    
    public TogglePanelItemRenderer() {
        this(true);
    }
    
    protected TogglePanelItemRenderer(boolean hideInactiveItems) {
        this.hideInactiveItems = hideInactiveItems;
    }
    
    @Override
    protected String getStyleClass(UIComponent component) {
        return concatClasses("rf-tgp-itm", attributeAsString(component, "styleClass"));
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.TogglePanelItem", component.getClientId(context),
            getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        AbstractTogglePanelItem panelItem = (AbstractTogglePanelItem) component;

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", panelItem.getName());
        options.put("togglePanelId", panelItem.getParent().getClientId(context));
        options.put("switchMode", panelItem.getSwitchType());

        AbstractTogglePanelItem item = (AbstractTogglePanelItem) component;
        AbstractTogglePanel panel = item.getParentPanel();
        options.put("index", panel.getChildIndex(item.getName()));

        TogglePanelRenderer.addEventOption(context, component, options, LEAVE);
        TogglePanelRenderer.addEventOption(context, component, options, ENTER);

        return options;
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractTogglePanelItem.class;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
    
    protected void encodePlaceHolderWithJs(FacesContext context, AbstractTogglePanelItem item) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("id", item.getClientId(context), null);
        writer.writeAttribute("style", "display:none;", null);

        writeJavaScript(writer, context, item);

        writer.endElement("div");
    }

    private boolean shouldEncodeItem(FacesContext context, AbstractTogglePanelItem item) {
        return item.isActive() || item.getSwitchType() == SwitchType.client;
    }
    
    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        AbstractTogglePanelItem item = (AbstractTogglePanelItem) component;
        
        if (shouldEncodeItem(context, item)) {
            doEncodeItemBegin(writer, context, component);
        }
    }

    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component)
        throws IOException {

        AbstractTogglePanelItem item = (AbstractTogglePanelItem) component;
        
        if (shouldEncodeItem(context, item)) {
            renderChildren(context, component);
        }
    }
    
    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        AbstractTogglePanelItem item = (AbstractTogglePanelItem) component;
        
        if (shouldEncodeItem(context, item)) {
            doEncodeItemEnd(writer, context, component);
        } else {
            encodePlaceHolderWithJs(context, item);
        }
    }
    
    @Override
    protected String getStyle(UIComponent component) {
        String attributeStyle = super.getStyle(component);
        if (hideInactiveItems && !((AbstractTogglePanelItem) component).isActive()) {
            return HtmlUtil.concatStyles(attributeStyle, "display: none");
        } else {
            return attributeStyle;
        }
    }
}

