/**
 *
 */
package org.richfaces.example;

import javax.validation.constraints.Pattern;

/**
 * @author asmirnov
 *
 */
public class PatternBean extends Validable<String> {
    private String value;

    /**
     * @return the text
     */
    @Pattern(regexp = "[a-z].*")
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
        return "Text Value, Pattern '[a-Z].*' Validation";
    }

    public String getLabel() {
        return "pattern";
    }
}
