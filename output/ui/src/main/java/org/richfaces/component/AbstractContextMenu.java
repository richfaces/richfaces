/**
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
 **/
package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.ContextMenuRendererBase;
import org.richfaces.renderkit.html.DropDownMenuRendererBase;

import javax.faces.component.UIComponentBase;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(family = AbstractContextMenu.COMPONENT_FAMILY, type = AbstractContextMenu.COMPONENT_TYPE,
        renderer = @JsfRenderer(type = ContextMenuRendererBase.RENDERER_TYPE),
        tag = @Tag(name = "contextMenu"), attributes = {"events-props.xml", "core-props.xml", "i18n-props.xml" })
public abstract class AbstractContextMenu extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.richfaces.ContextMenu";
    public static final String COMPONENT_FAMILY = "org.richfaces.ContextMenu";

    @Attribute
    public abstract String getShowEvent();

    @Attribute
    public abstract Mode getMode();

    @Attribute
    public abstract boolean isDisabled();

    @Attribute
    public abstract int getHideDelay();

    @Attribute
    public abstract int getShowDelay();

    @Attribute
    public abstract int getPopupWidth();

    @Attribute
    public abstract int getHorizontalOffset();

    @Attribute
    public abstract int getVerticalOffset();

    // TODO is it correct or cdk issue
    @Attribute
    public abstract Positioning getJointPoint();

    @Attribute
    public abstract Positioning getDirection();

    @Attribute(events = @EventName("groupshow"))
    public abstract String getOngroupshow();

    @Attribute(events = @EventName("grouphide"))
    public abstract String getOngrouphide();

    @Attribute(events = @EventName("show"))
    public abstract String getOnshow();

    @Attribute(events = @EventName("hide"))
    public abstract String getOnhide();

    @Attribute(events = @EventName("itemclick"))
    public abstract String getOnitemclick();

    @Attribute
    public abstract String getLabel();

    @Attribute(generate = false, hidden = true, readOnly = true)
    public Object getCssRoot() {
        return "ctx";
    }

    public enum Facets {
        label,
        labelDisabled
    }
}

