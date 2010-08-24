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

package org.ajax4jsf.renderkit;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletResponse;

import org.ajax4jsf.Messages;
import org.ajax4jsf.component.AjaxClientBehavior;
import org.ajax4jsf.component.AjaxComponent;
import org.ajax4jsf.component.AjaxContainer;
import org.ajax4jsf.component.AjaxLoadBundleComponent;
import org.ajax4jsf.component.AjaxViewRoot;
import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSReference;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.application.ServiceTracker;
import org.richfaces.log.RichfacesLogger;
import org.slf4j.Logger;

/**
 * @author shura
 *         <p/>
 *         Some utilites for render AJAX components.
 */
public final class AjaxRendererUtils {

    public static final String AJAX_ABORT_ATTR = "ignoreDupResponses";
    public static final String AJAX_AREAS_RENDERED = "org.ajax4jsf.areas.rendered";
    public static final String AJAX_DELAY_ATTR = "requestDelay";

    /**
     * Name Javasript function for submit AJAX request
     */
    public static final String AJAX_FUNCTION_NAME = "RichFaces.ajax";

    /**
     * @since 3.3.0
     */
    public static final String AJAX_PROCESS_ATTRIBUTE = "process";
    public static final String AJAX_QUEUE_ATTR = "eventsQueue";
    public static final String AJAX_REGIONS_ATTRIBUTE = "reRender";
    public static final String AJAX_SINGLE_ATTR = "ajaxSingle";
    public static final String AJAX_SINGLE_PARAMETER_NAME = "ajaxSingle";
    public static final String ALL = "@all";
    public static final String FORM = "@form";
    public static final String THIS = "@this";
    public static final String REGION = "@region";
    public static final String NONE = "@none";

    public static final Set<String> GLOBAL_META_COMPONENTS;

    static {
        GLOBAL_META_COMPONENTS = new HashSet<String>(2);

        GLOBAL_META_COMPONENTS.add(ALL);
        GLOBAL_META_COMPONENTS.add(NONE);
    }

    /**
     * Attribute to keep
     */
    public static final String LIMITRENDER_ATTR_NAME = "limitRender";

    /**
     * Attribute for keep JavaScript function name for call before updating
     * DOM tree.
     */
    public static final String ONBEFOREDOMUPDATE_ATTR_NAME = "onbeforedomupdate";
    public static final String ONBEGIN_ATTR_NAME = "onbegin";

    /**
     * Attribute for keep JavaScript function name for call after complete
     * request.
     */
    public static final String ONCOMPLETE_ATTR_NAME = "oncomplete";

    public static final String DATA_ATTR_NAME = "data";

    /**
     * Attribute for keep JavaScript function name for call after complete
     * request.
     */
    public static final String ONCOMPLETE_CONTENT_ID = "org.ajax4jsf.oncomplete";
    public static final String SIMILARITY_GROUPING_ID_ATTR = "similarityGroupingId";

    /**
     * Attribute for keep clientId of status component
     */
    public static final String STATUS_ATTR_NAME = "status";
    public static final String VALUE_ATTR = "value";

    public static final String EXTENSION_ID = "org.richfaces.extension";
    public static final String AJAX_COMPONENT_ID_PARAMETER = "org.richfaces.ajax.component";
    public static final String BEHAVIOR_EVENT_PARAMETER = "javax.faces.behavior.event";

    public static final String QUEUE_ID_ATTRIBUTE = "queueId";

    private static final String BEFOREDOMUPDATE_ELEMENT_NAME = "beforedomupdate";
    private static final String COMPLETE_ELEMENT_NAME = "complete";
    private static final String DATA_ELEMENT_NAME = "data";
    private static final String COMPONENT_DATA_ELEMENT_NAME = "componentData";

    private static final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();
    private static final Class<?> OBJECT_ARRAY_CLASS = new Object[0].getClass();
    private static final Logger LOG = RichfacesLogger.RENDERKIT.getLogger();

    /**
     * Static class - protect constructor
     */
    private AjaxRendererUtils() {
    }

    private static enum BehaviorEventOptionsData {
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
        },
        event {
            @Override
            public String getAttributeValue(AjaxClientBehavior behavior) {
                return behavior.getOnevent();
            }
        };

