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

import java.io.IOException;

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

    public void appendScript(Appendable target) throws IOException {
        target.append("function () {");
        target.append("var vars = {");

        boolean isFirst = true;

        for (String var : vars) {
            if (isFirst) {
                isFirst = false;
            } else {
                target.append(',');
            }

            target.append(var);
            target.append(':');
            target.append(var);
        }

        target.append("};");
        target.append("return function() { with (vars) {");
        target.append(function.toScript());
        target.append("}}}()");
    }

}
