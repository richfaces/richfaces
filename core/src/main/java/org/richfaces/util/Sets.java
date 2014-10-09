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
    private static final String BRACKETS = "\\[.*\\]";
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

                if (Pattern.matches(BRACKETS, areasString)) {
                    areasString = areasString.substring(1, areasString.length() - 1);
                }

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
