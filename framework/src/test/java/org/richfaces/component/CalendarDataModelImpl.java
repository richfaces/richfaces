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

import java.util.Date;

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

        item.setEnabled(false);

        return item;
    }

    public Object getToolTip(Date date) {
        return null;
    }
}
