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

package org.richfaces.application;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

/**
 *
 */
public class ServicesFactoryImpl implements ServicesFactory {

    private ClassToInstanceMap<Object> instances;

    /*
     * (non-Javadoc)
     * @see org.richfaces.services.ServicesFactory#getInstance(java.lang.Class)
     */
    @Override
    public <T> T getInstance(Class<T> type) throws ServiceException {
        return instances.getInstance(type);
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.services.ServicesFactory#setInstance(java.lang.Class, java.lang.Object)
     */
    @Override
    public <T> void setInstance(Class<T> type, T instance) {
        instances.putInstance(type, instance);
    }

    /**
     * Allows to configure and initialize a set of modules
     */
    public void init(Iterable<Module> modules) {
        instances = MutableClassToInstanceMap.create();
        for (Module module : modules) {
            module.configure(this);
        }
        for (Object service : instances.values()) {
            if (service instanceof Initializable) {
                Initializable initializableService = (Initializable) service;
                initializableService.init();
            }
        }
        instances = ImmutableClassToInstanceMap.copyOf(instances);
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.services.ServicesFactory#release()
     */
    @Override
    public void release() {
        for (Object service : instances.values()) {
            if (service instanceof Initializable) {
                Initializable initializableService = (Initializable) service;
                initializableService.release();
            }
        }
        instances = null;
    }
}
