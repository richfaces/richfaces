/**
 *
 */
package org.richfaces.example;

import javax.validation.constraints.Size;

/**
 * @author asmirnov
 *
 */
public class SizeBean extends Validable<String> {
    @Size(max = 10, min = 2, message = "incorrect field length")
    private String value;

    /**
     * @return the text
     */
    public String getValue() {
        return value;
    }

    /**
     * @param text the text to set
     */
    public void setValue(String text) {
        this.value = text;
    }

    public String getDescription() {
        return "Validate String Length, for a range 2-10 chars";
    }

    public String getLabel() {
        return "size";
    }
}
