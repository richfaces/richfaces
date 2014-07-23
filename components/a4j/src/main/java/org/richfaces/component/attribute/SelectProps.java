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

package org.richfaces.component.attribute;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;

/**
 * Interface defining the methods for select-props.xml
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public interface SelectProps {

    /**
     * The width of the list element (in pixels)
     */
    @Attribute(description = @Description(value = "The width of the list element (in pixels)"))
    String getListWidth();

    /**
     * The height of the list element (in pixels)
     */
    @Attribute(description = @Description(value = "The height of the list element (in pixels)"))
    String getListHeight();

    /**
     * Used to set the display text when value is undefined
     */
    @Attribute(description = @Description(value = "Used to set the display text when value is undefined"))
    String getDefaultLabel();

    /**
     * Space-separated list of CSS style class(es) to be applied to the list element when it is in the "active" state.
     * This value must be passed through as the "class" attribute on generated markup.
     */
    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) to be applied to the list element when it is in the \"active\" state. This value must be passed through as the \"class\" attribute on generated markup."))
    String getActiveClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the list element when it is in the "changed" state.
     * This value must be passed through as the "class" attribute on generated markup.
     */
    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) to be applied to the list element when it is in the \"changed\" state. This value must be passed through as the \"class\" attribute on generated markup."))
    String getChangedClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the list items. This value must be passed through as the "class" attribute on generated markup.
     */
    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) to be applied to the list items. This value must be passed through as the \"class\" attribute on generated markup."))
    String getItemClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the selected list item. This value must be passed through as the "class" attribute on generated markup.
     */
    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) to be applied to the selected list item. This value must be passed through as the \"class\" attribute on generated markup."))
    String getSelectItemClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the list element when it is disabled. This value must be passed through as the "class" attribute on generated markup.
     */
    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) to be applied to the list element when it is disabled. This value must be passed through as the \"class\" attribute on generated markup."))
    String getDisabledClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the list element. This value must be passed through as the "class" attribute on generated markup.
     */
    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) to be applied to the list element. This value must be passed through as the \"class\" attribute on generated markup."))
    String getListClass();
}