/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

package org.richfaces.component;

import java.util.Comparator;

import javax.el.ValueExpression;

import org.richfaces.model.Filter;
import org.richfaces.model.FilterField;
import org.richfaces.model.SortField;

public class UIColumn extends javax.faces.component.UIColumn implements Column {

    protected enum PropertyKeys {
        filter, filterValue, comparator, sortOrder, breakRowBefore
    }
    
    public void setBreakRowBefore(boolean breakRowBefore) {
        getStateHelper().put(PropertyKeys.breakRowBefore, breakRowBefore);
    }

    public boolean getBreakRowBefore() {
        return (Boolean) getStateHelper().eval(PropertyKeys.breakRowBefore, false);
    }
    
    public void setSortOrder(SortOrder sortOrder) {
        getStateHelper().put(PropertyKeys.sortOrder, sortOrder);
    }

    public SortOrder getSortOrder() {
        return (SortOrder) getStateHelper().eval(PropertyKeys.sortOrder, SortOrder.unsorted);
    }

    public void setFilter(Filter<?> filter) {
        getStateHelper().put(PropertyKeys.filter, filter);
    }

    public Filter<?> getFilter() {
        return (Filter<?>) getStateHelper().eval(PropertyKeys.filter);
    }

    public void setFilterValue(Object filterValue) {
        getStateHelper().put(PropertyKeys.filterValue, filterValue);
    }

    public Object getFilterValue() {
        return getStateHelper().eval(PropertyKeys.filterValue);
    }

    public void setComparator(Comparator<?> comparator) {
        getStateHelper().put(PropertyKeys.comparator, comparator);
    }

    public Comparator<?> getComparator() {
        return (Comparator<?>) getStateHelper().eval(PropertyKeys.comparator);
    }

    public FilterField getFilterField() {
        FilterField field = null;
        Filter<?> filter = getFilter();
        ValueExpression filterExpression = getValueExpression("filterExpression");
        if (filter != null || filterExpression != null) {
            field = new FilterField(filterExpression, filter, getFilterValue());
        }
        return field;
    }

    public SortField getSortField() {
        SortField field = null;
        SortOrder sortOrder = getSortOrder();
        if (sortOrder != null && !SortOrder.unsorted.equals(sortOrder)) {
            Comparator<?> comparator = getComparator();
            ValueExpression sortBy = getValueExpression("sortBy");
            if (comparator != null || sortBy != null) {
                field = new SortField(sortBy, comparator, sortOrder);
            }
        }
        return field;
    }

}
