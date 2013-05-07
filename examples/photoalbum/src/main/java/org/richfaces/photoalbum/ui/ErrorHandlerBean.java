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

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Named;

import org.richfaces.photoalbum.event.EventType;
import org.richfaces.photoalbum.event.Events;
import org.richfaces.photoalbum.event.SimpleEvent;

/**
 * Convenience UI class for global eeror-checking mechanism
 *
 * @author Andrey Markhel
 */
@Named
@RequestScoped
public class ErrorHandlerBean {
    private List<String> errors = new ArrayList<String>();

    public List<String> getErrors() {
        return errors;
    }

    public boolean isErrorExist() {
        return errors.size() > 0;
    }

    /**
     * Convenience method that observes <code>Constants.ADD_ERROR_EVENT</code>. After error occured add error to the list of
     * erors andon rerendering modal panel with all errors will be showed.
     *
     * @param e - string representation of error.
     */
    public void addToErrors(@Observes @EventType(Events.ADD_ERROR_EVENT) SimpleEvent se) {
        errors.add(se.getMessage());
    }
}
