/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.ui.ajax.command;

import com.google.common.base.Strings;

import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Locale;

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
