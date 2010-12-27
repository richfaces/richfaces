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
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

/**
 * @author Anton Belevich
 *
 */

@JsfComponent(
    type = AbstractCollapsibleSubTableToggler.COMPONENT_TYPE,
    family = AbstractCollapsibleSubTableToggler.COMPONENT_FAMILY, 
    generate = "org.richfaces.component.UICollapsibleSubTableToggleControl",
    renderer = @JsfRenderer(type = "org.richfaces.CollapsibleSubTableTogglerRenderer"),
    tag = @Tag( name = "collapsibleSubTableToggler")
 )
public abstract class AbstractCollapsibleSubTableToggler extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.CollapsibleSubTableToggler";

    public static final String COMPONENT_FAMILY = "org.richfaces.CollapsibleSubTableToggler";
    
    public static final String DEFAULT_EVENT = "onclick"; 
    
    @Attribute
    public abstract String getExpandLabel();
    
    @Attribute
    public abstract String getCollapseLabel();

    @Attribute
    public abstract String getExpandIcon();

    @Attribute
    public abstract String getCollapseIcon();
    
    @Attribute(defaultValue = DEFAULT_EVENT)
    public abstract String getEvent();
    
    @Attribute
    public String getFor() {
        return (String)getStateHelper().eval("for");
    }
    
    public void setFor(String forId) {
        getStateHelper().put("for", forId);
    }
}
