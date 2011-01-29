/**
 * 
 */
package org.richfaces.example;

import org.hibernate.validator.constraints.Length;


/**
 * @author asmirnov
 *
 */
public class LengthBean implements Validable {
	
	@Length(max=10,min=2,message="incorrect field length")
	private String text;
	
	private int intValue;

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the intValue
	 */
	public int getIntValue() {
		return intValue;
	}

	/**
	 * @param intValue the intValue to set
	 */
	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public String getTextDescription() {
		return "Validate String Length, for a range 2-10 chars";
	}

	public String getIntDescription() {
		return "Integer Value, no restrictions";
	}

	public String getIntSummary() {
		return "Invalid user name";
	}

	public String getTextSummary() {
		return "Invalid user name";
	}

}
