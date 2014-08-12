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

/**
 * Encode chars as JavaScript sequences
 *
 * @author shura
 */
import java.nio.CharBuffer;

public class JSEncoder {
    // private char APOSTROPHE[] = { '\\', '\'' };
    private static final char[] ENCODE_HEX = "0123456789ABCDEF".toCharArray();

    private static final char[] ENCODE_QUOT = "\\\"".toCharArray();
    private static final char[] ENCODE_LF = "\\n".toCharArray();
    private static final char[] ENCODE_BC = "\\b".toCharArray();
    private static final char[] ENCODE_FF = "\\f".toCharArray();
    private static final char[] ENCODE_CR = "\\r".toCharArray();
    private static final char[] ENCODE_TAB = "\\t".toCharArray();
    private static final char[] ENCODE_BS = "\\\\".toCharArray();
    private static final char[] ENCODE_FS = "\\/".toCharArray();

    // frequent  '-' ']' '<' '>' chars
    private static final char[] ENCODE_HM = "\\u002D".toCharArray();
    private static final char[] ENCODE_RB = "\\u005D".toCharArray();
    private static final char[] ENCODE_LT = "\\u003C".toCharArray();
    private static final char[] ENCODE_GT = "\\u003E".toCharArray();

    private static final CharBuffer ENCODE_QUOT_CB = CharBuffer.wrap(ENCODE_QUOT);
    private static final CharBuffer ENCODE_LF_CB = CharBuffer.wrap(ENCODE_LF);
    private static final CharBuffer ENCODE_BC_CB = CharBuffer.wrap(ENCODE_BC);
    private static final CharBuffer ENCODE_FF_CB = CharBuffer.wrap(ENCODE_FF);
    private static final CharBuffer ENCODE_CR_CB = CharBuffer.wrap(ENCODE_CR);
    private static final CharBuffer ENCODE_TAB_CB = CharBuffer.wrap(ENCODE_TAB);
    private static final CharBuffer ENCODE_BS_CB = CharBuffer.wrap(ENCODE_BS);
    private static final CharBuffer ENCODE_FS_CB = CharBuffer.wrap(ENCODE_FS);
    private static final CharBuffer ENCODE_HM_CB = CharBuffer.wrap(ENCODE_HM);
    private static final CharBuffer ENCODE_RB_CB = CharBuffer.wrap(ENCODE_RB);
    private static final CharBuffer ENCODE_LT_CB = CharBuffer.wrap(ENCODE_LT);
    private static final CharBuffer ENCODE_GT_CB = CharBuffer.wrap(ENCODE_GT);


    // private static final char ENCODE_ESC[] = "\\e".toCharArray();

    /**
     * Create a new instance of this <code>XMLEncoder</code>.
     */
    public JSEncoder() {
    }

    /**
     * Return true or false whether this encoding/format can encode the specified character or not.
     * <p>
     * This method will return true for the following character range: <br />
     * <code>
     *   <nobr>\b | \f | \t | \r | \n | " | \ | / | [#x20-#xD7FF] | [#xE000-#xFFFD]</nobr>
     * </code>
     * </p>
     *
     * @see <a href="http://www.w3.org/TR/REC-xml#charsets">W3C XML 1.0 </a>
     * @see <a href="http://json.org/">JSON.org</a>
     */
    public static boolean compile(char c) {
        if ((c == '\b') || (c == '\f') | (c == '\t') || (c == '\n') || (c == '\r') || (c == '"') || (c == '\\') || (c == '/')
            || (c == ']') || // ] - to avoid conflicts in CDATA
            (c == '<') || // - escape HTML markup characters
            (c == '>') || // - HTML
            (c == '&') || // - HTML
            (c == '-') || // - HTML comments
            (c < 0x20) || // See <http://www.w3.org/TR/REC-xml#charsets>
            ((c > 0xd7ff) && (c < 0xe000)) || (c > 0xfffd) || (c > 0xff)) {
            return false;
        }

        return true;
    }

    public static CharBuffer encodeCharBuffer(char c) {
        switch (c) {
            case '\b':
                return ENCODE_BC_CB;

            case '\f':
                return ENCODE_FF_CB;

            case '\t':
                return ENCODE_TAB_CB;

            case '\n':
                return ENCODE_LF_CB;

            case '\r':
                return ENCODE_CR_CB;

            case '"':
                return ENCODE_QUOT_CB;

            case '\\':
                return ENCODE_BS_CB;

            case '/':
                return ENCODE_FS_CB;

            case '-':
                return ENCODE_HM_CB;

            case ']':
                return ENCODE_RB_CB;

            case '<':
                return ENCODE_LT_CB;

            case '>':
                return ENCODE_GT_CB;

            default: {
                char[] ret = { '\\', 'u', ENCODE_HEX[c >> 0xc & 0xf], ENCODE_HEX[c >> 0x8 & 0xf],
                     ENCODE_HEX[c >> 0x4 & 0xf], ENCODE_HEX[c & 0xf] };

                return CharBuffer.wrap(ret);
            }
        }
    }

    /**
     * Return an array of characters representing the encoding for the specified character.
     */
    public char[] encode(char c) {
        switch (c) {
            case '\b':
                return ENCODE_BC;

            case '\f':
                return ENCODE_FF;

            case '\t':
                return ENCODE_TAB;

            case '\n':
                return ENCODE_LF;

            case '\r':
                return ENCODE_CR;

            case '"':
                return ENCODE_QUOT;

            case '\\':
                return ENCODE_BS;

            case '/':
                return ENCODE_FS;

            case '-':
                return ENCODE_HM;

            case ']':
                return ENCODE_RB;

            case '<':
                return ENCODE_LT;

            case '>':
                return ENCODE_GT;

            default: {
                char[] ret = { '\\', 'u', ENCODE_HEX[c >> 0xc & 0xf], ENCODE_HEX[c >> 0x8 & 0xf], ENCODE_HEX[c >> 0x4 & 0xf],
                        ENCODE_HEX[c & 0xf] };

                return ret;
            }
        }
    }
}
