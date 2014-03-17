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
package org.richfaces.renderkit;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.javascript.JSFunction;
import org.richfaces.services.ServiceTracker;
import org.richfaces.javascript.DnDScript;
import org.richfaces.javascript.JavaScriptService;

/**
 * @author abelevich
 *
 */
public abstract class DnDRenderBase extends RendererBase {
    public void buildAndStoreScript(FacesContext facesContext, UIComponent component) {
        JavaScriptService javaScriptService = ServiceTracker.getService(JavaScriptService.class);
        DnDScript dragScript = buildClientScript(facesContext, component);
        if (javaScriptService != null && dragScript != null) {
            javaScriptService.addPageReadyScript(facesContext, dragScript);
        }
    }

    public abstract Map<String, Object> getOptions(FacesContext facesContext, UIComponent component);

    public abstract String getScriptName();

    public abstract DnDScript createScript(String name);

    public String getParentClientId(FacesContext facesContext, UIComponent component) {
        UIComponent parent = component.getParent();
        return (parent != null) ? parent.getClientId(facesContext) : "";
    }

    private DnDScript buildClientScript(FacesContext facesContext, UIComponent component) {
        DnDScript script = null;
        String scriptName = getScriptName();
        if (!"".equals(scriptName)) {
            JSFunction function = new JSFunction(scriptName);
            function.addParameter(component.getClientId(facesContext));
            function.addParameter(getOptions(facesContext, component));
            script = createScript(function.toScript());
        }
        return script;
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        buildAndStoreScript(context, component);
    }
}
