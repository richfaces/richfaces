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
import org.richfaces.component.attribute.AjaxCommandProps;
import org.richfaces.component.attribute.BypassProps;
import org.richfaces.view.facelets.html.AjaxPollHandler;

/**
 * <p>
 * The &lt;a4j:poll&gt; component allows periodical sending of Ajax requests to the server. It is used for repeatedly
 * updating a page at specific time intervals.
 * </p>
 * @author shura
 */
@JsfComponent(tag = @Tag(generate = false, handlerClass = AjaxPollHandler.class, type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.PollRenderer"))
public abstract class AbstractPoll extends BasicActionComponent implements AjaxCommandProps, BypassProps {
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

    /**
     * Specifies the time in milliseconds between requests. The default for this value is 1000 ms (1 second)
     */
    @Attribute
    public abstract int getInterval();

    /**
     * Enables/disables polling to the server.  Using Expression Language (EL), the enabled attribute can point to a
     * bean property to apply a particular attribute value
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isEnabled();

    /**
     * The client-side script method to be called after the timer counts down, but before the Ajax request is initiated
     */
    @Attribute(events = @EventName(value = TIMER, defaultEvent = true))
    public abstract String getOntimer();
}