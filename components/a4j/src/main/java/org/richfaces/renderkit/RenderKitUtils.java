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
package org.richfaces.renderkit;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.ScriptUtils;
import org.richfaces.renderkit.ComponentAttribute.Kind;
import static org.richfaces.renderkit.AjaxConstants.AJAX_COMPONENT_ID_PARAMETER;

/**
 * @author Nick Belaevski
 *
 */
public final class RenderKitUtils {
    /**
     *
     */
    static final String BEHAVIOR_SOURCE_ID = "javax.faces.source";
    /**
     *
     */
    static final String BEHAVIOR_EVENT_NAME = "javax.faces.behavior.event";
    /**
     *
     */
    private static final String XHTML_ATTRIBUTE_PREFIX = "xml:";
    /**
     *
     */
    private static final String XHTML_CONTENT_TYPE = "application/xhtml+xml";
    // TODO - check what's in MyFaces
    private static final String ATTRIBUTES_THAT_ARE_SET = UIComponentBase.class.getName() + ".attributesThatAreSet";
    private static final String[] BOOLEAN_ATTRIBUTE_NAMES = { "checked", "compact", "declare", "defer", "disabled", "ismap",
            "multiple", "nohref", "noshade", "nowrap", "readonly", "selected" };
    private static final String[] URI_ATTRIBUTE_NAMES = { "action", "background", "cite", "classid", "codebase", "data",
            "href", "longdesc", "profile", "src", "usemap" };
    private static final String[] XHTML_ATTRIBUTE_NAMES = { "lang" };
    private static final String DISABLED_ATTRIBUTE_NAME = "disabled";

    /**
     * Wrapper class around object value used to transform values into particular JS objects
     *
     * @author Nick Belaevski
     * @since 3.3.2
     */
    public enum ScriptHashVariableWrapper {

        /**
         * No-op default wrapper
         */
        noop {
            @Override
            Object wrap(Object o) {
                return o;
            }
        },
        /**
         * Convert parameter to array of srings.
         */
        asArray {
            @Override
            Object wrap(Object o) {
                return asArray(o);
            }
        },
        /**
         * Event handler functions wrapper. Wraps
         *
         * <pre>
         * functionCode
         * </pre>
         *
         * object into:
         *
         * <pre>
         * function(event) {
         *   functionCode
         * }
         * </pre>
         */
        eventHandler {
            @Override
            Object wrap(Object o) {
                return new JSFunctionDefinition("event").addToBody(o);
            }
        };

        /**
         * Method that does the wrapping
         *
         * @param o object to wrap
         * @return wrapped object
         */
        abstract Object wrap(Object o);
    }

    private RenderKitUtils() {
        // utility constructor
    }

    public static String[] asArray(Object object) {
        if (object == null) {
            return null;
        }

        Class<?> componentType = object.getClass().getComponentType();

        if (String.class.equals(componentType)) {
            return (String[]) object;
        } else if (componentType != null) {
            Object[] objects = (Object[]) object;
            String[] result = new String[objects.length];

            for (int i = 0; i < objects.length; i++) {
                Object o = objects[i];

                if (o == null) {
                    continue;
                }

                result[i] = o.toString();
            }

            return result;
        } else if (object instanceof Collection) {
            Collection<?> collection = (Collection<?>) object;
            String[] result = new String[collection.size()];
            Iterator<?> iterator = collection.iterator();

            for (int i = 0; i < result.length; i++) {
                Object next = iterator.next();

                if (next == null) {
                    continue;
                }

                result[i] = next.toString();
            }

            return result;
        } else {
            String string = object.toString().trim();
            String[] split = string.split("\\s*,\\s*");

            return split;
        }
    }

