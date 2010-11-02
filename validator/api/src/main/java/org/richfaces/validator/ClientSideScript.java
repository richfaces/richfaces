/**
 * 
 */
package org.richfaces.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation describes client-side version of Converter/validator.
 * @author asmirnov
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClientSideScript {
    
    String library() default "";
    
    String resource();
    
    String function();

}
