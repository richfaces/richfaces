/**
 * 
 */
package org.richfaces.demo.progressBar;

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
public class ProgressBarBean implements Serializable {

    private static final long serialVersionUID = -314414475508376585L;

    private boolean buttonRendered = true;
    private boolean enabled = false;
    private Long startTime;

    public String startProcess() {
        setEnabled(true);
        setButtonRendered(false);
        setStartTime(new Date().getTime());
        return null;
    }

    public Long getCurrentValue() {
        if (isEnabled()) {
            Long current = (new Date().getTime() - startTime) / 1000;
            if (current >= 100) {
                setButtonRendered(true);
            } else if (current.equals(0)) {
                return new Long(1);
            }
            return (new Date().getTime() - startTime) / 1000;
        }
        if (startTime == null) {
            return Long.valueOf(-1);
        } else {
            return Long.valueOf(100);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public boolean isButtonRendered() {
        return buttonRendered;
    }

    public void setButtonRendered(boolean buttonRendered) {
        this.buttonRendered = buttonRendered;
    }
}
