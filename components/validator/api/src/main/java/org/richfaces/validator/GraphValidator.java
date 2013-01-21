/**
 *
 */
package org.richfaces.validator;

import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 * This interface should be implemented by the JSF {@link Validator} which able to validate entire graph.
 *
 * @author asmirnov
 *
 */
public interface GraphValidator {
    Collection<String> validateGraph(FacesContext context, UIComponent component, Object value, Class<?>[] groups)
        throws ValidatorException;
}
