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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.richfaces.util.SeparatorChar;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

/**
 * {@link ExtendedVisitContext} that allows track visit of implicitly processed subtrees and adds support for shortIds
 *
 * @author Nick Belaevski
 */
public class BaseExtendedVisitContext extends ExtendedVisitContext {

    // The client ids to visit
    private Collection<String> clientIds;
    private Collection<String> shortIds;
    private SetMultimap<String, String> subtreeIds;
    private ListMultimap<String, String> directSubtreeIds;
    private CollectionProxy proxiedClientIds;

    /**
     * Creates a PartialVisitorContext instance with the specified hints.
     *
     * @param facesContext the FacesContext for the current request
     * @param clientIds the client ids of the components to visit
     * @param hints a the VisitHints for this visit
     * @throws NullPointerException if {@code facesContext} is {@code null}
     */
    public BaseExtendedVisitContext(VisitContext visitContextToWrap, FacesContext facesContext, Collection<String> clientIds, Set<VisitHint> hints,
        ExtendedVisitContextMode contextMode) {

        super(visitContextToWrap, facesContext, contextMode);

        // Initialize our various collections
        initializeCollections(clientIds);
    }

    // Called to initialize our various collections.
    private void initializeCollections(Collection<String> clientIds) {
        this.subtreeIds = HashMultimap.create();
        this.directSubtreeIds = ArrayListMultimap.create();

        this.shortIds = new HashSet<String>();

        this.clientIds = Sets.newHashSet();

        // creates a proxy that allows to track subtree/shortIds to visit
        this.proxiedClientIds = new CollectionProxy();
        this.proxiedClientIds.addAll(clientIds);
    }

    /**
     * @see VisitContext#invokeVisitCallback VisitContext.invokeVisitCallback()
     */
    @Override
    public VisitResult invokeVisitCallback(UIComponent component, VisitCallback callback) {
        if (shortIds.contains(buildExtendedComponentId(component))) {
            String clientId = buildExtendedClientId(component);

            if (clientIds.contains(clientId)) {
                VisitResult visitResult = callback.visit(this, component);

                removeNode(clientId, true);

                if (clientIds.isEmpty() && shouldCompleteOnEmptyIds()) {
                    return VisitResult.COMPLETE;
                } else {
                    return visitResult;
                }
            }
        }

        return invokeVisitCallbackForImplicitComponent(component, callback);
    }

    protected VisitResult invokeVisitCallbackForImplicitComponent(UIComponent component, VisitCallback callback) {
        return VisitResult.ACCEPT;
    }

    /**
     * Adds a clientId to a list of tracked subtrees/shortIds
     */
    private boolean addNode(String clientId) {
        if (clientIds.add(clientId)) {
            visitClientId(clientId, addNode);

            return true;
        }

        return false;
    }

    /**
     * Removes a clientId from a list of tracked subtrees/shortIds
     */
    private void removeNode(String clientId, boolean removeFromClientIds) {
        if (!removeFromClientIds || clientIds.remove(clientId)) {
            visitClientId(clientId, removeNode);
        }
    }

    /**
     * Use the {@link ClientIdTrackingStrategy} implementations to either add or remove subtrees/shortIds collections
     */
    protected void visitClientId(String clientId, ClientIdTrackingStrategy tracker) {
        IdSplitIterator splitIterator = new IdSplitIterator(clientId);

        boolean isFirstIteration = true;

        while (splitIterator.hasNext()) {
            String shortId = splitIterator.next();
            String subtreeId = splitIterator.getSubtreeId();

            int metaSepIdx = shortId.indexOf(META_COMPONENT_SEPARATOR_CHAR);

            if (subtreeId != null) {
                tracker.visitSubtreeId(subtreeId, clientId);
                tracker.visitDirectSubtreeId(subtreeId, shortId);
            }

            if (metaSepIdx >= 0) {
                String componentId = shortId.substring(0, metaSepIdx);

                String extraBaseId = SeparatorChar.JOINER.join(subtreeId, componentId);
                tracker.visitDirectSubtreeId(extraBaseId, shortId);
                tracker.visitSubtreeId(extraBaseId, clientId);
            }

            if (isFirstIteration) {
                isFirstIteration = false;
                tracker.visitShortId(shortId);
            }
        }
    }

