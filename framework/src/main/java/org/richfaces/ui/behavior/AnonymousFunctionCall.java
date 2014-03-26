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

package org.richfaces.ui.behavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ajax4jsf.javascript.ScriptStringBase;

public class AnonymousFunctionCall extends ScriptStringBase {

    private StringBuffer body = new StringBuffer();

    private List<Object> parameterNames = new ArrayList<Object>();
    private List<Object> parameterValues = new ArrayList<Object>();

    public AnonymousFunctionCall(Object... parameterNames) {
        this.parameterNames.addAll(Arrays.asList(parameterNames));
    }

    @Override
    public void appendScript(Appendable target) throws IOException {
        target.append(LEFT_ROUND_BRACKET).append(FUNCTION).append(LEFT_ROUND_BRACKET);
        boolean first = true;
        for (Object element : parameterNames) {
            if (!first) {
                target.append(COMMA);
            }
            target.append(element.toString());
            first = false;
        }
        target.append(RIGHT_ROUND_BRACKET).append(LEFT_CURLY_BRACKET).append(body).append(RIGHT_CURLY_BRACKET)
                .append(RIGHT_ROUND_BRACKET).append(LEFT_ROUND_BRACKET);
        first = true;
        for (Object element : parameterValues) {
            if (!first) {
                target.append(COMMA);
            }
            target.append(element.toString());
            first = false;
        }
        target.append(RIGHT_ROUND_BRACKET);
    }

    public AnonymousFunctionCall addParameterName(Object... param) {
        parameterNames.addAll(Arrays.asList(param));
        return this;
    }

    public AnonymousFunctionCall addParameterValue(Object... param) {
        parameterValues.addAll(Arrays.asList(param));
        return this;
    }

    public AnonymousFunctionCall addToBody(Object body) {
        this.body.append(body);
        return this;
    }
}
