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

import java.util.Iterator;

import javax.faces.FacesWrapper;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

/**
 * @author Nick Belaevski
 */
public class PersistenceLifecycleFactory extends LifecycleFactory implements FacesWrapper<LifecycleFactory> {
    private LifecycleFactory lifecycleFactory;
    private Lifecycle defaultLifecycle;

    public PersistenceLifecycleFactory(LifecycleFactory lifecycleFactory) {
        super();
        this.lifecycleFactory = lifecycleFactory;
    }

    @Override
    public void addLifecycle(String lifecycleId, Lifecycle lifecycle) {
        getWrapped().addLifecycle(lifecycleId, lifecycle);
    }

    @Override
    public Lifecycle getLifecycle(String lifecycleId) {
        if (LifecycleFactory.DEFAULT_LIFECYCLE.equals(lifecycleId)) {
            if (defaultLifecycle == null) {
                createDefaultLifecycle();
            }

            return defaultLifecycle;
        }

        return lifecycleFactory.getLifecycle(lifecycleId);
    }

    private void createDefaultLifecycle() {
        defaultLifecycle = new PersistenceLifecycle(lifecycleFactory.getLifecycle(DEFAULT_LIFECYCLE));
    }

    @Override
    public Iterator<String> getLifecycleIds() {
        return lifecycleFactory.getLifecycleIds();
    }
}
