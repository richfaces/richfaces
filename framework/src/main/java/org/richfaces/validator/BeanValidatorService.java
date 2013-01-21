package org.richfaces.validator;

import java.util.Collection;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_4_0">
 * This interface describes service that gets Bean Validator constrains for EL-expressions
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface BeanValidatorService {
    /**
     * <p class="changed_added_4_0">
     * Get all constrains for given EL-expression
     * </p>
     *
     * @param context
     * @param expression
     * @param message TODO
     */
    Collection<ValidatorDescriptor> getConstrains(FacesContext context, ValueExpression expression, String message,
        Class<?>... groups);

    /**
     * <p class="changed_added_4_0">
     * Validate expression for a new value.
     * </p>
     *
     * @param context
     * @param expression
     * @param newValue
     * @param groups
     */
    Collection<String> validateExpression(FacesContext context, ValueExpression expression, Object newValue, Class<?>... groups);

    /**
     * <p class="changed_added_4_0">
     * Validate entire object.
     * </p>
     *
     * @param context
     * @param object
     * @param groups
     */
    Collection<String> validateObject(FacesContext context, Object object, Class<?>... groups);
}
