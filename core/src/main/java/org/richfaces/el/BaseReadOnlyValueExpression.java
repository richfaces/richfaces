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

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 *
 */
public abstract class BaseReadOnlyValueExpression extends ValueExpression {
    /**
     *
     */
    private static final long serialVersionUID = -1094028009026806965L;
    private Class<?> type;

    public BaseReadOnlyValueExpression(Class<?> type) {
        super();
        this.type = type;
    }

    @Override
    public Class<?> getExpectedType() {
        return type;
    }

    @Override
    public Class<?> getType(ELContext context) {
        return type;
    }

    @Override
    public abstract Object getValue(ELContext context);

    @Override
    public boolean isReadOnly(ELContext context) {
        return true;
    }

    @Override
    public void setValue(ELContext context, Object value) {
        throw new UnsupportedOperationException("setValue(ELContext, Object)");
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public String getExpressionString() {
        return null;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public boolean isLiteralText() {
        return false;
    }

    protected FacesContext getFacesContext(ELContext elContext) {
        return (FacesContext) elContext.getContext(FacesContext.class);
    }
}
