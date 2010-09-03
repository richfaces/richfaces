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
import org.ajax4jsf.renderkit.RendererUtils;
import org.richfaces.component.AbstractTogglePanelTitledItem;
import org.richfaces.renderkit.RenderKitUtils;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

import static org.richfaces.component.AbstractTogglePanelTitledItem.HeaderStates;
import static org.richfaces.component.html.HtmlAccordionItem.PropertyKeys;
import static org.richfaces.component.util.HtmlUtil.concatClasses;
import static org.richfaces.renderkit.RenderKitUtils.renderPassThroughAttributes;

/**
 *
 * <div id="clientId" class="rf-ac-itm">
 *     <div id="clientId:header" class="rf-ac-itm-hdr">
 *         <div class="rf-ac-itm-hdr-inact">Level 1</div>
 *         <div class="rf-ac-itm-hdr-act"  >Level 1</div>
 *         <div class="rf-ac-itm-hdr-dis">Level 1</div>
 *     </div>
 *     <div id="clientId:content" class="rf-ac-itm-cnt">
 *         Content will be here.
 *     </div>
 * </div>
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
    @ResourceDependency(library = "org.richfaces", name = "TogglePanelItem.js"),
    @ResourceDependency(library = "org.richfaces", name = "AccordionItem.js")
    })
public class AccordionItemRenderer extends TogglePanelItemRenderer {

    private static final RenderKitUtils.Attributes HEADER_ATTRIBUTES = RenderKitUtils.attributes()
        .generic("style", PropertyKeys.headerStyle.toString())
        .generic("onclick", PropertyKeys.onheaderclick.toString(), "headerclick")
        .generic("ondblclick", PropertyKeys.onheaderdblclick.toString(), "headerdblclick")
        .generic("onmousedown", PropertyKeys.onheadermousedown.toString(), "headermousedown")
        .generic("onmousemove", PropertyKeys.onheadermousemove.toString(), "headermousemove")
        .generic("onmouseup", PropertyKeys.onheadermouseup.toString(), "headermouseup");

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(writer, context, component);

        encodeHeader(context, component, writer);

        encodeContentBegin(component, writer);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return concatClasses("rf-ac-itm", attributeAsString(component, "styleClass"));
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
        
        writer.startElement(RendererUtils.HTML.SCRIPT_ELEM, component);
        writer.writeAttribute(RendererUtils.HTML.TYPE_ATTR, "text/javascript", "type");
        writer.writeText(script, null);
        writer.endElement(RendererUtils.HTML.SCRIPT_ELEM);
    }

    private void encodeContentBegin(UIComponent component, ResponseWriter writer) throws IOException {
        writer.startElement("div", component);
        writer.writeAttribute("class", concatClasses("rf-ac-itm-cnt", attributeAsString(component, "contentClass")), null);
        writer.writeAttribute("id", component.getClientId() + ":content", null);

        AbstractTogglePanelTitledItem item = (AbstractTogglePanelTitledItem) component;
        if (!item.isActive() || item.isDisabled()) {
            writer.writeAttribute("style", "display: none", null);
        }
    }

    private void encodeContentEnd(UIComponent component, ResponseWriter responseWriter) throws IOException {
        responseWriter.endElement("div");
    }

    private void encodeHeader(FacesContext context, UIComponent component, ResponseWriter writer) throws IOException {

        writer.startElement("div", component);
        writer.writeAttribute("class", concatClasses("rf-ac-itm-hdr", attributeAsString(component, PropertyKeys.headerClass)), null);
        writer.writeAttribute("id", component.getClientId() + ":header", null);
        renderPassThroughAttributes(context, component, HEADER_ATTRIBUTES);

        AbstractTogglePanelTitledItem titledItem = (AbstractTogglePanelTitledItem) component;
        boolean isActive = titledItem.isActive();
        boolean isDisabled = titledItem.isDisabled(); 
        encodeHeader(context, titledItem, writer, HeaderStates.inactive, !isActive && !isDisabled);
        encodeHeader(context, titledItem, writer, HeaderStates.active, isActive && !isDisabled);
        encodeHeader(context, titledItem, writer, HeaderStates.disable, isDisabled);

        writer.endElement("div");
    }

    private void encodeHeader(FacesContext facesContext, AbstractTogglePanelTitledItem component, ResponseWriter writer,
                              HeaderStates state, Boolean isDisplay) throws IOException {
        
        writer.startElement("div", component);

        if (!isDisplay) {
            writer.writeAttribute("style", "display : none", null);
        }

        String name = "headerClass" + capitalize(state.toString());
        writer.writeAttribute("class", concatClasses("rf-ac-itm-hdr-" + state.abbreviation(), attributeAsString(component, name)), null);

        UIComponent headerFacet = component.getHeaderFacet(state);
        if (headerFacet != null && headerFacet.isRendered()) {
            headerFacet.encodeAll(facesContext);
        } else {
            Object headerText = component.getAttributes().get("header");
            if (headerText != null && !headerText.equals("")) {
                writer.writeText(headerText, null);
            }
        }

        writer.endElement("div");
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

