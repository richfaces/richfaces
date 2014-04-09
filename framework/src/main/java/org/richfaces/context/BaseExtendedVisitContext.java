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
import java.util.EnumSet;
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
 * @author Nick Belaevski
 *
 */
public class BaseExtendedVisitContext extends ExtendedVisitContext {
    protected interface ClientIdVisitor {
        void visitSubtreeId(String baseId, String clientId);

        void visitDirectSubtreeId(String baseId, String shortId);

        void visitShortId(String shortId);
    }

    protected final ClientIdVisitor addNodeVisitor = new ClientIdVisitor() {
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
    protected final ClientIdVisitor removeNodeVisitor = new ClientIdVisitor() {
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

    // The client ids to visit
    private Collection<String> clientIds;
    private Collection<String> shortIds;
    private SetMultimap<String, String> subtreeIds;
    private ListMultimap<String, String> directSubtreeIds;
    // Our visit hints
    private Set<VisitHint> hints;
    private CollectionProxy proxiedClientIds;

    /**
     * Creates a PartialVisitorContext instance with the specified hints.
     *
     * @param facesContext the FacesContext for the current request
     * @param clientIds the client ids of the components to visit
     * @param hints a the VisitHints for this visit
     * @throws NullPointerException if {@code facesContext} is {@code null}
     */
    public BaseExtendedVisitContext(FacesContext facesContext, Collection<String> clientIds, Set<VisitHint> hints,
        ExtendedVisitContextMode contextMode) {

        super(facesContext, contextMode);

        // Initialize our various collections
        initializeCollections(clientIds);

        // Copy and store hints - ensure unmodifiable and non-empty
        EnumSet<VisitHint> hintsEnumSet = ((hints == null) || (hints.isEmpty())) ? EnumSet.noneOf(VisitHint.class) : EnumSet
            .copyOf(hints);

        this.hints = Collections.unmodifiableSet(hintsEnumSet);
    }

    protected void visitClientId(String clientId, ClientIdVisitor visitor) {
        IdSplitIterator splitIterator = new IdSplitIterator(clientId);

        boolean isFirstIteration = true;

        while (splitIterator.hasNext()) {
            String shortId = splitIterator.next();
            String subtreeId = splitIterator.getSubtreeId();

            int metaSepIdx = shortId.indexOf(META_COMPONENT_SEPARATOR_CHAR);

            if (subtreeId != null) {
                visitor.visitSubtreeId(subtreeId, clientId);
                visitor.visitDirectSubtreeId(subtreeId, shortId);
            }

            if (metaSepIdx >= 0) {
                String componentId = shortId.substring(0, metaSepIdx);

                String extraBaseId = SeparatorChar.JOINER.join(subtreeId, componentId);
                visitor.visitDirectSubtreeId(extraBaseId, shortId);
                visitor.visitSubtreeId(extraBaseId, clientId);
            }

            if (isFirstIteration) {
                isFirstIteration = false;
                visitor.visitShortId(shortId);
            }
        }
    }

    private boolean addNode(String clientId) {
        if (clientIds.add(clientId)) {
            visitClientId(clientId, addNodeVisitor);

            return true;
        }

        return false;
    }

    private void removeNode(String clientId, boolean removeFromClientIds) {
        if (!removeFromClientIds || clientIds.remove(clientId)) {
            visitClientId(clientId, removeNodeVisitor);
        }
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
        return proxiedClientIds;
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

    protected void addDirectSubtreeIdsToVisitForImplicitComponents(UIComponent component, Set<String> result) {
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

    // Called to initialize our various collections.
    private void initializeCollections(Collection<String> clientIds) {
        this.subtreeIds = HashMultimap.create();
        this.directSubtreeIds = ArrayListMultimap.create();

        this.shortIds = new HashSet<String>();

        this.clientIds = Sets.newHashSet();

        this.proxiedClientIds = new CollectionProxy();
        this.proxiedClientIds.addAll(clientIds);
    }

    public VisitContext createNamingContainerVisitContext(UIComponent component, Collection<String> directIds) {
        return new NamingContainerVisitContext(getFacesContext(), getVisitMode(), component, directIds);
    }
}
