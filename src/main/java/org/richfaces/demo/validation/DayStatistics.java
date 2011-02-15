/**
 * 
 */
package org.richfaces.demo.validation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Ilya Shaikovsky
 * 
 */
@ManagedBean
@RequestScoped
public class DayStatistics {

    private List<PassTime> dayPasstimes = new ArrayList<PassTime>();

    public DayStatistics() {
        dayPasstimes.add(new PassTime("Sport", 0));
        dayPasstimes.add(new PassTime("Entertainment", 0));
        dayPasstimes.add(new PassTime("Sleeping", 0));
        dayPasstimes.add(new PassTime("Games", 0));
    }

    public List<PassTime> getDayPasstimes() {
        return dayPasstimes;
    }

    public void setDayPasstimes(List<PassTime> dayPasstimes) {
        this.dayPasstimes = dayPasstimes;
    }

    @NotNull
    @Min(value = 1, message = "Please fill at list one entry")
    @Max(value = 24, message = "Only 24h in a day!")
    public Integer getTotalTime() {
        Integer result = new Integer(0);
        for (PassTime passtime : dayPasstimes) {
            result += passtime.getTime();
        }
        return result;
    }

    public void store(ActionEvent event) {
        FacesContext.getCurrentInstance().addMessage(
            event.getComponent().getClientId(FacesContext.getCurrentInstance()),
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Changes Stored Successfully", "Changes Stored Successfully"));
    }
}
