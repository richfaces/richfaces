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
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.model.Filter;
import org.richfaces.model.FilterField;
import org.richfaces.model.SortField;

/**
 * <p> The &lt;rich:column&gt; component facilitates columns in a table. It supports merging columns and rows, sorting,
 * filtering, and customized skinning. </p>
 *
 * @author Anton Belevich
 */
@JsfComponent(type = AbstractColumn.COMPONENT_TYPE, family = AbstractColumn.COMPONENT_FAMILY, facets = {
        @Facet(name = "header", description = @Description("Column header")),
        @Facet(name = "footer", description = @Description("Column footer")) }, generate = "org.richfaces.component.UIColumn", tag = @Tag(name = "column"))
public abstract class AbstractColumn extends javax.faces.component.UIColumn implements Column {
    public static final String COMPONENT_TYPE = "org.richfaces.Column";
    public static final String COMPONENT_FAMILY = "org.richfaces.Column";

    /**
     * if "true" next column begins from the first row
     */
    @Attribute
    public abstract boolean isBreakRowBefore();

    /**
     * Defines order which will be used for sorting column: unsorted (default), ascending, descending
     */
    @Attribute
    public abstract SortOrder getSortOrder();

    /**
     * Provides Filter instance which determines if given row value will be displayed.
     */
    @Attribute
    public abstract Filter<?> getFilter();

    /**
     * Defines current filtering value
     */
    @Attribute
    public abstract Object getFilterValue();

    /**
     * Corresponds to the HTML rowspan attribute
     */
    @Attribute
    public abstract int getRowspan();

    /**
     * Corresponds to the HTML colspan attribute
     */
    @Attribute
    public abstract int getColspan();

    /**
     * Defines value binding to the comparator that is used to compare the values
     */
    @Attribute
    public abstract Comparator<?> getComparator();

    /**
     * Defines EL expression which returns true if given row should be displayed (EL expressions should use variable defined in
     * filterVar attribute of dataTable)
     */
    @Attribute
    public abstract Object getFilterExpression();

    /**
     * Defines a bean property which is used for sorting of a column.
     */
    @Attribute
    public abstract Object getSortBy();

    /**
     * Assigns one or more space-separated CSS class names to any footer generated for this component
     */
    @Attribute
    public abstract String getFooterClass();

    /**
     * Assigns one or more space-separated CSS class names to any header generated for this component
     */
    @Attribute
    public abstract String getHeaderClass();

    /**
     * Attribute defines width of column.
     */
    @Attribute
    public abstract String getWidth();

    /**
     * CSS style rules to be applied to the component
     */
    @Attribute
    public abstract String getStyle();

    /**
     * Assigns one or more CSS class names to the component. Corresponds to the HTML "class" attribute.
     */
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
