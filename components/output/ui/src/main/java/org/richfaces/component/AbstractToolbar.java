/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.component;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.ToolbarRendererBase;

/**
 * <p>The &lt;rich:toolbar&gt; component is a horizontal toolbar. Any JavaServer Faces (JSF) component can be added to the
 * toolbar.</p>
 */
@JsfComponent(family = AbstractToolbar.COMPONENT_FAMILY, type = AbstractToolbar.COMPONENT_TYPE, renderer = @JsfRenderer(type = ToolbarRendererBase.RENDERER_TYPE), tag = @Tag(name = "toolbar"), attributes = { "core-props.xml" })
public abstract class AbstractToolbar extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.richfaces.Toolbar";
    public static final String COMPONENT_FAMILY = "org.richfaces.Toolbar";

    /**
     * A height of a bar in pixels. If a height is not defined, a bar height depends of the "headerFontSize" skin parameter.
     */
    @Attribute
    public abstract String getHeight();

    /**
     * <p>A width of a bar that can be defined in pixels or as percentage.</p>
     * <p> Default value is "100%".</p>
     */
    @Attribute
    public abstract String getWidth();

    /**
     * CSS style(s) to be applied when the item elements are rendered.
     */
    @Attribute
    public abstract String getItemClass();

    /**
     * Space-separated list of CSS style class(es) to be applied when the item element is rendered. This value must be
     * passed through as the "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getItemStyle();

    /**
     * <p>A separator between items on a bar. Possible values are "none", "line", "square", "disc" and "grid".</p>
     * <p>Default value is "none".</p>
     */
    @Attribute
    public abstract String getItemSeparator();

    /**
     * The client-side script method to be called when an item is clicked
     */
    @Attribute(events = @EventName("itemclick"))
    public abstract String getOnitemclick();

    /**
     * The client-side script method to be called when an item is double-clicked
     */
    @Attribute(events = @EventName("itemdblclick"))
    public abstract String getOnitemdblclick();

    /**
     * The client-side script method to be called when a mouse button is pressed down over an item
     */
    @Attribute(events = @EventName("itemmousedown"))
    public abstract String getOnitemmousedown();

    /**
     * The client-side script method to be called when a mouse button is released over an item
     */
    @Attribute(events = @EventName("itemmouseup"))
    public abstract String getOnitemmouseup();

    /**
     * The client-side script method to be called when a pointer is moved onto an item
     */
    @Attribute(events = @EventName("itemmouseover"))
    public abstract String getOnitemmouseover();

    /**
     * The client-side script method to be called when a pointer is moved within an item
     */
    @Attribute(events = @EventName("itemmousemove"))
    public abstract String getOnitemmousemove();

    /**
     * The client-side script method to be called when a pointer is moved away from an item
     */
    @Attribute(events = @EventName("itemmouseout"))
    public abstract String getOnitemmouseout();

    /**
     * The client-side script method to be called when a key is pressed and released over an item
     */
    @Attribute(events = @EventName("itemkeypress"))
    public abstract String getOnitemkeypress();

    /**
     * The client-side script method to be called when a key is pressed down over the element
     */
    @Attribute(events = @EventName("itemkeydown"))
    public abstract String getOnitemkeydown();

    /**
     * The client-side script method to be called when a key is released
     */
    @Attribute(events = @EventName("itemkeyup"))
    public abstract String getOnitemkeyup();
}
