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

package org.ajax4jsf.renderkit;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * storing tempates variables
 *
 * @author ayukhovich@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.2 $ $Date: 2007/02/28 17:01:01 $
 */
public class ComponentVariables implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4310787278096026676L;
    private transient Map<String, Object> variablesMap = new HashMap<String, Object>();

    /**
     * setting variable
     *
     * @param variableName
     * @param variable
     */
    public void setVariable(final String variableName, final Object variable) {
        variablesMap.put(variableName, variable);
    }

    /**
     * @param variableName
     * @return
     */
    public Object getVariable(final String variableName) {
        return variablesMap.get(variableName);
    }

    /**
     * @param variableName
     * @param addValue
     */
    public void addValueToVariable(final String variableName, final Object addValue) {
        Object variable = getVariable(variableName);

        if (variable == null) {
            return;
        }

        if (variable instanceof Integer) {
            addToValue(variableName, (Integer) variable, addValue);
        }
    }

    /**
     * @param variableName
     * @param variable
     * @param addValue
     */
    protected void addToValue(final String variableName, Integer variable, final Object addValue) {
        Integer retultValue;

        if (addValue instanceof Integer) {
            Integer intAddValue = (Integer) addValue;

            retultValue = new Integer(variable.intValue() + intAddValue.intValue());
        } else {
            retultValue = variable;
        }

        setVariable(variableName, retultValue);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        variablesMap = new HashMap<String, Object>();
    }
}
