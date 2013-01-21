/**
 *
 */
package org.richfaces.example;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author asmirnov
 *
 */
public class NotEmptyBean extends Validable<String> {
    @NotEmpty
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
        return "Text value, Not Empty Validation";
    }

    public String getLabel() {
        return "notEmpty";
    }
}
