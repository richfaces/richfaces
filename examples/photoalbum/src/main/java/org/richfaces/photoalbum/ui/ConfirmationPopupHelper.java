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
package org.richfaces.photoalbum.ui;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.photoalbum.manager.AlbumManager;
import org.richfaces.photoalbum.manager.ImageManager;
import org.richfaces.photoalbum.manager.ShelfManager;
import org.richfaces.photoalbum.model.Album;
import org.richfaces.photoalbum.model.Image;
import org.richfaces.photoalbum.model.Shelf;

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

    private Image image;

    private Shelf shelf;

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
        switch (action) {
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