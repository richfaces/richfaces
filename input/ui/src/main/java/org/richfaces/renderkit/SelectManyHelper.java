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
 */
package org.richfaces.renderkit;

import org.richfaces.component.AbstractSelectManyComponent;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.renderkit.util.HtmlDimensions;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class SelectManyHelper {
    public static final String ITEM_CSS = "rf-pick-opt";

    public static void encodeHeader(FacesContext facesContext, UIComponent component, SelectManyRendererBase renderer, String rowClass, String cellClass) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;
        Iterator<UIColumn> headers = select.columns();

        if (headers.hasNext()) {
            writer.startElement("tr", component);
            StringBuilder headerClass = new StringBuilder(rowClass);
            if (select.getHeaderClass() != null && !select.getHeaderClass().isEmpty()) {
                if (headerClass.length() > 0) {
                    headerClass.append(" ");
                }
                headerClass.append(select.getHeaderClass());
            }

            writer.writeAttribute("class", headerClass, null);
            while (headers.hasNext()) {
                UIColumn header = headers.next();
                writer.startElement("th", component);
                writer.writeAttribute("class", cellClass, null);
                UIComponent facet = header.getFacet("header");
                if (facet != null && facet.isRendered()) {
                    facet.encodeBegin(facesContext);
                    if (facet.getRendersChildren()) {
                        facet.encodeChildren(facesContext);
                    } else {
                        renderer.renderChildren(facesContext, facet);
                    }
                    facet.encodeEnd(facesContext);
                }
                writer.endElement("th");
            }
            writer.endElement("tr");
        }
    }

    public static void encodeRows(FacesContext facesContext, UIComponent component, SelectManyRendererBase renderer, List<ClientSelectItem> clientSelectItems, boolean source) throws IOException {
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;
        if (clientSelectItems != null && !clientSelectItems.isEmpty()) {
            String clientId = component.getClientId(facesContext);
            int i = 0;
            Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
            Object oldColumnVar = requestMap.get(select.getColumnVar());
            for (ClientSelectItem clientSelectItem : clientSelectItems) {
                if (source == !clientSelectItem.isSelected()) {
                    requestMap.put(select.getColumnVar(), clientSelectItem.getSelectItem().getValue());
                    encodeOneRow(facesContext, component, renderer, clientSelectItem);
                }
            }
            requestMap.put(select.getColumnVar(), oldColumnVar);
            oldColumnVar = null;
        }
    }

    public static void encodeOneRow(FacesContext facesContext, UIComponent component, SelectManyRendererBase renderer, ClientSelectItem clientSelectItem) throws IOException {
        AbstractSelectManyComponent table = (AbstractSelectManyComponent) component;

        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = table.getClientId(facesContext);
        writer.startElement(HtmlConstants.TR_ELEMENT, table);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", "rf-pick-opt", null);

        writer.writeAttribute(HtmlConstants.VALUE_ATTRIBUTE, clientSelectItem.getConvertedValue(), null);

        String cellClassName = "rf-pick-cell";

        String[] columnClasses;
        if (table.getColumnClasses() != null) {
            columnClasses = table.getColumnClasses().split(",");
        } else {
            columnClasses = new String[0];
        }
        int columnCounter = 0;
        Iterator<UIColumn> columnIterator = table.columns();
        while (columnIterator.hasNext()) {
            UIColumn column = columnIterator.next();
            if (column.isRendered()) {
                writer.startElement(HtmlConstants.TD_ELEM, table);

                Object width = column.getAttributes().get("width");
                if (width != null) {
                    writer.writeAttribute("style", "width: " + HtmlDimensions.formatSize(width.toString()), null);
                }

                String columnClass;
                if (columnClasses.length > 0) {
                    columnClass = String.format("%s %s", cellClassName, columnClasses[columnCounter % columnClasses.length]);
                } else {
                    columnClass = cellClassName;
                }
                writer.writeAttribute("class", columnClass, null);
                renderer.renderChildren(facesContext, column);
                writer.endElement(HtmlConstants.TD_ELEM);
                columnCounter++;
            }
        }
        writer.endElement(HtmlConstants.TR_ELEMENT);
    }

    public static void encodeItems(FacesContext facesContext, UIComponent component, boolean source, List<ClientSelectItem> clientSelectItems) throws IOException {
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;
        if (clientSelectItems != null && !clientSelectItems.isEmpty()) {
            ResponseWriter writer = facesContext.getResponseWriter();
            String clientId = component.getClientId(facesContext);
            int i = 0;
            for (ClientSelectItem clientSelectItem : clientSelectItems) {
                String itemClientId = clientId + "Item" + (i++);
                clientSelectItem.setClientId(itemClientId);
                writer.startElement(HtmlConstants.DIV_ELEM, component);
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, itemClientId, null);
                writer.writeAttribute(HtmlConstants.VALUE_ATTRIBUTE, clientSelectItem.getConvertedValue(), null);
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
                        HtmlUtil.concatClasses(select.getItemClass(), ITEM_CSS), null);
                String label = clientSelectItem.getLabel();
                if (label != null && label.trim().length() > 0) {
                    writer.writeText(label, null);
                } else {
                    writer.write("\u00a0");
                }
                writer.endElement(HtmlConstants.DIV_ELEM);
                writer.write('\n');
            }
        }
    }

    public static List<ClientSelectItem> getClientSelectItems(FacesContext facesContext, AbstractSelectManyComponent select, List<SelectItem> selectItems) {
        List<ClientSelectItem> clientSelectItems = new ArrayList<ClientSelectItem>();
        Object object = select.getValue();
        List values;
        if (object == null) {
            values = new ArrayList();
        }
        else if (object instanceof List) {
            values = (List) object;
        } else if (object instanceof Object[]) {
            values = Arrays.asList((Object[]) object);
        } else {
            throw new IllegalArgumentException("Value expression must evaluate to either a List or Object[]");
        }
        Set<Object> valuesSet = new HashSet<Object>(values);
        int sortOrder = 0;
        // TODO: Deal with SelectItemGroups
        for (SelectItem selectItem : selectItems) {
            boolean selected = valuesSet.contains(selectItem.getValue());
            ClientSelectItem clientSelectItem = SelectHelper.generateClientSelectItem(facesContext, select, selectItem, sortOrder, selected);
            clientSelectItems.add(clientSelectItem);
            sortOrder++;
        }
        return clientSelectItems;
    }

    public static Object getConvertedValue(FacesContext facesContext, UIComponent component, Object val) throws ConverterException {
        String[] values = (val == null) ? new String[0] : (String[]) val;
        Converter converter = SelectManyHelper.getItemConverter(facesContext, component);
        ValueExpression ve = component.getValueExpression("value");
        if (ve != null) {
            Class<?> modelType = ve.getType(facesContext.getELContext());
            if (modelType.isArray()) {
                Object targetForConvertedValues = Array.newInstance(modelType, values.length);
                for (int i = 0; i < values.length; i++) {
                    if (converter != null) {
                        ((Object[]) targetForConvertedValues)[i] = converter.getAsObject(facesContext, component, values[i]);
                    } else {
                        ((Object[]) targetForConvertedValues)[i] = values[i];
                    }
                }
                return targetForConvertedValues;
            } else if (Collection.class.isAssignableFrom(modelType)) {
                Collection targetForConvertedValues;
                String collectionType = (String) component.getAttributes().get("collectionType");
                if (collectionType != null) {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    if (classLoader == null) {
                        classLoader = SelectManyRendererBase.class.getClassLoader();
                    }
                    try {
                        targetForConvertedValues = classLoader.loadClass(collectionType).asSubclass(Collection.class).newInstance();
                    } catch (InstantiationException e) {
                        throw new FacesException(e);
                    } catch (IllegalAccessException e) {
                        throw new FacesException(e);
                    } catch (ClassNotFoundException e) {
                        throw new FacesException(e);
                    }
                } else {
                    Collection value = (Collection) ((EditableValueHolder) component).getValue();
                    if (value instanceof Cloneable) {
                        try {
                            targetForConvertedValues = (Collection) value.getClass().getMethod("clone").invoke(value);
                            targetForConvertedValues.clear();
                        } catch (IllegalAccessException e) {
                            throw new FacesException(e);
                        } catch (InvocationTargetException e) {
                            throw new FacesException(e);
                        } catch (NoSuchMethodException e) {
                            throw new FacesException(e);
                        }
                    } else {
                        if (SortedSet.class.isAssignableFrom(modelType)) {
                            targetForConvertedValues = new TreeSet();
                        } else if (Queue.class.isAssignableFrom(modelType)) {
                            targetForConvertedValues = new LinkedList();
                        } else if (Set.class.isAssignableFrom(modelType)) {
                            targetForConvertedValues = new HashSet(values.length);
                        } else {
                            targetForConvertedValues = new ArrayList(values.length);
                        }
                    }
                    for (int i = 0; i < values.length; i++) {
                        if (converter != null) {
                            targetForConvertedValues.add(converter.getAsObject(facesContext, component, values[i]));
                        } else {
                            targetForConvertedValues.add(values[i]);
                        }
                    }
                }
                return targetForConvertedValues;
            } else {
                throw new FacesException(String.format("ModelType (%s) must be either an Array, or a Collection", modelType));
            }
        } else {
            Object targetForConvertedValues = new Object[values.length];
            for (int i = 0; i < values.length; i++) {
                if (converter != null) {
                    ((Object[]) targetForConvertedValues)[i] = converter.getAsObject(facesContext, component, values[i]);
                } else {
                    ((Object[]) targetForConvertedValues)[i] = values[i];
                }
            }
            return targetForConvertedValues;
        }
    }

    public static Converter getItemConverter(FacesContext facesContext, UIComponent component) {
        Converter converter = null;
        if (component instanceof ValueHolder) {
            // If the component has an attached Converter, use it.
            converter = ((ValueHolder) component).getConverter();
            if (converter != null) {
                return converter;
            }
        }
        // If not, look for a ValueExpression for value (if any). The ValueExpression must point to something that is:
        ValueExpression ve = component.getValueExpression("value");
        if (ve != null) {
            Class<?> valueType = ve.getType(facesContext.getELContext());
            // An array of primitives (such as int[]). Look up the registered by-class Converter for this primitive type.
            // An array of objects (such as Integer[] or String[]). Look up the registered by-class Converter for the underlying element type.
            if (valueType != null && valueType.isArray()) {
                converter = facesContext.getApplication().createConverter(valueType);
            }
            //A java.util.Collection. Do not convert the values.
        }
        // If for any reason a Converter cannot be found, assume the type to be a String array.
        return converter;
    }

    public static List<ClientSelectItem> selectItemsFilter(List<ClientSelectItem> selectItems, boolean filterSelected) {
        List<ClientSelectItem> result = new ArrayList<ClientSelectItem>();
        for (ClientSelectItem selectItem : selectItems) {
            if (selectItem.isSelected() == filterSelected) {
                result.add(selectItem);
            }
        }
        return result;
    }
}
