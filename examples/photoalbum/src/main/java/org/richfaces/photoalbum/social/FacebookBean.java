package org.richfaces.photoalbum.social;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.agorava.Facebook;
import org.agorava.FacebookServicesHub;
import org.agorava.core.cdi.AgoravaExtension;
import org.jboss.logging.Logger;
import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;

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

    @Inject
    @Facebook
    FacebookServicesHub serviceHub;

    @Inject
    Logger log;

    public FacebookServicesHub getServiceHub() {
        return serviceHub;
    }

    public void redirectToAuthorizationURL(String url) throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(url);
    }

    public String initSession() {
        return serviceHub.getService().getAuthorizationUrl();
    }

    public void init() throws IOException {
        AgoravaExtension.setMultiSession(false);
        // although set false by default, we need to set it manually here
        redirectToAuthorizationURL(initSession());
    }

    public void connect() {
        serviceHub.getService().initAccessToken();
    }

    private JSONArray json;

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
                    albums.add(new JSONObject(json.get(i).toString()));
                } catch (JSONException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return albums;
    }
}
