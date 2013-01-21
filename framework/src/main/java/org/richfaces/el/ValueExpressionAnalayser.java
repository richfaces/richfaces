/**
 *
 */
package org.richfaces.el;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * Implementation of this interface extract information about bean property from EL-expression.
 *
 * @author asmirnov
 *
 */
public interface ValueExpressionAnalayser {
    ValueDescriptor getPropertyDescriptor(FacesContext context, ValueExpression expression) throws ELException;

    ValueDescriptor updateValueAndGetPropertyDescriptor(FacesContext context, ValueExpression expression, Object newValue)
        throws ELException;
}
