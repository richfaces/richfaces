/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.ajax4jsf.tests;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ValueBinding;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com
 * created 13.04.2007
 *
 */
public class MockValueBinding extends ValueBinding {
    private Class clazz;
    private Object value;

    public MockValueBinding(Object value, Class clazz) {
        super();
        this.clazz = clazz;
        this.value = value;
    }

    public Class getType(FacesContext context) throws EvaluationException, PropertyNotFoundException {
        return clazz;
    }

    public Object getValue(FacesContext context) throws EvaluationException, PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException();
        }

        return value;
    }

    public boolean isReadOnly(FacesContext context) throws EvaluationException, PropertyNotFoundException {
        return false;
    }

    public void setValue(FacesContext context, Object value) throws EvaluationException, PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException();
        }

        this.value = value;
    }
}
