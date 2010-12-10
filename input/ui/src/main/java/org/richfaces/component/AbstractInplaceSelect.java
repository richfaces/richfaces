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
import org.richfaces.cdk.annotations.EventName;
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
public abstract class AbstractInplaceSelect extends AbstractSelectComponent implements InplaceComponent {
    
    public static final String COMPONENT_TYPE = "org.richfaces.InplaceSelect";
    
    public static final String COMPONENT_FAMILY = "org.richfaces.Select";
    
    @Attribute(defaultValue="InplaceState.ready")
    public abstract InplaceState getState();
    
    @Attribute(defaultValue="true")
    public abstract boolean isOpenOnEdit();
    
    @Attribute(defaultValue="true")
    public abstract boolean isSaveOnSelect();

    @Attribute(defaultValue="true")
    public abstract boolean isSaveOnBlur();

    @Attribute(defaultValue="false")
    public abstract boolean isShowControls();
    
    @Attribute
    public abstract int getTabindex();
    
    @Override
    @Attribute
    public abstract String getItemClass();
    
    @Override
    @Attribute
    public abstract String getSelectItemClass();
    
    @Override
    @Attribute
    public abstract String getListClass();
    
    @Attribute(defaultValue="click")
    public abstract String getEditEvent();

    @Attribute(events=@EventName("inputclick"))
    public abstract String getOninputclick();
    
    @Attribute(events=@EventName("inputdblclick"))
    public abstract String getOninputdblclick();
    
    @Attribute(events=@EventName("inputmousedown"))
    public abstract String getOninputmousedown();
    
    @Attribute(events=@EventName("inputmousemove"))
    public abstract String getOninputmousemove();
    
    @Attribute(events=@EventName("inputmouseout"))
    public abstract String getOninputmouseout();
    
    @Attribute(events=@EventName("inputmouseover"))
    public abstract String getOninputmouseover();
    
    @Attribute(events=@EventName("inputmouseup"))
    public abstract String getOninputmouseup();
    
    @Attribute(events=@EventName("inputkeydown"))
    public abstract String getOninputkeydown();
    
    @Attribute(events=@EventName("inputkeypress"))
    public abstract String getOninputkeypress();
    
    @Attribute(events=@EventName("inputkeyup"))
    public abstract String getOninputkeyup();
    
    @Attribute(events=@EventName("inputselect"))
    public abstract String getOninputselect();
}
