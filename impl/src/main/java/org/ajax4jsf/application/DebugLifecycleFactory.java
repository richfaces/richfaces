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

package org.ajax4jsf.application;

import org.richfaces.log.RichfacesLogger;
import org.slf4j.Logger;

import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import java.util.Iterator;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:12 $
 */
public class DebugLifecycleFactory extends LifecycleFactory {
    private static final Logger LOG = RichfacesLogger.APPLICATION.getLogger();
    private DebugLifecycle debugLifecycle;
    private LifecycleFactory defaultFactory;

    /**
     * @param defaultFactory
     */
    public DebugLifecycleFactory(LifecycleFactory defaultFactory) {
        super();
        this.defaultFactory = defaultFactory;

        if (LOG.isDebugEnabled()) {
            LOG.debug("Created Lifecycle instance");
        }
    }

//  private Map _lifecycles = new HashMap();

    /**
     * @return the debugLifecycle
     */
    private DebugLifecycle getDebugLifecycle() {
        if (debugLifecycle == null) {
            debugLifecycle = new DebugLifecycle(defaultFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE));
        }

        return debugLifecycle;
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.lifecycle.LifecycleFactory#addLifecycle(java.lang.String, javax.faces.lifecycle.Lifecycle)
     */
    @Override
    public void addLifecycle(String lifecycleId, Lifecycle lifecycle) {
        this.defaultFactory.addLifecycle(lifecycleId, lifecycle);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Added lifecycle with ID " + lifecycleId);
        }
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.lifecycle.LifecycleFactory#getLifecycle(java.lang.String)
     */
    @Override
    public Lifecycle getLifecycle(String lifecycleId) {
        Lifecycle life;

        if (DebugLifecycle.DEBUG_LYFECYCLE_ID.equals(lifecycleId)) {
            life = getDebugLifecycle();
        } else {
            life = this.defaultFactory.getLifecycle(lifecycleId);
        }

        return life;
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.lifecycle.LifecycleFactory#getLifecycleIds()
     */
    public Iterator getLifecycleIds() {
        return this.defaultFactory.getLifecycleIds();
    }
}
