package org.richfaces.photoalbum.event;

import org.richfaces.photoalbum.domain.Image;
/**
 * Image event, carries an image and its (relative) path.
 * Temporary solution.
 * 
 * @author mpetrov
 *
 */
public class ImageEvent {
    private Image image;
    private String path;
    
    public ImageEvent(Image image) {
        this.image = image;
    }
    
    public ImageEvent(Image image, String path) {
        this.image = image;
        this.path = path;
    }
    
    public Image getImage() {
        return image;
    }
    
    public String getPath() {
        return path;
    }
}