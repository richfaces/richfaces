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

import java.io.Serializable;

import java.util.EventObject;
import java.util.concurrent.atomic.AtomicBoolean;

import org.ajax4jsf.event.PushEventListener;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class PushEventTracker implements PushEventListener, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4362297582678089326L;
    private final AtomicBoolean eventPerformed = new AtomicBoolean(false);

    public void onEvent(EventObject event) {
        eventPerformed.set(true);
    }

    public boolean pollStatus() {
        return eventPerformed.compareAndSet(true, false);
    }
}
