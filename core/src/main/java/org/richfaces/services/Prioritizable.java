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

/**
 * Marks service which defines its priority between the services of same type
 *
 * @author Lukas Fryc
 */
public interface Prioritizable {

    /**
     * <p>Zero-relative priority.</p>
     *
     * <p>The higher priority means that the service will be loaded sooner,<br>
     * the lower priority means that the service will be loaded later.</p>
     *
     * <p>By default, any service (even though it isn't Prioritizable has priority equal to 0.</p>
     *
     * @return priority of the service as compared to other service of same type
     */
    int getPriority();
}
