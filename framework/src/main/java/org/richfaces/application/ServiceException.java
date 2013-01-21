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

import javax.faces.FacesException;

/**
 * <p class="changed_added_4_0">
 * Exception fired by service tracker if it cannot create requested service.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@SuppressWarnings("serial")
public class ServiceException extends FacesException {
    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    public ServiceException() {
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param message
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param cause
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param message
     * @param cause
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
