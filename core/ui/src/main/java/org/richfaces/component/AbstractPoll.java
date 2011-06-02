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
 * Component for periodically call AJAX events on server ( poll actions )
 *
 * @author shura
 *
 */
@JsfComponent(tag = @Tag(generate = false, handler = "org.richfaces.view.facelets.html.AjaxPollHandler", type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.PollRenderer"), attributes = { "ajax-props.xml" })
public abstract class AbstractPoll extends AbstractActionComponent {
    public static final String COMPONENT_TYPE = "org.richfaces.Poll";
    public static final String COMPONENT_FAMILY = "org.richfaces.Poll";
    public static final String TIMER = "timer";
    public static final String ON_TIMER = "ontimer";
    public static final String BEGIN = "begin";
    public static final String ON_BEGIN = "onbegin";
    public static final String COMPLETE = "complete";
    public static final String ON_COMPLETE = "oncomplete";
    public static final String BEFOREDOMUPDATE = "beforedomupdate";
    public static final String ON_BEFOREDOMUPDATE = "onbeforedomupdate";

    @Attribute
    public abstract int getInterval();

    @Attribute(defaultValue = "true")
    public abstract boolean isEnabled();

    @Attribute(events = @EventName(value = TIMER, defaultEvent = true))
    public abstract String getOntimer();
}