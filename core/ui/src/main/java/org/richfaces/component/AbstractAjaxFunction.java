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

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.2 $ $Date: 2007/01/23 20:01:04 $
 *
 */
@JsfComponent(
    renderer = @JsfRenderer(type = "org.richfaces.FunctionRenderer"),
    tag = @Tag(name = "jsFunction", type = TagType.Facelets)
)
public abstract class AbstractAjaxFunction extends AbstractActionComponent {

    public static final String COMPONENT_FAMILY = "javax.faces.Command";

    public static final String COMPONENT_TYPE = "org.richfaces.Function";

    @Attribute
    public abstract boolean isLimitRender();

    @Attribute(required = true)
    public abstract String getName();

    @Attribute
    public abstract Object getExecute();

    @Attribute
    public abstract Object getRender();

    @Attribute
    public abstract String getStatus();

    @Attribute(events = @EventName("begin"))
    public abstract String getOnbegin();

    @Attribute(events = @EventName("beforedomupdate"))
    public abstract String getOnbeforedomupdate();

    @Attribute(events = @EventName("complete"))
    public abstract String getOncomplete();
}
