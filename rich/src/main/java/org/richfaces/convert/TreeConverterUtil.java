/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.convert;

import java.text.MessageFormat;

import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 *
 */
public final class TreeConverterUtil {
    private static final class SeparatorCharHolder {
        static final char SEPARATOR_CHAR = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());

        private SeparatorCharHolder() {
        }
    }

    private static final char ESCAPE_CHAR = '_';

    private TreeConverterUtil() {
    }

    private static boolean shouldEscape(char c) {
        if ('a' <= c && c <= 'z') {
            return false;
        }

        if ('A' <= c && c <= 'Z') {
            return false;
        }

        if ('0' <= c && c <= '9') {
            return false;
        }

        if (c == ESCAPE_CHAR || c == SeparatorCharHolder.SEPARATOR_CHAR) {
            return true;
        }

        if ('-' == c || '\u00B7' == c) {
            return false;
        }

        if ('\u00C0' <= c && c <= '\u00D6') {
            return false;
        }

        if ('\u00D8' <= c && c <= '\u00F6') {
            return false;
        }

        if ('\u00F8' <= c && c <= '\u02FF') {
            return false;
        }

        if ('\u0370' <= c && c <= '\u037D') {
            return false;
        }

        if ('\u037F' <= c && c <= '\u1FFF') {
            return false;
        }

        if ('\u200C' <= c && c <= '\u200D') {
            return false;
        }

        if ('\u2070' <= c && c <= '\u218F') {
            return false;
        }

        if ('\u2C00' <= c && c <= '\u2FEF') {
            return false;
        }

        if ('\u3001' <= c && c <= '\uD7FF') {
            return false;
        }

        if ('\uF900' <= c && c <= '\uFDCF') {
            return false;
        }

        if ('\uFDF0' <= c && c <= '\uFFFD') {
            return false;
        }

        if ('\u0300' <= c && c <= '\u036F') {
            return false;
        }

        if ('\u203F' <= c && c <= '\u2040') {
            return false;
        }

        return true;
    }

    private static int parseHexString(String s) {
        int result = 0;

        for (int i = 0; i < s.length(); i++) {
            result <<= 4;

            char c = s.charAt(i);

            int digitValue = Character.digit(c, 16);
            if (digitValue < 0) {
                throw new NumberFormatException();
            }

            result += digitValue;
        }

        return result;
    }

    private static void checkAvailable(String s, int idx, int len) {
        if (s.length() < idx + len) {
            throw new IllegalArgumentException(MessageFormat.format(
                "Expected {0} available chars in ''{1}'' string starting from {2} index", len, s, idx));
        }
    }

    private static char getNextChar(String s, int idx) {
        checkAvailable(s, idx, 1);

        return s.charAt(idx);
    }

    private static char readNextCharFromHexCode(String s, int idx, int len) {
        checkAvailable(s, idx, len);

        String numString = s.substring(idx, idx + len);
        try {
            int charValue = parseHexString(numString);
            return (char) charValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(MessageFormat.format(
                "Substring ''{0}'' of ''{1}'' string is not a valid hex number ", numString, s));
        }
    }

    public static String unescape(String s) {
        StringBuilder sb = new StringBuilder(s.length());

        int startIdx = 0;

        while (startIdx >= 0) {
            int idx = s.indexOf(ESCAPE_CHAR, startIdx);

            if (idx >= 0) {
                sb.append(s.subSequence(startIdx, idx));

                idx++; // skip escape char

                char c = getNextChar(s, idx);

                switch (c) {
                    case ESCAPE_CHAR:
                        idx++;
                        sb.append(c);
                        break;

                    case 'x':
                        idx++;
                        sb.append(readNextCharFromHexCode(s, idx, 2));
                        idx += 2;
                        break;

                    case 'u':
                        idx++;
                        sb.append(readNextCharFromHexCode(s, idx, 4));
                        idx += 4;
                        break;

                    default:
                        throw new IllegalArgumentException(MessageFormat.format(
                            "Unexpected char ''{0}'' in ''{1}'' string located at index {2}", c, s, idx));
                }
            } else {
                sb.append(s.subSequence(startIdx, s.length()));
            }

            startIdx = idx;
        }

        return sb.toString();
    }

    public static String escape(String s) {
        StringBuilder sb = new StringBuilder(s.length());

        int start = 0;

        char[] cs = s.toCharArray();
        for (int idx = 0; idx < cs.length; idx++) {
            char c = cs[idx];

            if (shouldEscape(c)) {
                sb.append(cs, start, idx - start);
                sb.append(ESCAPE_CHAR);

                if (c == ESCAPE_CHAR) {
                    sb.append(c); // ${escapeChar}
                } else {
                    String asHex = Integer.toHexString(c);
                    switch (asHex.length()) {

                        case 1:
                            sb.append("x0"); // _x05
                            break;
                        case 2:
                            sb.append("x"); // _xef
                            break;
                        case 3:
                            sb.append("u0"); // _u0fed
                            break;
                        case 4:
                            sb.append("u"); // _ufcda
                            break;

                        default:
                            throw new IllegalArgumentException();
                    }

                    sb.append(asHex);
                }

                start = idx + 1;
            }
        }

        if (start < s.length()) {
            sb.append(cs, start, s.length() - start);
        }

        return sb.toString();
    }
}