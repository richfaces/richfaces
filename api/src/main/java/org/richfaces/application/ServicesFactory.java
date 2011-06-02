/*
 * $Id$
 *
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
package org.richfaces.application;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface ServicesFactory {
    /**
     * <p class="changed_added_4_0">
     * Get service instance associated with given type, usually service interface or base abstract class.
     * </p>
     *
     * @param <T> service type.
     * @param type Base class implemented by service.
     * @return Current service implementation.
     * @throws ServiceException if factory cannot create requested service.
     */
    <T> T getInstance(Class<T> type) throws ServiceException;

    /**
     * <p class="changed_added_4_0">
     * Associate concrete instance with service.
     * </p>
     *
     * @param <T> service type.
     * @param type Base class implemented by service.
     * @param instance service instance.
     */
    <T> void setInstance(Class<T> type, T instance);

    /**
     * <p class="changed_added_4_0">
     * Release all services.
     * </p>
     */
    void release();
}
