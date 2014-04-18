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

import java.util.Date;

/**
 * @author Alexej Kushunin created 19.06.2007
 *
 */
public interface CalendarDataModel {
    /**
     * @return array of CalendarDataModelItems for selected dates. This method will be called every time when components will
     *         need next block of CalendarDataItems. That may happens when calendar rendered, or when user navigate to
     *         next(previous) month or in any other case when calendar renders. This method will be called in Ajax mode when
     *         Calendar renders new page.
     * */
    CalendarDataModelItem[] getData(Date[] dateArray);

    /**
     * @return tool tip when it's used in "single" mode This method used when tool tips are displayed in "single" mode
     * */
    Object getToolTip(Date date);
}
