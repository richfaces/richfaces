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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;

import org.richfaces.component.AbstractTree;
import org.richfaces.component.ComponentPredicates;
import org.richfaces.component.TreeModelAdaptor;
import org.richfaces.component.TreeModelRecursiveAdaptor;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.base.Predicates;
import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 * 
 */
public class DeclarativeTreeDataModelImpl extends TreeSequenceKeyModel<DeclarativeModelKey, Object> {

    private static final Logger LOGGER = RichfacesLogger.MODEL.getLogger();
    
    private final class DeclarativeModelIterator implements Iterator<TreeDataModelTuple> {
        
        private UIComponent component;

        private SequenceRowKey<DeclarativeModelKey> baseKey;
        
        private int counter = 0;
        
        private Iterator<?> nodesIterator;
        
        public DeclarativeModelIterator(UIComponent component, SequenceRowKey<DeclarativeModelKey> baseKey, Iterator<?> nodesIterator) {
            super();
            this.component = component;
            this.baseKey = baseKey;
            this.nodesIterator = nodesIterator;
        }

        public TreeDataModelTuple next() {
            Object nextNode = nodesIterator.next();
            DeclarativeModelKey key = new DeclarativeModelKey(component.getId(), counter++);
            
            SequenceRowKey<DeclarativeModelKey> newKey = baseKey.append(key);
            setCurrentState(newKey, nextNode, component);
            
            return new TreeDataModelTuple(newKey, nextNode);
        }
        
        public boolean hasNext() {
            return nodesIterator.hasNext();
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }
    
    private final class DeclarativeModelCompositeIterator extends ForwardingIterator<TreeDataModelTuple> {
        
        private UIComponent component;

        private SequenceRowKey<DeclarativeModelKey> key;
        
        private Iterator<TreeDataModelTuple> iterator;
        
        public DeclarativeModelCompositeIterator(UIComponent component, SequenceRowKey<DeclarativeModelKey> key) {
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
                    
                    Collection<?> nodes = (Collection<?>) parentRecursiveAdaptor.getNodes();
                    
                    if (nodes != null) {
                        list.add(new DeclarativeModelIterator(component, key, nodes.iterator()));
                    }
                }

                if (component.getChildCount() > 0) {
                    for (UIComponent child : Iterables.filter(component.getChildren(), ComponentPredicates.isRendered())) {
                        Collection<?> nodes = null;

                        if (child instanceof TreeModelRecursiveAdaptor) {
                            TreeModelRecursiveAdaptor treeModelRecursiveAdaptor = (TreeModelRecursiveAdaptor) child;
                            
                            nodes = (Collection<?>) treeModelRecursiveAdaptor.getRoots();
                        } else if (child instanceof TreeModelAdaptor) {
                            TreeModelAdaptor treeModelAdaptor = (TreeModelAdaptor) child;
                            
                            nodes = (Collection<?>) treeModelAdaptor.getNodes();
                        }

                        if (nodes != null) {
                            list.add(new DeclarativeModelIterator(child, key, nodes.iterator()));
                        }
                    }
                }
                
                iterator = Iterators.concat(list.iterator());
            }
            
            return iterator;
        }
        
    }
    
    private String var;
    
    private Map<String, Object> contextMap;
    
    private UIComponent tree;
    
    private UIComponent currentComponent;
    
    public DeclarativeTreeDataModelImpl(AbstractTree tree, String var, Map<String, Object> contextMap) {
        super();
        this.tree = tree;
        this.currentComponent = tree;
        this.var = var;
        this.contextMap = contextMap;
    }

    protected UIComponent getCurrentComponent() {
        return currentComponent;
    }

    protected void setCurrentState(SequenceRowKey<DeclarativeModelKey> key, Object data, UIComponent currentComponent) {
        setRowKeyAndData(key, data);
        this.currentComponent = currentComponent;
    }
    
    public boolean isLeaf() {
        if (currentComponent instanceof TreeModelRecursiveAdaptor) {
            return false;
        }

        if (currentComponent.getChildCount() == 0) {
            return true;
        }
        
        return Iterables.contains(currentComponent.getChildren(), Predicates.instanceOf(TreeModelAdaptor.class));
    }

    /* (non-Javadoc)
     * @see org.richfaces.model.TreeDataModel#children()
     */
    public Iterator<TreeDataModelTuple> children() {
        return new DeclarativeModelCompositeIterator(currentComponent, safeGetRowKey());
    }

    /* (non-Javadoc)
     * @see org.richfaces.model.TreeDataModel#getParentRowKey(java.lang.Object)
     */
    public Object getParentRowKey(Object rowKey) {
        // TODO Auto-generated method stub
        return null;
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
    protected void walkKey(SequenceRowKey<DeclarativeModelKey> key) {
        Object initialContextValue = null;

        if (var != null) {
            initialContextValue = contextMap.remove(var);
        }
        
        try {
            this.currentComponent = tree;

            super.walkKey(key);
        } finally {
            if (var != null) {
                try {
                    contextMap.put(var, initialContextValue);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }
    
    @Override
    protected void walkNext(DeclarativeModelKey segment) {
        String modelId = segment.getModelId();
        
        UIComponent modelComponent;

        if (currentComponent instanceof TreeModelRecursiveAdaptor && modelId.equals(currentComponent.getId())) {
            modelComponent = currentComponent;
        } else {
            modelComponent = Iterables.find(currentComponent.getChildren(), ComponentPredicates.withId(modelId));
        }

        Object nodes = null;
        
        if (modelComponent instanceof TreeModelRecursiveAdaptor) {
            TreeModelRecursiveAdaptor recursiveAdaptor = (TreeModelRecursiveAdaptor) modelComponent;
            
            if (currentComponent.equals(modelComponent)) {
                nodes = recursiveAdaptor.getNodes();
            } else {
                nodes = recursiveAdaptor.getRoots();
            }
        } else {
            nodes = ((TreeModelAdaptor) modelComponent).getNodes();
        }

        Object data = Iterables.get((Iterable<?>) nodes, (Integer) segment.getModelKey());
        setCurrentState(safeGetRowKey().append(segment), data, modelComponent);

        if (var != null) {
            contextMap.put(var, data);
        }
    }
    
}
