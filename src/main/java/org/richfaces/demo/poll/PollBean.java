/**
 * 
 */
package org.richfaces.demo.poll;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * @author Ilya Shaikovsky
 * 
 */
@ManagedBean
@ViewScoped
public class PollBean implements Serializable {

    private static final int POLL_DISABLEMENT_INTERVAL = 60000;
    private static final long serialVersionUID = 7871292328251171957L;
    private Date pollStartTime;
    private boolean pollEnabled;

    public PollBean() {
        pollEnabled = true;
    }

    public Date getDate() {
        Date date = new Date();
        if (null == pollStartTime) {
            pollStartTime = new Date();
            return date;
        }
        if ((date.getTime() - pollStartTime.getTime()) >= POLL_DISABLEMENT_INTERVAL) {
            setPollEnabled(false);
        }
        return date;
    }

    public boolean getPollEnabled() {
        return pollEnabled;
    }

    public void setPollEnabled(boolean pollEnabled) {
        if (pollEnabled) {
            setPollStartTime(null);
        }
        this.pollEnabled = pollEnabled;
    }

    public Date getPollStartTime() {
        return pollStartTime;
    }

    public void setPollStartTime(Date pollStartTime) {
        this.pollStartTime = pollStartTime;
    }

}
