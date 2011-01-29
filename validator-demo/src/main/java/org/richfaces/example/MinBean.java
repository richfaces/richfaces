/**
 * 
 */
package org.richfaces.example;

import javax.validation.constraints.Min;


/**
 * @author asmirnov
 *
 */
public class MinBean implements Validable {
	
	private String text;
	
	@Min(2)
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
		return "Text value, no restrictions";
	}

	public String getIntDescription() {
		// TODO Auto-generated method stub
		return "Integer Value, more then 1";
	}

	public String getIntSummary() {
		// TODO Auto-generated method stub
		return "Invalid rooms qty";
	}

	public String getTextSummary() {
		// TODO Auto-generated method stub
		return null;
	}

}
