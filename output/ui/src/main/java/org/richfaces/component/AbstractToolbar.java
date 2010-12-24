/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
import org.richfaces.renderkit.html.ToolbarRendererBase;

@JsfComponent(family = AbstractToolbar.COMPONENT_FAMILY, type = AbstractToolbar.COMPONENT_TYPE, 
        renderer=@JsfRenderer(type = ToolbarRendererBase.RENDERER_TYPE), tag = @Tag(name="toolbar"),
        attributes = {"core-props.xml"}
)
public abstract class AbstractToolbar extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.Toolbar";

    public static final String COMPONENT_FAMILY = "org.richfaces.Toolbar";
    
    @Attribute
    public abstract String getHeight();
    
    @Attribute(defaultValue = "100%")
    public abstract String getWidth();
    
    @Attribute
    public abstract String getItemClass();
    
    @Attribute
    public abstract String getItemStyle();
        
    @Attribute
    public abstract String getItemSeparator();
    
    @Attribute(events=@EventName("itemclick"))
    public abstract String getOnitemclick();

    @Attribute(events=@EventName("itemdblclick"))
    public abstract String getOnitemdblclick();
    
    @Attribute(events=@EventName("itemmousedown"))
    public abstract String getOnitemmousedown();
    
    @Attribute(events=@EventName("itemmouseup"))
    public abstract String getOnitemmouseup();
    
    @Attribute(events=@EventName("itemmouseover"))
    public abstract String getOnitemmouseover();
    
    @Attribute(events=@EventName("itemmousemove"))
    public abstract String getOnitemmousemove();
    
    @Attribute(events=@EventName("itemmouseout"))
    public abstract String getOnitemmouseout();
    
    @Attribute(events=@EventName("itemkeypress"))
    public abstract String getOnitemkeypress();

    @Attribute(events=@EventName("itemkeydown"))
    public abstract String getOnitemkeydown();

    @Attribute(events=@EventName("itemkeyup"))
    public abstract String getOnitemkeyup();
    
}
