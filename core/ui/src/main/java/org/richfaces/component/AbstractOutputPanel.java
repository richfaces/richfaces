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

import javax.faces.component.UIPanel;

import org.ajax4jsf.component.AjaxOutput;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * <p>
 * The &lt;a4j:outputPanel&gt; component is used to group together components in to update them as a whole, rather than having
 * to specify the components individually.
 * <p>
 * @author asmirnov@exadel.com
 */
@JsfComponent(renderer = @JsfRenderer(type = "org.richfaces.OutputPanelRenderer"), tag = @Tag(type = TagType.Facelets),
        attributes = {"events-mouse-props.xml", "events-key-props.xml", "i18n-props.xml", "core-props.xml"})
public abstract class AbstractOutputPanel extends UIPanel implements AjaxOutput {
    public static final String COMPONENT_TYPE = "org.richfaces.OutputPanel";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";

    /**
     * Defines, whether the content of this component must be (or not) included in AJAX response created by parent AJAX
     * Container, even if it is not forced by reRender list of ajax action. Ignored if component marked to output by
     * some Ajax action component. Default value is "false".
     */
    @Attribute
    public abstract boolean isAjaxRendered();

    /**
     * Flag to mark all child components to non-transient. If true, all children components will be set to non-transient
     * state and keep in saved components tree. For output in self-renderer region all content ( By default, all content
     * in &lt;f:verbatim&gt; tags and non-jsf elements in facelets, marked as transient - since, self-rendered ajax
     * regions don't plain output for ajax processing ). Default value is "true"
     */
    @Attribute
    public abstract boolean isKeepTransient();

    /**
     * HTML layout for generated markup. Possible values: "block" for generating an HTML &lt;div&gt; element, "inline"
     * for generating an HTML &lt;span&gt; element, and "none" for generating no HTML element. There is a minor exception
     * for the "none" case where a child element has the property "rendered" set to "false". In this case, we create an
     * empty &lt;span&gt; element with same ID as the child element to use as a placeholder for later processing.
     * <p>
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
