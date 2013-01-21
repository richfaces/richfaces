package org.richfaces;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "rf11474")
@ViewScoped
public class RF11474 {

    private String activeTab;

    public String getActiveTab() {
        System.out.println("getActiveTab() == " + activeTab);
        return activeTab;
    }

    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
        System.out.println("setActiveTab(" + activeTab + ")");
    }

    public void goToTab1() {
        System.out.println("goToTab1");
        activeTab = "tab1";
    }

    public void goToTab2() {
        System.out.println("goToTab2");
        activeTab = "tab2";
    }

    public void goToTab3() {
        System.out.println("goToTab3");
        activeTab = "tab3";
    }
}
