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
 * @author shura
 * Encode chars as JavaScript sequences
 */
public class JSEncoder {

    // private char APOSTROPHE[] = { '\\', '\'' };
    private static final char[] ENCODE_HEX = "0123456789ABCDEF".toCharArray();
    private static final char[] ENCODE_APOS = "\\'".toCharArray();
    private static final char[] ENCODE_QUOT = "\\\"".toCharArray();
    private static final char[] ENCODE_LF = "\\n".toCharArray();
    private static final char[] ENCODE_FF = "\\f".toCharArray();
    private static final char[] ENCODE_CR = "\\r".toCharArray();
    private static final char[] ENCODE_TAB = "\\t".toCharArray();
    private static final char[] ENCODE_BS = "\\\\".toCharArray();

    // private static final char ENCODE_ESC[] = "\\e".toCharArray();

    /**
     * Create a new instance of this <code>XMLEncoder</code>.
     */
    public JSEncoder() {}

    /**
     * Return true or false wether this encoding can encode the specified
     * character or not.
     * <p>
     * This method will return true for the following character range: <br />
     * <code>
     *   <nobr>#x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD]</nobr>
     * </code>
     * </p>
     *
     * @see <a href="http://www.w3.org/TR/REC-xml#charsets">W3C XML 1.0 </a>
     */
    public boolean compile(char c) {
        if ((c == 0x09) || // [\t]
                (c == 0x0a) || // [\n]
                    (c == 0x0d) || // [\r](c == 0x22) || // ["]
                        (c == 0x22) || // ["]
                            (c == 0x27) || // [']
                                (c == 0x5c) || // [\]
                                    (c == 0x03) || // [esc]
                                        (c == ']') || // ] - to avoid conflicts in CDATA
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
     * Return an array of characters representing the encoding for the specified
     * character.
     */
    public char[] encode(char c) {
        switch (c) {
            case 0x03 :
                return ENCODE_FF; // (>) [&gt;]

            case 0x09 :
                return ENCODE_TAB; // (>) [&gt;]

            case 0x0a :
                return ENCODE_LF; // (>) [&gt;]

            case 0x0d :
                return ENCODE_CR; // (>) [&gt;]

            case 0x22 :
                return ENCODE_QUOT; // (") [&quot;]

            case 0x27 :
                return ENCODE_APOS; // (') [&apos;]

            case 0x5c :
                return ENCODE_BS; // (<) [&lt;]

            default : {
                if (c > 0xff) {
                    char[] ret = {
                        '\\', 'u', ENCODE_HEX[c >> 0xc & 0xf], ENCODE_HEX[c >> 0x8 & 0xf], ENCODE_HEX[c >> 0x4 & 0xf],
                        ENCODE_HEX[c & 0xf]
                    };

                    return ret;
                }

                char[] ret = {'\\', 'x', ENCODE_HEX[c >> 0x4 & 0xf], ENCODE_HEX[c & 0xf]};

                return ret;
            }
        }
    }
}
