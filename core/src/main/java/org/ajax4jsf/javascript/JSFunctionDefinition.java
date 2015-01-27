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
import java.util.List;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.2 $ $Date: 2007/01/24 13:22:31 $
 *
 */
public class JSFunctionDefinition extends ScriptStringBase implements ScriptString {
    private List<Object> parameters = new ArrayList<Object>();
    private StringBuffer body = new StringBuffer();
    private String name;

    /**
     * Construct {@link JSFunctionDefinition} with arbitrary list of params
     *
     * @param params
     */
    public JSFunctionDefinition(Object... params) {
        parameters.addAll(Arrays.asList(params));
    }

    public void addParameter(Object param) {
        parameters.add(param);
    }

    public JSFunctionDefinition addToBody(Object body) {
        this.body.append(body);

        return this;
    }

    public void appendScript(Appendable target) throws IOException {
        appendFunctionName(target);
        appendParameters(target);
        target.append(LEFT_CURLY_BRACKET);
        appendBody(target);
        target.append(RIGHT_CURLY_BRACKET);
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    protected void appendFunctionName(Appendable target) throws IOException {
        if (null != name) {
            target.append(name).append(" = ");
        }

        target.append(FUNCTION);
    }

    protected void appendBody(Appendable target) throws IOException {
        target.append(body);
    }

    private void appendParameters(Appendable target) throws IOException {
        target.append(LEFT_ROUND_BRACKET);

        boolean first = true;

        for (Object element : parameters) {
            if (!first) {
                target.append(COMMA);
            }

            target.append(element.toString());
            first = false;
        }

        target.append(RIGHT_ROUND_BRACKET);
    }
}
