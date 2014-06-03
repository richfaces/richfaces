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

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.photoalbum.model.Album;
import org.richfaces.photoalbum.model.Event;
import org.richfaces.photoalbum.model.Image;
import org.richfaces.photoalbum.model.MetaTag;
import org.richfaces.photoalbum.model.Shelf;
import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.EventTypeQualifier;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.NavEvent;
import org.richfaces.photoalbum.model.event.SimpleEvent;

/**
 * This class represent 'M' in MVC pattern. It is storage to application flow related data such as selectedAlbum, image,
 * mainArea to preview etc..
 * 
 * @author Andrey Markhel
 */

@Named
@ApplicationScoped
public class Model implements Serializable {

    private static final long serialVersionUID = -1767281809514660171L;

    private Image selectedImage;

    private Album selectedAlbum;

    private User selectedUser;

    private Shelf selectedShelf;

    private MetaTag selectedTag;

    private Event selectedEvent;

    private NavigationEnum mainArea = NavigationEnum.ANONYM;

    private List<Image> images;

    @Inject
    @Any
    javax.enterprise.event.Event<SimpleEvent> event;
    @Inject
    MetaTag metatag;

    /**
     * This method invoked after the almost user actions, to prepare properly data to show in the UI.
     * 
     * @param mainArea - next Area to show(determined in controller)
     * @param selectedUser - user, that was selected(determined in controller)
     * @param selectedShelf - shelf, that was selected(determined in controller)
     * @param selectedAlbum - album, that was selected(determined in controller)
     * @param selectedImage - image, that was selected(determined in controller)
     * @param images - list of images, to show during slideshow process(determined in controller)
     */
    public void resetModel(NavigationEnum mainArea, User selectedUser, Shelf selectedShelf, Album selectedAlbum,
        Image selectedImage, List<Image> images) {
        if (this.mainArea != null && this.mainArea.equals(NavigationEnum.FILE_UPLOAD)) {
            event.select(new EventTypeQualifier(Events.CLEAR_FILE_UPLOAD_EVENT)).fire(new SimpleEvent());
        }
        this.setSelectedAlbum(selectedAlbum);
        this.setSelectedImage(selectedImage);
        this.setSelectedShelf(selectedShelf);
        this.setSelectedUser(selectedUser);
        // this.setMainArea(mainArea);
        this.mainArea = mainArea;
        this.images = images;
    }

    public void resetModel(NavigationEnum mainArea, User selectedUser, Shelf selectedShelf, Album selectedAlbum,
        Image selectedImage, List<Image> images, Event selectedEvent) {

        resetModel(mainArea, selectedUser, selectedShelf, selectedAlbum, selectedImage, images);
        this.selectedEvent = selectedEvent;
    }

    /**
     * This method observes <code> Constants.UPDATE_MAIN_AREA_EVENT </code>event and invoked after the user actions, that not
     * change model, but change area to preview
     * 
     * @param mainArea - next Area to show
     * 
     */
    public void setMainArea(@Observes @EventType(Events.UPDATE_MAIN_AREA_EVENT) NavEvent ne) {
        if (this.mainArea != null && this.mainArea.equals(NavigationEnum.FILE_UPLOAD)) {
            event.select(new EventTypeQualifier(Events.CLEAR_FILE_UPLOAD_EVENT)).fire(new SimpleEvent());
        }
        this.mainArea = ne.getNav();
    }

    /**
     * This method observes <code> Constants.UPDATE_SELECTED_TAG_EVENT </code>event and invoked after the user click on any
     * metatag.
     * 
     * @param selectedTag - clicked tag
     * 
     */
    public void setSelectedTag(MetaTag tag) {
        this.selectedTag = tag;
    }

    public void observeSelectedTag(@Observes @EventType(Events.UPDATE_SELECTED_TAG_EVENT) SimpleEvent se) {
        this.selectedTag = metatag;
    }

    public NavigationEnum getMainArea() {
        return mainArea;
    }

    public Image getSelectedImage() {
        return selectedImage;
    }

    private void setSelectedImage(Image selectedImage) {
        this.selectedImage = selectedImage;
    }

    public Album getSelectedAlbum() {
        return selectedAlbum;
    }

    public void setSelectedAlbum(Album selectedAlbum) {
        this.selectedAlbum = selectedAlbum;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    private void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public Shelf getSelectedShelf() {
        return selectedShelf;
    }

    private void setSelectedShelf(Shelf selectedShelf) {
        this.selectedShelf = selectedShelf;
    }

    public MetaTag getSelectedTag() {
        return selectedTag;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }
}