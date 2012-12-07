/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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



/**
 * @author Nick Belaevski
 *
 */
public final class FastJoiner {
    private String separator;

    private FastJoiner(String separator) {
        super();
        this.separator = separator;
    }

    private String maskNullString(String s) {
        if (s != null) {
            return s;
        }

        return "";
    }

    public String join(String s1, String s2) {
        if (s1 == null) {
            return maskNullString(s2);
        } else if (s2 == null) {
            return maskNullString(s1);
        }

        StringBuilder sb = new StringBuilder(s1.length() + separator.length() + s2.length());

        sb.append(s1);
        if (!s2.startsWith(separator)) {
            sb.append(separator);
        }
        sb.append(s2);

        return sb.toString();
    }

    public String join(String... strings) {
        StringBuilder sb = new StringBuilder();

        boolean first = true;

        for (String s : strings) {
            if (s == null) {
                continue;
            }

            if (first) {
                first = false;
            } else {
                sb.append(separator);
            }

            sb.append(s);
        }

        return sb.toString();
    }

    public String join(Iterable<String> strings) {
        StringBuilder sb = new StringBuilder();

        boolean first = true;

        for (String s : strings) {
            if (s == null) {
                continue;
            }

            if (first) {
                first = false;
            } else {
                sb.append(separator);
            }

            sb.append(s);
        }

        return sb.toString();
    }

    public static FastJoiner on(String sep) {
        return new FastJoiner(sep);
    }

    public static FastJoiner on(char c) {
        return new FastJoiner(String.valueOf(c));
    }
}
