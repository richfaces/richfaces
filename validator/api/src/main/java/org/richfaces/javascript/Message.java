/**
 * 
 */
package org.richfaces.javascript;

import javax.faces.application.FacesMessage;

/**
 * @author asmirnov
 *
 */
public final class Message {

    private final int severity;
    private final String detail;
    private final String summary;

    public Message(int severity,String detail,String summary) {
        this.severity = severity;
        this.detail = detail;
        this.summary = summary;
    }
    public Message(FacesMessage msg) {
        this.severity = msg.getSeverity().getOrdinal();
        this.summary = msg.getSummary();
        this.detail = msg.getDetail();
    }
    /**
     * @return the severity
     */
    public int getSeverity() {
        return severity;
    }
    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }
    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

}
