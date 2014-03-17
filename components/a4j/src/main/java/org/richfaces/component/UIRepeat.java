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

import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * <p>
 * The non-visual &lt;a4j:repeat&gt; component is used to iterate through a data model. The component renders child content for
 * every iteration according to the current object data.
 * </p>
 *
 * @author Nick Belaevski
 */
@JsfComponent(tag = @Tag(name = "repeat", handler = "org.richfaces.view.facelets.html.RepeatHandler", type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.RepeatRenderer"), attributes = {
        "iteration-props.xml", "rows-prop.xml", "sequence-props.xml" })
public class UIRepeat extends UISequence {
    public static final String COMPONENT_TYPE = "org.richfaces.Repeat";
    public static final String COMPONENT_FAMILY = "javax.faces.Data";

    public UIRepeat() {
        setRendererType("org.richfaces.RepeatRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
}
