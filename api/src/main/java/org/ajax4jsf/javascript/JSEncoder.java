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
package org.ajax4jsf.javascript;

/**
 * @author shura Encode chars as JavaScript sequences
 */
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
    public boolean compile(char c) {
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

            default: {
                char[] ret = { '\\', 'u', ENCODE_HEX[c >> 0xc & 0xf], ENCODE_HEX[c >> 0x8 & 0xf], ENCODE_HEX[c >> 0x4 & 0xf],
                        ENCODE_HEX[c & 0xf] };

                return ret;
            }
        }
    }
}
