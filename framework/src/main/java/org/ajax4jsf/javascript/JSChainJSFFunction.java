/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

/**
 * @author Nick Belaevski
 *
 */
public class JSChainJSFFunction extends JSFunction {
    public JSChainJSFFunction(Object... parameters) {
        super("jsf.util.chain", (Object[]) createFunctionArgs(parameters));
    }

    private static Object[] createFunctionArgs(Object[] sourceParams) {
        Object[] result = new Object[sourceParams.length + 2];

        result[0] = JSReference.THIS;
        result[1] = JSReference.EVENT;

        for (int i = 0; i < sourceParams.length; i++) {
            result[i + 2] = sourceParams[i];
        }
        return result;
    }
}
