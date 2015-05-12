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

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereHandler;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.handler.ReflectorServletProcessor;
import org.richfaces.application.push.impl.PushContextFactoryImpl;

import com.google.common.collect.Maps;

/**
 * Push servlet wraps {@link AtmosphereServlet} and during initialization it adds pre-initialized {@link AtmosphereHandler} for
 * specified mapping.
 *
 * @author Nick Belaevski
 */
public final class PushServlet extends AtmosphereServlet {
    private static final long serialVersionUID = 2483746935231439236L;

    private static final Map<String, String> DEFAULT_INIT_PARAMETERS = Maps.newHashMap();

    static {
        // default Atmosphere configuration
        DEFAULT_INIT_PARAMETERS.put(ApplicationConfig.DISABLE_ONSTATE_EVENT, "true");
    }

    /**
     * Prevents multi-initialization since ReflectorServletProcessor tries to initialize provided Servlet
     */
    private boolean initialized = false;
    private boolean destroyed = false;

    /*
     * (non-Javadoc)
     *
     * @see org.atmosphere.cpr.AtmosphereServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(final ServletConfig sc) throws ServletException {
        if (!initialized) {
            super.init(new ServletConfigDefaultsFacade(sc, DEFAULT_INIT_PARAMETERS));
            this.initialized = true;

            String mapping = (String) sc.getServletContext()
                    .getAttribute(PushContextFactoryImpl.PUSH_HANDLER_MAPPING_ATTRIBUTE);

            if (mapping == null) {
                mapping = "*";
            }

            ReflectorServletProcessor r = new ReflectorServletProcessor(this);
            r.setFilterClassName(PushHandlerFilter.class.getName());

            framework().addAtmosphereHandler(mapping, r).initAtmosphereHandler(sc);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.atmosphere.cpr.AtmosphereServlet#destroy()
     */
    @Override
    public void destroy() {
        if (!destroyed) {
            this.destroyed = true;
            super.destroy();
        }
    }
}
