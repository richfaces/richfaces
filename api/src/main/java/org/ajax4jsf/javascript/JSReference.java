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

/**
 * Create reference to JavaScript variable with optional index.
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:58:30 $
 *
 */
public class JSReference extends ScriptStringBase {
    public static final JSReference EVENT = new JSReference("event");
    public static final JSReference THIS = new JSReference("this");
    public static final JSReference TRUE = new JSReference("true");
    public static final JSReference NULL = new JSReference("null");
    public static final JSReference FALSE = new JSReference("false");
    private Object index = null;
    private String name;

    /**
     * @param name
     */
    public JSReference(String name) {

        // TODO Auto-generated constructor stub
        this.name = name;
    }

    /**
     * @param name
     * @param index
     */
    public JSReference(String name, Object index) {

        // TODO Auto-generated constructor stub
        this.name = name;
        this.index = index;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.javascript.ScriptString#appendScript(java.lang.StringBuffer)
     */
    public void appendScript(StringBuffer functionString) {
        functionString.append(name);

        if (null != index) {
            functionString.append("[").append(ScriptUtils.toScript(index)).append("]");
        }
    }
}