        public abstract String getAttributeValue(AjaxClientBehavior behavior);
    }

    /**
     * Build JavaScript onclick event for given component
     *
     * @param uiComponent  -
     *                     component for build event
     * @param facesContext
     * @return <code>StringBuffer</code> with Javascript code
     */
    public static StringBuffer buildOnClick(UIComponent uiComponent, FacesContext facesContext) {
        return buildOnClick(uiComponent, facesContext, false);
    }

    /**
     * Build JavaScript onclick event for given component
     *
     * @param uiComponent          -
     *                             component for build event
     * @param facesContext
     * @param omitDefaultActionUrl - default action URL is not encoded if parameter is true
     * @return <code>StringBuffer</code> with Javascript code
     */
    public static StringBuffer buildOnClick(UIComponent uiComponent, FacesContext facesContext,
                                            boolean omitDefaultActionUrl) {
        return buildOnEvent(uiComponent, facesContext, HTML.ONCLICK_ATTRIBUTE, omitDefaultActionUrl);
    }

    /**
     * Build JavaScript event for component
     *
     * @param uiComponent  -
     *                     component for build event
     * @param facesContext
     * @param eventName    -
     *                     name of event
     * @return <code>StringBuffer</code> with Javascript code
     */
    public static StringBuffer buildOnEvent(UIComponent uiComponent, FacesContext facesContext, String eventName) {
        return buildOnEvent(uiComponent, facesContext, eventName, false);
    }

    /**
     * Build JavaScript event for component
     *
     * @param uiComponent          -
     *                             component for build event
     * @param facesContext
     * @param eventName            -
     *                             name of event
     * @param omitDefaultActionUrl - default action URL is not encoded if parameter is true
     * @return <code>StringBuffer</code> with Javascript code
     */
    public static StringBuffer buildOnEvent(UIComponent uiComponent, FacesContext facesContext, String eventName,
                                            boolean omitDefaultActionUrl) {
        StringBuffer onEvent = new StringBuffer();

//      if (null != eventName) {
//          String commandOnEvent = (String) uiComponent.getAttributes().get(
//                  eventName);
//          if (commandOnEvent != null) {
//              onEvent.append(commandOnEvent);
//              onEvent.append(';');
//          }
//      }
//      JSFunction ajaxFunction = buildAjaxFunction(uiComponent, facesContext);
//      // Create formal parameter for non-input elements ???
//      // Link Control pseudo-object
//      // Options map. Possible options for function call :
//      // control - name of form control for submit.
//      // name - name for link control \
//      // value - value of control. - possible replace by parameters ?
//      // single true/false - submit all form or only one control.
//      // affected - array of element's ID for update on responce.
//      // oncomplete - function for call after complete request.
//      // status - id of request status component.
//      // parameters - map of parameters name/value for append on request.
//      // ..........
//      ajaxFunction.addParameter(buildEventOptions(facesContext, uiComponent,  omitDefaultActionUrl));
//
//      // appendAjaxSubmitParameters(facesContext, uiComponent, onEvent);
//      ajaxFunction.appendScript(onEvent);
//      if (uiComponent instanceof AjaxSupport) {
//          AjaxSupport support = (AjaxSupport) uiComponent;
//          if (support.isDisableDefault()) {
//              onEvent.append("; return false;");
//          }
//      }
//      LOG.debug(Messages.getMessage(Messages.BUILD_ONCLICK_INFO, uiComponent
//              .getId(), onEvent.toString()));
        return onEvent;
    }

    public static AjaxEventOptions buildEventOptions(FacesContext facesContext, UIComponent component) {
        return buildEventOptions(facesContext, component, null);
    }

    public static AjaxEventOptions buildEventOptions(FacesContext facesContext, UIComponent component,
                                                     AjaxClientBehavior ajaxBehavior) {
        AjaxEventOptions ajaxEventOptions = new AjaxEventOptions();
        Map<String, Object> parametersMap = RENDERER_UTILS.createParametersMap(facesContext, component);
        String ajaxStatusName = getAjaxStatus(component);

        if (ajaxBehavior != null) {
            ajaxStatusName = (ajaxBehavior.getStatus() != null) ? ajaxBehavior.getStatus() : ajaxStatusName;
            appenAjaxBehaviorOptions(ajaxBehavior, ajaxEventOptions);
        } else {
            appendComponentOptions(facesContext, component, ajaxEventOptions);
        }

        if ((ajaxStatusName != null) && (ajaxStatusName.length() != 0)) {
            ajaxEventOptions.set(STATUS_ATTR_NAME, ajaxStatusName);
        }

        if (!parametersMap.isEmpty()) {
            ajaxEventOptions.getParameters().putAll(parametersMap);
        }

        return ajaxEventOptions;
    }

    private static boolean isNotEmpty(String value) {
        return (value != null) && (value.length() != 0);
    }

    private static void appenAjaxBehaviorOptions(AjaxClientBehavior behavior, AjaxEventOptions ajaxEventOptions) {
        for (BehaviorEventOptionsData optionsData : BehaviorEventOptionsData.values()) {
            String eventHandlerValue = optionsData.getAttributeValue(behavior);

            if (isNotEmpty(eventHandlerValue)) {
                ajaxEventOptions.set(optionsData.toString(), eventHandlerValue);
            }
        }
    }

    private static void appendComponentOptions(FacesContext facesContext, UIComponent component,
                                               AjaxEventOptions ajaxEventOptions) {
        String behaviorName = "begin";
        HandlersChain handlersChain = new HandlersChain(facesContext, component);
        String inlineHandler = getAjaxOnBegin(component);

        handlersChain.addInlineHandlerAsValue(inlineHandler);
        handlersChain.addBehaviors(behaviorName);

        String handlerScript = handlersChain.toScript();

        if (isNotEmpty(handlerScript)) {
            ajaxEventOptions.set(behaviorName, handlerScript);
        }

        String queueId = getQueueId(component);
        if (isNotEmpty(queueId)) {
            ajaxEventOptions.set(QUEUE_ID_ATTRIBUTE, queueId);
        }
        
        ajaxEventOptions.set("incId", "1");
    }

