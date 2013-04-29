package org.richfaces.photoalbum.event;

import org.richfaces.photoalbum.domain.Event;
import org.richfaces.photoalbum.domain.Shelf;

/**
 * Shelf event, carries a shelf and its (relative) path. Temporary solution.
 * 
 * @author mpetrov
 * 
 */
public class ShelfEvent {
    private Shelf shelf;
    private String path;

    private Event event;
    
    public ShelfEvent(Shelf shelf) {
        this.shelf = shelf;
    }
    
    public ShelfEvent(Event event) {
        this.event = event;
    }

    public ShelfEvent(Shelf shelf, String path) {
        this.shelf = shelf;
        this.path = path;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public String getPath() {
        return path;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    
    
}
