/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ajax4jsf.util.base64;

import java.io.ByteArrayOutputStream;
import java.util.BitSet;

/**
 * <p>
 * Implements the 'www-form-urlencoded' encoding scheme,
 * also misleadingly known as URL encoding.
 * </p>
 * <p>
 * For more detailed information please refer to
 * <a href="http://www.w3.org/TR/html4/interact/forms.html#h-17.13.4.1">
 * Chapter 17.13.4 'Form content types'</a> of the
 * <a href="http://www.w3.org/TR/html4/">HTML 4.01 Specification</a>
 * </p>
 * <p>
 * This codec is meant to be a replacement for standard Java classes
 * {@link java.net.URLEncoder} and {@link java.net.URLDecoder}
 * on older Java platforms, as these classes in Java versions below
 * 1.4 rely on the platform's default charset encoding.
 * </p>
 *
 * @author Apache Software Foundation
 * @version $Id: URLCodec.java,v 1.1.2.1 2007/01/09 18:59:14 alexsmirnov Exp $
 * @since 1.2
 */
public class URLCodec implements BinaryEncoder, BinaryDecoder {
    protected static final byte ESCAPE_CHAR = '%';
    /**
     * BitSet of www-form-url safe characters.
     */
    protected static final BitSet WWW_FORM_URL = new BitSet(256);

    // Static initializer for www_form_url
    static {

        // alpha characters
        for (int i = 'a'; i <= 'z'; i++) {
            WWW_FORM_URL.set(i);
        }

        for (int i = 'A'; i <= 'Z'; i++) {
            WWW_FORM_URL.set(i);
        }

        // numeric characters
        for (int i = '0'; i <= '9'; i++) {
            WWW_FORM_URL.set(i);
        }

        // special chars
        WWW_FORM_URL.set('-');
        WWW_FORM_URL.set('_');
        WWW_FORM_URL.set('.');
        WWW_FORM_URL.set('*');

        // blank to be replaced with +
        WWW_FORM_URL.set(' ');
    }

    /**
     * Default constructor.
     */
    public URLCodec() {
        super();
    }

    public static final byte[] encodeUrl(byte[] bytes) {
        return encodeUrl(WWW_FORM_URL, bytes);
    }

    /**
     * Encodes an array of bytes into an array of URL safe 7-bit
     * <p/>
     * characters. Unsafe characters are escaped.
     *
     * @param urlsafe bitset of characters deemed URL safe
     * @param bytes array of bytes to convert to URL safe characters
     * @return array of bytes containing URL safe characters
     */
    public static final byte[] encodeUrl(BitSet urlsafe, byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        if (urlsafe == null) {
            urlsafe = WWW_FORM_URL;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i];

            if (b < 0) {
                b = 256 + b;
            }

            if (urlsafe.get(b)) {
                if (b == ' ') {
                    b = '+';
                }

                buffer.write(b);
            } else {
                buffer.write('%');

                char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
                char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));

                buffer.write(hex1);
                buffer.write(hex2);
            }
        }

        return buffer.toByteArray();
    }

    /**
     * Decodes an array of URL safe 7-bit characters into an array of
     * <p/>
     * original bytes. Escaped characters are converted back to their
     * <p/>
     * original representation.
     *
     * @param bytes array of URL safe characters
     * @return array of original bytes
     * @throws DecoderException Thrown if URL decoding is unsuccessful
     */
    public static final byte[] decodeUrl(byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i];

            if (b == '+') {
                buffer.write(' ');
            } else if (b == '%') {
                try {
                    int u = Character.digit((char) bytes[++i], 16);
                    int l = Character.digit((char) bytes[++i], 16);

                    if ((u == -1) || (l == -1)) {
                        throw new DecoderException("Invalid URL encoding");
                    }

                    buffer.write((char) ((u << 4) + l));
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new DecoderException("Invalid URL encoding");
                }
            } else {
                buffer.write(b);
            }
        }

        return buffer.toByteArray();
    }

    /**
     * Encodes an array of bytes into an array of URL safe 7-bit
     * <p/>
     * characters. Unsafe characters are escaped.
     *
     * @param bytes array of bytes to convert to URL safe characters
     * @return array of bytes containing URL safe characters
     */
    public byte[] encode(byte[] bytes) {
        return encodeUrl(WWW_FORM_URL, bytes);
    }

    /**
     * Decodes an array of URL safe 7-bit characters into an array of
     * <p/>
     * original bytes. Escaped characters are converted back to their
     * <p/>
     * original representation.
     *
     * @param bytes array of URL safe characters
     * @return array of original bytes
     * @throws DecoderException Thrown if URL decoding is unsuccessful
     */
    public byte[] decode(byte[] bytes) throws DecoderException {
        return decodeUrl(bytes);
    }

    /**
     * Encodes an object into its URL safe form. Unsafe characters are
     * <p/>
     * escaped.
     *
     * @param pObject string to convert to a URL safe form
     * @return URL safe object
     * @throws EncoderException Thrown if URL encoding is not
     *         <p/>
     *         applicable to objects of this type or
     *         <p/>
     *         if encoding is unsuccessful
     */
    public Object encode(Object pObject) throws EncoderException {
        if (pObject == null) {
            return null;
        } else if (pObject instanceof byte[]) {
            return encode((byte[]) pObject);
        } else if (pObject instanceof String) {
            return encode((String) pObject);
        } else {
            throw new EncoderException("Objects of type " + pObject.getClass().getName() + " cannot be URL encoded");
        }
    }

    /**
     * Decodes a URL safe object into its original form. Escaped
     * <p/>
     * characters are converted back to their original representation.
     *
     * @param pObject URL safe object to convert into its original form
     * @return original object
     * @throws DecoderException Thrown if URL decoding is not
     *         <p/>
     *         applicable to objects of this type
     *         <p/>
     *         if decoding is unsuccessful
     */
    public Object decode(Object pObject) throws DecoderException {
        if (pObject == null) {
            return null;
        } else if (pObject instanceof byte[]) {
            return decode((byte[]) pObject);
        } else if (pObject instanceof String) {
            return decode((String) pObject);
        } else {
            throw new DecoderException("Objects of type " + pObject.getClass().getName() + " cannot be URL decoded");
        }
    }
}
