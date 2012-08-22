/**
 *
 */
package org.richfaces.example;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author asmirnov
 *
 */
@ManagedBean
@RequestScoped
public class GraphValidatorBean implements Cloneable {
    @Min(0)
    @Max(10)
    private int first;
    @Min(0)
    @Max(10)
    private int second;
    @Min(0)
    @Max(10)
    private int third;
    private String actionResult;

    /**
     * @return the actionResult
     */
    public String getActionResult() {
        return actionResult;
    }

    /**
     * @param actionResult the actionResult to set
     */
    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    /**
     * @return the first
     */
    public int getFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(int first) {
        this.first = first;
    }

    /**
     * @return the second
     */
    public int getSecond() {
        return second;
    }

    /**
     * @param second the second to set
     */
    public void setSecond(int second) {
        this.second = second;
    }

    /**
     * @return the third
     */
    public int getThird() {
        return third;
    }

    /**
     * @param third the third to set
     */
    public void setThird(int third) {
        this.third = third;
    }

    /**
     * @return total summ of the list values.
     */
    @Max(value = 20, message = "Total value should be less then 20")
    public int getSumm() {
        return first + second + third;
    }

    public String action() {
        // Persist your data here
        setActionResult("Data have been saved");
        return null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
