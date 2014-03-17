package org.richfaces.view.facelets.html;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;

import org.richfaces.component.AbstractGraphValidator;

@SuppressWarnings("serial")
public class SetupValidatorsParameter implements Serializable {
    private final AbstractGraphValidator graphValidator;
    private final Class<? extends Validator> defaultValidatorClass;
    private final Class<?>[] groups;

    public SetupValidatorsParameter(AbstractGraphValidator graphValidator, Class<? extends Validator> defaultValidatorClass, Class<?>[] groups) {
        this.graphValidator = graphValidator;
        this.defaultValidatorClass = defaultValidatorClass;
        this.groups = groups;
    }

    public Validator getValidator() {
        return this.graphValidator.createChildrenValidator();
    }

    public Class<? extends Validator> getDefaultValidatorClass() {
        return this.defaultValidatorClass;
    }

    public Class<?>[] getGroups() {
        return this.groups;
    }
}