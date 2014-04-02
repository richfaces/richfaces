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
package org.richfaces.context;

import static org.richfaces.component.MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.AjaxContainer;
import org.richfaces.component.ComponentIterators;
import org.richfaces.component.MetaComponentResolver;
import org.richfaces.context.IdParser.Node;
import org.richfaces.util.SeparatorChar;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;

/**
 * @author Nick Belaevski
 *
 */
public final class ComponentIdResolver {
    private static final Joiner EMPTY_STRING_JOINER = Joiner.on("").skipNulls();
    private static Map<String, String> metaComponentSubstitutions = new HashMap<String, String>();

    static {
        metaComponentSubstitutions.put(AjaxContainer.META_COMPONENT_ID, AjaxContainer.DEFAULT_RENDER_ID);
    }

    private Set<String> resolvedIds;
    private Set<String> unresolvedIds;
    private Set<String> absoluteIds = null;
    private UIComponent containerTopMatchComponent;
    private LinkedList<UIComponent> componentsStack = null;
    private FacesContext facesContext;
    private ComponentIdResolverNode rootNode;

    public ComponentIdResolver(FacesContext facesContext) {
        super();

        this.facesContext = facesContext;

        this.resolvedIds = new HashSet<String>();
        this.unresolvedIds = new HashSet<String>();
        this.rootNode = new ComponentIdResolverNode(null, null);
    }

    private static boolean isNotEmpty(Collection<?> c) {
        return c != null && !c.isEmpty();
    }

    private static boolean isRoot(UIComponent component) {
        return component.getParent() == null;
    }

    private static UIComponent findRoot(UIComponent component) {
        UIComponent c = component;

        while (!isRoot(c)) {
            c = c.getParent();
        }

        return c;
    }

    private static UIComponent findContainer(UIComponent component) {
        UIComponent c = component;

        while (c != null && !(c instanceof NamingContainer)) {
            c = c.getParent();
        }

        return c;
    }

    private static Iterator<MetaComponentResolver> createResolversChainIterator(UIComponent component) {
        return Iterators.filter(ComponentIterators.parentsAndSelf(component), MetaComponentResolver.class);
    }

    private static String substituteUnresolvedMetaComponentId(FacesContext context, UIComponent component,
        String metaComponentId) {
        Iterator<MetaComponentResolver> iterator = createResolversChainIterator(component);

        while (iterator.hasNext()) {
            MetaComponentResolver metaComponentResolver = (MetaComponentResolver) iterator.next();

            String resolvedId = metaComponentResolver.substituteUnresolvedClientId(context, component, metaComponentId);

            if (resolvedId != null) {
                return resolvedId;
            }
        }

        return null;
    }

    private static String resolveMetaComponentId(FacesContext context, UIComponent component, String metaComponentId) {
        Iterator<MetaComponentResolver> iterator = createResolversChainIterator(component);

        while (iterator.hasNext()) {
            MetaComponentResolver metaComponentResolver = (MetaComponentResolver) iterator.next();

            String resolvedId = metaComponentResolver.resolveClientId(context, component, metaComponentId);

            if (resolvedId != null) {
                return resolvedId;
            }
        }

        return null;
    }

    // used in unit tests
    static void setMetaComponentSubstitutions(Map<String, String> substitutionsMap) {
        metaComponentSubstitutions = substitutionsMap;
    }

    private boolean hasFunctionNodes(Node[] nodes) {
        for (Node node : nodes) {
            if (node.getFunction() != null) {
                return true;
            }
        }

        return false;
    }

    private String getMetaComponentId(String s) {
        int metaComponentIdx = s.indexOf(META_COMPONENT_SEPARATOR_CHAR);

        if (metaComponentIdx < 0) {
            return null;
        } else {
            return s.substring(metaComponentIdx);
        }
    }

    private Collection<String> computeClientIds(FacesContext context, UIComponent topMatchComponent,
        UIComponent bottomMatchComponent, String id) {

        Node[] nodes = IdParser.parse(id);
        if (!hasFunctionNodes(nodes)) {
            return Collections.singleton(EMPTY_STRING_JOINER.join(bottomMatchComponent.getClientId(facesContext),
                getMetaComponentId(id)));
        } else {
            String topMatchClientId = topMatchComponent.getClientId(facesContext);
            Node[] topMatchNodes = IdParser.parse(topMatchClientId);

            // topMatchNodes & nodes have 1 element overlap
            Node[] mergedNodes = new Node[topMatchNodes.length + nodes.length - 1];

            int destPos = topMatchNodes.length;

            System.arraycopy(topMatchNodes, 0, mergedNodes, 0, destPos);
            System.arraycopy(nodes, 1, mergedNodes, destPos, nodes.length - 1);

            ClientIdFunctionEvaluator evaluator = new ClientIdFunctionEvaluator(context, mergedNodes);

            return evaluator.evaluate(topMatchComponent);
        }
    }

