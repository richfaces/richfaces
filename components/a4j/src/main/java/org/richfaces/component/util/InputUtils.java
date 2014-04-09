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

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.richfaces.renderkit.util.RendererUtils;

import com.google.common.base.Strings;

/**
 * @author Maksim Kaszynski
 * @author Manfred Geiler
 */
public final class InputUtils {
    private static final ConverterLookupStrategy DEFAULT_CONVERTER_LOOKUP_STRATEGY = new ConverterLookupStrategy() {
        public Converter getConverterByValue(FacesContext context, UIComponent component, Object value) {
            Converter result = null;

            if (component instanceof ValueHolder) {
                result = ((ValueHolder) component).getConverter();
            }

            if (result == null && value != null) {
                result = getConverterForType(context, value.getClass());
            }

            return result;
        }

        public Converter getConverterByProperty(FacesContext context, UIComponent component) {
            return findConverter(context, component, "value");
        }
    };

    private InputUtils() {
    }

    public interface ConverterLookupStrategy {
        Converter getConverterByProperty(FacesContext context, UIComponent component);

        Converter getConverterByValue(FacesContext context, UIComponent component, Object value);
    }

    public static boolean isDisabled(UIComponent component) {
        return RendererUtils.getInstance().isBooleanAttribute(component, "disabled");
    }

    public static boolean isReadOnly(UIComponent component) {
        return RendererUtils.getInstance().isBooleanAttribute(component, "readonly");
    }

    /**
     * Returns converter for given Java type
     */
    public static Converter getConverterForType(FacesContext context, Class<?> type) {
        // see getConvertedValue
        if (type == null || Object.class.equals(type)) {
            return null;
        }

        return context.getApplication().createConverter(type);
    }

    public static String getConvertedStringValue(FacesContext context, UIComponent component, Object value)
        throws ConverterException {
        return getConvertedStringValue(context, component, DEFAULT_CONVERTER_LOOKUP_STRATEGY, value);
    }

    public static String getConvertedStringValue(FacesContext context, UIComponent component,
        ConverterLookupStrategy converterLookupStrategy, Object value) throws ConverterException {

        Converter converter = converterLookupStrategy.getConverterByValue(context, component, value);
        if (converter != null) {
            return converter.getAsString(context, component, value);
        }

        if (value == null) {
            return "";
        }

        if (value instanceof String) {
            return (String) value;
        }

        return value.toString();
    }

    /**
     * <p>Returns {@link Converter} based on a converter configured for provided {@link UIComponent} or returns converter for type referenced by given property's {@link ValueExpression}.</p>
     *
     * <p>If no converter applied for conditions above, null is returned instead.</p>
     */
    public static Converter findConverter(FacesContext facesContext, UIComponent component, String property) {
        Converter converter = null;

        if (component instanceof ValueHolder) {
            converter = ((ValueHolder) component).getConverter();
        }

        if (converter == null) {

            ValueExpression ve = component.getValueExpression(property);

            if (ve != null) {

                Class<?> valueType = ve.getType(facesContext.getELContext());
                if (valueType == null || Object.class.equals(valueType)) {
                    // No converter needed
                } else {
                    converter = facesContext.getApplication().createConverter(valueType);
                }
            }
        }

        return converter;
    }

    public static Object getConvertedValue(FacesContext context, UIComponent component, Object val) throws ConverterException {

        return getConvertedValue(context, component, DEFAULT_CONVERTER_LOOKUP_STRATEGY, val);
    }

    public static Object getConvertedValue(FacesContext context, UIComponent component,
        ConverterLookupStrategy converterLookupStrategy, Object val) throws ConverterException {

        String submittedString = (String) val;
        if (Strings.isNullOrEmpty(submittedString)) {
            return null;
        }

        Converter converter = converterLookupStrategy.getConverterByProperty(context, (UIInput) component);
        if (converter != null) {
            return converter.getAsObject(context, component, submittedString);
        }

        return submittedString;
    }

    public static String getInputValue(FacesContext context, UIComponent component) throws ConverterException {
        return getInputValue(context, component, DEFAULT_CONVERTER_LOOKUP_STRATEGY);
    }

    public static String getInputValue(FacesContext context, UIComponent component,
        ConverterLookupStrategy converterLookupStrategy) throws ConverterException {

        UIInput input = (UIInput) component;
        String submittedValue = (String) input.getSubmittedValue();

        if (submittedValue != null) {
            return submittedValue;
        }

        Object value = input.getValue();
        Converter converter = converterLookupStrategy.getConverterByValue(context, input, value);

        if (converter != null) {
            return converter.getAsString(context, input, value);
        } else {
            return value != null ? value.toString() : "";
        }
    }
}
