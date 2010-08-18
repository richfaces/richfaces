package org.richfaces;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.Serializable;

public class TogglePanelBean implements Serializable {

    private static final long serialVersionUID = -2403138958014741653L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TogglePanelBean.class);
    
    private String name;

    private String value = "name2";

    public TogglePanelBean() {
        LOGGER.info("post construct: initialize");
        name = "John";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        System.out.println("value = " + value);
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void itemChangeActionListener() {
        LOGGER.info("TogglePanelBean.itemChangeActionListener");
    }
}
