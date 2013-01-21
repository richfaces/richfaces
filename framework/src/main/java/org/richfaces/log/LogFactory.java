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
package org.richfaces.log;

/**
 * <p class="changed_added_4_0">
 * This class produces loggers used by whole RichFaces library.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public final class LogFactory {
    private static final Logger DEFAULT_LOGGER = new JavaLogger();

    private LogFactory() {
        // This class is not instantiable.
    }

    /**
     * <p class="changed_added_4_0">
     * This method creates default logger.
     * </p>
     *
     * @return
     */
    public static Logger getLogger() {
        return DEFAULT_LOGGER;
    }

    /**
     * <p class="changed_added_4_0">
     * This method produces logger instance for given category.
     * </p>
     *
     * @param category
     * @return
     */
    public static Logger getLogger(String category) {
        return new JavaLogger(category);
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }
}
