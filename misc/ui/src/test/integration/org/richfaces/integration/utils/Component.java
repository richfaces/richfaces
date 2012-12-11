/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.integration.utils;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Set;

/**
 * Helper class for creating component and its String representation.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class Component {

    private final String name;
    private ComponentsList componentsInBody = new ComponentsList();
    private Set<Attribute> attributes = Sets.<Attribute>newLinkedHashSet();
    private XMLNS namespace;

    private Component(Component component) {
        this.name = component.name;
        this.componentsInBody.addAll(component.componentsInBody);
        this.namespace = component.namespace;
        this.attributes.addAll(component.attributes);
    }

    public Component(String name) {
        this.name = name;
    }

    public Component(String name, XMLNS namespace) {
        this.name = name;
        this.namespace = namespace;
    }

    protected boolean addAtribute(Attribute a) {
        return attributes.add(a);
    }

    public String getName() {
        return name;
    }

    public Component addToBody(Component... components) {
        Component result = new Component(this);
        result.componentsInBody.addAll(Arrays.asList(components));
        return result;
    }

    public Component addToBody(Component component) {
        Component result = new Component(this);
        result.componentsInBody.add(component);
        return result;
    }

    /**
     * Ignores all namespaces defined in XMLNS.ignored()
     */
    public Set<XMLNS> getAllNamespaces() {
        Set<XMLNS> result = Sets.<XMLNS>newLinkedHashSet();
        if (namespace != null) {
            result.add(namespace);
        }
        for (Component c : componentsInBody) {
            result.addAll(c.getAllNamespaces());
        }
        result.removeAll(XMLNS.ignored());
        return result;
    }

    public Component usingNameSpace(XMLNS namespace) {
        Component component = new Component(this);
        component.namespace = namespace;
        return component;
    }

    public Component withAttribute(Attribute a) {
        Component component = new Component(this);
        component.attributes.remove(a);
        component.addAtribute(a);
        return component;
    }

    public Component withAttribute(String name, String value) {
        Component component = new Component(this);
        Attribute a = new Attribute(name, value);
        component.attributes.remove(a);
        component.addAtribute(a);
        return component;
    }

    public Component withId(String id) {
        Component component = new Component(this);
        component.addAtribute(new Attribute("id", id));
        return component;
    }

    public Component withValue(String value) {
        Component component = new Component(this);
        component.addAtribute(new Attribute("value", value));
        return component;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        if (namespace != null && !namespace.name.isEmpty()) {
            sb.append(namespace.name).append(":");
        }
        sb.append(name).append(" ");
        for (Attribute attribute : attributes) {
            sb.append(attribute);
        }
        if (componentsInBody == null || componentsInBody.isEmpty()) {
            sb.append("/>");
        } else {//has body
            sb.append(">").append("\n").append(componentsInBody).append("\n</");
            if (namespace != null && !namespace.name.isEmpty()) {
                sb.append(namespace.name).append(":");
            }
            sb.append(name).append(">");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(attributes, componentsInBody, name, namespace);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Component other = (Component) obj;
        return Objects.equal(this.attributes, other.attributes)
                && Objects.equal(this.componentsInBody, other.componentsInBody)
                && Objects.equal(this.name, other.name)
                && Objects.equal(this.namespace, other.namespace);
    }

    public static Component br() {
        return new Component("br");
    }

    public static Component fConvertDateTime() {
        return new Component("convertDateTime", XMLNS.jsfCore());
    }
    public static Component fSelectItems() {
        return new Component("selectItems", XMLNS.jsfCore());
    }

    public static Component hInputText() {
        return new Component("inputText", XMLNS.jsfHtml());
    }

    public static Component hInputTextarea() {
        return new Component("inputTextarea", XMLNS.jsfHtml());
    }

    public static Component hOutputText() {
        return new Component("outputText", XMLNS.jsfHtml());
    }

    public static Component hCommandButton() {
        return new Component("commandButton", XMLNS.jsfHtml());
    }

    public static Component a4jCommandButton() {
        return new Component("commandButton", XMLNS.a4j());
    }

    public static Component a4jOutputPanel() {
        return new Component("outputPanel", XMLNS.a4j());
    }

    public static Component richAutocomplete() {
        return new Component("autocomplete", XMLNS.richInput());
    }

    public static Component richCalendar() {
        return new Component("calendar", XMLNS.richInput());
    }

    public static Component richInplaceInput() {
        return new Component("inplaceInput", XMLNS.richInput());
    }

    public static Component richInplaceSelect() {
        return new Component("inplaceSelect", XMLNS.richInput());
    }

    public static Component richPlaceholder() {
        return new Component("placeholder", XMLNS.richMisc());
    }

    public static Component richSelect() {
        return new Component("select", XMLNS.richInput());
    }

    public static Component uiDebug() {
        return new Component("debug", XMLNS.jsfFacelets());
    }
}
