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

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.AjaxClientBehavior;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSReference;
import org.richfaces.component.AbstractActionComponent;
import org.richfaces.component.BasicActionComponent;
import org.richfaces.component.attribute.AjaxProps;
import org.richfaces.renderkit.AjaxConstants;
import org.richfaces.renderkit.AjaxFunction;
import org.richfaces.renderkit.AjaxOptions;
import org.richfaces.renderkit.HtmlConstants;

import com.google.common.base.Strings;

/**
 * @author shura
 *         <p/>
 *         Some utilites for render AJAX components.
 */
public final class AjaxRendererUtils {
    public static final String BEGIN_EVENT_NAME = "begin";
    public static final String AJAX_ABORT_ATTR = "ignoreDupResponses";
    public static final String AJAX_AREAS_RENDERED = "org.ajax4jsf.areas.rendered";
    public static final String AJAX_DELAY_ATTR = "requestDelay";
    public static final String AJAX_QUEUE_ATTR = "eventsQueue";
    public static final String AJAX_SINGLE_ATTR = "ajaxSingle";
    public static final String AJAX_SINGLE_PARAMETER_NAME = "ajaxSingle";
    public static final String ERROR_EVENT_NAME = "error";
    public static final String ONBEGIN_ATTR_NAME = "onbegin";
    public static final String ONERROR_ATTR_NAME = "onerror";
    /**
     * Attribute for keep JavaScript function name for call after complete request.
     */
    public static final String ONCOMPLETE_CONTENT_ID = "org.ajax4jsf.oncomplete";
    public static final String SIMILARITY_GROUPING_ID_ATTR = "similarityGroupingId";
    /**
     * Attribute for keep clientId of status component
     */
    public static final String STATUS_ATTR_NAME = "status";
    public static final String VALUE_ATTR = "value";
    public static final String QUEUE_ID_ATTRIBUTE = "queueId";
    private static final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();

    /**
     * Static class - protect constructor
     */
    private AjaxRendererUtils() {
    }

    private static enum BehaviorOptionsData {
        begin {
            @Override
            public String getAttributeValue(AjaxClientBehavior behavior) {
                return behavior.getOnbegin();
            }
        },
        error {
            @Override
            public String getAttributeValue(AjaxClientBehavior behavior) {
                return behavior.getOnerror();
            }
        },
        queueId {
            @Override
            public String getAttributeValue(AjaxClientBehavior behavior) {
                return behavior.getQueueId();
            }
        };

        public abstract String getAttributeValue(AjaxClientBehavior behavior);
    }

    /**
     * Build JavaScript onclick event for given component
     *
     * @param uiComponent - component for build event
     * @param facesContext
     * @return <code>StringBuffer</code> with Javascript code
     */
    public static StringBuffer buildOnClick(UIComponent uiComponent, FacesContext facesContext) {
        return buildOnClick(uiComponent, facesContext, false);
    }

    /**
     * Build JavaScript onclick event for given component
     *
     * @param uiComponent - component for build event
     * @param facesContext
     * @param omitDefaultActionUrl - default action URL is not encoded if parameter is true
     * @return <code>StringBuffer</code> with Javascript code
     */
    public static StringBuffer buildOnClick(UIComponent uiComponent, FacesContext facesContext, boolean omitDefaultActionUrl) {
        return buildOnEvent(uiComponent, facesContext, HtmlConstants.ONCLICK_ATTRIBUTE, omitDefaultActionUrl);
    }

    /**
     * Build JavaScript event for component
     *
     * @param uiComponent - component for build event
     * @param facesContext
     * @param eventName - name of event
     * @return <code>StringBuffer</code> with Javascript code
     */
    public static StringBuffer buildOnEvent(UIComponent uiComponent, FacesContext facesContext, String eventName) {
        return buildOnEvent(uiComponent, facesContext, eventName, false);
    }

