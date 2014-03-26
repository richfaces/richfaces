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
package org.richfaces.renderkit.util;

import static org.richfaces.renderkit.util.AjaxRendererUtils.buildAjaxFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorContext.Parameter;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSReference;
import org.ajax4jsf.javascript.ScriptString;
import org.richfaces.util.RendererUtils;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public final class HandlersChain {
    /**
     *
     */
    private static final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();
    // private static final Logger LOG = RichfacesLogger.RENDERKIT.getLogger();
    private boolean hasSubmittingBehavior = false;
    private boolean includeClientId = false;
    // TODO: review for optimization
    private List<String> handlers = new ArrayList<String>(2);
    private FacesContext facesContext;
    private UIComponent component;
    private Collection<Parameter> parameters;

    public HandlersChain(FacesContext facesContext, UIComponent component) {
        this.facesContext = facesContext;
        this.component = component;
    }

    public HandlersChain(FacesContext facesContext, UIComponent component, boolean includeClientId) {
        this.facesContext = facesContext;
        this.component = component;
        this.includeClientId = includeClientId;
    }

    public HandlersChain(FacesContext facesContext, UIComponent component, Collection<Parameter> parameters) {
        this.facesContext = facesContext;
        this.component = component;
        this.parameters = parameters;
    }

    public HandlersChain(FacesContext facesContext, UIComponent component, Collection<Parameter> parameters,
        boolean includeClientId) {
        this.facesContext = facesContext;
        this.component = component;
        this.parameters = parameters;
        this.includeClientId = includeClientId;
    }

    private static boolean isNotEmpty(String s) {
        return (s != null) && (s.length() != 0);
    }

    private List<ClientBehavior> getBehaviorsList(String behaviorName) {
        List<ClientBehavior> behaviors = null;

        if (component instanceof ClientBehaviorHolder) {
            ClientBehaviorHolder clientBehaviorHolder = (ClientBehaviorHolder) component;
            Map<String, List<ClientBehavior>> clientBehaviorsMap = clientBehaviorHolder.getClientBehaviors();

            if (clientBehaviorsMap != null) {
                behaviors = clientBehaviorsMap.get(behaviorName);
            }
        }

        return behaviors;
    }

    private Collection<Parameter> getParameters() {
        if (parameters == null) {
            Map<String, Object> parametersMap = RENDERER_UTILS.createParametersMap(facesContext, component);

            parameters = createParametersList(parametersMap);
        }

        return parameters;
    }

    public boolean hasSubmittingBehavior() {
        return hasSubmittingBehavior;
    }

    public void addInlineHandlerAsValue(String handlerValue) {
        if (isNotEmpty(handlerValue)) {
            handlers.add(handlerValue);
        }
    }

    public void addInlineHandlerFromAttribute(String attributeName) {
        addInlineHandlerAsValue((String) component.getAttributes().get(attributeName));
    }

    public void addBehaviors(String domEventName) {
        addBehaviors(domEventName, null);
    }

    public void addBehaviors(String domEventName, String logicalEventName) {
        String name = domEventName;
        List<ClientBehavior> behaviorsList = getBehaviorsList(domEventName);

        if ((behaviorsList == null) && (logicalEventName != null)) {
            behaviorsList = getBehaviorsList(logicalEventName);
            name = logicalEventName;
        }

        if (behaviorsList == null) {
            return;
        }

        ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(facesContext, component,
            name, includeClientId ? component.getClientId(facesContext) : null, getParameters());

        for (ClientBehavior clientBehavior : behaviorsList) {
            String behaviorScript = clientBehavior.getScript(behaviorContext);

            if (isNotEmpty(behaviorScript)) {
                if (clientBehavior.getHints().contains(ClientBehaviorHint.SUBMITTING)) {
                    hasSubmittingBehavior = true;
                }

                handlers.add(behaviorScript);
            }
        }
    }

    public void addAjaxSubmitFunction() {
        if (!this.hasSubmittingBehavior()) {
            hasSubmittingBehavior = true;

            ScriptString ajaxFunction = buildAjaxFunction(facesContext, component);
            this.addInlineHandlerAsValue(ajaxFunction.toScript());
        }
    }

    public String toScript() {
        String result = null;

        if (!handlers.isEmpty()) {
            if (handlers.size() == 1) {
                result = handlers.get(0);
            } else {
                JSFunction jsFunction = new JSFunction("jsf.util.chain", JSReference.THIS, JSReference.EVENT);

                for (String handler : handlers) {
                    jsFunction.addParameter(handler);
                }

                result = jsFunction.toScript();
            }
        }

        return result;
    }

    public static List<Parameter> createParametersList(Map<String, Object> parametersMap) {
        List<Parameter> parameters = new ArrayList<Parameter>(parametersMap.size());

        for (Entry<String, Object> entry : parametersMap.entrySet()) {
            parameters.add(new Parameter(entry.getKey(), entry.getValue()));
        }

        return parameters;
    }
}
