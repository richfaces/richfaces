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

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;

import org.richfaces.component.AbstractSelectManyComponent;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.component.util.SelectItemsInterface;
import org.richfaces.component.util.SelectUtils;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.util.HtmlDimensions;

import javax.annotation.Nullable;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
    private static final Logger LOG = RichfacesLogger.APPLICATION.getLogger();

    public static final String CELL_CSS = "-c";
    public static final String ITEM_CSS = "-opt";
    public static final String ITEM_CSS_DIS = "-opt-dis";
    public static final String BUTTON_CSS = "-btn";
    public static final String BUTTON_CSS_DIS = "-btn-dis";
    public static Comparator<ClientSelectItem> clientSelectItemComparator = new Comparator<ClientSelectItem>() {
        public int compare(ClientSelectItem clientSelectItem, ClientSelectItem clientSelectItem1) {
            Integer sortOrder = (clientSelectItem == null || clientSelectItem.getSortOrder() == null) ? 0 : clientSelectItem.getSortOrder();
            Integer sortOrder1 = (clientSelectItem1 == null || clientSelectItem1.getSortOrder() == null) ? 0 : clientSelectItem1.getSortOrder();
            return sortOrder.compareTo(sortOrder1);
        }
    };


    public static Predicate<ClientSelectItem> SELECTED_PREDICATE =  new Predicate<ClientSelectItem>() {
        public boolean apply(@Nullable ClientSelectItem clientSelectItem) {
            return clientSelectItem.isSelected();
        }
    };

    public static Predicate<ClientSelectItem> UNSELECTED_PREDICATE = Predicates.not(SELECTED_PREDICATE);

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

    public static void encodeRows(FacesContext facesContext, UIComponent component, SelectManyRendererBase renderer, Iterator<ClientSelectItem> clientSelectItems, String cssPrefix) throws IOException {
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;
        if (clientSelectItems != null && clientSelectItems.hasNext()) {
            String clientId = component.getClientId(facesContext);
            Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
            Object oldVar = requestMap.get(select.getVar());
            while (clientSelectItems.hasNext()) {
                ClientSelectItem clientSelectItem = clientSelectItems.next();
                requestMap.put(select.getVar(), clientSelectItem.getSelectItem().getValue());
                encodeOneRow(facesContext, component, renderer, clientSelectItem, cssPrefix);
            }
            requestMap.put(select.getVar(), oldVar);
            oldVar = null;
        }
    }

    public static void encodeOneRow(FacesContext facesContext, UIComponent component, SelectManyRendererBase renderer, ClientSelectItem clientSelectItem, String cssPrefix) throws IOException {
        AbstractSelectManyComponent table = (AbstractSelectManyComponent) component;
        String defaultItemCss = cssPrefix + ITEM_CSS;
        String defaultItemCssDis = cssPrefix + ITEM_CSS_DIS;

        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = table.getClientId(facesContext);
        String itemClientId = clientId + "Item" + clientSelectItem.getSortOrder();
        clientSelectItem.setClientId(itemClientId);
        writer.startElement(HtmlConstants.TR_ELEMENT, table);
        writer.writeAttribute("id", itemClientId, null);
        String itemCss;
        if (!table.isDisabled()) {
            itemCss = HtmlUtil.concatClasses(table.getItemClass(), defaultItemCss);
        } else {
            itemCss = HtmlUtil.concatClasses(table.getItemClass(), defaultItemCss, defaultItemCssDis);
        }
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, itemCss, null);

        String cellClassName = cssPrefix + CELL_CSS;

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
                    columnClass = HtmlUtil.concatClasses(cellClassName, columnClasses[columnCounter % columnClasses.length], column.getAttributes().get("styleClass"));
                } else {
                    columnClass = HtmlUtil.concatClasses(cellClassName, column.getAttributes().get("styleClass"));
                }
                writer.writeAttribute("class", columnClass, null);
                renderer.renderChildren(facesContext, column);
                writer.endElement(HtmlConstants.TD_ELEM);
                columnCounter++;
            }
        }
        writer.endElement(HtmlConstants.TR_ELEMENT);
    }

    public static void encodeItems(FacesContext facesContext, UIComponent component, Iterator<ClientSelectItem> clientSelectItems, String cssPrefix) throws IOException {
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;
        String defaultItemCss = cssPrefix + ITEM_CSS;
        String defaultItemCssDis = cssPrefix + ITEM_CSS_DIS;
        if (clientSelectItems != null && clientSelectItems.hasNext()) {
            ResponseWriter writer = facesContext.getResponseWriter();
            String clientId = component.getClientId(facesContext);
            while (clientSelectItems.hasNext()) {
                ClientSelectItem clientSelectItem = clientSelectItems.next();
                String itemClientId = clientId + "Item" + clientSelectItem.getSortOrder();
                clientSelectItem.setClientId(itemClientId);
                writer.startElement(HtmlConstants.DIV_ELEM, component);
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, itemClientId, null);
                String itemCss;
                if (!select.isDisabled() && !clientSelectItem.getSelectItem().isDisabled()) {
                    itemCss = HtmlUtil.concatClasses(select.getItemClass(), defaultItemCss);
                } else {
                    itemCss = HtmlUtil.concatClasses(select.getItemClass(), defaultItemCss, defaultItemCssDis);
                }
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, itemCss, null);
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

    public static List<ClientSelectItem> getClientSelectItems(FacesContext facesContext, AbstractSelectManyComponent select, Iterator<SelectItem> selectItems) {
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
        int count = values.size();
        // TODO: Deal with SelectItemGroups
        while (selectItems.hasNext()) {
            SelectItem selectItem = selectItems.next();
            boolean selected;
            int sortOrder;
            if (values.contains(selectItem.getValue())) { // TODO: this requires value#equals() to be overridden. Redo with comparators?
                selected = true;
                sortOrder = values.indexOf(selectItem.getValue());
            } else {
                selected = false;
                sortOrder = count;
            }
            ClientSelectItem clientSelectItem = SelectHelper.generateClientSelectItem(facesContext, select, selectItem, sortOrder, selected);
            clientSelectItems.add(clientSelectItem);
            if (!selected) {
                count++;
            }
        }
        Collections.sort(clientSelectItems, clientSelectItemComparator);
        return clientSelectItems;
    }

    public static Object getConvertedValue(FacesContext facesContext, UIComponent component, Object val) throws ConverterException {
        String[] values = (val == null) ? new String[0] : (String[]) val;
        Converter converter = SelectManyHelper.getItemConverter(facesContext, component);
        ValueExpression ve = component.getValueExpression("value");
        Object targetForConvertedValues = null;
        if (ve != null) {
            // If the component has a ValueExpression for value, let modelType be the type of the value expression
            Class<?> modelType = ve.getType(facesContext.getELContext());
            if (modelType.isArray()) {
                // If the component has a ValueExpression for value and the type of the expression is an array, let targetForConvertedValues be a new array of the expected type.
                Class<?> arrayComponentType = modelType.getComponentType();
                targetForConvertedValues = Array.newInstance(arrayComponentType, values.length);
            } else if (Collection.class.isAssignableFrom(modelType) || Object.class.equals(modelType)) {
                // If modelType is a Collection, do the following to arrive at targetForConvertedValues:
                // Ask the component for its attribute under the key "collectionType"
                Object collectionType = component.getAttributes().get("collectionType");
                if (collectionType != null) {
                    // Let targetForConvertedValues be a new instance of Collection implemented by the concrete class specified in collectionType
                    Class<?> collectionClass = getCollectionClass(collectionType);
                    try {
                        targetForConvertedValues = collectionClass.newInstance();
                    } catch (Exception e) {
                        throw new FacesException(e);
                    }
                } else {
                    // If there is no "collectionType" attribute, call getValue() on the component
                    // The result will implement Collection.
                    Collection value = (Collection) ((EditableValueHolder) component).getValue();
                    if (value instanceof Cloneable) {
                        // If the result also implements Cloneable, let targetForConvertedValues be the result of calling its clone() method,
                        // then calling clear() on the cloned Collection.
                        try {
                            targetForConvertedValues = (Collection) value.getClass().getMethod("clone").invoke(value);
                            ((Collection) targetForConvertedValues).clear();
                        } catch (Exception e) {
                            // If unable to clone the value for any reason, log a message
                            LOG.log(Logger.Level.WARNING, "Unable to clone collection");
                        }
                    }
                    if (targetForConvertedValues == null) {
                        // and proceed to the next step
                        Class<?> collectionClass = value == null ? modelType : value.getClass();
                        try {
                            // If modelType is a concrete class, let targetForConvertedValues be a new instance of that class.
                            targetForConvertedValues = collectionClass.newInstance();
                            ((Collection) targetForConvertedValues).clear();
                        } catch (Exception e) {
                            // Otherwise, the concrete type for targetForConvertedValues is taken from the following table
                            if (Collection.class.isAssignableFrom(modelType)) {
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
                        }
                    }
                }
            } else {
                throw new FacesException("ValueExpression must be either an Array, or a Collection");
            }
        } else {
            // If the component does not have a ValueExpression for value, let targetForConvertedValues be an array of type Object.
            targetForConvertedValues = new Object[values.length];
        }
        for (int i = 0; i < values.length; i++) {
            Object value;
            if (converter == null) {
                value = values[i];
            } else {
                value = converter.getAsObject(facesContext, component, values[i]);
            }
            if (targetForConvertedValues.getClass().isArray()) {
                Array.set(targetForConvertedValues, i, value);
            } else {
                ((Collection) targetForConvertedValues).add(value);
            }
        }
        return targetForConvertedValues;
    }

    private static Class getCollectionClass(Object collectionType) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<? extends Collection> collectionClass = null;
        if (classLoader == null) {
            classLoader = SelectManyRendererBase.class.getClassLoader();
        }
        if (collectionType instanceof Class) {
            collectionClass = (Class<? extends Collection>) collectionType;
        } else if (collectionType instanceof String) {
            try {
                collectionClass = classLoader.loadClass((String) collectionType).asSubclass(Collection.class);
            } catch (ClassNotFoundException e) {
                throw new FacesException(e);
            }
        } else {
            throw new FacesException("'collectionType' should resolve to type String or Class. Found: "
                + collectionType.getClass().getName());
        }
        return collectionClass;
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
        if (converter == null) {
            // Spec says "If for any reason a Converter cannot be found, assume the type to be a String array." However
            // if we don't have an explicit converter, see if one is registered for the class of the SelectItem values
            Iterator<SelectItem> selectItems = SelectUtils.getSelectItems(facesContext, component);
            converter = getSelectItemConverter(facesContext.getApplication(), selectItems);
        }

        return converter;
    }

    public static Converter getSelectItemConverter(Application facesApplication, Iterator<SelectItem> selectItems) {
        Converter converter = null;
        while (selectItems.hasNext() && converter == null) {
            SelectItem selectItem = selectItems.next();
            if (selectItem instanceof SelectItemGroup) {
                SelectItemGroup selectItemGroup = (SelectItemGroup) selectItem;
                Iterator<SelectItem> groupSelectItems = Iterators.forArray(selectItemGroup.getSelectItems());
                // Recursively get the converter from the SelectItems of the SelectItemGroup
                converter = getSelectItemConverter(facesApplication, groupSelectItems);
            }
            else {
                Class<?> selectItemClass = selectItem.getValue().getClass();
                if (String.class.equals(selectItemClass)) {
                    return null; // No converter required for strings
                }
                try {
                    converter = facesApplication.createConverter(selectItemClass); // Lookup the converter registered for the class
                }
                catch (FacesException exception) {
                    // Converter cannot be created
                }
            }
        }
        return converter;
    }

    public static UISelectItems getPseudoSelectItems(SelectItemsInterface selectItemsInterface) {
        UISelectItems selectItems = null;
        if (selectItemsInterface.getVar() != null && selectItemsInterface.getItemValues() != null) {
            selectItems = new UISelectItems();
            selectItems.setValue(selectItemsInterface.getItemValues());
            selectItems.getAttributes().put("var", selectItemsInterface.getVar());
            if (selectItemsInterface.getItemValue() != null) {
                selectItems.getAttributes().put("itemValue", selectItemsInterface.getItemValue());
            }
            if (selectItemsInterface.getItemLabel() != null) {
                selectItems.getAttributes().put("itemLabel", selectItemsInterface.getItemLabel());
            }
        }
        return selectItems;
    }

}
