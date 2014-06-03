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
 * Class encapsulated all functionality, related to drag'n'drop process.
 *
 * @author Andrey Markhel
 */
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.event.DropEvent;
import org.richfaces.event.DropListener;
import org.richfaces.photoalbum.model.Album;
import org.richfaces.photoalbum.model.Event;
import org.richfaces.photoalbum.model.Image;
import org.richfaces.photoalbum.model.Shelf;
import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.model.actions.IAlbumAction;
import org.richfaces.photoalbum.model.actions.IEventAction;
import org.richfaces.photoalbum.model.event.AlbumEvent;
import org.richfaces.photoalbum.model.event.ErrorEvent;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.ImageEvent;
import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.PhotoAlbumException;
import org.richfaces.photoalbum.util.Preferred;
import org.richfaces.photoalbum.util.ApplicationUtils;

@Named("dndManager")
public class DnDManager implements DropListener {

    @Inject
    @Preferred
    User user;

    @Inject
    FileManager fileManager;

    @Inject
    IAlbumAction albumAction;

    @Inject
    IEventAction eventAction;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    javax.enterprise.event.Event<ErrorEvent> error;
    @Inject
    @EventType(Events.ALBUM_DRAGGED_EVENT)
    javax.enterprise.event.Event<AlbumEvent> albumEvent;
    @Inject
    @EventType(Events.IMAGE_DRAGGED_EVENT)
    javax.enterprise.event.Event<ImageEvent> imageEvent;

    /**
     * Listener, that is invoked during drag'n'drop process. Only registered users can drag images.
     *
     * @param event - event, indicated that drag'n'drop started
     */
    public void processDrop(DropEvent dropEvent) {
        if (user == null) {
            return;
        }
        Object dragValue = dropEvent.getDragValue();
        Object dropValue = dropEvent.getDropValue();
        if (dragValue instanceof Image) {
            // If user drag image
            if (!((Album) dropValue).getOwner().getLogin().equals(user.getLogin())) {
                // Drag in the album, that not belongs to user
                error.fire(new ErrorEvent("", Constants.DND_PHOTO_ERROR));
                return;
            }
            handleImage((Image) dragValue, (Album) dropValue);
        } else if (dragValue instanceof Album) {
            // If user drag album
            if (!((Shelf) dropValue).getOwner().getLogin().equals(user.getLogin())) {
                // Drag in the shelf, that not belongs to user
                error.fire(new ErrorEvent("", Constants.DND_ALBUM_ERROR));
                return;
            }
            handleAlbum((Album) dragValue, (Shelf) dropValue);
        }
    }

    private void handleAlbum(Album dragValue, Shelf dropValue) {
        if (dragValue.getShelf().equals(dropValue)) {
            return;
        }
        String pathOld = dragValue.getPath();
        dropValue.addAlbum(dragValue);
        try {
            albumAction.editAlbum(dragValue);
        } catch (Exception e) {
            error.fire(new ErrorEvent("Error:", Constants.ERROR_IN_DB + "<br/>" + e.getMessage()));
            return;
        }
        albumEvent.fire(new AlbumEvent(dragValue, pathOld));
        ApplicationUtils.addToRerender(Constants.TREE_ID);
    }

    private void handleImage(Image dragValue, Album dropValue) {
        if (dragValue.getAlbum().equals(dropValue)) {
            return;
        }
        String pathOld = dragValue.getFullPath();
        dropValue.addImage(dragValue);
        try {
            albumAction.editAlbum(dropValue);
        } catch (Exception e) {
            error.fire(new ErrorEvent("Error:", Constants.ERROR_IN_DB + "<br/>" + e.getMessage()));
            return;
        }
        imageEvent.fire(new ImageEvent(dragValue, pathOld));
        ApplicationUtils.addToRerender(Constants.TREE_ID);
    }

    public void addAlbumToEvent(DropEvent dropEvent) {
        if (user == null) {
            return;
        }

        Object dragValue = dropEvent.getDragValue();
        Event event = (Event) dropEvent.getDropValue();

        if (dragValue instanceof Album) {
            Album album = (Album) dragValue;

            String pathOld = album.getPath();

            event.getShelf().addAlbum(album);

            try {
                albumAction.editAlbum(album);
                eventAction.editEvent(event);
            } catch (PhotoAlbumException e) {
                error.fire(new ErrorEvent("Error:", Constants.ERROR_IN_DB + ": " + e.getMessage()));
            }
            albumEvent.fire(new AlbumEvent(album, pathOld));
        }
    }
}
