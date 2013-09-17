/**
 *
 */
package org.richfaces.javascript;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.faces.application.ResourceDependency;

/**
 * This annotation describes client-side version of Converter/validator.
 *
 * @author asmirnov
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClientSideScript {
    String function();

    ResourceDependency[] resources() default {};
}
