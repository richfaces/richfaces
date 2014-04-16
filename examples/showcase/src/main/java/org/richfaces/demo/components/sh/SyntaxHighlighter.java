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

package org.richfaces.demo.components.sh;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSFunction;

@FacesComponent(value = "syntaxHighlighter")
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "jquery.js"), @ResourceDependency(library = "js", name = "xregexp.js"),
        @ResourceDependency(library = "js", name = "shCore.js"),
        @ResourceDependency(library = "js", name = "shBrushJScript.js"),
        @ResourceDependency(library = "js", name = "shBrushJava.js"),
        @ResourceDependency(library = "js", name = "shBrushXml.js"),
        @ResourceDependency(library = "js", name = "shBrushCss.js"),
        @ResourceDependency(library = "js", name = "shBrushPlain.js"),
        @ResourceDependency(library = "css", name = "shCore.css"),
        @ResourceDependency(library = "css", name = "shThemeDefault.css")})
public class SyntaxHighlighter extends UIComponentBase {
    private static final String COMPONENT_FAMILY = "org.richfaces.SyntaxHighlighter";
    private static final String DEFAULT_SOURCE_TYPE = "xhtml";

    enum propertyKeys {
        sourceType,
        src,
        style,
        styleClass
    }

    ;

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    private void renderStream(FacesContext context, InputStream stream) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        InputStreamReader in = new InputStreamReader(stream);
        char[] temp = new char[1024];
        try {
            int bytes;
            while ((bytes = in.read(temp)) > 0) {
                writer.writeText(temp, 0, bytes);
            }
        } catch (IOException e) {
            throw new FacesException(e);
        } finally {
            in.close();
        }
    }

    private void renderLabel(FacesContext context, String label) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.writeText(label.toCharArray(), 0, label.length());
    }

    private void renderContent(FacesContext context) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String src = getSrc();
        if (src != null) {
            InputStream stream = ec.getResourceAsStream(src);
            if (stream != null) {
                renderStream(context, stream);
            } else {
                renderLabel(context, "resource for highlight not found");
            }
        } else {
            renderLabel(context, "src may not be null");
        }
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", null);
        writer.writeAttribute("id", this.getClientId(context), null);
        writer.writeAttribute("class", this.getStyleClass(), null);
        writer.writeAttribute("style", this.getStyle(), null);
        writer.startElement("pre", null);
        writer.writeAttribute("class", new StringBuffer().append("brush: ").append(this.getSourceType()), null);
        renderContent(context);
        writer.endElement("pre");
        JSFunction function = new JSFunction("SyntaxHighlighter.all");
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("var brs = $('#" + this.getClientId(context).replaceAll(":", "\\\\\\\\:") + "').find('br');");
        writer.write("brs.length && brs.replaceWith('\\n');");
        writer.write(function.toScript());
        writer.endElement("script");
        writer.endElement("div");
    }

    public String getSourceType() {
        return (String) getStateHelper().eval(propertyKeys.sourceType, DEFAULT_SOURCE_TYPE);
    }

    public void setSourceType(String sourceType) {
        getStateHelper().put(propertyKeys.sourceType, sourceType);
    }

    public String getSrc() {
        return (String) getStateHelper().eval(propertyKeys.src);
    }

    public void setSrc(String src) {
        getStateHelper().put(propertyKeys.src, src);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(propertyKeys.style);
    }

    public void setStyle(String style) {
        getStateHelper().put(propertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(propertyKeys.styleClass);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(propertyKeys.styleClass, styleClass);
    }
}
