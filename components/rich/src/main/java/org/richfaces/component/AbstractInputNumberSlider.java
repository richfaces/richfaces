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
 * <p> The &amp;lt;r:inputNumberSlider&amp;gt; component provides a slider for changing numerical values.
 * Optional features include control arrows to step through the values,
 * a tool-tip to display the value while sliding,
 * and a text field for typing the numerical value which can then be validated against the slider's range.
 * </p>
 */
@JsfComponent(
        generate = "org.richfaces.component.html.HtmlInputNumberSlider",
        type = AbstractInputNumberSlider.COMPONENT_TYPE,
        family = AbstractInputNumberSlider.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.InputNumberSliderRenderer"),
        tag = @Tag(name = "inputNumberSlider"))
public abstract class AbstractInputNumberSlider extends UIInputNumber implements AccesskeyProps, CoreProps, EventsKeyProps, EventsMouseProps, FocusProps, I18nProps, InputProps {
    public static final String COMPONENT_TYPE = "org.richfaces.InputNumberSlider";
    public static final String COMPONENT_FAMILY = "javax.faces.Input";

    @Attribute(hidden = true)
    public abstract boolean isReadonly();

    /**
     * Assigns one or more space-separated CSS class names to the decrease arrow element
     */
    @Attribute(passThrough = true)
    public abstract String getDecreaseClass();

    /**
     * Assigns one or more space-separated CSS class names to the decrease arrow element selected
     */
    @Attribute(passThrough = true)
    public abstract String getDecreaseSelectedClass();

    /**
     * Delay in pressed increase/decrease arrows in miliseconds. &lt;br /&gt;Default value is "200".
     */
    @Attribute(passThrough = true, defaultValue = "200")
    public abstract int getDelay();

    /**
     * If set to "false" this attribute makes the text field "read-only", so the value can be changed only from a handle. &lt;br /&gt;Default value is "true".
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isEnableManualInput();

    /**
     * Assigns one or more space-separated CSS class names to the handle element
     */
    @Attribute(passThrough = true)
    public abstract String getHandleClass();

    /**
     * Assigns one or more space-separated CSS class names to the handle element selected
     */
    @Attribute(passThrough = true)
    public abstract String getHandleSelectedClass();

    /**
     * Assigns one or more space-separated CSS class names to the increase arrow element
     */
    @Attribute(passThrough = true)
    public abstract String getIncreaseClass();

    /**
     * Assigns one or more space-separated CSS class names to the increase arrow element selected
     */
    @Attribute(passThrough = true)
    public abstract String getIncreaseSelectedClass();

    /**
     * Assigns one or more space-separated CSS class names to the component input field
     */
    @Attribute(passThrough = true)
    public abstract String getInputClass();

    /**
     * If "right", the InputText Box would be rendered on the right side of the ruler.
     * If "left", the InputText Box would be rendered on the left side of the ruler.
     * If "top", the InputText Box would be rendered on the top of the ruler.
     * If "bottom", the InputText Box would be rendered on the bottom of the ruler.
     */
    @Attribute(defaultValue = "InputNumberSliderInputPosition.DEFAULT")
    public abstract InputNumberSliderInputPosition getInputPosition();

    /**
     * Similar to the "Size" attribute of h:inputText.
     * <br />
     * Default value is "3".
     */
    @Attribute(defaultValue = "3")
    public abstract int getInputSize();

    /**
     * Attribute to set an "end" value.
     * <br />
     * Default value is "100"
     */
    @Attribute(defaultValue = "100")
    public abstract String getMaxValue();

    /**
     * Specifies the maximum number of digits that could be entered into the input field.
     * The maximum number is unlimited by default.
     * If entered value exceeds the value specified in "maxValue" attribute than the slider takes a maximum value position.
     */
    @Attribute(defaultValue = "Integer.MIN_VALUE")
    public abstract Integer getMaxlength();

    /**
     * Attribute to set the "start" value.
     * <br />
     * Default value is "0"
     */
    @Attribute(defaultValue = "0")
    public abstract String getMinValue();

    /**
     * False value for this attribute makes increase/decrease arrows invisible.
     * <br />
     * Default value is "false".
     */
    @Attribute(defaultValue = "false")
    public abstract boolean isShowArrows();

    /**
     * If the min/max values are shown on the right/left borders of a control.
     * <br />
     * Default value is "true".
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isShowBoundaryValues();

    /**
     * False value for this attribute makes text a field invisible.
     * <br />
     * Default value is "true".
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isShowInput();

    /**
     * If "true"the current value is shown in the tooltip when a handle control is in a "dragged" state.
     * <br />
     * Default value is "true".
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isShowTooltip();

    /**
     * Parameter that determines a step between the nearest values while using a handle.
     * <br />
     * Default value is "1".
     */
    @Attribute(defaultValue = "1")
    public abstract String getStep();

    /**
     * Assigns one or more space-separated CSS class names to the tooltip element of the handle
     */
    @Attribute(passThrough = true)
    public abstract String getTooltipClass();

    /**
     * Assigns one or more space-separated CSS class names to the track slider element
     */
    @Attribute(passThrough = true)
    public abstract String getTrackClass();

    /**
     * Tells how handle should look like. Possible values: arrow (default), bar
     */
    @Attribute(passThrough = true, defaultValue = "org.richfaces.component.InputNumberSliderHandleType.DEFAULT")
    public abstract InputNumberSliderHandleType getHandleType();

}
