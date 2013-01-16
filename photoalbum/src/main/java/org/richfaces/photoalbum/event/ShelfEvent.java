package org.richfaces.photoalbum.event;

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

    public ShelfEvent(Shelf shelf) {
        this.shelf = shelf;
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
}
