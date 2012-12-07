/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.photoalbum.manager;

/**
 * Class encapsulated all functionality, related to working with album.
 *
 * @author Andrey Markhel
 */
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.event.AlbumEvent;
import org.richfaces.photoalbum.event.EventType;
import org.richfaces.photoalbum.event.EventTypeQualifier;
import org.richfaces.photoalbum.event.Events;
import org.richfaces.photoalbum.event.SimpleEvent;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IAlbumAction;
import org.richfaces.photoalbum.util.Preferred;

@Named
@RequestScoped
public class AlbumManager implements Serializable {

    private static final long serialVersionUID = 2631634926126857691L;

    private boolean validationSuccess = false;

    private boolean errorInCreate = false;

    @Inject
    private IAlbumAction albumAction;

    @Inject
    @Preferred
    private User user;
    @Inject
    Model model;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    Event<SimpleEvent> error;
    @Inject
    @Any
    Event<AlbumEvent> albumEvent;

    private Album album = new Album();

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    /**
     * Method, that invoked on creation of the new album. Only registered users can create new albums.
     *
     * @param album - new album
     *
     */
    // @AdminRestricted
    public void addAlbum(Album album) {
        if (user == null) {
            return;
        }
        // Shelf must be not-null
        if (album.getShelf() == null) {
            // facesMessages.addToControl(Constants.SHELF_ID, Constants.SHELF_MUST_BE_NOT_NULL_ERROR, new Object[0]);
            FacesContext.getCurrentInstance().addMessage(Constants.SHELF_ID,
                new FacesMessage(Constants.SHELF_MUST_BE_NOT_NULL_ERROR));
            // Contexts.getConversationContext().set(Constants.ALBUM_VARIABLE, album);
            return;
        }
        // Album name must be unique in shelf
        if (user.hasAlbumWithName(album)) {
            error.fire(new SimpleEvent(Constants.SAME_ALBUM_EXIST_ERROR));
            return;
        }
        // All data is valid
        validationSuccess = true;
        try {
            // Save to DB
            album.setCreated(new Date());
            albumAction.addAlbum(album);
        } catch (Exception e) {
            error.fire(new SimpleEvent(Constants.ALBUM_SAVING_ERROR));
            return;
        }
        // Reset 'album' component in conversation scope
        // Contexts.getConversationContext().set(Constants.ALBUM_VARIABLE, null);

        // Raise 'albumAdded' event
        albumEvent.select(new EventTypeQualifier(Events.ALBUM_ADDED_EVENT)).fire(new AlbumEvent(album));
        // album = null;
    }

    /**
     * Method, that invoked when user want to create new album. Only registered users can create new albums.
     *
     * @param shelf - shelf, that will contain new album
     * @param isShowAlbumAfterCreate - indicate is we need to show created album after create.
     *
     */
    // @AdminRestricted
    public void createAlbum(Shelf shelf, boolean isShowAlbumAfterCreate) {
        if (user == null) {
            return;
        }
        album = new Album();
        if (shelf == null) {
            if (model.getSelectedShelf() != null) {
                shelf = model.getSelectedShelf();
            } else if (user.getShelves().size() > 0) {
                shelf = user.getShelves().get(0);
            }
            if (shelf == null) {
                error.fire(new SimpleEvent(Constants.NO_SHELF_ERROR));
                setErrorInCreate(true);
                return;
            }
        }
        album.setShelf(shelf);
        album.setShowAfterCreate(isShowAlbumAfterCreate);
        // Reset 'album' component in conversation scope
        // Contexts.getConversationContext().set(Constants.ALBUM_VARIABLE, album);
    }

    /**
     * Method, that invoked when user click 'Edit album' button. Only registered users can edit albums.
     *
     * @param album - edited album
     * @param editFromInplace - indicate whether edit process was initiated by inplaceInput component
     */
    // @AdminRestricted
    public void editAlbum(Album album, boolean editFromInplace) {
        if (user == null) {
            return;
        }
        try {
            if (user.hasAlbumWithName(album)) {
                error.fire(new SimpleEvent(Constants.SAME_ALBUM_EXIST_ERROR));
                albumAction.resetAlbum(album);
                return;
            }
            if (editFromInplace) {
                // We need validate album name manually
                Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                Set<ConstraintViolation<Album>> constraintViolations = validator.validate(album);
                if (constraintViolations.size() > 0) {
                    for (ConstraintViolation<Album> cv : constraintViolations) {
                        error.fire(new SimpleEvent(cv.getMessage()));
                    }
                    // If error occured we need refresh album to display correct value in inplaceInput
                    albumAction.resetAlbum(album);
                    return;
                }
            }
            albumAction.editAlbum(album);
        } catch (Exception e) {
            error.fire(new SimpleEvent(Constants.ALBUM_SAVING_ERROR));
            albumAction.resetAlbum(album);
            return;
        }
        // Reset 'album' component in conversation scope
        albumEvent.select(new EventTypeQualifier(Events.ALBUM_EDITED_EVENT)).fire(new AlbumEvent(album));
    }

    /**
     * Method, that invoked when user click 'Delete album' button. Only registered users can delete albums.
     *
     * @param album - album to delete
     *
     */
    // @AdminRestricted
    public void deleteAlbum(Album album) {
        if (user == null) {
            return;
        }
        String pathToDelete = album.getPath();
        try {
            albumAction.deleteAlbum(album);
        } catch (Exception e) {
            error.fire(new SimpleEvent(Constants.ALBUM_DELETING_ERROR));
            return;
        }
        // Raise 'albumDeleted' event, parameter path - path of Directory to delete
        albumEvent.select(new EventTypeQualifier(Events.ALBUM_DELETED_EVENT)).fire(new AlbumEvent(album, pathToDelete));
    }

    public boolean isValidationSuccess() {
        return validationSuccess;
    }

    public void setValidationSuccess(boolean validationSuccess) {
        this.validationSuccess = validationSuccess;
    }

    public boolean isErrorInCreate() {
        return errorInCreate;
    }

    public void setErrorInCreate(boolean errorInCreate) {
        this.errorInCreate = errorInCreate;
    }
}