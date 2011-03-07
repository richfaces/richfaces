/**
 * 
 */
package org.richfaces.example;

import javax.validation.constraints.AssertTrue;


/**
 * @author asmirnov
 *
 */
public class BooleanBean extends Validable<Boolean> {
	
	@AssertTrue()
	private Boolean value = false;

	/**
	 * @return the intValue
	 */
	public Boolean getValue() {
		return value;
	}

	/**
	 * @param intValue the intValue to set
	 */
	public void setValue(Boolean intValue) {
		this.value = intValue;
	}


	public String getDescription() {
		return "Boolean Value, should be true";
	}

    public String getLabel() {
        return "assertTrue";
    }

}