    /**
     * Build JavaScript event for component
     *
     * @param uiComponent - component for build event
     * @param facesContext
     * @param eventName - name of event
     * @param omitDefaultActionUrl - default action URL is not encoded if parameter is true
     * @return <code>StringBuffer</code> with Javascript code
     */
    public static StringBuffer buildOnEvent(UIComponent uiComponent, FacesContext facesContext, String eventName,
        boolean omitDefaultActionUrl) {
        StringBuffer onEvent = new StringBuffer();

        // if (null != eventName) {
        // String commandOnEvent = (String) uiComponent.getAttributes().get(
        // eventName);
        // if (commandOnEvent != null) {
        // onEvent.append(commandOnEvent);
        // onEvent.append(';');
        // }
        // }
        // JSFunction ajaxFunction = buildAjaxFunction(uiComponent, facesContext);
        // // Create formal parameter for non-input elements ???
        // // Link Control pseudo-object
        // // Options map. Possible options for function call :
        // // control - name of form control for submit.
        // // name - name for link control \
        // // value - value of control. - possible replace by parameters ?
        // // single true/false - submit all form or only one control.
        // // affected - array of element's ID for update on responce.
        // // oncomplete - function for call after complete request.
        // // status - id of request status component.
        // // parameters - map of parameters name/value for append on request.
        // // ..........
        // ajaxFunction.addParameter(buildEventOptions(facesContext, uiComponent, omitDefaultActionUrl));
        //
        // // appendAjaxSubmitParameters(facesContext, uiComponent, onEvent);
        // ajaxFunction.appendScript(onEvent);
        // if (uiComponent instanceof AjaxSupport) {
        // AjaxSupport support = (AjaxSupport) uiComponent;
        // if (support.isDisableDefault()) {
        // onEvent.append("; return false;");
        // }
        // }
        // LOG.debug(Messages.getMessage(Messages.BUILD_ONCLICK_INFO, uiComponent
        // .getId(), onEvent.toString()));
        return onEvent;
    }

    // TODO make this function private
    public static AjaxOptions buildEventOptions(FacesContext facesContext, UIComponent component) {
        AjaxOptions ajaxOptions = new AjaxOptions();
        appendComponentOptions(facesContext, component, ajaxOptions);

        Map<String, Object> parametersMap = RENDERER_UTILS.createParametersMap(facesContext, component);
        ajaxOptions.addParameters(parametersMap);

        if (component instanceof BasicActionComponent) {
            if (((BasicActionComponent) component).isResetValues()) {
                ajaxOptions.setParameter(AjaxConstants.RESET_VALUES_PARAMETER, true);
            }
        }

        return ajaxOptions;
    }

    private static AjaxOptions buildAjaxOptions(ClientBehaviorContext behaviorContext, AjaxClientBehavior ajaxBehavior) {
        FacesContext facesContext = behaviorContext.getFacesContext();
        UIComponent component = behaviorContext.getComponent();

        AjaxOptions ajaxOptions = new AjaxOptions();

        Map<String, Object> parametersMap = RENDERER_UTILS.createParametersMap(facesContext, component);
        ajaxOptions.addParameters(parametersMap);

        String ajaxStatusName = ajaxBehavior.getStatus();
        if (Strings.isNullOrEmpty(ajaxStatusName)) {
            ajaxStatusName = getAjaxStatus(component);
        }
        if (!Strings.isNullOrEmpty(ajaxStatusName)) {
            ajaxOptions.set(STATUS_ATTR_NAME, ajaxStatusName);
        }

        appenAjaxBehaviorOptions(behaviorContext, ajaxBehavior, ajaxOptions);

        return ajaxOptions;
    }

    private static boolean isNotEmpty(String value) {
        return (value != null) && (value.length() != 0);
    }

    private static void appenAjaxBehaviorOptions(ClientBehaviorContext behaviorContext, AjaxClientBehavior behavior,
        AjaxOptions ajaxOptions) {
        ajaxOptions.setParameter(AjaxConstants.BEHAVIOR_EVENT_PARAMETER, behaviorContext.getEventName());
        ajaxOptions.setBeforesubmitHandler(behavior.getOnbeforesubmit());

        for (BehaviorOptionsData optionsData : BehaviorOptionsData.values()) {
            String optionValue = optionsData.getAttributeValue(behavior);

            if (isNotEmpty(optionValue)) {
                ajaxOptions.set(optionsData.toString(), optionValue);
            }
        }
    }

