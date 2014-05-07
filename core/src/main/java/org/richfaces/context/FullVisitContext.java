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

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import java.util.Collection;
import java.util.Set;

/**
 * A VisitContext used to visit all children and trees.  Use visitHints to filter tune which components are visited.
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class FullVisitContext extends VisitContext {
    private final FacesContext facesContext;
    private final Set<VisitHint> visitHints;

    public FullVisitContext(FacesContext facesContext, Set<VisitHint> visitHints) {
        this.facesContext = facesContext;
        this.visitHints = visitHints;
    }

    @Override
    public FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    public Collection<String> getIdsToVisit() {
        return VisitContext.ALL_IDS;
    }

    @Override
    public Collection<String> getSubtreeIdsToVisit(UIComponent component) {
        return VisitContext.ALL_IDS;
    }

    @Override
    public VisitResult invokeVisitCallback(UIComponent component, VisitCallback callback) {
        return callback.visit(this, component);
    }

    @Override
    public Set<VisitHint> getHints() {
        return visitHints;
    }
}
