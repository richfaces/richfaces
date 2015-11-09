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

import org.richfaces.application.push.impl.PushContextFactoryImpl;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.collect.Iterables;

/**
 * Checks whether the {@link PushServlet} should be registered automatically on application startup.
 *
 * @author Nick Belaevski
 */
public class PushServletContainerInitializer extends AbstractServletContainerInitializer {

    private static final Logger LOGGER = RichfacesLogger.WEBAPP.getLogger();
    private static final String ATMOSPHERE_SERVLET_CLASS = "org.atmosphere.cpr.AtmosphereServlet";
    private static final String SKIP_SERVLET_REGISTRATION_PARAM = "org.richfaces.push.skipPushServletRegistration";
    private static final String PUSH_CONTEXT_DEFAULT_MAPPING = '/' + PushContextFactoryImpl.PUSH_CONTEXT_RESOURCE_NAME;

    /**
     * Uses dynamic servlet registartaion for registering PushServlet automatically
     */
    private static void registerPushServlet(ServletContext context) {
        Dynamic dynamicRegistration = context.addServlet("AutoRegisteredPushServlet", PushServlet.class);
        dynamicRegistration.addMapping(PUSH_CONTEXT_DEFAULT_MAPPING);
        dynamicRegistration.setAsyncSupported(true);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Auto-registered servlet " + PushServlet.class.getSimpleName() + " with mapping '" + PUSH_CONTEXT_DEFAULT_MAPPING + "'");
        }
    }

    /**
     * If Atmosphere is on classpath and {@link PushFilter} wasn't registered in current {@link ServletContext},
     *
     * the {@link PushServlet} will be registered automatically.
     */
    @Override
    public void onStartup(Set<Class<?>> clasess, ServletContext servletContext) throws ServletException {
        if (Boolean.valueOf(servletContext.getInitParameter(SKIP_SERVLET_REGISTRATION_PARAM))) {
            return;
        }

        if (!isAtmospherePresent()) {
            return;
        }

        if (hasFilterMapping(PushFilter.class, servletContext)) {
            return;
        }

        try {
            String pushHandlerMapping;

            ServletRegistration servletRegistration = getServletRegistration(PushServlet.class, servletContext);
            if (servletRegistration == null) {
                registerPushServlet(servletContext);
                pushHandlerMapping = PUSH_CONTEXT_DEFAULT_MAPPING;
            } else {
                pushHandlerMapping = Iterables.get(servletRegistration.getMappings(), 0);
            }

            servletContext.setAttribute(PushContextFactoryImpl.PUSH_HANDLER_MAPPING_ATTRIBUTE, pushHandlerMapping);
        } catch (Exception e) {
            servletContext.log(MessageFormat.format("Caught exception when registering RichFaces Push Servlet: {0}", e.getMessage()), e);
        }
    }

    private boolean isAtmospherePresent() {
        try {
            Class.forName(ATMOSPHERE_SERVLET_CLASS, false, Thread.currentThread().getContextClassLoader());

            return true;
        } catch (ClassNotFoundException e) {
            // no atmosphere present - no push then
            LOGGER.debug("AtmosphereServlet class is not present on classpath, PushServlet won't be registered automatically");
        } catch (LinkageError e) {
            // atmosphere is missing some dependency - no push too
            LOGGER.error(e.getMessage(), e);
        }

        return false;
    }
}
