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
package org.richfaces.component;

import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.PartialStateHolder;
import javax.faces.component.StateHelper;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.convert.ConverterUtil;

/**
 * <p>The &lt;rich:treeModelAdaptor&gt; component takes an object which implements the Map or Iterable interfaces. It
 * adds all the object entries to the parent node as child nodes.</p>
 *
 * @author Nick Belaevski
 */
public abstract class AbstractTreeModelAdaptor extends UIComponentBase {
    private enum PropertyKeys {
        rowKeyConverter,
        rowKeyConverterSet
    }

    private Converter rowKeyConverter;

    @Attribute
    public Converter getRowKeyConverter() {
        if (this.rowKeyConverter != null) {
            return this.rowKeyConverter;
        }

        ValueExpression converterExpression = getValueExpression(PropertyKeys.rowKeyConverter.toString());
        if (converterExpression != null) {
            return (Converter) converterExpression.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setRowKeyConverter(Converter converter) {
        StateHelper stateHelper = getStateHelper();
        stateHelper.put(PropertyKeys.rowKeyConverterSet, Boolean.TRUE);

        this.rowKeyConverter = converter;
    }

    protected void memoizeDefaultRowKeyConverter(Object value) {
        if (isSetRowKeyConverter()) {
            return;
        }

        if (value instanceof Iterable<?>) {
            setRowKeyConverter(ConverterUtil.integerConverter());
        } else if (value instanceof Map<?, ?>) {
            setRowKeyConverter(ConverterUtil.stringConverter());
        }
    }

    private boolean isSetRowKeyConverter() {
        return isLocalRowKeyConverterSet() || getValueExpression(PropertyKeys.rowKeyConverter.toString()) != null;
    }

    private boolean isLocalRowKeyConverterSet() {
        Boolean value = (Boolean) getStateHelper().get(PropertyKeys.rowKeyConverterSet);
        return Boolean.TRUE.equals(value);
    }

    @Override
    public void markInitialState() {
        super.markInitialState();

        if (rowKeyConverter instanceof PartialStateHolder) {
            ((PartialStateHolder) rowKeyConverter).markInitialState();
        }
    }

    @Override
    public void clearInitialState() {
        super.clearInitialState();

        if (rowKeyConverter instanceof PartialStateHolder) {
            ((PartialStateHolder) rowKeyConverter).clearInitialState();
        }
    }

    @Override
    public void restoreState(FacesContext context, Object stateObject) {
        Object[] state = (Object[]) stateObject;

        super.restoreState(context, state[0]);

        boolean converterHasPartialState = Boolean.TRUE.equals(state[1]);
        Object savedConverterState = state[2];

        if (converterHasPartialState) {
            ((StateHolder) rowKeyConverter).restoreState(context, savedConverterState);
        } else {
            rowKeyConverter = (Converter) UIComponentBase.restoreAttachedState(context, savedConverterState);
        }
    }

    @Override
    public Object saveState(FacesContext context) {
        Object parentState = super.saveState(context);

        Object converterState = null;
        boolean nullDelta = true;

        boolean converterHasPartialState = false;

        if (initialStateMarked()) {
            if (!isLocalRowKeyConverterSet() && rowKeyConverter != null && rowKeyConverter instanceof PartialStateHolder) {
                // Delta
                StateHolder holder = (StateHolder) rowKeyConverter;
                if (!holder.isTransient()) {
                    Object attachedState = holder.saveState(context);
                    if (attachedState != null) {
                        nullDelta = false;
                        converterState = attachedState;
                    }
                    converterHasPartialState = true;
                } else {
                    converterState = null;
                }
            } else if (isLocalRowKeyConverterSet() || rowKeyConverter != null) {
                // Full
                converterState = saveAttachedState(context, rowKeyConverter);
                nullDelta = false;
            }

            if (parentState == null && nullDelta) {
                // No values
                return null;
            }
        } else {
            converterState = saveAttachedState(context, rowKeyConverter);
        }

        return new Object[] { parentState, converterHasPartialState, converterState };
    }
}
