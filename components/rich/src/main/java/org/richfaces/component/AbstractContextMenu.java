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
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.component.attribute.PositionProps;
import org.richfaces.renderkit.html.ContextMenuRendererBase;

import javax.faces.component.UIComponentBase;

/**
 * <p>
 *     The &lt;rich:contextMenu&gt; component is used for creating a hierarchical context menu that are activated on
 *     events like onmouseover, onclick etc. The component can be applied to any element on the page.
 * </p>
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(family = AbstractContextMenu.COMPONENT_FAMILY, type = AbstractContextMenu.COMPONENT_TYPE,
        renderer = @JsfRenderer(type = ContextMenuRendererBase.RENDERER_TYPE), tag = @Tag(name = "contextMenu"))
public abstract class AbstractContextMenu extends AbstractMenuContainer implements CoreProps, EventsKeyProps, EventsMouseProps, I18nProps, PositionProps {
    public static final String COMPONENT_TYPE = "org.richfaces.ContextMenu";
    public static final String COMPONENT_FAMILY = "org.richfaces.ContextMenu";

    /**
     * If the value of the 'attached' attribute is true, the component is attached to the component, specified in the
     * 'target' attribute or to the parent component, if 'target' is not defined. Default value is 'true'.
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isAttached();

    /**
     * Client identifier of the component or id of the existing DOM element that is a source for a given event.
     * If target is defined, the event is attached on the client. If both attached and target attributes are
     * defined, and attribute attached has value 'false', it is considered to have higher priority.
     */
    @Attribute
    public abstract String getTarget();

    /**
     * The jQuery selector used to filter which child DOM elements of the target/parent to which the contextMenu will
     * be attached.
     */
    @Attribute
    public abstract String getTargetSelector();

    /**
     * If "true" the menu will stay visible when the mouse is moved away from it. Default value: "false".
     */
    @Attribute(defaultValue = "false")
    public abstract boolean isSticky();

    public Object getCssRoot() {
        return "ctx";
    }

    @Attribute(hidden = true)
    public abstract Positioning getJointPoint();
}