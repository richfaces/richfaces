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

import javax.faces.component.UISelectOne;

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
public abstract class AbstractInplaceSelect extends UISelectOne implements InplaceComponent {
    
    public static final String COMPONENT_TYPE = "org.richfaces.InplaceSelect";
    
    public static final String COMPONENT_FAMILY = "org.richfaces.InplaceSelect";

    @Attribute(defaultValue="250px")
    public abstract String getListWidth();
    
    @Attribute(defaultValue="100px")
    public abstract String getListHeight();

    @Attribute(defaultValue="InplaceState.ready")
    public abstract InplaceState getState();
    
    @Attribute
    public abstract String getDefaultLabel();
    
    @Attribute(defaultValue="true")
    public abstract boolean isSaveOnBlur();

    @Attribute(defaultValue="false")
    public abstract boolean isShowControls(); 
    
    @Attribute(defaultValue="click")
    public abstract String getEditEvent();
    
    @Attribute(events=@EventName("blur"))
    public abstract String getOnblur();

    @Attribute(events=@EventName("click"))
    public abstract String getOnclick();
    
    @Attribute(events=@EventName("ondblclick"))
    public abstract String getOndblclick();
   
    @Attribute(events=@EventName("focus"))
    public abstract String getOnfocus();
    
    @Attribute(events=@EventName("keydown"))
    public abstract String getOnkeydown();
    
    @Attribute(events=@EventName("keypress"))
    public abstract String getOnkeypress();
    
    @Attribute(events=@EventName("keyup"))
    public abstract String getOnkeypup();
    
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
    
    @Attribute(events=@EventName("select"))
    public abstract String getOnselect();
    
    @Attribute(events=@EventName("change"))
    public abstract String getOnchange();

}
