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

/**
 * Create reference to JavaScript variable with optional index.
 *
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
    private final Object index;
    private final String name;

    /**
     * @param name
     */
    public JSReference(String name) {
        this.name = name;
        this.index = null;
    }

    /**
     * @param name
     * @param index
     */
    public JSReference(String name, Object index) {
        this.name = name;
        this.index = index;
    }

    public void appendScript(Appendable target) throws IOException {
        target.append(name);

        if (null != index) {
            target.append("[");
            ScriptUtils.appendScript(target, index);
            target.append("]");
        }
    }
}
