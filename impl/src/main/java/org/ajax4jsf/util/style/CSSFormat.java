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

package org.ajax4jsf.util.style;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Simple utility class for CSS style formatting
 * Current version isn't thread-safe and doesn't provide any validation
 * <p/>
 * Usage is simle
 * <code>
 * CSSFormat format = new CSSFormat();
 * format.add("background-position", "top left");
 * format.addURL("background-image", "/images/corner.gif");
 * responseWriter.writeAttribute("style", format, null);
 * </code>
 *
 * @author Maksim Kaszynski
 */
public class CSSFormat {
    private Map properties;

    /**
     * Constructs an empty CSSFormat object
     */
    public CSSFormat() {
        properties = new HashMap();
    }

    /**
     * Constructs CSSFormat object
     * and fills it with given parameters
     *
     * @param property
     * @param value
     */
    public CSSFormat(String property, String value) {
        properties = new HashMap(3);
        add(property, value);
    }

    /**
     * Surrounds given URL with <code>url()</code>
     *
     * @param url
     * @return
     */
    public static String url(String url) {
        StringBuffer buf = new StringBuffer(url.length() + 7);

        // escape url according to http://www.w3.org/TR/REC-CSS1#url
        buf.append("url(").append(url.replaceAll("([\\)\\(\\s,\'\"])", "\\\\$1")).append(")");

        return buf.toString();
    }

    /**
     * Formats property-value pair in CSS fashion
     *
     * @param property
     * @param value
     * @return
     */
    public static String propertyValue(String property, String value) {
        return formatPropertyValue(property, value);
    }

    private static String formatPropertyValue(Object property, Object value) {
        StringBuffer buf = new StringBuffer();

        buf.append(property).append(": ").append(value).append(";");

        return buf.toString();
    }

    /**
     * Adds property. If such property already exists,
     * its value is replaced with new one
     *
     * @param property
     * @param value
     * @return itself
     */
    public CSSFormat add(String property, String value) {
        properties.put(property, value);

        return this;
    }

    /**
     * adds a property with URL value
     * given value is wrapped in <code>url()</code> clause
     *
     * @param property
     * @param url
     * @return itself
     */
    public CSSFormat addURL(String property, String url) {
        properties.put(property, url(url));

        return this;
    }

    /**
     * Concatenates all properties with their values to produce single-line CSS output
     */
    public String toString() {
        return concatenate(null);
    }

    /**
     * Concatenates all properties with their values to produce CSS output
     *
     * @param separator - custom string to be inserted between properties
     * @return
     */
    public String concatenate(String separator) {
        StringBuffer output = new StringBuffer();

        for (Iterator iter = properties.entrySet().iterator(); iter.hasNext();) {
            Map.Entry entry = (Map.Entry) iter.next();

            output.append(formatPropertyValue(entry.getKey(), entry.getValue()));

            if ((separator != null) && iter.hasNext()) {
                output.append(separator);
            }
        }

        return output.toString();
    }
}
