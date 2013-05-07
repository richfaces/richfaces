/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.model;

/**
 * @author Alexej Kushunin created 19.06.2007
 *
 */
public interface CalendarDataModelItem {
    /**
     * @return true if date is �selectable� on calendar, default implementation return true
     **/
    boolean isEnabled();

    /**
     * @return String that will be appended to style class for that date span. For example it may be �relevant holyday� � that
     *         mean class will be like �rich-cal-day relevant holyday�. Default implementation return empty string.
     * */
    String getStyleClass();

    /**
     * @return any additional payload that must be JSON-serialazable object. May be used in custom date representation on
     *         calendar (inside custom facet).
     */
    Object getData();

    /**
     * @return true if given date has an associated with it tooltip data. Default implementation return false.
     */
    boolean hasToolTip();

    /**
     * @return tool tip data that will be used in �batch� tooltip loading mode.
     **/
    Object getToolTip();

    /**
     * @return day of the month on which data must be shown.
     **/
    int getDay();
}
