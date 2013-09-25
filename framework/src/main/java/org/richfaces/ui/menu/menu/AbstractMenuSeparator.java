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
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.menu.dropDownMenu.AbstractDropDownMenu;

import javax.faces.component.UIComponentBase;

/**
 * <p>The &lt;r:menuSeparator&gt; component represents a separating divider in a menu control.</p>
 */
@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractMenuSeparator.COMPONENT_TYPE, renderer = @JsfRenderer(type = MenuSeparatorRendererBase.RENDERER_TYPE), tag = @Tag(name = "menuSeparator"))
public abstract class AbstractMenuSeparator extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.MenuSeparator";

    @Attribute(generate = false, hidden = true, readOnly = true)
    public Object getCssRoot() {
        return getParent().getAttributes().get("cssRoot");
    }

}
