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

package org.ajax4jsf.webapp;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContext;

/**
 * @author asmirnov
 */

//TODO: deprecate
public class PollEventsManager implements Serializable {
    public static final String EVENTS_MANAGER_KEY = PollEventsManager.class.getName();
    private static final long serialVersionUID = -6257285396790747665L;

    // TODO should use cache - push is application-scoped, not session-scoped
    private ConcurrentMap<String, PushEventsCounter> counters = new ConcurrentHashMap<String, PushEventsCounter>();

    public void init(ServletContext servletContext) {
        servletContext.setAttribute(EVENTS_MANAGER_KEY, this);
    }

    public PushEventsCounter getListener(String key) {
        PushEventsCounter counter = counters.get(key);

        if (counter != null) {
            return counter;
        } else {
            counter = new PushEventsCounter();

            PushEventsCounter oldCounter = counters.putIfAbsent(key, counter);

            if (oldCounter != null) {
                return oldCounter;
            } else {
                return counter;
            }
        }
    }
}
