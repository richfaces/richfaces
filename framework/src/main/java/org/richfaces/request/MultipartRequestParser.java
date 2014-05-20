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
package org.richfaces.request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 * @author Michal Petrov
 *
 */

public class MultipartRequestParser {
    static final String PARAM_FILENAME = "filename";
    static final String PARAM_CONTENT_TYPE = "Content-Type";
    public static final String UID_KEY = "rf_fu_uid";
    private static final Pattern AMPERSAND = Pattern.compile("&+");
    private static final Logger LOGGER = RichfacesLogger.CONTEXT.getLogger();
    private static final Pattern FILE_NAME_PATTERN = Pattern.compile(".*filename=\"(.*)\"");

    public static String getParameterValueFromQueryString(String queryString) {
        if (queryString != null) {
            String[] nvPairs = AMPERSAND.split(queryString);
            for (String nvPair : nvPairs) {
                if (nvPair.length() == 0) {
                    continue;
                }

                int eqIdx = nvPair.indexOf('=');
                if (eqIdx >= 0) {
                    try {
                        String name = URLDecoder.decode(nvPair.substring(0, eqIdx), "UTF-8");

                        if (UID_KEY.equals(name)) {
                            return URLDecoder.decode(nvPair.substring(eqIdx + 1), "UTF-8");
                        }
                    } catch (UnsupportedEncodingException e) {
                        // log warning and skip this parameter
                        LOGGER.debug(e.getMessage(), e);
                    }
                }
            }
        }

        return null;
    }

    private static String decodeFileName(String name) {
        String fileName = null;
        try {
            StringBuilder builder = new StringBuilder();
            String[] codes = name.split(";");
            if (codes != null) {
                for (String code : codes) {
                    if (code.startsWith("&")) {
                        String sCode = code.replaceAll("[&#]*", "");
                        Integer iCode = Integer.parseInt(sCode);
                        builder.append(Character.toChars(iCode));
                    } else {
                        builder.append(code);
                    }
                }
                fileName = builder.toString();
            }
        } catch (Exception e) {
            fileName = name;
        }

        return fileName;
    }

    public static String parseFileName(String parseStr) {
        Matcher m = FILE_NAME_PATTERN.matcher(parseStr);
        if (m.matches()) {
            String name = m.group(1);
            if (name.startsWith("&")) {
                return decodeFileName(name);
            } else {
                return name;
            }
        }
        return null;
    }
}
