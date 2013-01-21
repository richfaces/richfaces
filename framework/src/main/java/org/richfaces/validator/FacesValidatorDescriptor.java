package org.richfaces.validator;

import javax.faces.application.FacesMessage;
import javax.faces.validator.Validator;

public class FacesValidatorDescriptor extends BaseFacesObjectDescriptor<Validator> implements ValidatorDescriptor {
    public FacesValidatorDescriptor(Class<? extends Validator> validatorClass, FacesMessage message) {
        super(validatorClass, message);
    }
}
