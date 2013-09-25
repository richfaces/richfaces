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
package org.richfaces.ui.ajax.status;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

import javax.faces.component.UIComponentBase;

/**
 * <p>
 * The &lt;r:status&gt; component displays the status of current Ajax requests. The status can be either in progress,
 * complete, or an error is shown after a failed request.
 * </p>
 * @author Nick Belaevski
 */
@JsfComponent(renderer = @JsfRenderer(type = "org.richfaces.ui.StatusRenderer"), tag = @Tag(type = TagType.Facelets))
public abstract class AbstractAjaxStatus extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.Status";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.Status";

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
     * The client-side script method to be called when the request results in an error
     */
    @Attribute(events = @EventName("error"))
    public abstract String getOnerror();

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
