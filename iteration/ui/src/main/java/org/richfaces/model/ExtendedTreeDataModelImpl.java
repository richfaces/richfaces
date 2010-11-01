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
package org.richfaces.model;

import java.util.Iterator;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitResult;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.richfaces.component.TreeRange;

/**
 * @author Nick Belaevski
 * 
 */
public class ExtendedTreeDataModelImpl<E> extends ExtendedDataModel<E> implements TreeDataModel<E> {

    private TreeDataModel<E> wrappedModel;
    
    public ExtendedTreeDataModelImpl(TreeDataModel<E> wrappedModel) {
        super();
        this.wrappedModel = wrappedModel;
    }

    public boolean isDataAvailable() {
        return wrappedModel.isDataAvailable();
    }

    public E getData() {
        return wrappedModel.getData();
    }

    public Iterator<Object> getChildrenRowKeysIterator(Object rowKey) {
        return wrappedModel.getChildrenRowKeysIterator(rowKey);
    }

    public Object getParentRowKey(Object rowKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowKey(Object key) {
        wrappedModel.setRowKey(key);
    }

    @Override
    public Object getRowKey() {
        return wrappedModel.getRowKey();
    }

    protected void walk(FacesContext context, DataVisitor visitor, Range range, Object argument, Iterator<Object> keysIterator) {
        while (keysIterator.hasNext()) {
            Object object = (Object) keysIterator.next();
            
            DataVisitResult visitResult = visitor.process(context, object, argument);
            if (visitResult == DataVisitResult.CONTINUE) {
                if (((TreeRange) range).shouldIterateChildren(object)) {
                    Iterator<Object> childrenIterator = getChildrenRowKeysIterator(object);
                    walk(context, visitor, range, argument, childrenIterator);
                }
            }
        }
    }

    @Override
    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
        TreeRange treeRange = (TreeRange) range;
        if (treeRange.shouldIterateChildren(null)) {
            Iterator<Object> iterator = getChildrenRowKeysIterator(null);
            walk(context, visitor, range, argument, iterator);
        }
    }

    @Override
    public boolean isRowAvailable() {
        return wrappedModel.isDataAvailable();
    }

    @Override
    public int getRowCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E getRowData() {
        return wrappedModel.getData();
    }

    @Override
    public int getRowIndex() {
        throw new UnsupportedOperationException();
    }

    public void setRowIndex(int rowIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getWrappedData() {
        return wrappedModel.getWrappedData();
    }

    @Override
    public void setWrappedData(Object data) {
        wrappedModel.setWrappedData(data);
    }


}
