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

import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.event.ErrorEvent;
import org.richfaces.photoalbum.event.EventType;
import org.richfaces.photoalbum.event.Events;
import org.richfaces.photoalbum.event.SimpleEvent;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.util.Utils;

@ApplicationScoped
@Named("slideshow")
public class SlideshowManager implements Serializable {

    private static final long serialVersionUID = 7801877176558409702L;

    private Integer slideshowIndex;

    private Integer startSlideshowIndex;

    private Image selectedImage;

    private boolean active;

    private boolean errorDetected;

    @Inject
    Model model;

    @Inject
    FileManager fileManager;

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
        if (model.getImages() == null || model.getImages().size() < 1) {
            onError(true);
            return;
        }
        if (this.selectedImage == null) {
            this.selectedImage = model.getImages().get(this.slideshowIndex);
        }
        // mark image as 'visited'
        this.selectedImage.setVisited(true);
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
        this.selectedImage = selectedImage;

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

    public Image getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Image selectedImage) {
        this.selectedImage = selectedImage;
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
        if (slideshowIndex == model.getImages().size() - 1) {
            slideshowIndex = -1;
        }
        slideshowIndex++;
        // To prevent slideshow mechanism working in cycle.
        if (slideshowIndex == startSlideshowIndex) {
            onError(false);
            return;
        }
        selectedImage = model.getImages().get(slideshowIndex);
        // mark image as 'visited'
        this.selectedImage.setVisited(true);
        // Check if that image was recently deleted. If yes, stopping slideshow
        checkIsFileRecentlyDeleted();
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
        Utils.addToRerender(Constants.MAINAREA_ID);
        if (isShowOnUI) {
            error.fire(new ErrorEvent(Constants.NO_IMAGES_FOR_SLIDESHOW_ERROR));
        }
        return;
    }

    private void checkIsFileRecentlyDeleted() {
        if (!fileManager.isFilePresent(this.selectedImage.getFullPath())) {
            error.fire(new ErrorEvent(Constants.IMAGE_RECENTLY_DELETED_ERROR));
            active = false;
            errorDetected = true;
            Utils.addToRerender(Constants.MAINAREA_ID);
            model.resetModel(NavigationEnum.ALBUM_IMAGE_PREVIEW, this.selectedImage.getAlbum().getOwner(), this.selectedImage
                .getAlbum().getShelf(), this.selectedImage.getAlbum(), null, this.selectedImage.getAlbum().getImages());
            return;
        }
    }
}