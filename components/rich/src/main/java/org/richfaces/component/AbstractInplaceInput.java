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

import javax.faces.component.UIInput;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.DisabledProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.FocusProps;

/**
 * <p> The &lt;rich:inplaceInput&gt; component allows information to be entered in-line in blocks of text, improving
 * readability of the text. Multiple input regions can be navigated with keyboard navigation. The component has three
 * functional states: the view state, where the component displays its initial setting, such as &quot;click to
 * edit&quot;; the edit state, where the user can input text; and the &quot;changed&quot; state, where the new value for
 * the component has been confirmed but can be edited again if required. </p>
 *
 * @author Anton Belevich
 */
@JsfComponent(type = AbstractInplaceInput.COMPONENT_TYPE, family = AbstractInplaceInput.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.InplaceInputRenderer"), tag = @Tag(name = "inplaceInput"))
public abstract class AbstractInplaceInput extends UIInput implements InplaceComponent, CoreProps, DisabledProps, EventsKeyProps, EventsMouseProps, FocusProps {
    public static final String COMPONENT_TYPE = "org.richfaces.InplaceInput";
    public static final String COMPONENT_FAMILY = "org.richfaces.InplaceInput";

    /**
     * Used to set the display text when value is undefined
     */
    @Attribute
    public abstract String getDefaultLabel();

    /**
     *  Defines whether the changes should apply when the focus is lost. Default value - "true".
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isSaveOnBlur();

    /**
     * <p>Used to specify the event that switches the component to the edit state</p>
     * <p>Default is "click"</p>
     */
    @Attribute
    public abstract String getEditEvent();

    /**
     * If "true" is set, buttons for confirming or canceling are added to the component
     */
    @Attribute
    public abstract boolean isShowControls();

    /**
     * The width of the input element
     */
    @Attribute
    public abstract String getInputWidth();

    /**
     * Space-separated list of CSS style class(es) to be applied when this element is active. This value must be
     * passed through as the "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getActiveClass();

    /**
     * Space-separated list of CSS style class(es) to be applied when the value of this element is changed. This value must be
     * passed through as the "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getChangedClass();

    /**
     * Space-separated list of CSS style class(es) to be applied when this element is rendered. This value must be
     * passed through as the "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getDisabledClass();

    /**
     * The client-side script method to be called when
     */
    @Attribute(events = @EventName("inputclick"))
    public abstract String getOninputclick();

    /**
     * The client-side script method to be called when
     */
    @Attribute(events = @EventName("inputdblclick"))
    public abstract String getOninputdblclick();

    /**
     * The client-side script method to be called when
     */
    @Attribute(events = @EventName("inputmousedown"))
    public abstract String getOninputmousedown();

    /**
     * The client-side script method to be called when
     */
    @Attribute(events = @EventName("inputmousemove"))
    public abstract String getOninputmousemove();

    /**
     * The client-side script method to be called when
     */
    @Attribute(events = @EventName("inputmouseout"))
    public abstract String getOninputmouseout();

    /**
     * The client-side script method to be called when
     */
    @Attribute(events = @EventName("inputmouseover"))
    public abstract String getOninputmouseover();

    /**
     * The client-side script method to be called when
     */
    @Attribute(events = @EventName("inputmouseup"))
    public abstract String getOninputmouseup();

    /**
     * The client-side script method to be called when
     */
    @Attribute(events = @EventName("inputkeydown"))
    public abstract String getOninputkeydown();

    /**
     * The client-side script method to be called when
     */
    @Attribute(events = @EventName("inputkeypress"))
    public abstract String getOninputkeypress();

    /**
     * The client-side script method to be called when
     */
    @Attribute(events = @EventName("inputkeyup"))
    public abstract String getOninputkeyup();

    /**
     * The client-side script method to be called when
     */
    @Attribute(events = @EventName("inputselect"))
    public abstract String getOninputselect();

    /**
     * Javascript code executed when this element loses focus and its value has been modified since gaining focus.
     */
    @Attribute(events = @EventName(value = "change", defaultEvent = true))
    public abstract String getOnchange();

    //---------

    @Attribute(hidden = true)
    public abstract InplaceState getState();

    // TODO: what is default event?, add onViewActivated, onEditActivated events support
}
