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

import static org.richfaces.renderkit.HtmlConstants.CLASS_ATTRIBUTE;
import static org.richfaces.renderkit.HtmlConstants.DIV_ELEM;
import static org.richfaces.renderkit.HtmlConstants.ID_ATTRIBUTE;
import static org.richfaces.renderkit.HtmlConstants.STYLE_ATTRIBUTE;

import java.io.IOException;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSObject;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractTab;
import org.richfaces.component.AbstractTogglePanelTitledItem;
import org.richfaces.renderkit.HtmlConstants;

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
    @ResourceDependency(library = "org.richfaces", name = "togglePanelItem.js"),
    @ResourceDependency(library = "org.richfaces", name = "tab.js")
})
@JsfRenderer(type = "org.richfaces.TabRenderer", family = AbstractTab.COMPONENT_FAMILY)
public class TabRenderer extends TogglePanelItemRenderer {

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(writer, context, component);

        encodeContentBegin(context, component, writer);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return concatClasses("rf-tb", attributeAsString(component, "styleClass"));
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        encodeContentEnd(component, writer);

        super.doEncodeEnd(writer, context, component);
    }

    @Override
    protected void writeJavaScript(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        Object script = getScriptObject(context, component);
        if (script == null) {
            return;
        }

        writer.startElement(HtmlConstants.SCRIPT_ELEM, component);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, "text/javascript", "type");
        writer.writeText(script, null);
        writer.endElement(HtmlConstants.SCRIPT_ELEM);
    }

    private void encodeContentBegin(FacesContext context, UIComponent component, ResponseWriter writer) throws IOException {
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(CLASS_ATTRIBUTE, concatClasses("rf-tb-cnt", attributeAsString(component, "contentClass")), null);
        writer.writeAttribute(ID_ATTRIBUTE, component.getClientId(context) + ":content", null);

        AbstractTogglePanelTitledItem item = (AbstractTogglePanelTitledItem) component;
        if (!item.isActive() || item.isDisabled()) {
            writer.writeAttribute(STYLE_ATTRIBUTE, "display: none", null);
        }
    }

    private void encodeContentEnd(UIComponent component, ResponseWriter responseWriter) throws IOException {
        responseWriter.endElement(DIV_ELEM);
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.Tab", component.getClientId(context),
            getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        Map<String, Object> res = super.getScriptObjectOptions(context, component);
        res.put("disabled", ((AbstractTab) component).isDisabled());
        res.put("enter", ((AbstractTab) component).getOnenter());
        res.put("leave", ((AbstractTab) component).getOnleave());

        return res;
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractTogglePanelTitledItem.class;
    }
}

