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
import javax.faces.context.FacesContext;

import org.richfaces.application.configuration.ConfigurationServiceHelper;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.ColumnProps;
import org.richfaces.component.attribute.StyleClassProps;
import org.richfaces.component.attribute.StyleProps;
import org.richfaces.application.CoreConfiguration;
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
        @Facet(name = "footer", description = @Description("Column footer")) }, tag = @Tag(name = "column"))
public abstract class AbstractColumn extends javax.faces.component.UIColumn implements Column, ColumnProps, StyleClassProps, StyleProps {
    public static final String COMPONENT_TYPE = "org.richfaces.Column";
    public static final String COMPONENT_FAMILY = "org.richfaces.Column";
    private static Boolean builtInSortControlsEnabled;
    private static Boolean builtInFilterControlsEnabled;

    /**
     * If "true" next column begins from the first row.
     */
    @Attribute
    public abstract boolean isBreakRowBefore();

    /**
     * Defines order which will be used for sorting column: unsorted (default), ascending, descending.
     */
    @Attribute
    public abstract SortOrder getSortOrder();

    /**
     * Provides Filter instance which determines if given row value will be displayed.
     */
    @Attribute
    public abstract Filter<?> getFilter();

    /**
     * Defines current filtering value.
     */
    @Attribute
    public abstract Object getFilterValue();

    /**
     * Defines current filter type. Possible values: string, custom.
     * If custom is used, no filter box is created, you are responsible for creating your own filter input.<br/>
     * Default: string
     */
    @Attribute(defaultValue = "string")
    public abstract String getFilterType();

    /**
     * The message to be displayed when the filter expression is not able to be evaluated using the provided filter value.
     */
    @Attribute
    public abstract String getFilterConverterMessage();

    /**
     * The submitted filter value.  Set when the evaluation of the filter expression fails.
     */
    @Attribute(hidden = true)
    public abstract String getSubmittedFilterValue();

    /**
     * Defines current sorting type. Possible values: string, custom.
     * If custom is used, no sorting controls are created, you are responsible for creating your own.<br/>
     * Default: string
     */
    @Attribute(defaultValue = "string")
    public abstract String getSortType();

    /**
     * Corresponds to the HTML rowspan attribute.
     */
    @Attribute
    public abstract int getRowspan();

    /**
     * Corresponds to the HTML colspan attribute.
     */
    @Attribute
    public abstract int getColspan();

    /**
     * Defines value binding to the comparator that is used to compare the values.
     */
    @Attribute
    public abstract Comparator<?> getComparator();

    /**
     * Defines EL expression which returns true if given row should be displayed (EL expressions should use variable defined in
     * filterVar attribute of dataTable).
     */
    @Attribute
    public abstract Object getFilterExpression();

    /**
     * Defines a bean property which is used for sorting of a column.
     */
    @Attribute
    public abstract Object getSortBy();

    /**
     * Attribute defines width of column.
     */
    @Attribute
    public abstract String getWidth();

    /**
     * Allows customizing column name in column visibility control in rich:extendedDataTable (showColumnControl)
     */
    @Attribute
    public abstract String getName();

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

    public boolean useBuiltInFilter() {
        return isBuiltInFilterControlsEnabled() && getFilterField() != null &&  ! "custom".equals(getFilterType());
    }

    public boolean useBuiltInSort() {
        return isBuiltInSortControlsEnabled() && getValueExpression("sortBy") != null && ! "custom".equals(getSortType());
    }

    public static boolean isBuiltInSortControlsEnabled(){
        if(builtInSortControlsEnabled== null){
            FacesContext context = FacesContext.getCurrentInstance();
            builtInSortControlsEnabled =  ConfigurationServiceHelper.getBooleanConfigurationValue(context, CoreConfiguration.Items.builtInSortControlsEnabled);
        }
        return Boolean.TRUE.equals(builtInSortControlsEnabled);
    }

    public static boolean isBuiltInFilterControlsEnabled(){
        if(builtInFilterControlsEnabled== null){
            FacesContext context = FacesContext.getCurrentInstance();
            builtInFilterControlsEnabled =  ConfigurationServiceHelper.getBooleanConfigurationValue(context, CoreConfiguration.Items.builtInFilterControlsEnabled);
        }
        return Boolean.TRUE.equals(builtInFilterControlsEnabled);
    }


}
