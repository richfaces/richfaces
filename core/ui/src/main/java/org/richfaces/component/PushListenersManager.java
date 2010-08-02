/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.richfaces.component;

import javax.faces.context.FacesContext;

import org.ajax4jsf.util.LRUMap;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class PushListenersManager {
    private static final String CONTEXT_ATTRIBUTE_NAME = "richFacesPushListenersManager";
    private LRUMap<String, PushEventTracker> listeners;

    public PushListenersManager() {

        // TODO configure map size
        this.listeners = new LRUMap<String, PushEventTracker>();
    }

    public static PushListenersManager getInstance(FacesContext context) {
        return (PushListenersManager) context.getExternalContext().getApplicationMap().get(CONTEXT_ATTRIBUTE_NAME);
    }

    public PushEventTracker getListener(String name) {
        synchronized (listeners) {

            // LRUMap involves write for each operation, so RWLock is not acceptable here
            PushEventTracker listener = listeners.get(name);

            if (listener == null) {
                listener = new PushEventTracker();
                listeners.put(name, listener);
            }

            return listener;
        }
    }
}
