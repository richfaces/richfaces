package org.richfaces.photoalbum.event;

/**
 * 
 * Event to be used for displaying error notification
 * 
 * @author mpetrov
 *
 */

public class ErrorEvent {
    private String summary;
    private String detail;

    public ErrorEvent(String heading, String message) {
        this.summary = heading;
        this.detail = message;
    }
    
    public ErrorEvent(String message) {
        new ErrorEvent("", message);
    }

    public String getSummary() {
        return summary;
    }

    public String getDetail() {
        return detail;
    }
    
}
