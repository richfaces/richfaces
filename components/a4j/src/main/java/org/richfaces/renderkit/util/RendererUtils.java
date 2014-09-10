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
package org.richfaces.renderkit.util;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehaviorContext.Parameter;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.Messages;
import org.ajax4jsf.component.JavaScriptParameter;
import org.ajax4jsf.javascript.JSReference;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RenderKitUtils;

/**
 * Util class for common render operations - render pass-through html attributes, iterate over child components etc.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.6 $ $Date: 2007/02/08 19:07:16 $
 *
 */
public class RendererUtils {
    public static final String DUMMY_FORM_ID = ":_form";
    // we'd better use this instance multithreadly quickly
    private static final RendererUtils INSTANCE = new RendererUtils();
    /**
     * Substitutions for components properies names and HTML attributes names.
     */
    private static final Map<String, String> SUBSTITUTIONS = new HashMap<String, String>();

    static {
        SUBSTITUTIONS.put(HtmlConstants.CLASS_ATTRIBUTE, "styleClass");

        Arrays.sort(HtmlConstants.PASS_THRU);
        Arrays.sort(HtmlConstants.PASS_THRU_EVENTS);
        Arrays.sort(HtmlConstants.PASS_THRU_BOOLEAN);
        Arrays.sort(HtmlConstants.PASS_THRU_URI);
    }

    // can be created by subclasses;
    // administratively restricted to be created by package members ;)
    protected RendererUtils() {
        super();
    }

    /**
     * Use this method to get singleton instance of RendererUtils
     *
     * @return singleton instance
     */
    public static RendererUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Encode id attribute with clientId component property
     *
     * @param context
     * @param component
     * @throws IOException
     */
    public void encodeId(FacesContext context, UIComponent component) throws IOException {
        encodeId(context, component, HtmlConstants.ID_ATTRIBUTE);
    }

    /**
     * Encode clientId to custom attribute ( for example, to control name )
     *
     * @param context
     * @param component
     * @param attribute
     * @throws IOException
     */
    public void encodeId(FacesContext context, UIComponent component, String attribute) throws IOException {
        String clientId = null;

        try {
            clientId = component.getClientId(context);
        } catch (Exception e) {

            // just ignore if clientId wasn't inited yet
        }

        if (null != clientId) {
            context.getResponseWriter().writeAttribute(attribute, clientId, (String) getComponentAttributeName(attribute));
        }
    }

    /**
     * Encode id attribute with clientId component property. Encoded only if id not auto generated.
     *
     * @param context
     * @param component
     * @throws IOException
     */
    public void encodeCustomId(FacesContext context, UIComponent component) throws IOException {
        if (hasExplicitId(component)) {
            context.getResponseWriter().writeAttribute(HtmlConstants.ID_ATTRIBUTE, component.getClientId(context),
                HtmlConstants.ID_ATTRIBUTE);
        }
    }

    /**
     * Returns value of the parameter. If parameter is instance of <code>JavaScriptParameter</code>, <code>NoEcape</code>
     * attribute is applied.
     *
     * @param parameter instance of <code>UIParameter</code>
     * @return <code>Object</code> parameter value
     */
    public Object createParameterValue(UIParameter parameter) {
        Object value = parameter.getValue();
        boolean escape = true;

        if (parameter instanceof JavaScriptParameter) {
            JavaScriptParameter actionParam = (JavaScriptParameter) parameter;

            escape = !actionParam.isNoEscape();
        }

        if (escape) {
            if (value == null) {
                value = "";
            }
        } else {
            value = new JSReference(value.toString());
        }

        return value;
    }

