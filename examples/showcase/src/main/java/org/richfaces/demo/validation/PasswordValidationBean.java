package org.richfaces.demo.validation;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

@ManagedBean
@SessionScoped
public class PasswordValidationBean implements Cloneable, Serializable {
    private static final long serialVersionUID = 1952428504080910113L;
    @Size(min = 5, max = 15, message = "Password length must be between {min} and {max} characters.")
    private String password = "";
    private String confirm = "";

    @AssertTrue(message = "Different passwords entered!")
    public boolean isPasswordsEquals() {
        return password.equals(confirm);
    }

    public void storeNewPassword() {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully changed!", "Successfully changed!"));
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm() {
        return confirm;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
