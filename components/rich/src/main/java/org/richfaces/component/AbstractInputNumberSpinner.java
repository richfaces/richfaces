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
package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.AccesskeyProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.DisabledProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.FocusProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.component.attribute.InputProps;


/**
 * <p> The &amp;lt;r:inputNumberSpinner&amp;gt; component is a single-line input field with buttons to increase and decrease a numerical value.
 * The value can be changed using the corresponding directional keys on a keyboard, or by typing into the field.
 * </p>
 */
@JsfComponent(
        generate = "org.richfaces.component.html.HtmlInputNumberSpinner",
        type = AbstractInputNumberSpinner.COMPONENT_TYPE,
        family = AbstractInputNumberSpinner.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.InputNumberSpinnerRenderer"),
        tag = @Tag(name = "inputNumberSpinner"))
public abstract class AbstractInputNumberSpinner extends UIInputNumber implements AccesskeyProps, CoreProps, EventsKeyProps, EventsMouseProps, FocusProps, I18nProps, InputProps {
    public static final String COMPONENT_TYPE = "org.richfaces.InputNumberSpinner";
    public static final String COMPONENT_FAMILY = "javax.faces.Input";


    @Attribute(hidden = true)
    public abstract boolean isReadonly();

    /**
     * If "true" after the current value reaches the border value it is reversed to another border value after next increasing/decreasing.
     * In other case possibilities of next increasing (or decreasing) will be locked.
     * <br />
     * Default value is " true ".
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isCycled();

    /**
     * If set to "false" this attribute makes the text field "read-only", so the value can be changed only from a handle. &lt;br /&gt;Default value is "true".
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isEnableManualInput();

    /**
     * Assigns one or more space-separated CSS class names to the component input field
     */
    @Attribute(passThrough = true)
    public abstract String getInputClass();

    /**
     * Attribute specifies the initial length of input in characters.
     * <br />
     * Default value is "10".
     */
    @Attribute(defaultValue = "10")
    public abstract int getInputSize();

    /**
     * Attribute to set an "end" value.
     * <br />
     * Default value is "100"
     */
    @Attribute(defaultValue = "100")
    public abstract String getMaxValue();

    /**
     * Attribute to set the "start" value.
     * <br />
     * Default value is "0"
     */
    @Attribute(defaultValue = "0")
    public abstract String getMinValue();

    /**
     * The client-side script method to be called when the 'Down' button is clicked
     */
    @Attribute(events = @EventName("downclick"))
    public abstract String getOndownclick();

    /**
     * The client-side script method to be called when the component input field is clicked
     */
    @Attribute(events = @EventName("inputclick"))
    public abstract String getOninputclick();

    /**
     * The client-side script method to be called when the component input field is double-clicked
     */
    @Attribute(events = @EventName("inputdblclick"))
    public abstract String getOninputdblclick();

    /**
     * The client-side script method to be called when a key is pressed down in the input field
     */
    @Attribute(events = @EventName("inputkeydown"))
    public abstract String getOninputkeydown();

    /**
     * The client-side script method to be called when a key is pressed and released in the input field
     */
    @Attribute(events = @EventName("inputkeypress"))
    public abstract String getOninputkeypress();

    /**
     * The client-side script method to be called when a key is released in the input field
     */
    @Attribute(events = @EventName("inputkeyup"))
    public abstract String getOninputkeyup();

    /**
     * The client-side script method to be called when a mouse button is pressed down in the input field
     */
    @Attribute(events = @EventName("inputmousedown"))
    public abstract String getOninputmousedown();

    /**
     * The client-side script method to be called when a pointer is moved within the input field
     */
    @Attribute(events = @EventName("inputmousemove"))
    public abstract String getOninputmousemove();

    /**
     * The client-side script method to be called when a pointer is moved away from the input field
     */
    @Attribute(events = @EventName("inputmouseout"))
    public abstract String getOninputmouseout();

    /**
     * The client-side script method to be called when a pointer is moved onto the input field
     */
    @Attribute(events = @EventName("inputmouseover"))
    public abstract String getOninputmouseover();

    /**
     * The client-side script method to be called when a mouse button is released
     */
    @Attribute(events = @EventName("inputmouseup"))
    public abstract String getOninputmouseup();

    /**
     * The client-side script method to be called when the 'Up' button is clicked
     */
    @Attribute(events = @EventName("upclick"))
    public abstract String getOnupclick();

    /**
     * Parameter that determines the step between nearest values while using controls.
     * <br ./>
     * Default value is "1"
     */
    @Attribute(defaultValue = "1")
    public abstract String getStep();
}
