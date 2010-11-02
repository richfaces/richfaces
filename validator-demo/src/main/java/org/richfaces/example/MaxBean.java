/**
 * 
 */
package org.richfaces.example;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

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
//	@CreditCardNumber
	@Pattern(regexp="\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d")
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
		return "Text value, should be correct credit card number";
	}

	public String getIntDescription() {
		// TODO Auto-generated method stub
		return "Integer Value, less then 10";
	}

	public String getIntSummary() {
		// TODO Auto-generated method stub
		return "Invalid number of items";
	}

	public String getTextSummary() {
		// TODO Auto-generated method stub
		return "Invalid payment card";
	}

}
