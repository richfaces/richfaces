package org.richfaces.photoalbum.social;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;
import org.richfaces.log.Logger;

@Named
@SessionScoped
public class FacebookBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String service;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    Logger log;

    public void redirectToAuthorizationURL(String url) throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(url);
    }

    private JSONArray json;

    private JSONObject userInfo;

    public JSONArray getJson() {
        return json;
    }

    public void setJson(JSONArray json) {
        this.json = json;
    }

    public List<JSONObject> getAvailableAlbums() {
        List<JSONObject> albums = new ArrayList<JSONObject>();

        if (json != null) {
            for (int i = 0; i < json.length(); i++) {
                try {
                    albums.add(json.getJSONObject(i));
                } catch (JSONException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return albums;
    }

    public JSONObject getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(JSONObject userInfo) {
        this.userInfo = userInfo;
    }
}
