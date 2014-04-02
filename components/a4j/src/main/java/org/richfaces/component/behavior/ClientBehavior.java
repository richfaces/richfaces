/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.component.behavior;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anton Belevich
 *
 *         base class for the client behaviors
 */
public abstract class ClientBehavior extends ClientBehaviorBase {
    private StateHelper behaviorStateHelper = null;
    private Map<String, ValueExpression> bindings;

    public StateHelper getStateHelper() {
        if (behaviorStateHelper == null) {
            behaviorStateHelper = new BehaviorStateHelper(this);
        }
        return behaviorStateHelper;
    }

    public ValueExpression getValueExpression(String name) {
        if (name == null) {
            throw new NullPointerException();
        }

        return ((bindings == null) ? null : bindings.get(name));
    }

    public void setValueExpression(String name, ValueExpression valueExpression) {
        if (name == null) {
            throw new NullPointerException();
        }

        if (valueExpression != null) {

            if (valueExpression.isLiteralText()) {
                setLiteralValue(name, valueExpression);
            } else {
                if (bindings == null) {
                    bindings = new HashMap<String, ValueExpression>(6, 1.0f);
                }
                bindings.put(name, valueExpression);
            }
        } else {
            if (bindings != null) {
                bindings.remove(name);
                if (bindings.isEmpty()) {
                    bindings = null;
                }
            }
        }
        clearInitialState();
    }

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public void setLiteralValue(String name, ValueExpression expression) {
        assert (expression.isLiteralText());
        ELContext context = getFacesContext().getELContext();

        try {
            setLiteralAttribute(name, expression.getValue(context));
        } catch (ELException elem) {
            throw new FacesException(elem);
        }
    }

    public abstract void setLiteralAttribute(String name, Object value);

    @Override
    public Object saveState(FacesContext context) {
        Object[] state = null;

        Object parentState = super.saveState(context);
        if (initialStateMarked()) {
            if (parentState != null) {
                state = new Object[] { parentState };
            }
        } else {

            Object savedHelper = null;
            if (behaviorStateHelper != null) {
                savedHelper = behaviorStateHelper.saveState(context);
            }

            state = new Object[3];
            state[0] = parentState;
            state[1] = savedHelper;
            state[2] = saveBindings(context, bindings);
        }
        return state;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        if (state != null) {
            Object[] stateObject = (Object[]) state;
            super.restoreState(context, stateObject[0]);

            if (stateObject.length == 3) {
                getStateHelper().restoreState(context, stateObject[1]);
                this.bindings = restoreBindings(context, stateObject[2]);

                clearInitialState();
            }
        }
    }

    // Utility for saving bindings state
    private static Object saveBindings(FacesContext context, Map<String, ValueExpression> bindings) {

        if (bindings == null) {
            return (null);
        }

        Object[] values = new Object[2];
        values[0] = bindings.keySet().toArray(new String[bindings.size()]);

        Object[] bindingValues = bindings.values().toArray();
        for (int i = 0; i < bindingValues.length; i++) {
            bindingValues[i] = UIComponentBase.saveAttachedState(context, bindingValues[i]);
        }

        values[1] = bindingValues;

        return (values);
    }

    // Utility for restoring bindings from state
    private static Map<String, ValueExpression> restoreBindings(FacesContext context, Object state) {
        if (state == null) {
            return (null);
        }

        Object[] values = (Object[]) state;
        String[] names = (String[]) values[0];
        Object[] states = (Object[]) values[1];
        Map<String, ValueExpression> bindings = new HashMap<String, ValueExpression>();
        for (int i = 0; i < names.length; i++) {
            bindings.put(names[i], (ValueExpression) UIComponentBase.restoreAttachedState(context, states[i]));
        }
        return (bindings);
    }

    protected boolean compare(Serializable key, String name) {
        boolean retValue = false;
        if (key != null) {
            String keyName = key.toString();
            retValue = keyName.equals(name);
        }
        return retValue;
    }
}
