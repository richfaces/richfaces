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
package org.richfaces.renderkit.util;

import org.ajax4jsf.Messages;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

public final class HtmlDimensions {
    private static final Pattern PATTERN_NUMERIC = Pattern.compile("^[+-]?\\d+(\\.\\d+)?$");
    private static final Pattern PATTERN_PX = Pattern.compile("^[+-]?\\d+(\\.\\d+)?px$");
    private static final Pattern PATTERN_PT = Pattern.compile("^[+-]?\\d+(\\.\\d+)?pt$");
    private static final Pattern PATTERN_PCT = Pattern.compile("^[+-]?\\d+(\\.\\d+)?%$");
    private static final NumberFormat NUMERIC_FORMAT = new DecimalFormat();
    private static final DecimalFormat PX_FORMAT = new DecimalFormat();
    private static final DecimalFormat PT_FORMAT = new DecimalFormat();
    private static final NumberFormat PCT_FORMAT = NumberFormat.getPercentInstance();

    static {
        PX_FORMAT.setPositiveSuffix("px");
        PX_FORMAT.setNegativeSuffix("px");
        PT_FORMAT.setPositiveSuffix("pt");
        PT_FORMAT.setNegativeSuffix("pt");
    }

    private HtmlDimensions() {
    }

    public static Double decode(String size) {

        // TODO - handle px,ex,pt enc suffixes.
        double d = 0;

        try {
            if (size != null) {
                if (PATTERN_NUMERIC.matcher(size).matches()) {
                    synchronized (NUMERIC_FORMAT) {
                        d = NUMERIC_FORMAT.parse(size).doubleValue();
                    }
                } else if (PATTERN_PX.matcher(size).matches()) {
                    synchronized (PX_FORMAT) {
                        d = PX_FORMAT.parse(size).doubleValue();
                    }
                } else if (PATTERN_PT.matcher(size).matches()) {
                    synchronized (PT_FORMAT) {
                        d = PT_FORMAT.parse(size).doubleValue() * (4/3.0);
                    }
                } else if (PATTERN_PCT.matcher(size).matches()) {
                    synchronized (PCT_FORMAT) {
                        d = PCT_FORMAT.parse(size).doubleValue();
                    }
                }
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException(Messages.getMessage(Messages.DECODE_PARAMETER_ERROR, new Object[] { "size",
                    size, e.getMessage() }));
        }

        return new Double(d);
    }

    public static String formatPx(Double value) {
        return value.intValue() + "px";
    }

    public static String formatPct(Double value) {
        String v = "";

        synchronized (PCT_FORMAT) {
            v = PCT_FORMAT.format(value.doubleValue());
        }

        return v;
    }

    public static String formatSize(String size) {
        if (size != null) {
            String incomingSize = size.trim();
            if (incomingSize.length() > 0) {
                char lastChar = incomingSize.charAt(incomingSize.length() - 1);
                if (Character.isDigit(lastChar)) {
                    return incomingSize + "px";
                }
            }
        }
        return size;
    }
}
