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
package org.richfaces.model;

import static org.richfaces.configuration.ConfigurationServiceHelper.getBooleanConfigurationValue;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModelListener;

import org.richfaces.configuration.IterationComponentsConfiguration;

/**
 * @author Konstantin Mishin
 *
 */
public class ArrangeableModel extends ExtendedDataModel<Object> implements Arrangeable {
    private ArrangeableState state;
    private List<Object> rowKeys;
    private ExtendedDataModel<?> originalModel;
    private String var;
    private String filterVar;
    private Comparator<? super String> stringComparator;

    public ArrangeableModel(ExtendedDataModel<?> originalModel, String var, String filterVar) {
        this.originalModel = originalModel;
        this.var = var;
        this.filterVar = filterVar;
    }

    public void addDataModelListener(DataModelListener listener) {
        originalModel.addDataModelListener(listener);
    }

    public void removeDataModelListener(DataModelListener listener) {
        originalModel.removeDataModelListener(listener);
    }

    public DataModelListener[] getDataModelListeners() {
        return originalModel.getDataModelListeners();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.model.ExtendedDataModel#getRowKey()
     */
    public Object getRowKey() {
        return originalModel.getRowKey();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.model.ExtendedDataModel#setRowKey(java.lang.Object)
     */
    public void setRowKey(Object key) {
        originalModel.setRowKey(key);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.model.ExtendedDataModel#walk(javax.faces.context.FacesContext, org.richfaces.model.DataVisitor,
     * org.richfaces.model.Range, java.lang.Object)
     */
    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
        final SequenceRange seqRange = (SequenceRange) range;
        int rows = seqRange.getRows();
        int rowCount = getRowCount();
        int currentRow = seqRange.getFirstRow();
        if (rows > 0) {
            rows += currentRow;
            rows = Math.min(rows, rowCount);
        } else {
            rows = rowCount;
        }
        for (; currentRow < rows; currentRow++) {
            visitor.process(context, rowKeys.get(currentRow), argument);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#getRowCount()
     */
    public int getRowCount() {
        if (rowKeys == null) {
            return -1;
        } else {
            return rowKeys.size();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#getRowData()
     */
    public Object getRowData() {
        return originalModel.getRowData();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#getRowIndex()
     */
    public int getRowIndex() {
        return rowKeys.indexOf(originalModel.getRowKey());
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#getWrappedData()
     */
    public Object getWrappedData() {
        return originalModel.getWrappedData();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#isRowAvailable()
     */
    public boolean isRowAvailable() {
        return originalModel.isRowAvailable();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#setRowIndex(int)
     */
    public void setRowIndex(int rowIndex) {
        Object originalKey = null;
        if (rowIndex >= 0 && rowIndex < rowKeys.size()) {
            originalKey = rowKeys.get(rowIndex);
        }
        originalModel.setRowKey(originalKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.model.DataModel#setWrappedData(java.lang.Object)
     */
    public void setWrappedData(Object data) {
        originalModel.setWrappedData(data);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.model.Modifiable#modify(org.richfaces.model.ModifiableState)
     */
    public void arrange(FacesContext context, ArrangeableState state) {
        initializeRowKeys(context);
        if (state != null) {
            this.state = state;
            Map<String, Object> map = context.getExternalContext().getRequestMap();
            Object value = null;
            Object filterValue = null;
            if (var != null && var.length() > 0) {
                value = map.get(var);
            }
            if (filterVar != null && filterVar.length() > 0) {
                filterValue = map.get(filterVar);
            }
            filter(context);
            sort(context);
            if (var != null && var.length() > 0) {
                map.put(var, value);
            }
            if (filterVar != null && filterVar.length() > 0) {
                map.put(filterVar, filterValue);
            }
        }
    }

    private void initializeRowKeys(FacesContext context) {
        int rowCount = originalModel.getRowCount();
        if (rowCount > 0) {
            rowKeys = new ArrayList<Object>(rowCount);
        } else {
            rowKeys = new ArrayList<Object>();
        }
        Object rowKey = originalModel.getRowKey();
        originalModel.walk(context, new DataVisitor() {
            public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
                originalModel.setRowKey(rowKey);
                if (originalModel.isRowAvailable()) {
                    rowKeys.add(rowKey);
                }
                return DataVisitResult.CONTINUE;
            }
        }, new SequenceRange(0, -1), null);
        originalModel.setRowKey(rowKey);
    }

    private void filter(FacesContext context) {
        List<FilterField> filterFields = state.getFilterFields();
        if (filterFields != null && !filterFields.isEmpty()) {
            List<Object> filteredCollection = new ArrayList<Object>();
            for (Object rowKey : rowKeys) {
                if (accept(context, rowKey)) {
                    filteredCollection.add(rowKey);
                }
            }
            rowKeys = filteredCollection;
        }
    }

    private void sort(final FacesContext context) {
        List<SortField> sortFields = state.getSortFields();
        if (sortFields != null && !sortFields.isEmpty()) {
            Collections.sort(rowKeys, new Comparator<Object>() {
                public int compare(Object rowKey1, Object rowKey2) {
                    return ArrangeableModel.this.compare(context, rowKey1, rowKey2);
                }
            });
        }
    }

    private boolean accept(FacesContext context, Object rowKey) {
        originalModel.setRowKey(rowKey);
        Object object = originalModel.getRowData();
        updateVar(context, var, object);
        for (FilterField filterField : state.getFilterFields()) {
            Filter filter = filterField.getFilter();
            if (filter != null && !filter.accept(object)) {
                return false;
            } else {
                ValueExpression filterExpression = filterField.getFilterExpression();
                if (filterExpression != null) {
                    updateVar(context, filterVar, filterField.getFilterValue());
                    if (Boolean.FALSE.equals(filterExpression.getValue(context.getELContext()))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int compare(FacesContext context, Object rowKey1, Object rowKey2) {
        originalModel.setRowKey(rowKey1);
        Object object1 = originalModel.getRowData();
        originalModel.setRowKey(rowKey2);
        Object object2 = originalModel.getRowData();
        int result = 0;
        for (Iterator<SortField> iterator = state.getSortFields().iterator(); iterator.hasNext() && result == 0;) {
            SortField sortField = iterator.next();
            SortOrder sortOrder = sortField.getSortOrder();
            if (sortOrder != null && !SortOrder.unsorted.equals(sortOrder)) {
                Comparator comparator = sortField.getComparator();
                if (comparator != null) {
                    result = comparator.compare(object1, object2);
                } else {
                    ValueExpression sortBy = sortField.getSortBy();
                    if (sortBy != null) {
                        updateVar(context, var, object1);
                        Object value1 = sortBy.getValue(context.getELContext());
                        updateVar(context, var, object2);
                        Object value2 = sortBy.getValue(context.getELContext());
                        result = compareSortByValues(context, value1, value2);
                    }
                }
                if (SortOrder.descending.equals(sortOrder)) {
                    result = -result;
                }
            }
        }
        return result;
    }

    private int compareSortByValues(FacesContext context, Object value1, Object value2) {
        int result = 0;
        if (value1 instanceof String && value2 instanceof String) {
            if (stringComparator == null) {
                stringComparator = createStringComparator(context);
            }
            result = stringComparator.compare(((String) value1).trim(), ((String) value2).trim());
        } else if (value1 == null && value2 != null) {
            result = -1;
        } else if (value2 == null && value1 != null) {
            result = 1;
        } else if (value1 instanceof Comparable<?>) {
            result = ((Comparable) value1).compareTo(value2);
        }
        return result;
    }

    private Comparator<? super String> createStringComparator(FacesContext context) {
        Comparator<? super String> comparator = null;
        Locale locale = state.getLocale();
        if (locale != null
            && getBooleanConfigurationValue(context, IterationComponentsConfiguration.Items.datatableUsesViewLocale)) {
            comparator = Collator.getInstance(locale);
        } else {
            comparator = new Comparator<String>() {
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            };
        }
        return comparator;
    }

    private void updateVar(FacesContext context, String var, Object value) {
        if (var != null && var.length() > 0) {
            context.getExternalContext().getRequestMap().put(var, value);
        }
    }
}
