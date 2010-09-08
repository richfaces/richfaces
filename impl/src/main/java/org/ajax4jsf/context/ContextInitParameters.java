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

package org.ajax4jsf.context;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * This class hold all methods for get application init parameters. Created for
 * single access point to all parameters - simplest for a documentation.
 *
 * @author asmirnov
 */
public final class ContextInitParameters {
    
    private static final String INIT_PARAM_PREFIX = ContextInitParameters.class.getSimpleName() + ":";
    private static final Object NULL = new Object() {

        public String toString() {
            return ContextInitParameters.class.getSimpleName() + ": null Object";
        };
        
    };

    /**
     *
     */
    private ContextInitParameters() {

        // this is a only static methods for a access to Web app Init
        // parameters. Do not Instantiate !
    }

    static int getInteger(FacesContext context, String[] paramNames, int defaultValue) {
        String initParameter = getInitParameter(context, paramNames);

        if (null == initParameter) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(initParameter);
            } catch (NumberFormatException e) {
                throw new FacesException("Context parameter " + paramNames + " must have integer value");
            }
        }
    }

    static String getString(FacesContext context, String[] paramNames, String defaultValue) {
        String initParameter = getInitParameter(context, paramNames);

        if (null == initParameter) {
            return defaultValue;
        } else {
            return initParameter;
        }
    }

    static boolean getBoolean(FacesContext context, String[] paramNames, boolean defaultValue) {
        String initParameter = getInitParameter(context, paramNames);

        if (null == initParameter) {
            return defaultValue;
        } else if ("true".equalsIgnoreCase(initParameter) || "yes".equalsIgnoreCase(initParameter)) {
            return true;
        } else if ("false".equalsIgnoreCase(initParameter) || "no".equalsIgnoreCase(initParameter)) {
            return false;
        } else {
            throw new FacesException("Illegal value [" + initParameter + "] for a init parameter +" + paramNames
                + ", only logical values 'true' or 'false' is allowed");
        }
    }

    static String getInitParameter(FacesContext context, String[] paramNames) {
        ExternalContext externalContext = context.getExternalContext();
        String value = null;

        for (int i = 0; (i < paramNames.length) && (null == value); i++) {
            value = externalContext.getInitParameter(paramNames[i]);
        }

        return value;
    }
    
    private static boolean getBooleanValue(Object value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }

        String stringValue = (String) value;
        
        return ("true".equalsIgnoreCase(stringValue) || "yes".equalsIgnoreCase(stringValue));
    }
    
    private static Object evaluateInitParameterExpression(FacesContext context, Object parameterValue) {
        if (parameterValue == NULL || parameterValue == null) {
            return null;
        } else if (parameterValue instanceof ValueExpression) {
            ValueExpression expression = (ValueExpression) parameterValue;
            
            return (String) expression.getValue(context.getELContext());
        } else {
            return parameterValue.toString();
        }
    }

}