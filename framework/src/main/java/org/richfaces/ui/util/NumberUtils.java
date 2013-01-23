package org.richfaces.ui.util;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import java.math.BigDecimal;

public final class NumberUtils {
    private static final Logger LOGGER = RichfacesLogger.COMPONENTS.getLogger();

    private NumberUtils() {

    }

    /**
     * Converts value attr to number value
     *
     * @param v - value attr
     * @return result
     */
    public static Number getNumber(Object v) {
        Number result = null;
        if (v != null) {
            try {
                if (v instanceof String) { // String
                    result = Double.parseDouble((String) v);
                } else {
                    Number n = (Number) v;
                    if ((n instanceof BigDecimal) || (n instanceof Double) // Double
                        // or
                        // BigDecimal
                        || (n instanceof Float)) {
                        result = n.floatValue();
                    } else if (n instanceof Integer || n instanceof Long) { // Integer
                        result = n.longValue();
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            return result;
        }
        return new Integer(0);
    }
}
