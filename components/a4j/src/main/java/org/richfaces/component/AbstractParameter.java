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
package org.richfaces.component;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.ajax4jsf.Messages;
import org.ajax4jsf.component.JavaScriptParameter;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.util.PartialStateHolderUtil;
import org.richfaces.view.facelets.html.ParameterHandler;

/**
 * <p>
 * The &lt;a4j:param&gt; behavior combines the functionality of the JavaServer Faces (JSF) components
 * &lt;f:param&gt; and &lt;f:actionListener&gt;.
 * </p>
 * @author shura, alexsmirnov
 */
@JsfComponent(tag = @Tag(name = "param", handlerClass = ParameterHandler.class, generate = false, type = TagType.Facelets))
public abstract class AbstractParameter extends UIParameter implements ActionListener, JavaScriptParameter {
    public static final String COMPONENT_TYPE = "org.richfaces.Parameter";
    public static final String COMPONENT_FAMILY = UIParameter.COMPONENT_FAMILY;
    private static final String ASSIGN_TO = "assignTo";

    /** ********************************************************* */
    /**
     * Converter for update value with this parameter
     */
    private Converter converter = null;

    @Attribute(hidden = true)
    public abstract boolean isRendered();

    /**
     * If set to true, the value will not enclosed within single quotes and there will be no escaping of characters.
     * This allows the use of the value as JavaScript code for calculating value on the client-side.
     * This doesn't work with non-AJAX components.
     */
    @Attribute
    public abstract boolean isNoEscape();

    /**
     * EL expression for updatable bean property. This property will be updated if the parent command component performs an actionEvent.
     */
    @Attribute
    public abstract Object getAssignTo();

    public abstract void setNoEscape(boolean noEscape);

    public void setAssignToExpression(ValueExpression ve) {
        setValueExpression(ASSIGN_TO, ve);
    }

    public ValueExpression getAssignToExpression() {
        return getValueExpression(ASSIGN_TO);
    }

    public void setConverter(Converter converter) {
        clearInitialState();

        this.converter = converter;
    }

    /**
     * The converter attribute can be used to specify how to convert the value before it is submitted to the data model.
     */
    @Attribute(generate = false)
    public Converter getConverter() {
        return converter;
    }

    public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
        FacesContext context = getFacesContext();
        ELContext elContext = context.getELContext();
        ValueExpression updateBinding = getAssignToExpression();

        if (updateBinding != null && (!updateBinding.isReadOnly(elContext))) {
            String requestValue = context.getExternalContext().getRequestParameterMap().get(getName());

            Object convertedValue = requestValue;

            if (requestValue != null) {
                Class<?> type = updateBinding.getType(elContext);
                Converter converter = createConverter(context, type);

                if (null != converter) {
                    convertedValue = converter.getAsObject(context, this, requestValue);
                }
            }

            if (null != convertedValue) {
                updateBinding.setValue(elContext, convertedValue);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIParameter#getName()
     */

    public String getName() {
        String name = super.getName();

        // If name not set - use clientId. be Careful !
        if (null == name) {
            name = getClientId(FacesContext.getCurrentInstance());
        }

        return name;
    }

    public Object getValue() {
        Object value = super.getValue();

        // TODO - perform conversion if converter is present.
        if (null != value) {
            Class<?> type = value.getClass();
            FacesContext context = getFacesContext();
            Converter converter = createConverter(context, type);

            if (null != converter) {
                value = converter.getAsString(context, this, value);
            }
        }

        return value;
    }

    /** ********************************************************* */
    /**
     * @param context Faces Context
     * @param type Type of class
     * @return converter
     * @throws FacesException if something goes wrong
     */
    private Converter createConverter(FacesContext context, Class<?> type) throws FacesException {
        Converter converter = getConverter();

        if (converter == null && type != null && !type.equals(String.class) && !type.equals(Object.class)) {
            try {
                converter = context.getApplication().createConverter(type);
            } catch (Exception e) {
                throw new FacesException(Messages.getMessage(Messages.NO_CONVERTER_REGISTERED, type.getName()), e);
            }
        }

        return converter;
    }

    @Override
    public void markInitialState() {
        super.markInitialState();

        Converter c = getConverter();
        if (c instanceof PartialStateHolder) {
            ((PartialStateHolder) c).markInitialState();
        }
    }

    @Override
    public void clearInitialState() {
        if (initialStateMarked()) {
            super.clearInitialState();

            Converter c = getConverter();
            if (c instanceof PartialStateHolder) {
                ((PartialStateHolder) c).clearInitialState();
            }
        }
    }

    @Override
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        Object superState = super.saveState(context);
        Object converterState = PartialStateHolderUtil.saveState(context, this, converter);

        if (superState == null && converterState == null) {
            return null;
        }

        return new Object[] { superState, converterState };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (state == null) {
            return;
        }

        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        converter = (Converter) PartialStateHolderUtil.restoreState(context, values[1], converter);
    }
}
