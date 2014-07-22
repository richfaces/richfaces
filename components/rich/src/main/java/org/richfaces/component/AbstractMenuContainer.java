/**
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
 **/
package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.component.attribute.DisabledProps;
import org.richfaces.component.attribute.EventsPopupsProps;
import org.richfaces.component.attribute.PositionProps;

import javax.faces.component.UIComponentBase;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public abstract class AbstractMenuContainer extends UIComponentBase implements DisabledProps, EventsPopupsProps, PositionProps {

    /**
     * <p>Defines an event on the parent element to display the menu</p>
     * <p>Default value is 'contextmenu'</p>
     */
    @Attribute()
    public abstract String getShowEvent();

    /**
     * <p>
     * Sets the submission mode for all menu items of the menu except those where this attribute redefined.
     * Possible value are 'ajax','client' and 'server'
     * </p>
     * <p>Default value is 'server'</p>
     */
    @Attribute()
    public abstract Mode getMode();

    /**
     * <p>Delay (in ms) between losing focus and menu closing</p>
     * <p>Default value is "300"</p>
     */
    @Attribute
    public abstract int getHideDelay();

    /**
     * <p>Delay (in ms) between observing the showEvent and menu opening</p>
     * <p>Default value is "50"</p>
     */
    @Attribute
    public abstract int getShowDelay();

    /**
     * Sets minimum width for all lists that will appear.
     */
    @Attribute
    public abstract int getPopupWidth();

    /**
     * The client-side script method to be called when a child menuGroup is expanded
     */
    @Attribute(events = @EventName("groupshow"))
    public abstract String getOngroupshow();

    /**
     * The client-side script method to be called when a child menuGroup is hidden
     */
    @Attribute(events = @EventName("grouphide"))
    public abstract String getOngrouphide();

    /**
     * The client-side script method to be called when a menu item is clicked
     */
    @Attribute(events = @EventName("itemclick"))
    public abstract String getOnitemclick();

    @Attribute(generate = false, hidden = true, readOnly = true)
    public abstract Object getCssRoot();
}