    protected void addIdImmediately(String id) {
        Node[] nodes = IdParser.parse(id);

        ComponentIdResolverNode resolverNode = rootNode;

        for (Node node : nodes) {
            if (node.getFunction() != null) {
                continue;
            }

            String image = node.getImage();

            int metaSepIdx = image.indexOf(META_COMPONENT_SEPARATOR_CHAR);
            if (metaSepIdx >= 0) {
                image = image.substring(0, metaSepIdx);
            }

            if (Strings.isNullOrEmpty(image)) {
                continue;
            }

            resolverNode = resolverNode.getOrCreateChild(image);
        }

        unresolvedIds.add(id);
        resolverNode.addFullId(id);
    }

    public void addId(String id) {
        if (isAbsolute(id)) {
            if (absoluteIds == null) {
                absoluteIds = new HashSet<String>();
            }

            absoluteIds.add(id.substring(1));
        } else {
            addIdImmediately(id);
        }
    }

    public Set<String> getResolvedIds() {
        return resolvedIds;
    }

    private ComponentIdResolverNode buildInversedTreeForNode(ComponentIdResolverNode directNode) {
        ComponentIdResolverNode localNode = directNode;
        ComponentIdResolverNode localInversedNode = rootNode;

        while (localNode != null) {
            ComponentIdResolverNode parent = localNode.getParent();
            if (parent != null) {
                localInversedNode = localInversedNode.getOrCreateChild(localNode.getId());
            }

            localNode = parent;
        }

        return localInversedNode;
    }

    private boolean isAbsolute(String id) {
        return id.charAt(0) == SeparatorChar.SEPARATOR_CHAR;
    }

    private void buildInversedFilteredTreeRecursively(ComponentIdResolverNode directNode) {
        Set<String> fullIds = directNode.getFullIds();
        if (isNotEmpty(fullIds)) {
            ComponentIdResolverNode inversedNode = null;
            for (String fullId : fullIds) {
                if (isAbsolute(fullId)) {
                    continue;
                }

                if (inversedNode == null) {
                    inversedNode = buildInversedTreeForNode(directNode);
                }

                inversedNode.addFullId(fullId);
            }
        }

        Collection<ComponentIdResolverNode> children = directNode.getChildren().values();
        for (ComponentIdResolverNode node : children) {
            buildInversedFilteredTreeRecursively(node);
        }
    }

    protected void clearAllUnresolvedIds() {
        rootNode.clearChildren();
        absoluteIds = null;
    }

    protected boolean hasUnresolvedIds() {
        return rootNode.hasChildren();
    }

