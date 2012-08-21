package org.richfaces.photoalbum.event;

/**
 * Simple event to be used in general situations, it may or may not carry a message
 * 
 * @author mpetrov
 * 
 */
public class SimpleEvent {
    private String message;

    public SimpleEvent() {

    }

    public SimpleEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
