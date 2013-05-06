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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;
import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Event;
import org.richfaces.photoalbum.event.ErrorEvent;
import org.richfaces.photoalbum.event.EventType;
import org.richfaces.photoalbum.event.Events;
import org.richfaces.photoalbum.event.ShelfEvent;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IAlbumAction;
import org.richfaces.photoalbum.service.IEventAction;
import org.richfaces.photoalbum.service.PhotoAlbumException;
import org.richfaces.photoalbum.util.FileHandler;

/**
 * This class takes care of downloading a list of images from given URLs and putting them into a new album
 * 
 * @author mpetrov
 *
 */

@Named
@SessionScoped
public class FileDownloadManager implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3815919720565571122L;

    @Inject
    FileUploadManager fileUploadManager;

    @Inject
    AlbumManager albumManager;

    @Inject
    IAlbumAction albumAction;

    @Inject
    IEventAction eventAction;

    @Inject
    Model model;

    @Inject
    @EventType(Events.EVENT_EDITED_EVENT)
    javax.enterprise.event.Event<ShelfEvent> shelfEvent;
    
    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    javax.enterprise.event.Event<ErrorEvent> error;

    private List<String> imageUrls;

    private Album album;
    private String albumName;
    private String albumId;

    private Logger log = Logger.getLogger("FileDownloadManager");

    public void setImages(String json) throws JSONException {
        JSONArray ja = new JSONArray(json);

        if (ja.length() == 0) {
            return;
        }

        imageUrls = new ArrayList<String>();

        setAlbumId(ja.getJSONObject(0).getString("aid"));

        for (int i = 0; i < ja.length(); i++) {
            imageUrls.add(ja.getJSONObject(i).getString("src_big"));
        }
    }

    public void createAlbum(String name) {
        album = new Album();
        album.setName(name);
        album.setShelf(model.getSelectedEvent().getShelf());

        albumManager.addAlbum(album);
    }

    public void downloadImages(String clientId) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }

        // make new album to store the photos in
        createAlbum(albumName);
        album = albumAction.resetAlbum(album);

        // process the URLs
        for (String imageUrl : imageUrls) {
            uploadImage(imageUrl, album.getName() + imageUrl.substring(imageUrl.lastIndexOf(Constants.DOT)), album);
        }

        // save the album
        try {
            albumAction.editAlbum(album);
            album = albumAction.resetAlbum(album);
        } catch (PhotoAlbumException pae) {
            error.fire(new ErrorEvent("Error", "error saving album<br/>" + pae.getMessage()));
            log.log(Level.INFO, "error saving album", pae);
        }

        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Done!", "Album has been successfully downloaded.");
        FacesContext.getCurrentInstance().addMessage(clientId, fm);

        // remove the FB album from the event
        Event event = album.getShelf().getEvent();
        event.getFacebookAlbums().remove(getAlbumId());

        try {
            eventAction.editEvent(event);
        } catch (PhotoAlbumException pae) {
            error.fire(new ErrorEvent("Error", "error removing album<br/>" + pae.getMessage()));
            log.log(Level.INFO, "error", pae);
        }

        // reset the view
        shelfEvent.fire(new ShelfEvent(event));
    }

    private void uploadImage(String imageUrl, String imageName, Album album) {
        File file = new File(imageName);
        int i;
        try {
            URL url = new URL(imageUrl);
            URLConnection con = url.openConnection();
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file.getName()));
            while ((i = bis.read()) != -1) {
                bos.write(i);
            }
            bos.flush();
            bis.close();
        } catch (MalformedInputException malformedInputException) {
            error.fire(new ErrorEvent("Error", "error uploading image<br/>" + malformedInputException.getMessage()));
            log.log(Level.ALL, "error", malformedInputException);
        } catch (IOException ioException) {
            error.fire(new ErrorEvent("Error", "IO error<br/>" + ioException.getMessage()));
            log.log(Level.ALL, "error", ioException);
        }

        fileUploadManager.uploadFile(new FileHandler(file), album);
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