    protected boolean findComponentsInContainerRecursively(UIComponent component, ComponentIdResolverNode node) {
        if (!hasUnresolvedIds()) {
            return true;
        }

        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                if (findComponentsInContainer(child, node, false)) {
                    return true;
                }
            }
        }

        if (component.getFacetCount() > 0) {
            for (UIComponent child : component.getFacets().values()) {
                if (findComponentsInContainer(child, node, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean resolveId(ComponentIdResolverNode node, UIComponent topMatch, UIComponent bottomMatch) {
        boolean shouldStop = false;
        Set<String> fullIds = node.getFullIds();
        if (isNotEmpty(fullIds)) {
            for (String fullId : fullIds) {
                unresolvedIds.remove(fullId);

                String metaComponentId;

                int idx = fullId.indexOf(META_COMPONENT_SEPARATOR_CHAR);
                if (idx >= 0) {
                    metaComponentId = fullId.substring(idx + 1);
                } else {
                    metaComponentId = null;
                }

                String resolvedId = null;

                if (metaComponentId != null && metaComponentId.length() != 0) {
                    resolvedId = resolveMetaComponentId(facesContext, bottomMatch, metaComponentId);

                    if (resolvedId == null) {
                        resolvedId = substituteUnresolvedMetaComponentId(facesContext, bottomMatch, metaComponentId);
                    }

                    if (resolvedId == null) {
                        resolvedId = metaComponentSubstitutions.get(metaComponentId);
                    }
                }

                if (ContextUtils.GLOBAL_META_COMPONENTS.contains(resolvedId)) {
                    resolvedIds.clear();
                    resolvedIds.add(resolvedId);

                    shouldStop = true;
                    break;
                } else {
                    if (resolvedId != null) {
                        String predefinedMetaComponentId = ContextUtils.INSTANCE.getPredefinedMetaComponentId(
                            facesContext, bottomMatch, resolvedId);

                        if (predefinedMetaComponentId != null) {
                            resolvedId = predefinedMetaComponentId;
                        }
                        resolvedIds.add(resolvedId);
                    } else {
                        resolvedIds.addAll(computeClientIds(facesContext, topMatch, bottomMatch, fullId));
                    }
                }
            }
            node.resetFullIds();
        }

        if (shouldStop) {
            clearAllUnresolvedIds();
        }

        return shouldStop;
    }

    protected boolean findComponentsInContainer(UIComponent component, ComponentIdResolverNode node, boolean resolveNCChildren) {
        ComponentIdResolverNode matchedChild = node.getChild(component.getId());
        if (matchedChild != null) {
            try {
                if (rootNode.equals(node)) {
                    containerTopMatchComponent = component;
                }

                if (resolveId(matchedChild, containerTopMatchComponent, component)) {
                    return true;
                }

                if (matchedChild.hasChildren()) {
                    if (component instanceof NamingContainer) {
                        if (findComponentsInContainerRecursively(component, matchedChild)) {
                            return true;
                        }
                    }
                } else {
                    matchedChild.remove();
                    if (!hasUnresolvedIds()) {
                        return true;
                    }
                }
            } finally {
                if (rootNode.equals(node)) {
                    containerTopMatchComponent = null;
                }
            }
        }

        if (!(component instanceof NamingContainer) || resolveNCChildren) {
            return findComponentsInContainerRecursively(component, node);
        }

        return false;
    }

    protected void matchStackedComponents() {
        UIComponent bottomMatchComponent = componentsStack.getFirst();
        if (rootNode.getChild(bottomMatchComponent.getId()) != null) {
            Iterator<UIComponent> iterator = componentsStack.iterator();
            ComponentIdResolverNode node = rootNode;

            while (iterator.hasNext() && node != null) {
                UIComponent component = iterator.next();

                node = node.getChild(component.getId());

                if (node != null) {
                    if (resolveId(node, component, bottomMatchComponent)) {
                        break;
                    }

                    if (!node.hasChildren()) {
                        node.remove();
                    }
                }
            }
        }
    }

    protected boolean findComponentsBelowRecursively(UIComponent component) {
        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                if (findComponentsBelow(child)) {
                    return true;
                }
            }
        }
        if (component.getFacetCount() > 0) {
            for (UIComponent facet : component.getFacets().values()) {
                if (findComponentsBelow(facet)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean findComponentsBelow(UIComponent component) {
        if (componentsStack == null) {
            componentsStack = new LinkedList<UIComponent>();
        }

        componentsStack.addFirst(component);
        matchStackedComponents();

        boolean result;
        boolean componentRemoved = false;

        if (hasUnresolvedIds()) {
            if (!(component instanceof NamingContainer)) {
                componentRemoved = true;
                componentsStack.removeFirst();
            }

            result = findComponentsBelowRecursively(component);
        } else {
            result = true;
        }

        if (!componentRemoved) {
            componentsStack.removeFirst();
        }

        return result;
    }

    public void resolve(UIComponent component) {
        assert component != null;

        UIComponent c = component;

        // meta-components-only IDs like "@region" are handled by this line
        resolveId(rootNode, c, c);

        if (hasUnresolvedIds()) {
            UIComponent container = findContainer(c);
            while (container != null && !isRoot(container)) {
                boolean resolutionResult = findComponentsInContainer(container, rootNode, true);
                if (resolutionResult) {
                    break;
                }

                container = findContainer(container.getParent());
            }
        }

        UIComponent root = findRoot(c);

        if (isNotEmpty(absoluteIds)) {
            for (String absoluteId : absoluteIds) {
                addIdImmediately(absoluteId);
            }

            absoluteIds.clear();
        }

        if (hasUnresolvedIds()) {
            findComponentsInContainer(root, rootNode, true);
        }

        if (hasUnresolvedIds()) {
            ComponentIdResolverNode directTreeRootNode = rootNode;
            rootNode = new ComponentIdResolverNode(null, null);
            buildInversedFilteredTreeRecursively(directTreeRootNode);

            findComponentsBelow(root);
        }

        // TODO nick - LOG here?
        resolvedIds.addAll(unresolvedIds);
    }
}
