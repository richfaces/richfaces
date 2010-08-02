/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.richfaces.component;

import javax.faces.component.UIOutput;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;

/**
 * @author Nick Belaevski
 *
 */
@JsfComponent(renderer = @JsfRenderer(type = "org.richfaces.StatusRenderer"))
public abstract class AbstractAjaxStatus extends UIOutput {

    public static final String COMPONENT_TYPE = "org.richfaces.Status";

    public static final String COMPONENT_FAMILY = "org.richfaces.Status";

    @Attribute(events = @EventName("start"))
    public abstract String getOnstart();

    @Attribute(events = @EventName("stop"))
    public abstract String getOnstop();

    @Attribute(events = @EventName("error"))
    public abstract String getOnerror();

    @Attribute(events = @EventName("success"))
    public abstract String getOnsuccess();
}
