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
package org.richfaces.el.util;

/**
 * @author asmirnov
 */
public final class ELUtils {
    private ELUtils() {

        // Utility class with static methods only - do not instantiate.
    }

    /**
     * Get EL-enabled value. Return same string, if not el-expression. Otherthise, return parsed and evaluated expression.
     *
     * @param context - current Faces Context.
     * @param value - string to parse.
     * @return - interpreted el or unmodified value.
     */
    public static boolean isValueReference(String value) {
        if (value == null) {
            return false;
        }

        int start = value.indexOf("#{");

        if (start >= 0) {
            int end = value.lastIndexOf('}');

            if ((end >= 0) && (start < end)) {
                return true;
            }
        }

        return false;
    }
}
