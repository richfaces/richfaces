/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.MeteorServlet;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author Nick Belaevski
 *
 */
public final class PushServlet extends MeteorServlet {
    private static final long serialVersionUID = 2483746935231439236L;

    private static final class ServletConfigDefaultsWrapper implements ServletConfig {
        private static final Map<String, String> DEFAULT_INIT_PARAMETERS = Maps.newHashMap();

        static {
            DEFAULT_INIT_PARAMETERS.put("org.atmosphere.filter", PushHandlerFilter.class.getName());
            DEFAULT_INIT_PARAMETERS.put(ApplicationConfig.DISABLE_ONSTATE_EVENT, "true");
        }

        private final ServletConfig config;

        public ServletConfigDefaultsWrapper(ServletConfig config) {
            super();
            this.config = config;
        }

        public String getServletName() {
            return config.getServletName();
        }

        public ServletContext getServletContext() {
            return config.getServletContext();
        }

        public String getInitParameter(String name) {
            String parameter = config.getInitParameter(name);

            if (parameter == null) {
                parameter = config.getServletContext().getInitParameter(name);
            }

            if (parameter == null) {
                parameter = DEFAULT_INIT_PARAMETERS.get(name);
            }

            return parameter;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public Enumeration getInitParameterNames() {
            Set<String> result = Sets.newHashSet();

            Iterators.addAll(result, (Iterator<? extends String>) DEFAULT_INIT_PARAMETERS.keySet());
            Iterators.addAll(result, Iterators.forEnumeration(config.getInitParameterNames()));
            Iterators.addAll(result, Iterators.forEnumeration(config.getServletContext().getInitParameterNames()));

            return Iterators.asEnumeration(result.iterator());
        }
    }

    @Override
    public void init(ServletConfig sc) throws ServletException {
        super.init(new ServletConfigDefaultsWrapper(sc));
    }
}
