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

package org.richfaces.photoalbum.social.gplus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;
import org.richfaces.photoalbum.model.event.ErrorEvent;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;

@Named
@ApplicationScoped
public class GooglePlusAlbumCache {

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    Event<ErrorEvent> error;

    private Map<String, JSONObject> albums = new HashMap<String, JSONObject>();
    // < albumId, {album} >
    private Map<String, Map<String, JSONObject>> images = new HashMap<String, Map<String, JSONObject>>();
    // < albumId, < imageId, {image} > >

    private String currentAlbumId;
    private String currentImageId;

    public void storeAlbums(List<JSONObject> albumsList) {
        storeAlbums(albumsList, false);
    }

    public void storeAlbums(List<JSONObject> albumsList, boolean rewrite) {
        String albumId;

        try {
            for (JSONObject jo : albumsList) {

                albumId = jo.getString("fullId");
                if (albums.containsKey(albumId) && !rewrite) {
                    // the album has already been loaded
                    return;
                } else if (rewrite) {
                    albums.remove(albumId);
                }
                images.put(albumId, null);
                albums.put(albumId, jo);
            }
        } catch (JSONException je) {
            error.fire(new ErrorEvent("Error", je.getMessage()));
        }
    }

    public void setAlbumImages(String imagesArray) {
        String imageId = "";
        String albumId = "";
        JSONObject jo;

        try {
            JSONArray ja = new JSONArray(imagesArray);
            albumId = ja.getJSONObject(0).getString("fullAlbumId");

            currentAlbumId = albumId;

            if (images.get(albumId) != null) {
                // these images are already cached
                return;
            }
            
            int size = ja.length();
            
            // put size in the album
            albums.get(albumId).put("size", size);

            images.put(albumId, new HashMap<String, JSONObject>());

            for (int i = 0; i < size; i++) {
                jo = ja.getJSONObject(i);

                if (!jo.has("albumId") || !jo.has("id")) {
                    error.fire(new ErrorEvent("Error, object does not contain images"));

                }

                imageId = jo.getString("id");

                images.get(albumId).put(imageId, jo);
            }

            albums.get(albumId).put("size", images.get(albumId).size());
        } catch (JSONException je) {
            error.fire(new ErrorEvent("Error", je.getMessage()));
        }
    }

    public JSONObject getAlbum(String albumId) {
        return albums.get(albumId);
    }

    public Map<String, JSONObject> getImagesOfAlbum(String albumId) {
        return images.get(albumId);
    }

    public List<JSONObject> getAlbums(List<String> albumIds) {
        if (albumIds == null) {
            return null;
        }

        List<JSONObject> list = new ArrayList<JSONObject>();

        for (String id : albumIds) {
            list.add(albums.get(id));
        }

        return list;
    }

    public String getCurrentAlbumId() {
        return currentAlbumId;
    }

    public void setCurrentAlbumId(String currentAlbumId) {
        this.currentAlbumId = currentAlbumId;
    }

    public String getCurrentImageId() {
        return currentImageId;
    }

    public void setCurrentImageId(String currentImageId) {
        this.currentImageId = currentImageId;
    }

    public JSONObject getCurrentAlbum() {
        return albums.get(currentAlbumId);
    }

    public List<JSONObject> getCurrentImages() {
        return new ArrayList<JSONObject>(images.get(currentAlbumId).values());
    }

    public JSONObject getCurrentImage() {
        return images.get(currentAlbumId).get(currentImageId);
    }

    public List<JSONObject> getAllImages() {
        List<JSONObject> list = new ArrayList<JSONObject>();

        for (Map<String, JSONObject> album : images.values()) {
            if (album != null && album.values() != null && !album.values().isEmpty()) {
                list.addAll(album.values());
            }
        }

        return list;
    }

    public boolean isAlbumLoaded(String albumId) {
        return images.containsKey(albumId) && (images.get(albumId) != null);
    }

    public void setAlbumAndImages(String aiJson) {
        try {
            JSONObject jAlbum = new JSONObject(aiJson);

            JSONArray albumImages = jAlbum.getJSONArray("images");
            jAlbum.remove("images");

            String albumId = jAlbum.getString("fullId");
            int size = albumImages.length();
            
            jAlbum.put("size", size);
            albums.put(albumId, jAlbum);
            images.put(albumId, new HashMap<String, JSONObject>());

            JSONObject jo;
            String imageId;
            Map<String, JSONObject> album = images.get(albumId);
            for (int i = 0; i < albumImages.length(); i++) {
                jo = albumImages.getJSONObject(i);

                if (!jo.has("id")) {
                    error.fire(new ErrorEvent("Error, object does not contain images"));
                }

                imageId = jo.getString("id");

                album.put(imageId, jo);
            }

        } catch (JSONException je) {
            error.fire(new ErrorEvent("Error", je.getMessage()));
        }
    }
}
