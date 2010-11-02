package org.richfaces.validator;

import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;


public interface FacesValidatorService {

    ValidatorDescriptor getValidatorDescription(FacesContext context,Validator validator);
}
