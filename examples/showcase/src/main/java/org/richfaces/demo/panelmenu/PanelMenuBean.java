package org.richfaces.demo.panelmenu;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.event.ItemChangeEvent;

@ManagedBean
@ViewScoped
public class PanelMenuBean {
    private String current;
    private boolean singleMode;

    public boolean isSingleMode() {
        return singleMode;
    }

    public void setSingleMode(boolean singleMode) {
        this.singleMode = singleMode;
    }

    public String getCurrent() {
        return this.current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public void updateCurrent(ItemChangeEvent event) {
        setCurrent(event.getNewItemName());
    }
}
