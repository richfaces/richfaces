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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;
import org.richfaces.photoalbum.model.Album;
import org.richfaces.photoalbum.model.Event;
import org.richfaces.photoalbum.model.actions.IAlbumAction;
import org.richfaces.photoalbum.model.actions.IEventAction;
import org.richfaces.photoalbum.model.event.ErrorEvent;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.ShelfEvent;
import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.FileHandler;
import org.richfaces.photoalbum.util.PhotoAlbumException;

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
    EventManager eventManager;

    @Inject
    Model model;

    @Inject
    @EventType(Events.EVENT_EDITED_EVENT)
    javax.enterprise.event.Event<ShelfEvent> shelfEvent;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    javax.enterprise.event.Event<ErrorEvent> error;

    private List<String> imageUrls;
    private Iterator<String> iterator;

    private String pBarText;
    private int pBarValue = -1;

    private int size;
    private int count;

    private Album album;
    private String albumId;

    private void setImages(Map<String, JSONObject> images) throws JSONException {
        imageUrls = new ArrayList<String>();

        for (JSONObject jo : images.values()) {
            if (jo.has("src_big")) {
                imageUrls.add(jo.getString("src_big"));
            } else if (jo.has("src")) {
                imageUrls.add(jo.getString("src"));
            } else if (jo.has("url")) {
                imageUrls.add(jo.getString("url"));
            }
        }
    }

    private void createAlbum(String name, Event event) {
        album = new Album();
        album.setName(name);
        album.setShelf(event.getShelf());

        albumManager.addAlbum(album);
    }

    public void setUpDownload(String albumName, String albumId, Map<String, JSONObject> images, Event event) {
        try {
            setImages(images);
        } catch (JSONException je) {
            error.fire(new ErrorEvent("Error", "error saving album<br/>" + je.getMessage()));
        }
        
        size = imageUrls.size();
        count = 0;

        iterator = imageUrls.iterator();

        pBarText = "0 / " + size;
        pBarValue = 0;

        this.albumId = albumId;

        createAlbum(albumName, event);

        album = albumAction.resetAlbum(album);
    }

    public void downloadNext() {
        if (!hasNext()) {
            return;
        }
        String imageUrl = iterator.next();

        uploadImage(imageUrl, album.getName() + imageUrl.substring(imageUrl.lastIndexOf(Constants.DOT)), album);
        count++;
        pBarValue = (count * 100) / size;
        pBarText = count + " / " + size;
    }

    public void finishDownload() {
        Event event = album.getShelf().getEvent();

        // save the album
        try {
            albumAction.editAlbum(album);
            album = albumAction.resetAlbum(album);
        } catch (PhotoAlbumException pae) {
            error.fire(new ErrorEvent("Error", "error saving album<br/>" + pae.getMessage()));
        }

        // remove the remote album from the event
        if (event.getRemoteAlbumIds().contains("F" + albumId)) {
            event.getRemoteAlbumIds().remove("F" + albumId);
        } else if (event.getRemoteAlbumIds().contains("G" + albumId)) {
            event.getRemoteAlbumIds().remove("G" + albumId);
        } 

        try {
            eventAction.editEvent(event);
        } catch (PhotoAlbumException pae) {
            error.fire(new ErrorEvent("Error", "error removing album<br/>" + pae.getMessage()));
        }

        pBarText = "";
        pBarValue = -1;
        
        UIComponent root = FacesContext.getCurrentInstance().getViewRoot();
        UIComponent component = root.findComponent("overForm");
        FacesContext.getCurrentInstance().addMessage(component.getClientId(FacesContext.getCurrentInstance()),
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Album has been successfully imported"));

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
        } catch (IOException ioException) {
            error.fire(new ErrorEvent("Error", "IO error<br/>" + ioException.getMessage()));
        }

        fileUploadManager.uploadFile(new FileHandler(file), album);
    }

    public int getValue() {
        return pBarValue;
    }

    public String getText() {
        return pBarText;
    }

    public Iterator<String> getIterator() {
        return iterator;
    }

    public boolean hasNext() {
        return iterator != null && iterator.hasNext();
    }
}
