package org.richfaces.demo.editor;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class EditorBean {

    @ManagedProperty(value = "#{fairyTailBean}")
    private FairyTailBean fairyTailBean;

    private String toolbar = "basic";
    private String skin = "richfaces";
    private String language = "fr";
    private String value = null;

    public void setFairyTailBean(FairyTailBean fairyTailBean) {
        this.fairyTailBean = fairyTailBean;
    }

    public String getToolbar() {
        return toolbar;
    }

    public void setToolbar(String toolbar) {
        this.toolbar = toolbar;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getValue() {
        if (value == null) {
            return fairyTailBean.getFairyTail();
        }
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
