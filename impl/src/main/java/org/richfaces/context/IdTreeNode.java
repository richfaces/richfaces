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

package org.richfaces.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.richfaces.renderkit.util.CoreAjaxRendererUtils;

final class IdTreeNode {

    private static final char LIST_OPENING_CHAR = '[';

    private static final char LIST_CLOSING_CHAR = ']';

    private String componentId;

    private String metadataComponentId;

    private List<IdTreeNode> childNodes;

    private IdTreeNode(String componentId, String metadataComponentId) {
        this.componentId = componentId;
        this.metadataComponentId = metadataComponentId;
    }

    public IdTreeNode() {
        this(null, null);
    }

    private void setChildNodes(String[] componentIds) {
        childNodes = new ArrayList<IdTreeNode>(componentIds.length);
        for (String componentId : componentIds) {
            childNodes.add(new IdTreeNode(componentId, null));
        }
    }

    private void setChildNode(String componentId, String metadataComponentId) {
        childNodes = Collections.singletonList(new IdTreeNode(componentId, metadataComponentId));
    }

    private void appendToStringBuilder(StringBuilder sb, int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            sb.append(' ');
        }
        sb.append('+');

        if (componentId != null) {
            sb.append(componentId);

            if (metadataComponentId != null) {
                sb.append('(');
                sb.append(metadataComponentId);
                sb.append(')');
            }

        } else {
            sb.append("<empty>");
        }

        sb.append('\n');

        if (childNodes != null) {
            for (IdTreeNode childNode : childNodes) {
                childNode.appendToStringBuilder(sb, indentLevel + 2);
            }
        }
    }

    public void appendNodesFromParser(IdParser idParser) {
        List<IdTreeNode> nodes = Collections.singletonList(this);

        while (idParser.findNext()) {
            String componentId = idParser.getComponentId();
            String metadataComponentId = idParser.getMetadataComponentId();

            int childNodesCount = 0;

            if (metadataComponentId != null) {
                for (IdTreeNode node : nodes) {
                    node.setChildNode(componentId, metadataComponentId);
                    childNodesCount++;
                }
            } else {
                if (componentId.length() > 2 && componentId.charAt(0) == LIST_OPENING_CHAR &&
                    componentId.charAt(componentId.length() - 1) == LIST_CLOSING_CHAR) {

                    String[] split = CoreAjaxRendererUtils.asIdsArray(componentId.substring(1, componentId.length() - 1));
                    for (IdTreeNode node : nodes) {
                        node.setChildNodes(split);
                        childNodesCount += split.length;
                    }
                } else {
                    for (IdTreeNode node : nodes) {
                        node.setChildNode(componentId, null);
                        childNodesCount++;
                    }
                }
            }

            List<IdTreeNode> newNodesList;
            if (nodes.size() == 1) {
                newNodesList = nodes.get(0).getChildNodes();
            } else {
                newNodesList = new ArrayList<IdTreeNode>(childNodesCount);
                for (IdTreeNode node : nodes) {
                    newNodesList.addAll(node.getChildNodes());
                }
            }

            nodes = newNodesList;
        }
    }

    public List<IdTreeNode> getChildNodes() {
        return childNodes;
    }

    public String getComponentId() {
        return componentId;
    }

    public String getMetadataComponentId() {
        return metadataComponentId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendToStringBuilder(sb, 0);

        return sb.toString();
    }
}
