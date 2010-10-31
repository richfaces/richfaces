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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.richfaces.component.MetaComponentResolver;

/**
 * @author Nick Belaevski
 *
 */
public class BaseExtendedVisitContext extends ExtendedVisitContext {

    static final String ANY_WILDCARD = "*";

    private static final int SHORT_ID_IN_CLIENTID_SEGMENTS_NUMBER = 2;

    private final class CollectionProxy extends AbstractCollection<String> {

        private CollectionProxy() {
        }

        @Override
        public boolean isEmpty() {
            return directNodesMap.isEmpty();
        }

        @Override
        public int size() {
            return directNodesMap.size();
        }

        @Override
        public Iterator<String> iterator() {
            return new IteratorProxy(directNodesMap.keySet().iterator());
        }

        @Override
        public boolean add(String o) {
            return addNode(o);
        }
    }
    // Little proxy collection implementation. We proxy the id
    // collection so that we can detect modifications and update
    // our internal state when ids to visit are added or removed.

    // Little proxy iterator implementation used by CollectionProxy
    // so that we can catch removes.
    private final class IteratorProxy implements Iterator<String> {

        private Iterator<String> wrapped;

        private String current = null;

        private IteratorProxy(Iterator<String> wrapped) {
            this.wrapped = wrapped;
        }

        public boolean hasNext() {
            return wrapped.hasNext();
        }

        public String next() {
            current = wrapped.next();

            return current;
        }

        public void remove() {
            if (current != null) {
                ComponentMatcherNode node = directNodesMap.get(current);
                removeNode(node);

                current = null;
            }

            wrapped.remove();
        }
    }

    private interface NodeOperationCommand {

        public ComponentMatcherNode getNextNode(ComponentMatcherNode currentNode, String nodeId, boolean isPattern);

        public boolean processLastNode(ComponentMatcherNode lastNode, String fullId);
    }

    private NodeOperationCommand addNodeOperation = new NodeOperationCommand() {

        public boolean processLastNode(ComponentMatcherNode lastNode, String fullId) {
            if (!directNodesMap.containsKey(fullId)) {
                directNodesMap.put(fullId, lastNode);
                lastNode.markAdded();

                ComponentMatcherNode n = lastNode;
                int addedSegmentsCount = 0;
                while (n != null && addedSegmentsCount < SHORT_ID_IN_CLIENTID_SEGMENTS_NUMBER) {
                    if (!n.isPatternNode() && !n.isMetaComponentNode()) {
                        String shortId = n.getSource();
                        if (shortId != null) {
                            addedSegmentsCount++;
                            //TODO filter meta component ids
                            shortIds.add(shortId);
                        }
                    }

                    n = n.getParentNode();
                }

                if (!lastNode.hasPatternNodeInChain()) {
                    lastNode.addSubtreeId(fullId);
                }

                return true;
            }

            return false;
        }

        public ComponentMatcherNode getNextNode(ComponentMatcherNode currentNode, String nodeId, boolean isPattern) {
            return currentNode.getOrCreateChild(nodeId, isPattern);
        }
    };

    private NodeOperationCommand removeNodeOperation = new NodeOperationCommand() {

        public boolean processLastNode(ComponentMatcherNode lastNode, String fullId) {
            ComponentMatcherNode node = directNodesMap.remove(fullId);
            if (node != null) {
                if (!node.hasPatternNodeInChain()) {
                    node.removeSubtreeId(fullId);
                }

                removeNode(node);

                return true;
            }

            return false;
        }

        public ComponentMatcherNode getNextNode(ComponentMatcherNode currentNode, String nodeId, boolean isPattern) {
            return currentNode.getChild(nodeId, isPattern);
        }
    };

    private IdParser idParser;

    // The client ids to visit
    private Collection<String> clientIds;

    private Collection<String> shortIds;

    // Our visit hints
    private Set<VisitHint> hints;

    private ComponentMatcherNode rootNode;

    private Map<String, ComponentMatcherNode> directNodesMap;

    /**
     * Creates a PartialVisitorContext instance with the specified hints.
     *
     * @param facesContext
     *            the FacesContext for the current request
     * @param clientIds
     *            the client ids of the components to visit
     * @param hints
     *            a the VisitHints for this visit
     * @throws NullPointerException
     *             if {@code facesContext} is {@code null}
     */
    public BaseExtendedVisitContext(FacesContext facesContext, Collection<String> clientIds, Set<VisitHint> hints,
        ExtendedVisitContextMode contextMode) {

        super(facesContext, contextMode);

        // Initialize our various collections
        initializeCollections(clientIds);

        // Copy and store hints - ensure unmodifiable and non-empty
        EnumSet<VisitHint> hintsEnumSet = ((hints == null) || (hints.isEmpty())) ? EnumSet.noneOf(VisitHint.class)
            : EnumSet.copyOf(hints);

        this.hints = Collections.unmodifiableSet(hintsEnumSet);
    }

