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

package org.richfaces.demo.tables;

import java.io.Serializable;
import java.util.Comparator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.demo.tables.model.capitals.Capital;
import org.richfaces.model.SortOrder;

@ManagedBean
@ViewScoped
public class CapitalsSortingBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6237417487105926855L;
    private static final String TIMEZONE_GMT_SEPARATOR = "-";
    private SortOrder capitalsOrder = SortOrder.unsorted;
    private SortOrder statesOrder = SortOrder.unsorted;
    private SortOrder timeZonesOrder = SortOrder.unsorted;

    public void sortByCapitals() {
        statesOrder = SortOrder.unsorted;
        timeZonesOrder = SortOrder.unsorted;
        if (capitalsOrder.equals(SortOrder.ascending)) {
            setCapitalsOrder(SortOrder.descending);
        } else {
            setCapitalsOrder(SortOrder.ascending);
        }
    }

    public void sortByStates() {
        capitalsOrder = SortOrder.unsorted;
        timeZonesOrder = SortOrder.unsorted;
        if (statesOrder.equals(SortOrder.ascending)) {
            setStatesOrder(SortOrder.descending);
        } else {
            setStatesOrder(SortOrder.ascending);
        }
    }

    public void sortByTimeZones() {
        statesOrder = SortOrder.unsorted;
        capitalsOrder = SortOrder.unsorted;
        if (timeZonesOrder.equals(SortOrder.ascending)) {
            setTimeZonesOrder(SortOrder.descending);
        } else {
            setTimeZonesOrder(SortOrder.ascending);
        }
    }

    public Comparator<Capital> getTimeZoneComparator() {
        return new Comparator<Capital>() {
            public int compare(Capital o1, Capital o2) {
                int tz1Int = Integer.valueOf(o1.getTimeZone().split(TIMEZONE_GMT_SEPARATOR)[1]);
                int tz2Int = Integer.valueOf(o2.getTimeZone().split(TIMEZONE_GMT_SEPARATOR)[1]);
                if (tz1Int == tz2Int) {
                    return 0;
                }
                if (tz1Int > tz2Int) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };
    }

    public SortOrder getCapitalsOrder() {
        return capitalsOrder;
    }

    public void setCapitalsOrder(SortOrder capitalsOrder) {
        this.capitalsOrder = capitalsOrder;
    }

    public SortOrder getStatesOrder() {
        return statesOrder;
    }

    public void setStatesOrder(SortOrder statesOrder) {
        this.statesOrder = statesOrder;
    }

    public SortOrder getTimeZonesOrder() {
        return timeZonesOrder;
    }

    public void setTimeZonesOrder(SortOrder timeZonesOrder) {
        this.timeZonesOrder = timeZonesOrder;
    }
}
