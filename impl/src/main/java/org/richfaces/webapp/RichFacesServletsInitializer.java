/*
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
package org.richfaces.webapp;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * <p>
 * Runs all servlet container initializers listed in {@link #INITIALIZERS}.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class RichFacesServletsInitializer implements ServletContainerInitializer {

    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();

    private Class<?>[] INITIALIZERS = new Class<?>[] { PushServletContainerInitializer.class,
            ResourceServletContainerInitializer.class };

    /**
     * Takes classes listed in {@link #INITIALIZERS} and tries to initialize them as {@link ServletContainerInitializer}s.
     */
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {

        for (Class<?> clazz : INITIALIZERS) {
            ServletContainerInitializer initializer = null;

            try {
                initializer = (ServletContainerInitializer) clazz.newInstance();
            } catch (Exception e) {
                LOGGER.error("Failed to instantiate servlet initializer " + clazz.getName(), e);
            }

            if (initializer != null) {
                try {
                    initializer.onStartup(null, servletContext);
                } catch (Exception e) {
                    LOGGER.error("Failed to initialize servlet by " + clazz.getName(), e);
                }
            }
        }

    }
}
