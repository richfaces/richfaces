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

package org.richfaces.photoalbum.social.facebook;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.event.DropEvent;
import org.richfaces.event.DropListener;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;
import org.richfaces.photoalbum.manager.FileDownloadManager;
import org.richfaces.photoalbum.model.Album;
import org.richfaces.photoalbum.model.Event;
import org.richfaces.photoalbum.model.actions.IEventAction;
import org.richfaces.photoalbum.model.event.ErrorEvent;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.util.PhotoAlbumException;

@Named("fbDndManager")
@SessionScoped
public class FacebookAlbumDndManager implements Serializable, DropListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    FacebookAlbumCache fac;

    @Inject
    IEventAction ea;

    @Inject
    FileDownloadManager fdm;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    javax.enterprise.event.Event<ErrorEvent> error;

    private String albumId;
    private Event event;

    private boolean albumAlreadyShared;

    @Override
    public void processDrop(DropEvent event) {
        /*
         * album is dropped onto an event
         *
         * when this happens we give user a choice to either share or import the album; in the meantime we only save the
         * elements
         */

        this.albumId = (String) event.getDragValue();
        this.event = (Event) event.getDropValue();

        // check if the album is already shared
        if (this.event.getFacebookAlbumIds().contains(albumId)) {
            setAlbumAlreadyShared(true);
            error.fire(new ErrorEvent("This album is already shared in this event"));
            return;
        }

        String albumName = "";
        try {
            albumName = fac.getAlbum(albumId).getString("name");
        } catch (JSONException e) {
            // nothing, the exception will not be thrown
        }

        for (Album album : this.event.getShelf().getAlbums()) {
            if (album.getName() == albumName) {
                setAlbumAlreadyShared(true);
                error.fire(new ErrorEvent("Album with this name has already been imported to this event"));
                return;
            }
        }

        setAlbumAlreadyShared(false);
    }

    public void shareAlbum() {
        event.getRemoteAlbumIds().add("F" + albumId);

        try {
            ea.editEvent(event);
        } catch (PhotoAlbumException pae) {
            error.fire(new ErrorEvent("Error saving event", pae.getMessage()));
        }
    }

    public void importAlbum() {
        try {
            fdm.setUpDownload(fac.getAlbum(albumId).getString("name"), albumId, fac.getImageMap(albumId), event);
        } catch (JSONException je) {
            error.fire(new ErrorEvent("Error importing album", je.getMessage()));
        }

    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public JSONObject getAlbum() {
        return fac.getAlbum(getAlbumId());
    }

    public boolean isAlbumAlreadyShared() {
        return albumAlreadyShared;
    }

    public void setAlbumAlreadyShared(boolean albumAlreadyShared) {
        this.albumAlreadyShared = albumAlreadyShared;
    }
}
