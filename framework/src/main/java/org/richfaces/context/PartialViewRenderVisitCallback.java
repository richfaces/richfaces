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

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;

import org.richfaces.component.MetaComponentEncoder;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 *
 */
final class PartialViewRenderVisitCallback implements VisitCallback {

    private static final Logger LOG = RichfacesLogger.CONTEXT.getLogger();

    private FacesContext ctx;



    PartialViewRenderVisitCallback(FacesContext ctx) {
        this.ctx = ctx;
    }

    private void logException(Exception e) {
        if (LOG.isErrorEnabled()) {
            LOG.error(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.visit.VisitCallback#visit(javax.faces.component.visit.VisitContext,
     * javax.faces.component.UIComponent)
     */
    public VisitResult visit(VisitContext context, UIComponent target) {
        String metaComponentId = (String) ctx.getAttributes().get(ExtendedVisitContext.META_COMPONENT_ID);
        if (metaComponentId != null) {
            MetaComponentEncoder encoder = (MetaComponentEncoder) target;
            try {
                encoder.encodeMetaComponent(ctx, metaComponentId);
            } catch (Exception e) {
                logException(e);
            }
        } else {
            PartialResponseWriter writer = ctx.getPartialViewContext().getPartialResponseWriter();

            try {
                writer.startUpdate(target.getClientId(ctx));
                try {
                    // do the default behavior...
                    target.encodeAll(ctx);
                } catch (Exception ce) {
                    logException(ce);
                }

                writer.endUpdate();
            } catch (IOException e) {
                logException(e);
            }
        }

        // Once we visit a component, there is no need to visit
        // its children, since processDecodes/Validators/Updates and
        // encodeAll() already traverse the subtree. We return
        // VisitResult.REJECT to supress the subtree visit.
        return VisitResult.REJECT;
    }
}