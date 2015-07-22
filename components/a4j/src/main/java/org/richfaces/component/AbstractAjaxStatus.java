/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.component;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.ErrorProps;

/**
 * <p>
 * The &lt;a4j:status&gt; component displays the status of current Ajax requests. The status can be either in progress,
 * complete, or an error is shown after a failed request.
 * </p>
 * @author Nick Belaevski
 */
@JsfComponent(renderer = @JsfRenderer(type = "org.richfaces.StatusRenderer"), tag = @Tag(type = TagType.Facelets))
public abstract class AbstractAjaxStatus extends UIComponentBase implements ErrorProps {
    public static final String COMPONENT_TYPE = "org.richfaces.Status";
    public static final String COMPONENT_FAMILY = "org.richfaces.Status";

    /**
     * The client-side script method to be called when the request starts
     */
    @Attribute(events = @EventName("start"))
    public abstract String getOnstart();

    /**
     * The client-side script method to be called when the request stops
     */
    @Attribute(events = @EventName("stop"))
    public abstract String getOnstop();

    /**
     * The client-side script method to be called when the request completes successfully
     */
    @Attribute(events = @EventName("success"))
    public abstract String getOnsuccess();

    /**
     * The name of the status component
     */
    @Attribute
    public abstract String getName();

    /**
     * The text shown after the request has been started and is currently in progress
     */
    @Attribute
    public abstract String getStartText();

    /**
     * The text shown once the request is complete
     */
    @Attribute
    public abstract String getStopText();

    /**
     * The text shown when an error has occurred
     */
    @Attribute
    public abstract String getErrorText();

    /**
     * The css style attribute for the start text
     */
    @Attribute
    public abstract String getStartStyle();

    /**
     * The css style attribute for the stop text
     */
    @Attribute
    public abstract String getStopStyle();

    /**
     * The css style attribute for the error text
     */
    @Attribute
    public abstract String getErrorStyle();

    /**
     * The css class attribute used to style the start text
     */
    @Attribute
    public abstract String getStartStyleClass();

    /**
     * The css class attribute used to style the stop text
     */
    @Attribute
    public abstract String getStopStyleClass();

    /**
     * The css class attribute used to style the error text
     */
    @Attribute
    public abstract String getErrorStyleClass();
}
