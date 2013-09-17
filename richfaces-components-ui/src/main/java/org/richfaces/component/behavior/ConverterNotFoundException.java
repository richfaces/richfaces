/*
 * $Id$
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.component.behavior;

/**
 * <p class="changed_added_4_0">
 * This exception thrown if Client validator cannot determine Converter associated with target UIComponent
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@SuppressWarnings("serial")
public class ConverterNotFoundException extends Exception {
    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    public ConverterNotFoundException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param message
     */
    public ConverterNotFoundException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param cause
     */
    public ConverterNotFoundException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param message
     * @param cause
     */
    public ConverterNotFoundException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
}
