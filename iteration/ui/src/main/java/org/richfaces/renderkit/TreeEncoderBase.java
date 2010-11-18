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

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.model.DataVisitResult;
import org.richfaces.component.AbstractTree;
import org.richfaces.component.AbstractTreeNode;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.model.TreeDataVisitor;
import org.richfaces.renderkit.TreeRendererBase.QueuedData;

abstract class TreeEncoderBase implements TreeDataVisitor {

    static final String TREE_NODE_STATE_ATTRIBUTE = "__treeNodeState";

    protected final FacesContext context;

    protected final ResponseWriter responseWriter;

    protected final AbstractTree tree;

    private LinkedList<QueuedData> queuedDataList = new LinkedList<QueuedData>();

    private QueuedData queuedData;

    public TreeEncoderBase(FacesContext context, AbstractTree tree) {
        super();
        this.context = context;
        this.responseWriter = context.getResponseWriter();
        this.tree = tree;
    }

    protected void encodeTree() throws IOException {
        tree.walk(context, this, null);
    }

    protected void flushNode() throws IOException {
        if (!queuedData.isEncoded()) {
            tree.setRowKey(context, queuedData.getRowKey());
            
            TreeNodeState state;
            if (tree.isLeaf()) {
                state = TreeNodeState.leaf;
            } else {
                if (queuedData.isVisited()) {
                    state = TreeNodeState.leaf;
                } else {
                    state = TreeNodeState.collapsed;
                }
            }
            
            writeTreeNodeStartElement(state);
        }

        writeTreeNodeEndElement();
    }
    
    protected void flushParentNode() throws IOException {
        if (queuedDataList.isEmpty()) {
            return;
        }
        
        QueuedData data = queuedDataList.getLast();
        if (!data.isEncoded()) {
            data.setEncoded(true);
            tree.setRowKey(context, data.getRowKey());
            
            writeTreeNodeStartElement(tree.isExpanded() ? TreeNodeState.expanded : TreeNodeState.collapsed);
        }
    }
    
    public void enterNode() {
        if (queuedData != null) {
            queuedData.makeVisited();
            queuedDataList.add(queuedData);
            queuedData = null;
        }
    }

    public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
        try {
            if (queuedData != null) {
                flushNode();
                queuedData = null;
            } else {
                flushParentNode();
            }
        } catch (IOException e) {
            throw new FacesException(e.getMessage(), e);
        }

        if (rowKey != null) {
            tree.setRowKey(context, rowKey);

            if (tree.isRowAvailable() && tree.findTreeNodeComponent() != null) {
                queuedData = new QueuedData(rowKey);
            }
        }

        return DataVisitResult.CONTINUE;
    }

    public void exitNode() {
        try {
            if (queuedData != null) {
                flushNode();
                queuedData = null;
            }

            if (!queuedDataList.isEmpty()) {
                queuedData = queuedDataList.removeLast();
            }
        } catch (IOException e) {
            throw new FacesException(e.getMessage(), e);
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