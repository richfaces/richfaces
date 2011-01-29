/**
 * 
 */
package org.richfaces.example;

import javax.validation.constraints.Max;

import org.hibernate.validator.constraints.Email;


/**
 * @author asmirnov
 *
 */
public class MaxBean implements Validable {
	
	private String text;
	
	@Max(10)
	private int intValue;

	/**
	 * @return the text
	 */
	@Email
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
		return "Text value, should be correct email address";
	}

	public String getIntDescription() {
		return "Integer Value, less then 10";
	}

	public String getIntSummary() {
		return "Invalid number of items";
	}

	public String getTextSummary() {
		return "Invalid payment card";
	}

}
