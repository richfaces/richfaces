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

/**
 * @author Nick Belaevski
 *
 */
@ResourceDependencies(value = {
        @ResourceDependency(name = "jquery.js") , @ResourceDependency(name = "richfaces.js") ,
        @ResourceDependency(name = "richfaces-jsf-log.js"), @ResourceDependency(name = "richfaces.css")
        })
@JsfComponent(
        tag = @Tag(name = "log", type = TagType.Facelets),
        renderer = @JsfRenderer(type = "org.richfaces.AjaxLogRenderer")
)
public abstract class AbstractAjaxLog extends UIComponentBase {

    public static final String COMPONENT_TYPE="org.richfaces.AjaxLog";

    public static final String COMPONENT_FAMILY = "org.richfaces.AjaxLog";

    @Attribute(defaultValue = "")
    public abstract String getStyle();

    @Attribute(defaultValue = "")
    public abstract String getLevel();

    @Attribute(defaultValue = "")
    public abstract String getStyleClass();

//  public abstract String getHotkey();
//  public abstract void setHotkey(String newvalue);
//
//  public abstract String getName();
//  public abstract void setName(String newvalue);
//
//  public abstract String getWidth();
//  public abstract void setWidth(String newvalue);
//
//  public abstract String getHeight();
//  public abstract void setHeight(String newvalue);
//
//  public abstract String getLevel();
//  public abstract void setLevel(String newvalue);
//
//  public abstract boolean isPopup();
//  public abstract void setPopup(boolean popup);
}
