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
package org.richfaces.ui.output.panel;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.ui.attribute.CoreProps;

import javax.faces.component.UIComponentBase;

/**
 * <p>The &lt;r:panel&gt; component is a bordered panel with an optional header.</p>
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.PanelRenderer"),
        attributes = {"events-mouse-props.xml", "events-key-props.xml" })
public abstract class AbstractPanel extends UIComponentBase implements CoreProps {
    public static final String COMPONENT_TYPE = "org.richfaces.Panel";
    public static final String COMPONENT_FAMILY = "org.richfaces.Panel";

    /**
     * Space-separated list of CSS style class(es) to be applied to the panel header.
     */
    @Attribute
    public abstract String getHeader();

    /**
     * Space-separated list of CSS style class(es) to be applied to the panel header.
     */
    @Attribute
    public abstract String getHeaderClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the panel header.
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
