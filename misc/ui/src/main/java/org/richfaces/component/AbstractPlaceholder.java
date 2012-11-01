/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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
package org.richfaces.component;

import javax.faces.component.UIOutput;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.renderkit.PlaceholderRendererBase;

/**
 * Adds placeholder capability to HTML input and textarea elements. A placeholder typically appears as light gray text within an
 * input or textarea element whenever the element is empty and does not have focus. This provides a hint to the user as to what
 * the input or textarea element is used for, or the type of input that is required.
 */
@JsfComponent(tag = @Tag(name = "placeholder", type = TagType.Facelets), renderer = @JsfRenderer(family = AbstractPlaceholder.COMPONENT_FAMILY, type = PlaceholderRendererBase.RENDERER_TYPE), attributes = { "javax.faces.component.ValueHolder.xml" })
public abstract class AbstractPlaceholder extends UIOutput {
    // ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.Placeholder";

    public static final String COMPONENT_TYPE = "org.richfaces.Placeholder";

    @Attribute(required = true)
    public abstract Object getValue();

    /**
     * The jQuery selector used to filter which child DOM elements of the target/parent to which the placeholder will be
     * attached.
     */
    @Attribute
    public abstract String getSelector();

    /**
     * Space-separated list of CSS style class(es) which will be applied to the target input component when placeholder is
     * active.
     */
    @Attribute
    public abstract String getStyleClass();
}
