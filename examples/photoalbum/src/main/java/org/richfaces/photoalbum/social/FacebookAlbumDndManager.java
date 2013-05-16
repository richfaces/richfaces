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

package org.richfaces.photoalbum.social;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;
import org.richfaces.photoalbum.domain.Event;
import org.richfaces.photoalbum.event.ErrorEvent;
import org.richfaces.photoalbum.event.EventType;
import org.richfaces.photoalbum.event.Events;
import org.richfaces.photoalbum.manager.FileDownloadManager;
import org.richfaces.photoalbum.service.IEventAction;
import org.richfaces.photoalbum.service.PhotoAlbumException;
import org.richfaces.ui.drag.dropTarget.DropEvent;
import org.richfaces.ui.drag.dropTarget.DropListener;


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

    @Override
    public void processDrop(DropEvent event) {
        /*
         * album is dropped onto an event
         *
         * when this happens we give user a choice to either share or import the album
         * in the meantime we only save the elements
         *
         */

        this.albumId = (String) event.getDragValue();
        this.event = (Event) event.getDropValue();
    }

    public void shareAlbum() {
        event.getFacebookAlbums().add(albumId);

        try {
        ea.editEvent(event);
        } catch (PhotoAlbumException pae) {
            error.fire(new ErrorEvent("Error saving event", pae.getMessage()));
        }
    }

    public void importAlbum() {
        Logger logger = Logger.getLogger("FbDnD");

        logger.info("importing album");
        try {
            logger.info("trying");
            logger.info("aid: " + albumId);
            logger.info("" + (fac.getImagesOfAlbum(albumId) == null));
            fdm.setImages(fac.getImagesOfAlbum(albumId));

            logger.info("set Images");

            fdm.downloadImages();

            logger.info("donwloaded images");
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
}
