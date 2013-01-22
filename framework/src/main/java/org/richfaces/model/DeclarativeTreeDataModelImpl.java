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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.richfaces.component.AbstractTree;
import org.richfaces.component.DeclarativeTreeDataModelWalker;
import org.richfaces.component.TreeModelAdaptor;
import org.richfaces.component.TreeModelRecursiveAdaptor;
import org.richfaces.convert.DeclarativeModelSequenceKeyConverter;
import org.richfaces.model.iterators.DeclarativeTreeDataModelCompositeTuplesIterator;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 * @author Nick Belaevski
 *
 */
public class DeclarativeTreeDataModelImpl extends TreeSequenceKeyModel<Object> implements DeclarativeTreeModel<Object> {
    private static final Converter DEFAULT_CONVERTER = new DeclarativeModelSequenceKeyConverter();
    private static final Predicate<Object> TREE_MODEL_ADAPTOR_INSTANCE_PREDICATE = Predicates
        .instanceOf(TreeModelAdaptor.class);
    private AbstractTree tree;
    private UIComponent currentComponent;

    public DeclarativeTreeDataModelImpl(AbstractTree tree) {
        this.tree = tree;
        this.currentComponent = tree;
    }

    public UIComponent getCurrentComponent() {
        return currentComponent;
    }

    public boolean isLeaf() {
        UIComponent currentComponent = getCurrentComponent();

        TreeModelAdaptor adaptor = (TreeModelAdaptor) currentComponent;

        if (adaptor.isLeaf()) {
            return true;
        }

        if (adaptor instanceof TreeModelRecursiveAdaptor) {
            return false;
        }

        if (currentComponent.getChildCount() == 0) {
            return true;
        }

        return !Iterables.any(currentComponent.getChildren(), TREE_MODEL_ADAPTOR_INSTANCE_PREDICATE);
    }

    public Iterator<TreeDataModelTuple> children() {
        return new DeclarativeTreeDataModelCompositeTuplesIterator(currentComponent, getRowKey());
    }

    @Override
    public Object getWrappedData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWrappedData(Object data) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void setupKey(SequenceRowKey key) {
        setRowKeyAndData(null, null);
        this.currentComponent = tree;

        if (key != null) {
            DeclarativeTreeDataModelWalker walker = new DeclarativeTreeDataModelWalker(FacesContext.getCurrentInstance(), tree);
            walker.walk(key);

            setRowKeyAndData(key, walker.getData());
            this.currentComponent = walker.getCurrentComponent();
        }
    }

    public TreeDataModelTuple createSnapshot() {
        return new DeclarativeTreeDataModelTuple(getRowKey(), getData(), getCurrentComponent());
    }

    public void restoreFromSnapshot(TreeDataModelTuple tuple) {
        DeclarativeTreeDataModelTuple declarativeModelTuple = (DeclarativeTreeDataModelTuple) tuple;

        setRowKeyAndData((SequenceRowKey) tuple.getRowKey(), tuple.getData());
        this.currentComponent = declarativeModelTuple.getComponent();
    }

    public Converter getRowKeyConverter() {
        return DEFAULT_CONVERTER;
    }
}
