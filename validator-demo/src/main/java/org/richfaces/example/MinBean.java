/**
 *
 */
package org.richfaces.example;

import javax.validation.constraints.Min;

/**
 * @author asmirnov
 *
 */
public class MinBean extends Validable<Integer> {
    @Min(2)
    private Integer value = 2;

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
        return "Integer Value, more then 2";
    }

    public String getLabel() {
        return "min";
    }
}