    private static Map<String, List<ClientBehavior>> getClientBehaviorsMap(UIComponent component) {
        Map<String, List<ClientBehavior>> result;
        if (component instanceof ClientBehaviorHolder) {
            ClientBehaviorHolder clientBehaviorHolder = (ClientBehaviorHolder) component;

            result = clientBehaviorHolder.getClientBehaviors();
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }

    static boolean isDisabled(UIComponent component) {
        Object disabledAttributeValue = component.getAttributes().get(DISABLED_ATTRIBUTE_NAME);
        if (disabledAttributeValue == null) {
            return false;
        }

        if (disabledAttributeValue instanceof Boolean) {
            return Boolean.TRUE.equals(disabledAttributeValue);
        }

        return Boolean.valueOf(disabledAttributeValue.toString());
    }

    static String escape(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        int start = 0;
        int end;

        while ((end = s.indexOf('\'', start)) >= 0) {
            sb.append(s, start, end);
            sb.append("\\'");

            start = end + 1;
        }

        sb.append(s, start, s.length());

        return sb.toString();
    }

    static boolean chain(StringBuilder sb, Object object, boolean isChained) {
        if (object != null) {
            String objectString = object.toString().trim();
            if (objectString.length() != 0) {
                final boolean localIsChained;

                if (!isChained && sb.length() != 0) {
                    // extract previously stored handler
                    String previousHandlerString = sb.toString();
                    // clear builder object
                    sb.setLength(0);

                    // append escaped handler
                    sb.append("'");
                    sb.append(escape(previousHandlerString));
                    sb.append("'");

                    localIsChained = true;
                } else {
                    // use passed in value of chained indicator
                    localIsChained = isChained;
                }

                if (localIsChained) {
                    sb.append(",'");
                    sb.append(escape(objectString));
                    sb.append("'");

                    return true;
                } else {
                    sb.append(objectString);
                    return false;
                }
            }
        }

        // no changes, pass chained indicator we initially used
        return isChained;
    }

    private static Object createBehaviorsChain(Object inlineHandlerValue, ClientBehaviorContext behaviorContext,
            List<ClientBehavior> behaviors) {

        boolean isChained = false;
        StringBuilder result = new StringBuilder();

        isChained = chain(result, inlineHandlerValue, isChained);
        for (ClientBehavior behavior : behaviors) {
            isChained = chain(result, behavior.getScript(behaviorContext), isChained);

            if (behavior.getHints().contains(ClientBehaviorHint.SUBMITTING)) {
                break;
            }
        }

        if (result.length() == 0) {
            return null;
        }

        if (isChained) {
            result.insert(0, "return jsf.util.chain(this, event, ");
            result.append(")");
        }

        return result.toString();
    }

    private static boolean isAttributeSet(Object attributeValue) {
        // TODO - consider required attributes with "" value (like 'alt')
        if (attributeValue == null) {
            return false;
        } else if (attributeValue instanceof String) {
            return ((String) attributeValue).length() > 0;
        } else if (attributeValue instanceof Integer && (Integer) attributeValue == Integer.MIN_VALUE) {
            return false;
        } else if (attributeValue instanceof Double && (Double) attributeValue == Double.MIN_VALUE) {
            return false;
        } else if (attributeValue instanceof Character && (Character) attributeValue == Character.MIN_VALUE) {
            return false;
        } else if (attributeValue instanceof Float && (Float) attributeValue == Float.MIN_VALUE) {
            return false;
        } else if (attributeValue instanceof Short && (Short) attributeValue == Short.MIN_VALUE) {
            return false;
        } else if (attributeValue instanceof Byte && (Byte) attributeValue == Byte.MIN_VALUE) {
            return false;
        } else if (attributeValue instanceof Long && (Long) attributeValue == Long.MIN_VALUE) {
            return false;
        } else if (attributeValue instanceof Collection<?> || attributeValue instanceof Map<?, ?>) {
            return true;
        }

        return attributeValue.toString().length() > 0;
    }

    public static boolean shouldRenderAttribute(Object attributeValue) {
        // TODO - consider required attributes with "" value (like 'alt')
        if (!isAttributeSet(attributeValue)) {
            return false;
        }

        if (attributeValue instanceof Boolean && Boolean.FALSE.equals(attributeValue)) {
            return false;
        }

        return attributeValue.toString().length() > 0;
    }

    public static String prefixAttributeName(String attributeName, boolean isXhtmlMode) {
        if (isXhtmlMode) {
            if (Arrays.binarySearch(XHTML_ATTRIBUTE_NAMES, attributeName) >= 0) {
                return XHTML_ATTRIBUTE_PREFIX + attributeName;
            }
        }

        return attributeName;
    }

    public static String prefixAttributeName(String attributeName, ResponseWriter responseWriter) {
        return prefixAttributeName(attributeName, XHTML_CONTENT_TYPE.equals(responseWriter.getContentType()));
    }

    public static void renderAttribute(FacesContext facesContext, String attributeName, Object attributeValue)
            throws IOException {

        if (!shouldRenderAttribute(attributeValue)) {
            return;
        }

        ResponseWriter writer = facesContext.getResponseWriter();

        String prefixedAttributeName = prefixAttributeName(attributeName, writer);

        if (Arrays.binarySearch(URI_ATTRIBUTE_NAMES, attributeName) >= 0) {
            writer.writeURIAttribute(prefixedAttributeName, attributeValue, null);
        } else if (Arrays.binarySearch(BOOLEAN_ATTRIBUTE_NAMES, attributeName) >= 0) {
            boolean booleanAttributeValue = Boolean.valueOf(String.valueOf(attributeValue));
            if (booleanAttributeValue) {
                // TODO - is passing in Boolean.TRUE value documented somewhere?
                writer.writeAttribute(prefixedAttributeName, Boolean.TRUE, null);
            }
        } else {
            writer.writeAttribute(prefixedAttributeName, attributeValue, null);
        }
    }

    // TODO - create special method for event handlers that will return String?
    // TODO - add check for 'disabled'?
    public static Object getAttributeAndBehaviorsValue(FacesContext facesContext, UIComponent component,
            ComponentAttribute componentAttribute) {
        if (facesContext == null) {
            throw new NullPointerException("facesContext");
        }

        if (component == null) {
            throw new NullPointerException("component");
        }

        if (componentAttribute == null) {
            throw new NullPointerException("componentAttribute");
        }

        String componentAttributeName = componentAttribute.getComponentAttributeName();
        Object attributeValue = component.getAttributes().get(componentAttributeName);

        String[] eventNames = componentAttribute.getEventNames();
        if (eventNames.length > 0) {
            Map<String, List<ClientBehavior>> behaviorsMap = getClientBehaviorsMap(component);
            if (behaviorsMap.size() > 0) {
                for (String eventName : eventNames) {
                    if (behaviorsMap.containsKey(eventName)) {
                        List<ClientBehavior> behaviorsList = behaviorsMap.get(eventName);
                        if (!behaviorsList.isEmpty()) {
                            // TODO - parameters handling
                            ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(
                                    facesContext, component, eventName, null, null);
                            attributeValue = createBehaviorsChain(attributeValue, behaviorContext, behaviorsList);
                        }
                        break;
                    }
                }
            }
        }
        return attributeValue;
    }

    public static void renderAttributeAndBehaviors(FacesContext facesContext, UIComponent component,
            ComponentAttribute componentAttribute) throws IOException {
        Object attributeValue = getAttributeAndBehaviorsValue(facesContext, component, componentAttribute);
        renderAttribute(facesContext, componentAttribute.getHtmlAttributeName(), attributeValue);
    }

    public static void renderPassThroughAttributesOptimized(FacesContext context, UIComponent component,
            Map<String, ComponentAttribute> knownAttributesMap) throws IOException {

        Object attributesThatAreSetObject = component.getAttributes().get(ATTRIBUTES_THAT_ARE_SET);
        if (attributesThatAreSetObject instanceof Collection<?>) {
            boolean disabled = isDisabled(component);
            Set<String> handledAttributes = new HashSet<String>(knownAttributesMap.size());

            Collection<?> attributesThatAreSet = (Collection<?>) attributesThatAreSetObject;
            for (Object attributeNameObject : attributesThatAreSet) {
                if (attributeNameObject == null) {
                    continue;
                }

                String attributeName = attributeNameObject.toString();

                ComponentAttribute knownAttribute = knownAttributesMap.get(attributeName);
                if (knownAttribute != null) {
                    handledAttributes.add(knownAttribute.getHtmlAttributeName());

                    if (disabled && knownAttribute.getEventNames() != null) {
                        continue;
                    }

                    renderAttributeAndBehaviors(context, component, knownAttribute);
                }
            }

            // render attributes that haven't been processed yet - there can be behaviors
            for (ComponentAttribute knownAttribute : knownAttributesMap.values()) {

                if (handledAttributes.contains(knownAttribute.getHtmlAttributeName())) {
                    continue;
                }

                renderAttributeAndBehaviors(context, component, knownAttribute);
            }
        } else {
            // switch to unoptimized mode
            renderPassThroughAttributes(context, component, knownAttributesMap);
        }
    }

    public static void renderPassThroughAttributes(FacesContext context, UIComponent component,
            Map<String, ComponentAttribute> knownAttributesMap) throws IOException {
        Collection<ComponentAttribute> attributes = knownAttributesMap.values();

        renderPassThroughAttributes(context, component, attributes);
    }

    public static void renderPassThroughAttributes(FacesContext context, UIComponent component,
            Collection<ComponentAttribute> attributes) throws IOException {
        boolean disabled = isDisabled(component);
        for (ComponentAttribute knownAttribute : attributes) {
            if (!disabled || knownAttribute.getEventNames().length == 0) {
                renderAttributeAndBehaviors(context, component, knownAttribute);
            }
        }
    }

    public static String decodeBehaviors(FacesContext context, UIComponent component) {
        if (!(component instanceof ClientBehaviorHolder)) {
            return null;
        }

        ClientBehaviorHolder holder = (ClientBehaviorHolder) component;
        Map<String, List<ClientBehavior>> behaviors = holder.getClientBehaviors();

        if (behaviors == null || behaviors.isEmpty()) {
            return null;
        }

        ExternalContext externalContext = context.getExternalContext();
        Map<String, String> parametersMap = externalContext.getRequestParameterMap();
        String behaviorEvent = parametersMap.get(BEHAVIOR_EVENT_NAME);

        if (behaviorEvent == null) {
            return null;
        }

        List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);
        String behaviorSource = parametersMap.get(BEHAVIOR_SOURCE_ID);
        String ajaxSource = parametersMap.get(AJAX_COMPONENT_ID_PARAMETER);
        String clientId = component.getClientId(context);

        if (behaviorSource != null && behaviorSource.equals(clientId) ||
            clientId.equals(ajaxSource)) {
            if (behaviorsForEvent != null && !behaviorsForEvent.isEmpty()) {
                for (ClientBehavior behavior : behaviorsForEvent) {
                    behavior.decode(context, component);
                }

                return behaviorEvent;
            }
        }

        return null;
    }

