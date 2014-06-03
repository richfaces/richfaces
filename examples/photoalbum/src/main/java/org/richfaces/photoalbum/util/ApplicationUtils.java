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
package org.richfaces.photoalbum.util;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.SimpleEvent;

/**
 * Utility class for actions, related to direct access or modification of current request
 *
 * @author Andrey Markhel
 */
public class ApplicationUtils {

    @SuppressWarnings("unused")
    @Produces
    @PersistenceContext
    private EntityManager em;

    private ApplicationUtils() {

    }

    @Inject
    @EventType(Events.CHECK_USER_EXPIRED_EVENT)
    Event<SimpleEvent> event;

    /**
     * Utility method for adding FacesMessages to specified component
     *
     * @param componentId - component identifier
     * @param message - message to add
     */
    public static void addFacesMessage(String componentId, String summary, String detail) {
        UIComponent root = FacesContext.getCurrentInstance().getViewRoot();
        UIComponent component = root.findComponent(componentId);
        FacesContext.getCurrentInstance().addMessage(component.getClientId(FacesContext.getCurrentInstance()),
            new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }

    /**
     * Utility method for get reference to current HTTPSession
     *
     * @return session object
     */
    @Produces
    @Preferred
    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

    /**
     * Utility method for programmatically adding specified component to rerender after AJAX request complete.
     *
     * @param componentId - id of component should be added to rerender
     */
    // may not work
    @SuppressWarnings("unused")
    public static void addToRerender(String componentId) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExtendedPartialViewContext epvc = ExtendedPartialViewContext.getInstance(fc);
            UIComponent destComponent = fc.getViewRoot().findComponent(componentId);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
    }

    public void fireCheckUserExpiredEvent() {
        event.fire(new SimpleEvent());
    }
}
