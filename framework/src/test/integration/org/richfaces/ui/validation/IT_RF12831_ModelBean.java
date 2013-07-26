package org.richfaces.ui.validation;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

@ManagedBean(name = "modelBean")
@ViewScoped
public class IT_RF12831_ModelBean {

    private IT_RF12831_Model model;

    @PostConstruct
    private void init() {
        System.out.println("Init");

        model = new IT_RF12831_Model();
        model.setField1("my value");
    }

    public IT_RF12831_Model getModel() {
        return model;
    }

    public void setModel(IT_RF12831_Model model) {
        this.model = model;
    }

    public void validateField1(FacesContext context, UIComponent toValidate, Object value) {

        System.out.println("argument value: " + value);
        System.out.println("model value: " + model.getField1());
        // my expectation is that value is equal to associated model field - validation should be performed after applying
        // request values.
        // If it is not the code will throw RuntimeException

        if (!model.getField1().equals(value)) {
            throw new RuntimeException("value passed to the validation method should be equal to the associated model field!");
        }

        // this would be a workaround to the bug
        model.setField1((String) value);

        // validation logic omitted
    }

}