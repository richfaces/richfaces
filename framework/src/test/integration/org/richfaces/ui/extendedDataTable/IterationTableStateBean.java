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
package org.richfaces.ui.extendedDataTable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.richfaces.model.SortOrder;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@SessionScoped
@Named
public class IterationTableStateBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String[] array = {"3","6","4","8","2","1","5","7","9","0" };
    private List<String> values = Arrays.asList(array);
    private String widthState = "{'columnsWidthState':{'column1':'210px','column2':'75px'}}";
    private String orderState = "{'columnsOrderState':['column2','column1','column3']}";
    private String sortState = "{'columnsSortState':{'column2':'descending'}}";
    private String filterState = "{'columnsFilterState':{'column2':'6'}}";
    private String[] columnsOrder = null; //{"column1", "column2"};

    private SortOrder sortOrder;// = SortOrder.ascending;
    private SortOrder sortOrder2;// = SortOrder.ascending;
    private Long filterValue = 10L;


    public IterationTableStateBean() {
    }

    public List<String> getValues() {
        return values;
    }

    public String getWidthState() {
        return widthState;
    }

    public void setWidthState(String widthState) {
        this.widthState = widthState;
    }

    public String getOrderState() {
        return orderState;
    }

    public String getSortState() {
        return sortState;
    }

    public String getFilterState() {
        return filterState;
    }

    public String[] getColumnsOrder() {
        return columnsOrder;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public SortOrder getSortOrder2() {
        return sortOrder2;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(Long filterValue) {
        this.filterValue = filterValue;
    }

}
