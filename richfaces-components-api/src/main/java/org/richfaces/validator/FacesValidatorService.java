package org.richfaces.validator;

import javax.faces.component.EditableValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;

public interface FacesValidatorService {
    ValidatorDescriptor getValidatorDescription(FacesContext context, EditableValueHolder component, Validator validator,
        String message);
}
