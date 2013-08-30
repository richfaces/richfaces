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

package org.richfaces.ui.menu.menu;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.CoreProps;
import org.richfaces.ui.attribute.EventsKeyProps;
import org.richfaces.ui.attribute.EventsMouseProps;
import org.richfaces.ui.attribute.I18nProps;
import org.richfaces.ui.common.Positioning;
import org.richfaces.ui.menu.dropDownMenu.AbstractDropDownMenu;

import javax.faces.component.UIOutput;

/**
 * <p>The &lt;r:menuGroup&gt; component represents an expandable sub-menu in a menu control. The
 * &lt;r:menuGroup&gt; component can contain a number of &lt;r:menuItem&gt; components, or further nested
 * &lt;r:menuGroup&gt; components.</p>
 */
@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractMenuGroup.COMPONENT_TYPE,
        facets = {@Facet(name = "icon", generate = false), @Facet(name = "iconDisabled", generate = false) },
        renderer = @JsfRenderer(type = MenuGroupRendererBase.RENDERER_TYPE), tag = @Tag(name = "menuGroup"),
        attributes = {"position-props.xml" })
public abstract class AbstractMenuGroup extends UIOutput implements CoreProps, EventsKeyProps, EventsMouseProps, I18nProps {
    public static final String COMPONENT_TYPE = "org.richfaces.MenuGroup";

    /**
     * Disables the menu component, so it will not activate/expand
     */
    @Attribute
    public abstract boolean isDisabled();

    /**
     * The icon to be displayed with the menu item
     */
    @Attribute
    public abstract String getIcon();

    /**
     * The icon to be displayed with the menu item when it is disabled
     */
    @Attribute
    public abstract String getIconDisabled();

    /**
     * The text label for the menu item. Alternatively, use the label facet to define content for the label
     */
    @Attribute
    public abstract String getLabel();

    //---------- position-props.xml

    @Attribute
    public abstract Positioning getDirection();

    // TODO is it correct or cdk issue
    @Attribute
    public abstract Positioning getJointPoint();

    @Attribute
    public abstract int getVerticalOffset();

    @Attribute
    public abstract int getHorizontalOffset();

    /**
     * The client-side script method to be called when this menuGroup is shown
     */
    @Attribute(events = @EventName("show"))
    public abstract String getOnshow();

    /**
     * The client-side script method to be called when this menuGroup is hidden
     */
    @Attribute(events = @EventName("hide"))
    public abstract String getOnhide();

    @Attribute(hidden = true)
    public abstract Object getValue();

    @Attribute(generate = false, hidden = true, readOnly = true)
    public Object getCssRoot() {
        return getParent().getAttributes().get("cssRoot");
    }

    public enum Facets {
        icon,
        iconDisabled
    }
}
