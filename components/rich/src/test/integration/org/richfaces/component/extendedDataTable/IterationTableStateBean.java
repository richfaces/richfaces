/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
 **/
package org.richfaces.component.extendedDataTable;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIColumn;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.richfaces.component.AbstractExtendedDataTable;
import org.richfaces.component.ExtendedDataTableState;
import org.richfaces.component.SortOrder;
import org.richfaces.json.JSONException;

import com.google.common.collect.Lists;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@SessionScoped
@Named
public class IterationTableStateBean implements Serializable {

    public static final String NOT_RUN = "did not run";
    public static final String PASSED = "passed";

    private static final long serialVersionUID = 1L;

    private final String[] array = { "3", "6", "4", "8", "2", "1", "5", "7", "9", "0" };
    private String checkAscendingSortOrderResult = NOT_RUN;
    private String checkColumnFilterEquals3Result = NOT_RUN;
    private String checkColumnOrderAfterDndResult = NOT_RUN;
    private String checkWidthResizedAfterDnDResult = NOT_RUN;
    private final String[] columnsOrder = null; //{"column1", "column2"};
    private final String filterState = "{'columnsFilterState':{'column2':'6'}}";
    private Long filterValue = 10L;
    private final String orderState = "{'columnsOrderState':['column2','column1','column3']}";

    private SortOrder sortOrder;// = SortOrder.ascending;
    private SortOrder sortOrder2;// = SortOrder.ascending;
    private final String sortState = "{'columnsSortState':{'column2':'descending'}}";
    private final List<String> values = Arrays.asList(array);
    private String widthState = "{'columnsWidthState':{'column1':'210px','column2':'75px'}}";

    public IterationTableStateBean() {
    }

    public void checkAscendingSortOrder() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractExtendedDataTable edtComponent = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("myForm").findComponent("edt");
        ExtendedDataTableState tableState = new ExtendedDataTableState(edtComponent.getTableState());
        UIColumn column = new UIColumn();
        column.setId("column2");

        String order = tableState.getColumnSort(column);
        String expectedOrder = "ascending";
        if (!expectedOrder.equals(order)) {
            checkAscendingSortOrderResult = MessageFormat.format("Expected order <{0}>, but have <{1}>.", expectedOrder, order);
            return;
        }
        checkAscendingSortOrderResult = PASSED;
    }

    public void checkColumnFilterEquals3() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractExtendedDataTable edtComponent = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("myForm").findComponent("edt");
        ExtendedDataTableState tableState = new ExtendedDataTableState(edtComponent.getTableState());
        UIColumn column = new UIColumn();
        column.setId("column2");

        String columnFilter = tableState.getColumnFilter(column);
        if (!"3".equals(columnFilter)) {
            checkColumnFilterEquals3Result = MessageFormat.format("Expected column filter to be <3>, but have <{0}>.", columnFilter);
            return;
        }
        checkColumnFilterEquals3Result = PASSED;
    }

    public void checkColumnOrderAfterDnd() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractExtendedDataTable edtComponent = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("myForm").findComponent("edt");
        ExtendedDataTableState tableState = new ExtendedDataTableState(edtComponent);

        String[] expectedOrder = { "column3", "column1", "column2" };
        String[] order = tableState.getColumnsOrder();
        if (!Arrays.deepEquals(expectedOrder, order)) {
            checkColumnOrderAfterDndResult = MessageFormat.format("Expected columns order <{0}>, but have <{1}>.", Lists.newArrayList(expectedOrder), Lists.newArrayList(order));
            return;
        }
        checkColumnOrderAfterDndResult = PASSED;
    }

    public void checkWidthResizedAfterDnD() throws JSONException {
        ExtendedDataTableState beanState = new ExtendedDataTableState(getWidthState());
        String expectedColumnsWidth = "270px";
        String columnsWidthState = beanState.toJSON().getJSONObject("columnsWidthState").getString("column1");
        if (!expectedColumnsWidth.equals(columnsWidthState)) {
            checkWidthResizedAfterDnDResult = MessageFormat.format("Backing bean table state should be updated. Expected columnsWidthState to be <{0}>, but have <{1}>.", expectedColumnsWidth, columnsWidthState);
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractExtendedDataTable edtComponent = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("myForm").findComponent("edt");
        ExtendedDataTableState tableState = new ExtendedDataTableState(edtComponent);

        columnsWidthState = tableState.toJSON().getJSONObject("columnsWidthState").getString("column1");
        if (!expectedColumnsWidth.equals(columnsWidthState)) {
            checkWidthResizedAfterDnDResult = MessageFormat.format("EDT tableState should be updated. Expected columnsWidthState to be <{0}>, but have <{1}>.", expectedColumnsWidth, columnsWidthState);
            return;
        }
        checkWidthResizedAfterDnDResult = PASSED;
    }

    public String getCheckAscendingSortOrderResult() {
        return checkAscendingSortOrderResult;
    }

    public String getCheckColumnFilterEquals3Result() {
        return checkColumnFilterEquals3Result;
    }

    public String getCheckColumnOrderAfterDndResult() {
        return checkColumnOrderAfterDndResult;
    }

    public String getCheckWidthResizedAfterDnDResult() {
        return checkWidthResizedAfterDnDResult;
    }

    public String[] getColumnsOrder() {
        return columnsOrder;
    }

    public String getFilterState() {
        return filterState;
    }

    public Long getFilterValue() {
        return filterValue;
    }

    public String getOrderState() {
        return orderState;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public SortOrder getSortOrder2() {
        return sortOrder2;
    }

    public String getSortState() {
        return sortState;
    }

    public List<String> getValues() {
        return values;
    }

    public String getWidthState() {
        return widthState;
    }

    public void setFilterValue(Long filterValue) {
        this.filterValue = filterValue;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setWidthState(String widthState) {
        this.widthState = widthState;
    }
}
