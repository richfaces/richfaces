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
package org.richfaces.ui.drag.dragIndicator;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

/**
 * <p>
 *     The &lt;r:dragIndicator&gt; component defines a graphical element to display under the mouse cursor during a
 *     drag-and-drop operation.
 * </p>
 * @author abelevich
 */
@JsfComponent(type = AbstractDragIndicator.COMPONENT_TYPE, family = AbstractDragIndicator.COMPONENT_FAMILY, renderer = @JsfRenderer(type = "org.richfaces.ui.DragIndicatorRenderer"), tag = @Tag(name = "dragIndicator"))
public abstract class AbstractDragIndicator extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.DragIndicator";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.DragIndicator";

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * Assigns one or more space-separated CSS class names to the indicator which are applied when a drop is accepted
     */
    @Attribute
    public abstract String getAcceptClass();

    /**
     * Assigns one or more space-separated CSS class names to the indicator which are applied when a drop is rejected
     */
    @Attribute
    public abstract String getRejectClass();

    /**
     * Assigns one or more space-separated CSS class names to the component. Corresponds to the HTML "class" attribute.
     */
    @Attribute
    public abstract String getDraggingClass();
}
