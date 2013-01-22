/**
 *
 */
package org.richfaces;

import java.io.Serializable;
import java.text.DateFormat;
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
    private static final long serialVersionUID = -446286889238296278L;
    private int minValue = 0;
    private int maxValue = 100;
    private int value = 50;
    private String label = "'label' attribute";
    private boolean childrenRendered = false;
    private boolean enabled = false;
    private boolean initialFacetRendered = true;
    private boolean finishFacetRendered = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isChildrenRendered() {
        return childrenRendered;
    }

    public void setChildrenRendered(boolean childrenRendered) {
        this.childrenRendered = childrenRendered;
    }

    public boolean isInitialFacetRendered() {
        return initialFacetRendered;
    }

    public void setInitialFacetRendered(boolean renderInitialFacet) {
        this.initialFacetRendered = renderInitialFacet;
    }

    public boolean isFinishFacetRendered() {
        return finishFacetRendered;
    }

    public void setFinishFacetRendered(boolean renderFinishFacet) {
        this.finishFacetRendered = renderFinishFacet;
    }

    public void decreaseValueByFive() {
        value -= 5;
    }

    public void increaseValueByFive() {
        value += 5;
    }

    public String getCurrentTimeAsString() {
        return DateFormat.getTimeInstance().format(new Date());
    }
}