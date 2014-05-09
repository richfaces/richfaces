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
 * <p> The &lt;rich:editor&gt; component is used for creating a WYSIWYG editor on a page. </p>
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
@JsfComponent(type = AbstractEditor.COMPONENT_TYPE, family = AbstractEditor.COMPONENT_FAMILY, renderer = @JsfRenderer(type = "org.richfaces.EditorRenderer"), facets = @Facet(name = "config", description = @Description("Detailed configuration of editor in JSON format")), tag = @Tag(name = "editor"), attributes = "core-props.xml")
public abstract class AbstractEditor extends UIInput {
    public static final String COMPONENT_TYPE = "org.richfaces.Editor";
    public static final String COMPONENT_FAMILY = "org.richfaces.Editor";

    /**
     * <p>
     * Used to change the configuration of the toolbar's button set. There are two configurations available:
     * basic (default), full (enables all of the features).
     * <p>
     * It is also possible to define a custom toolbar using the CKEditor toolbar configuration in a config facet
     * </p>
     * <p>
     * Default is basic
     * </p>
     */
    @Attribute(defaultValue = "Basic")
    public abstract String getToolbar();

    /**
     * <p>Set the skin of the richfaces editor.  Bundled skins include: richfaces, kama, v2, office2003</p>
     * <p>Default is richfaces</p>
     */
    @Attribute(defaultValue = "richfaces")
    public abstract String getSkin();

    /**
     * Used to switch the editor into a read-only mode.
     */
    @Attribute(defaultValue = "false")
    public abstract boolean isReadonly();

    /**
     * The width of the editor
     */
    @Attribute(defaultValue = EditorRendererBase.DEFAULT_WIDTH)
    public abstract String getWidth();

    /**
     * The hieght of the editor
     */
    @Attribute(defaultValue = EditorRendererBase.DEFAULT_HEIGHT)
    public abstract String getHeight();

    /**
     * Code describing the language used in the generated markup for this component.
     */
    @Attribute
    public abstract String getLang();

    /**
     * The client-side script method to be called once the editor is initialized and ready to be handle user interaction
     */
    @Attribute(events = @EventName("init"))
    public abstract String getOninit();

    /**
     * The client-side script method to be called when the editor loses focus
     */
    @Attribute(events = @EventName("blur"))
    public abstract String getOnblur();

    /**
     * The client-side script method to be called when the editor receives focus
     */
    @Attribute(events = @EventName("focus"))
    public abstract String getOnfocus();

    /**
     * The client-side script method to be called on blur event when editor content has been changed after previous focus
     */
    @Attribute(events = @EventName(value = "change", defaultEvent = true))
    public abstract String getOnchange();

    /**
     * The client-side script method to be called immediately after the editor content has been changed
     */
    @Attribute(events = @EventName("dirty"))
    public abstract String getOndirty();

    /**
     * Detailed configuration of editor in JSON format
     */
    @Attribute
    public abstract UIComponent getConfig();
}
