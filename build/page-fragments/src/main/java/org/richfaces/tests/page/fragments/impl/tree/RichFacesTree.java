/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.page.fragments.impl.tree;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.common.AdvancedInteractions;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

import com.google.common.collect.Lists;

public class RichFacesTree implements Tree, AdvancedInteractions<RichFacesTree.AdvancedTreeInteractionsImpl> {

    @Root
    private WebElement root;

    @FindByJQuery("> .rf-tr-nd")
    private List<RichFacesTreeNode> childNodes;
    @FindByJQuery("> .rf-tr-nd")
    private List<WebElement> childNodesElements;

    private final AdvancedTreeInteractionsImpl interactions = new AdvancedTreeInteractionsImpl();

    @Override
    public AdvancedTreeInteractionsImpl advanced() {
        return interactions;
    }

    @Override
    public TreeNode collapseNode(int index) {
        return collapseNode(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public TreeNode collapseNode(ChoicePicker picker) {
        return childNodes.get(getIndexOfPickedElement(picker)).advanced().collapse();
    }

    @Override
    public TreeNode expandNode(int index) {
        return expandNode(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public TreeNode expandNode(ChoicePicker picker) {
        return childNodes.get(getIndexOfPickedElement(picker)).advanced().expand();
    }

    protected List<RichFacesTreeNode> getChildNodes() {
        return Collections.unmodifiableList(childNodes);
    }

    protected List<WebElement> getChildNodesElements() {
        return Collections.unmodifiableList(childNodesElements);
    }

    protected int getIndexOfPickedElement(ChoicePicker picker) {
        if (childNodesElements.isEmpty()) {
            throw new RuntimeException("Cannot find child node, because there are no child nodes.");
        }
        return Utils.getIndexOfElement(picker.pick(childNodesElements));
    }

    @Override
    public TreeNode selectNode(int index) {
        return selectNode(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public TreeNode selectNode(ChoicePicker picker) {
        return childNodes.get(getIndexOfPickedElement(picker)).advanced().select();
    }

    public class AdvancedTreeInteractionsImpl implements Tree.AdvancedTreeInteractions {

        private final Event DEFAULT_TOGGLE_NODE_EVENT = Event.CLICK;
        private final boolean DEFAULT_TOGGLE_BY_HANDLE = Boolean.TRUE;
        private boolean toggleByHandle = DEFAULT_TOGGLE_BY_HANDLE;
        private Event toggleNodeEvent = DEFAULT_TOGGLE_NODE_EVENT;

        @Override
        public TreeNode getFirstNode() {
            return getNodes().get(0);
        }

        @Override
        public List<? extends TreeNode> getLeafNodes() {
            List<TreeNode> result = Lists.newArrayList();
            for (TreeNode treeNode : childNodes) {
                if (treeNode.advanced().isLeaf()) {
                    result.add(treeNode);
                }
            }
            return Collections.unmodifiableList(result);
        }

        @Override
        public List<? extends TreeNode> getNodes() {
            return Collections.unmodifiableList(childNodes);
        }

        @Override
        public List<? extends TreeNode> getNodesCollapsed() {
            List<TreeNode> result = Lists.newArrayList();
            for (TreeNode treeNode : childNodes) {
                if (treeNode.advanced().isCollapsed()) {
                    result.add(treeNode);
                }
            }
            return Collections.unmodifiableList(result);
        }

        @Override
        public List<WebElement> getNodesElements() {
            return Collections.unmodifiableList(childNodesElements);
        }

        @Override
        public List<? extends TreeNode> getNodesExpanded() {
            List<TreeNode> result = Lists.newArrayList();
            for (TreeNode treeNode : childNodes) {
                if (treeNode.advanced().isExpanded()) {
                    result.add(treeNode);
                }
            }
            return Collections.unmodifiableList(result);
        }

        @Override
        public WebElement getRootElement() {
            return root;
        }

        @Override
        public List<? extends TreeNode> getSelectedNodes() {
            List<TreeNode> result = Lists.newArrayList();
            for (TreeNode treeNode : childNodes) {
                if (treeNode.advanced().isSelected()) {
                    result.add(treeNode);
                }
            }
            return Collections.unmodifiableList(result);
        }

        protected Event getToggleNodeEvent() {
            return toggleNodeEvent;
        }

        protected boolean isToggleByHandle() {
            return toggleByHandle;
        }

        @Override
        public void setupToggleByHandle() {
            this.toggleByHandle = DEFAULT_TOGGLE_BY_HANDLE;
        }

        @Override
        public void setupToggleByHandle(boolean toggleByHandle) {
            this.toggleByHandle = toggleByHandle;
        }

        @Override
        public void setupToggleNodeEvent() {
            this.toggleNodeEvent = DEFAULT_TOGGLE_NODE_EVENT;
        }

        @Override
        public void setupToggleNodeEvent(Event toggleNodeEvent) {
            this.toggleNodeEvent = toggleNodeEvent;
        }
    }
}
