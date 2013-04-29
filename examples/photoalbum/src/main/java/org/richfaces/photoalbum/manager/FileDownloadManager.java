package org.richfaces.photoalbum.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;
import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IAlbumAction;
import org.richfaces.photoalbum.service.PhotoAlbumException;
import org.richfaces.photoalbum.util.FileHandler;

@Named
@SessionScoped
public class FileDownloadManager implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3815919720565571122L;

    @Inject
    FileUploadManager fileUploadManager;

    @Inject
    AlbumManager albumManager;

    @Inject
    IAlbumAction albumAction;

    @Inject
    Model model;

    private Logger log = Logger.getLogger("FDManager");

    private List<String> imageUrls;

    //private boolean pollEnabled = false;

    private Album album;
    private String albumName;

    private int size = 0;
    private int current = 0;

    public void setImages(String json) throws JSONException {
        JSONArray ja = new JSONArray(json);

        if (ja.length() == 0) {
            return;
        }

        imageUrls = new ArrayList<String>();

        for (int i = 0; i < ja.length(); i++) {
            imageUrls.add(ja.getJSONObject(i).getString("src_big"));
        }

        //setPollEnabled(true);
    }

    public void createAlbum(String name) {
        album = new Album();
        album.setName(name);
        album.setEvent(model.getSelectedEvent());

        albumManager.addAlbum(album);
    }

    public void downloadImages() {
//        setPollEnabled(true);
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }

        setSize(imageUrls.size());
        setCurrent(0);

        createAlbum(albumName);
        album = albumAction.resetAlbum(album);

        for (String imageUrl : imageUrls) {
            uploadImage(imageUrl, album.getName() + imageUrl.lastIndexOf(Constants.DOT), album);
            setCurrent(getCurrent() + 1);
        }

        try {
            albumAction.editAlbum(album);

            album = albumAction.resetAlbum(album);
        } catch (PhotoAlbumException pae) {
            log.log(Level.INFO, "error saving album", pae);
        }
        
        //setPollEnabled(false);
    }
    
    private void uploadImage(String imageUrl, String imageName, Album album) {
        File file = new File(imageName);
        int i;
        try {
            URL url = new URL(imageUrl);
            URLConnection con = url.openConnection();
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file.getName()));
            while ((i = bis.read()) != -1) {
                bos.write(i);
            }
            bos.flush();
            bis.close();
        } catch (MalformedInputException malformedInputException) {
            log.log(Level.ALL, "error", malformedInputException);
        } catch (IOException ioException) {
            log.log(Level.ALL, "error", ioException);
        }        
        
        fileUploadManager.uploadFile(new FileHandler(file), album);
    }

//    public boolean isPollEnabled() {
//        return pollEnabled;
//    }
//
//    public void setPollEnabled(boolean pollEnabled) {
//        this.pollEnabled = pollEnabled;
//    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public int getProgress() {
        if (size == 0) {
            return 0;
        }
        return (current / size) * 100;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
