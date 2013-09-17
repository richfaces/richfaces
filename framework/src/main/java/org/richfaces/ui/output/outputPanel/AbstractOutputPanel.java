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
package org.richfaces.ui.output.outputPanel;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.ui.common.AjaxOutput;

import javax.faces.component.UIPanel;

/**
 * <p>
 * The &lt;r:outputPanel&gt; component is used to group together components in to update them as a whole, rather than having
 * to specify the components individually.
 * <p>
 *
 * @author asmirnov@exadel.com
 */
@JsfComponent(renderer = @JsfRenderer(type = "org.richfaces.ui.OutputPanelRenderer"), tag = @Tag(type = TagType.Facelets), attributes = {
        "events-mouse-props.xml", "events-key-props.xml", "i18n-props.xml", "core-props.xml", "AjaxOutput-props.xml" })
public abstract class AbstractOutputPanel extends UIPanel implements AjaxOutput {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.OutputPanel";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";

    @Attribute(defaultValue = "false")
    public abstract boolean isAjaxRendered();

    @Attribute(defaultValue= "true")
    public abstract boolean isKeepTransient();

    /**
     * HTML layout for generated markup. Possible values: "block" for generating an HTML &lt;div&gt; element and "inline" for
     * generating an HTML &lt;span&gt; element.
     *
     * Default value is "inline"
     */
    @Attribute
    public abstract OutputPanelLayout getLayout();

    @Attribute(events = @EventName("click"))
    public abstract String getOnclick();

    @Attribute(events = @EventName("dblclick"))
    public abstract String getOndblclick();

    @Attribute(events = @EventName("keydown"))
    public abstract String getOnkeydown();

    @Attribute(events = @EventName("keypress"))
    public abstract String getOnkeypress();

    @Attribute(events = @EventName("keyup"))
    public abstract String getOnkeyup();

    @Attribute(events = @EventName("mousedown"))
    public abstract String getOnmousedown();

    @Attribute(events = @EventName("mousemove"))
    public abstract String getOnmousemove();

    @Attribute(events = @EventName("mouseout"))
    public abstract String getOnmouseout();

    @Attribute(events = @EventName("mouseover"))
    public abstract String getOnmouseover();

    @Attribute(events = @EventName("mouseup"))
    public abstract String getOnmouseup();

    @Attribute
    public abstract String getStyle();

    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract String getTitle();

    @Attribute
    public abstract String getDir();

    @Attribute
    public abstract String getLang();
}
