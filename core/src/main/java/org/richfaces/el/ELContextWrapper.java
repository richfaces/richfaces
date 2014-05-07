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
package org.richfaces.el;

import java.util.Locale;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

/**
 * @author asmirnov
 */
public class ELContextWrapper extends ELContext {
    private final ELContext parent;
    private final ELResolver resolver;

    /**
     * @param parent
     */
    public ELContextWrapper(ELContext parent, ELResolver resolver) {
        super();
        this.resolver = resolver;
        this.parent = parent;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.el.ELContext#getELResolver()
     */
    @Override
    public ELResolver getELResolver() {
        return resolver;
    }

    /**
     * @return
     * @see javax.el.ELContext#getFunctionMapper()
     */
    public FunctionMapper getFunctionMapper() {
        return parent.getFunctionMapper();
    }

    /**
     * @return
     * @see javax.el.ELContext#getVariableMapper()
     */
    public VariableMapper getVariableMapper() {
        return parent.getVariableMapper();
    }

    /**
     * @param key
     * @return
     * @see javax.el.ELContext#getContext(java.lang.Class)
     */
    public Object getContext(Class key) {
        return parent.getContext(key);
    }

    /**
     * @param key
     * @param contextObject
     * @see javax.el.ELContext#putContext(java.lang.Class, java.lang.Object)
     */
    public void putContext(Class key, Object contextObject) {
        parent.putContext(key, contextObject);
    }

    public Locale getLocale() {
        return parent.getLocale();
    }

    public void setLocale(Locale locale) {
        parent.setLocale(locale);
    }
}
