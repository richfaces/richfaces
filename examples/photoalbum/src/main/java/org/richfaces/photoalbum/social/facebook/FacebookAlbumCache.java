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
public class FacebookAlbumCache {

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    Event<ErrorEvent> error;

    private Map<String, JSONObject> albums = new HashMap<String, JSONObject>();
    // < albumId, {album} >
    private Map<String, Map<String, JSONObject>> images = new HashMap<String, Map<String, JSONObject>>();
    // < albumId, < imageId, {image} > >

    private String currentAlbumId;
    private String currentImageId;

    private boolean loaded = false;
    private boolean emptyCache = true;

    public void setAll(String json) {
        albums = new HashMap<String, JSONObject>();
        images = new HashMap<String, Map<String, JSONObject>>();

        try {
            JSONArray jAlbums = new JSONArray(json);

            String albumId;
            JSONObject jo;

            for (int i = 0; i < jAlbums.length(); i++) {
                jo = jAlbums.getJSONObject(i);

                if (!jo.has("id")) {
                    error.fire(new ErrorEvent("Error, object does not contain albums"));
                }

                albumId = jo.getString("id");

                images.put(albumId, new HashMap<String, JSONObject>());
                if (jo.getInt("size") > 0) {
                    storeImagesToAlbum(albumId, jo.getJSONArray("images"));
                    jo.remove("images");
                }
                albums.put(albumId, jo);
            }
            loaded = true;
            emptyCache = jAlbums.length() == 0;
        } catch (JSONException e) {
            error.fire(new ErrorEvent("Error: ", e.getMessage()));
        }
    }

    public void storeImagesToAlbum(String albumId, JSONArray ja) {
        String imageId = "";
        JSONObject jo;
        Map<String, JSONObject> album = images.get(albumId);

        try {
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);

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

    public JSONObject getAlbum(String albumId) {
        return albums.get(albumId);
    }

    public Map<String, JSONObject> getImageMap(String albumId) {
        return images.get(albumId);
    }

    public List<JSONObject> getImagesFromAlbum(String albumId) {
        return new ArrayList<JSONObject>(images.get(albumId).values());
    }

    public List<JSONObject> getAlbums() {
        return new ArrayList<JSONObject>(albums.values());
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

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isEmptyCache() {
        return emptyCache;
    }

    public void setEmptyCache(boolean emptyCache) {
        this.emptyCache = emptyCache;
    }
}
