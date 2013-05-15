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

package org.richfaces.ui.misc.placeholder;

import org.richfaces.renderkit.RendererBase;
import org.richfaces.util.InputUtils;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;

import java.io.IOException;

@ResourceDependencies({ @ResourceDependency(name = "base-component.reslib", library = "org.richfaces", target = "head"),
        @ResourceDependency(name = "jquery.watermark.js", library = "org.richfaces", target = "head"),
        @ResourceDependency(name = "placeholder.js", library = "org.richfaces/misc/placeholder", target = "head"),
        @ResourceDependency(name = "placeholder.css", library = "org.richfaces/misc/placeholder") })
public abstract class PlaceholderRendererBase extends RendererBase {

    public static final String RENDERER_TYPE = "org.richfaces.PlaceholderRenderer";

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        AbstractPlaceholder placeholder = (AbstractPlaceholder) component;

        // skip direct rendering for nested usage (workaround for RF-12589)
        if (placeholder.getSelector() == null || placeholder.getSelector().isEmpty()) {
            return;
        }

        super.encodeEnd(context, component);
    }

    @Override
    public void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeEnd(writer, context, component);
    }

    public String getConvertedValue(FacesContext facesContext, AbstractPlaceholder placeholder) {
        final Object value = placeholder.getValue();

        Converter converter = InputUtils.findConverter(facesContext, placeholder, "value");

        if (converter != null) {
            return converter.getAsString(facesContext, placeholder, value);
        } else {
            return value != null ? value.toString() : "";
        }
    }
}
