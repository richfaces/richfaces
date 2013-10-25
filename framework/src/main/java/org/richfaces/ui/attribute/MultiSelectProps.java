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

package org.richfaces.ui.attribute;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;

/**
 * Interface defining the methods for multiselect-props.xml
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public interface MultiSelectProps {
    /**
     * Expose the value from the value attribute under this request scoped key so that it may be referred to in EL for the value of other attributes.
     */
    @Attribute
    String getVar();

    /**
     * Optional attribute that is a literal string that is the fully qualified class name of a concrete class that
     * implements java.util.Collection, or an EL expression that evaluates to either 1. such a String, or 2. the Class object itself.
     */
    @Attribute
    String getCollectionType();

    /**
     * The height of the list element (in pixels)
     */
    @Attribute
    String getListHeight();

    /**
     * The minimum height of the item list (in pixels).
     */
    @Attribute()
    String getMinListHeight();

    /**
     * The maximum height of the item list (in pixels).
     */
    @Attribute()
    String getMaxListHeight();

    /**
     * Space-separated list of CSS style class(es) to be applied to the columns of the list elements. This value must be passed through as the "class" attribute on generated markup.
     */
    @Attribute
    String getColumnClasses();

    /**
     * Javascript code executed when the list element loses focus and its value has been modified since gaining focus.
     */
    @Attribute(events = @EventName(value = "change", defaultEvent = true))
    String getOnchange();

    /**
     * Javascript code executed when this element receives focus
     */
    @Attribute(events = @EventName("focus"))
    String getOnfocus();

    /**
     * Javascript code executed when this element loses focus.
     */
    @Attribute(events = @EventName("blur"))
    String getOnblur();
}