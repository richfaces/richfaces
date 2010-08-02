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

package org.richfaces.webapp.taglib;

import org.ajax4jsf.Messages;
import org.richfaces.component.UIDataAdaptor;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.ActionSource2;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.event.MethodExpressionValueChangeListener;
import javax.faces.validator.MethodExpressionValidator;
import javax.faces.webapp.UIComponentELTag;

/**
 * @author Maksim Kaszynski
 */
public abstract class UIComponentELTagBase extends UIComponentELTag {
    protected void setActionListenerProperty(UIComponent component, MethodExpression actionListener) {
        if (actionListener != null) {
            if (component instanceof ActionSource2) {
                ActionSource2 actionSource2 = (ActionSource2) component;

                actionSource2.addActionListener(new MethodExpressionActionListener(actionListener));
            } else {
                throw new IllegalArgumentException(Messages.getMessage(Messages.NO_ACTION_SOURCE2_ERROR,
                    component.getClientId(getFacesContext())));
            }
        }
    }

    protected void setActionProperty(UIComponent component, MethodExpression action) {
        if (action != null) {
            if (component instanceof ActionSource2) {
                ActionSource2 actionSource2 = (ActionSource2) component;

                actionSource2.setActionExpression(action);
            } else {
                throw new IllegalArgumentException(Messages.getMessage(Messages.NO_ACTION_SOURCE2_ERROR,
                    component.getClientId(getFacesContext())));
            }
        }
    }

    protected void setConverterProperty(UIComponent component, ValueExpression converter) {
        if (converter != null) {
            if (component instanceof ValueHolder) {
                ValueHolder output = (ValueHolder) component;

                if (!converter.isLiteralText()) {
                    component.setValueExpression("converter", converter);
                } else {
                    Converter conv = FacesContext.getCurrentInstance().getApplication().createConverter(
                        converter.getExpressionString());

                    output.setConverter(conv);
                }
            } else {
                throw new IllegalArgumentException(Messages.getMessage(Messages.NO_VALUE_HOLDER_ERROR,
                    component.getClass().getName()));
            }
        }
    }

    protected void setRowKeyConverterProperty(UIComponent component, ValueExpression converter) {
        if (converter != null) {
            if (component instanceof UIDataAdaptor) {
                UIDataAdaptor data = (UIDataAdaptor) component;

                if (!converter.isLiteralText()) {
                    component.setValueExpression("rowKeyConverter", converter);
                } else {
                    Converter conv = FacesContext.getCurrentInstance().getApplication().createConverter(
                        converter.getExpressionString());

                    data.setRowKeyConverter(conv);
                }
            } else {
                throw new IllegalArgumentException(Messages.getMessage(Messages.NO_DATA_ADAPTOR,
                    component.getClass().getName()));
            }
        }
    }

    protected void setValidatorProperty(UIComponent component, MethodExpression validator) {
        if (validator != null) {
            if (component instanceof EditableValueHolder) {
                EditableValueHolder input = (EditableValueHolder) component;

                input.addValidator(new MethodExpressionValidator(validator));
            } else {
                throw new IllegalArgumentException(Messages.getMessage(Messages.NO_EDITABLE_VALUE_HOLDER_ERROR,
                    component.getId()));
            }
        }
    }

    protected void setValueChangeListenerProperty(UIComponent component, MethodExpression valueChangeListener) {
        if (valueChangeListener != null) {
            if (component instanceof EditableValueHolder) {
                EditableValueHolder input = (EditableValueHolder) component;

                input.addValueChangeListener(new MethodExpressionValueChangeListener(valueChangeListener));
            } else {
                throw new IllegalArgumentException(Messages.getMessage(Messages.NO_EDITABLE_VALUE_HOLDER_ERROR,
                    component.getId()));
            }
        }
    }
}
