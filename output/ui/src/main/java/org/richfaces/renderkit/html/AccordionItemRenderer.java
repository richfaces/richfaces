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
import org.richfaces.component.AbstractTogglePanelItem;
import org.richfaces.component.AbstractTogglePanelTitledItem;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 *
 * <div id="clientId" class="rf-aci">
 *     <div id="clientId:header" class="rf-aci-h">
 *         <div class="rf-aci-h-inactive">Level 1</div>
 *         <div class="rf-aci-h-active"  >Level 1</div>
 *         <div class="rf-aci-h-disabled">Level 1</div>
 *     </div>
 *     <div id="clientId:content" class="rf-aci-c">
 *         Content will be here.
 *     </div>
 * </div>
 *
 * @author akolonitsky
 * @since 2010-08-05
 */
@ResourceDependencies({ // TODO review
    @ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-event.js"),
    @ResourceDependency(name = "richfaces-base-component.js"),
    @ResourceDependency(library = "org.richfaces", name = "TogglePanelItem.js"),
    @ResourceDependency(library = "org.richfaces", name = "AccordionItem.js")
    })
public class AccordionItemRenderer extends TogglePanelItemRenderer {

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(writer, context, component);

        encodeHeader(context, component, writer);

        encodeContentBegin(component, writer);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return "rf-aci " + attributeAsString(component, "styleClass");
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        encodeContentEnd(component, writer);

        super.doEncodeEnd(writer, context, component);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void writeJavaScript(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        // todo how to call method from parent of parent class? 

        Object script = getScriptObject(context, component);
        if (script != null) {
            writer.startElement(RendererUtils.HTML.SCRIPT_ELEM, component);
            writer.writeAttribute(RendererUtils.HTML.TYPE_ATTR, "text/javascript", "type");
            writer.writeText(script, null);
            writer.endElement(RendererUtils.HTML.SCRIPT_ELEM);
        }
    }

    private void encodeContentBegin(UIComponent component, ResponseWriter writer) throws IOException {
        writer.startElement("div", component);
        writer.writeAttribute("class", "rf-aci-c " + attributeAsString(component, "contentClass"), null);
        writer.writeAttribute("id", component.getClientId() + ":content", null);

        if (!((AbstractTogglePanelItem) component).isActive()) {
            writer.writeAttribute("style", "display: none", null);
        }
    }

    private void encodeContentEnd(UIComponent component, ResponseWriter responseWriter) throws IOException {
        responseWriter.endElement("div");
    }

    private void encodeHeader(FacesContext facesContext, UIComponent component, ResponseWriter responseWriter) throws IOException {

        responseWriter.startElement("div", component);
        responseWriter.writeAttribute("class", "rf-aci-h " + attributeAsString(component, "contentClass"), null);
        responseWriter.writeAttribute("id", component.getClientId() + ":header", null);

        AbstractTogglePanelTitledItem titledItem = (AbstractTogglePanelTitledItem) component;
        boolean isActive = titledItem.isActive();
        boolean isDisabled = titledItem.isDisabled(); 
        encodeHeader(facesContext, component, responseWriter, "inactive", !isActive && !isDisabled);
        encodeHeader(facesContext, component, responseWriter, "active", isActive && !isDisabled);
        encodeHeader(facesContext, component, responseWriter, "disable", isDisabled);

        responseWriter.endElement("div");
    }

    private void encodeHeader(FacesContext facesContext, UIComponent component, ResponseWriter writer,
                              String state, Boolean isDisplay) throws IOException {
        
        writer.startElement("div", component);

        if (!isDisplay) {
            writer.writeAttribute("style", "display : none", null);
        }

        String name = "headerClass" + capitalize(state);
        writer.writeAttribute("class", "rf-aci-h-" + state + " " + attributeAsString(component, name), name);


        UIComponent headerFacet = component.getFacet("header");
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
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractTogglePanelTitledItem.class;
    }
}

