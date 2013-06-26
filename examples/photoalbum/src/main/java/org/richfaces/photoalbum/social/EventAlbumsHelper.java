package org.richfaces.photoalbum.social;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.json.JSONObject;
import org.richfaces.photoalbum.manager.Model;
import org.richfaces.photoalbum.social.gplus.GooglePlusAlbumCache;

@Named
@SessionScoped
public class EventAlbumsHelper implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    Model model;

    @Inject
    GooglePlusAlbumCache gpac;

    private List<String> emptyAlbumIds = new LinkedList<String>();
    private List<String> eventAlbumIds = new ArrayList<String>();

    private boolean pollEnabled;

    public void init() {
        emptyAlbumIds = new LinkedList<String>();
        eventAlbumIds = new ArrayList<String>();

        eventAlbumIds = model.getSelectedEvent().getGooglePlusAlbumIds();

        for (String fullId : eventAlbumIds) {
            if (!gpac.isAlbumLoaded(fullId)) {
                emptyAlbumIds.add(fullId);
            }
        }

        setPollEnabled(emptyAlbumIds.size() > 0);
    }

    public boolean isEventEmpty() {
        return eventAlbumIds.size() == 0;
    }

    public boolean isAllLoaded() {
        return emptyAlbumIds.size() == 0 && !isEventEmpty();
    }

    public List<JSONObject> getEventPhotos() {

        List<JSONObject> photos = new ArrayList<JSONObject>();
        for (String albumId : eventAlbumIds) {
            if (gpac.isAlbumLoaded(albumId)) {
                photos.addAll(gpac.getImagesOfAlbum(albumId).values());
            }
        }
        return photos;
    }

    public String getNextId() {
        if (emptyAlbumIds.size() == 0) {
            return "0";
        }

        return emptyAlbumIds.get(0);
    }

    public void loadNext() {
        if (!gpac.isAlbumLoaded(getNextId())) {
            return;
        }

        emptyAlbumIds.remove(getNextId());

        setPollEnabled(emptyAlbumIds.size() != 0);
    }

    public boolean isPollEnabled() {
        return pollEnabled;
    }

    public void setPollEnabled(boolean pollEnabled) {
        this.pollEnabled = pollEnabled;
    }
}
