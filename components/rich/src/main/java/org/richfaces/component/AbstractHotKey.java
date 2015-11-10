/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * <p>The &lt;rich:hotKey&gt; component allows registering hot keys on the page or particular elements and defining
 * client side processing functions for these keys.</p>
 *
 * @author ilya_shaikovsky
 * @author Lukas Fryc
 */
@JsfComponent(type = AbstractHotKey.COMPONENT_TYPE, family = AbstractHotKey.COMPONENT_FAMILY, renderer = @JsfRenderer(type = "org.richfaces.HotKeyRenderer"), tag = @Tag(type = TagType.Facelets))
public abstract class AbstractHotKey extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.richfaces.HotKey";
    public static final String COMPONENT_FAMILY = "org.richfaces.HotKey";

    @Attribute(required = true, description = @Description("The key sequence to be pressed, single keys separated by + (e.g. 'ctrl+a'), more key sequences separated by space. Special keys are accepted as follows: backspace, tab, return, shift, ctrl, alt, pause, capslock, esc, space, pageup, pagedown, end, home, left, up, right, down, insert, del, numlock, scroll, meta, f1, f2, ..., f12"))
    public abstract String getKey();

    @Attribute(defaultValue = "false", description = @Description("The switch which enables handling events coming from input. Default value - \"false\""))
    public abstract boolean isEnabledInInput();

    @Attribute(defaultValue = "true", description = @Description("The switch which prevents native browser actions (prevents default) to be taken and stops event propagation up to the tree. Default value - \"true\""))
    public abstract boolean isPreventDefault();

    @Attribute(description = @Description("The jQuery selector (subset of CSS selectors defined by W3C) of the DOM root from which key events should be handled. When no value provided, events are handled for whole document. ID selectors starting with hash sign (#) will be expanded from componentId to clientId form. (e.g. #component is expanded to #form:component in case that component is nested in form)"))
    public abstract String getSelector();

    @Attribute(events = @EventName(value = "keydown", defaultEvent = true), description = @Description("Event handler to be fired when registered key sequence is pressed down"))
    public abstract String getOnkeydown();

    @Attribute(events = @EventName(value = "keyup"), description = @Description("Event handler to be fired when registered key sequence is pressed up"))
    public abstract String getOnkeyup();
}