    private static String getHandlerScript(FacesContext facesContext, UIComponent component, String attributeName,
        String eventName) {
        HandlersChain handlersChain = new HandlersChain(facesContext, component);
        String inlineHandler = (String) component.getAttributes().get(attributeName);

        handlersChain.addInlineHandlerAsValue(inlineHandler);
        handlersChain.addBehaviors(eventName);

        return handlersChain.toScript();
    }

    private static void appendComponentOptions(FacesContext facesContext, UIComponent component, AjaxOptions ajaxOptions) {
        String handlerScript = getHandlerScript(facesContext, component, ONBEGIN_ATTR_NAME, BEGIN_EVENT_NAME);
        if (!Strings.isNullOrEmpty(handlerScript)) {
            ajaxOptions.set(BEGIN_EVENT_NAME, handlerScript);
        }

        handlerScript = getHandlerScript(facesContext, component, ONERROR_ATTR_NAME, ERROR_EVENT_NAME);
        if (!Strings.isNullOrEmpty(handlerScript)) {
            ajaxOptions.set(ERROR_EVENT_NAME, handlerScript);
        }

        String queueId = getQueueId(component);
        if (!Strings.isNullOrEmpty(queueId)) {
            ajaxOptions.set(QUEUE_ID_ATTRIBUTE, queueId);
        }

        ajaxOptions.set("incId", "1");

        String status = getAjaxStatus(component);
        if (!Strings.isNullOrEmpty(status)) {
            ajaxOptions.set(STATUS_ATTR_NAME, status);
        }
    }

    // public static AjaxEventOptions buildEventOptions(FacesContext facesContext,
    // UIComponent uiComponent, Map<String, Object> params) {
    //
    // return buildEventOptions(facesContext, uiComponent, params, false);
    // }

