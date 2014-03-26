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
package org.ajax4jsf.javascript;

import java.io.IOException;
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

    public void appendScript(Appendable target) throws IOException {
        target.append(name).append('(');

        boolean first = true;
        List<?> parameters = getParameters();

        if (null != parameters) {
            for (Iterator<?> param = parameters.iterator(); param.hasNext();) {
                Object element = param.next();

                if (!first) {
                    target.append(',');
                }

                if (null != element) {
                    ScriptUtils.appendScript(target, element);
                } else {
                    target.append("null");
                }

                first = false;
            }
        }

        target.append(")");
    }

    /**
     * @return the parameters
     */
    public List<Object> getParameters() {
        return this.parameters;
    }
}
