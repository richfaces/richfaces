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

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.ContextCallback;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;
import javax.servlet.jsp.jstl.sql.Result;

import org.ajax4jsf.model.DataComponentState;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceDataModel;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SequenceState;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.model.CollectionDataModel;

/**
 * @author Nick Belaevski
 */
public class UISequence extends UIDataAdaptor {
    private Object iterationStatusVarObject;
    private Converter defaultRowKeyConverter;

    protected enum PropertyKeys {
        first, rows, value, iterationStatusVar
    }

    protected void updateState(SequenceState state) {
        state.setFirst(getActualFirst());
        state.setRows(getActualRows());
    }

    protected void updateState() {
        DataComponentState localState = getLocalComponentState();
        if (localState instanceof SequenceState) {
            updateState((SequenceState) localState);
        }
    }

    protected int getActualFirst() {
        return getFirst();
    }

    protected int getActualRows() {
        return getRows();
    }

    @SuppressWarnings("unchecked")
    protected DataModel<?> createFacesModel(Object value) {
        DataModel<?> model = null;

        if (value == null) {
            model = new ListDataModel(Collections.EMPTY_LIST);
        } else if (value instanceof DataModel) {
            model = (DataModel) value;
        } else if (value instanceof List) {
            model = new ListDataModel((List) value);
        } else if (Object[].class.isAssignableFrom(value.getClass())) {
            model = new ArrayDataModel((Object[]) value);
        } else if (value instanceof ResultSet) {
            model = new ResultSetDataModel((ResultSet) value);
        } else if (value instanceof Result) {
            model = new ResultDataModel((Result) value);
        } else if (value instanceof Collection) {
            model = new CollectionDataModel((Collection) value);
        } else {
            model = new ScalarDataModel(value);
        }

        return model;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ExtendedDataModel<?> createExtendedDataModel() {
        ExtendedDataModel<?> model = null;

        // Synthesize a DataModel around our current value if possible
        Object value = getValue();

        if (value instanceof ExtendedDataModel<?>) {
            model = (ExtendedDataModel<?>) value;
        } else {
            model = new SequenceDataModel(createFacesModel(value));
        }

        return model;
    }

    @Override
    protected DataComponentState createComponentState() {
        SequenceState state = new SequenceState();

        updateState(state);

        return state;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.UIDataAdaptor#getRowKeyConverter()
     */

    // TODO make this a property of model

    @Override
    @Attribute
    public Converter getRowKeyConverter() {
        Converter converter = super.getRowKeyConverter();

        if (converter == null) {
            if (defaultRowKeyConverter == null) {
                defaultRowKeyConverter = getFacesContext().getApplication().createConverter(Integer.class);
            }

            converter = defaultRowKeyConverter;
        }

        return converter;
    }

    @Attribute
    public int getFirst() {
        return (Integer) getStateHelper().eval(PropertyKeys.first, 0);
    }

    public void setFirst(int first) {
        getStateHelper().put(PropertyKeys.first, first);
        updateState();
    }

    @Attribute
    public int getRows() {
        return (Integer) getStateHelper().eval(PropertyKeys.rows, 0);
    }

    public void setRows(int rows) {
        getStateHelper().put(PropertyKeys.rows, rows);
        updateState();
    }

    @Attribute
    public Object getValue() {
        return getStateHelper().eval(PropertyKeys.value);
    }

    public void setValue(Object value) {
        resetDataModel();
        getStateHelper().put(PropertyKeys.value, value);
    }

    @Attribute
    public String getIterationStatusVar() {
        return (String) getStateHelper().get(PropertyKeys.iterationStatusVar);
    }

    public void setIterationStatusVar(String iterationStatusVar) {
        getStateHelper().put(PropertyKeys.iterationStatusVar, iterationStatusVar);
    }

    public int getRowIndex() {
        return getExtendedDataModel().getRowIndex();
    }

    @Override
    public void captureOrigValue(FacesContext faces) {
        super.captureOrigValue(faces);

        String iterationStatusVar = getIterationStatusVar();
        if (iterationStatusVar != null) {
            Map<String, Object> variablesMap = getVariablesMap(faces);

            iterationStatusVarObject = variablesMap.get(iterationStatusVar);
        }
    }

    @Override
    public void restoreOrigValue(FacesContext faces) {
        super.restoreOrigValue(faces);

        String iterationStatusVar = getIterationStatusVar();
        if (iterationStatusVar != null) {
            Map<String, Object> variablesMap = getVariablesMap(faces);

            if (iterationStatusVarObject != null) {
                variablesMap.put(iterationStatusVar, iterationStatusVarObject);
            } else {
                variablesMap.remove(iterationStatusVar);
            }
        }
    }

    @Override
    protected void setupVariable(FacesContext faces, boolean rowSelected) {
        super.setupVariable(faces, rowSelected);

        // TODO nick - should iterationStatus be available out of iteration?
        // if yes, better name than "iterationStatusVar" is required
        String iterationStatusVar = getIterationStatusVar();
        if (iterationStatusVar != null) {
            Map<String, Object> requestMap = getVariablesMap(faces);

            if (rowSelected) {
                Integer begin = null;
                Integer end = null;

                Range range = getComponentState().getRange();
                if (range instanceof SequenceRange) {
                    SequenceRange sequenceRange = (SequenceRange) range;

                    begin = sequenceRange.getFirstRow();
                    int iRows = sequenceRange.getRows();

                    if (iRows > 0) {
                        // end is zero-based
                        end = begin + iRows - 1;
                    }
                }

                SequenceIterationStatus iterationStatus = new SequenceIterationStatus(begin, end, getRowIndex(), getRowCount());

                requestMap.put(iterationStatusVar, iterationStatus);
            } else {
                requestMap.remove(iterationStatusVar);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setValueBinding(String name, javax.faces.el.ValueBinding binding) {
        if ("value".equals(name)) {
            resetDataModel();
        } else if ("first".equals(name) || "rows".equals(name)) {
            updateState();
        }

        super.setValueBinding(name, binding);
    }

    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        if ("value".equals(name)) {
            resetDataModel();
        } else if ("first".equals(name) || "rows".equals(name)) {
            updateState();
        }

        super.setValueExpression(name, binding);
    }

    @Override
    protected void preEncodeBegin(FacesContext context) {
        super.preEncodeBegin(context);

        updateState();
    }

    /**
     * Translates the relative row ID back to the original one containing a row key and invokes the callback on the affected
     * row.
     */
    @Override
    public boolean invokeOnRow(FacesContext facesContext, String clientId, ContextCallback callback) {
        if ((null == facesContext) || (null == clientId) || (null == callback)) {
            throw new NullPointerException();
        }
        char separatorChar = UINamingContainer.getSeparatorChar(facesContext);
        String baseId = getClientId(facesContext);

        if (!matchesBaseId(clientId, baseId, separatorChar)) {
            return false;
        }

        String rowId = clientId.substring(baseId.length() + 1);
        if (rowId.indexOf(separatorChar) >= 0) {
            return false;
        }
        StringBuilder rowKeyId = new StringBuilder(baseId).append(separatorChar).append(toRowKey(rowId));
        return super.invokeOnRow(facesContext, rowKeyId.toString(), callback);
    }

    private int getRelativeRowIndex() {
        int rowIndex = getRowIndex();
        int rows = getRows();

        if (rows > 1) {
            return rowIndex % rows;
        }

        return rowIndex;
    }

    public String getRelativeClientId(FacesContext facesContext) {
        char separatorChar = UINamingContainer.getSeparatorChar(facesContext);
        // save current rowKey
        Object savedRowKey = getRowKey();

        setRowKey(null);

        // retrieve base client id without rowkey part
        StringBuilder baseId = new StringBuilder(getClientId(facesContext));

        // restore rowKey
        setRowKey(savedRowKey);

        return baseId.append(separatorChar).append(getRelativeRowIndex()).toString();
    }

    /**
     * Converts the specified potential relative row ID to corresponding row key.
     */
    private Object toRowKey(String relativeRowId) {
        Object result = relativeRowId;// fall through: original ID
        int savedRowIndex = getRowIndex();
        int absoluteIndex = getAbsoluteRowIndex(relativeRowId);
        ExtendedDataModel<?> dataModel = getExtendedDataModel();
        if (absoluteIndex >= 0 && absoluteIndex < dataModel.getRowCount()) {
            dataModel.setRowIndex(absoluteIndex);
            result = dataModel.getRowKey();
            dataModel.setRowIndex(savedRowIndex);
        }
        return result;
    }

    /**
     * Converts the potential relative row index back to the absolute one.
     */
    private int getAbsoluteRowIndex(String rowIndex) {
        try {
            int relativeIndex = Integer.parseInt(rowIndex);
            int firstRow = getActualFirst();
            return firstRow + relativeIndex;
        } catch (Exception ignore) {
            return -1;
        }
    }
}
