package org.richfaces.demo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "skinBean")
@SessionScoped
public class SkinBean {
    private String skin = "classic";

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }
}