    private IdParser setupIdParser(String id) {
        if (idParser == null) {
            idParser = new IdParser(UINamingContainer.getSeparatorChar(getFacesContext()),
                MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR);
        }

        idParser.setId(id);

        return idParser;
    }

    private ComponentMatcherNode findMatchingNode(String clientId) {
        ComponentMatcherNode node = rootNode;

        IdParser idParser = setupIdParser(clientId);

        while (node != null && idParser.findNext()) {
            String componentId = idParser.getComponentId();
            String metadataComponentId = idParser.getMetadataComponentId();

            if (metadataComponentId != null) {
                node = node.getChild(componentId, false);
                if (node != null) {
                    node = node.getChild(metadataComponentId, false);
                }
            } else {
                node = node.getMatchedChild(componentId);
            }
        }

        return node;
    }

    private ComponentMatcherNode findAddedNode(String clientId) {
        ComponentMatcherNode node = findMatchingNode(clientId);

        if (node != null && !node.isAdded()) {
            node = null;
        }

        return node;
    }

    private void removeNode(ComponentMatcherNode nodeToRemove) {
        nodeToRemove.markRemoved();

        ComponentMatcherNode node = nodeToRemove;
        while (node != null && !node.hasDirectChildren()) {
            ComponentMatcherNode parentNode = node.getParentNode();
            if (parentNode != null) {
                parentNode.removeChild(node);
                node = parentNode;
            } else {
                break;
            }
        }
    }

    private boolean invokeNodeOperation(NodeOperationCommand command, ComponentMatcherNode currentNode,
        IdTreeNode idTreeNode, StringBuilder sb) {

        String componentId = idTreeNode.getComponentId();
        String metadataComponentId = idTreeNode.getMetadataComponentId();

        ComponentMatcherNode nextNode;

        if (metadataComponentId != null) {
            nextNode = command.getNextNode(currentNode, componentId, false);
            if (nextNode != null) {
                nextNode = command.getNextNode(nextNode, metadataComponentId, false);
                nextNode.setMetaComponentNode(true);
            }
        } else {
            boolean isPattern = ANY_WILDCARD.equals(componentId);
            nextNode = command.getNextNode(currentNode, componentId, isPattern);
        }

        boolean result = false;

        if (nextNode != null) {
            final int bufferLength = sb.length();
            if (bufferLength != 0) {
                //TODO replace with constant
                sb.append(':');
            }
            sb.append(componentId);
            if (metadataComponentId != null) {
                sb.append(metadataComponentId);
            }

            List<IdTreeNode> idTreeChildNodes = idTreeNode.getChildNodes();
            if (idTreeChildNodes != null) {
                final int newBufferLength = sb.length();

                for (IdTreeNode idTreeChildNode : idTreeChildNodes) {
                    result |= invokeNodeOperation(command, nextNode, idTreeChildNode, sb);

                    sb.setLength(newBufferLength);
                }
            } else {
                result |= command.processLastNode(nextNode, sb.toString());
            }

            sb.setLength(bufferLength);
        }

        return result;
    }

    private boolean invokeRootNodeOperation(NodeOperationCommand command, IdTreeNode idTreeNode) {
        boolean result = false;

        List<IdTreeNode> idTreeChildNodes = idTreeNode.getChildNodes();
        if (idTreeChildNodes != null) {
            StringBuilder sb = new StringBuilder();

            for (IdTreeNode idTreeChildNode : idTreeChildNodes) {
                result |= invokeNodeOperation(command, rootNode, idTreeChildNode, sb);
            }
        }

        return result;
    }

    private boolean addNode(String patternId) {
        IdTreeNode idTreeNode = new IdTreeNode();
        idTreeNode.appendNodesFromParser(setupIdParser(patternId));

        return invokeRootNodeOperation(addNodeOperation, idTreeNode);
    }

    private boolean removeNode(String patternId) {
        IdTreeNode idTreeNode = new IdTreeNode();
        idTreeNode.appendNodesFromParser(setupIdParser(patternId));

        return invokeRootNodeOperation(removeNodeOperation, idTreeNode);
    }

