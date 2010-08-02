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

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import org.richfaces.component.UISequence;

/**
 * @author Nick Belaevski
 *
 */
public class AjaxTableComponentImpl extends UISequence {

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

    @Override
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
            return super.visitFixedChildren(visitContext, callback);
        }
    }

}
