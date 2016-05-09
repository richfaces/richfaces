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

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

/**
 * Forms a facade for {@link ServletContext} that can hold map of default configuration used in case no other configuration provided.
 */
public final class ServletConfigDefaultsFacade implements ServletConfig {

    private final ServletConfig config;
    private final Map<String, String> defaults;

    public ServletConfigDefaultsFacade(ServletConfig config, Map<String, String> defaults) {
        super();
        this.config = config;
        this.defaults = defaults;
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
            parameter = defaults.get(name);
        }

        return parameter;
    }

    public Enumeration<String> getInitParameterNames() {
        Set<String> result = Sets.newHashSet();

        Iterators.addAll(result, defaults.keySet().iterator());
        Iterators.addAll(result, Iterators.forEnumeration(config.getInitParameterNames()));
        Iterators.addAll(result, Iterators.forEnumeration(config.getServletContext().getInitParameterNames()));

        return Iterators.asEnumeration(result.iterator());
    }
}