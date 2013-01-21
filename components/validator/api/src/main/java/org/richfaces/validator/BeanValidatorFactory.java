/**
 *
 */
package org.richfaces.validator;

import java.lang.annotation.Annotation;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;

/**
 * @author asmirnov
 *
 */
public interface BeanValidatorFactory {
    Validator getValidator(FacesContext context);

    FacesMessage interpolateMessage(FacesContext context, ConstraintDescriptor<? extends Annotation> constrain);
}
