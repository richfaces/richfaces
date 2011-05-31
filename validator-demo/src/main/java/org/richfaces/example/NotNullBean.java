/**
 *
 */
package org.richfaces.example;

import javax.validation.constraints.NotNull;

/**
 * @author asmirnov
 *
 */
public class NotNullBean extends Validable<String> {
    @NotNull
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
        return "Text Value, Not Null Validation";
    }

    public String getLabel() {
        return "notNull";
    }
}
