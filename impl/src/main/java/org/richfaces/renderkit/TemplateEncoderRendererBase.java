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

package org.richfaces.renderkit;

import org.ajax4jsf.io.SAXResponseWriter;
import org.ajax4jsf.renderkit.RendererBase;
import org.richfaces.component.TemplateComponent;
import org.xml.sax.ContentHandler;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Nick Belaevski - mailto:nbelaevski@exadel.com
 *         created 22.06.2007
 */
public abstract class TemplateEncoderRendererBase extends RendererBase {
    public final boolean getRendersChildren() {
        return true;
    }

    protected void writeScriptBody(FacesContext context, UIComponent component, boolean children) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        ContentHandler handler = createContentHandler(writer);
        SAXResponseWriter saxResponseWriter = new SAXResponseWriter(handler);

        context.setResponseWriter(saxResponseWriter);

        TemplateComponent templateComponent = null;

        if (component instanceof TemplateComponent) {
            templateComponent = (TemplateComponent) component;
        }

        try {
            if (templateComponent != null) {
                templateComponent.startTemplateEncode();
            }

            saxResponseWriter.startDocument();

            // TODO - how to handle extra "root" element?
            saxResponseWriter.startElement("root", component);

            if (children) {
                this.renderChildren(context, component);
            } else {
                component.encodeAll(context);
            }

            saxResponseWriter.endElement("root");
            saxResponseWriter.endDocument();
        } finally {
            if (templateComponent != null) {
                templateComponent.endTemplateEncode();
            }

            context.setResponseWriter(writer);
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("script", component);
        writer.write("var evaluator = ");
        writeScriptBody(context, component, true);
        writer.write(";\n new Insertion.Top($('" + component.getClientId(context)
            + "'), evaluator.invoke('getContent', window).join(''));");
        writer.endElement("script");
    }

    protected ContentHandler createContentHandler(Writer writer) {
        return new MacroDefinitionJSContentHandler(writer, "Richfaces.evalMacro(\"", "\", context)");
    }
}
