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
 * @author abelevich
 *
 */
@JsfComponent(
        type = AbstractSelect.COMPONENT_TYPE,
        family = AbstractSelect.COMPONENT_FAMILY, 
        generate = "org.richfaces.component.UISelect",
        renderer = @JsfRenderer(type = "org.richfaces.SelectRenderer"),
        tag = @Tag(name="select")
)
public abstract class AbstractSelect extends UISelectOne {
    
    public static final String COMPONENT_TYPE = "org.richfaces.Select";
    
    public static final String COMPONENT_FAMILY = "org.richfaces.Select";
    
    @Attribute(defaultValue="250px")
    public abstract String getListWidth();
    
    @Attribute(defaultValue="100px")
    public abstract String getListHeight();
    
    @Attribute(defaultValue="true")
    public abstract boolean isShowButton();
    
    @Attribute
    public abstract String getDefaultLabel();
    
    @Attribute(events=@EventName("blur"))
    public abstract String getOnblur();

    @Attribute(events=@EventName("click"))
    public abstract String getOnclick();
    
    @Attribute(events=@EventName("dblclick"))
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
    
    @Attribute(events=@EventName("listclick"))
    public abstract String getOnlistclick();

    @Attribute(events=@EventName("listdblclick"))
    public abstract String getOnlistdblclick();
    
    @Attribute(events=@EventName("listmousedown"))
    public abstract String getOnlistmousedown();
    
    @Attribute(events=@EventName("listmouseup"))
    public abstract String getOnlistmouseup();
    
    @Attribute(events=@EventName("listmouseover"))
    public abstract String getOnlistmouseover();
    
    @Attribute(events=@EventName("listmousemove"))
    public abstract String getOnlistmousemove();
    
    @Attribute(events=@EventName("listmouseout"))
    public abstract String getOnlistmouseout();
    
    @Attribute(events=@EventName("listkeypress"))
    public abstract String getOnlistkeypress();

    @Attribute(events=@EventName("listkeydown"))
    public abstract String getOnlistkeydown();

    @Attribute(events=@EventName("listkeyup"))
    public abstract String getOnlistkeyup();
    
    @Attribute(events=@EventName("select"))
    public abstract String getOnselect();
    
    @Attribute(events=@EventName("change"))
    public abstract String getOnchange();
    //TODO: add list event attributes
    
    @Attribute(defaultValue = "rf-sel-opt")
    public abstract String getItemCss();
    
    //TODO: rename css class "rf-sel-opt-sel"
    @Attribute(defaultValue = "rf-sel-sel")
    public abstract String getSelectItemCss();
    
    @Attribute(defaultValue = "rf-sel-lst-cord")
    public abstract String getListCss();

}
