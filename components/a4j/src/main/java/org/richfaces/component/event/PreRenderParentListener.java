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
package org.richfaces.component.event;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.PreRenderComponentEvent;

/**
 * <p>
 * Listener takes component to be processed before parent component is rendered.
 * </p>
 *
 * <p>
 * The parent does not need to be parent component, but given component will be searched for in context of parent component by
 * its id using {@link UIComponent#findComponent(String)}.
 * </p>
 *
 * <p>
 * The listener for {@link PreRenderComponentEvent} will be automatically bound to parent component.
 * </p>
 */
public abstract class PreRenderParentListener implements ComponentSystemEventListener, Serializable {

    private static final long serialVersionUID = 2106663563922715641L;

    private String componentId;

    public PreRenderParentListener(UIComponent parent, UIComponent component) {
        this.componentId = component.getId();
        parent.subscribeToEvent(PreRenderComponentEvent.class, this);
    }

    /**
     * Will be processed before the component which this event is registered to is rendered
     */
    protected abstract void preRenderParent(FacesContext facesContext, UIComponent component);

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        if (event instanceof PreRenderComponentEvent) {
            final FacesContext facesContext = FacesContext.getCurrentInstance();
            final UIComponent parent = event.getComponent();
            final UIComponent component = parent.findComponent(componentId);

            preRenderParent(facesContext, component);
        }
    }
}