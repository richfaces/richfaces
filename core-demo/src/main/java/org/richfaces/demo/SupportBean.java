package org.richfaces.demo;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

@SessionScoped
@ManagedBean(name = "supportBean")
public class SupportBean implements Serializable {
    
    private static final long serialVersionUID = -110973149750159911L;

    private String text1;
    private String text2;

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public void behaviorListener(AjaxBehaviorEvent event) {
        System.out.println("SupportBean.behaviorListener()");
    }
}
