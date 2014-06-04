package org.richfaces.photoalbum.util;

import org.richfaces.json.JSONObject;
import org.richfaces.photoalbum.model.Image;

/**
 * Class to handle local (photoalbum.domain.Image) and remote images (JSONobject)
 * 
 * @author mpetrov
 * 
 */
public class ImageHandler {
    private Image localImage;
    private JSONObject remoteImage;

    private int type;

    public static final int LOCAL = 1;
    public static final int FACEBOOK = 2;
    public static final int GOOGLE = 3;

    public ImageHandler(Object o) {
        setImage(o);
    }
    
    public void setImage(Object o) {
        if (o == null) {
            return;
        }
        if (o instanceof Image) {
            localImage = (Image) o;
            type = LOCAL;
        } else if (o instanceof JSONObject) {
            remoteImage = (JSONObject) o;
            String imageType = remoteImage.optString("type");
            
            if (imageType.equals("facebook")) {
                type = FACEBOOK;
            } else if (imageType.equals("google")) {
                type = GOOGLE;
            }
        }
    }

    public boolean isLocalImage() {
        return type == LOCAL;
    }
    
    public boolean isFacebookImage() {
        return type == FACEBOOK;
    }
    
    public boolean isGoogleImage() {
        return type == GOOGLE;
    }

    public Object getImage() {
        return isLocalImage() ? localImage : remoteImage;
    }

    public String getName() {
        return isLocalImage() ? localImage.getName() : remoteImage.optString("name");
    }

    public String getThumbUrl() {
        return isLocalImage() ? localImage.getFullPath() : remoteImage.optString("thumbUrl");
    }
    
    public String getUrl() {
        return isLocalImage() ? localImage.getFullPath() : remoteImage.optString("url");
    }

    public String getId() {
        return isLocalImage() ? localImage.getId().toString() : remoteImage.optString("id");
    }

    public String getAlbumId() {
        return isLocalImage() ? localImage.getAlbum().getId().toString() : remoteImage.optString("fullAlbumId");
    }
    
    public int getType() {
        return type;
    }

    public String getDescription() {
        return isLocalImage() ? localImage.getDescription() : "";
    }
}
