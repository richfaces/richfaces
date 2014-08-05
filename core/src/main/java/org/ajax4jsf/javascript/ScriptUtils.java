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

import org.ajax4jsf.Messages;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import javax.faces.FacesException;
import javax.faces.context.ResponseWriter;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.SimpleTimeZone;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.3 $ $Date: 2007/01/24 13:22:31 $
 */
public final class ScriptUtils {
    private static final Logger LOG = RichfacesLogger.UTIL.getLogger();
    // there is the same pattern in client-side code:
    // richfaces.js - RichFaces.escapeCSSMetachars(s)
    private static final char[] CSS_SELECTOR_CHARS_TO_ESCAPE = createSortedCharArray("#;&,.+*~':\"!^$[]()=>|/");

    /**
     * This is utility class, don't instantiate.
     */
    private ScriptUtils() {
    }

    private static char[] createSortedCharArray(String s) {
        char[] cs = s.toCharArray();
        Arrays.sort(cs);
        return cs;
    }

    private static void appendScript(Appendable appendable, Object obj, Map<Object, Boolean> cycleBusterMap) throws IOException {
        Boolean cycleBusterValue = cycleBusterMap.put(obj, Boolean.TRUE);

        if (cycleBusterValue != null) {
            if (LOG.isDebugEnabled()) {
                String formattedMessage;
                try {
                    formattedMessage = Messages.getMessage(Messages.JAVASCRIPT_CIRCULAR_REFERENCE, obj);
                } catch (MissingResourceException e) {
                    // ignore exception: workaround for unit tests
                    formattedMessage = MessageFormat.format("Circular reference serializing object to JS: {0}", obj);
                }

                LOG.debug(formattedMessage);
            }
            appendable.append("null");
        } else if (null == obj) {
            // TODO nick - skip non-rendered values like Integer.MIN_VALUE
            appendable.append("null");
        } else if (obj instanceof ScriptString) {
            ((ScriptString) obj).appendScript(appendable);
        } else if (obj.getClass().isArray()) {
            appendable.append("[");

            boolean first = true;

            for (int i = 0; i < Array.getLength(obj); i++) {
                Object element = Array.get(obj, i);

                if (!first) {
                    appendable.append(',');
                }

                appendScript(appendable, element, cycleBusterMap);
                first = false;
            }

            appendable.append("] ");
        } else if (obj instanceof Collection<?>) {

            // Collections put as JavaScript array.
            @SuppressWarnings("unchecked")
            Collection<Object> collection = (Collection<Object>) obj;

            appendable.append("[");

            boolean first = true;

            for (Iterator<Object> iter = collection.iterator(); iter.hasNext();) {
                Object element = iter.next();

                if (!first) {
                    appendable.append(',');
                }

                appendScript(appendable, element, cycleBusterMap);
                first = false;
            }

            appendable.append("] ");
        } else if (obj instanceof Map<?, ?>) {

            // Maps put as JavaScript hash.
            @SuppressWarnings("unchecked")
            Map<Object, Object> map = (Map<Object, Object>) obj;

            appendable.append("{");

            boolean first = true;

            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                if (!first) {
                    appendable.append(',');
                }

                appendEncodedString(appendable, entry.getKey());
                appendable.append(":");
                appendScript(appendable, entry.getValue(), cycleBusterMap);
                first = false;
            }

            appendable.append("} ");
        } else if (obj instanceof Number || obj instanceof Boolean) {

            // numbers and boolean put as-is, without conversion
            appendable.append(obj.toString());
        } else if (obj instanceof String) {

            // all other put as encoded strings.
            appendEncodedString(appendable, obj);
        } else if (obj instanceof Character) {
            appendEncodedString(appendable, obj);
        } else if (obj instanceof Enum<?>) {

            // all other put as encoded strings.
            appendEncodedString(appendable, obj);
        } else {

            // All other objects threaded as Java Beans.
            appendable.append("{");

            PropertyDescriptor[] propertyDescriptors;

            try {
                propertyDescriptors = PropertyUtils.getPropertyDescriptors(obj);
            } catch (Exception e) {
                throw new FacesException("Error in conversion Java Object to JavaScript", e);
            }

            boolean ignorePropertyReadException = obj.getClass().getName().startsWith("java.sql.")
                || obj.getClass().equals(SimpleTimeZone.class);
            boolean first = true;

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String key = propertyDescriptor.getName();

                if ("class".equals(key)) {
                    continue;
                }

                Object propertyValue;

                try {
                    propertyValue = PropertyUtils.readPropertyValue(obj, propertyDescriptor);
                } catch (Exception e) {
                    if (!ignorePropertyReadException) {
                        throw new FacesException("Error in conversion Java Object to JavaScript", e);
                    } else {
                        continue;
                    }
                }

                if (!first) {
                    appendable.append(',');
                }

                appendEncodedString(appendable, key);
                appendable.append(":");
                appendScript(appendable, propertyValue, cycleBusterMap);
                first = false;
            }