    public static Attributes attributes() {
        return new Attributes();
    }

    public static Attributes attributes(Enum<?>... attrs) {
        Attributes res = new Attributes();
        for (Enum<?> attr : attrs) {
            res.generic(attr.toString(), attr.toString());
        }

        return res;
    }

    public static Attributes attributes(String... attrs) {
        Attributes res = new Attributes();
        for (String attr : attrs) {
            res.generic(attr, attr);
        }

        return res;
    }

    /**
     * Checks if the argument passed in is empty or not. Object is empty if it is: <br />
     * - <code>null<code><br />
     *  - zero-length string<br />
     *  - empty collection<br />
     *  - empty map<br />
     *  - zero-length array<br />
     *
     * @param o object to check for emptiness
     * @since 3.3.2
     * @return <code>true</code> if the argument is empty, <code>false</code> otherwise
     */
    private static boolean isEmpty(Object o) {
        if (null == o) {
            return true;
        }
        if (o instanceof String) {
            return (0 == ((String) o).length());
        }
        if (o instanceof Collection) {
            return ((Collection<?>) o).isEmpty();
        }
        if (o instanceof Map) {
            return ((Map<?, ?>) o).isEmpty();
        }
        if (o.getClass().isArray()) {
            return Array.getLength(o) == 0;
        }
        return false;
    }

