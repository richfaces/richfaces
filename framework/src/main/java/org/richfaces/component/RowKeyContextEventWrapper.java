/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 */
public class RowKeyContextEventWrapper extends FacesEvent {
    private static final long serialVersionUID = -869970815228914529L;
    private static final Logger LOGGER = RichfacesLogger.COMPONENTS.getLogger();
    private FacesEvent event;
    private Object eventRowKey;
    private Object initialRowKey;

    public RowKeyContextEventWrapper(UIDataAdaptor component, FacesEvent event, Object eventRowKey) {
        super(component);

        this.event = event;
        this.eventRowKey = eventRowKey;
    }

    public FacesEvent getFacesEvent() {
        return this.event;
    }

    public PhaseId getPhaseId() {
        return this.event.getPhaseId();
    }

    public void setPhaseId(PhaseId phaseId) {
        this.event.setPhaseId(phaseId);
    }

    public boolean isAppropriateListener(FacesListener listener) {
        return false;
    }

    public void processListener(FacesListener listener) {
        throw new IllegalStateException();
    }

    public UIDataAdaptor getComponent() {
        return (UIDataAdaptor) super.getComponent();
    }

    public Object getEventRowKey() {
        return eventRowKey;
    }

    protected void setupEventContext(FacesContext facesContext) {
        getComponent().setRowKey(facesContext, getEventRowKey());
    }

    public void broadcast(FacesContext context) throws AbortProcessingException {
        // Set up the correct context and fire our wrapped event
        UIDataAdaptor dataAdaptor = getComponent();
        initialRowKey = dataAdaptor.getRowKey();

        UIComponent compositeParent = null;

        UIComponent targetComponent = event.getComponent();

        try {
            if (!UIComponent.isCompositeComponent(targetComponent)) {
                compositeParent = UIComponent.getCompositeComponentParent(targetComponent);
            }

            if (compositeParent != null) {
                compositeParent.pushComponentToEL(context, null);
            }

            setupEventContext(context);

            targetComponent.pushComponentToEL(context, null);
            targetComponent.broadcast(event);
        } finally {
            try {
                dataAdaptor.setRowKey(context, initialRowKey);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }

            initialRowKey = null;

            targetComponent.popComponentFromEL(context);

            if (compositeParent != null) {
                compositeParent.popComponentFromEL(context);
            }
        }
    }
}