    /**
     * @param facesContext
     * @param uiComponent
     * @return
     */
    // public static Map<String, Object> buildEventOptions(FacesContext facesContext,
    // UIComponent uiComponent, Map<String, Object> params, boolean omitDefaultActionUrl) {
    // String clientId = uiComponent.getClientId(facesContext);
    // Map<String, Object> componentAttributes = uiComponent.getAttributes();
    // Map<String, Object> options = new HashMap<String, Object>();
    //
    // UIComponent nestingContainer = (UIComponent) findAjaxContainer(
    // facesContext, uiComponent);
    // String containerClientId = nestingContainer.getClientId(facesContext);
    // if (containerClientId != null && !AjaxViewRoot.ROOT_ID.equals(containerClientId)) {
    // options.put("containerId", containerClientId);
    // }
    //
    // Map<String, Object> parameters = new HashMap<String, Object>();
    // UIComponent targetComponent = (uiComponent instanceof AjaxSupport)?uiComponent.getParent():uiComponent;
    // // UIForm form = getNestingForm(uiComponent);
    // // "input" - if assigned to html input element.
    // boolean input = targetComponent instanceof EditableValueHolder;
    // // Action component - button etc.
    // // boolean action = targetComponent instanceof ActionSource;
    //
    // boolean ajaxSingle = Boolean.TRUE.equals(componentAttributes
    // .get(AJAX_SINGLE_ATTR));
    // // For input components in single mode or without form submit input
    // // control )
    // if (ajaxSingle ) {
    // parameters.put(AJAX_SINGLE_PARAMETER_NAME, targetComponent.getClientId(facesContext));
    // // options.put("single", JSReference.TRUE);
    // if (input) {
    // options.put("control", JSReference.THIS);
    // }
    // }
    // // Control value for submit
    // String controlName;
    // Object controlValue;
    // // TODO - make compatible with JSF RI/MyFaces ? use submittedValue ( if
    // // any ) for UIInput, converted value for ValueHolder.
    // controlName = clientId;
    // controlValue = clientId;
    // parameters.put(controlName, controlValue);
    // AjaxContext ajaxContext = AjaxContext.getCurrentInstance(facesContext);
    //
    // String ajaxActionURL = ajaxContext.getAjaxActionURL(facesContext);
    // if (omitDefaultActionUrl) {
    // UIComponent form = getNestingForm(uiComponent);
    // if (form != null && !RENDERER_UTILS.isBooleanAttribute(form, "ajaxSubmit")) {
    // if (RENDERER_UTILS.getActionUrl(facesContext).equals(ajaxActionURL)) {
    // ajaxActionURL = null;
    // }
    // }
    // }
    //
    // if (ajaxActionURL != null) {
    // // Setup action URL. For portlet environment, it will be different from
    // // page.
    // options.put("actionUrl", ajaxActionURL);
    // }
    //
    // // Add application-wide Ajax parameters
    // parameters.putAll(ajaxContext.getCommonAjaxParameters());
    // // add child parameters
    // appendParameters(facesContext, uiComponent, parameters);
    //
    // if (params != null) {
    // parameters.putAll(params);
    // }
    //
    // if (!parameters.isEmpty()) {
    // options.put("parameters", parameters);
    // }
    // // parameter to render only current list of areas.
    // // if (isAjaxLimitRender(uiComponent)) {
    // // Set<? extends Object> ajaxAreas = getAjaxAreas(uiComponent);
    // // Set<String> areasIds = new HashSet<String>();
    // // if (null != ajaxAreas) {
    // // for (Iterator<? extends Object> iter = ajaxAreas.iterator(); iter.hasNext();) {
    // // String id = (String) iter.next();
    // // UIComponent comp = RendererUtils.getInstance().
    // // findComponentFor(uiComponent, id);
    // // if (null != comp) {
    // // areasIds.add(comp.getClientId(facesContext));
    // // } else {
    // // areasIds.add(id);
    // // }
    // // }
    // // }
    // // options.put("affected", areasIds);
    // // }
    // String oncomplete = getAjaxOncomplete(uiComponent);
    // if (null != oncomplete) {
    // options.put(ONCOMPLETE_ATTR_NAME, buildAjaxOncomplete(oncomplete));
    // }
    //
    // String beforeupdate = getAjaxOnBeforeDomUpdate(uiComponent);
    // if (null != beforeupdate) {
    // options.put(ONBEFOREDOMUPDATE_ATTR_NAME, buildAjaxOnBeforeDomUpdate(beforeupdate));
    // }
    //
    //
    // String status = getAjaxStatus(uiComponent);
    // if (null != status) {
    // options.put("status", status);
    // }
    // String queue = (String) componentAttributes.get(AJAX_QUEUE_ATTR);
    // String implicitQueue = null;
    //
    // Integer requestDelay = (Integer) componentAttributes
    // .get(AJAX_DELAY_ATTR);
    // if (null != requestDelay && requestDelay.intValue() > 0) {
    // options.put(AJAX_DELAY_ATTR, requestDelay);
    // if (null == queue) {
    // implicitQueue = clientId;
    // }
    // }
    // Boolean ignoreDupResponses = (Boolean) componentAttributes
    // .get(AJAX_ABORT_ATTR);
    // if (null != ignoreDupResponses && ignoreDupResponses.booleanValue()) {
    // options.put(AJAX_ABORT_ATTR, JSReference.TRUE);
    // if (null == queue) {
    // implicitQueue = clientId;
    // }
    // }
    //
    // if (null != queue) {
    // options.put(AJAX_QUEUE_ATTR, queue);
    // } else if (implicitQueue != null) {
    // options.put("implicitEventsQueue", clientId);
    // }
    //
    // ExternalContext externalContext = facesContext.getExternalContext();
    // String namespace = externalContext.encodeNamespace("");
    // if (namespace != null && namespace.length() != 0) {
    // options.put("namespace", namespace);
    // }
    //
    // String similarityGroupingId = (String) componentAttributes.get(SIMILARITY_GROUPING_ID_ATTR);
    // if (similarityGroupingId == null || similarityGroupingId.length() == 0) {
    // similarityGroupingId = clientId;
    // } else {
    // similarityGroupingId = externalContext.encodeNamespace(similarityGroupingId);
    // }
    //
    // options.put(SIMILARITY_GROUPING_ID_ATTR, similarityGroupingId);
    //
    // // request timeout.
    // Integer timeout = (Integer) componentAttributes.get("timeout");
    // if (null != timeout && timeout.intValue() > 0) {
    // options.put("timeout", timeout);
    // }
    // // Encoding for requests
    // String encoding = (String) componentAttributes.get("encoding");
    // if (null != encoding) {
    // options.put("encoding", encoding);
    // }
    // return options;
    // }
    // /**
    // * Create call to Ajax Submit function with first two parameters
    // *
    // * @param uiComponent
    // * @param facesContext
    // * @param functionName
    // * @return
    // */
    // public static JSFunction buildAjaxFunction(UIComponent uiComponent,
    // FacesContext facesContext) {
    // JSFunction ajaxFunction = buildAjaxFunction(uiComponent, facesContext,
    // AJAX_FUNCTION_NAME);
    // // client-side script must have reference to event-enabled object.
    // ajaxFunction.addParameter(new JSReference("event"));
    // return ajaxFunction;
    // }

