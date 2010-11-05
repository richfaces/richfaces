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

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.swing.tree.TreeNode;

import org.ajax4jsf.model.DataComponentState;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.convert.SequenceRowKeyConverter;
import org.richfaces.event.TreeSelectionEvent;
import org.richfaces.event.TreeSelectionListener;
import org.richfaces.model.ExtendedTreeDataModelImpl;
import org.richfaces.model.SwingTreeNodeDataModelImpl;
import org.richfaces.model.TreeDataModel;
import org.richfaces.renderkit.MetaComponentRenderer;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

/**
 * @author Nick Belaevski
 * 
 */
@JsfComponent(
    type = AbstractTree.COMPONENT_TYPE,
    family = AbstractTree.COMPONENT_FAMILY, 
    tag = @Tag(name = "tree"),
    renderer = @JsfRenderer(type = "org.richfaces.TreeRenderer")
)
public abstract class AbstractTree extends UIDataAdaptor implements MetaComponentResolver, MetaComponentEncoder {

    public static final String COMPONENT_TYPE = "org.richfaces.Tree";

    public static final String COMPONENT_FAMILY = "org.richfaces.Tree";

    public static final String SELECTION_META_COMPONENT_ID = "selection";
    
    private static final class MatchingTreeNodePredicate implements Predicate<UIComponent> {
        
        private String type;

        public MatchingTreeNodePredicate(String type) {
            super();
            this.type = type;
        }

        public boolean apply(UIComponent input) {
            if (!(input instanceof AbstractTreeNode)) {
                return false;
            }
            
            if (!input.isRendered()) {
                return false;
            }
            
            String nodeType = ((AbstractTreeNode) input).getType();
            if (type == null && nodeType == null) {
                return true;
            }
            
            return type != null && type.equals(nodeType);
        }
    };
    
    private static final Converter ROW_KEY_CONVERTER = new SequenceRowKeyConverter();

    /**
     * @author Nick Belaevski
     * 
     */
    private final class TreeComponentState implements DataComponentState {
        public Range getRange() {
            return new TreeRange(getFacesContext(), AbstractTree.this);
        }
    }

    private enum PropertyKeys {
        selection
    }

    public AbstractTree() {
        setKeepSaved(true);
        setRendererType("org.richfaces.TreeRenderer");
    }

    public abstract Object getValue();

    public abstract boolean isImmediate();

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Attribute(defaultValue = "SwitchType.DEFAULT")
    public abstract SwitchType getToggleType();

    @Attribute(defaultValue = "SwitchType.client")
    public abstract SwitchType getSelectionType();
    
    public abstract String getNodeType();
    
    //TODO - move to template
    public abstract String getStyleClass();
    
    public Collection<Object> getSelection() {
        @SuppressWarnings("unchecked")
        Collection<Object> selection = (Collection<Object>) getStateHelper().eval(PropertyKeys.selection);
        if (selection == null) {
            selection = new HashSet<Object>();
            
            ValueExpression ve = getValueExpression(PropertyKeys.selection.toString());
            if (ve != null) {
                ve.setValue(getFacesContext().getELContext(), selection);
            } else {
                getStateHelper().put(PropertyKeys.selection, selection);
            }
        }
        
        return selection;
    }
    
    public void setSelection(Collection<Object> selection) {
        getStateHelper().put(PropertyKeys.selection, selection);
    }
    
    @Override
    protected ExtendedDataModel<?> createExtendedDataModel() {
        ExtendedTreeDataModelImpl<?> model = new ExtendedTreeDataModelImpl<TreeNode>(new SwingTreeNodeDataModelImpl());
        model.setWrappedData(getValue());
        return model;
    }

    @Override
    protected DataComponentState createComponentState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Converter getRowKeyConverter() {
        Converter converter = super.getRowKeyConverter();
        if (converter == null) {
            converter = ROW_KEY_CONVERTER;
        }
        return converter;
    }

    public Iterator<Object> getChildrenRowKeysIterator(FacesContext faces, Object rowKey) {
        TreeDataModel<?> dataModel = (TreeDataModel<?>) getExtendedDataModel();
        return dataModel.getChildrenRowKeysIterator(rowKey);
    }

    public AbstractTreeNode findTreeNodeComponent() {
        if (getChildCount() == 0) {
            return null;
        }

        Iterator<UIComponent> iterator = Iterators.filter(getChildren().iterator(), 
            new MatchingTreeNodePredicate(getNodeType()));
        
        if (iterator.hasNext()) {
            return (AbstractTreeNode) iterator.next();
        }

        return null;
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        if (event instanceof TreeSelectionEvent) {
            TreeSelectionEvent selectionEvent = (TreeSelectionEvent) event;
            
            final Collection<Object> newSelection = selectionEvent.getNewSelection();

            Collection<Object> selectionCollection = getSelection();
            
            Iterables.removeIf(selectionCollection, new Predicate<Object>() {
                public boolean apply(Object input) {
                    return !newSelection.contains(input);
                };
            });
            
            if (!newSelection.isEmpty()) {
                Iterables.addAll(selectionCollection, newSelection);
            }
        }
    }
    
    @Override
    protected boolean visitFixedChildren(VisitContext visitContext, VisitCallback callback) {
        if (visitContext instanceof ExtendedVisitContext) {
            ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) visitContext;
            
            if (ExtendedVisitContextMode.RENDER == extendedVisitContext.getVisitMode()) {
                VisitResult result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback, SELECTION_META_COMPONENT_ID);
                if (result != VisitResult.ACCEPT) {
                    return result == VisitResult.COMPLETE;
                }
            }
        }

        return super.visitFixedChildren(visitContext, callback);
    }
    
    void decodeMetaComponent(FacesContext context, String metaComponentId) {
        ((MetaComponentRenderer) getRenderer(context)).decodeMetaComponent(context, this, metaComponentId);
    }

    public void encodeMetaComponent(FacesContext context, String metaComponentId) throws IOException {
        ((MetaComponentRenderer) getRenderer(context)).encodeMetaComponent(context, this, metaComponentId);
    }
    
    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (SELECTION_META_COMPONENT_ID.equals(metaComponentId)) {
            return getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + metaComponentId;
        }
        
        return null;
    }
    
    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent,
        String metaComponentId) {

        return null;
    }

    @Override
    protected Iterator<UIComponent> dataChildren() {
        AbstractTreeNode treeNodeComponent = findTreeNodeComponent();
        if (treeNodeComponent != null) {
            return Iterators.<UIComponent>singletonIterator(treeNodeComponent);
        } else {
            return Iterators.<UIComponent>emptyIterator();
        }
    }
    
    @Override
    public DataComponentState getComponentState() {
        return new TreeComponentState();
    }
    
    public void addSelectionListener(TreeSelectionListener listener) {
        addFacesListener(listener);
    }

    public TreeSelectionListener[] getSelectionListeners() {
        return (TreeSelectionListener[]) getFacesListeners(TreeSelectionListener.class);
    }
    
    public void removeSelectionListener(TreeSelectionListener listener) {
        removeFacesListener(listener);
    }

    @Attribute(hidden = true)
    public boolean isExpanded() {
        if (getRowKey() == null) {
            return true;
        }
        
        AbstractTreeNode treeNode = findTreeNodeComponent();
        if (treeNode == null) {
            return false;
        }
        
        return treeNode.isExpanded();
    }
}
