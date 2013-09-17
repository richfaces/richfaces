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

package org.richfaces.ui.menu.dropDownMenu;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.menu.menu.AbstractMenuContainer;

/**
 * The &lt;r:dropDownMenu&gt; component is used for creating a drop-down, hierarchical menu. It can be used with the
 * &lt;r:toolbar&gt; component to create menus in an application's toolbar.
 */
@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractDropDownMenu.COMPONENT_TYPE, facets = {
        @Facet(name = "label", generate = false), @Facet(name = "labelDisabled", generate = false) }, renderer = @JsfRenderer(type = DropDownMenuRendererBase.RENDERER_TYPE), tag = @Tag(name = "dropDownMenu"), attributes = {
        "events-mouse-props.xml", "events-key-props.xml", "core-props.xml", "i18n-props.xml", "position-props.xml" })
public abstract class AbstractDropDownMenu extends AbstractMenuContainer {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.DropDownMenu";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.DropDownMenu";

    /**
     * The text label for the menu item. Alternatively, use the label facet to define content for the label
     */
    @Attribute
    public abstract String getLabel();

    public Object getCssRoot() {
        return "ddm";
    }

    public enum Facets {
        label,
        labelDisabled
    }
}
