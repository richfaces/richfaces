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
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.AjaxProps;
import org.richfaces.ui.attribute.BypassProps;
import org.richfaces.ui.attribute.CoreProps;
import org.richfaces.ui.attribute.EventsKeyProps;
import org.richfaces.ui.attribute.EventsMouseProps;
import org.richfaces.ui.attribute.I18nProps;
import org.richfaces.ui.common.AbstractActionComponent;
import org.richfaces.ui.common.Mode;
import org.richfaces.ui.menu.dropDownMenu.AbstractDropDownMenu;

/**
 * <p>The &lt;r:menuItem&gt; component represents a single item in a menu control. The &lt;r:menuItem&gt;
 * component can be also be used as a seperate component without a parent menu component, such as on a toolbar.</p>
 */
@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractMenuItem.COMPONENT_TYPE,
        facets = {@Facet(name = "icon", generate = false), @Facet(name = "iconDisabled", generate = false) },
        renderer = @JsfRenderer(type = MenuItemRendererBase.RENDERER_TYPE), tag = @Tag(name = "menuItem"))
public abstract class AbstractMenuItem extends AbstractActionComponent implements AjaxProps, BypassProps, CoreProps, EventsKeyProps, EventsMouseProps, I18nProps {
    public static final String COMPONENT_TYPE = "org.richfaces.MenuItem";
    public static final String CSS_ROOT_DEFAULT = "ddm";

    /**
     * <p>Determines how the menu item requests are submitted.  Valid values:</p>
     * <ol>
     *     <li>server, the default setting, submits the form normally and completely refreshes the page.</li>
     *     <li>ajax performs an Ajax form submission, and re-renders elements specified with the render attribute.</li>
     *     <li>
     *         client causes the action and actionListener items to be ignored, and the behavior is fully defined by
     *         the nested components instead of responses from submissions
     *     </li>
     * </ol>
     */
    @Attribute
    public abstract Mode getMode();

    /**
     * <p>The text label for the menu item. Alternatively, use the label facet to define content for the label</p>
     * <p>Default is server</p>
     */
    @Attribute
    public abstract Object getLabel();

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
     * Disables the menu component, so it will not be clickable
     */
    @Attribute
    public abstract boolean isDisabled();

    @Attribute(hidden = true)
    public abstract Object getValue();

    @Attribute(generate = false, hidden = true, readOnly = true)
    public Object getCssRoot() {
        Object cssRoot = getParent().getAttributes().get("cssRoot");
        if (cssRoot == null) {
            cssRoot = CSS_ROOT_DEFAULT;
        }
        return cssRoot;
    }

    public enum Facets {
        icon,
        iconDisabled
    }
}
