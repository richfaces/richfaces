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
import javax.faces.event.PhaseId;

import org.richfaces.component.MetaComponentProcessor;

/**
 * @author Nick Belaevski
 *
 */
final class PartialViewExecuteVisitCallback implements VisitCallback {
    private FacesContext facesContext;
    private PhaseId phaseId;

    PartialViewExecuteVisitCallback(FacesContext context, PhaseId phaseId) {
        super();
        this.facesContext = context;
        this.phaseId = phaseId;
    }

    public VisitResult visit(VisitContext context, UIComponent target) {
        String metaComponentId = (String) facesContext.getAttributes().get(ExtendedVisitContext.META_COMPONENT_ID);
        if (metaComponentId != null) {
            MetaComponentProcessor executor = (MetaComponentProcessor) target;
            executor.processMetaComponent(facesContext, metaComponentId);
        } else if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
            target.processDecodes(facesContext);
        } else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
            target.processValidators(facesContext);
        } else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
            target.processUpdates(facesContext);
        } else {
            throw new IllegalArgumentException(phaseId.toString());
        }

        return VisitResult.REJECT;
    }
}
