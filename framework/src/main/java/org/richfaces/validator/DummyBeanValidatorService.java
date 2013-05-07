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

/**
 *
 */
package org.richfaces.validator;

import java.util.Collection;
import java.util.Collections;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * @author asmirnov
 *
 */
public class DummyBeanValidatorService implements BeanValidatorService {
    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.validator.BeanValidatorService#getConstrains(javax.faces.context.FacesContext,
     * javax.el.ValueExpression, java.lang.Class<?>[])
     */
    public Collection<ValidatorDescriptor> getConstrains(FacesContext context, ValueExpression expression, String message,
        Class<?>... groups) {
        return Collections.emptySet();
    }

    public Collection<String> validateExpression(FacesContext context, ValueExpression expression, Object newValue,
        Class<?>... groups) {
        return Collections.emptySet();
    }

    public Collection<String> validateObject(FacesContext context, Object object, Class<?>... groups) {
        return Collections.emptySet();
    }
}
