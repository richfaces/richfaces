package org.richfaces.component.validation;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@ManagedBean
@ViewScoped
public class GraphValidatorBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @NotEmpty
    private String inputText = "";
    private Class<?>[] validationGroups = new Class[] { AlwaysPassingGroup.class };

    public String getInputText() {
        return inputText;
    }

    public Class<?>[] getValidationGroups() {
        return validationGroups;
    }

    @AssertTrue(message = "group-failure", groups = { AlwaysPassingGroup.class })
    public boolean isGroupValid() {
        return Boolean.TRUE;
    }

    public void setAlwaysPassingValidationGroup() {
        setValidationGroups(new Class<?>[] { AlwaysPassingGroup.class });
    }

    public void setEmptyValidationGroup() {
        setValidationGroups(new Class<?>[] {});
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public void setValidationGroups(Class<?>[] validationGroups) {
        this.validationGroups = validationGroups;
    }

    public interface AlwaysPassingGroup {
    }
}
