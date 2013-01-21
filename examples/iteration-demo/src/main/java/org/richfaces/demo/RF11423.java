package org.richfaces.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "rf11423")
@SessionScoped
public class RF11423 implements Serializable {
    // ------------------------------ FIELDS ------------------------------

    private List<String> titles;

    // --------------------- GETTER / SETTER METHODS ---------------------

    public List<String> getTitles() {
        if (titles == null) {
            titles = new ArrayList<String>();
            titles.add("James Bond");
            titles.add("Johnny English");
        }
        return titles;
    }
}
