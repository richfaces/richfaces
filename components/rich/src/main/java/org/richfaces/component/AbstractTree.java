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
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UpdateModelException;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.ajax4jsf.model.DataComponentState;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.richfaces.application.FacesMessages;
import org.richfaces.application.MessageFactory;
import org.richfaces.application.ServiceTracker;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.AjaxProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.component.attribute.ImmediateProps;
import org.richfaces.component.attribute.SequenceProps;
import org.richfaces.component.attribute.TreeCommonProps;
import org.richfaces.component.attribute.TreeProps;
import org.richfaces.component.util.MessageUtil;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.event.TreeSelectionChangeEvent;
import org.richfaces.event.TreeSelectionChangeListener;
import org.richfaces.event.TreeSelectionChangeSource;
import org.richfaces.event.TreeToggleEvent;
import org.richfaces.event.TreeToggleListener;
import org.richfaces.event.TreeToggleSource;
import org.richfaces.model.ClassicTreeNodeDataModelImpl;
import org.richfaces.model.DeclarativeModelKey;
import org.richfaces.model.DeclarativeTreeDataModelImpl;
import org.richfaces.model.DeclarativeTreeModel;
import org.richfaces.model.SwingTreeNodeDataModelImpl;
import org.richfaces.model.TreeDataModel;
import org.richfaces.model.TreeDataModelTuple;
import org.richfaces.model.TreeDataVisitor;
import org.richfaces.model.TreeNode;
import org.richfaces.renderkit.MetaComponentRenderer;
import org.richfaces.view.facelets.TreeHandler;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;

/**
 * <p>The &lt;rich:tree&gt; component provides a hierarchical tree control. Each &lt;rich:tree&gt; component typically
 * consists of &lt;rich:treeNode&gt; child components. The appearance and behavior of the tree and its nodes can be
 * fully customized.</p>
 *
 * @author Nick Belaevski
 */
@JsfComponent(type = AbstractTree.COMPONENT_TYPE, family = AbstractTree.COMPONENT_FAMILY,
        tag = @Tag(name = "tree", handlerClass = TreeHandler.class),
        renderer = @JsfRenderer(type = "org.richfaces.TreeRenderer"))
// TODO add rowData caching for wrapper events
public abstract class AbstractTree extends UIDataAdaptor implements MetaComponentResolver, MetaComponentEncoder, TreeSelectionChangeSource, TreeToggleSource, AjaxProps, CoreProps, EventsKeyProps, EventsMouseProps, ImmediateProps, I18nProps, SequenceProps, TreeProps, TreeCommonProps {
    public static final String COMPONENT_TYPE = "org.richfaces.Tree";
    public static final String COMPONENT_FAMILY = "org.richfaces.Tree";
    public static final String SELECTION_META_COMPONENT_ID = "selection";
    public static final String DEFAULT_TREE_NODE_ID = "__defaultTreeNode";
    public static final String DEFAULT_TREE_NODE_FACET_NAME = "defaultNode";
    private static final String COMPONENT_FOR_MODEL_UNAVAILABLE = "Component is not available for model {0}";
    private static final String CONVERTER_FOR_MODEL_UNAVAILABLE = "Row key converter is not available for model {0}";

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

            String nodeType = ((AbstractTreeNode) input).getType();
            if (type == null && nodeType == null) {
                return true;
            }

