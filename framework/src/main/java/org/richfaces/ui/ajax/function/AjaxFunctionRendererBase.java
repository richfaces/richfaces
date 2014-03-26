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
package org.richfaces.ui.ajax.function;

import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSReference;
import org.ajax4jsf.javascript.ScriptUtils;
import org.richfaces.ui.ajax.command.AjaxCommandRendererBase;
import org.richfaces.ui.common.AjaxFunction;
import org.richfaces.ui.common.AjaxOptions;
import org.richfaces.util.AjaxRendererUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;

import java.util.Iterator;
import java.util.Map;

/**
 * @author shura
 *
 */
public abstract class AjaxFunctionRendererBase extends AjaxCommandRendererBase {
    public String getFunction(FacesContext context, UIComponent component) {
        String functionName = (String) component.getAttributes().get("name");

        if (functionName == null) {
            throw new FacesException("Value of 'name' attribute of r:jsFunction component is null!");
        }

        StringBuilder script = new StringBuilder(functionName).append("=");
        JSFunctionDefinition func = new JSFunctionDefinition();

        // func.setName(component.getName());
        // Create AJAX Submit function.
        AjaxFunction ajaxFunction = AjaxRendererUtils.buildAjaxFunction(context, component);
        ajaxFunction.setEvent(null);
        AjaxOptions options = ajaxFunction.getOptions();

        if (options.hasParameters()) {
            Map<String, Object> parameters = options.getParameters();

            // Fill parameters.
            for (Iterator<UIComponent> it = component.getChildren().iterator(); it.hasNext();) {
                UIComponent child = it.next();

                if (child instanceof UIParameter) {
                    UIParameter parameter = (UIParameter) child;
                    String name = parameter.getName();

                    func.addParameter(name);

                    // Put parameter name to AJAX.Submit parameter, with default value.
                    JSReference reference = new JSReference(name);

                    if (null != parameter.getValue()) {
                        reference = new JSReference(name + "||" + ScriptUtils.toScript(parameters.get(name)));
                    }

                    // Replace parameter value to reference.
                    parameters.put(name, reference);
                }
            }
        }

        // TODO - added in 4.0 - ?
        func.addToBody(ajaxFunction.toScript());
        func.appendScriptToStringBuilder(script);

        return script.toString();
    }
}
