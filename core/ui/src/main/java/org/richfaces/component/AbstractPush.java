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

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * @author Nick Belaevski
 *
 */
@JsfComponent(type = AbstractPush.COMPONENT_TYPE, family = AbstractPush.COMPONENT_FAMILY, tag = @Tag(name = "push", type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.PushRenderer"))
public abstract class AbstractPush extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.richfaces.Push";
    public static final String COMPONENT_FAMILY = "org.richfaces.Push";

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Attribute(required = true)
    public abstract String getAddress();

    @Attribute(events = { @EventName("dataavailable") })
    public abstract String getOndataavailable();

    @Attribute(events = { @EventName("error") })
    public abstract String getOnerror();
}
