/**
 *
 */
package org.richfaces.validator;

import java.util.Collection;
import java.util.Collections;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * @author asmirnov
 *
 */
public class DummyBeanValidatorService implements BeanValidatorService {
    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.validator.BeanValidatorService#getConstrains(javax.faces.context.FacesContext,
     * javax.el.ValueExpression, java.lang.Class<?>[])
     */
    public Collection<ValidatorDescriptor> getConstrains(FacesContext context, ValueExpression expression, String message,
        Class<?>... groups) {
        return Collections.emptySet();
    }

    public Collection<String> validateExpression(FacesContext context, ValueExpression expression, Object newValue,
        Class<?>... groups) {
        return Collections.emptySet();
    }

    public Collection<String> validateObject(FacesContext context, Object object, Class<?>... groups) {
        return Collections.emptySet();
    }
}
