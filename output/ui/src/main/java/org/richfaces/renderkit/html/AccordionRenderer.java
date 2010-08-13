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
import org.richfaces.component.AbstractAccordion;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author akolonitsky
 * @since 2010-08-05
 */
@ResourceDependencies( { // TODO review
    @ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-base-component.js"),
    @ResourceDependency(library = "org.richfaces", name = "accordion.ecss"),
    @ResourceDependency(library = "org.richfaces", name = "TogglePanel.js"),
    @ResourceDependency(library = "org.richfaces", name = "Accordion.js") })
public class AccordionRenderer extends TogglePanelRenderer {

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(writer, context, component);

        writeJavaScript(writer, context, component);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return "rf-ac" + attributeAsString(component, "styleClass");
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        writer.endElement(RendererUtils.HTML.DIV_ELEM);
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractAccordion.class;
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.Accordion",
            component.getClientId(), getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        Map<String, Object> options = super.getScriptObjectOptions(context, component);
        options.remove("items");

        return options;
    }
}

