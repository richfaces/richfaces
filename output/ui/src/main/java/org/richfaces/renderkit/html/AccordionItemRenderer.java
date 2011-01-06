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
import org.richfaces.component.AbstractTogglePanelTitledItem;
import org.richfaces.component.html.HtmlAccordionItem;
import org.richfaces.component.html.HtmlAccordionItem.PropertyKeys;
import org.richfaces.renderkit.RenderKitUtils;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

import static org.richfaces.renderkit.HtmlConstants.*;
import static org.richfaces.renderkit.RenderKitUtils.renderPassThroughAttributes;

/**
 *
 * @author akolonitsky
 * @since 2010-08-05
 */
@ResourceDependencies({
    @ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-event.js"),
    @ResourceDependency(name = "richfaces-base-component.js"),
    @ResourceDependency(library = "org.richfaces", name = "togglePanelItem.js"),
    @ResourceDependency(library = "org.richfaces", name = "accordionItem.js")
    })
public class AccordionItemRenderer extends TogglePanelItemRenderer {

    private static final RenderKitUtils.Attributes HEADER_ATTRIBUTES = RenderKitUtils.attributes()
        .generic("style", PropertyKeys.headerStyle.toString())
        .generic("onclick", PropertyKeys.onheaderclick.toString(), "headerclick")
        .generic("ondblclick", PropertyKeys.onheaderdblclick.toString(), "headerdblclick")
        .generic("onmousedown", PropertyKeys.onheadermousedown.toString(), "headermousedown")
        .generic("onmousemove", PropertyKeys.onheadermousemove.toString(), "headermousemove")
        .generic("onmouseup", PropertyKeys.onheadermouseup.toString(), "headermouseup");

    private final TableIconsRendererHelper<HtmlAccordionItem> headerRenderer = new AccordionItemHeaderRenderer();

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(writer, context, component);

        encodeHeader(writer, context, (HtmlAccordionItem) component);

        encodeContentBegin(component, writer);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return concatClasses("rf-ac-itm", attributeAsString(component, "styleClass"));
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        encodeContentEnd(writer, component);

        super.doEncodeEnd(writer, context, component);
    }

    private void encodeContentBegin(UIComponent component, ResponseWriter writer) throws IOException {
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(CLASS_ATTRIBUTE, concatClasses("rf-ac-itm-cnt", attributeAsString(component, "contentClass")), null);
        writer.writeAttribute("id", component.getClientId() + ":content", null);

        AbstractTogglePanelTitledItem item = (AbstractTogglePanelTitledItem) component;
        if (!item.isActive() || item.isDisabled()) {
            writer.writeAttribute(STYLE_ATTRIBUTE, "display: none", null);
        }
    }

    private void encodeContentEnd(ResponseWriter writer, UIComponent component) throws IOException {
        writer.endElement(DIV_ELEM);
    }

    private void encodeHeader(ResponseWriter writer, FacesContext context, HtmlAccordionItem component) throws IOException {
        writer.startElement(DIV_ELEM, component);

        String stateCssClass = "rf-ac-itm-hdr-" + (component.isDisabled() ? "dis" : (component.isActive() ? "act" : "inact"));
        writer.writeAttribute(CLASS_ATTRIBUTE, concatClasses("rf-ac-itm-hdr", stateCssClass, attributeAsString(component, PropertyKeys.headerClass)), null);

        writer.writeAttribute(ID_ATTRIBUTE, component.getClientId() + ":header", null);
        renderPassThroughAttributes(context, component, HEADER_ATTRIBUTES);

        headerRenderer.encodeHeader(writer, context, component);

        writer.endElement(DIV_ELEM);
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.AccordionItem", component.getClientId(),
            getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        Map<String, Object> res = super.getScriptObjectOptions(context, component);
        res.put("disabled", ((AbstractTogglePanelTitledItem) component).isDisabled());

        return res;
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractTogglePanelTitledItem.class;
    }
}

