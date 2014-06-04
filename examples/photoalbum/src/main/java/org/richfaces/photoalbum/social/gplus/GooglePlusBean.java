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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
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
@SessionScoped
public class GooglePlusBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    Event<ErrorEvent> error;

    @Inject
    GooglePlusAlbumCache gpac;

    private JSONObject userInfo;

    private List<JSONObject> userAlbums;

    public void setUserInfoJSON(String userJson) {
        try {
            userInfo = new JSONObject(userJson);
        } catch (JSONException e) {
            error.fire(new ErrorEvent("Error", e.getMessage()));
        }
    }

    public JSONObject getUserInfo() {
        return userInfo;
    }

    public String getUserId() {
        return userInfo != null ? userInfo.optString("id", "1") : "1";
    }

    public String getName() {
        JSONObject name = userInfo.optJSONObject("name");

        return name.optString("givenName") + " " + name.optString("familyName");
    }

    public List<JSONObject> getUserAlbums() {
        return userAlbums;
    }

    public void setUserAlbumsJSON(String albumJson) {
        JSONArray ja = new JSONArray();
        this.userAlbums = new ArrayList<JSONObject>();

        try {
            ja = new JSONArray(albumJson);

            for (int i = 0; i < ja.length(); i++) {
                userAlbums.add(ja.getJSONObject(i));
            }

            gpac.storeAlbums(userAlbums);
        } catch (JSONException e) {
            error.fire(new ErrorEvent("Error", e.getMessage()));
        }
    }

    public List<String> getUserAlbumIds() {
        List<String> ids = new ArrayList<String>();

        for (JSONObject jo : userAlbums) {
            ids.add(getUserId() + ":" + jo.optString("id"));
        }

        return ids;
    }
}
