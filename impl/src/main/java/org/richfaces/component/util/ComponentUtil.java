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

package org.richfaces.component.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Nick Belaevski
 *         mailto:nbelaevski@exadel.com
 *         created 20.07.2007
 */
public final class ComponentUtil {
    private ComponentUtil() {
    }

    public static String[] asArray(Object object) {
        if (object == null) {
            return null;
        }

        Class componentType = object.getClass().getComponentType();

        if (String.class.equals(componentType)) {
            return (String[]) object;
        } else if (componentType != null) {
            Object[] objects = (Object[]) object;
            String[] result = new String[objects.length];

            for (int i = 0; i < objects.length; i++) {
                Object o = objects[i];

                if (o == null) {
                    continue;
                }

                result[i] = o.toString();
            }

            return result;
        } else if (object instanceof Collection) {
            Collection collection = (Collection) object;
            String[] result = new String[collection.size()];
            Iterator iterator = collection.iterator();

            for (int i = 0; i < result.length; i++) {
                Object next = iterator.next();

                if (next == null) {
                    continue;
                }

                result[i] = next.toString();
            }

            return result;
        } else {
            String string = object.toString().trim();
            String[] split = string.split("\\s*,\\s*");

            return split;
        }
    }
}
