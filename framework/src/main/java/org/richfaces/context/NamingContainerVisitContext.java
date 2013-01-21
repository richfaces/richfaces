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

final class NamingContainerVisitContext extends ExtendedVisitContext {
    private final class IdsProxyCollection extends AbstractCollection<String> {
        @Override
        public Iterator<String> iterator() {
            throw new UnsupportedOperationException("iterator() method is not supported by this collection implementation");
        }

        @Override
        public int size() {
            throw new UnsupportedOperationException("size() method is not supported by this collection implementation");
        }

        @Override
        public boolean isEmpty() {
            return ids.isEmpty();
        }
    }

    ;

    private Set<String> ids;
    private IdsProxyCollection idsToVisit;
    private UIComponent startingComponent;

    public NamingContainerVisitContext(FacesContext facesContext, ExtendedVisitContextMode visitMode, UIComponent component,
        Collection<String> ids) {

        super(facesContext, visitMode);

        // Make sure component is a NamingContainer
        if (!(component instanceof NamingContainer)) {
            throw new IllegalArgumentException("Component is not a NamingContainer: " + component);
        }

        this.ids = new HashSet<String>(ids);
        this.idsToVisit = new IdsProxyCollection();
        this.startingComponent = component;
    }

    @Override
    public VisitResult invokeVisitCallback(UIComponent component, VisitCallback callback) {
        VisitResult result = VisitResult.ACCEPT;
        String id = buildExtendedComponentId(component);
        if (ids.contains(id)) {
            result = callback.visit(this, component);
            ids.remove(id);

            if (ids.isEmpty()) {
                return VisitResult.COMPLETE;
            }
        }

        // cancel visiting children for nested naming containers
        if (component instanceof NamingContainer && !component.equals(startingComponent)) {
            return VisitResult.REJECT;
        }

        return result;
    }

    @Override
    public Collection<String> getSubtreeIdsToVisit(UIComponent component) {
        if (!(component instanceof NamingContainer)) {
            throw new IllegalArgumentException(component.toString());
        }

        // TODO nick - check clientId, e.g. to avoid visiting components with client ids like
        // "table:0:nested" ("table" is a starting component)
        if (startingComponent.equals(component)) {
            return VisitContext.ALL_IDS;
        }

        return Collections.emptySet();
    }

    public Collection<String> getDirectSubtreeIdsToVisit(UIComponent component) {
        if (!(component instanceof NamingContainer)) {
            throw new IllegalArgumentException(component.toString());
        }

        if (startingComponent.equals(component)) {
            return Collections.unmodifiableCollection(ids);
        }

        return Collections.emptySet();
    }

    @Override
    public Set<VisitHint> getHints() {
        return Collections.emptySet();
    }

    @Override
    public Collection<String> getIdsToVisit() {
        return idsToVisit;
    }

    public VisitContext createNamingContainerVisitContext(UIComponent component, Collection<String> directIds) {
        return new NamingContainerVisitContext(getFacesContext(), getVisitMode(), component, directIds);
    }
}
