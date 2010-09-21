package org.richfaces.component;

import java.math.BigDecimal;

public final class NumberUtils {
    private NumberUtils() {
    	
    }
    /**
     * Converts value attr to number value
     * 
     * @param v -
     *            value attr
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
                e.getMessage();
            }
            return result;
        }
        return new Integer(0);
    }
}
