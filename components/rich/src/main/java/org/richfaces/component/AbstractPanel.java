/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;

/**
 * <p>The &lt;rich:panel&gt; component is a bordered panel with an optional header.</p>
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.PanelRenderer"))
public abstract class AbstractPanel extends UIComponentBase implements CoreProps, EventsKeyProps, EventsMouseProps {
    public static final String COMPONENT_TYPE = "org.richfaces.Panel";
    public static final String COMPONENT_FAMILY = "org.richfaces.Panel";

    /**
     * Provides the text on the panel header.
     */
    @Attribute
    public abstract String getHeader();

    /**
     * Space-separated list of CSS style class(es) to be applied to the panel header.
     */
    @Attribute
    public abstract String getHeaderClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the panel body.
     */
    @Attribute
    public abstract String getBodyClass();

    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
}