//  public static AjaxEventOptions buildEventOptions(FacesContext facesContext,
//          UIComponent uiComponent, Map<String, Object> params) {
//
//      return buildEventOptions(facesContext, uiComponent, params, false);
//  }

    /**
     * @param facesContext
     * @param uiComponent
     * @return
     */
//  public static Map<String, Object> buildEventOptions(FacesContext facesContext,
//          UIComponent uiComponent, Map<String, Object> params, boolean omitDefaultActionUrl) {
//      String clientId = uiComponent.getClientId(facesContext);
//      Map<String, Object> componentAttributes = uiComponent.getAttributes();
//      Map<String, Object> options = new HashMap<String, Object>();
//
//      UIComponent nestingContainer = (UIComponent) findAjaxContainer(
//              facesContext, uiComponent);
//      String containerClientId = nestingContainer.getClientId(facesContext);
//      if (containerClientId != null && !AjaxViewRoot.ROOT_ID.equals(containerClientId)) {
//          options.put("containerId", containerClientId);
//      }
//
//      Map<String, Object> parameters = new HashMap<String, Object>();
//      UIComponent targetComponent = (uiComponent instanceof AjaxSupport)?uiComponent.getParent():uiComponent;
//      // UIForm form = getNestingForm(uiComponent);
//      // "input" - if assigned to html input element.
//      boolean input = targetComponent instanceof EditableValueHolder;
//      // Action component - button etc.
////        boolean action = targetComponent instanceof ActionSource;
//
//      boolean ajaxSingle = Boolean.TRUE.equals(componentAttributes
//              .get(AJAX_SINGLE_ATTR));
//      // For input components in single mode or without form submit input
//      // control )
//      if (ajaxSingle ) {
//          parameters.put(AJAX_SINGLE_PARAMETER_NAME, targetComponent.getClientId(facesContext));
//          // options.put("single", JSReference.TRUE);
//          if (input) {
//              options.put("control", JSReference.THIS);
//          }
//      }
//      // Control value for submit
//      String controlName;
//      Object controlValue;
//      // TODO - make compatible with JSF RI/MyFaces ? use submittedValue ( if
//      // any ) for UIInput, converted value for ValueHolder.
//      controlName = clientId;
//      controlValue = clientId;
//      parameters.put(controlName, controlValue);
//      AjaxContext ajaxContext = AjaxContext.getCurrentInstance(facesContext);
//
//      String ajaxActionURL = ajaxContext.getAjaxActionURL(facesContext);
//      if (omitDefaultActionUrl) {
//          UIComponent form = getNestingForm(uiComponent);
//          if (form != null && !RENDERER_UTILS.isBooleanAttribute(form, "ajaxSubmit")) {
//              if (RENDERER_UTILS.getActionUrl(facesContext).equals(ajaxActionURL)) {
//                  ajaxActionURL = null;
//              }
//          }
//      }
//
//      if (ajaxActionURL != null) {
//          // Setup action URL. For portlet environment, it will be different from
//          // page.
//          options.put("actionUrl", ajaxActionURL);
//      }
//
//      // Add application-wide Ajax parameters
//      parameters.putAll(ajaxContext.getCommonAjaxParameters());
//      // add child parameters
//      appendParameters(facesContext, uiComponent, parameters);
//
//      if (params != null) {
//          parameters.putAll(params);
//      }
//
//      if (!parameters.isEmpty()) {
//          options.put("parameters", parameters);
//      }
//      // parameter to render only current list of areas.
////        if (isAjaxLimitToList(uiComponent)) {
////            Set<? extends Object> ajaxAreas = getAjaxAreas(uiComponent);
////            Set<String> areasIds = new HashSet<String>();
////            if (null != ajaxAreas) {
////                for (Iterator<? extends Object> iter = ajaxAreas.iterator(); iter.hasNext();) {
////                    String id = (String) iter.next();
////                    UIComponent comp = RendererUtils.getInstance().
////                        findComponentFor(uiComponent, id);
////                    if (null != comp) {
////                        areasIds.add(comp.getClientId(facesContext));
////                    } else {
////                        areasIds.add(id);
////                    }
////                }
////            }
////            options.put("affected", areasIds);
////        }
//      String oncomplete = getAjaxOncomplete(uiComponent);
//      if (null != oncomplete) {
//          options.put(ONCOMPLETE_ATTR_NAME, buildAjaxOncomplete(oncomplete));
//      }
//
//      String beforeupdate = getAjaxOnBeforeDomUpdate(uiComponent);
//      if (null != beforeupdate) {
//          options.put(ONBEFOREDOMUPDATE_ATTR_NAME, buildAjaxOnBeforeDomUpdate(beforeupdate));
//      }
//
//
//      String status = getAjaxStatus(uiComponent);
//      if (null != status) {
//          options.put("status", status);
//      }
//      String queue = (String) componentAttributes.get(AJAX_QUEUE_ATTR);
//      String implicitQueue = null;
//
//      Integer requestDelay = (Integer) componentAttributes
//              .get(AJAX_DELAY_ATTR);
//      if (null != requestDelay && requestDelay.intValue() > 0) {
//          options.put(AJAX_DELAY_ATTR, requestDelay);
//          if (null == queue) {
//              implicitQueue = clientId;
//          }
//      }
//      Boolean ignoreDupResponses = (Boolean) componentAttributes
//              .get(AJAX_ABORT_ATTR);
//      if (null != ignoreDupResponses && ignoreDupResponses.booleanValue()) {
//          options.put(AJAX_ABORT_ATTR, JSReference.TRUE);
//          if (null == queue) {
//              implicitQueue = clientId;
//          }
//      }
//
//      if (null != queue) {
//          options.put(AJAX_QUEUE_ATTR, queue);
//      } else if (implicitQueue != null) {
//          options.put("implicitEventsQueue", clientId);
//      }
//
//      ExternalContext externalContext = facesContext.getExternalContext();
//      String namespace = externalContext.encodeNamespace("");
//      if (namespace != null && namespace.length() != 0) {
//          options.put("namespace", namespace);
//      }
//
//      String similarityGroupingId = (String) componentAttributes.get(SIMILARITY_GROUPING_ID_ATTR);
//      if (similarityGroupingId == null || similarityGroupingId.length() == 0) {
//          similarityGroupingId = clientId;
//      } else {
//          similarityGroupingId = externalContext.encodeNamespace(similarityGroupingId);
//      }
//
//      options.put(SIMILARITY_GROUPING_ID_ATTR, similarityGroupingId);
//
//      // request timeout.
//      Integer timeout = (Integer) componentAttributes.get("timeout");
//      if (null != timeout && timeout.intValue() > 0) {
//          options.put("timeout", timeout);
//      }
//      // Encoding for requests
//      String encoding = (String) componentAttributes.get("encoding");
//      if (null != encoding) {
//          options.put("encoding", encoding);
//      }
//      return options;
//  }
//  /**
//   * Create call to Ajax Submit function with first two parameters
//   *
//   * @param uiComponent
//   * @param facesContext
//   * @param functionName
//   * @return
//   */
//  public static JSFunction buildAjaxFunction(UIComponent uiComponent,
//          FacesContext facesContext) {
//      JSFunction ajaxFunction = buildAjaxFunction(uiComponent, facesContext,
//              AJAX_FUNCTION_NAME);
//      // client-side script must have reference to event-enabled object.
//      ajaxFunction.addParameter(new JSReference("event"));
//      return ajaxFunction;
//  }

    /**
     * Create call to Ajax Submit function with first two parameters
     *
     * @param facesContext
     * @param uiComponent
     * @param functionName
     * @return
     */
    public static JSFunction buildAjaxFunction(FacesContext facesContext, UIComponent uiComponent,
                                               String functionName) {
        JSFunction ajaxFunction = new JSFunction(functionName);

        ajaxFunction.addParameter(uiComponent.getClientId(facesContext));
        ajaxFunction.addParameter(JSReference.EVENT);

        return ajaxFunction;
    }

    /**
     * Append common parameters ( array of affected areas, status area id, on
     * complete function ) to JavaScript event string.
     *
     * @param uiComponent
     * @param onClick -
     *            buffer with JavaScript code eg... AJAX.Submit(form,this
     */

    // public static void appendAjaxSubmitParameters(FacesContext facesContext,
    // UIComponent uiComponent, StringBuffer onClick)
    // {
    // Set ajaxAreas = getAjaxAreas(uiComponent);
    // onClick.append(',');
    // // parameter to render only current list of areas.
    // if (isAjaxLimitToList(uiComponent) && ajaxAreas != null &&
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
     * Get list of clientId's for given component
     *
     * @param uiComponent
     * @return List of areas Id's , updated by this component.
     */
    public static Set<String> getAjaxAreas(UIComponent uiComponent) {
        Object areas;

        if (uiComponent instanceof AjaxComponent) {
            areas = ((AjaxComponent) uiComponent).getReRender();
        } else {
            areas = uiComponent.getAttributes().get(AjaxRendererUtils.AJAX_REGIONS_ATTRIBUTE);
        }

        return asSet(areas);
    }

    /**
     * Returns set of areas to be processed as a result of this component action invocation
     *
     * @param component
     * @return set of IDs that should be processed as a
     * @since 3.3.0
     */
    public static Set<String> getAjaxAreasToProcess(UIComponent component) {
        Object areas;

        if (component instanceof AjaxComponent) {
            areas = ((AjaxComponent) component).getProcess();
        } else {
            areas = component.getAttributes().get(AjaxRendererUtils.AJAX_PROCESS_ATTRIBUTE);
        }

        return asSet(areas);
    }

    /**
     * Split parameter string into array of strings.
     * @param valuesSet
     * @return
     */
    public static String[] asArray(String valuesSet) {
        return IdSplitBuilder.split(valuesSet);
    }

    /**
     * Convert parameter ( Collection, List, array, String, comma-separated
     * String, whitespace-separate String) to set of strings.
     *
     * @param valueToSet -
     *                   object for conversion to Set.
     * @return - set of strings.
     */
    @SuppressWarnings("unchecked")
    public static Set<String> asSet(Object valueToSet) {
        if (null != valueToSet) {

            // Simplest case - set.
            if (valueToSet instanceof Set) {
                return new LinkedHashSet<String>((Set<String>) valueToSet);
            } else if (valueToSet instanceof Collection) { // Other collections.
                return new LinkedHashSet<String>((Collection<String>) valueToSet);
            } else if (OBJECT_ARRAY_CLASS.isAssignableFrom(valueToSet.getClass())) { // Array
                return new LinkedHashSet<String>(Arrays.asList((String[]) valueToSet));
            } else if (valueToSet instanceof String) { // Tokenize string.
                String areasString = ((String) valueToSet).trim();

                if (areasString.contains(",") || areasString.contains(" ")) {
                    String[] values = asArray(areasString);
                    Set<String> result = new LinkedHashSet<String>(values.length);
                    for (String value : values) {
                        result.add(value);
                    }

                    return result;
                } else {
                    Set<String> areasSet = new LinkedHashSet<String>(5);

                    areasSet.add(areasString);

                    return areasSet;
                }
            }
        }

        return null;
    }

    /**
     * Get status area Id for given component.
     *
     * @param component
     * @return clientId of status area, or <code>null</code>
     */
    public static String getAjaxStatus(UIComponent component) {
        String statusId;

        if (component instanceof AjaxComponent) {
            statusId = ((AjaxComponent) component).getStatus();
        } else {
            statusId = (String) component.getAttributes().get(STATUS_ATTR_NAME);
        }

        return statusId;

//      if (null != statusId) {
//          UIComponent status = RendererUtils.getInstance().
//              findComponentFor(component, statusId);
//
//          if (null != status) {
//              statusId = status
//                      .getClientId(FacesContext.getCurrentInstance());
//          } else {
//              LOG.warn(Messages.getMessage(
//                      Messages.AJAX_STATUS_COMPONENT_NOT_FOWND_WARNING,
//                      component.getId()));
//          }
//      }
//      return statusId;
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

    /**
     * Get function name for call on completed ajax request.
     *
     * @param component for wich calculate function name
     * @return name of JavaScript function or <code>null</code>
     */
    //TODO nick - refactor - remove this method?
    public static String getAjaxOncomplete(UIComponent component) {
        if (component instanceof AjaxComponent) {
            return ((AjaxComponent) component).getOncomplete();
        }

        return (String) component.getAttributes().get(ONCOMPLETE_ATTR_NAME);
    }

    /**
     * Get function name for call before update DOM.
     *
     * @param component for wich calculate function name
     * @return name of JavaScript function or <code>null</code>
     */
    //TODO nick - refactor - remove this method?
    public static String getAjaxOnBeforeDomUpdate(UIComponent component) {
        if (component instanceof AjaxComponent) {
            return ((AjaxComponent) component).getOnbeforedomupdate();
        }

        return (String) component.getAttributes().get(ONBEFOREDOMUPDATE_ATTR_NAME);
    }

    //TODO nick - refactor - remove this method?
    public static String getAjaxOnBegin(UIComponent component) {
        if (component instanceof AjaxComponent) {
            return ((AjaxComponent) component).getOnbegin();
        }

        return (String) component.getAttributes().get(ONBEGIN_ATTR_NAME);
    }

    /**
     * @param component
     * @return
     * @since 4.0
     */
    public static Object getAjaxData(UIComponent component) {
        if (component instanceof AjaxComponent) {
            return ((AjaxComponent) component).getData();
        }

        return component.getAttributes().get(DATA_ATTR_NAME);
    }

    /**
     * Calculate, must be component render only given areas, or all sended from
     * server.
     *
     * @param component
     * @return <code>true</code> if client must render ONLY given areas.
     */
    public static boolean isAjaxLimitRender(UIComponent component) {
        boolean result = false;

        if (component instanceof AjaxComponent) {
            result = ((AjaxComponent) component).isLimitRender();
        } else {
            try {
                result = ((Boolean) component.getAttributes().get(LIMITRENDER_ATTR_NAME)).booleanValue();
            } catch (NullPointerException e) {

                // NullPointer - ignore ...
            } catch (ClassCastException e1) {

                // not Boolean - false ...
            }
        }

        return result;
    }

    /**
     * Replacement for buggy in MyFaces <code>RendererUtils</code>
     *
     * @param component
     * @return
     */
    public static String getAbsoluteId(UIComponent component) {
        if (component == null) {
            throw new NullPointerException(Messages.getMessage(Messages.COMPONENT_NULL_ERROR_2));
        }

        StringBuffer idBuf = new StringBuffer();

        idBuf.append(component.getId());

        UIComponent parent = component;

        while ((parent = parent.getParent()) != null) {
            if (parent instanceof NamingContainer) {
                idBuf.insert(0, NamingContainer.SEPARATOR_CHAR);
                idBuf.insert(0, parent.getId());
            }
        }

        idBuf.insert(0, NamingContainer.SEPARATOR_CHAR);
        LOG.debug(Messages.getMessage(Messages.CALCULATE_COMPONENT_ID_INFO, component.getId(), idBuf.toString()));

        return idBuf.toString();
    }

    /**
     * Find nested form for given component
     *
     * @param component
     * @return nested <code>UIForm</code> component, or <code>null</code>
     */
    public static UIComponent getNestingForm(UIComponent component) {
        UIComponent parent = component;

        // Search enclosed UIForm or ADF UIXForm component
        while ((parent != null) && !(parent instanceof UIForm)
            && !("org.apache.myfaces.trinidad.Form".equals(parent.getFamily()))
            && !("oracle.adf.Form".equals(parent.getFamily()))) {
            parent = parent.getParent();
        }

        return parent;
    }

    protected static String getAjaxActionUrl(FacesContext facesContext) {
        return AjaxContext.getCurrentInstance(facesContext).getAjaxActionURL(facesContext);
    }

    /**
     * @param facesContext
     * @param uiComponent
     * @return
     */
    public static org.ajax4jsf.component.AjaxContainer findAjaxContainer(FacesContext facesContext,
                                                                         UIComponent uiComponent) {
        UIComponent parent = uiComponent.getParent();

        while ((parent != null) && !(parent instanceof org.ajax4jsf.component.AjaxContainer)) {
            parent = parent.getParent();
        }

        org.ajax4jsf.component.AjaxContainer nestingContainer = null;

        if (parent != null) {

            // link is nested inside a form
            nestingContainer = (org.ajax4jsf.component.AjaxContainer) parent;
        } else if (facesContext.getViewRoot() instanceof AjaxViewRoot) {
            nestingContainer = (AjaxContainer) facesContext.getViewRoot();
        }

        return nestingContainer;
    }

    /**
     * Encode rendered areas as special HTML tag ( span in current release )
     *
     * @param context
     * @param component
     * @throws IOException
     */
    public static void encodeAreas(FacesContext context, UIComponent component) throws IOException {
        AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);
        ExternalContext externalContext = context.getExternalContext();
        Map<String, Object> requestMap = externalContext.getRequestMap();
        Set<String> rendered = ajaxContext.getAjaxRenderedAreas();

        // write special area for list of rendered elements. Client-side
        // Java
        // Script
        // read this structure for update areas of DOM tree.
        ResponseWriter out = context.getResponseWriter();

        // Create <span> element to keep list rendered aread ( in title
        // attribute )
        // More right will create special namespace for such
        // information,
        // but I want to keep simple html ( xhtml ) document - on case
        // I have troubles with microsoft XMLHTTP validations.
        out.startElement(AjaxContainerRenderer.AJAX_RESULT_GROUP_TAG, component);
        out.writeAttribute(HTML.NAME_ATTRIBUTE, AjaxContainerRenderer.AJAX_UPDATE_HEADER, null);

        StringBuffer senderString = new StringBuffer();

        for (Iterator<String> it = rendered.iterator(); it.hasNext();) {
            String id = it.next();

            senderString.append(id);

            if (it.hasNext()) {
                senderString.append(',');
            }
        }

        out.writeAttribute(AjaxContainerRenderer.AJAX_RESULT_GROUP_ATTR, senderString, null);
        out.endElement(AjaxContainerRenderer.AJAX_RESULT_GROUP_TAG);

        // For sequences and client-saved states.
        out.startElement(AjaxContainerRenderer.AJAX_VIEW_STATE_TAG, component);
        out.writeAttribute(HTML.ID_ATTRIBUTE, AjaxContainerRenderer.AJAX_VIEW_STATE_ID, null);
        writeState(context);
        out.endElement(AjaxContainerRenderer.AJAX_VIEW_STATE_TAG);

        // Write rendered flag to html <meta>
        out.startElement(AjaxContainerRenderer.AJAX_RESULT_GROUP_TAG, component);
        out.writeAttribute(HTML.ID_ATTRIBUTE, AjaxContainerRenderer.AJAX_FLAG_HEADER, null);
        out.writeAttribute(HTML.NAME_ATTRIBUTE, AjaxContainerRenderer.AJAX_FLAG_HEADER, null);
        out.writeAttribute(AjaxContainerRenderer.AJAX_RESULT_GROUP_ATTR, "true", null);
        out.endElement(AjaxContainerRenderer.AJAX_RESULT_GROUP_TAG);

        // set response header with list of rendered ID's
        Object response = externalContext.getResponse();

        // Use reflection for send responce headers - we can get
        // different responces classes
        // for different environment ( portal, cocoon etc )
        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            httpResponse.setHeader(AjaxContainerRenderer.AJAX_FLAG_HEADER, "true");
        } else {
            try {
                Method setHeadergMethod = response.getClass().getMethod("setHeader",
                    new Class[]{String.class, String.class});

                setHeadergMethod.invoke(response, AjaxContainerRenderer.AJAX_FLAG_HEADER, "true");
            } catch (Exception e) {
                LOG.error(Messages.getMessage(Messages.DETECTING_ENCODING_DISABLED_ERROR));
                LOG.error(Messages.getMessage(Messages.OBTAIN_RESPONSE_SET_HEADER_ERROR, e));
            }
        }

        Map<String, Object> responseDataMap = ajaxContext.getResponseComponentDataMap();

        // Get data serializer instance
        AJAXDataSerializer serializer = ServiceTracker.getService(context, AJAXDataSerializer.class);

        // Put data to JavaScript handlers, inside <span> elements.
        for (Map.Entry<String, Object> entry : responseDataMap.entrySet()) {
            out.startElement(HTML.SPAN_ELEM, component);
            out.writeAttribute(HTML.ID_ATTRIBUTE, entry.getKey(), null);

            String dataString = serializer.asString(entry.getValue());

            out.write(dataString);
            out.endElement(HTML.SPAN_ELEM);
        }

        // Include active 'oncomplete' function content :
        Object oncomplete = ajaxContext.getOncomplete();

        if (null != oncomplete) {
            out.startElement(HTML.SPAN_ELEM, component);
            out.writeAttribute(HTML.ID_ATTRIBUTE, ONCOMPLETE_CONTENT_ID, null);
            out.writeText(oncomplete, null);
            out.endElement(HTML.SPAN_ELEM);
        }

        // For self-rendered case, we use own methods for replace stateKey by
        // real value in XML filter.
        // if(ajaxContext.isSelfRender()){
        // saveViewState(context, out);
        // }
        requestMap.put(AJAX_AREAS_RENDERED, "true");
    }

    /**
     * Write state saving markers to context, include MyFaces view sequence.
     *
     * @param context
     * @throws IOException
     */
    public static void writeState(FacesContext context) throws IOException {
        context.getApplication().getViewHandler().writeState(context);
    }

    /**
     * Encode declaration for AJAX response. Render &lt;html&gt;&lt;body&gt;
     *
     * @param context
     * @param component
     * @throws IOException
     */
    public static void encodeAjaxBegin(FacesContext context, UIComponent component) throws IOException {

        // AjaxContainer ajax = (AjaxContainer) component;
        ResponseWriter out = context.getResponseWriter();

        // DebugUtils.traceView("ViewRoot in AJAX Page encode begin");
        out.startElement("html", component);

        Locale locale = context.getViewRoot().getLocale();

        out.writeAttribute(HTML.LANG_ATTRIBUTE, locale.toString(), "lang");
        out.startElement("body", component);
    }

    /**
     * End encoding of AJAX response. Render tag with included areas and close
     * &lt;/body&gt;&lt;/html&gt;
     *
     * @param context
     * @param component
     * @throws IOException
     */
    public static void encodeAjaxEnd(FacesContext context, UIComponent component) throws IOException {

        // AjaxContainer ajax = (AjaxContainer) component;
        ResponseWriter out = context.getResponseWriter();

        // DebugUtils.traceView("ViewRoot in AJAX Page encode begin");
        encodeAreas(context, component);
        out.endElement("body");
        out.endElement("html");
    }

    /**
     * Find all instances of {@link UILoadBundle} in view tree and load bundles
     * to request-scope map.
     *
     * @param context
     * @throws IOException
     */
    public static void loadBundles(FacesContext context) {

        // TODO - performanse improove - don't seek by all components tree.
        loadBundles(context, context.getViewRoot());
    }

    /**
     * Recursive helper for {@link #loadBundles(FacesContext)}
     *
     * @param context
     * @param component
     * @throws IOException
     */
    private static void loadBundles(FacesContext context, UIComponent component) {

        // Iterate over cildrens
        for (UIComponent child : component.getChildren()) {
            loadCildBundles(context, child);
        }

        // Iterate over facets
        for (UIComponent child : component.getFacets().values()) {
            loadCildBundles(context, child);
        }
    }

    /**
     * @param context
     * @param child
     */
    private static void loadCildBundles(FacesContext context, UIComponent child) {
        if (child instanceof AjaxLoadBundleComponent) {
            try {
                child.encodeBegin(context);
            } catch (IOException e) {

                // DO nothing - really, LoadBundle don't can throw exceptions.
            }
        } else {
            loadBundles(context, child);
        }
    }

    /**
     * @param facesContext
     * @return
     */
    public static boolean isAjaxRequest(FacesContext facesContext) {
        return AjaxContext.getCurrentInstance(facesContext).isAjaxRequest();
    }

    /**
     * TODO: add deprecation
     *
     * @param facesContext
     * @param component
     * @param id
     */
    public static void addRegionByName(FacesContext facesContext, UIComponent component, String id) {
        AjaxContext.getCurrentInstance(facesContext).addComponentToAjaxRender(component, id);
    }

    /**
     * @param facesContext
     * @param component
     */
    public static void addRegionsFromComponent(UIComponent component, FacesContext facesContext) {
        AjaxContext.getCurrentInstance(facesContext).addRegionsFromComponent(component);
    }

    private static void startExtensionElementIfNecessary(
        PartialResponseWriter partialResponseWriter,
        Map<String, String> attributes,
        boolean[] writingState) throws IOException {

        if (!writingState[0]) {
            writingState[0] = true;

            partialResponseWriter.startExtension(attributes);
        }
    }

    private static void endExtensionElementIfNecessary(
        PartialResponseWriter partialResponseWriter,
        boolean[] writingState) throws IOException {

        if (writingState[0]) {
            writingState[0] = false;

            partialResponseWriter.endExtension();
        }
    }

    public static void renderAjaxExtensions(FacesContext facesContext, UIComponent component) throws IOException {
        AjaxContext ajaxContext = AjaxContext.getCurrentInstance(facesContext);

        Map<String, String> attributes = Collections.singletonMap(HTML.ID_ATTRIBUTE,
            facesContext.getExternalContext().encodeNamespace(EXTENSION_ID));
        PartialResponseWriter writer = facesContext.getPartialViewContext().getPartialResponseWriter();
        boolean[] writingState = new boolean[]{false};

        Object onbeforedomupdate = ajaxContext.getOnbeforedomupdate();
        if (onbeforedomupdate != null) {
            String string = onbeforedomupdate.toString();
            if (string.length() != 0) {
                startExtensionElementIfNecessary(writer, attributes, writingState);
                writer.startElement(BEFOREDOMUPDATE_ELEMENT_NAME, component);
                writer.writeText(onbeforedomupdate, null);
                writer.endElement(BEFOREDOMUPDATE_ELEMENT_NAME);
            }
        }

        Object oncomplete = ajaxContext.getOncomplete();
        if (oncomplete != null) {
            String string = oncomplete.toString();
            if (string.length() != 0) {
                startExtensionElementIfNecessary(writer, attributes, writingState);
                writer.startElement(COMPLETE_ELEMENT_NAME, component);
                writer.writeText(oncomplete, null);
                writer.endElement(COMPLETE_ELEMENT_NAME);
            }
        }

        Object responseData = ajaxContext.getResponseData();
        if (responseData != null) {
            startExtensionElementIfNecessary(writer, attributes, writingState);
            writer.startElement(DATA_ELEMENT_NAME, component);

            AJAXDataSerializer serializer = ServiceTracker.getService(facesContext, AJAXDataSerializer.class);
            writer.writeText(serializer.asString(responseData), null);

            writer.endElement(DATA_ELEMENT_NAME);
        }

        Map<String, Object> responseComponentDataMap = ajaxContext.getResponseComponentDataMap();
        if (responseComponentDataMap != null && !responseComponentDataMap.isEmpty()) {
            startExtensionElementIfNecessary(writer, attributes, writingState);
            writer.startElement(COMPONENT_DATA_ELEMENT_NAME, component);

            AJAXDataSerializer serializer = ServiceTracker.getService(facesContext, AJAXDataSerializer.class);
            writer.writeText(serializer.asString(responseComponentDataMap), null);

            writer.endElement(COMPONENT_DATA_ELEMENT_NAME);
        }
        
        endExtensionElementIfNecessary(writer, writingState);

    }

}
