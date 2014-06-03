/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.photoalbum.manager;

/**
 * Class encapsulated all functionality, related to working with slideshow.
 *
 * @author Andrey Markhel
 */
import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.json.JSONObject;
import org.richfaces.photoalbum.model.Image;
import org.richfaces.photoalbum.model.event.ErrorEvent;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.SimpleEvent;
import org.richfaces.photoalbum.social.facebook.FacebookAlbumCache;
import org.richfaces.photoalbum.social.gplus.GooglePlusAlbumCache;
import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.ImageHandler;
import org.richfaces.photoalbum.util.ApplicationUtils;

@ApplicationScoped
@Named("slideshow")
public class SlideshowManager implements Serializable {

    private static final long serialVersionUID = 7801877176558409702L;

    private Integer slideshowIndex;

    private Integer startSlideshowIndex;

    private ImageHandler selectedImage;

    private boolean active;

    private boolean errorDetected;

    @Inject
    Model model;

    @Inject
    FileManager fileManager;

    @Inject
    GooglePlusAlbumCache gpac;

    @Inject
    FacebookAlbumCache fac;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    Event<ErrorEvent> error;

    private int interval = Constants.INITIAL_DELAY;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * This method invoked after user click on 'Start slideshow' button and no image is selected. After execution of this method
     * slideshow will be activated.
     * 
     */
    public void startSlideshow() {
        if (!this.active) {
            initSlideshow();
        }
        if (this.selectedImage == null && (model.getImages() == null || model.getImages().size() < 1)) {
            onError(true);
            return;
        }
        if (this.selectedImage == null) {
            this.selectedImage = new ImageHandler(model.getImages().get(this.slideshowIndex));
        }
        // mark image as 'visited'
        if (this.selectedImage.getType() == ImageHandler.LOCAL) {
            ((Image) this.selectedImage.getImage()).setVisited(true);
        }
        // Check if that image was recently deleted. If yes, immediately stop slideshow process
        checkIsFileRecentlyDeleted();
    }

    /**
     * This method invoked after user click on 'Start slideshow' button. After execution of this method slideshow will be
     * activated starting from selected image.
     * 
     * @param selectedImage - first image to show during slideshow
     */
    public void startSlideshow(Image selectedImage) {
        initSlideshow();
        this.slideshowIndex = model.getImages().indexOf(selectedImage);
        this.startSlideshowIndex = this.slideshowIndex;
        this.selectedImage = new ImageHandler(selectedImage);

        startSlideshow();
    }
    
    public void startSlideshow(JSONObject remoteImage) {
        initSlideshow();
        this.selectedImage = new ImageHandler(remoteImage);
        switch(selectedImage.getType()) {
            case ImageHandler.FACEBOOK:
                this.slideshowIndex = fac.getCurrentImages().indexOf(remoteImage);
                break;
            case ImageHandler.GOOGLE:
                this.slideshowIndex = gpac.getCurrentImages().indexOf(remoteImage);
        }
        this.startSlideshowIndex = this.slideshowIndex;

        startSlideshow();
    }
    
    public void startSlideshowRemote(int kind) {
        this.slideshowIndex = this.startSlideshowIndex = 0;
        switch(kind) {
            case ImageHandler.FACEBOOK:
                this.selectedImage = new ImageHandler(fac.getCurrentImages().get(0));
                break;
            case ImageHandler.GOOGLE:
                this.selectedImage = new ImageHandler(gpac.getCurrentImages().get(0));
        }
        
        startSlideshow();
    }

    /**
     * This method invoked after user click on 'Stop slideshow' button. After execution of this method slideshow will be
     * de-activated.
     * 
     */
    public void stopSlideshow(@Observes @EventType(Events.STOP_SLIDESHOW_EVENT) SimpleEvent se) {
        active = false;
        errorDetected = false;
        this.selectedImage = null;
        this.slideshowIndex = 0;
        this.startSlideshowIndex = 0;
    }

    public void stopSlideshow() {
        stopSlideshow(new SimpleEvent());
    }

    public Integer getSlideshowIndex() {
        return slideshowIndex;
    }

    public void setSlideshowIndex(Integer slideshowIndex) {
        this.slideshowIndex = slideshowIndex;
    }

    public ImageHandler getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Image selectedImage) {
        this.selectedImage.setImage(selectedImage);
    }

    /**
     * This method used to prepare next image to show during slideshow
     * 
     */
    public void showNextImage() {
        if (!active) {
            onError(false);
            return;
        }
        // reset index if we reached last image
        if (isLastImage()) {
            slideshowIndex = -1;
        }
        slideshowIndex++;
        // To prevent slideshow mechanism working in cycle.
        if (slideshowIndex == startSlideshowIndex) {
            onError(false);
            return;
        }
        setNextImage();
        // Check if that image was recently deleted. If yes, stopping slideshow
        checkIsFileRecentlyDeleted();
    }
    
    private boolean isLastImage() {
        switch(selectedImage.getType()) {
            case ImageHandler.LOCAL: 
                return slideshowIndex == model.getImages().size() - 1;
            case ImageHandler.FACEBOOK:
                return slideshowIndex == fac.getCurrentImages().size() - 1;
            case ImageHandler.GOOGLE:
                return slideshowIndex == gpac.getCurrentImages().size() - 1;
            default:
                return true;
        }
    }
    
    private void setNextImage() {
        switch(selectedImage.getType()) {
            case ImageHandler.LOCAL: 
                selectedImage.setImage(model.getImages().get(slideshowIndex));
                ((Image) this.selectedImage.getImage()).setVisited(true);
                break;
            case ImageHandler.FACEBOOK:
                selectedImage.setImage(fac.getCurrentImages().get(slideshowIndex));
                break;
            case ImageHandler.GOOGLE:
                selectedImage.setImage(gpac.getCurrentImages().get(slideshowIndex));
                break;
        }
    }

    public Integer getStartSlideshowIndex() {
        return startSlideshowIndex;
    }

    public void setStartSlideshowIndex(Integer startSlideshowIndex) {
        this.startSlideshowIndex = startSlideshowIndex;
    }

    public boolean isErrorDetected() {
        return errorDetected;
    }

    public void setErrorDetected(boolean errorDetected) {
        this.errorDetected = errorDetected;
    }

    private void initSlideshow() {
        active = true;
        errorDetected = false;
        this.slideshowIndex = 0;
        this.startSlideshowIndex = 0;
    }

    private void onError(boolean isShowOnUI) {
        stopSlideshow();
        errorDetected = true;
        ApplicationUtils.addToRerender(Constants.MAINAREA_ID);
        if (isShowOnUI) {
            error.fire(new ErrorEvent(Constants.NO_IMAGES_FOR_SLIDESHOW_ERROR));
        }
        return;
    }

    private void checkIsFileRecentlyDeleted() {
        if (!selectedImage.isLocalImage()) {
            return;
        }

        Image image = (Image) selectedImage.getImage();

        if (!fileManager.isFilePresent(image.getFullPath())) {
            error.fire(new ErrorEvent(Constants.IMAGE_RECENTLY_DELETED_ERROR));
            active = false;
            errorDetected = true;
            ApplicationUtils.addToRerender(Constants.MAINAREA_ID);
            model.resetModel(NavigationEnum.ALBUM_IMAGE_PREVIEW, image.getAlbum().getOwner(), image.getAlbum().getShelf(),
                image.getAlbum(), null, image.getAlbum().getImages());
            return;
        }
    }
}