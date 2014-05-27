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
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.richfaces.component.MetaComponentProcessor;

/**
 * Wraps {@link VisitCallback} in order to be able execute own logic during {@link #visit(VisitContext, UIComponent)}
 * processing.
 *
 * This wrapper allows to process {@link MetaComponentProcessor} components.
 *
 * @author Nick Belaevski
 *
 */
final class MetaComponentProcessingVisitCallback implements VisitCallback {
    private FacesContext facesContext;
    private VisitCallback wrapped;

    MetaComponentProcessingVisitCallback(VisitCallback visitCallbackToWrap, FacesContext context) {
        super();
        this.facesContext = context;
        this.wrapped = visitCallbackToWrap;
    }

    /**
     * If context contains a {@link ExtendedVisitContext#META_COMPONENT_ID} a given target component will be treated as
     * {@link MetaComponentProcessor} and its {@link MetaComponentProcessor#processMetaComponent(FacesContext, String)} is
     * called.
     *
     * Otherwise the processor delegates to wrapped {@link VisitCallback} instance.
     */
    public VisitResult visit(VisitContext context, UIComponent target) {
        if (target instanceof MetaComponentProcessor) {
            String metaComponentId = (String) facesContext.getAttributes().get(ExtendedVisitContext.META_COMPONENT_ID);
            if (metaComponentId != null) {
                MetaComponentProcessor executor = (MetaComponentProcessor) target;
                executor.processMetaComponent(facesContext, metaComponentId);

                // Once we visit a component, there is no need to visit
                // its children, since processDecodes/Validators/Updates and
                // encodeAll() already traverse the subtree. We return
                // VisitResult.REJECT to supress the subtree visit.
                return VisitResult.REJECT;
            }
        }

        return wrapped.visit(context, target);
    }
}
