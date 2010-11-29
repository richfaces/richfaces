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
import java.util.LinkedList;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.component.AbstractTree;
import org.richfaces.component.AbstractTreeNode;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.model.TreeDataModelTuple;
import org.richfaces.model.TreeDataVisitor;

abstract class TreeEncoderBase implements TreeDataVisitor {

    /**
     * @author Nick Belaevski
     * 
     */
    private static final class QueuedData {

        private boolean encoded = false;

        private boolean visited = false;
        
        //TODO - should be part of tree state
        private boolean leaf;
        
        private TreeDataModelTuple tuple;
        
        public QueuedData(TreeDataModelTuple tuple, boolean leaf) {
            super();
            this.tuple = tuple;
            this.leaf = leaf;
        }

        public boolean isEncoded() {
            return encoded;
        }
        
        public void setEncoded(boolean encoded) {
            this.encoded = encoded;
        }

        public void makeVisited() {
            this.visited = true;
        }
        
        public boolean isVisited() {
            return visited;
        }
        
        public TreeDataModelTuple getTuple() {
            return tuple;
        }
        
        public boolean isLeaf() {
            return leaf;
        }
    }

    static final String TREE_NODE_STATE_ATTRIBUTE = "__treeNodeState";

    protected final FacesContext context;

    protected final ResponseWriter responseWriter;

    protected final AbstractTree tree;

    private LinkedList<QueuedData> queuedDataList = new LinkedList<QueuedData>();

    public TreeEncoderBase(FacesContext context, AbstractTree tree) {
        super();
        this.context = context;
        this.responseWriter = context.getResponseWriter();
        this.tree = tree;
    }

    protected void encodeTree() throws IOException {
        tree.walkModel(context, this);
    }

    protected void flushParentNode() throws IOException {
        if (queuedDataList.isEmpty()) {
            return;
        }
        
        QueuedData data = queuedDataList.getLast();
        if (!data.isEncoded()) {
            data.setEncoded(true);
            tree.setRowKeyAndData(context, data.getTuple().getRowKey(), data.getTuple().getData());
            
            TreeNodeState nodeState = getNodeState(data.isLeaf(), false);
            
            writeTreeNodeStartElement(nodeState);
            tree.findTreeNodeComponent().encodeAll(context);
        }
    }

    private TreeNodeState getNodeState(boolean leaf, boolean visited) {
        TreeNodeState nodeState;
        if (leaf) {
            nodeState = TreeNodeState.leaf;
        } else if (visited) {
            nodeState = TreeNodeState.expandedNoChildren;
        } else if (tree.isExpanded()) {
            nodeState = TreeNodeState.expanded;
        } else {
            nodeState = TreeNodeState.collapsed;
        }
        return nodeState;
    }

    public void beforeChildrenVisit() {
        if (!queuedDataList.isEmpty()) {
            queuedDataList.getLast().makeVisited();
        }
    }

    /* (non-Javadoc)
     * @see org.richfaces.model.TreeDataVisitor#afterChildrenVisit()
     */
    public void afterChildrenVisit() {
        // TODO Auto-generated method stub
        
    }
    
    public void enterNode() {
        TreeDataModelTuple tuple = new TreeDataModelTuple(tree.getRowKey(), tree.getRowData());
        QueuedData queuedData = new QueuedData(tuple, tree.isLeaf());
        
        try {
            flushParentNode();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        tree.setRowKeyAndData(context, tuple.getRowKey(), tuple.getData());
        queuedDataList.add(queuedData);
    }
    
    public void exitNode() {
        QueuedData data = queuedDataList.removeLast();
        
        tree.setRowKeyAndData(context, data.getTuple().getRowKey(), data.getTuple().getData());
        if (!data.isEncoded()) {
            try {
                writeTreeNodeStartElement(getNodeState(data.isLeaf(), data.isVisited()));
                tree.findTreeNodeComponent().encodeAll(context);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        try {
            writeTreeNodeEndElement();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void writeTreeNodeStartElement(TreeNodeState nodeState) throws IOException {
        AbstractTreeNode treeNodeComponent = tree.findTreeNodeComponent();

        context.getAttributes().put(TREE_NODE_STATE_ATTRIBUTE, nodeState);

        responseWriter.startElement(HtmlConstants.DIV_ELEM, tree);
        responseWriter.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, 
            HtmlUtil.concatClasses("rf-tr-nd", nodeState.getNodeClass()), 
            null);
        responseWriter.writeAttribute(HtmlConstants.ID_ATTRIBUTE, treeNodeComponent.getClientId(context), null);

        emitClientToggleEvent(treeNodeComponent, nodeState);
    }

    protected void writeTreeNodeEndElement() throws IOException {
        responseWriter.endElement(HtmlConstants.DIV_ELEM);
    }

    public abstract void encode() throws IOException;

    private void emitClientToggleEvent(AbstractTreeNode treeNode, TreeNodeState nodeState) {
        if (treeNode.getClientId(context).equals(context.getAttributes().get(TreeNodeRendererBase.AJAX_TOGGLED_NODE_ATTRIBUTE))) {
            TreeNodeState initialState = (TreeNodeState) context.getAttributes().get(TreeNodeRendererBase.AJAX_TOGGLED_NODE_STATE_ATTRIBUTE);

            if (initialState.isDifferentThan(nodeState)) {
                AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);
                ajaxContext.appendOncomplete(new JSFunction("RichFaces.ui.TreeNode.emitToggleEvent", treeNode.getClientId(context)));
            }
        }
    }
}