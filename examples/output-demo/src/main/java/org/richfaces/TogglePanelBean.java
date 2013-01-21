package org.richfaces;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.event.ItemChangeEvent;
import org.richfaces.log.LogFactory;
import org.richfaces.log.Logger;

@ManagedBean
@ViewScoped
public class TogglePanelBean implements Serializable {
    private static final long serialVersionUID = -2403138958014741653L;
    private static final Logger LOGGER = LogFactory.getLogger(TogglePanelBean.class);
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
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void itemChangeActionListener() {
        LOGGER.info("TogglePanelBean.itemChangeActionListener");
    }

    public void itemChangeActionListener(ItemChangeEvent event) {
        LOGGER.info("TogglePanelBean.itemChangeActionListener(event)");
    }
}
