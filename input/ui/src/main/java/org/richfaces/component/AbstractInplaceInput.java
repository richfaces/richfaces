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
import org.richfaces.cdk.annotations.EventName;
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
public abstract class AbstractInplaceInput extends UIInput implements InplaceComponent {
    
    public static final String COMPONENT_TYPE = "org.richfaces.InplaceInput";
    
    public static final String COMPONENT_FAMILY = "org.richfaces.InplaceInput";

    @Attribute
    public abstract boolean isDisabled();
   
    @Attribute
    public abstract String getDefaultLabel();
    
    @Attribute(defaultValue="true")
    public abstract boolean isSaveOnBlur();
    
    @Attribute
    public abstract InplaceState getState();
    
    @Attribute
    public abstract String getEditEvent();
    
    @Attribute
    public abstract boolean isShowControls();
    
    @Attribute
    public abstract String getInputWidth();
    
    @Attribute
    public abstract int getTabindex();
    
    @Attribute
    public abstract String getReadyStateClass();

    @Attribute
    public abstract String getEditStateClass();

    @Attribute
    public abstract String getChangedStateClass();

    @Attribute
    public abstract String getDisabledStateClass();
   
    @Attribute
    public abstract String getEditClass();
    
    @Attribute
    public abstract String getNoneClass();
    
    @Attribute(events=@EventName("click"))
    public abstract String getOnclick();
    
    @Attribute(events=@EventName("ondblclick"))
    public abstract String getOndblclick();
   
    @Attribute(events=@EventName("keydown"))
    public abstract String getOnkeydown();
    
    @Attribute(events=@EventName("keypress"))
    public abstract String getOnkeypress();
    
    @Attribute(events=@EventName("keyup"))
    public abstract String getOnkeyup();
    
    @Attribute(events=@EventName("mousedown"))
    public abstract String getOnmousedown();
    
    @Attribute(events=@EventName("mousemove"))
    public abstract String getOnmousemove();
    
    @Attribute(events=@EventName("mouseout"))
    public abstract String getOnmouseout();
    
    @Attribute(events=@EventName("mouseover"))
    public abstract String getOnmouseover();
    
    @Attribute(events=@EventName("mouseup"))
    public abstract String getOnmouseup();

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
    
    @Attribute(events=@EventName("change"))
    public abstract String getOnchange();
    
    @Attribute(events=@EventName("focus"))
    public abstract String getOnfocus();
  
    @Attribute(events=@EventName("blur"))
    public abstract String getOnblur();

    
    //TODO: what is default event?, add onViewActivated, onEditActivated events support
}
