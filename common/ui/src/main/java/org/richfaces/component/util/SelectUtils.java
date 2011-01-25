/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.component.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import org.ajax4jsf.Messages;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Maksim Kaszynski
 */
public final class SelectUtils {
    private static final Logger LOG = RichfacesLogger.APPLICATION.getLogger();

    private SelectUtils() {
    }

    /**
     * Gathers all select items from specified component's children
     *
     * @param context
     * @param component
     * @return list of {@link SelectItems} taken from f:selectItem and f:selectItems
     */
    public static List<SelectItem> getSelectItems(FacesContext context, UIComponent component) {
        ArrayList<SelectItem> list = new ArrayList<SelectItem>();
        Iterator<UIComponent> kids = component.getChildren().iterator();

        while (kids.hasNext()) {
            UIComponent kid = kids.next();

            if (kid instanceof UISelectItem) {
                Object value = ((UISelectItem) kid).getValue();

                if (value == null) {
                    UISelectItem item = (UISelectItem) kid;

                    list.add(new SelectItem(item.getItemValue(), item.getItemLabel(), item.getItemDescription(),
                        item.isItemDisabled()));
                } else if (value instanceof SelectItem) {
                    list.add((SelectItem) value);
                } else {
                    String valueClass = value != null ? "'" + value.getClass().getName() + "'" : "";

                    throw new IllegalArgumentException(Messages.getMessage(Messages.INVALID_ATTRIBUTE_VALUE,
                        valueClass, "<selectItem>"));
                }
            } else if ((kid instanceof UISelectItems) && (null != context)) {
                Object value = ((UISelectItems) kid).getValue();

                if (value instanceof SelectItem) {
                    list.add((SelectItem) value);
                } else if (value instanceof SelectItem[]) {
                    SelectItem[] items = (SelectItem[]) value;

                    list.addAll(Arrays.asList(items));
                } else if (value instanceof Collection) {
                    list.addAll((Collection<SelectItem>) value);
                } else if (value instanceof Map) {
                    Map<Object, Object> map = (Map<Object, Object>) value;
                    Set<Entry<Object, Object>> entrySet = map.entrySet();

                    for (Entry<Object, Object> entry : entrySet) {
                        list.add(new SelectItem(entry.getValue(), entry.getKey().toString(), null));
                    }
                } else {
                    String valueClass = (value != null) ? "'" + value.getClass().getName() + "'" : "";

                    throw new IllegalArgumentException(Messages.getMessage(Messages.INVALID_ATTRIBUTE_VALUE,
                        valueClass, "<selectItems>"));
                }
            }
        }

        return list;
    }