    public static void addToScriptHash(Map<String, Object> hash, String name, Object value) {

        addToScriptHash(hash, name, value, null, null);
    }

    public static void addToScriptHash(Map<String, Object> hash, String name, Object value, Object defaultValue) {

        addToScriptHash(hash, name, value, defaultValue, null);
    }

    /**
     * Puts value into map under specified key if the value is not empty and not default. Performs optional value wrapping.
     *
     * @param hash
     * @param name
     * @param value
     * @param defaultValue
     * @param wrapper
     *
     * @since 3.3.2
     */
    public static void addToScriptHash(Map<String, Object> hash, String name, Object value, Object defaultValue,
            ScriptHashVariableWrapper wrapper) {

        ScriptHashVariableWrapper wrapperOrDefault = wrapper != null ? wrapper : ScriptHashVariableWrapper.noop;

        if (!isEmpty(value) && isAttributeSet(value)) {
            if (defaultValue != null) {
                if (!String.valueOf(defaultValue).equals(value.toString())) {
                    hash.put(name, wrapperOrDefault.wrap(value));
                }
            } else {
                if (!(value instanceof Boolean) || ((Boolean) value).booleanValue()) {
                    hash.put(name, wrapperOrDefault.wrap(value));
                }
            }
        }
    }

    public static void addToScriptHash(Map<String, Object> hash, FacesContext facesContext, UIComponent component,
            Attributes attributes, ScriptHashVariableWrapper wrapper) {

        boolean disabled = isDisabled(component);
        for (ComponentAttribute knownAttribute : attributes) {
            if (!disabled || knownAttribute.getEventNames().length == 0) {
                String attributeName = knownAttribute.getHtmlAttributeName();
                addToScriptHash(hash, attributeName, getAttributeAndBehaviorsValue(facesContext, component, knownAttribute),
                        knownAttribute.getDefaultValue(), wrapper);
            }
        }
    }

    public static String toScriptArgs(Object... objects) {
        if (objects == null) {
            return "";
        }

        int lastNonNullIdx = objects.length - 1;
        for (; 0 <= lastNonNullIdx; lastNonNullIdx--) {
            if (!isEmpty(objects[lastNonNullIdx])) {
                break;
            }
        }

        if (lastNonNullIdx < 0) {
            return "";
        }

        if (lastNonNullIdx == 0) {
            return ScriptUtils.toScript(objects[lastNonNullIdx]);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= lastNonNullIdx; i++) {
            if (sb.length() > 0) {
                sb.append(",");
            }

            sb.append(ScriptUtils.toScript(objects[i]));
        }

        return sb.toString();
    }

