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
package org.richfaces.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

/**
 * Coercing object as a set
 *
 * @author shura
 */
public final class Sets {

    private static final Pattern ID_SPLIT_PATTERN = Pattern.compile("\\s*(\\s|,)\\s*");

    private Sets() {
    }

    @SuppressWarnings("unchecked")
    public static Set<String> asSet(Object valueToSet) {
        if (null != valueToSet) {

            // Simplest case - set.
            if (valueToSet instanceof Set) {
                return new LinkedHashSet<String>((Set<String>) valueToSet);
            } else if (valueToSet instanceof Collection) { // Other collections.
                return new LinkedHashSet<String>((Collection<String>) valueToSet);
            } else if (Object[].class.isAssignableFrom(valueToSet.getClass())) { // Array
                return new LinkedHashSet<String>(Arrays.asList((String[]) valueToSet));
            } else if (valueToSet instanceof String) { // Tokenize string.
                String areasString = ((String) valueToSet).trim();

                if (areasString.contains(",") || areasString.contains(" ")) {
                    String[] values = ID_SPLIT_PATTERN.split(areasString);

                    Set<String> result = new LinkedHashSet<String>(values.length);
                    for (String value : values) {
                        if (Strings.isNullOrEmpty(value)) {
                            continue;
                        }

                        result.add(value);
                    }

                    return result;
                } else {
                    Set<String> areasSet = new LinkedHashSet<String>(5);

                    if (!Strings.isNullOrEmpty(areasString)) {
                        areasSet.add(areasString);
                    }

                    return areasSet;
                }
            }
        }

        return null;
    }
}
