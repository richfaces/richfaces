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

/**
 *
 */
package org.richfaces.ui.core;

import java.io.IOException;
import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.javascript.ScriptUtils;
import org.richfaces.resource.ResourceKey;

/**
 * @author asmirnov
 *
 */
public class ScriptsRenderer extends ResourceRenderer {
    public static final String RENDERER_TYPE = "org.richfaces.ui.renderer.ScriptsRenderer";
    private static final String TEXT_JAVASCRIPT = "text/javascript";
    private static final String SRC = "src";
    private static final String TYPE = "type";
    private static final String SCRIPT = "script";
    private static final ResourceKey JQUERY = ResourceKey.create("jquery.js", "org.richfaces");

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        UIScripts validatorScript = (UIScripts) component;
        ResponseWriter responseWriter = context.getResponseWriter();
        // TODO - change behavior for AJAX requests.
        Collection<Object> scripts = validatorScript.getScripts();
        encodeDependentResources(context, component, scripts);
        Collection<Object> pageReadyScripts = validatorScript.getPageReadyScripts();
        if (!pageReadyScripts.isEmpty()) {
            encodeResource(component, context, JQUERY);
            encodeDependentResources(context, component, pageReadyScripts);
        }
        responseWriter.startElement(SCRIPT, null);
        responseWriter.writeAttribute(TYPE, TEXT_JAVASCRIPT, null);
        for (Object script : scripts) {
            ScriptUtils.writeToStream(responseWriter, script);
            responseWriter.writeText("\n", null);
        }
        if (!pageReadyScripts.isEmpty()) {
            responseWriter.writeText("RichFaces.jQuery(document).ready(function() {\n", null);
            for (Object script : pageReadyScripts) {
                ScriptUtils.writeToStream(responseWriter, script);
                responseWriter.writeText("\n", null);
            }
            responseWriter.writeText("});\n", null);
        }
        responseWriter.endElement(SCRIPT);
    }
}
