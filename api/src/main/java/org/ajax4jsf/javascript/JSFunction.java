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



package org.ajax4jsf.javascript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.3 $ $Date: 2007/02/06 16:23:26 $
 *
 */
public class JSFunction extends ScriptStringBase implements ScriptString {
    private List<Object> parameters = new ArrayList<Object>();
    private String name;

    /**
     * @param name
     * @param parameters
     */
    public JSFunction(String name, Object... parameters) {
        this.name = name;
        this.parameters.addAll(Arrays.asList(parameters));
    }

    public JSFunction addParameter(Object parameter) {
        getParameters().add(parameter);

        return this;
    }

    public void appendScript(StringBuffer functionString) {
        functionString.append(name).append('(');

        boolean first = true;
        List<?> parameters = getParameters();

        if (null != parameters) {
            for (Iterator<?> param = parameters.iterator(); param.hasNext(); ) {
                Object element = param.next();

                if (!first) {
                    functionString.append(',');
                }

                if (null != element) {
                    functionString.append(ScriptUtils.toScript(element));
                } else {
                    functionString.append("null");
                }

                first = false;
            }
        }

        functionString.append(")");
    }

    /**
     * @return the parameters
     */
    public List<Object> getParameters() {
        return this.parameters;
    }
}
