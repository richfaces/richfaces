/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.demo.arrangeablemodel;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;

class PersistenceLifecycle extends Lifecycle {
    private static final class PersistenceServiceRef {
        static final PersistenceService PERSISTENCE_SERVICE = (PersistenceService) FacesContext.getCurrentInstance()
                .getExternalContext().getApplicationMap().get("persistenceService");

        private PersistenceServiceRef() {
        }
    }

    private Lifecycle lifecycle;

    public PersistenceLifecycle(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public void addPhaseListener(PhaseListener listener) {
        lifecycle.addPhaseListener(listener);
    }

    public PhaseListener[] getPhaseListeners() {
        return lifecycle.getPhaseListeners();
    }

    public void removePhaseListener(PhaseListener listener) {
        lifecycle.removePhaseListener(listener);
    }

    public void execute(FacesContext context) throws FacesException {
        try {
            lifecycle.execute(context);
        } finally {
            PersistenceServiceRef.PERSISTENCE_SERVICE.closeEntityManager();
        }
    }

    public void render(FacesContext context) throws FacesException {
        try {
            lifecycle.render(context);
        } finally {
            PersistenceServiceRef.PERSISTENCE_SERVICE.closeEntityManager();
        }
    }
}