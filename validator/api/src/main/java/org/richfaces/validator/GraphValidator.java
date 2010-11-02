/**
 * 
 */
package org.richfaces.validator;

import java.util.Collection;
import java.util.Set;

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

    public Collection<String> validateGraph(FacesContext context, UIComponent component, Object value,
        Set<String> profiles) throws ValidatorException;

}
