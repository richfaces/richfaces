package org.richfaces.component.validation;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Named
@SessionScoped
public class GraphValidatorBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @NotEmpty
    private String inputText = "";
    private boolean selectBooleanCheckbox = true;
    private Class<?>[] validationGroups = new Class[] { MethodValidationGroup.class };
    private boolean groupValid = true;
    private boolean actionInvoked = false;

    @AssertTrue(message = "group-failure", groups = { MethodValidationGroup.class })
    public boolean isGroupValid() {
        return groupValid;
    }

    public void action() {
        actionInvoked = true;
    }

    public interface MethodValidationGroup {
    }

    public interface FieldValidationGroup {
    }

    // getters / setters

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public boolean isSelectBooleanCheckbox() {
        return selectBooleanCheckbox;
    }

    public void setSelectBooleanCheckbox(boolean selectBooleanCheckbox) {
        this.selectBooleanCheckbox = selectBooleanCheckbox;
    }

    public Class<?>[] getValidationGroups() {
        return validationGroups;
    }

    public void setValidationGroups(Class<?>[] validationGroups) {
        this.validationGroups = validationGroups;
    }

    public boolean isActionInvoked() {
        return actionInvoked;
    }
}
