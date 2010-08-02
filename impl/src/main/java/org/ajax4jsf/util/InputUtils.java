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

package org.ajax4jsf.util;

import org.ajax4jsf.Messages;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.Serializable;

/**
 * @author Maksim Kaszynski
 * @author Manfred Geiler
 */
public final class InputUtils {
    public static final String EMPTY_STRING = new String();
    public static final Object NOTHING = new Serializable() {
    };

    private InputUtils() {
    }

    public static boolean isTrue(Object obj) {
        if (!(obj instanceof Boolean)) {
            return false;
        }

        return ((Boolean) obj).booleanValue();
    }

    public static boolean isDisabled(UIComponent component) {
        return isTrue(component.getAttributes().get("disabled"));
    }

    public static boolean isReadOnly(UIComponent component) {
        return isTrue(component.getAttributes().get("readonly"));
    }

    public static Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue)
        throws ConverterException {
        String newValue = (String) submittedValue;
        ValueExpression valueExpression = component.getValueExpression("value");
        Converter converter = null;

        if (component instanceof ValueHolder) {
            converter = ((ValueHolder) component).getConverter();
        }

        if ((converter == null) && (valueExpression != null)) {
            Class converterType = valueExpression.getType(context.getELContext());

            if ((converterType == null) || (converterType == Object.class) || (converterType == String.class)) {
                return newValue;
            } else {
                converter = context.getApplication().createConverter(converterType);

                if (converter == null) {
                    throw new ConverterException(Messages.getMessage(Messages.NO_CONVERTER_FOUND_ERROR,
                        converterType.getName()));
                }
            }
        } else if (converter == null) {
            return newValue;
        }

        return converter.getAsObject(context, component, newValue);
    }

    public static String getConvertedStringValue(FacesContext context, UIComponent component, Object value) {
        Converter converter = null;

        if (component instanceof ValueHolder) {
            converter = ((ValueHolder) component).getConverter();
        }

        if (converter == null) {
            if (value == null) {
                return "";
            } else if (value instanceof String) {
                return (String) value;
            }

            Class converterType = value.getClass();

            if (converterType != null) {
                converter = context.getApplication().createConverter(converterType);
            }

            if (converter == null) {
                return value.toString();
            }
        }

        return converter.getAsString(context, component, value);
    }
}