    /**
     * @see VisitContext#getHints VisitContext.getHints
     */
    @Override
    public Set<VisitHint> getHints() {
        return hints;
    }

    /**
     * @see VisitContext#getIdsToVisit VisitContext.getIdsToVisit()
     */
    @Override
    public Collection<String> getIdsToVisit() {

        // We just return our clientIds collection. This is
        // the modifiable (but proxied) collection of all of
        // the client ids to visit.
        return clientIds;
    }

    protected boolean hasImplicitSubtreeIdsToVisit(UIComponent component) {
        return false;
    }
    
    /**
     * @see VisitContext#getSubtreeIdsToVisit VisitContext.getSubtreeIdsToVisit()
     */
    @Override
    public Collection<String> getSubtreeIdsToVisit(UIComponent component) {

        // Make sure component is a NamingContainer
        if (!(component instanceof NamingContainer)) {
            throw new IllegalArgumentException("Component is not a NamingContainer: " + component);
        }

        if (hasImplicitSubtreeIdsToVisit(component)) {
            return VisitContext.ALL_IDS;
        }

        String clientId = buildExtendedClientId(component);

        ComponentMatcherNode node = findMatchingNode(clientId);

        Collection<String> result = null;


        if (node != null) {
            if (node.hasKidPatternNodes()) {
                result = VisitContext.ALL_IDS;
            } else {
                Collection<String> subtreeIds = node.getSubtreeIds();
                if (subtreeIds != null) {
                    result = Collections.unmodifiableCollection(subtreeIds);
                } else {
                    //TODO nick - this code addresses the case of parent pattern nodes, and can be optimized
                    if (node.hasDirectIdChildren()) {
                        result = VisitContext.ALL_IDS;
                    } else {
                        result = Collections.emptySet();
                    }
                }
            }
        } else {
            result = Collections.emptySet();
        }

        return result;
    }

    protected void addDirectSubtreeIdsToVisitForImplicitComponents(UIComponent component, Set<String> result) {
    }

    public Collection<String>getDirectSubtreeIdsToVisit(UIComponent component) {
        // Make sure component is a NamingContainer
        if (!(component instanceof NamingContainer)) {
            throw new IllegalArgumentException("Component is not a NamingContainer: " + component);
        }

        String clientId = component.getClientId(getFacesContext());
        ComponentMatcherNode node = findMatchingNode(clientId);

        if (node != null && node.hasDirectPatternChildren()) {
            return VisitContext.ALL_IDS;
        }

        Set<String> result = new HashSet<String>();
        if (node != null && node.hasDirectIdChildren()) {
            result.addAll(node.getIdChildren().keySet());
        }

        addDirectSubtreeIdsToVisitForImplicitComponents(component, result);

        if (result != null && !result.isEmpty()) {
            return Collections.unmodifiableCollection(result);
        } else {
            return Collections.emptySet();
        }
    }

    protected VisitResult invokeVisitCallbackForImplicitComponent(UIComponent component, VisitCallback callback) {
        return VisitResult.ACCEPT;
    }

    protected boolean shouldCompleteOnEmptyIds() {
        return true;
    }
    
    /**
     * @see VisitContext#invokeVisitCallback VisitContext.invokeVisitCallback()
     */
    @Override
    public VisitResult invokeVisitCallback(UIComponent component, VisitCallback callback) {
        if (shortIds.contains(component.getId())) {
            String clientId = buildExtendedClientId(component);
            ComponentMatcherNode node = findAddedNode(clientId);
            if (node != null) {
                VisitResult visitResult = callback.visit(this, component);

                removeNode(clientId);

                if (clientIds.isEmpty() && shouldCompleteOnEmptyIds()) {
                    return VisitResult.COMPLETE;
                } else {
                    return visitResult;
                }
            }
        }

        return invokeVisitCallbackForImplicitComponent(component, callback);
    }

    // Called to initialize our various collections.
    private void initializeCollections(Collection<String> clientIds) {
        this.rootNode = new ComponentMatcherNode();
        this.directNodesMap = new HashMap<String, ComponentMatcherNode>();
        this.shortIds = new HashSet<String>();
        this.clientIds = new CollectionProxy();
        this.clientIds.addAll(clientIds);
    }

    public VisitContext createNamingContainerVisitContext(UIComponent component, Collection<String> directIds) {
        return new NamingContainerVisitContext(getFacesContext(), getVisitMode(), component, directIds);
    }
}
