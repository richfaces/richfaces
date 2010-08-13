/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.renderkit.ComponentAttribute.Kind;

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

    private static final String[] BOOLEAN_ATTRIBUTE_NAMES = { "checked", "compact", "declare", "defer", "disabled",
        "ismap", "multiple", "nohref", "noshade", "nowrap", "readonly", "selected" };

    private static final String[] URI_ATTRIBUTE_NAMES = { "action", "background", "cite", "classid", "codebase",
        "data", "href", "longdesc", "profile", "src", "usemap" };

    private static final String[] XHTML_ATTRIBUTE_NAMES = { "lang" };

    private static final String DISABLED_ATTRIBUTE_NAME = "disabled";

    private RenderKitUtils() {
        // utility constructor
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
            result.insert(0, "jsf.util.chain(");
            result.append(")");
        }

        return result.toString();
    }

    public static boolean shouldRenderAttribute(Object attributeValue) {
        if (attributeValue == null) {
            return false;
        } else if (attributeValue instanceof String) {
            return ((String) attributeValue).length() > 0;
        } else if (attributeValue instanceof Boolean && Boolean.FALSE.equals(attributeValue)) {
            return false;
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
                            ClientBehaviorContext behaviorContext =
                                ClientBehaviorContext.createClientBehaviorContext(facesContext, component, eventName,
                                    null, null);
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

        boolean disabled = isDisabled(component);
        Set<String> handledAttributes = new HashSet<String>(knownAttributesMap.size());
        Object attributesThatAreSetObject = component.getAttributes().get(ATTRIBUTES_THAT_ARE_SET);
        if (attributesThatAreSetObject instanceof Collection<?>) {
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

    public static void renderPassThroughAttributes(FacesContext context, UIComponent component, Collection<ComponentAttribute> attributes)
        throws IOException {
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
        String clientId = component.getClientId(context);

        if (behaviorSource != null && behaviorSource.equals(clientId)) {
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

    public static Attributes attributes(Enum<?> ... attrs) {
        Attributes res = new Attributes();
        for (Enum<?> attr : attrs) {
            res.generic(attr.toString(), attr.toString());
        }

        return res;
    }
    
    public static Attributes attributes(String ... attrs) {
        Attributes res = new Attributes();
        for (String attr : attrs) {
            res.generic(attr, attr);
        }
        
        return res;
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
        
        public Attributes defaultValue(Object value){
            last.setDefaultValue(value);
            return this;
        }

    }

}
