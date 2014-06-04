package org.richfaces.photoalbum.model.event;

/**
 * 
 * Event to be used for displaying error notification
 * 
 * @author mpetrov
 *
 */

public class ErrorEvent extends SimpleEvent {
    private String summary;

    public ErrorEvent(String summary, String message) {
        super(message);
        this.summary = summary;
    }
    
    public ErrorEvent(String message) {
        this("", message);
    }

    public String getSummary() {
        return summary;
    }

    public String getDetail() {
        return super.getMessage();
    }
    
}
