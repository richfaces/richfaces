/**
 * 
 */
package org.richfaces.example;

import javax.validation.constraints.Pattern;


/**
 * @author asmirnov
 *
 */
public class PatternBean implements Validable<String> {
	
	private String value;
	
	/**
	 * @return the text
	 */
	@Pattern(regexp="^[a-Z][a-Z1-9_]$")
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
		return "Text Value, Pattern '^[a-Z][a-Z1-9_]$' Validation";
	}
    public String getLabel() {
        return "pattern";
    }

}
