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

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;


/**
 * @author Anton Belevich
 *
 */

@JsfComponent(
    type = AbstractInplaceSelect.COMPONENT_TYPE,
    family = AbstractInplaceSelect.COMPONENT_FAMILY, 
    generate = "org.richfaces.component.UIInplaceSelect",
    renderer = @JsfRenderer(type = "org.richfaces.InplaceSelectRenderer"),
    tag = @Tag(name="inplaceSelect")
)
public abstract class AbstractInplaceSelect extends AbstractSelect implements InplaceComponent {
    
    public static final String COMPONENT_TYPE = "org.richfaces.InplaceSelect";
    
    public static final String COMPONENT_FAMILY = "org.richfaces.Select";

    @Attribute(defaultValue="InplaceState.ready")
    public abstract InplaceState getState();
    
    @Attribute(defaultValue="false")
    public abstract boolean isOpenOnEdit();

    @Attribute(defaultValue="true")
    public abstract boolean isSaveOnBlur();

    @Attribute(defaultValue="false")
    public abstract boolean isShowControls(); 
    
    @Attribute(defaultValue="click")
    public abstract String getEditEvent();

    @Override
    @Attribute(defaultValue="rf-is-opt")
    public abstract String getItemCss();
    
    @Override
    @Attribute(defaultValue="rf-is-sel")
    public abstract String getSelectItemCss();
    
    @Override
    @Attribute(defaultValue="rf-is-lst-cord")
    public abstract String getListCss();
    
}
