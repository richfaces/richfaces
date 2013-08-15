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
package org.richfaces.ui.ajax.log;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

/**
 * <p>
 * The &lt;r:log&gt; component generates JavaScript that opens a debug window, logging application information such as
 * requests, responses, and DOM changes.
 * </p>
 * @author Nick Belaevski
 */
@ResourceDependencies(value = { @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "ajax/log.js"),
        @ResourceDependency(library = "org.richfaces", name = "ajax/log.ecss") })
@JsfComponent(tag = @Tag(name = "log", type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.ui.AjaxLogRenderer"))
public abstract class AbstractAjaxLog extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.AjaxLog";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.AjaxLog";

    /**
     * CSS style(s) to be applied when this component is rendered.
     */
    @Attribute
    public abstract String getStyle();

    /**
     * Sets the logging level, can be one of 'debug', 'info', 'warn', 'error'.
     */
    @Attribute
    public abstract String getLevel();

    /**
     * Space-separated list of CSS style class(es) to be applied when this element is rendered. This value must be
     * passed through as the "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getStyleClass();

    /**
     * Determines how the log appears on the page, can be one of: "inline", "popup". When set to "popup", the popup
     * window is opened by pressing the key combination Ctrl + Shift + L
     */
    @Attribute
    public abstract LogMode getMode();

    // public abstract String getHotkey();
    // public abstract void setHotkey(String newvalue);
    //
    // public abstract String getName();
    // public abstract void setName(String newvalue);
    //
    // public abstract String getWidth();
    // public abstract void setWidth(String newvalue);
    //
    // public abstract String getHeight();
    // public abstract void setHeight(String newvalue);
    //
    // public abstract String getLevel();
    // public abstract void setLevel(String newvalue);
    //
    // public abstract boolean isPopup();
    // public abstract void setPopup(boolean popup);
}
