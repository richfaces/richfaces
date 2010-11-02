/**
 * 
 */
package org.richfaces.example;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author asmirnov
 *
 */
public class MinMaxBean implements Validable {
	
	private String text;
	
	@Min(value=2,message="Value {0} should be more than {value}")
	@Max(10)
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
		return "Text Value, no restrictions";
	}

	public String getIntDescription() {
		// TODO Auto-generated method stub
		return "Integer Value, valid values from 2 to 10";
	}

	public String getIntSummary() {
		// TODO Auto-generated method stub
		return "Invalid price";
	}

	public String getTextSummary() {
		// TODO Auto-generated method stub
		return null;
	}

}
