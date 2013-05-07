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
