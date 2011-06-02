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
package org.richfaces.resource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public final class ResourceUtils {
    private static final long MILLISECOND_IN_SECOND = 1000L;
    private static final String QUOTED_STRING_REGEX = "(?:\\\\[\\x00-\\x7F]|[^\"\\\\])+";
    private static final Pattern ETAG_PATTERN = Pattern.compile("(?:W/)?\"(" + QUOTED_STRING_REGEX + ")\"(?:,\\s*)?");

    private ResourceUtils() {
    }

    public static String formatWeakTag(String eTag) {
        String formattedTag = formatTag(eTag);

        if (formattedTag == null) {
            return null;
        }

        return "W/" + formattedTag;
    }

    public static String formatTag(String eTag) {
        if (eTag == null) {
            return null;
        }

        if (!eTag.matches(QUOTED_STRING_REGEX)) {
            throw new IllegalArgumentException("tag must matches to regexp '" + QUOTED_STRING_REGEX + '\'');
        }

        return '\"' + eTag + '\"';
    }

    public static boolean matchTag(String eTag, String eTagHeaderValue) {
        if ((eTag == null) || (eTagHeaderValue == null)) {
            throw new IllegalArgumentException("tag and tagHeaderValue must be not null");
        }

        Matcher eTagMatcher = ETAG_PATTERN.matcher(eTag);

        if (!eTagMatcher.find()) {
            throw new IllegalArgumentException();
        }

        String tag = eTagMatcher.group(1);
        Matcher eTagHeaderMatcher = ETAG_PATTERN.matcher(eTagHeaderValue);

        while (eTagHeaderMatcher.find()) {
            if (tag.equals(eTagHeaderMatcher.group(1))) {
                return true;
            }
        }

        return false;
    }

    public static long millisToSecond(long millisecond) {
        return millisecond / MILLISECOND_IN_SECOND;
    }

    public static long secondToMillis(long second) {
        return second * MILLISECOND_IN_SECOND;
    }
}
