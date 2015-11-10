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

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.StyleClassProps;
import org.richfaces.component.attribute.StyleProps;

/**
 * <p>
 * The &lt;a4j:log&gt; component generates JavaScript that opens a debug window, logging application information such as
 * requests, responses, and DOM changes.
 * </p>
 * @author Nick Belaevski
 */
@ResourceDependencies(value = { @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "log.js"),
        @ResourceDependency(library = "org.richfaces", name = "log.ecss") })
@JsfComponent(tag = @Tag(name = "log", type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.AjaxLogRenderer"))
public abstract class AbstractAjaxLog extends UIComponentBase implements StyleProps, StyleClassProps {
    public static final String COMPONENT_TYPE = "org.richfaces.AjaxLog";
    public static final String COMPONENT_FAMILY = "org.richfaces.AjaxLog";

    /**
     * Sets the logging level, can be one of 'debug', 'info' (default), 'warn', 'error'.
     */
    @Attribute
    public abstract String getLevel();

    /**
     * Determines how the log appears on the page, can be one of: "inline" (default), "popup" or "console". When set to "popup", the popup
     * window is opened by pressing the key combination Ctrl + Shift + L. "console" mode will log messages in the JavaScript console.
     */
    @Attribute
    public abstract LogMode getMode();

    /**
     * Key to open (in combination with Ctrl+Shift) the popup window. Default value is "L"
     */
    @Attribute
    public abstract String getHotkey();

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
