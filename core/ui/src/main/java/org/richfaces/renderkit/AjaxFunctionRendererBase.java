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

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSReference;
import org.ajax4jsf.javascript.ScriptUtils;
import org.richfaces.renderkit.util.AjaxRendererUtils;

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
            throw new FacesException("Value of 'name' attribute of a4j:jsFunction component is null!");
        }

        StringBuffer script = new StringBuffer(functionName).append("=");
        JSFunctionDefinition func = new JSFunctionDefinition();

        // func.setName(component.getName());
        // Create AJAX Submit function.
        JSFunction ajaxFunction = AjaxRendererUtils.buildAjaxFunction(context, component,
                                      AjaxRendererUtils.AJAX_FUNCTION_NAME);
        AjaxEventOptions options = AjaxRendererUtils.buildEventOptions(context, component);

        if (options.hasParameters()) {
            Map<String, Object> parameters = options.getParameters();

//          if (null == parameters) {
//              parameters = new HashMap<String, Object>();
//              options.put("parameters", parameters);
//          }
            // Fill parameters.
            for (Iterator<UIComponent> it = component.getChildren().iterator(); it.hasNext(); ) {
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

        if (!options.isEmpty()) {
            ajaxFunction.addParameter(options);
        }

        // TODO - added in 4.0 - ?
        func.addParameter(JSReference.EVENT);
        func.addToBody(ajaxFunction.toScript());
        func.appendScript(script);

        return script.toString();
    }
}
