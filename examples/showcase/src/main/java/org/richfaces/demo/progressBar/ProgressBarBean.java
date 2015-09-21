package org.richfaces.demo.progressBar;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * @author Ilya Shaikovsky
 */
@ManagedBean
@ViewScoped
public class ProgressBarBean implements Serializable {
    private static final long serialVersionUID = -314414475508376585L;
    private boolean buttonRendered = true;
    private boolean enabled = false;
    private int currentValue;

    public String startProcess() {
        setEnabled(true);
        setButtonRendered(false);
        setCurrentValue(0);
        return null;
    }

    public void increment() {
        if (isEnabled() && currentValue < 100) {
            currentValue += 2;
            if (currentValue >= 100) {
                setButtonRendered(true);
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isButtonRendered() {
        return buttonRendered;
    }

    public void setButtonRendered(boolean buttonRendered) {
        this.buttonRendered = buttonRendered;
    }

    public int getCurrentValue() {
        if (!isEnabled()) {
            return -1;
        }
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

}
