package org.richfaces.demo;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

@ManagedBean(name = "commandBean")
@SessionScoped
public class CommandBean implements Serializable {

    private static final long serialVersionUID = 3485896940723796437L;

    private String name;

    private boolean pollEnabled;

    public boolean isPollEnabled() {
        return pollEnabled;
    }

    public void setPollEnabled(boolean pollEnabled) {
        this.pollEnabled = pollEnabled;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void submit() {
        name = "Hello " + name;
    }

    public void reset() {
        name = "";
    }

    public Date getDate() {
        return new Date();
    }

    public void listener(AjaxBehaviorEvent event) {
        System.out.println("CommandBean.listener()");
    }

    public void enablePoll(ActionEvent event) {
        setPollEnabled(true);
    }

    public void disablePoll(ActionEvent event) {
        setPollEnabled(false);
    }
}
