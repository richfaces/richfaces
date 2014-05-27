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
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

/**
 * Wraps parent {@link VisitContext} and executes {@link MetaComponentProcessingVisitCallback} during its
 * {@link #invokeVisitCallback(UIComponent, VisitCallback)} method.
 *
 * @author Nick Belaevski
 */
public class ExtendedExecuteVisitContext extends BaseExtendedVisitContext {

    public ExtendedExecuteVisitContext(VisitContext visitContextToWrap, FacesContext facesContext,
            Collection<String> clientIds, Set<VisitHint> hints) {
        super(visitContextToWrap, facesContext, clientIds, hints, ExtendedVisitContextMode.EXECUTE);
    }

    /**
     * Instead of execution of {@link VisitCallback} directly, we use {@link MetaComponentProcessingVisitCallback} that executes
     * additional logic for meta-component processing.
     */
    @Override
    public VisitResult invokeVisitCallback(UIComponent component, VisitCallback callbackToWrap) {
        MetaComponentProcessingVisitCallback callback = new MetaComponentProcessingVisitCallback(callbackToWrap,
                getFacesContext());
        return super.invokeVisitCallback(component, callback);
    }
}
