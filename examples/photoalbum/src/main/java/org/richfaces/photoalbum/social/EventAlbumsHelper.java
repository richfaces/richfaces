package org.richfaces.photoalbum.social;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.json.JSONObject;
import org.richfaces.photoalbum.manager.Model;
import org.richfaces.photoalbum.model.Event;
import org.richfaces.photoalbum.model.Image;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.SimpleEvent;
import org.richfaces.photoalbum.social.facebook.FacebookAlbumCache;
import org.richfaces.photoalbum.social.gplus.GooglePlusAlbumCache;
import org.richfaces.photoalbum.util.ImageHandler;
import org.richfaces.photoalbum.util.converters.ListConverter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

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

    @Inject
    FacebookAlbumCache fac;

    private String fbAlbumIds;

    private List<String> emptyFacebookIds = new LinkedList<String>();
    private List<String> facebookAlbumIds = new ArrayList<String>();
    private List<String> emptyGoogleIds = new LinkedList<String>();
    private List<String> googleAlbumIds = new ArrayList<String>();

    private List<ImageHandler> images  = new ArrayList<ImageHandler>();

    private boolean pollEnabled;

    private boolean facebookNeedsUpdate;
    private boolean googleNeedsUpdate;

    /**
     * Initializes the helper - checks if remote albums are loaded in their caches and prepares list of those that are not
     *
     * @param se - event triggered by user selecting an Event to view
     */
    public void init(@Observes @EventType(Events.EVENT_DISPLAYED_EVENT) SimpleEvent se) {
        images = new ArrayList<ImageHandler>();
        setFacebookNeedsUpdate(false);
        setGoogleNeedsUpdate(false);

        Event event = model.getSelectedEvent();

        // add local images into the list
        for (Image i : event.getShelf().getImages()) {
            images.add(new ImageHandler(i));
        }

        /*
         * Facebook
         */

        List<String> facebookIds = event.getFacebookAlbumIds();

        if (facebookIds.size() != 0) {
            // check if albums are loaded
            emptyFacebookIds = new ArrayList<String>(Collections2.filter(facebookIds, new Predicate<String>() {

                @Override
                public boolean apply(String id) {
                    return !fac.isAlbumLoaded(id);
                }
            }));

            facebookAlbumIds = new ArrayList<String>(Collections2.filter(facebookIds, new Predicate<String>() {

                @Override
                public boolean apply(String id) {
                    return fac.isAlbumLoaded(id);
                }
            }));

            // set up the id
            setFbAlbumIds(ListConverter.sListToString(emptyFacebookIds));

            // load the loaded images
            for (String lId : facebookAlbumIds) {
                for (JSONObject fImage : fac.getImagesFromAlbum(lId)) {
                    images.add(new ImageHandler(fImage));
                }
            }

            setFacebookNeedsUpdate(emptyFacebookIds.size() > 0);
        }

        /*
         * Google+
         */

        emptyGoogleIds = new LinkedList<String>();
        googleAlbumIds = new ArrayList<String>();

        googleAlbumIds = event.getGooglePlusAlbumIds();

        if (googleAlbumIds.size() != 0) {

            for (String fullId : googleAlbumIds) {
                if (!gpac.isAlbumLoaded(fullId)) {
                    emptyGoogleIds.add(fullId);
                } else {
                    for (JSONObject gImage : gpac.getImagesOfAlbum(fullId).values()) {
                        images.add(new ImageHandler(gImage));
                    }
                }
            }
        }

        setGoogleNeedsUpdate(emptyGoogleIds.size() > 0);
        setPollEnabled(emptyGoogleIds.size() > 0);
    }

    private void addGoogleImages(String albumId) {
        for (JSONObject jo : gpac.getImagesOfAlbum(albumId).values()) {
            images.add(new ImageHandler(jo));
        }
    }

    public void loadFBImages() {
        for (String albumId : emptyFacebookIds) {
            for (JSONObject jo : fac.getImagesFromAlbum(albumId)) {
                images.add(new ImageHandler(jo));
            }
        }
    }

    public boolean isEventEmpty() {
        return googleAlbumIds.size() == 0;
    }

    public boolean isAllLoaded() {
        return emptyGoogleIds.size() == 0 && !isEventEmpty();
    }

    public String getNextId() {
        if (emptyGoogleIds.size() == 0) {
            return "0";
        }

        return emptyGoogleIds.get(0);
    }

    public void loadNext() {
        String currentId = getNextId();

        if (!gpac.isAlbumLoaded(currentId)) {
            return;
        }

        addGoogleImages(currentId);

        emptyGoogleIds.remove(getNextId());
        setPollEnabled(emptyGoogleIds.size() != 0);
    }

    public boolean isPollEnabled() {
        return pollEnabled;
    }

    public void setPollEnabled(boolean pollEnabled) {
        this.pollEnabled = pollEnabled;
    }

    public List<ImageHandler> getImages() {
        Collections.sort(images, new Comparator<ImageHandler>() {

            @Override
            public int compare(ImageHandler o1, ImageHandler o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return images;
    }

    public void setImages(List<ImageHandler> images) {
        this.images = images;
    }

    public String getFbAlbumIds() {
        return fbAlbumIds;
    }

    public void setFbAlbumIds(String fbAlbumIds) {
        this.fbAlbumIds = fbAlbumIds;
    }

    public boolean isFacebookNeedsUpdate() {
        return facebookNeedsUpdate;
    }

    public void setFacebookNeedsUpdate(boolean facebookNeedsUpdate) {
        this.facebookNeedsUpdate = facebookNeedsUpdate;
    }

    public boolean isGoogleNeedsUpdate() {
        return googleNeedsUpdate;
    }

    public void setGoogleNeedsUpdate(boolean googleNeedsUpdate) {
        this.googleNeedsUpdate = googleNeedsUpdate;
    }
}
