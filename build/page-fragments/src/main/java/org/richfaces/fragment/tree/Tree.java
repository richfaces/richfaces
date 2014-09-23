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
package org.richfaces.fragment.tree;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.picker.ChoicePicker;

public interface Tree {

    /**
     * Collapses child node at index.
     *
     * @param index index of node
     * @return      collapsed node
     */
    TreeNode collapseNode(int index);

    /**
     * Collapses child node chosen by picker.
     *
     * @param  picker object for defining rules according which the child node will be picked and collapsed
     * @return        collapsed node
     */
    TreeNode collapseNode(ChoicePicker picker);

    /**
     * Expands child node at index.
     *
     * @param  index index of node
     * @return       expanded node
     */
    TreeNode expandNode(int index);

    /**
     * Expands child node chosen by picker.
     *
     * @param picker object for defining rules according which the child node will be picked and expanded
     * @return       expanded node
     */
    TreeNode expandNode(ChoicePicker picker);

    /**
     * Selects child node at index.
     *
     * @param index index of node
     * @return      selected node
     */
    TreeNode selectNode(int index);

    /**
     * Selects child node chosen by picker.
     *
     * @param picker object for defining rules according which the child node will be picked and selected
     * @return       selected node
     */
    TreeNode selectNode(ChoicePicker picker);

    public interface AdvancedTreeInteractions {

        /**
         * @return first visible child node
         */
        TreeNode getFirstNode();

        /**
         * @return child leaf nodes
         */
        List<? extends TreeNode> getLeafNodes();

        /**
         * @return child nodes
         */
        List<? extends TreeNode> getNodes();

        /**
         * @return collapsed child nodes
         */
        List<? extends TreeNode> getNodesCollapsed();

        /**
         * @return child nodes elements
         */
        List<WebElement> getNodesElements();

        /**
         * @return expanded child nodes
         */
        List<? extends TreeNode> getNodesExpanded();

        WebElement getRootElement();

        /**
         * @return selected child nodes
         */
        List<? extends TreeNode> getSelectedNodes();

        void setToggleByHandle();

        void setToggleByHandle(boolean toggleByHandle);

        void setToggleNodeEvent();

        void setToggleNodeEvent(Event toggleNodeEvent);

    }

    public interface TreeNode extends Tree {

        AdvancedTreeNodeInteractions advanced();

        public interface AdvancedTreeNodeInteractions extends AdvancedTreeInteractions {

            /**
             * Expands this node.
             *
             * @return same node
             */
            TreeNode expand();

            /**
             * Collapses this node.
             *
             * @return same node
             */
            TreeNode collapse();

            /**
             * @return element which contains icon and label.
             */
            WebElement getContainerElement();

            WebElement getHandleElement();

            WebElement getHandleLoadingElement();

            WebElement getIconElement();

            WebElement getLabelElement();

            /**
             * @return the first child element of every node. Contains label, icon, handle.
             */
            WebElement getNodeInfoElement();

            boolean isCollapsed();

            boolean isExpanded();

            boolean isLeaf();

            boolean isSelected();

            /**
             * Selects this node.
             *
             * @return same node
             */
            TreeNode select();

            /**
             * Checks styleClasses of root, handle, and icon elements.
             *
             * @return WaitingWrapper
             */
            WaitingWrapper waitUntilNodeIsCollapsed();

            /**
             * Checks styleClasses of root, handle, and icon elements.
             *
             * @return WaitingWrapper
             */
            WaitingWrapper waitUntilNodeIsExpanded();

            WaitingWrapper waitUntilNodeIsNotSelected();

            WaitingWrapper waitUntilNodeIsSelected();
        }
    }
}