            return type != null && type.equals(nodeType);
        }
    }

    ;

    private enum PropertyKeys {
        selection
    }

    private transient TreeRange treeRange;
    private transient UIComponent currentComponent = this;
    private transient Map<String, UIComponent> declatariveModelsMap = null;

    public AbstractTree() {
        setKeepSaved(true);
        setRendererType("org.richfaces.TreeRenderer");
    }

    protected TreeRange getTreeRange() {
        if (treeRange == null) {
            treeRange = new TreeRange(this);
        }

        return treeRange;
    }

    /**
     * Points to the data model
     */
    @Attribute
    public abstract Object getValue();

    @Attribute
    public abstract String getNodeClass();

    @Attribute(events = @EventName("nodetoggle"))
    public abstract String getOnnodetoggle();

    @Attribute(events = @EventName("beforenodetoggle"))
    public abstract String getOnbeforenodetoggle();

    @Attribute(events = @EventName("selectionchange"))
    public abstract String getOnselectionchange();

    @Attribute(events = @EventName("beforeselectionchange"))
    public abstract String getOnbeforeselectionchange();

    @Attribute
    public abstract SwitchType getToggleType();

    @Attribute
    public abstract SwitchType getSelectionType();

    @Attribute
    public abstract String getNodeType();

    @Attribute
    public abstract String getToggleNodeEvent();

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Attribute
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
    protected DataComponentState createComponentState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Attribute
    public Converter getRowKeyConverter() {
        Converter converter = super.getRowKeyConverter();
        if (converter == null) {
            converter = getTreeDataModel().getRowKeyConverter();
        }
        return converter;
    }

    protected Iterator<UIComponent> findMatchingTreeNodeComponent(String nodeType, UIComponent parentComponent) {
        Iterator<UIComponent> children = parentComponent.getChildren().iterator();
        if (parentComponent != this) {
            children = Iterators.concat(children, this.getChildren().iterator());
        }

        return Iterators.filter(children, new MatchingTreeNodePredicate(nodeType));
    }

    protected void setupCurrentComponent() {
        ExtendedDataModel<?> dataModel = getExtendedDataModel();
        if (dataModel instanceof DeclarativeTreeModel) {
            currentComponent = ((DeclarativeTreeModel) dataModel).getCurrentComponent();
        } else {
            currentComponent = this;
        }
    }

    public AbstractTreeNode findTreeNodeComponent() {
        String nodeType = getNodeType();

        Iterator<UIComponent> nodesItr = findMatchingTreeNodeComponent(nodeType, currentComponent);
        if (nodesItr.hasNext()) {
            while (nodesItr.hasNext()) {

                AbstractTreeNode node = (AbstractTreeNode) nodesItr.next();

                if (node.isRendered()) {
                    return node;
                }
            }
        } else if (Strings.isNullOrEmpty(nodeType) || isUseDefaultNode()) {
            return (AbstractTreeNode) getFacet(DEFAULT_TREE_NODE_FACET_NAME);
        }

        return null;
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        if (event instanceof TreeSelectionChangeEvent) {
            TreeSelectionChangeEvent selectionEvent = (TreeSelectionChangeEvent) event;

            final Collection<Object> newSelection = selectionEvent.getNewSelection();

            Collection<Object> selectionCollection = getSelection();

            Iterables.removeIf(selectionCollection, new Predicate<Object>() {
                public boolean apply(Object input) {
                    return !newSelection.contains(input);
                }

                ;
            });

            if (!newSelection.isEmpty()) {
                Iterables.addAll(selectionCollection, newSelection);
            }
        } else if (event instanceof TreeToggleEvent) {
            TreeToggleEvent toggleEvent = (TreeToggleEvent) event;
            AbstractTreeNode treeNodeComponent = findTreeNodeComponent();

            boolean newExpandedValue = toggleEvent.isExpanded();

            FacesContext context = getFacesContext();
            ValueExpression expression = treeNodeComponent
                    .getValueExpression(AbstractTreeNode.PropertyKeys.expanded.toString());
            if (expression != null) {
                ELContext elContext = context.getELContext();
                Exception caught = null;
                FacesMessage message = null;
                try {
                    expression.setValue(elContext, newExpandedValue);
                } catch (ELException e) {
                    caught = e;
                    String messageStr = e.getMessage();
                    Throwable result = e.getCause();
                    while (null != result && result.getClass().isAssignableFrom(ELException.class)) {
                        messageStr = result.getMessage();
                        result = result.getCause();
                    }
                    if (null == messageStr) {
                        MessageFactory messageFactory = ServiceTracker.getService(MessageFactory.class);
                        message = messageFactory.createMessage(context, FacesMessages.UIINPUT_UPDATE,
                                MessageUtil.getLabel(context, this));
                    } else {
                        message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageStr, messageStr);
                    }
                } catch (Exception e) {
                    caught = e;
                    MessageFactory messageFactory = ServiceTracker.getService(MessageFactory.class);
                    message = messageFactory.createMessage(context, FacesMessages.UIINPUT_UPDATE,
                            MessageUtil.getLabel(context, this));
                }
                if (caught != null) {
                    assert (message != null);
                    UpdateModelException toQueue = new UpdateModelException(message, caught);
                    ExceptionQueuedEventContext eventContext = new ExceptionQueuedEventContext(context, toQueue, this,
                            PhaseId.UPDATE_MODEL_VALUES);
                    context.getApplication().publishEvent(context, ExceptionQueuedEvent.class, eventContext);
                }
            } else {
                treeNodeComponent.setExpanded(newExpandedValue);
            }
        }
    }

    @Override
    protected boolean visitFixedChildren(VisitContext visitContext, VisitCallback callback) {
        if (visitContext instanceof ExtendedVisitContext) {
            ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) visitContext;

            if (ExtendedVisitContextMode.RENDER == extendedVisitContext.getVisitMode()) {
                VisitResult result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback,
                        SELECTION_META_COMPONENT_ID);
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

    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {

        return null;
    }

    @Override
    protected Iterator<UIComponent> dataChildren() {
        AbstractTreeNode treeNodeComponent = findTreeNodeComponent();
        if (treeNodeComponent != null) {
            return Iterators.<UIComponent> singletonIterator(treeNodeComponent);
        } else {
            return ImmutableSet.<UIComponent>of().iterator();
        }
    }

    public void addTreeSelectionChangeListener(TreeSelectionChangeListener listener) {
        addFacesListener(listener);
    }

    @Attribute(hidden = true)
    public TreeSelectionChangeListener[] getTreeSelectionChangeListeners() {
        return (TreeSelectionChangeListener[]) getFacesListeners(TreeSelectionChangeListener.class);
    }

    public void removeTreeSelectionChangeListener(TreeSelectionChangeListener listener) {
        removeFacesListener(listener);
    }

    public void addTreeToggleListener(TreeToggleListener listener) {
        addFacesListener(listener);
    }

    @Attribute(hidden = true)
    public TreeToggleListener[] getTreeToggleListeners() {
        return (TreeToggleListener[]) getFacesListeners(TreeToggleListener.class);
    }

    public void removeTreeToggleListener(TreeToggleListener listener) {
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

    // TODO review
    protected TreeDataModel<?> getTreeDataModel() {
        return (TreeDataModel<?>) getExtendedDataModel();
    }

    @Attribute(hidden = true)
    public boolean isLeaf() {
        return getTreeDataModel().isLeaf();
    }

    @Override
    public void walk(final FacesContext faces, final DataVisitor visitor, final Object argument) {
        walkModel(faces, new TreeDataVisitor() {
            public void enterNode() {
                visitor.process(faces, getRowKey(), argument);
            }

            public void exitNode() {
            }

            public void beforeChildrenVisit() {
            }

            public void afterChildrenVisit() {
            }
        });
    }

    @Override
    protected ExtendedDataModel<?> createExtendedDataModel() {
        ExtendedDataModel<?> dataModel;

        Object value = getValue();
        if (value == null) {
            dataModel = new DeclarativeTreeDataModelImpl(this);
        } else if (value instanceof TreeNode) {
            dataModel = new ClassicTreeNodeDataModelImpl();
            dataModel.setWrappedData(value);
        } else if (value instanceof TreeDataModel<?>) {
            if (value instanceof ExtendedDataModel<?>) {
                dataModel = (ExtendedDataModel<?>) value;
            } else {
                throw new IllegalArgumentException(MessageFormat.format(
                        "TreeDataModel implementation {0} is not a subclass of ExtendedDataModel", value.getClass().getName()));
            }
        } else {
            dataModel = new SwingTreeNodeDataModelImpl();
            dataModel.setWrappedData(value);
        }

        return dataModel;
    }

    public void walkModel(FacesContext context, TreeDataVisitor dataVisitor) {
        TreeDataModel<?> model = getTreeDataModel();

        if (!getTreeRange().shouldProcessNode()) {
            return;
        }

        boolean isRootNode = (getRowKey() == null);

        if (!isRootNode) {
            dataVisitor.enterNode();
        }

        walkModelChildren(context, dataVisitor, model);

        if (!isRootNode) {
            dataVisitor.exitNode();
        }
    }

    private void walkModelChildren(FacesContext context, TreeDataVisitor dataVisitor, TreeDataModel<?> model) {
        if (!getTreeRange().shouldIterateChildren()) {
            return;
        }

        dataVisitor.beforeChildrenVisit();

        Iterator<TreeDataModelTuple> childrenTuples = model.children();
        while (childrenTuples.hasNext()) {
            TreeDataModelTuple tuple = childrenTuples.next();

            restoreFromSnapshot(context, tuple);

            if (!getTreeRange().shouldProcessNode()) {
                continue;
            }

            dataVisitor.enterNode();

            walkModelChildren(context, dataVisitor, model);

            dataVisitor.exitNode();
        }

        dataVisitor.afterChildrenVisit();
    }

    @Override
    protected void resetDataModel() {
        super.resetDataModel();

        treeRange = null;
        declatariveModelsMap = null;
    }

    public TreeDataModelTuple createSnapshot() {
        return getTreeDataModel().createSnapshot();
    }

    public void restoreFromSnapshot(FacesContext context, TreeDataModelTuple tuple) {
        getTreeDataModel().restoreFromSnapshot(tuple);
        setRowKey(context, tuple.getRowKey());
    }

    @Override
    protected void restoreChildState(FacesContext facesContext) {
        setupCurrentComponent();
        super.restoreChildState(facesContext);
    }

    protected UIComponent findDeclarativeModel(String modelId) {
        if (declatariveModelsMap == null) {
            declatariveModelsMap = Maps.newHashMap();
        }

        UIComponent adaptor = declatariveModelsMap.get(modelId);
        if (adaptor == null) {
            adaptor = findComponent(modelId);
            if (adaptor != null) {
                declatariveModelsMap.put(modelId, adaptor);
            }
        }

        if (adaptor == null) {
            throw new IllegalStateException(MessageFormat.format(COMPONENT_FOR_MODEL_UNAVAILABLE, modelId));
        }

        return adaptor;
    }

    public String convertDeclarativeKeyToString(FacesContext context, DeclarativeModelKey declarativeKey)
            throws ConverterException {
        try {
            UIComponent component = findDeclarativeModel(declarativeKey.getModelId());

            TreeModelAdaptor adaptor = (TreeModelAdaptor) component;

            Converter rowKeyConverter = adaptor.getRowKeyConverter();
            if (rowKeyConverter == null) {
                throw new ConverterException(MessageFormat.format(CONVERTER_FOR_MODEL_UNAVAILABLE, declarativeKey.getModelId()));
            }

            return rowKeyConverter.getAsString(context, (UIComponent) adaptor, declarativeKey.getModelKey());
        } catch (ConverterException e) {
            throw e;
        } catch (Exception e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }

    public DeclarativeModelKey convertDeclarativeKeyFromString(FacesContext context, String modelId, String modelKeyAsString)
            throws ConverterException {

        try {
            UIComponent component = findDeclarativeModel(modelId);

            TreeModelAdaptor adaptor = (TreeModelAdaptor) component;

            Converter rowKeyConverter = adaptor.getRowKeyConverter();
            if (rowKeyConverter == null) {
                throw new ConverterException(MessageFormat.format(CONVERTER_FOR_MODEL_UNAVAILABLE, modelId));
            }

            Object modelKey = rowKeyConverter.getAsObject(context, (UIComponent) adaptor, modelKeyAsString);
            return new DeclarativeModelKey(modelId, modelKey);
        } catch (ConverterException e) {
            throw e;
        } catch (Exception e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }
}
