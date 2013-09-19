/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.Locale;

import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.renderkit.AjaxCommandRendererBase;

import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 *
 */
public abstract class CommandButtonRendererBase extends AjaxCommandRendererBase {
    protected void encodeTypeAndImage(FacesContext context, UIComponent uiComponent) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String type = (String) uiComponent.getAttributes().get("type");
        String image = (String) uiComponent.getAttributes().get("image");

        if (image != null) {
            if (!image.contains(ResourceHandler.RESOURCE_IDENTIFIER)) {
                image = context.getApplication().getViewHandler().getResourceURL(context, image);
                image = context.getExternalContext().encodeResourceURL(image);
            }
            writer.writeAttribute("type", "image", "image");
            writer.writeURIAttribute("src", image, "image");

            Object value = uiComponent.getAttributes().get("value");

            if (null == uiComponent.getAttributes().get("alt") && null != value) {
                writer.writeAttribute("alt", value, "value");
            }
        } else {
            if (!Strings.isNullOrEmpty(type)) {
                writer.writeAttribute("type", type.toLowerCase(Locale.US), "type");
            } else {
                writer.writeAttribute("type", "submit", "type");
            }
        }
    }
}
