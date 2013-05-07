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

import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 *
 */
public class AjaxTableComponentImpl extends UIData {
    private static final Logger LOG = RichfacesLogger.COMPONENTS.getLogger();

    private boolean visitMetaComponent(String name, ExtendedVisitContext visitContext, VisitCallback callback) {
        UIComponent facet = getFacet(name);
        if (facet != null) {
            VisitResult result = visitContext.invokeMetaComponentVisitCallback(this, callback, name);

            if (result == VisitResult.ACCEPT) {
                if (facet.visitTree(visitContext, callback)) {
                    result = VisitResult.COMPLETE;
                }
            }

            return result == VisitResult.COMPLETE;
        } else {
            return false;
        }
    }

    private boolean doVisitChildren(VisitContext context) {
        // TODO optimize for returned IDs
        Collection<String> idsToVisit = context.getSubtreeIdsToVisit(this);

        assert idsToVisit != null;

        // All ids or non-empty collection means we need to visit our children.
        return !idsToVisit.isEmpty();
    }

    @Override
    public boolean visitTree(VisitContext visitContext, VisitCallback callback) {

        // First check to see whether we are visitable. If not
        // short-circuit out of this subtree, though allow the
        // visit to proceed through to other subtrees.
        if (!isVisitable(visitContext)) {
            return false;
        }

        // Clear out the row index is one is set so that
        // we start from a clean slate.
        FacesContext facesContext = visitContext.getFacesContext();

        int oldRowIndex = getRowIndex();
        setRowIndex(-1);

        // Push ourselves to EL
        pushComponentToEL(facesContext, null);

        try {

            // Visit ourselves. Note that we delegate to the
            // VisitContext to actually perform the visit.
            VisitResult result = visitContext.invokeVisitCallback(this, callback);

            // If the visit is complete, short-circuit out and end the visit
            if (result == VisitResult.COMPLETE) {
                return true;
            }

            // Visit children, short-circuiting as necessary
            if ((result == VisitResult.ACCEPT) && doVisitChildren(visitContext)) {
                setRowIndex(-1);

                if (visitFixedChildren(visitContext, callback)) {
                    return true;
                }

                if (visitDataChildren(visitContext, callback)) {
                    return true;
                }
            }
        } finally {

            // Clean up - pop EL and restore old row index
            popComponentFromEL(facesContext);

            try {
                setRowIndex(oldRowIndex);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }

        // Return false to allow the visit to continue
        return false;
    }

    protected boolean visitFixedChildren(VisitContext visitContext, VisitCallback callback) {

        if (visitContext instanceof ExtendedVisitContext) {
            ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) visitContext;

            if (visitMetaComponent("header", extendedVisitContext, callback)) {
                return true;
            }

            if (visitMetaComponent("footer", extendedVisitContext, callback)) {
                return true;
            }

            return false;
        } else {
            for (UIComponent facet : getFacets().values()) {
                if (facet.visitTree(visitContext, callback)) {
                    return true;
                }
            }

            return false;
        }
    }

    protected boolean visitDataChildren(VisitContext visitContext, VisitCallback callback) {
        int rowIndex = 0;

        for (setRowIndex(rowIndex); isRowAvailable(); setRowIndex(++rowIndex)) {
            VisitResult result = visitContext.invokeVisitCallback(this, callback);

            if (result == VisitResult.COMPLETE) {
                return true;
            }

            if (result == VisitResult.REJECT) {
                continue;
            }

            for (UIComponent child : getChildren()) {
                if (child.visitTree(visitContext, callback)) {
                    return true;
                }
            }
        }

        return false;
    }
}
