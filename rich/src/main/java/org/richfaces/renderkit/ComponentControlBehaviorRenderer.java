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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.render.FacesBehaviorRenderer;
import javax.faces.render.RenderKitFactory;

import org.richfaces.component.UIHashParameter;
import org.richfaces.component.behavior.ComponentControlBehavior;
import org.richfaces.javascript.JSFunction;
import org.richfaces.javascript.JSFunctionDefinition;
import org.richfaces.javascript.JSReference;
import org.richfaces.javascript.ScriptUtils;
import org.richfaces.util.RendererUtils;

/**
 * @author Anton Belevich
 *
 */
@FacesBehaviorRenderer(rendererType = "org.richfaces.behavior.ComponentControlBehavior", renderKitId = RenderKitFactory.HTML_BASIC_RENDER_KIT)
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.rf4.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "component-control.js") })
public class ComponentControlBehaviorRenderer extends ClientBehaviorRenderer {
    /**
     *
     */
    private static final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();
    private static final String FUNC_NAME = "RichFaces.rf4.ui.ComponentControl.execute";
    private static final String REF_EVENT = "event";
    private static final String REF_COMPONENT = "component";
    private static final String PARAM_CALLBACK = "callback";
    private static final String PARAM_TARGET = "target";
    private static final String PARAM_SELECTOR = "selector";
    private static final String PARAM_ONBEFOREOPERATION = "onbeforeoperation";
    private static final Pattern COMMA_SEPARATED_STRING = Pattern.compile("\\s*,\\s*");

    private boolean isEmpty(String value) {
        return (value == null || value.trim().length() == 0);
    }

    @Override
    public String getScript(ClientBehaviorContext behaviorContext, ClientBehavior behavior) {
        FacesContext facesContext = behaviorContext.getFacesContext();

        ComponentControlBehavior controlBehavior = (ComponentControlBehavior) behavior;
        String apiFunctionName = controlBehavior.getOperation();
        String targetSourceString = controlBehavior.getTarget();
        String selector = controlBehavior.getSelector();
        // Fix https://issues.jboss.org/browse/RF-9745
        if (isEmpty(apiFunctionName) || (isEmpty(targetSourceString) && isEmpty(selector))) {
            throw new IllegalArgumentException(
                "One of the necessary attributes is null or empty. Check operation attribute and selector or target attributes.");
        }

        JSFunctionDefinition callback = new JSFunctionDefinition();
        callback.addParameter(new JSReference(REF_EVENT));
        callback.addParameter(new JSReference(REF_COMPONENT));

        // create callback function
        StringBuffer script = new StringBuffer();
        script.append(REF_COMPONENT).append("['").append(apiFunctionName).append("'].").append("apply").append("(");

        // get client api function parameters
        List<Object> apiFunctionParams = createSignature(controlBehavior);
        script.append(REF_COMPONENT).append(",").append(ScriptUtils.toScript(apiFunctionParams.toArray())).append(");");
        callback.addToBody(script);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(PARAM_CALLBACK, callback);
        parameters.put(PARAM_TARGET, resolveTargets(facesContext, targetSourceString));
        parameters.put(PARAM_SELECTOR, selector);

        String onBeforeOperation = controlBehavior.getOnbeforeoperation();
        if (null != onBeforeOperation && !onBeforeOperation.isEmpty()) {
            JSFunctionDefinition onBeforeOperationFunction = new JSFunctionDefinition(new JSReference(REF_EVENT));
            onBeforeOperationFunction.addToBody(onBeforeOperation);
            parameters.put(PARAM_ONBEFOREOPERATION, onBeforeOperationFunction);
        }

        // execution function
        JSFunction eventFunction = new JSFunction(FUNC_NAME);
        eventFunction.addParameter(new JSReference(REF_EVENT));
        eventFunction.addParameter(parameters);

        StringBuffer execution = new StringBuffer();
        execution.append(eventFunction.toScript());
        execution.append("; return false;");
        return execution.toString();
    }

    protected List<Object> createSignature(ComponentControlBehavior behavior) {
        List<Object> elements = new ArrayList<Object>();
        List<UIComponent> children = behavior.getChildren();

        for (UIComponent child : children) {
            if (child instanceof UIParameter) {
                UIParameter parameter = (UIParameter) child;
                Object value = RendererUtils.getInstance().createParameterValue(parameter);

                if (value != null) {
                    elements.add(value);
                }
            }

            if (child instanceof UIHashParameter) {
                UIHashParameter parameter = (UIHashParameter) child;
                String name = parameter.getName();

                Map<String, Object> value = RendererUtils.getInstance().createParametersMap(FacesContext.getCurrentInstance(),
                    child);

                if (value != null) {
                    if (name != null) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(name, value);
                        elements.add(map);
                    } else {
                        elements.add(value);
                    }
                }
            }
        }
        return elements;
    }

    protected String[] resolveTargets(FacesContext context, String targetSourceString) {
        if (targetSourceString == null) {
            return null;
        }

        // TODO nick - externalize to utility method
        String[] split = COMMA_SEPARATED_STRING.split(targetSourceString);
        UIComponent contextComponent = UIComponent.getCurrentComponent(context);
        if (contextComponent == null) {
            contextComponent = context.getViewRoot();
        }

        List<String> resultHolder = new ArrayList<String>(split.length);

        for (String target : split) {
            UIComponent targetComponent = RENDERER_UTILS.findComponentFor(contextComponent, target);

            String targetClientId;
            if (targetComponent != null) {
                targetClientId = targetComponent.getClientId(context);
            } else {
                targetClientId = target;
            }

            resultHolder.add(targetClientId);
        }

        return resultHolder.toArray(new String[resultHolder.size()]);
    }
}
