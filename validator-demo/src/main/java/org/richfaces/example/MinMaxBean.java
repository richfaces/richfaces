/**
 *
 */
package org.richfaces.example;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author asmirnov
 *
 */
public class MinMaxBean extends Validable<Integer> {
    @Min(2)
    @Max(10)
    private Integer value = 0;

    /**
     * @return the intValue
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @param intValue the intValue to set
     */
    public void setValue(Integer intValue) {
        this.value = intValue;
    }

    public String getDescription() {
        return "Integer Value, valid values from 2 to 10";
    }

    public String getLabel() {
        return "minMax";
    }
}
