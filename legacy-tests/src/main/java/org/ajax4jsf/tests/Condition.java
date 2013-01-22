
/**
 *
 */
package org.ajax4jsf.tests;

/**
 * @author Administrator
 *
 */
public class Condition {
    private boolean conditionTrue;
    private String message;

    public Condition(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isConditionTrue() {
        return conditionTrue;
    }

    public void setTrue() {
        setCondition(true);
    }

    public void setFalse() {
        setCondition(false);
    }

    public void setCondition(boolean conditionTrue) {
        this.conditionTrue = conditionTrue;
    }
}
