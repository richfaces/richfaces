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
package org.richfaces.webapp;

import java.text.MessageFormat;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * <p>
 * Initializes {@link ResourceServlet}.
 * </p>
 *
 * <p>
 * Initialization can be turned of by "org.richfaces.resources.skipResourceServletRegistration" context parameter.
 * </p>
 */
public class ResourceServletContainerInitializer extends AbstractServletContainerInitializer {

    private static final Logger LOGGER = RichfacesLogger.WEBAPP.getLogger();

    private static final String SKIP_SERVLET_REGISTRATION_PARAM = "org.richfaces.resources.skipResourceServletRegistration";
    public static final String RICHFACES_RESOURCES_DEFAULT_MAPPING = "/org.richfaces.resources/*";

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContainerInitializer#onStartup(java.util.Set, javax.servlet.ServletContext)
     */
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {
        if (Boolean.valueOf(servletContext.getInitParameter(SKIP_SERVLET_REGISTRATION_PARAM))) {
            return;
        }

        try {
            ServletRegistration servletRegistration = getServletRegistration(ResourceServlet.class, servletContext);
            if (servletRegistration == null) {
                registerServlet(servletContext);
            }
        } catch (Exception e) {
            servletContext
                    .log(MessageFormat.format("Exception registering RichFaces Resource Servlet: {0}", e.getMessage()), e);
        }
    }

    private static void registerServlet(ServletContext context) {
        Dynamic dynamicRegistration = context.addServlet("AutoRegisteredResourceServlet", ResourceServlet.class);
        dynamicRegistration.addMapping(RICHFACES_RESOURCES_DEFAULT_MAPPING);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Auto-registered servlet " + ResourceServlet.class.getSimpleName() + " with mapping '" + RICHFACES_RESOURCES_DEFAULT_MAPPING + "'");
        }
    }
}
