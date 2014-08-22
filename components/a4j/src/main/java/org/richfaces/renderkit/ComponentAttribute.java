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

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Nick Belaevski
 */
public class ComponentAttribute implements Comparable<ComponentAttribute> {
    public enum Kind {
        BOOL,
        GENERIC,
        URI
    }

    private final String htmlAttributeName;
    private String componentAttributeName;
    private Object defaultValue;
    private String[] eventNames = {};
    private Kind kind = Kind.GENERIC;

    // TODO handling for aliases: "styleClass" -> "class"

    public ComponentAttribute(String htmlAttributeName) {
        super();
        this.htmlAttributeName = htmlAttributeName;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the defaultValue
     */
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the kind
     */
    public Kind getKind() {
        return this.kind;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param kind the kind to set
     */
    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public static final Map<String, ComponentAttribute> createMap(ComponentAttribute... attributes) {
        Map<String, ComponentAttribute> result = new TreeMap<String, ComponentAttribute>();

        for (ComponentAttribute componentAttribute : attributes) {
            result.put(componentAttribute.getComponentAttributeName(), componentAttribute);
        }

        return result;
    }

    /**
     * @return the name
     */
    public String getHtmlAttributeName() {
        return htmlAttributeName;
    }

    /**
     * @return the componentAttributeName
     */
    public String getComponentAttributeName() {
        return (componentAttributeName == null) ? htmlAttributeName : componentAttributeName;
    }

    /**
     * @param componentAttributeName the componentAttributeName to set
     * @return
     */
    public ComponentAttribute setComponentAttributeName(String componentAttributeName) {
        this.componentAttributeName = componentAttributeName;

        return this;
    }

    /**
     * @return the eventNames
     */
    public String[] getEventNames() {
        return eventNames;
    }

    /**
     * @param eventNames the eventNames to set
     * @return
     */
    public ComponentAttribute setEventNames(String... eventNames) {
        this.eventNames = eventNames;

        return this;
    }

    public int compareTo(ComponentAttribute o) {
        return getHtmlAttributeName().compareTo(o.getHtmlAttributeName());
    }
}
