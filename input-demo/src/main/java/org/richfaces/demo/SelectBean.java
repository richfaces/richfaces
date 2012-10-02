package org.richfaces.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@ViewScoped
public class SelectBean implements Serializable {

    private List<SelectItem> values;
    private String value;

    @PostConstruct
    public void init() {
        values = new ArrayList<SelectItem>();
        for (String s : new String[] { "a", "b", "c", "d" }) {
            values.add(new SelectItem(s));
        }
    }

    public List<SelectItem> getValues() {
        return values;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
