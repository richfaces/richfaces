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
package org.richfaces.photoalbum.ui;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Named;

import org.richfaces.photoalbum.model.event.ErrorEvent;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.util.ApplicationUtils;

/**
 * Convenience UI class for global eeror-checking mechanism
 * 
 * @author Andrey Markhel
 */
@Named
@RequestScoped
public class ErrorHandlerBean {

    /**
     * Creates a <code><rich:notifyMessage></code> on the site.
     * 
     * @param summary - summary of the error
     * @param detail - detailed description of the error
     */
    public void showError(String summary, String detail) {
        ApplicationUtils.addFacesMessage("overForm", summary, detail);
    }

    /**
     * Convenience method that observes Event of type <code>Events.ADD_ERROR_EVENT</code>.
     * 
     * @param ee - event fired upon error.
     */
    public void addToErrors(@Observes @EventType(Events.ADD_ERROR_EVENT) ErrorEvent ee) {
        showError(ee.getSummary(), ee.getDetail());
    }

    /**
     * Pseudo-setter that shows error that originated in JS code, using <code><a4j:jsFunction></code> as a middle-man.
     * 
     * @param message - summary and detail separated by '$#$'
     */
    public void setJSError(String message) {
        String[] s = message.split("\\$#\\$");
        showError(s[0], s[1]);
    }
}
