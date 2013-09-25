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
package org.richfaces.ui.iteration;

import org.richfaces.model.SortMode;
import org.richfaces.model.SortOrder;
import org.richfaces.ui.iteration.column.AbstractColumn;
import org.richfaces.validator.MessageFactory;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Anton Belevich
 *
 */
public abstract class SortingFilteringRowsRenderer extends AbstractRowsRenderer {
    private static final String FILTERING_STRING = "r:filtering";
    private static final String SORTING_STRING = "r:sorting";
    private static final String FILTER_VALUE_STRING = "filterValue";
    private static final String SORT_ORDER_STRING = "sortOrder";
    private static final String SORT_PRIORITY_STRING = "sortPriority";
    private static final String SEPARATOR = ":";

    protected void decodeSortingFiltering(FacesContext context, UIComponent component) {
        if (component instanceof UIDataTableBase) {

            UIDataTableBase dataTableBase = (UIDataTableBase) component;
            Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
            String clientId = dataTableBase.getClientId(context);

            String filtering = requestMap.get(clientId + FILTERING_STRING);
            if (filtering != null && filtering.trim().length() > 0) {
                decodeFiltering(context, dataTableBase, filtering);
            }

            String sorting = requestMap.get(clientId + SORTING_STRING);
            if (sorting != null && sorting.trim().length() > 0) {
                decodeSorting(context, dataTableBase, sorting);
            }
        }
    }

    protected void decodeFiltering(FacesContext context, UIDataTableBase dataTableBase, String value) {
        String[] values = value.split(SEPARATOR);
        if (Boolean.parseBoolean(values[2])) {
            for (Iterator<UIComponent> iterator = dataTableBase.columns(); iterator.hasNext();) {
                UIComponent column = iterator.next();
                if (values[0].equals(column.getId())) {
                    updateAttribute(context, column, FILTER_VALUE_STRING, values[1]);
                } else {
                    updateAttribute(context, column, FILTER_VALUE_STRING, null);
                }
            }
        } else {
            UIComponent child = dataTableBase.findComponent(values[0]);
            try {

                updateAttribute(context, child, FILTER_VALUE_STRING, values[1]);
            } catch (FacesException e) {
                if (child instanceof AbstractColumn && ((AbstractColumn)child).isBuiltInFilterControlsEnabled() && e.getCause() instanceof ELException) {
                    addFilterConverterErrorMessage(context, (AbstractColumn) child, values[1], e);
                } else {
                    throw e;
                }
            }
        }
        context.getPartialViewContext().getRenderIds().add(dataTableBase.getClientId(context)); // TODO Use partial re-rendering
                                                                                                // here.
    }

    private void addFilterConverterErrorMessage(FacesContext context, AbstractColumn column, String submittedValue, Exception exception) {
        column.getAttributes().put("submittedFilterValue", submittedValue);
        FacesMessage message;
        String converterMessageString = column.getFilterConverterMessage();
        if (null != converterMessageString) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, converterMessageString, converterMessageString);
        } else {
            message = MessageFactory.createMessage(context, "org.richfaces.BUILT_IN_FILTER_VALUE_CONVERSION_ERROR");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setDetail(exception.getCause().getLocalizedMessage());
        }
        context.addMessage(column.getClientId(), message);
    }

    protected void decodeSorting(FacesContext context, UIDataTableBase dataTableBase, String value) {
        Set<Object> sortPriority = new LinkedHashSet<Object>();

        String[] values = value.split(SEPARATOR);
        String columnId = values[0];
        String sortOrder = values[1];
        boolean isClear = Boolean.parseBoolean(values[2]);

        if (isClear || SortMode.single.equals(dataTableBase.getSortMode())) {
            for (Iterator<UIComponent> iterator = dataTableBase.columns(); iterator.hasNext();) {
                UIComponent column = iterator.next();
                if (columnId.equals(column.getId())) {
                    updateSortOrder(context, column, sortOrder);
                    sortPriority.add(columnId);
                } else {
                    updateAttribute(context, column, SORT_ORDER_STRING, SortOrder.unsorted);
                }
            }
        } else {
            updateSortOrder(context, dataTableBase.findComponent(columnId), sortOrder);
            Collection<?> priority = dataTableBase.getSortPriority();
            if (priority != null) {
                priority.remove(columnId);
                sortPriority.addAll(priority);
            }
            sortPriority.add(columnId);
        }
        updateAttribute(context, dataTableBase, SORT_PRIORITY_STRING, sortPriority);
        context.getPartialViewContext().getRenderIds().add(dataTableBase.getClientId(context));
    }

    private void updateSortOrder(FacesContext context, UIComponent component, String value) {
        SortOrder sortOrder = SortOrder.ascending;
        try {
            sortOrder = SortOrder.valueOf(value);
        } catch (IllegalArgumentException e) {
            // If value isn't name of enum constant of SortOrder, toggle sortOrder of column.
            if (SortOrder.ascending.equals(component.getAttributes().get(SORT_ORDER_STRING))) {
                sortOrder = SortOrder.descending;
            }
        }
        updateAttribute(context, component, SORT_ORDER_STRING, sortOrder);
    }

    protected void updateAttribute(FacesContext context, UIComponent component, String attribute, Object value) {
        Object oldValue = component.getAttributes().get(attribute);
        if ((oldValue != null && !oldValue.equals(value)) || (oldValue == null && value != null)) {
            ELContext elContext = context.getELContext();
            ValueExpression ve = component.getValueExpression(attribute);
            if (ve != null && !ve.isReadOnly(elContext)) {
                component.getAttributes().put(attribute, null);
                try {
                    ve.setValue(elContext, value);
                } catch (ELException e) {
                    throw new FacesException(e);
                }
            } else {
                component.getAttributes().put(attribute, value);
            }
        }
    }
}
