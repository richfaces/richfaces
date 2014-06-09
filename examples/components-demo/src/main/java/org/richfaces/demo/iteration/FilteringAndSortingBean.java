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
package org.richfaces.demo.iteration;

import java.util.Comparator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.demo.iteration.model.Employee;
import org.richfaces.model.Filter;
import org.richfaces.model.SortOrder;

@ManagedBean(name = "filteringAndSortingBean")
@SessionScoped
public class FilteringAndSortingBean {
    private SortOrder nameSortOrder = SortOrder.unsorted;
    private SortOrder emailSortOrder = SortOrder.unsorted;
    private String nameFilterValue;
    private String titleFilterValue;

    public Filter<?> getFilter() {
        return new Filter<Employee>() {
            public boolean accept(Employee employee) {
                if (titleFilterValue != null && employee != null && employee.getTitle() != null) {
                    return employee.getTitle().toUpperCase().startsWith(titleFilterValue.toUpperCase());
                }
                return true;
            }
        };
    }

    public Comparator<?> getComparator() {
        return new Comparator<Employee>() {
            public int compare(Employee o1, Employee o2) {
                if (o1.getName() != null) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
                return 0;
            }
        };
    }

    public void setNameSortOrder(SortOrder nameSortOrder) {
        this.nameSortOrder = nameSortOrder;
    }

    public SortOrder getNameSortOrder() {
        return nameSortOrder;
    }

    public void setEmailSortOrder(SortOrder emailSortOrder) {
        this.emailSortOrder = emailSortOrder;
    }

    public SortOrder getEmailSortOrder() {
        return emailSortOrder;
    }

    public void setTitleFilterValue(String titleFilterValue) {
        this.titleFilterValue = titleFilterValue;
    }

    public String getTitleFilterValue() {
        return titleFilterValue;
    }

    public void setNameFilterValue(String nameFilterValue) {
        this.nameFilterValue = nameFilterValue;
    }

    public String getNameFilterValue() {
        return nameFilterValue;
    }
}
