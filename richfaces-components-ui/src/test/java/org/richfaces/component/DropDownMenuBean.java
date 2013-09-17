package org.richfaces.component;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class DropDownMenuBean {
    private static String _current = "none";

    public void doAction() {
        _current = "action";
    }

    public static String getCurrent() {
        return _current;
    }

    public static void setCurrent(String current) {
        _current = current;
    }
}
