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
package org.richfaces.photoalbum.ui;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.manager.AlbumManager;
import org.richfaces.photoalbum.manager.ImageManager;
import org.richfaces.photoalbum.manager.ShelfManager;

@Named
@ApplicationScoped
public class ConfirmationPopupHelper implements Serializable {

    private static final long serialVersionUID = 2561824019376412988L;

    private enum Actions {
        DELETE_SHELF,
        DELETE_ALBUM,
        DELETE_IMAGE
    }

    public Actions getDeleteShelf() {
        return Actions.DELETE_SHELF;
    }

    public Actions getDeleteAlbum() {
        return Actions.DELETE_ALBUM;
    }

    public Actions getDeleteImage() {
        return Actions.DELETE_IMAGE;
    }

    private String caption;

    private Actions action;

    // @In @Out
    //@Inject
    private Image image;

    // @In @Out
    //@Inject
    private Shelf shelf;

    // @In @Out
    //@Inject
    private Album album;

    @Inject
    AlbumManager albumManager;

    @Inject
    ShelfManager shelfManager;

    @Inject
    ImageManager imageManager;

    public void initImagePopup(Actions action, String caption, Image image) {
        this.caption = caption;
        this.action = action;
        this.image = image;
    }

    public void initAlbumData(Actions action, String caption, Album album) {
        this.caption = caption;
        this.action = action;
        this.album = album;
    }

    public void initShelfData(Actions action, String caption, Shelf shelf) {
        this.caption = caption;
        this.action = action;
        this.shelf = shelf;
    }

    private void deleteAlbum() {
        albumManager.deleteAlbum(this.album);
    }

    private void deleteImage() {
        imageManager.deleteImage(this.image);
    }
    
    private void deleteShelf() {
        shelfManager.deleteShelf(this.shelf);
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Actions getaction() {
        return action;
    }

    public void setaction(Actions action) {
        this.action = action;
    }

    public void doAction() {
        switch(action) {
            case DELETE_SHELF:
                deleteShelf();
                break;
            case DELETE_ALBUM:
                deleteAlbum();
                break;
            case DELETE_IMAGE:
                deleteImage();
                break;
        }
    }
}