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

import static org.richfaces.component.MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import org.richfaces.component.AjaxContainer;
import org.richfaces.component.MetaComponentResolver;
import org.richfaces.renderkit.util.CoreRendererUtils;

/**
 * @author Nick Belaevski
 *
 */
public final class ComponentIdResolver {

    private static Map<String, String> metaComponentSubstitutions = new HashMap<String, String>();

    static {
        metaComponentSubstitutions.put(AjaxContainer.META_COMPONENT_ID, AjaxContainer.DEFAULT_RENDER_ID);
    }

    private Set<String> resolvedIds;

    private Set<String> unresolvedIds;

    private Set<String> absoluteIds = null;

    private UIComponent containerTopMatchComponent;

    private LinkedList<UIComponent> componentsStack = null;

    private IdParser idParser;

    private char namingContainerSeparator;

    private FacesContext facesContext;

    private ComponentIdResolverNode rootNode;

    public ComponentIdResolver(FacesContext facesContext) {
        super();

        this.facesContext = facesContext;

        this.resolvedIds = new HashSet<String>();
        this.unresolvedIds = new HashSet<String>();
        this.rootNode = new ComponentIdResolverNode(null, null);
        this.namingContainerSeparator = UINamingContainer.getSeparatorChar(facesContext);
        this.idParser = new IdParser(namingContainerSeparator, MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR);
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

    private static String resolveMetaComponentId(FacesContext context, UIComponent component, String metaComponentId) {
        UIComponent c = component;
        while (c != null) {
            if (c instanceof MetaComponentResolver) {
                MetaComponentResolver metaComponentResolver = (MetaComponentResolver) c;

                String resolvedId = metaComponentResolver.resolveClientId(context, component, metaComponentId);

                if (resolvedId != null) {
                    return resolvedId;
                }
            }

            c = c.getParent();
        }

        return metaComponentSubstitutions.get(metaComponentId);
    }

    //used in unit tests
    static void setMetaComponentSubstitutions(Map<String, String> substitutionsMap) {
        metaComponentSubstitutions = substitutionsMap;
    }
    
    private String computeClientId(FacesContext context,
        UIComponent topMatchComponent, String id) {

        UIComponent container = findContainer(topMatchComponent.getParent());

        String containerClientId = null;

        if (container instanceof NamingContainer) {
            containerClientId = container.getContainerClientId(context);
        }

        if (containerClientId != null && containerClientId.length() != 0) {
            StringBuilder builder = new StringBuilder(containerClientId.length() + 1 /* separator */ + id.length());
            builder.append(containerClientId);
            builder.append(namingContainerSeparator);
            builder.append(id);
            return builder.toString();
        } else {
            return id;
        }
    }

    protected void addIdImmediately(String id) {
        idParser.setId(id);

        ComponentIdResolverNode node = rootNode;

        while (idParser.findNext()) {
            String componentId = idParser.getComponentId();

            if (componentId.length() == 0) {
                continue;
            }

            if (BaseExtendedVisitContext.ANY_WILDCARD.equals(componentId)) {
                continue;
            }

            if (componentId.length() > 2 && componentId.charAt(0) == '[' &&
                componentId.charAt(componentId.length() - 1) == ']') {

                continue;
            }

            node = node.getOrCreateChild(componentId);
        }

        unresolvedIds.add(id);
        node.addFullId(id);
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
        return id.charAt(0) == namingContainerSeparator;
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
                }

                if (CoreRendererUtils.GLOBAL_META_COMPONENTS.contains(resolvedId)) {
                    resolvedIds.clear();
                    resolvedIds.add(resolvedId);

                    shouldStop = true;
                    break;
                } else {
                    if (resolvedId != null) {
                        String predefinedMetaComponentId = CoreRendererUtils.INSTANCE.getPredefinedMetaComponentId(facesContext, 
                            bottomMatch, resolvedId);
                        
                        if (predefinedMetaComponentId != null) {
                            resolvedId = predefinedMetaComponentId;
                        }
                        resolvedIds.add(resolvedId);
                    } else {
                        String computedId = computeClientId(facesContext, topMatch, fullId);
                        resolvedIds.add(computedId);
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

        //meta-components-only IDs like "@region" are handled by this line
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

        //TODO nick - LOG here?
        resolvedIds.addAll(unresolvedIds);
    }
}
