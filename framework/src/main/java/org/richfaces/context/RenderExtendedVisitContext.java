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
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitContextWrapper;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.richfaces.ui.common.AjaxOutput;

/**
 * @author Nick Belaevski
 *
 */
public class RenderExtendedVisitContext extends BaseExtendedVisitContext {
    private boolean limitRender;

    public RenderExtendedVisitContext(VisitContext visitContextToWrap, FacesContext facesContext, Collection<String> clientIds, Set<VisitHint> hints,
        boolean limitRender) {
        super(visitContextToWrap, facesContext, clientIds, hints, ExtendedVisitContextMode.RENDER);

        this.limitRender = limitRender;
    }

    @Override
    protected boolean hasImplicitSubtreeIdsToVisit(UIComponent component) {
        return !limitRender && PartialViewContextAjaxOutputTracker.hasNestedAjaxOutputs(component);
    }

    protected void addDirectSubtreeIdsToVisitForImplicitComponents(UIComponent component, Set<String> result) {
        if (!limitRender) {
            Collection<String> directChildrenIds = PartialViewContextAjaxOutputTracker.getDirectChildrenIds(component);
            if (directChildrenIds != null && !directChildrenIds.isEmpty()) {
                result.addAll(directChildrenIds);
            }
        }
    }

    protected VisitResult invokeVisitCallbackForImplicitComponent(UIComponent component, VisitCallback callback) {
        if (!limitRender) {
            if (component instanceof AjaxOutput) {
                AjaxOutput ajaxOutput = (AjaxOutput) component;
                if (ajaxOutput.isAjaxRendered()) {

                    // TODO - remove explicit nested IDs from update
                    return callback.visit(this, component);
                }
            }
        }
        return VisitResult.ACCEPT;
    }

    protected boolean shouldCompleteOnEmptyIds() {
        return limitRender;
    }

    @Override
    public VisitResult invokeVisitCallback(UIComponent component, VisitCallback callbackToWrap) {
        PartialViewRenderVisitCallback callback = new PartialViewRenderVisitCallback(callbackToWrap, getFacesContext());
        return super.invokeVisitCallback(component, callback);
    }

    /**
     * Determine if the VisitContext is, or wraps, the RenderExtendedVisitContext
     * @param visitContext The VisitContext of the component tree visit.
     */
    public static boolean isRenderExtendedVisitContext(VisitContext visitContext) {
        if  (visitContext instanceof RenderExtendedVisitContext) {
            return true;
        } else {
            VisitContext wrapped = visitContext;
            while (wrapped instanceof VisitContextWrapper) {
                wrapped = ((VisitContextWrapper) wrapped).getWrapped();
                if  (wrapped instanceof RenderExtendedVisitContext) {
                    return true;
                }
            }
        }
        return false;
    }
}