    /**
     * Create call to Ajax Submit function with first two parameters
     *
     * @param facesContext
     * @param component
     * @return
     */
    public static AjaxFunction buildAjaxFunction(FacesContext facesContext, UIComponent component) {
        return new AjaxFunction(component.getClientId(facesContext), buildEventOptions(facesContext, component));
    }

    public static AjaxFunction buildAjaxFunction(ClientBehaviorContext behaviorContext, AjaxClientBehavior behavior) {
        Object source;

        AjaxOptions options = buildAjaxOptions(behaviorContext, behavior);

        if (behaviorContext.getSourceId() != null) {
            source = behaviorContext.getSourceId();
        } else {
            source = JSReference.THIS;

            FacesContext facesContext = behaviorContext.getFacesContext();
            UIComponent component = behaviorContext.getComponent();

            options.setAjaxComponent(component.getClientId(facesContext));
            options.set("sourceId", source);
        }

        return new AjaxFunction(source, options);
    }

    /**
     * Append common parameters ( array of affected areas, status area id, on complete function ) to JavaScript event string.
     *
     * @param uiComponent
     * @param onClick - buffer with JavaScript code eg... AJAX.Submit(form,this
     */

    // public static void appendAjaxSubmitParameters(FacesContext facesContext,
    // UIComponent uiComponent, StringBuffer onClick)
    // {
    // Set ajaxAreas = getAjaxAreas(uiComponent);
    // onClick.append(',');
    // // parameter to render only current list of areas.
    // if (isAjaxLimitRender(uiComponent) && ajaxAreas != null &&
    // ajaxAreas.size() > 0)
    // {
    // onClick.append('[');
    // Iterator areas = ajaxAreas.iterator();
    // boolean first = true;
    // while (areas.hasNext())
    // {
    // String element = (String) areas.next();
    // UIComponent component = uiComponent.findComponent(element);
    // if (null != component)
    // {
    // if (!first)
    // {
    // onClick.append(',');
    // }
    // else
    // {
    // first = false;
    // }
    // onClick.append('\'');
    // onClick.append(component.getClientId(facesContext));
    // onClick.append('\'');
    // }
    // }
    // onClick.append("]");
    // }
    // else
    // {
    // onClick.append("null");
    // }
    // // insert id of request status element.
    // onClick.append(',');
    // String status = getAjaxStatus(uiComponent);
    // if (null != status)
    // {
    // onClick.append('\'').append(status).append('\'');
    // }
    // else
    // {
    // onClick.append("null");
    // }
    // // insert function name for call after completed request
    // onClick.append(',');
    // String oncomplete = getAjaxOncomplete(uiComponent);
    // if (null != oncomplete)
    // {
    // onClick.append(oncomplete);
    // }
    // else
    // {
    // onClick.append("null");
    // }
    //
    // }

    /**
     * Get status area Id for given component.
     *
     * @param component
     * @return clientId of status area, or <code>null</code>
     */
    public static String getAjaxStatus(UIComponent component) {
        return (String) component.getAttributes().get(STATUS_ATTR_NAME);
    }

    public static String getQueueId(UIComponent component) {
        return (String) component.getAttributes().get(QUEUE_ID_ATTRIBUTE);
    }

    public static JSFunctionDefinition buildAjaxOncomplete(String body) {
        JSFunctionDefinition function = new JSFunctionDefinition("request", "event", "data");

        function.addToBody(body);

        return function;
    }

    public static JSFunctionDefinition buildAjaxOnBeforeDomUpdate(String body) {
        JSFunctionDefinition function = new JSFunctionDefinition("request", "event", "data");

        function.addToBody(body);

        return function;
    }
}
