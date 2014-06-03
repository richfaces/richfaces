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

    private boolean needsUpdate = true;

    private String currentAlbumId;
    private String currentImageId;

    public void setAll(String json) {
        try {
            JSONObject jo = new JSONObject(json);
            if (!jo.has("albums")) {
                return;
            }

            storeAlbums(jo.getJSONArray("albums"), jo.getJSONArray("covers"));
            storeImagesToAlbum(jo.getJSONArray("images"));
            setNeedsUpdate(false);
        } catch (JSONException e) {
            error.fire(new ErrorEvent("Error: ", e.getMessage()));
        }
    }

    public void setAlbums(String json) {
        try {
            JSONObject jo = new JSONObject(json);
            if (!jo.has("albums")) {
                return;
            }

            storeAlbums(jo.getJSONArray("albums"), jo.getJSONArray("covers"));
        } catch (JSONException e) {
            error.fire(new ErrorEvent("Error: ", e.getMessage()));
        }
    }

    public void storeAlbums(JSONArray jAlbums, JSONArray jCovers) {
        storeAlbums(jAlbums, jCovers, false);
    }

    public void storeAlbums(JSONArray jAlbums, JSONArray jCovers, boolean rewrite) {
        String albumId;
        JSONObject jo;
        JSONObject jc;
        int offset = 0;

        try {
            for (int i = 0; i < jAlbums.length(); i++) {
                jo = jAlbums.getJSONObject(i);
                jc = jCovers.getJSONObject(i - offset);

                if (!jo.has("id")) {
                    error.fire(new ErrorEvent("Error, object does not contain albums"));
                }

                albumId = jo.getString("id");

                if (jc.getString("pid").equals(jo.getString("cpid"))) {
                    jo.put("coverUrl", jc.getString("coverUrl"));
                } else { // album without cover -> empty
                    offset++;
                    jo.put("coverUrl", "resources/img/shell/frame_photo_120.png");
                    jo.put("empty", true);
                }

                if (albums.containsKey(albumId) && !rewrite) {
                    // the album has already been loaded
                    return;
                } else if (rewrite) {
                    albums.remove(albumId);
                }
                images.put(albumId, new HashMap<String, JSONObject>());
                albums.put(albumId, jo);
            }
        } catch (JSONException je) {
            error.fire(new ErrorEvent("Error", je.getMessage()));
        }
    }

    public void storeImagesToAlbum(JSONArray ja) {

        String imageId = "";
        String albumId = "";
        JSONObject jo;

        try {
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);

                if (!jo.has("albumId") || !jo.has("id")) {
                    error.fire(new ErrorEvent("Error, object does not contain images"));

                }

                albumId = jo.getString("albumId");
                imageId = jo.getString("id");

                images.get(albumId).put(imageId, jo);
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

    public boolean isAlbumLoaded(String albumId) {
        return albums.get(albumId) != null && (albums.get(albumId).optBoolean("empty", false) || !images.get(albumId).isEmpty());
    }

    // takes a list of id's from an event
    public boolean areAlbumsLoaded(List<String> albumIds) {
        if (needsUpdate) {
            return false;
        }

        if (albumIds != null) {
            for (String id : albumIds) {
                if (!isAlbumLoaded(id)) {
                    setNeedsUpdate(true);
                    return false;
                }
            }
        }

        setNeedsUpdate(false);
        return true;
    }

    public boolean isNeedsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
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
}