    public Map<String, Object> createParametersMap(FacesContext context, UIComponent component) {
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();

        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                if (child instanceof UIParameter) {
                    UIParameter parameter = (UIParameter) child;
                    String name = parameter.getName();
                    Object value = createParameterValue(parameter);

                    if (null == name) {
                        throw new IllegalArgumentException(Messages.getMessage(Messages.UNNAMED_PARAMETER_ERROR,
                            component.getClientId(context)));
                    }
                    parameters.put(name, value);
                }
            }
        }

        return parameters;
    }

    private void encodeBehaviors(FacesContext context, ClientBehaviorHolder behaviorHolder, String defaultHtmlEventName,
        String[] attributesExclusions) throws IOException {

        // if (attributesExclusions != null && attributesExclusions.length != 0) {
        // assert false : "Not supported yet";
        // }
        // TODO: disabled component check
        String defaultEventName = behaviorHolder.getDefaultEventName();
        Collection<String> eventNames = behaviorHolder.getEventNames();

        if (eventNames != null) {
            UIComponent component = (UIComponent) behaviorHolder;
            ResponseWriter writer = context.getResponseWriter();
            Collection<Parameter> parametersList = HandlersChain.createParametersList(createParametersMap(context, component));

            for (String behaviorEventName : eventNames) {
                String htmlEventName = "on" + behaviorEventName;

                if ((attributesExclusions == null) || (Arrays.binarySearch(attributesExclusions, htmlEventName) < 0)) {
                    HandlersChain handlersChain = new HandlersChain(context, component, parametersList);

                    handlersChain.addInlineHandlerFromAttribute(htmlEventName);
                    handlersChain.addBehaviors(behaviorEventName);

                    String handlerScript = handlersChain.toScript();

                    if (!isEmpty(handlerScript)) {
                        writer.writeAttribute(htmlEventName, handlerScript, htmlEventName);
                    }
                }
            }
        }
    }

    /**
     * Encode common pass-thru html attributes.
     *
     * @param context
     * @param component
     * @throws IOException
     */
    public void encodePassThru(FacesContext context, UIComponent component, String defaultHtmlEvent) throws IOException {

        encodeAttributesFromArray(context, component, HtmlConstants.PASS_THRU);

        if (component instanceof ClientBehaviorHolder) {
            ClientBehaviorHolder clientBehaviorHolder = (ClientBehaviorHolder) component;

            encodeBehaviors(context, clientBehaviorHolder, defaultHtmlEvent, null);
        } else {
            encodeAttributesFromArray(context, component, HtmlConstants.PASS_THRU_EVENTS);
        }
    }

    /**
     * Encode pass-through attributes except specified ones
     *
     * @param context
     * @param component
     * @param exclusions
     * @throws IOException
     */
    public void encodePassThruWithExclusions(FacesContext context, UIComponent component, String exclusions,
        String defaultHtmlEvent) throws IOException {

        if (null != exclusions) {
            String[] exclusionsArray = exclusions.split(",");

            encodePassThruWithExclusionsArray(context, component, exclusionsArray, defaultHtmlEvent);
        }
    }

    public void encodePassThruWithExclusionsArray(FacesContext context, UIComponent component, String[] exclusions,
        String defaultHtmlEvent) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Map<String, Object> attributes = component.getAttributes();

        Arrays.sort(exclusions);

        for (int i = 0; i < HtmlConstants.PASS_THRU.length; i++) {
            String attribute = HtmlConstants.PASS_THRU[i];

            if (Arrays.binarySearch(exclusions, attribute) < 0) {
                encodePassThruAttribute(context, attributes, writer, attribute);
            }
        }

        if (component instanceof ClientBehaviorHolder) {
            ClientBehaviorHolder clientBehaviorHolder = (ClientBehaviorHolder) component;

            encodeBehaviors(context, clientBehaviorHolder, defaultHtmlEvent, exclusions);
        } else {
            for (int i = 0; i < HtmlConstants.PASS_THRU_EVENTS.length; i++) {
                String attribute = HtmlConstants.PASS_THRU_EVENTS[i];

                if (Arrays.binarySearch(exclusions, attribute) < 0) {
                    encodePassThruAttribute(context, attributes, writer, attribute);
                }
            }
        }
    }

    /**
     * Encode one pass-thru attribute, with plain/boolean/url value, got from properly component attribute.
     *
     * @param context
     * @param writer
     * @param attribute
     * @throws IOException
     */
    public void encodePassThruAttribute(FacesContext context, Map<String, Object> attributes, ResponseWriter writer,
        String attribute) throws IOException {

        Object value = attributeValue(attribute, attributes.get(getComponentAttributeName(attribute)));

        if ((null != value) && RenderKitUtils.shouldRenderAttribute(value)) {
            if (Arrays.binarySearch(HtmlConstants.PASS_THRU_URI, attribute) >= 0) {
                String url = context.getApplication().getViewHandler().getResourceURL(context, value.toString());

                url = context.getExternalContext().encodeResourceURL(url);
                writer.writeURIAttribute(attribute, url, attribute);
            } else {
                writer.writeAttribute(attribute, value, attribute);
            }
        }
    }

    public void encodeAttributesFromArray(FacesContext context, UIComponent component, String[] attrs) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Map<String, Object> attributes = component.getAttributes();

        for (int i = 0; i < attrs.length; i++) {
            String attribute = attrs[i];

            encodePassThruAttribute(context, attributes, writer, attribute);
        }
    }

    /**
     * Encode attributes given by comma-separated string list.
     *
     * @param context current JSF context
     * @param component for with render attributes values
     * @param attrs comma separated list of attributes
     * @throws IOException
     */
    public void encodeAttributes(FacesContext context, UIComponent component, String attrs) throws IOException {
        if (null != attrs) {
            String[] attrsArray = attrs.split(",");

            encodeAttributesFromArray(context, component, attrsArray);
        }
    }

    /**
     * @param context
     * @param component
     * @param property
     * @param attributeName
     *
     * @throws IOException
     */
    public void encodeAttribute(FacesContext context, UIComponent component, Object property, String attributeName)
        throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Object value = component.getAttributes().get(property);

        if (RenderKitUtils.shouldRenderAttribute(value)) {
            writer.writeAttribute(attributeName, value, property.toString());
        }
    }

    public void encodeAttribute(FacesContext context, UIComponent component, String attribute) throws IOException {
        encodeAttribute(context, component, getComponentAttributeName(attribute), attribute);
    }

    /**
     * Checks if the argument passed in is empty or not. Object is empty if it is: <br />
     * - <code>null</code><br />
     * - zero-length string<br />
     * - empty collection<br />
     * - empty map<br />
     * - zero-length array<br />
     *
     * @param o object to check for emptiness
     * @since 3.3.2
     * @return <code>true</code> if the argument is empty, <code>false</code> otherwise
     */
    public boolean isEmpty(Object o) {
        if (null == o) {
            return true;
        }

        if (o instanceof String) {
            return 0 == ((String) o).length();
        }

        if (o instanceof Collection<?>) {
            return ((Collection<?>) o).isEmpty();
        }

        if (o instanceof Map<?, ?>) {
            return ((Map<?, ?>) o).isEmpty();
        }

        if (o.getClass().isArray()) {
            return Array.getLength(o) == 0;
        }

        return false;
    }

    /**
     * Convert HTML attribute name to component property name.
     *
     * @param key
     * @return
     */
    protected Object getComponentAttributeName(Object key) {
        Object converted = SUBSTITUTIONS.get(key);

        if (null == converted) {
            return key;
        } else {
            return converted;
        }
    }

    /**
     * Convert attribute value to proper object. For known html boolean attributes return name for true value, otherthise -
     * null. For non-boolean attributes return same value.
     *
     * @param name attribute name.
     * @param value
     * @return
     */
    protected Object attributeValue(String name, Object value) {
        if (null == value || Arrays.binarySearch(HtmlConstants.PASS_THRU_BOOLEAN, name) < 0) {
            return value;
        }

        boolean checked;

        if (value instanceof Boolean) {
            checked = ((Boolean) value).booleanValue();
        } else {
            checked = Boolean.parseBoolean(value.toString());
        }

        return checked ? name : null;
    }

    /**
     * Get boolean value of logical attribute
     *
     * @param component
     * @param name attribute name
     * @return true if attribute is equals Boolean.TRUE or String "true" , false otherwise.
     */
    public boolean isBooleanAttribute(UIComponent component, String name) {
        Object attrValue = component.getAttributes().get(name);
        boolean result = false;

        if (null != attrValue) {
            if (attrValue instanceof String) {
                result = "true".equalsIgnoreCase((String) attrValue);
            } else {
                result = Boolean.TRUE.equals(attrValue);
            }
        }

        return result;
    }

    public String encodePx(String value) {
        return HtmlDimensions.formatPx(HtmlDimensions.decode(value));
    }

    /**
     * formats given value to
     *
     * @param value
     *
     * @return
     */
    public String encodePctOrPx(String value) {
        if (value.indexOf('%') > 0) {
            return value;
        } else {
            return encodePx(value);
        }
    }

    /**
     * Find nested form for given component
     *
     * <b>Deprecated</b>: use {@link #getNestingForm(UIComponent)} instead
     *
     * @param component
     * @return nested <code>UIForm</code> component, or <code>null</code>
     */
    public UIComponent getNestingForm(UIComponent component) {
        return getNestingForm(null, component);
    }

    /**
     * Find nested form for given component
     *
     * @param component
     * @return nested <code>UIForm</code> component, or <code>null</code>
     */
    @Deprecated
    public UIComponent getNestingForm(FacesContext context, UIComponent component) {
        UIComponent parent = component;

        // Search enclosed UIForm or ADF UIXForm component
        while ((parent != null) && !(parent instanceof UIForm)
                && !("org.apache.myfaces.trinidad.Form".equals(parent.getFamily()))
                && !("oracle.adf.Form".equals(parent.getFamily()))) {
            parent = parent.getParent();
        }

        return parent;
    }

    /**
     * Detects whether given component is form
     */
    public boolean isForm(UIComponent component) {
        return component instanceof UIForm || "org.apache.myfaces.trinidad.Form".equals(component.getFamily()) || "oracle.adf.Form".equals(component.getFamily());
    }

    /**
     * Find submitted form for given context
     *
     * @param facesContext
     * @return submitted <code>UIForm</code> component, or <code>null</code>
     */
    public UIComponent getSubmittedForm(FacesContext facesContext) {
        if (!facesContext.isPostback()) {
            return null;
        }
        for (Entry<String, String> entry : facesContext.getExternalContext().getRequestParameterMap().entrySet()) {
            final String name = entry.getKey();
            final String value = entry.getValue();

            // form's name equals to its value
            if (isFormValueSubmitted(name, value)) {
                // in that case, name is equal to clientId
                UIComponent component = findComponentFor(facesContext.getViewRoot(), name);

                UIComponent form = getNestingForm(component);
                return form;
            }
        }
        facesContext.addMessage(null, new FacesMessage("The form wasn't detected for the request",
                "The form wasn't detected for the request - rendering does not have to behave well"));
        return null;
    }

    /**
     * Determines whenever given form has been submitted
     */
    public boolean isFormSubmitted(FacesContext context, UIForm form) {
        if (form != null) {
            String clientId = form.getClientId(context);
            String formRequestParam = context.getExternalContext().getRequestParameterMap().get(clientId);
            return isFormValueSubmitted(clientId, formRequestParam);
        }
        return false;
    }

    /**
     * Determines if a form was submitted based on its clientId (which equals to request parameter name) and submitted value
     *
     * @return true if clientId and value equals and they are not null; false otherwise
     */
    private boolean isFormValueSubmitted(String clientId, String value) {
        if (clientId == null) {
            return false;
        }
        return clientId.equals(value);
    }

    /**
     * @param context
     * @param component
     * @throws IOException
     */
    public void encodeBeginFormIfNessesary(FacesContext context, UIComponent component) throws IOException {
        UIComponent form = getNestingForm(context, component);

        if (null == form) {
            ResponseWriter writer = context.getResponseWriter();
            String clientId = component.getClientId(context) + DUMMY_FORM_ID;

            encodeBeginForm(context, component, writer, clientId);

            // writer.writeAttribute(HTML.STYLE_ATTRIBUTE, "margin:0;
            // padding:0;", null);
        }
    }

    /**
     * @param context
     * @param component
     * @param writer
     * @param clientId
     * @throws IOException
     */
    public void encodeBeginForm(FacesContext context, UIComponent component, ResponseWriter writer, String clientId)
        throws IOException {

        String actionURL = getActionUrl(context);
        String encodeActionURL = context.getExternalContext().encodeActionURL(actionURL);

        writer.startElement(HtmlConstants.FORM_ELEMENT, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId, null);
        writer.writeAttribute(HtmlConstants.METHOD_ATTRIBUTE, "post", null);
        writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, "margin:0; padding:0; display: inline;", null);
        writer.writeURIAttribute(HtmlConstants.ACTION_ATTRIBUTE, encodeActionURL, "action");
    }

    /**
     * @param context
     * @param component
     * @throws IOException
     */
    public void encodeEndFormIfNessesary(FacesContext context, UIComponent component) throws IOException {
        UIComponent form = getNestingForm(context, component);

        if (null == form) {
            ResponseWriter writer = context.getResponseWriter();

            // TODO - hidden form parameters ?
            encodeEndForm(context, writer);
        }
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
     * @param context
     * @param writer
     * @throws IOException
     */
    public void encodeEndForm(FacesContext context, ResponseWriter writer) throws IOException {

        UIViewRoot viewRoot = context.getViewRoot();
        for (UIComponent resource : viewRoot.getComponentResources(context, "form")) {
            resource.encodeAll(context);
        }

        writeState(context);
        writer.endElement(HtmlConstants.FORM_ELEMENT);
    }

    /**
     * @param facesContext
     * @return String A String representing the action URL
     */
    public String getActionUrl(FacesContext facesContext) {
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        String viewId = facesContext.getViewRoot().getViewId();

        return viewHandler.getActionURL(facesContext, viewId);
    }

    /**
     * Simplified version of {@link encodeId(FacesContext, UIComponent)}
     *
     * @param context
     * @param component
     * @return client id of current component
     */
    public String clientId(FacesContext context, UIComponent component) {
        String clientId = "";

        try {
            clientId = component.getClientId(context);
        } catch (Exception e) {
            // just ignore
        }

        return clientId;
    }

    /**
     * Write JavaScript with start/end elements and type.
     *
     * @param context
     * @param component
     * @param script
     */
    public void writeScript(FacesContext context, UIComponent component, Object script) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HtmlConstants.SCRIPT_ELEM, component);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, "text/javascript", "type");
        writer.writeText(script, null);
        writer.endElement(HtmlConstants.SCRIPT_ELEM);
    }

    /**
     * If target component contains generated id and for doesn't, correct for id
     *
     * @param forAttr
     * @param component
     *
     */
    public String correctForIdReference(String forAttr, UIComponent component) {
        int contains = forAttr.indexOf(UIViewRoot.UNIQUE_ID_PREFIX);

        if (contains <= 0) {
            String id = component.getId();
            int pos = id.indexOf(UIViewRoot.UNIQUE_ID_PREFIX);

            if (pos > 0) {
                return forAttr.concat(id.substring(pos));
            }
        }

        return forAttr;
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                child.encodeAll(context);
            }
        }
    }

    public boolean hasExplicitId(UIComponent component) {
        return component.getId() != null && !component.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX);
    }

    /**
     * Deprecated, use {@link #findComponentFor(UIComponent, String)}.
     */
    @Deprecated
    public UIComponent findComponentFor(FacesContext context, UIComponent component, String id) {
        return findComponentFor(component, id);
    }

    /**
     * <p>A modified JSF alghoritm for looking up components.</p>
     *
     * <p>First try to find the component with given ID in subtree and then lookup in parents' subtrees.</p>
     *
     * <p>If no component is found this way, it uses {@link #findUIComponentBelow(UIComponent, String)} applied to root component.</p>
     *
     * @param component
     * @param id
     * @return
     */
    public UIComponent findComponentFor(UIComponent component, String id) {
        if (id == null) {
            throw new NullPointerException("id is null!");
        }

        if (id.length() == 0) {
            return null;
        }

        UIComponent target = null;
        UIComponent parent = component;
        UIComponent root = component;

        while ((null == target) && (null != parent)) {
            target = parent.findComponent(id);
            root = parent;
            parent = parent.getParent();
        }

        if (null == target) {
            target = findUIComponentBelow(root, id);
        }

        return target;
    }

    /**
     * Looks up component with given ID in subtree of given component including all component's chilren, component's facets and subtrees under naming containers.
     */
    private UIComponent findUIComponentBelow(UIComponent root, String id) {
        UIComponent target = null;

        for (Iterator<UIComponent> iter = root.getFacetsAndChildren(); iter.hasNext();) {
            UIComponent child = (UIComponent) iter.next();

            if (child instanceof NamingContainer) {
                try {
                    target = child.findComponent(id);
                } catch (IllegalArgumentException iae) {
                    continue;
                }
            }

            if (target == null) {
                if ((child.getChildCount() > 0) || (child.getFacetCount() > 0)) {
                    target = findUIComponentBelow(child, id);
                }
            }

            if (target != null) {
                break;
            }
        }

        return target;
    }
}
