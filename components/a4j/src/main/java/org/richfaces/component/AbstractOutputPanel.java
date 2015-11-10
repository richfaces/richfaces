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
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.AjaxOutputProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.I18nProps;

/**
 * <p>
 * The &lt;a4j:outputPanel&gt; component is used to group together components in to update them as a whole, rather than having
 * to specify the components individually.
 * <p>
 *
 * @author asmirnov@exadel.com
 */
@JsfComponent(renderer = @JsfRenderer(type = "org.richfaces.OutputPanelRenderer"), tag = @Tag(type = TagType.Facelets))
public abstract class AbstractOutputPanel extends UIPanel implements AjaxOutput, AjaxOutputProps, CoreProps, EventsKeyProps, EventsMouseProps, I18nProps {
    public static final String COMPONENT_TYPE = "org.richfaces.OutputPanel";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";

    @Attribute(description = @Description("Defines, whether the content of this component must be (or not) included in AJAX response created by parent AJAX Container, even if it is not forced by reRender list of ajax action. Ignored if component marked to output by some Ajax action component. Default value - \"false\""), defaultValue = "false")
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
}
