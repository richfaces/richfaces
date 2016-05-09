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

import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.component.AbstractTree;
import org.richfaces.component.AbstractTreeNode;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.model.TreeDataModelTuple;
import org.richfaces.model.TreeDataVisitor;

abstract class TreeEncoderBase implements TreeDataVisitor {
    private static final class QueuedData {
        private enum State {
            initial,
            visited,
            encoded
        }

        private State state = State.initial;
        private TreeDataModelTuple tuple;

        QueuedData(TreeDataModelTuple tuple) {
            super();
            this.tuple = tuple;
        }

        public boolean isEncoded() {
            return state == State.encoded;
        }

        public void makeEncoded() {
            this.state = State.encoded;
        }

        public void makeVisited() {
            this.state = State.visited;
        }

        public boolean isVisited() {
            return state == State.visited;
        }

        public TreeDataModelTuple getTuple() {
            return tuple;
        }
    }

    static final String TREE_NODE_STATE_ATTRIBUTE = "__treeNodeState";
    protected final FacesContext context;
    protected final ResponseWriter responseWriter;
    protected final AbstractTree tree;
    private LinkedList<QueuedData> queuedDataList = new LinkedList<QueuedData>();

    TreeEncoderBase(FacesContext context, AbstractTree tree) {
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
            data.makeEncoded();
            tree.restoreFromSnapshot(context, data.getTuple());

            TreeNodeState nodeState = getNodeState(tree.isLeaf(), false);

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

    public void afterChildrenVisit() {
    }

    public void enterNode() {
        TreeDataModelTuple tuple = tree.createSnapshot();
        QueuedData queuedData = new QueuedData(tuple);

        try {
            flushParentNode();
        } catch (IOException e) {
            throw new FacesException(e.getMessage(), e);
        }

        tree.restoreFromSnapshot(context, tuple);
        queuedDataList.add(queuedData);
    }

    public void exitNode() {
        QueuedData data = queuedDataList.removeLast();

        tree.restoreFromSnapshot(context, data.getTuple());
        try {
            if (!data.isEncoded()) {
                writeTreeNodeStartElement(getNodeState(tree.isLeaf(), data.isVisited()));
                tree.findTreeNodeComponent().encodeAll(context);
            }

            writeTreeNodeEndElement();
        } catch (IOException e) {
            throw new FacesException(e.getMessage(), e);
        }
    }

    protected void writeTreeNodeStartElement(TreeNodeState nodeState) throws IOException {
        AbstractTreeNode treeNodeComponent = tree.findTreeNodeComponent();

        context.getAttributes().put(TREE_NODE_STATE_ATTRIBUTE, nodeState);

        responseWriter.startElement(HtmlConstants.DIV_ELEM, tree);
        responseWriter.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
            HtmlUtil.concatClasses("rf-tr-nd", nodeState.getNodeClass()), null);
        responseWriter.writeAttribute(HtmlConstants.ID_ATTRIBUTE, treeNodeComponent.getClientId(context), null);

        emitClientToggleEvent(treeNodeComponent, nodeState);
    }

    protected void writeTreeNodeEndElement() throws IOException {
        responseWriter.endElement(HtmlConstants.DIV_ELEM);
    }

    public abstract void encode() throws IOException;

    private void emitClientToggleEvent(AbstractTreeNode treeNode, TreeNodeState nodeState) {
        if (treeNode.getClientId(context).equals(context.getAttributes().get(TreeNodeRendererBase.AJAX_TOGGLED_NODE_ATTRIBUTE))) {
            TreeNodeState initialState = (TreeNodeState) context.getAttributes().get(
                TreeNodeRendererBase.AJAX_TOGGLED_NODE_STATE_ATTRIBUTE);

            if (initialState.isDifferentThan(nodeState)) {
                ExtendedPartialViewContext partialContext = ExtendedPartialViewContext.getInstance(context);
                partialContext.appendOncomplete(new JSFunction("RichFaces.ui.TreeNode.emitToggleEvent", treeNode
                    .getClientId(context)));
            }
        }
    }
}