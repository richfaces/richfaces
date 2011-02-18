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

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.model.Filter;
import org.richfaces.model.FilterField;
import org.richfaces.model.SortField;

/**
 * @author Anton Belevich
 *
 */

@JsfComponent(
    type = AbstractColumn.COMPONENT_TYPE,
    family = AbstractColumn.COMPONENT_FAMILY, 
    generate = "org.richfaces.component.UIColumn",
    tag = @Tag(name="column")
)
public abstract class AbstractColumn extends javax.faces.component.UIColumn implements Column {

    public static final String COMPONENT_TYPE = "org.richfaces.Column";
    
    public static final String COMPONENT_FAMILY = "org.richfaces.Column";
    
    @Attribute
    public abstract boolean isBreakRowBefore();
    
    @Attribute
    public abstract SortOrder getSortOrder();

    @Attribute
    public abstract Filter<?> getFilter();
    
    @Attribute
    public abstract Object getFilterValue();
    
    @Attribute
    public abstract int getRowspan();

    @Attribute
    public abstract int getColspan();

    @Attribute
    public abstract Comparator<?> getComparator();

    @Attribute
    public abstract Boolean isFilterExpression();

    @Attribute
    public abstract Object getSortBy();

    @Attribute
    public abstract String getFooterClass();

    @Attribute
    public abstract String getHeaderClass();

    @Attribute
    public abstract String getWidth();
    
    @Attribute
    public abstract String getStyle();
    
    @Attribute
    public abstract String getStyleClass();

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
