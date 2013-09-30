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
package org.richfaces.ui.iteration.tree.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;

import org.richfaces.ui.common.ComponentPredicates;
import org.richfaces.ui.iteration.tree.TreeModelAdaptor;
import org.richfaces.ui.iteration.tree.TreeModelRecursiveAdaptor;
import org.richfaces.model.SequenceRowKey;
import org.richfaces.model.TreeDataModelTuple;

import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

public class DeclarativeTreeDataModelCompositeTuplesIterator extends ForwardingIterator<TreeDataModelTuple> {
    private UIComponent component;
    private SequenceRowKey key;
    private Iterator<TreeDataModelTuple> iterator;

    public DeclarativeTreeDataModelCompositeTuplesIterator(UIComponent component, SequenceRowKey key) {
        super();
        this.component = component;
        this.key = key;
    }

    @Override
    protected Iterator<TreeDataModelTuple> delegate() {
        if (iterator == null) {
            List<Iterator<TreeDataModelTuple>> list = Lists.newArrayList();

            if (component instanceof TreeModelRecursiveAdaptor) {
                TreeModelRecursiveAdaptor parentRecursiveAdaptor = (TreeModelRecursiveAdaptor) component;

                Object nodes = parentRecursiveAdaptor.getNodes();

                Iterator<TreeDataModelTuple> tuplesIterator = createTuplesIterator(component, nodes);
                if (tuplesIterator != null) {
                    list.add(tuplesIterator);
                }
            }

            if (component.getChildCount() > 0) {
                for (UIComponent child : Iterables.filter(component.getChildren(), ComponentPredicates.isRendered())) {
                    Object nodes = null;

                    if (child instanceof TreeModelRecursiveAdaptor) {
                        TreeModelRecursiveAdaptor treeModelRecursiveAdaptor = (TreeModelRecursiveAdaptor) child;

                        nodes = treeModelRecursiveAdaptor.getRoots();
                    } else if (child instanceof TreeModelAdaptor) {
                        TreeModelAdaptor treeModelAdaptor = (TreeModelAdaptor) child;

                        nodes = treeModelAdaptor.getNodes();
                    }

                    Iterator<TreeDataModelTuple> tuplesIterator = createTuplesIterator(child, nodes);
                    if (tuplesIterator != null) {
                        list.add(tuplesIterator);
                    }
                }
            }

            iterator = Iterators.concat(list.iterator());
        }

        return iterator;
    }

    private Iterator<TreeDataModelTuple> createTuplesIterator(UIComponent component, Object nodes) {
        if (nodes != null) {
            if (nodes instanceof Iterable<?>) {
                Iterable<?> iterable = (Iterable<?>) nodes;

                return new IterableDataTuplesIterator(key, iterable.iterator(), component);
            } else if (nodes instanceof Map<?, ?>) {
                Map<?, ?> map = (Map<?, ?>) nodes;

                return new MapDataTuplesIterator(key, map, component);
            }
        }

        return null;
    }
}