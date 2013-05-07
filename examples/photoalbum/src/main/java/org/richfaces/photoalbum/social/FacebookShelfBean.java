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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;

@Named
@SessionScoped
public class FacebookShelfBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name = "Facebook albums";
    private String description = "Albums retrieved from Facebook";

    private Map<String, JSONObject> albums;
    private Map<String, Map<String, JSONObject>> images;

    private String currentAlbumId;
    private String currentImageId;

    private int imageSize;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setAlbums(Object json) throws JSONException {
        if (((String) json).equals("{}")) {
            albums = null;
            images = null;
            return;
        }
        loadAlbums(new JSONArray((String) json));
    }

    public void loadAlbums(JSONArray ja) throws JSONException {
        images = new HashMap<String, Map<String, JSONObject>>();
        albums = new HashMap<String, JSONObject>();
        imageSize = 0;

        String albumId;
        JSONObject jo;

        for (int i = 0; i < ja.length(); i++) {
            jo = ja.getJSONObject(i);
            if (jo.has("aid")) {
                albumId = jo.getString("aid");
                images.put(albumId, null);
                albums.put(albumId, jo);

                imageSize += jo.getInt("size");
            }
        }
    }

    public void setAlbumImages(String json) throws JSONException {
        JSONArray ja = new JSONArray(json);

        if (ja.length() == 0) {
            return;
        }

        String albumId = ja.getJSONObject(0).getString("aid");
        setCurrentAlbumId(albumId);

        loadAlbumImages(albumId, ja);
    }

    public void loadAlbumImages(String albumId, JSONArray ja) throws JSONException {
        if (!images.containsKey(albumId)) {
            return;
        }

        String imageId;
        JSONObject jo;

        images.put(albumId, new HashMap<String, JSONObject>());
        Map<String, JSONObject> album = images.get(albumId);

        for (int i = 0; i < ja.length(); i++) {
            jo = ja.getJSONObject(i);
            if (jo.has("pid")) {
                imageId = jo.getString("pid");
                album.put(imageId, jo);
            }
        }
    }

    public List<JSONObject> getAllAlbums() { // different name not to connect with setter
        if (albums == null) {
            return null;
        }
        return new ArrayList<JSONObject>(albums.values());
    }

    public JSONObject getAlbum(String albumId) {
        return albums.get(albumId);
    }

    public boolean isShelfLoaded() {
        return albums != null && !albums.isEmpty();
    }

    public boolean isAlbumLoaded(String albumId) {
        return images.get(albumId) != null;
    }

    public Map<String, JSONObject> getImages(String albumId) {
        return images.get(albumId);
    }

    public JSONObject getImage(String albumId, String imageId) {
        if (!images.containsKey(albumId) || !images.get(albumId).containsKey(imageId)) {
            return null;
        }

        return getImages(albumId).get(imageId);
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
        if (currentAlbumId == null) {
            return null;
        }

        return getAlbum(currentAlbumId);
    }

    public List<JSONObject> getCurrentAlbumImages() {
        if (currentAlbumId == null) {
            return null;
        }

        return new ArrayList<JSONObject>(images.get(currentAlbumId).values());
    }

    public JSONObject getCurrentImage() {
        if (currentAlbumId == null || currentImageId == null) {
            return null;
        }

        return getImage(currentAlbumId, currentImageId);
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }
}