    /**
     * Converts UISelectMany submitted value to converted value
     *
     * @param facesContext
     * @param component
     * @param submittedValue
     * @return
     * @throws ConverterException
     * @author Manfred Geiler
     */
    public static Object getConvertedUISelectManyValue(FacesContext facesContext, UISelectMany component,
                                                       String[] submittedValue) throws ConverterException {

        // Attention!
        // This code is duplicated in jsfapi component package.
        // If you change something here please do the same in the other class!
        if (submittedValue == null) {
            throw new NullPointerException("submittedValue");
        }

        ELContext elContext = facesContext.getELContext();
        ValueExpression vb = component.getValueExpression("value");
        Class<?> valueType = null;
        Class<?> arrayComponentType = null;

        if (vb != null) {
            valueType = vb.getType(elContext);

            if ((valueType != null) && valueType.isArray()) {
                arrayComponentType = valueType.getComponentType();
            }
        }

        Converter converter = component.getConverter();

        if (converter == null) {
            if (valueType == null) {

                // No converter, and no idea of expected type
                // --> return the submitted String array
                return submittedValue;
            }

            if (List.class.isAssignableFrom(valueType)) {

                // expected type is a List
                // --> according to javadoc of UISelectMany we assume that the
                // element type
                // is java.lang.String, and copy the String array to a new List
                List<String> lst = Arrays.asList(submittedValue);

                return lst;
            }

            if (arrayComponentType == null) {
                throw new IllegalArgumentException(Messages.getMessage(Messages.VALUE_BINDING_TYPE_ERROR));
            }

            if (String.class.equals(arrayComponentType)) {
                return submittedValue; // No conversion needed for String type
            }

            if (Object.class.equals(arrayComponentType)) {
                return submittedValue; // No conversion for Object class
            }

            try {
                converter = facesContext.getApplication().createConverter(arrayComponentType);
            } catch (FacesException e) {
                LOG.error(Messages.getMessage(Messages.NO_CONVERTER_FOUND_ERROR, arrayComponentType.getName()), e);

                return submittedValue;
            }
        }

        // Now, we have a converter...
        if (valueType == null) {

            // ...but have no idea of expected type
            // --> so let's convert it to an Object array
            int len = submittedValue.length;
            Object[] convertedValues = (Object[]) Array.newInstance((arrayComponentType == null)
                ? Object.class : arrayComponentType, len);

            for (int i = 0; i < len; i++) {
                convertedValues[i] = converter.getAsObject(facesContext, component, submittedValue[i]);
            }

            return convertedValues;
        }

        if (List.class.isAssignableFrom(valueType)) {

            // Curious case: According to specs we should assume, that the
            // element type
            // of this List is java.lang.String. But there is a Converter set
            // for this
            // component. Because the user must know what he is doing, we will
            // convert the values.
            int len = submittedValue.length;
            List<Object> lst = new ArrayList<Object>(len);

            for (int i = 0; i < len; i++) {
                lst.add(converter.getAsObject(facesContext, component, submittedValue[i]));
            }

            return lst;
        }

        if (arrayComponentType == null) {
            throw new IllegalArgumentException(Messages.getMessage(Messages.VALUE_BINDING_TYPE_ERROR));
        }

        if (arrayComponentType.isPrimitive()) {

            // primitive array
            int len = submittedValue.length;
            Object convertedValues = Array.newInstance(arrayComponentType, len);

            for (int i = 0; i < len; i++) {
                Array.set(convertedValues, i, converter.getAsObject(facesContext, component, submittedValue[i]));
            }

            return convertedValues;
        } else {

            // Object array
            int len = submittedValue.length;
            ArrayList<Object> convertedValues = new ArrayList<Object>(len);

            for (int i = 0; i < len; i++) {
                convertedValues.add(i, converter.getAsObject(facesContext, component, submittedValue[i]));
            }

            return convertedValues.toArray((Object[]) Array.newInstance(arrayComponentType, len));
        }
    }

    public static Object getConvertedUIInputValue(FacesContext facesContext, UIInput component, String submittedValue)
        throws ConverterException {

        /*
        * if (submittedValue == null)
        *   throw new NullPointerException("submittedValue");
        */
        if (InputUtils.EMPTY_STRING.equals(submittedValue)) {
            return null;
        }

        Converter converter = getConverterForProperty(facesContext, component, "value");
        if (converter != null) {
            return converter.getAsObject(facesContext, component, submittedValue);
        }

        return submittedValue;
    }

    /**
     * @param facesContext
     * @param component
     * @param property
     * @return converter for specified component attribute
     * @deprecated use SelectUtils.findConverter instead
     */
    
    @Deprecated
    public static Converter getConverterForProperty(FacesContext facesContext, UIOutput component, String property) {
        Converter converter = component.getConverter();

        if (converter == null) {
            ValueExpression ve = component.getValueExpression(property);

            if (ve != null) {
                Class<?> valueType = ve.getType(facesContext.getELContext());

                if ((valueType == null) || Object.class.equals(valueType)) {

                    // No converter needed
                } else {
                    converter = facesContext.getApplication().createConverter(valueType);

                    if (converter == null && !String.class.equals(valueType)) {
                        throw new ConverterException(Messages.getMessage(Messages.NO_CONVERTER_FOUND_ERROR,
                            valueType.getName()));
                    }
                }
            }
        }

        return converter;
    }
    
    public static Converter findConverter(FacesContext facesContext, UIOutput component, String property) {
        Converter converter = component.getConverter();

        if (converter == null) {

            ValueExpression ve = component.getValueExpression(property);
            
            if (ve != null) {

                Class<?> valueType = ve.getType(facesContext.getELContext());
                if ((valueType == null) || Object.class.equals(valueType)) {
                    // No converter needed
                } else {
                    converter = facesContext.getApplication().createConverter(valueType);
                }
                
            }
        }

        return converter;
    }
     
}
