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

import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * <p>
 * Provides common functionality for {@link ServletContainerInitializer} implementatios.
 * </p>
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public abstract class AbstractServletContainerInitializer implements ServletContainerInitializer {

    /**
     * Detects if given {@link Filter} class has been already registered.
     *
     * @param filterClass {@link Filter} implementation class
     * @param context to search for registration
     * @return true if given {@link Filter} class has been already registered.
     */
    protected boolean hasFilterMapping(Class<? extends Filter> filterClass, ServletContext context) {
        Collection<? extends FilterRegistration> filterRegistrations = context.getFilterRegistrations().values();
        for (FilterRegistration filterRegistration : filterRegistrations) {
            if (filterClass.getName().equals(filterRegistration.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Returns the servlet registration for given {@link Servlet} class, which has at least one mapping registered.
     * </p>
     *
     * <p>
     * Returns null otherwise.
     * </p>
     *
     * @param servletClass {@link Servlet} implementation class
     * @param context to search for registration
     * @return the servlet registration for given {@link Servlet} class, which has at least one mapping registered, null
     *         otherwise.
     */
    protected ServletRegistration getServletRegistration(Class<? extends Servlet> servletClass, ServletContext context) {
        Collection<? extends ServletRegistration> servletRegistrations = context.getServletRegistrations().values();
        for (ServletRegistration servletRegistration : servletRegistrations) {
            if (servletClass.getName().equals(servletRegistration.getClassName())) {
                if (servletRegistration.getMappings() != null && !servletRegistration.getMappings().isEmpty()) {
                    return servletRegistration;
                }
            }
        }
        return null;
    }
}
