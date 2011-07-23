/*
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
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.EditableValueHolder;
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
 *
 */
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"), @ResourceDependency(name = "jquery.js"),
        @ResourceDependency(name = "jquery.position.js"), @ResourceDependency(name = "richfaces.js"),
        @ResourceDependency(name = "richfaces-utils.js"), @ResourceDependency(name = "jquery.position.js"),
        @ResourceDependency(name = "richfaces-event.js"), @ResourceDependency(name = "richfaces-base-component.js"),
        @ResourceDependency(name = "richfaces-selection.js"),
        @ResourceDependency(library = "org.richfaces", name = "inputBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "popup.js"),
        @ResourceDependency(library = "org.richfaces", name = "list.js"),
        @ResourceDependency(library = "org.richfaces", name = "popupList.js"),
        @ResourceDependency(library = "org.richfaces", name = "pickList.js"),
        @ResourceDependency(library = "org.richfaces", name = "pickList.ecss") })
public class SelectManyRendererBase extends InputRendererBase {
    public static final String ITEM_CSS = "rf-pick-opt";
    private static final String HIDDEN_SUFFIX = "Hidden";

    public List<ClientSelectItem> getClientSelectItems(FacesContext facesContext, UIComponent component) {
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;
        List<SelectItem> selectItemsAll = SelectHelper.getSelectItems(facesContext, component);
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
        for (SelectItem selectItem : selectItemsAll) {
            boolean selected = valuesSet.contains(selectItem.getValue());
            ClientSelectItem clientSelectItem = SelectHelper.generateClientSelectItem(facesContext, component, selectItem, sortOrder, selected);
            clientSelectItems.add(clientSelectItem);
            sortOrder++;
        }
        return clientSelectItems;
    }

    public String csvEncodeSelectedItems(List<ClientSelectItem> clientSelectItems) {
        if (clientSelectItems == null || clientSelectItems.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<ClientSelectItem> iter = clientSelectItems.iterator();
        while (iter.hasNext()) {
            ClientSelectItem item = iter.next();
            if (item.isSelected()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(item.getConvertedValue());
            }
        }
        return sb.toString();
    }

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        if (!(component instanceof AbstractSelectManyComponent)) {
            throw new IllegalArgumentException(String.format("Component %s is not an AbstractSelectManyComponent", component.getClientId(context)));
        }
        AbstractSelectManyComponent picklist = (AbstractSelectManyComponent) component;

        String hiddenClientId = picklist.getClientId(context);
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

        if (picklist.isDisabled()) {
            return;
        }
        String value = paramMap.get(hiddenClientId);
        if (value != null) {
            if (value.trim().equals("")) {
                ((EditableValueHolder) picklist).setSubmittedValue(new String[] {});
            } else {
                String[] reqValues = value.split(",");
                ((EditableValueHolder) picklist).setSubmittedValue(reqValues);
            }
        } else {
            ((EditableValueHolder) picklist).setSubmittedValue(new String[] {});
        }
    }

    public Object getConvertedValue(FacesContext facesContext, UIComponent component, Object val) throws ConverterException {
        String[] values = (val == null) ? new String[0] : (String[]) val;
        Converter converter = getItemConverter(facesContext, component);
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

    public Converter getItemConverter(FacesContext facesContext, UIComponent component) {
        Converter converter = null;
        if (component instanceof ValueHolder) {
            converter = ((ValueHolder) component).getConverter();
        } else {
            ValueExpression ve = component.getValueExpression("value");
            if (ve != null) {
                Class<?> valueType = ve.getType(facesContext.getELContext());
                if (valueType.isArray()) {
                    converter = facesContext.getApplication().createConverter(valueType);
                }
            }
        }
        return converter;
    }

    public void encodeTargetItems(FacesContext context, UIComponent component, List<ClientSelectItem> clientSelectItems) throws IOException {
        List<ClientSelectItem> selectItemsForSelectedValues = selectItemsFilter(clientSelectItems, true);
        encodeItems(context, component, false, selectItemsForSelectedValues);
    }

    public void encodeSourceItems(FacesContext context, UIComponent component, List<ClientSelectItem> clientSelectItems) throws IOException {
        List<ClientSelectItem> selectItemsForAvailableList = selectItemsFilter(clientSelectItems, false);
        encodeItems(context, component, true, selectItemsForAvailableList);
    }

    protected List<ClientSelectItem> selectItemsFilter(List<ClientSelectItem> selectItems, boolean filterSelected) {
        List<ClientSelectItem> result = new ArrayList<ClientSelectItem>();
        for (ClientSelectItem selectItem : selectItems) {
            if (selectItem.isSelected() == filterSelected) {
                result.add(selectItem);
            }
        }
        return result;
    }

    private void encodeItems(FacesContext facesContext, UIComponent component, boolean source, List<ClientSelectItem> clientSelectItems) throws IOException {
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

    //TODO: Make the following methods DRY with the corresponding SelectRendererBase methods
    protected String getMinListHeight(AbstractSelectManyComponent select) {
        String height = HtmlDimensions.formatSize(select.getMinListHeight());
        if (height == null || height.length() == 0) {
            height = "20px";
        }
        return height;
    }

    protected String getMaxListHeight(AbstractSelectManyComponent select) {
        String height = HtmlDimensions.formatSize(select.getMaxListHeight());
        if (height == null || height.length() == 0) {
            height = "100px";
        }
        return height;
    }

    protected String getListHeight(AbstractSelectManyComponent select) {
        String height = HtmlDimensions.formatSize(select.getListHeight());
        if (height == null || height.length() == 0) {
            height = "auto";
        }
        return height;
    }

    protected String getListWidth(AbstractSelectManyComponent select) {
        String width = HtmlDimensions.formatSize(select.getListWidth());
        if (width == null || width.length() == 0) {
            width = "200px";
        }
        return width;
    }

    public String encodeHeightAndWidth(UIComponent component) {
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;

        String height = getListHeight(select);
        if (!"auto".equals(height)) {
            height = (height != null && height.trim().length() != 0) ? ("height: " + height) : "";
        } else {
            String minHeight = getMinListHeight(select);
            minHeight = (minHeight != null && minHeight.trim().length() != 0) ? ("min-height: " + minHeight) : "";

            String maxHeight = getMaxListHeight(select);
            maxHeight = (maxHeight != null && maxHeight.trim().length() != 0) ? ("max-height: " + maxHeight) : "";
            height = concatStyles(minHeight, maxHeight);
        }

        String width = getListWidth(select);
        width = (width != null && width.trim().length() != 0) ? ("width: " + width) : "";

        return concatStyles(height, width);
    }
}
