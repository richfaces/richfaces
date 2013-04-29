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
 * Class encapsulated all functionality, related to drag'n'drop process.
 *
 * @author Andrey Markhel
 */
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Event;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.event.AlbumEvent;
import org.richfaces.photoalbum.event.EventType;
import org.richfaces.photoalbum.event.Events;
import org.richfaces.photoalbum.event.ImageEvent;
import org.richfaces.photoalbum.event.SimpleEvent;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IAlbumAction;
import org.richfaces.photoalbum.service.IEventAction;
import org.richfaces.photoalbum.service.PhotoAlbumException;
import org.richfaces.photoalbum.util.Preferred;
import org.richfaces.photoalbum.util.Utils;
import org.richfaces.ui.drag.DropEvent;
import org.richfaces.ui.drag.DropListener;

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
    javax.enterprise.event.Event<SimpleEvent> error;
    @Inject
    @EventType(Events.ALBUM_DRAGGED_EVENT)
    javax.enterprise.event.Event<AlbumEvent> albumEvent;
    @Inject
    @EventType(Events.IMAGE_DRAGGED_EVENT)
    javax.enterprise.event.Event<ImageEvent> imageEvent;

    /**
     * Listenet, that invoked during drag'n'drop process. Only registered users can drag images.
     * 
     * @param event - event, indicated that drag'n'drop started
     */
    // @AdminRestricted
    public void processDrop(DropEvent dropEvent) {
        if (user == null)
            return;
        // Dropzone dropzone = (Dropzone) dropEvent.getComponent();
        Object dragValue = dropEvent.getDragValue();
        Object dropValue = dropEvent.getDropValue();
        if (dragValue instanceof Image) {
            // If user drag image
            if (!((Album) dropValue).getOwner().getLogin().equals(user.getLogin())) {
                // Drag in the album, that not belongs to user
                error.fire(new SimpleEvent(Constants.DND_PHOTO_ERROR));
                return;
            }
            handleImage((Image) dragValue, (Album) dropValue);
        } else if (dragValue instanceof Album) {
            // If user drag album
            if (!((Shelf) dropValue).getOwner().getLogin().equals(user.getLogin())) {
                // Drag in the shelf, that not belongs to user
                error.fire(new SimpleEvent(Constants.DND_ALBUM_ERROR));
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
            error.fire(new SimpleEvent(Constants.ERROR_IN_DB));
            return;
        }
        albumEvent.fire(new AlbumEvent(dragValue, pathOld));
        Utils.addToRerender(Constants.TREE_ID);
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
            error.fire(new SimpleEvent(Constants.ERROR_IN_DB));
            return;
        }
        imageEvent.fire(new ImageEvent(dragValue, pathOld));
        Utils.addToRerender(Constants.TREE_ID);
    }

    public void addAlbumToEvent(DropEvent dropEvent) {
        if (user == null)
            return;
        
        Object dragValue = dropEvent.getDragValue();
        Event event = (Event) dropEvent.getDropValue();
        
        if (dragValue instanceof Album) {
            Album album = (Album) dragValue;
            
            event.getAlbums().add(album);
            album.setEvent(event);

            try {
                albumAction.editAlbum(album);
                eventAction.editEvent(event);
            } catch (PhotoAlbumException e) {
                error.fire(new SimpleEvent(Constants.ERROR_IN_DB + e.getMessage()));
            }
            return;
        }
        
        if (dragValue instanceof String) {
            String aid = (String) dragValue;
            
            event.getFacebookAlbums().add(aid);
            
            try {
                eventAction.editEvent(event);
            } catch (PhotoAlbumException e) {
                error.fire(new SimpleEvent(Constants.ERROR_IN_DB + e.getMessage()));
            }
        }
        

        
    }
}
