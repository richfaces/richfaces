/**
 * 
 */
package org.richfaces.example;

/**
 * @author asmirnov
 *
 */
public interface Validable<T> {
	
	T getValue();
	
	void setValue(T value);
    
	String getDescription();
	
	String getLabel();
}
