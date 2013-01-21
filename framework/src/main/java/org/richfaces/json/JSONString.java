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
package org.richfaces.json;

/**
 * The <code>JSONString</code> interface allows a <code>toJSONString()</code> method so that a class can change the behavior of
 * <code>JSONObject.toString()</code>, <code>JSONArray.toString()</code>, and <code>JSONWriter.value(</code>Object<code>)</code>
 * . The <code>toJSONString</code> method will be used instead of the default behavior of using the Object's
 * <code>toString()</code> method and quoting the result.
 */
public interface JSONString {
    /**
     * The <code>toJSONString</code> method allows a class to produce its own JSON serialization.
     *
     * @return A strictly syntactically correct JSON text.
     */
    String toJSONString();
}