            appendable.append("} ");
        }

        if (cycleBusterValue == null) {
            cycleBusterMap.remove(obj);
        }
    }

    /**
     * Convert any Java Object to JavaScript representation ( as possible ) and write it to writer immediately
     *
     * @param responseWriter
     * @param obj
     * @throws IOException
     */
    public static void writeToStream(final ResponseWriter responseWriter, Object obj) throws IOException {
        appendScript(new ResponseWriterWrapper(responseWriter), obj, new IdentityHashMap<Object, Boolean>());
    }

    /**
     * Convert any Java Object to JavaScript representation ( as possible ).
     *
     * @param obj
     * @return Java Object converted to JavaScript representation
     */
    public static String toScript(Object obj) {
        StringBuilder sb = new StringBuilder();
        if ((obj != null) && (obj instanceof String))
            sb.ensureCapacity((int)(((String) obj).length() * 1.66));

        try {
            appendScript(sb, obj, new IdentityHashMap<Object, Boolean>());
        } catch (IOException e) {

            // ignore
        }

        return sb.toString();
    }

    public static void appendScript(Appendable appendable, Object obj) throws IOException {
        appendScript(appendable, obj, new IdentityHashMap<Object, Boolean>());
    }

    public static void appendEncodedString(Appendable appendable, Object obj) throws IOException {
        appendable.append("\"");
        appendEncoded(appendable, obj);
        appendable.append("\"");
    }


    public static void appendEncoded(Appendable appendable, Object obj) throws IOException {
        String s = obj.toString();
        int start = 0;
        int end = s.length();

        for(int i = start; i < end; i++){
            char c = s.charAt(i);

            if (JSEncoder.compile(c))
                continue;

            if (start != i)
                appendable.append(s, start, i);

            appendable.append(JSEncoder.encodeCharBuffer(c));
            start = i + 1;
       }

       if (start != end)
            appendable.append(s, start, end);
   }


    public static String getValidJavascriptName(String script) {
        String s = "av_" + getMD5scriptHash(script);
        StringBuffer buf = null;
        final int len = s.length();

        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);

            if (Character.isLetterOrDigit(c) || c == '_') {

                // allowed char
                if (buf != null) {
                    buf.append(c);
                }
            } else {
                if (buf == null) {
                    buf = new StringBuffer(s.length() + 10);
                    buf.append(s.substring(0, i));
                }

                buf.append('_');

                if (c < 16) {

                    // pad single hex digit values with '0' on the left
                    buf.append('0');
                }

                if (c < 128) {

                    // first 128 chars match their byte representation in UTF-8
                    buf.append(Integer.toHexString(c).toUpperCase());
                } else {
                    byte[] bytes;

                    try {
                        bytes = Character.toString(c).getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    for (int j = 0; j < bytes.length; j++) {
                        int intVal = bytes[j];

                        if (intVal < 0) {

                            // intVal will be >= 128
                            intVal = 256 + intVal;
                        } else if (intVal < 16) {

                            // pad single hex digit values with '0' on the left
                            buf.append('0');
                        }

                        buf.append(Integer.toHexString(intVal).toUpperCase());
                    }
                }
            }
        }

        return buf == null ? s : buf.toString();
    }

    public static String getMD5scriptHash(String script) {
        byte[] bytesOfScript = new byte[0];
        try {
            bytesOfScript = script.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 appears to be an unsupported character set on this platform", e);
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to locate MD5 hash algorithm", e);
        }
        md.reset();
        //byte[] thedigest = md.digest(bytesOfScript);
        md.update(bytesOfScript);
        byte[] messageDigest = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static boolean shouldRenderAttribute(Object attributeVal) {
        if (null == attributeVal) {
            return false;
        } else if (attributeVal instanceof Boolean && ((Boolean) attributeVal).booleanValue() == Boolean.FALSE.booleanValue()) {
            return false;
        } else if (attributeVal.toString().length() == 0) {
            return false;
        } else {
            return isValidProperty(attributeVal);
        }
    }

    public static boolean shouldRenderAttribute(String attributeName, Object attributeVal) {
        return shouldRenderAttribute(attributeVal);
    }

    /**
     * Test for valid value of property. by default, for non-setted properties with Java primitive types of JSF component return
     * appropriate MIN_VALUE .
     *
     * @param property - value of property returned from {@link javax.faces.component.UIComponent#getAttributes()}
     * @return true for setted property, false otherthise.
     */
    public static boolean isValidProperty(Object property) {
        if (null == property) {
            return false;
        } else if (property instanceof Integer && ((Integer) property).intValue() == Integer.MIN_VALUE) {
            return false;
        } else if (property instanceof Double && ((Double) property).doubleValue() == Double.MIN_VALUE) {
            return false;
        } else if (property instanceof Character && ((Character) property).charValue() == Character.MIN_VALUE) {
            return false;
        } else if (property instanceof Float && ((Float) property).floatValue() == Float.MIN_VALUE) {
            return false;
        } else if (property instanceof Short && ((Short) property).shortValue() == Short.MIN_VALUE) {
            return false;
        } else if (property instanceof Byte && ((Byte) property).byteValue() == Byte.MIN_VALUE) {
            return false;
        } else if (property instanceof Long && ((Long) property).longValue() == Long.MIN_VALUE) {
            return false;
        }
        return true;
    }

    /**
     * <p>
     * Escapes CSS meta-characters in string according to <a href="http://api.jquery.com/category/selectors/">jQuery
     * selectors</a> document.
     * </p>
     *
     * @param s {@link String} to escape meta-characters in
     * @return string with escaped characters.
     */
    public static String escapeCSSMetachars(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }

        StringBuilder builder = new StringBuilder();

        int start = 0;
        int idx = 0;

        int length = s.length();

        for (; idx < length; idx++) {
            char c = s.charAt(idx);

            int searchIdx = Arrays.binarySearch(CSS_SELECTOR_CHARS_TO_ESCAPE, c);
            if (searchIdx >= 0) {
                builder.append(s.substring(start, idx));

                builder.append("\\");
                builder.append(c);

                start = idx + 1;
            }
        }

        builder.append(s.substring(start, idx));

        return builder.toString();
    }
}
