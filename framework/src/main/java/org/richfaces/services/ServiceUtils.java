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
package org.richfaces.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.richfaces.application.Initializable;

public final class ServiceUtils {

    /**
     * <p>
     * Sorts a given list containing services by their priority.
     * </p>
     *
     * <p>
     * By default, each service has priority 0. A developer can re-define the priority by implementing
     * {@link Prioritizable#getPriority()} method.
     * </p>
     *
     * @param services list of services
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void sortByPriority(List<?> services) {
        Collections.sort(services, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                int p1 = (o1 instanceof Prioritizable) ? ((Prioritizable) o1).getPriority() : 0;
                int p2 = (o2 instanceof Prioritizable) ? ((Prioritizable) o2).getPriority() : 0;
                return p2 - p1;
            }
        });
    }

    /**
     * <p>
     * Initializes a collection of services.
     * </p>
     *
     * <p>
     * Only services which implements {@link Initializable} will be initialized. A rest of services will stay untouched.
     * </p>
     *
     * @param services a collection of services
     */
    public static void initialize(Collection<?> services) {
        for (Object service : services) {
            if (service instanceof Initializable) {
                ((Initializable) service).init();
            }
        }
    }

    /**
     * <p>
     * Releases (finalizes) a collection of services.
     * </p>
     *
     * <p>
     * Only services which implements {@link Initializable} will be released. A rest of services will stay untouched.
     * </p>
     *
     * @param services a collection of services
     */
    public static void release(Collection<?> services) {
        for (Object service : services) {
            if (service instanceof Initializable) {
                ((Initializable) service).release();
            }
        }
    }
}
