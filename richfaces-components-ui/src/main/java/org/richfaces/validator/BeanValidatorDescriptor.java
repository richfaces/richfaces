package org.richfaces.validator;

import java.lang.annotation.Annotation;

import javax.faces.application.FacesMessage;

public class BeanValidatorDescriptor extends BaseFacesObjectDescriptor<Annotation> implements ValidatorDescriptor {
    public BeanValidatorDescriptor(Class<? extends Annotation> validatorClass, FacesMessage message) {
        super(validatorClass, message);
    }
}