    /**
     * @see VisitContext#getIdsToVisit VisitContext.getIdsToVisit()
     */
    @Override
    public Collection<String> getIdsToVisit() {

        // We just return our clientIds collection. This is
        // the modifiable (but proxied) collection of all of
        // the client ids to visit.
        return proxiedClientIds;
    }

    /**
     * Return true whether this {@link VisitContext} allows to visit implicit IDs
     */
    protected boolean hasImplicitSubtreeIdsToVisit(UIComponent component) {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see javax.faces.component.visit.VisitContextWrapper#getSubtreeIdsToVisit(javax.faces.component.UIComponent)
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

        Collection<String> result;

        Set<String> ids = subtreeIds.get(clientId);
        if (!ids.isEmpty()) {
            result = Collections.unmodifiableCollection(ids);
        } else {
            // returned collection should be non-modifiable
            result = Collections.emptySet();
        }

        return result;
    }

    public Collection<String> getDirectSubtreeIdsToVisit(UIComponent component) {
        // Make sure component is a NamingContainer
        if (!(component instanceof NamingContainer)) {
            throw new IllegalArgumentException("Component is not a NamingContainer: " + component);
        }

        String clientId = component.getClientId(getFacesContext());

        Set<String> result = new HashSet<String>(directSubtreeIds.get(clientId));

        addDirectSubtreeIdsToVisitForImplicitComponents(component, result);

        if (result != null && !result.isEmpty()) {
            return Collections.unmodifiableCollection(result);
        } else {
            return Collections.emptySet();
        }
    }

    /**
     * Allows to add subtrees that contains implicit components to list IDs to visit
     */
    protected void addDirectSubtreeIdsToVisitForImplicitComponents(UIComponent component, Set<String> result) {
    }

    protected boolean shouldCompleteOnEmptyIds() {
        return true;
    }

    public VisitContext createNamingContainerVisitContext(UIComponent component, Collection<String> directIds) {
        return new NamingContainerVisitContext(this, getFacesContext(), getVisitMode(), component, directIds);
    }

    private final class CollectionProxy extends AbstractCollection<String> {
        private CollectionProxy() {
        }

        @Override
        public boolean isEmpty() {
            return clientIds.isEmpty();
        }

        @Override
        public int size() {
            return clientIds.size();
        }

        @Override
        public Iterator<String> iterator() {
            return new IteratorProxy(clientIds.iterator());
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
                removeNode(current, false);

                current = null;
            }

            wrapped.remove();
        }
    }

    /**
     * Allows to track what subtrees and shortIds were visited
     */
    protected interface ClientIdTrackingStrategy {
        void visitSubtreeId(String baseId, String clientId);

        void visitDirectSubtreeId(String baseId, String shortId);

        void visitShortId(String shortId);
    }

    /**
     * Tracking strategy that adds a node to the list of tracked subtree/shortIds
     */
    protected final ClientIdTrackingStrategy addNode = new ClientIdTrackingStrategy() {
        public void visitSubtreeId(String baseId, String clientId) {
            subtreeIds.put(baseId, clientId);
        }

        public void visitDirectSubtreeId(String baseId, String shortId) {
            directSubtreeIds.put(baseId, shortId);
        }

        public void visitShortId(String shortId) {
            shortIds.add(shortId);
        }
    };

    /**
     * Tracking strategy that removes a node from the list of tracked subtree/shortIds
     */
    protected final ClientIdTrackingStrategy removeNode = new ClientIdTrackingStrategy() {
        public void visitSubtreeId(String baseId, String clientId) {
            subtreeIds.remove(baseId, clientId);
        }

        public void visitShortId(String shortId) {
            // do nothing
        }

        public void visitDirectSubtreeId(String baseId, String shortId) {
            directSubtreeIds.remove(baseId, shortId);
        }
    };
}
