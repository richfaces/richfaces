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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class ComponentMatcherNode {

    private boolean added;

    private boolean patternNode;

    private String source;

    private boolean hasParentPatternNode;

    private int kidPatternNodesCounter = 0;

    private ComponentMatcherNode parentNode;

    private Map<String, ComponentMatcherNode> idChildren;

    private Map<String, ComponentMatcherNode> patternChildren;

    private Set<String> subtreeIds;

    private static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    private static boolean isEmpty(Map<?, ?> m) {
        return m == null || m.isEmpty();
    }

    void setParentNode(ComponentMatcherNode parentNode) {
        this.parentNode = parentNode;
    }

    void incrementKidPatternNodesCounter() {
        if (kidPatternNodesCounter == 0 && !isPatternNode()) {
            ComponentMatcherNode parentNode = getParentNode();
            if (parentNode != null) {
                parentNode.incrementKidPatternNodesCounter();
            }
        }

        kidPatternNodesCounter++;
    }

    void decrementKidPatternNodesCounter() {
        kidPatternNodesCounter--;

        if (kidPatternNodesCounter == 0 && !isPatternNode()) {
            ComponentMatcherNode parentNode = getParentNode();
            if (parentNode != null) {
                parentNode.decrementKidPatternNodesCounter();
            }
        }
    }

    void setHasParentPatternNode(boolean hasParentPatternNode) {
        this.hasParentPatternNode = hasParentPatternNode;

        if (!isPatternNode()) {
            if (idChildren != null) {
                for (ComponentMatcherNode child : idChildren.values()) {
                    child.setHasParentPatternNode(hasParentPatternNode);
                }
            }

            if (patternChildren != null) {
                for (ComponentMatcherNode child : patternChildren.values()) {
                    child.setHasParentPatternNode(hasParentPatternNode);
                }
            }
        }
    }

    boolean matches(String shortId) {
        if (isPatternNode()) {
            //TODO - modify when real patterns will be supported
            return true;
        } else {
            return source.equals(shortId);
        }
    }

    Map<String, ComponentMatcherNode> getChildrenMap(boolean isPattern) {
        return isPattern ? patternChildren : idChildren;
    }

    Map<String, ComponentMatcherNode> getOrCreateChildrenMap(boolean isPattern) {
        if (isPattern) {
            if (patternChildren == null) {
                patternChildren = new HashMap<String, ComponentMatcherNode>(1);
            }

            return patternChildren;
        } else {
            if (idChildren == null) {
                idChildren = new HashMap<String, ComponentMatcherNode>();
            }

            return idChildren;
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ComponentMatcherNode getParentNode() {
        return parentNode;
    }

    public ComponentMatcherNode getMatchedChild(String shortId) {
        ComponentMatcherNode node = null;
        if (idChildren != null) {
            node = idChildren.get(shortId);
        }

        if (node == null && patternChildren != null) {
            for (ComponentMatcherNode child : patternChildren.values()) {
                if (child.matches(shortId)) {
                    node = child;
                    break;
                }
            }
        }

        return node;
    }

    public ComponentMatcherNode getChild(String key, boolean isPatternNode) {
        Map<String, ComponentMatcherNode> childrenMap = getChildrenMap(isPatternNode);
        if (childrenMap != null) {
            return childrenMap.get(key);
        }

        return null;
    }

    public ComponentMatcherNode getOrCreateChild(String key, boolean isPatternNode) {
        Map<String, ComponentMatcherNode> childrenMap = getOrCreateChildrenMap(isPatternNode);
        ComponentMatcherNode childNode = childrenMap.get(key);
        if (childNode == null) {
            childNode = new ComponentMatcherNode();

            childNode.setPatternNode(isPatternNode);
            childNode.setSource(key);

            addChild(childNode);
        }

        return childNode;
    }

    public void addChild(ComponentMatcherNode child) {
        child.setParentNode(this);

        Map<String, ComponentMatcherNode> childrenMap = getOrCreateChildrenMap(child.isPatternNode());

        ComponentMatcherNode previousNode = childrenMap.get(child.getSource());
        if (previousNode != null) {
            removeChild(previousNode);
        }

        childrenMap.put(child.getSource(), child);

        if (child.isPatternNode() || child.hasKidPatternNodes()) {
            incrementKidPatternNodesCounter();
        }

        if (hasPatternNodeInChain()) {
            child.setHasParentPatternNode(true);
        }

        addAllSubtreeIds(child.getSubtreeIds());
    }

    public void removeChild(ComponentMatcherNode child) {
        Map<String, ComponentMatcherNode> childrenMap = getChildrenMap(child.isPatternNode());
        if (childrenMap != null) {
            if (child.getParentNode() == this) {
                child.setParentNode(null);
                child.setHasParentPatternNode(false);
                childrenMap.remove(child.getSource());

                if (child.isPatternNode() || child.hasKidPatternNodes()) {
                    decrementKidPatternNodesCounter();
                }

                removeAllSubtreeIds(child.getSubtreeIds());
            } else {
                //TODO - ?
            }
        }
    }

    public boolean hasDirectChildren() {
        return hasDirectIdChildren() || hasDirectPatternChildren();
    }

    public boolean hasDirectIdChildren() {
        return !isEmpty(idChildren);
    }

    public boolean hasDirectPatternChildren() {
        return !isEmpty(patternChildren);
    }

    public boolean hasKidPatternNodes() {
        return kidPatternNodesCounter > 0;
    }

    public void markAdded() {
        added = true;
    }

    public void markRemoved() {
        added = false;
    }

    public boolean isAdded() {
        return added;
    }

    public boolean hasParentPatternNode() {
        return hasParentPatternNode;
    }

    public boolean hasPatternNodeInChain() {
        return isPatternNode() || hasParentPatternNode();
    }

    public Collection<String> getSubtreeIds() {
        return subtreeIds;
    }

    public boolean hasSubtreeIds() {
        return !isEmpty(subtreeIds);
    }

    public void addAllSubtreeIds(Collection<String> ids) {
        if (ids != null) {
            if (subtreeIds == null) {
                subtreeIds = new HashSet<String>();
            }

            subtreeIds.addAll(ids);

            if (parentNode != null) {
                parentNode.addAllSubtreeIds(ids);
            }
        }
    }

    public void addSubtreeId(String subtreeId) {
        if (subtreeIds == null) {
            subtreeIds = new HashSet<String>();
        }

        subtreeIds.add(subtreeId);

        if (parentNode != null) {
            parentNode.addSubtreeId(subtreeId);
        }
    }

    public void removeAllSubtreeIds(Collection<String> ids) {
        if (ids != null) {
            if (subtreeIds != null) {
                subtreeIds.removeAll(ids);
            }

            if (parentNode != null) {
                parentNode.removeAllSubtreeIds(ids);
            }
        }
    }

    public void removeSubtreeId(String subtreeId) {
        if (subtreeIds != null) {
            subtreeIds.remove(subtreeId);
        }

        if (parentNode != null) {
            parentNode.removeSubtreeId(subtreeId);
        }
    }

    public Map<String, ComponentMatcherNode> getIdChildren() {
        return idChildren;
    }

    public Map<String, ComponentMatcherNode> getPatternChildren() {
        return patternChildren;
    }

    public boolean isPatternNode() {
        return patternNode;
    }

    public void setPatternNode(boolean patternNode) {
        this.patternNode = patternNode;
    }
}
