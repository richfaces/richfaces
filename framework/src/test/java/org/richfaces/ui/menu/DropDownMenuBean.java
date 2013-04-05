package org.richfaces.ui.menu;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("ddmBean")
@RequestScoped
public class DropDownMenuBean {

    private String current = "none";

    public void doAction() {
        this.current = "action";
    }

    public String getCurrent() {
        return current;
    }
}
