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
package org.richfaces.ui.select.inplaceSelect;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.CoreProps;
import org.richfaces.ui.attribute.EventsKeyProps;
import org.richfaces.ui.attribute.EventsMouseProps;
import org.richfaces.ui.attribute.FocusProps;
import org.richfaces.ui.attribute.SelectProps;
import org.richfaces.ui.common.InplaceComponent;
import org.richfaces.ui.common.InplaceState;
import org.richfaces.ui.select.AbstractSelectComponent;

/**
 * <p> The &lt;r:inplaceSelect&gt; component is similar to the &lt;r:inplaceInput&gt; component, except that the
 * &lt;r:inplaceSelect&gt; component uses a drop-down selection box to enter text instead of a regular text field.
 * Changes can be rendered either in-line or for the whole block, and inputs can be focused with keyboard navigation.
 * The component is based on the JSF UISelectOne component, so all the standard rules for value definition, processing,
 * conversion, and validation apply. </p>
 *
 * @author Anton Belevich
 */
@JsfComponent(type = AbstractInplaceSelect.COMPONENT_TYPE, family = AbstractInplaceSelect.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.ui.InplaceSelectRenderer"), tag = @Tag(name = "inplaceSelect"))
public abstract class AbstractInplaceSelect extends AbstractSelectComponent implements InplaceComponent, CoreProps, EventsKeyProps, EventsMouseProps, FocusProps, SelectProps {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.InplaceSelect";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.Select";

    /**
     * The width of the input element
     */
    @Attribute
    public abstract String getInputWidth();

    /**
     * If "true", this component is disabled
     */
    @Attribute
    public abstract boolean isDisabled();

    /**
     * If "false" do not switch the component to the edit state on "click"
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isOpenOnEdit();

    /**
     * When "false" do not save the item as the new control value when the user selects an item from the drop-down list
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isSaveOnSelect();

    /**
     *  If "true", apply the changes when the focus is lost
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isSaveOnBlur();

    /**
     * If "true" is set, buttons for confirming or canceling are added to the component
     */
    @Attribute
    public abstract boolean isShowControls();

    @Attribute
    public abstract String getDefaultLabel();

    /**
     * <p>The event on which to switch the component to the edit state</p>
     * <p>Default is "click"</p>
     */
    @Attribute()
    public abstract String getEditEvent();

    //--------- input events

    /**
     * Javascript code executed when a pointer button is clicked over the input element.
     */
    @Attribute(events = @EventName("inputclick"))
    public abstract String getOninputclick();

    /**
     * Javascript code executed when a pointer button is double clicked over the input element.
     */
    @Attribute(events = @EventName("inputdblclick"))
    public abstract String getOninputdblclick();

    /**
     * Javascript code executed when a pointer button is pressed down over the input element.
     */
    @Attribute(events = @EventName("inputmousedown"))
    public abstract String getOninputmousedown();

    /**
     * Javascript code executed when a pointer button is released over the input element.
     */
    @Attribute(events = @EventName("inputmouseup"))
    public abstract String getOninputmouseup();

    /**
     * Javascript code executed when a pointer button is moved onto the input element.
     */
    @Attribute(events = @EventName("inputmouseover"))
    public abstract String getOninputmouseover();

    /**
     * Javascript code executed when a pointer button is moved within the input element.
     */
    @Attribute(events = @EventName("inputmousemove"))
    public abstract String getOninputmousemove();

    /**
     * Javascript code executed when a pointer button is moved away from the input element.
     */
    @Attribute(events = @EventName("inputmouseout"))
    public abstract String getOninputmouseout();

    /**
     * Javascript code executed when a key is pressed down over the input element.
     */
    @Attribute(events = @EventName("inputkeydown"))
    public abstract String getOninputkeydown();

    /**
     * Javascript code executed when a key is pressed and released over the input element.
     */
    @Attribute(events = @EventName("inputkeypress"))
    public abstract String getOninputkeypress();

    /**
     * Javascript code executed when a key is released over the input element.
     */
    @Attribute(events = @EventName("inputkeyup"))
    public abstract String getOninputkeyup();

    /**
     * Javascript code executed when text in the input element is selected
     */
    @Attribute(events = @EventName("inputselect"))
    public abstract String getOninputselect();

    @Attribute(hidden = true)
    public abstract InplaceState getState();
}
