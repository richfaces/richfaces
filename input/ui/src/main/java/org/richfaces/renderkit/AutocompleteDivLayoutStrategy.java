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

public class AutocompleteDivLayoutStrategy extends AbstractAutocompleteLayoutStrategy implements AutocompleteEncodeStrategy {
    public void encodeFakeItem(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HtmlConstants.DIV_ELEM, component);
        responseWriter.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, "display:none", null);
        responseWriter.endElement(HtmlConstants.DIV_ELEM);
    }

    public void encodeItemsContainerBegin(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HtmlConstants.DIV_ELEM, component);
        responseWriter.writeAttribute(HtmlConstants.ID_ATTRIBUTE, getContainerElementId(facesContext, component), null);
    }

    public void encodeItemsContainerEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.endElement(HtmlConstants.DIV_ELEM);
    }

    public void encodeItemBegin(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HtmlConstants.DIV_ELEM, component);
    }

    public void encodeItemEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    public void encodeItem(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        encodeItemBegin(facesContext, component);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-au-itm rf-au-opt rf-au-fnt rf-au-inp", null);
        for (UIComponent child : component.getChildren()) {
            child.encodeAll(facesContext);
        }
        encodeItemEnd(facesContext, component);
    }
}
