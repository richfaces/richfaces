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
public class MaxBean implements Validable<Integer> {
	
	@Max(10)
	private Integer value = 0;

	/**
	 * @return the intValue
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * @param intValue the intValue to set
	 */
	public void setValue(Integer intValue) {
		this.value = intValue;
	}


	public String getDescription() {
		return "Integer Value, less then 10";
	}

    public String getLabel() {
        return "max";
    }

}
