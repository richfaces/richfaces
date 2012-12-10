package org.richfaces.photoalbum.event;

import org.richfaces.photoalbum.domain.Album;
/**
 * Album event, carries an album and its (relative) path.
 * Temporary solution before I figure out how to inject the properties.
 * 
 * @author mpetrov
 *
 */
public class AlbumEvent {
    private Album album;
    private String path;
    
    public AlbumEvent(Album album) {
        this.album = album;
    }
    
    public AlbumEvent(Album album, String path) {
        this.album = album;
        this.path = path;
    }
    
    public Album getAlbum() {
        return album;
    }
    
    public String getPath() {
        return path;
    }
}
