package org.richfaces.demo;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "editor")
public class EditorBean {
    
    private String value = "Editor Initial Value";
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
}
