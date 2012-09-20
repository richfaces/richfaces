package org.richfaces;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@ViewScoped
@ManagedBean
public class DynamicTabBean implements Serializable {

    private static final long serialVersionUID = -2403138958014741653L;
    private String name;
    private List<String> names;

    public DynamicTabBean() {
        System.out.println("post construct: initialize");
        name = "John";
        names = Arrays.asList(new String[] {"Dynamic Tab 1", "Dynamic Tab 2"});
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNames() {
        return names;
    }
}
