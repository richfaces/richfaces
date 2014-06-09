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
package org.richfaces.demo.input;

import java.util.Date;
import java.util.Random;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.richfaces.model.CalendarDataModel;
import org.richfaces.model.CalendarDataModelItem;

/**
 * @author Nick Belaevski - mailto:nbelaevski@exadel.com created 30.06.2007
 *
 */
@ManagedBean(name = "calendarDataModel")
@ApplicationScoped
public class CalendarDataModelImpl implements CalendarDataModel {
    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.CalendarDataModel#getData(java.util.Date[])
     */
    public CalendarDataModelItem[] getData(Date[] dateArray) {
        if (dateArray == null) {
            return null;
        }

        CalendarDataModelItem[] items = new CalendarDataModelItem[dateArray.length];
        for (int i = 0; i < dateArray.length; i++) {
            items[i] = createDataModelItem(dateArray[i]);
        }

        return items;
    }

    protected CalendarDataModelItem createDataModelItem(Date date) {
        CalendarDataModelItemImpl item = new CalendarDataModelItemImpl();

        if (new Random().nextInt(10) > 5) {
            item.setEnabled(true);
        } else {
            item.setEnabled(false);
        }

        return item;
    }

    public Object getToolTip(Date date) {
        return null;
    }
}
