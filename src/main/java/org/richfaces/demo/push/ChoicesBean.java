package org.richfaces.demo.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.ajax4jsf.event.PushEventListener;
import org.richfaces.demo.common.data.RandomHelper;

@ManagedBean(name = "choicesBean")
@ViewScoped
public class ChoicesBean implements Serializable{
    PushEventListener listener;

    private boolean enabled = true;
    private List<Choice> choices;
    private List<Choice> lastVotes;
    private String updateInfo;

    public ChoicesBean() {
        choices = new ArrayList<Choice>();
        lastVotes = new ArrayList<Choice>();
        choices.add(new Choice("Orange"));
        choices.add(new Choice("Pineapple"));
        choices.add(new Choice("Banana"));
        choices.add(new Choice("Kiwifruit"));
        choices.add(new Choice("Apple"));
        lastVotes.add(new Choice("Orange"));
        lastVotes.add(new Choice("Pineapple"));
        lastVotes.add(new Choice("Banana"));
        lastVotes.add(new Choice("Kiwifruit"));
        lastVotes.add(new Choice("Apple"));
    }

    public void initiateEvent() {
        for (Choice choice : lastVotes) {
            choice.setVotesCount(RandomHelper.rand(0, 3));
        }
        listener.onEvent(new EventObject(this));
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public void addListener(EventListener listener) {
        if (this.listener != listener) {
            this.listener = (PushEventListener) listener;
        }
    }

    public Date getTimeStamp() {
        return new Date();
    }

    public synchronized void start() {
        setEnabled(true);
    }

    public synchronized void stop() {
        setEnabled(false);
    }

    public void processUpdates() {
        for (Choice choice : lastVotes) {
            if (choice.getVotesCount() > 0) {
                int index = lastVotes.indexOf(choice);

                choices.get(index).increment(choice.getVotesCount());
            }
        }
        updateInfo = "[ ";
        for (Choice choice : lastVotes) {
            updateInfo += choice.getVotesCount() + " ";
        }
        updateInfo += "] ";
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public List<Choice> getLastVotes() {
        return lastVotes;
    }
}
