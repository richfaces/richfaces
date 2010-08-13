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

import javax.faces.component.UIInput;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

/**
 * @author Anton Belevich 
 *
 */
@JsfComponent(
    type = AbstractInplaceInput.COMPONENT_TYPE,
    family = AbstractInplaceInput.COMPONENT_FAMILY, 
    generate = "org.richfaces.component.UIInplaceInput",
    renderer = @JsfRenderer(type = "org.richfaces.InplaceInputRenderer"),
    tag = @Tag(name="inplaceInput")
)
public abstract class AbstractInplaceInput extends UIInput {
    
    public static final String COMPONENT_TYPE = "org.richfaces.InplaceInput";
    
    public static final String COMPONENT_FAMILY = "org.richfaces.InplaceInput";
    
    @Attribute
    public abstract String getDefaultLabel();
    
    @Attribute(defaultValue="InplaceState.ready")
    public abstract InplaceState getState();
    
    @Attribute(defaultValue="click")
    public abstract String getEditEvent();
    
    @Attribute(defaultValue="false")
    public abstract boolean isShowControls();
    
    @Attribute
    public abstract String getTabIndex();
}
