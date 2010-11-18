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
package org.richfaces.renderkit;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.component.AbstractTree;
import org.richfaces.component.AbstractTreeNode;
import org.richfaces.component.TreeRange;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.renderkit.TreeRendererBase.QueuedData;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

abstract class TreeEncoderBase {

    static final String TREE_NODE_STATE_ATTRIBUTE = "__treeNodeState";
    
    protected final FacesContext context;
    
    protected final ResponseWriter responseWriter;
    
    protected final AbstractTree tree;

    private TreeRange treeRange;
    
    private LinkedList<QueuedData> queuedData = new LinkedList<QueuedData>();
    
    public TreeEncoderBase(FacesContext context, AbstractTree tree) {
        super();
        this.context = context;
        this.responseWriter = context.getResponseWriter();
        this.tree = tree;
        
        this.treeRange = (TreeRange) tree.getComponentState().getRange();
    }

    protected void encodeTree(Iterator<Object> childrenIterator) throws IOException {
        Predicate<Object> renderedTreeNodeKeyPredicate = new Predicate<Object>() {
            public boolean apply(Object input) {
                tree.setRowKey(input);
                
                if (!tree.isRowAvailable()) {
                    return false;
                }
                
                return tree.findTreeNodeComponent() != null;
            }
        };
        
        UnmodifiableIterator<Object> filteredIterator = Iterators.filter(childrenIterator, renderedTreeNodeKeyPredicate);
        while (filteredIterator.hasNext()) {
            Object rowKey = filteredIterator.next();
            
            encodeTreeNode(rowKey, !filteredIterator.hasNext());
        }
    }
    
    protected void encodeTreeNode(Object rowKey, boolean isLastNode) throws IOException {
        if (!queuedData.isEmpty()) {
            QueuedData data = queuedData.getLast();
            if (!data.isEncoded()) {
                tree.setRowKey(context, data.getRowKey());
                
                writeTreeNodeStartElement(data.isExpanded() ? TreeNodeState.expanded : TreeNodeState.collapsed, data.isLastNode());
                
                data.setEncoded(true);
            }
        }
        
        tree.setRowKey(context, rowKey);

        boolean expanded = tree.isExpanded();
        queuedData.add(new QueuedData(rowKey, isLastNode, expanded));
        
        boolean iterateChildren = treeRange.shouldIterateChildren(rowKey);
        
        if (iterateChildren) {
            encodeTree(tree.getChildrenRowKeysIterator(context, rowKey));
        }

        QueuedData data = queuedData.removeLast();
        if (!data.isEncoded()) {
            TreeNodeState nodeState = iterateChildren ? TreeNodeState.leaf : TreeNodeState.collapsed;
            writeTreeNodeStartElement(nodeState, data.isLastNode());
        }
        
        writeTreeNodeEndElement();
    }
    
    protected void writeTreeNodeStartElement(TreeNodeState nodeState, boolean isLast) throws IOException {
        AbstractTreeNode treeNodeComponent = tree.findTreeNodeComponent();

        context.getAttributes().put(TREE_NODE_STATE_ATTRIBUTE, nodeState);
        
        responseWriter.startElement(HtmlConstants.DIV_ELEM, tree);
        responseWriter.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, 
            HtmlUtil.concatClasses("rf-tr-nd", isLast ? "rf-tr-nd-last" : null, nodeState.getNodeClass()), 
            null);
        responseWriter.writeAttribute(HtmlConstants.ID_ATTRIBUTE, treeNodeComponent.getClientId(context), null);
        
        emitClientToggleEvent(treeNodeComponent, nodeState);
        treeNodeComponent.encodeAll(context);
    }

    protected void writeTreeNodeEndElement() throws IOException {
        responseWriter.endElement(HtmlConstants.DIV_ELEM);
    }

    public abstract void encode() throws IOException;
    
    private void emitClientToggleEvent(AbstractTreeNode treeNode, TreeNodeState nodeState) {
        if (treeNode.getClientId(context).equals(context.getAttributes().get(TreeNodeRendererBase.AJAX_TOGGLED_NODE_ATTRIBUTE))) {
            TreeNodeState submittedState = ((Boolean) (context.getAttributes().get(TreeNodeRendererBase.AJAX_TOGGLED_NODE_STATE_ATTRIBUTE)))
                ? TreeNodeState.expanded : TreeNodeState.collapsed;
            
            if (submittedState == nodeState || nodeState == TreeNodeState.leaf) {
                AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);
                ajaxContext.appendOncomplete(new JSFunction("RichFaces.ui.TreeNode.emitToggleEvent", treeNode.getClientId(context)));
            }
        }
    }
}