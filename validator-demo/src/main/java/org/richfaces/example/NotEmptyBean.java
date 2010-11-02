/**
 * 
 */
package org.richfaces.example;

import javax.validation.constraints.Size;


/**
 * @author asmirnov
 *
 */
public class NotEmptyBean implements Validable {
	
	@Size(min=1)
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
		return "Text value, Not Empty Validation";
	}

	public String getIntDescription() {
		// TODO Auto-generated method stub
		return "Integer Value, no restrictions";
	}

	public String getIntSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTextSummary() {
		// TODO Auto-generated method stub
		return "Invalid password";
	}

}
