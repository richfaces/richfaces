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

import javax.faces.component.UISelectMany;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 *
 */
public abstract class AbstractSelectManyComponent extends UISelectMany {
    @Attribute()
    public abstract String getListWidth();

    @Attribute()
    public abstract String getListHeight();

    @Attribute
    public abstract String getDefaultLabel();

    @Attribute
    public abstract String getItemClass();

    @Attribute
    public abstract String getSelectItemClass();

    @Attribute
    public abstract String getActiveClass();

    @Attribute
    public abstract String getChangedClass();

    @Attribute
    public abstract String getDisabledClass();

    @Attribute
    public abstract String getEscape();

    @Attribute
    public abstract String getListClass();

    @Attribute(events = @EventName("blur"))
    public abstract String getOnblur();

    @Attribute(events = @EventName("click"))
    public abstract String getOnclick();

    @Attribute(events = @EventName(value = "change", defaultEvent = true))
    public abstract String getOnchange();

    @Attribute(events = @EventName("dblclick"))
    public abstract String getOndblclick();

    @Attribute(events = @EventName("focus"))
    public abstract String getOnfocus();

    @Attribute(events = @EventName("keydown"))
    public abstract String getOnkeydown();

    @Attribute(events = @EventName("keypress"))
    public abstract String getOnkeypress();

    @Attribute(events = @EventName("keyup"))
    public abstract String getOnkeyup();

    @Attribute(events = @EventName("mousedown"))
    public abstract String getOnmousedown();

    @Attribute(events = @EventName("mousemove"))
    public abstract String getOnmousemove();

    @Attribute(events = @EventName("mouseout"))
    public abstract String getOnmouseout();

    @Attribute(events = @EventName("mouseover"))
    public abstract String getOnmouseover();

    @Attribute(events = @EventName("mouseup"))
    public abstract String getOnmouseup();

    @Attribute(events = @EventName("listclick"))
    public abstract String getOnlistclick();

    @Attribute(events = @EventName("listdblclick"))
    public abstract String getOnlistdblclick();

    @Attribute(events = @EventName("listshow"))
    public abstract String getOnlistshow();

    @Attribute(events = @EventName("listhide"))
    public abstract String getOnlisthide();

    @Attribute(events = @EventName("listmousedown"))
    public abstract String getOnlistmousedown();

    @Attribute(events = @EventName("listmouseup"))
    public abstract String getOnlistmouseup();

    @Attribute(events = @EventName("listmouseover"))
    public abstract String getOnlistmouseover();

    @Attribute(events = @EventName("listmousemove"))
    public abstract String getOnlistmousemove();

    @Attribute(events = @EventName("listmouseout"))
    public abstract String getOnlistmouseout();

    @Attribute(events = @EventName("listkeypress"))
    public abstract String getOnlistkeypress();

    @Attribute(events = @EventName("listkeydown"))
    public abstract String getOnlistkeydown();

    @Attribute(events = @EventName("listkeyup"))
    public abstract String getOnlistkeyup();

    @Attribute(events = @EventName("selectitem"))
    public abstract String getOnselectitem();
}
