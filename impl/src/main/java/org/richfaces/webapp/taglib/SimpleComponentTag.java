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

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.el.MethodBinding;

/**
 * @author Maksim Kaszynski
 */
public class SimpleComponentTag extends UIComponentELTagBase {
    private MethodExpression action;
    private MethodExpression actionListener;
    private ValueExpression converter;
    private MethodExpression legacyBinding;
    private MethodExpression methodExpression;
    private ValueExpression title;
    private MethodExpression validator;
    private ValueExpression value;
    private MethodExpression valueChangeListener;

    public SimpleComponentTag() {

        // TODO Auto-generated constructor stub
    }

    @Override
    public String getComponentType() {
        return UIInput.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return "javax.faces.Text";
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        MyUIComponent myUIComponent = (MyUIComponent) component;

        myUIComponent.setValueExpression("value", value);
        setActionListenerProperty(myUIComponent, actionListener);
        setActionProperty(myUIComponent, action);
        setConverterProperty(myUIComponent, converter);
        setValidatorProperty(myUIComponent, validator);

        if (methodExpression != null) {
            myUIComponent.setMethodExpression(methodExpression);
        }

        if (legacyBinding != null) {
            myUIComponent.setLegacyMethodBinding(new MethodBindingMethodExpressionAdaptor(legacyBinding));
        }
    }

    @Override
    public void release() {
        super.release();
    }

    public void setAction(MethodExpression action) {
        this.action = action;
    }

    public MethodExpression getAction() {
        return action;
    }

    public MethodExpression getActionListener() {
        return actionListener;
    }

    public void setActionListener(MethodExpression listener) {
        actionListener = listener;
    }

    public MethodExpression getValueChangeListener() {
        return valueChangeListener;
    }

    public void setValueChangeListener(MethodExpression changeListener) {
        valueChangeListener = changeListener;
    }

    public MethodExpression getValidator() {
        return validator;
    }

    public void setValidator(MethodExpression validator) {
        this.validator = validator;
    }

    public ValueExpression getConverter() {
        return converter;
    }

    public void setConverter(ValueExpression converter) {
        this.converter = converter;
    }

    public ValueExpression getTitle() {
        return title;
    }

    public void setTitle(ValueExpression title) {
        this.title = title;
    }

    public ValueExpression getValue() {
        return value;
    }

    public void setValue(ValueExpression value) {
        this.value = value;
    }

    static class MyUIComponent extends UIComponentBase {
        private MethodBinding legacyMethodBinding;
        private MethodExpression methodExpression;

        @Override
        public String getFamily() {

            // TODO Auto-generated method stub
            return null;
        }

        public MethodBinding getLegacyMethodBinding() {
            return legacyMethodBinding;
        }

        public void setLegacyMethodBinding(MethodBinding legacyMethodBinding) {
            this.legacyMethodBinding = legacyMethodBinding;
        }

        public MethodExpression getMethodExpression() {
            return methodExpression;
        }

        public void setMethodExpression(MethodExpression methodExpression) {
            this.methodExpression = methodExpression;
        }
    }
}
