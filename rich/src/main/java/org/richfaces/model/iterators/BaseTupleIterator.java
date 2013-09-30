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
package org.richfaces.model.iterators;

import javax.faces.component.UIComponent;

import org.richfaces.model.DeclarativeModelKey;
import org.richfaces.model.DeclarativeTreeDataModelTuple;
import org.richfaces.model.SequenceRowKey;
import org.richfaces.model.TreeDataModelTuple;

import com.google.common.collect.UnmodifiableIterator;

/**
 * @author Nick Belaevski
 *
 */
public abstract class BaseTupleIterator extends UnmodifiableIterator<TreeDataModelTuple> {
    private SequenceRowKey baseKey;
    private UIComponent component;
    private Object rowKey;
    private Object data;

    public BaseTupleIterator(SequenceRowKey baseKey) {
        this(baseKey, null);
    }

    public BaseTupleIterator(SequenceRowKey baseKey, UIComponent component) {
        super();
        this.baseKey = baseKey;
        this.component = component;
    }

    protected abstract void proceedToNext();

    protected UIComponent getComponent() {
        return component;
    }

    protected SequenceRowKey getBaseKey() {
        return baseKey;
    }

    protected void setKeyAndData(Object rowKey, Object data) {
        this.rowKey = rowKey;
        this.data = data;
    }

    protected Object getKey() {
        return rowKey;
    }

    public final TreeDataModelTuple next() {
        proceedToNext();

        Object modelKey = getWrappedKey();
        SequenceRowKey nextKey = getCompositeKey(modelKey);
        return createTuple(nextKey);
    }

    protected TreeDataModelTuple createTuple(SequenceRowKey key) {
        TreeDataModelTuple result;

        if (component != null) {
            result = new DeclarativeTreeDataModelTuple(key, data, component);
        } else {
            result = new TreeDataModelTuple(key, data);
        }

        return result;
    }

    protected Object getWrappedKey() {
        Object modelKey;

        if (getComponent() != null) {
            modelKey = new DeclarativeModelKey(getComponent().getId(), getKey());
        } else {
            modelKey = getKey();
        }

        return modelKey;
    }

    protected SequenceRowKey getCompositeKey(Object modelKey) {
        SequenceRowKey result;

        if (getBaseKey() != null) {
            result = getBaseKey().append(modelKey);
        } else {
            result = new SequenceRowKey(modelKey);
        }

        return result;
    }
}