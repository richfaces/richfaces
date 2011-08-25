/*
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
 */
package org.richfaces.component;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.EditorRendererBase;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
@JsfComponent(type = AbstractEditor.COMPONENT_TYPE, family = AbstractEditor.COMPONENT_FAMILY, generate = "org.richfaces.component.UIEditor", renderer = @JsfRenderer(type = "org.richfaces.EditorRenderer"), tag = @Tag(name = "editor"), attributes = "core-props.xml")
public abstract class AbstractEditor extends UIInput {
    public static final String COMPONENT_TYPE = "org.richfaces.Editor";
    public static final String COMPONENT_FAMILY = "org.richfaces.Editor";

    @Attribute(defaultValue = "Basic")
    public abstract String getToolbar();

    @Attribute(defaultValue = "false")
    public abstract boolean isReadonly();

    @Attribute(defaultValue = EditorRendererBase.DEFAULT_WIDTH)
    public abstract String getWidth();

    @Attribute(defaultValue = EditorRendererBase.DEFAULT_HEIGHT)
    public abstract String getHeight();

    @Attribute(events = @EventName("init"))
    public abstract String getOninit();

    @Attribute(events = @EventName("blur"))
    public abstract String getOnblur();

    @Attribute(events = @EventName("focus"))
    public abstract String getOnfocus();

    @Attribute(events = @EventName("change"))
    public abstract String getOnchange();

    @Facet(description = @Description("Detailed configuration of editor in JSON format"))
    public abstract UIComponent getConfig();
}
