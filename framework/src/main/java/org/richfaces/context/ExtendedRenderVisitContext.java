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

import org.ajax4jsf.component.AjaxOutput;


/**
 * <p>Wraps parent {@link VisitContext} and executes following operations:</p>
 *
 * <ul>
 * <li>makes sure implicit rendering areas ({@link AjaxOutput}s) are also visited
 * <li>executes {@link MetaComponentEncodingVisitCallback} during {@link #invokeVisitCallback(UIComponent, VisitCallback)} method.</li>
 * </ul>
 *
 * @author Nick Belaevski
 */
public class ExtendedRenderVisitContext extends BaseExtendedVisitContext {
    private boolean limitRender;

    public ExtendedRenderVisitContext(VisitContext visitContextToWrap, FacesContext facesContext, Collection<String> clientIds, Set<VisitHint> hints,
        boolean limitRender) {
        super(visitContextToWrap, facesContext, clientIds, hints, ExtendedVisitContextMode.RENDER);

        this.limitRender = limitRender;
    }

    /**
     * Instead of execution of {@link VisitCallback} directly, we use {@link MetaComponentEncodingVisitCallback} that executes
     * additional logic for meta-component encoding.
     */
    @Override
    public VisitResult invokeVisitCallback(UIComponent component, VisitCallback callbackToWrap) {
        MetaComponentEncodingVisitCallback callback = new MetaComponentEncodingVisitCallback(callbackToWrap, getFacesContext());
        return super.invokeVisitCallback(component, callback);
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.context.BaseExtendedVisitContext#hasImplicitSubtreeIdsToVisit(javax.faces.component.UIComponent)
     */
    @Override
    protected boolean hasImplicitSubtreeIdsToVisit(UIComponent component) {
        return !limitRender && AjaxOutputTracker.hasNestedAjaxOutputs(component);
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.context.BaseExtendedVisitContext#addDirectSubtreeIdsToVisitForImplicitComponents(javax.faces.component.UIComponent, java.util.Set)
     */
    protected void addDirectSubtreeIdsToVisitForImplicitComponents(UIComponent component, Set<String> result) {
        if (!limitRender) {
            Collection<String> directChildrenIds = AjaxOutputTracker.getDirectChildrenIds(component);
            if (directChildrenIds != null && !directChildrenIds.isEmpty()) {
                result.addAll(directChildrenIds);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.context.BaseExtendedVisitContext#invokeVisitCallbackForImplicitComponent(javax.faces.component.UIComponent, javax.faces.component.visit.VisitCallback)
     */
    @Override
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

    /*
     * (non-Javadoc)
     * @see org.richfaces.context.BaseExtendedVisitContext#shouldCompleteOnEmptyIds()
     */
    @Override
    protected boolean shouldCompleteOnEmptyIds() {
        return limitRender;
    }

    /**
     * Determine if the VisitContext is, or wraps, the RenderExtendedVisitContext
     * @param visitContext The VisitContext of the component tree visit.
     */
    public static boolean isExtendedRenderVisitContext(VisitContext visitContext) {
        if  (visitContext instanceof ExtendedRenderVisitContext) {
            return true;
        } else {
            VisitContext wrapped = visitContext;
            while (wrapped instanceof VisitContextWrapper) {
                wrapped = ((VisitContextWrapper) wrapped).getWrapped();
                if  (wrapped instanceof ExtendedRenderVisitContext) {
                    return true;
                }
            }
        }
        return false;
    }
}
