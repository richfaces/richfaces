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

import java.io.IOException;

import javax.faces.FacesException;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.2 $ $Date: 2007/01/26 10:38:52 $
 *
 */
public abstract class ScriptStringBase implements ScriptString {
    public static final String FUNCTION = "function";
    public static final String EQUALS = "=";
    public static final String DOT = ".";
    public static final String COMMA = ",";
    public static final String EMPTY_STRING = " ";
    public static final String LEFT_CURLY_BRACKET = "{";
    public static final String RIGHT_CURLY_BRACKET = "}";
    public static final String LEFT_ROUND_BRACKET = "(";
    public static final String RIGHT_ROUND_BRACKET = ")";
    public static final String LEFT_SQUARE_BRACKET = "[";
    public static final String RIGHT_SQUARE_BRACKET = "]";
    public static final String COLON = ":";

    public String toScript() {
        StringBuilder builder = new StringBuilder();

        try {
            appendScript(builder);
            return builder.toString();
        } catch (IOException e) {
            throw new FacesException(e.getMessage(), e);
        }
    }

    public void appendScriptToStringBuilder(StringBuilder stringBuilder) {
        try {
            appendScript(stringBuilder);
        } catch (IOException e) {
            // ignore
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return toScript();
    }
}
