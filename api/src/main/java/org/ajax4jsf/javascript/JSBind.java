/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

/**
 * Created 04.08.2008
 * @author Nick Belaevski
 * @since 3.2.2
 */
public class JSBind extends ScriptStringBase {
    private JSFunction function;
    private String[] vars;

    public JSBind(JSFunction function, String... vars) {
        super();
        this.function = function;
        this.vars = vars;
    }

    public void appendScript(StringBuffer functionString) {
        functionString.append("function () {");
        functionString.append("var vars = {");

        boolean isFirst = true;

        for (String var : vars) {
            if (isFirst) {
                isFirst = false;
            } else {
                functionString.append(',');
            }

            functionString.append(var);
            functionString.append(':');
            functionString.append(var);
        }

        functionString.append("};");
        functionString.append("return function() { with (vars) {");
        functionString.append(function.toScript());
        functionString.append("}}}()");
    }
}