    public static String getResourcePath(FacesContext context, String library, String resourceName) {
        String path = null;
        if (resourceName != null) {
            ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
            Resource resource = (library != null) ? resourceHandler.createResource(resourceName, library) : resourceHandler
                    .createResource(resourceName);
            if (resource != null) {
                path = resource.getRequestPath();
            }
        }
        return path;
    }

    public static String getResourceURL(String url, FacesContext context) {
        if (null == url) {
            return null;
        }

        Application application = context.getApplication();
        ExternalContext externalContext = context.getExternalContext();

        String value = url;

        if (value.length() == 0 || value.charAt(0) != '/' || !value.startsWith(externalContext.getRequestContextPath())) {
            value = application.getViewHandler().getResourceURL(context, value);
        }

        return externalContext.encodeResourceURL(value);
    }

    public static Object getFirstNonEmptyAttribute(String attributeName, UIComponent component) {
        Object attributeValue = component.getAttributes().get(attributeName);

        return !isEmpty(attributeValue) ? attributeValue : null;
    }

    public static Object getFirstNonEmptyAttribute(String attributeName, UIComponent componentA, UIComponent componentB) {
        Object attributeValue = componentA.getAttributes().get(attributeName);

        if (!isEmpty(attributeValue)) {
            return attributeValue;
        }

        attributeValue = componentB.getAttributes().get(attributeName);

        if (!isEmpty(attributeValue)) {
            return attributeValue;
        }

        return null;
    }

    public static Object getFirstNonEmptyAttribute(String attributeName, UIComponent... components) {
        for (UIComponent component : components) {
            Object attributeValue = component.getAttributes().get(attributeName);
            if (!isEmpty(attributeValue)) {
                return attributeValue;
            }
        }

        return null;
    }

    @SuppressWarnings("serial")
    public static final class Attributes extends TreeSet<ComponentAttribute> {
        private ComponentAttribute last;

        public void render(FacesContext context, UIComponent component) throws IOException {
            renderPassThroughAttributes(context, component, this);
        }

        public Attributes generic(String name, String componentAttribute, String... events) {
            ComponentAttribute attribute = createAttribute(name, componentAttribute);
            attribute.setEventNames(events);
            attribute.setKind(Kind.GENERIC);
            return this;
        }

        private ComponentAttribute createAttribute(String name, String componentAttribute) {
            ComponentAttribute attribute = new ComponentAttribute(name);
            attribute.setComponentAttributeName(componentAttribute);
            add(attribute);
            last = attribute;
            return attribute;
        }

        public Attributes uri(String name, String componentAttribute) {
            ComponentAttribute attribute = createAttribute(name, componentAttribute);
            attribute.setKind(Kind.URI);
            return this;
        }

        public Attributes bool(String name, String componentAttribute) {
            ComponentAttribute attribute = createAttribute(name, componentAttribute);
            attribute.setKind(Kind.BOOL);
            return this;
        }

        public Attributes defaultValue(Object value) {
            last.setDefaultValue(value);
            return this;
        }
    }

    public static String getBehaviorSourceId(FacesContext facesContext) {
        return facesContext.getExternalContext().getRequestParameterMap().get(BEHAVIOR_SOURCE_ID);
    }

    public static boolean hasFacet(UIComponent component, String facetName) {
        return component.getFacet(facetName) != null && component.getFacet(facetName).isRendered();
    }



    /**
     * Tries to evaluate an attribute as {@link ValueExpression}. If the attribute doesn't hold {@link ValueExpression} or the
     * expression evaluates to <tt>null</tt>, the value of the attribute is returned.
     *
     * @param attribute the name of a component's attribute
     * @param component the component
     * @param context the context
     * @return the evaluated {@link ValueExpression} for a given attribute or the value of the attribute (in case the attribute
     *         isn't {@link ValueExpression} or it evaluates to null)
     */
    @SuppressWarnings("unchecked")
    public static <T> T evaluateAttribute(String attribute, UIComponent component, FacesContext context) {
        ValueExpression valueExpression = component.getValueExpression(attribute);

        if (valueExpression != null) {
            T evaluatedValue = (T) valueExpression.getValue(context.getELContext());
            if (evaluatedValue != null) {
                return evaluatedValue;
            }
        }

        return (T) component.getAttributes().get(attribute);
    }
}
