/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

package org.richfaces.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.component.AbstractAutoComplete;

public class AutocompleteDivLayoutStrategy extends AbstractAutocompleteLayoutStrategy implements
    AutoCompleteEncodeStrategy {

    public void encodeFakeItem(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HTML.DIV_ELEM, component);
        responseWriter.writeAttribute(HTML.STYLE_ATTRIBUTE, "display:none", null);
        responseWriter.endElement(HTML.DIV_ELEM);

    }

    public void encodeItemsContainerBegin(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HTML.DIV_ELEM, component);
        responseWriter.writeAttribute(HTML.ID_ATTRIBUTE, getContainerElementId(facesContext, component), null);
        // responseWriter.writeAttribute(HTML.CLASS_ATTRIBUTE, "cb_list_ul", null);
    }

    public void encodeItemsContainerEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.endElement(HTML.DIV_ELEM);
    }

    public void encodeItem(FacesContext facesContext, AbstractAutoComplete comboBox, Object item) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.DIV_ELEM, comboBox);
        writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "cb_option cb_font rf-ac-i", null);

        if (comboBox.getChildCount() > 0) {
            for (UIComponent child : comboBox.getChildren()) {
                child.encodeAll(facesContext);
            }
        } else {
            if (item != null) {
                // TODO nick - use converter
                String value = null;
                if (comboBox.getItemConverter() != null) {
                    value = comboBox.getItemConverter().getAsString(facesContext, comboBox, item);
                }
                if (value != null) {
                    writer.writeText(value, null);
                }
                writer.writeText(item, null);
                // writer.writeText(InputUtils.getConvertedValue(facesContext, comboBox, item), null);
            }
        }

        writer.endElement(HTML.DIV_ELEM);

    }

}
