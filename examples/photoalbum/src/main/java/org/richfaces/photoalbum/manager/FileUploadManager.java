/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.photoalbum.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.model.UploadedFile;
import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.event.EventType;
import org.richfaces.photoalbum.event.Events;
import org.richfaces.photoalbum.event.ImageEvent;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IImageAction;
import org.richfaces.photoalbum.service.PhotoAlbumException;
import org.richfaces.photoalbum.ui.FileWrapper;
import org.richfaces.photoalbum.util.FileHandler;
import org.richfaces.photoalbum.util.Preferred;
import org.richfaces.ui.input.fileUpload.FileUploadEvent;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.jpeg.JpegDirectory;

/**
 * Class encapsulated all functionality, related to file-upload process.
 *
 * @author Andrey Markhel
 */

@Named
@RequestScoped
public class FileUploadManager implements Serializable {

    private static final long serialVersionUID = 4969087557225414955L;

    @Inject
    IImageAction imageAction;

    // @In(required = true, scope = ScopeType.CONVERSATION)
    // @Out(scope = ScopeType.CONVERSATION)
    @Inject
    FileWrapper fileWrapper;

    @Inject
    Model model;

    @Inject
    @Preferred
    User user;

    @Inject
    private FileManager fileManager;

    @Inject
    @EventType(Events.IMAGE_ADDED_EVENT)
    Event<ImageEvent> imageEvent;

    Logger log = Logger.getLogger("FUManager");

    /**
     * Listenet, that invoked during file upload process. Only registered users can upload images.
     *
     * @param event - event, indicated that file upload started
     */
    // @AdminRestricted
    public void listener(FileUploadEvent event) {
        if (user == null) {
            return;
        }
        UploadedFile file = event.getUploadedFile();
        // Construct image from item
        uploadFile(new FileHandler(file), model.getSelectedAlbum());
    }

    public void uploadFile(FileHandler fileHandler, Album album) {
        Image image = constructImage(fileHandler);
        try {
            // Extract metadata(size, camera model etc..)
            extractMetadata(fileHandler, image);
        } catch (Exception e1) {
            addError(fileHandler, image, Constants.FILE_PROCESSING_ERROR);
            return;
        }

        image.setAlbum(album);
        if (image.getAlbum() == null) {
            addError(fileHandler, image, Constants.NO_ALBUM_TO_DOWNLOAD_ERROR);
            return;
        }
        try {
            // Check if image with given name already exist
            if (imageAction.isImageWithThisPathExist(image.getAlbum(), image.getPath())) {
                // If exist generate new path for image
                String newPath = generateNewPath(image);
                image.setPath(newPath);
                image.setName(newPath);
            }
            // Save to database
            imageAction.addImage(image);
        } catch (Exception e) {
            addError(fileHandler, image, Constants.IMAGE_SAVING_ERROR);
            return;
        }
        try {
            // Save to disk
            if (!fileManager.addImage(image.getFullPath(), fileHandler)) {
                addError(fileHandler, image, Constants.FILE_SAVE_ERROR);
                return;
            }
        } catch (IOException ioe) {
            log.log(Level.INFO, "error", ioe);
            addError(image, Constants.FILE_SAVE_ERROR + " - " + ioe.getMessage());
        }

        // Prepare to show in UI
        fileWrapper.setComplete(false);
        fileWrapper.getFiles().add(image);
        imageEvent.fire(new ImageEvent(image));
        // Delete temporary file
        try {
            fileHandler.delete();
        } catch (IOException ioe) {
            log.log(Level.INFO, "error", ioe);
            addError(image, "Error deleting file - " + ioe.getMessage());
        }
    }

    private String generateNewPath(Image image) throws PhotoAlbumException {
        String path = image.getPath().substring(0, image.getPath().lastIndexOf(Constants.DOT));
        Long countCopies = imageAction.getCountIdenticalImages(image.getAlbum(), path) + 1;
        String newPath = fileManager.transformPath(image.getPath(), "_" + countCopies);
        return newPath;
    }

    private void addError(FileHandler fileHandler, Image image, String error) {
        addError(image, error);
        try {
            fileHandler.delete();
        } catch (IOException e) {
            addError(image, e.getMessage());
        }
    }

    private void addError(Image image, String error) {
        fileWrapper.onFileUploadError(image, error);
    }

    private Image constructImage(FileHandler fileHandler) {
        long size = fileHandler.getSize();
        String name = fileHandler.getName();

        Image image = new Image();
        image.setUploaded(new Date());
        image.setDescription(name);
        image.setName(name);
        image.setSize(size);
        image.setPath(name);
        image.setAllowComments(true);
        return image;
    }

    /*
     * NOTE: all the following classes may not work like they used to in the previous version; this is due to certain classes no
     * longer being part of the com.drew.* libraries
     */
    private void extractMetadata(FileHandler fileHandler, Image image) {
        InputStream in = null;
        try {
            in = fileHandler.getInputStream();
            Metadata metadata = JpegMetadataReader.readMetadata(in);
            Directory exifDirectory = metadata.getDirectory(ExifIFD0Directory.class);
            Directory jpgDirectory = metadata.getDirectory(JpegDirectory.class);
            if (exifDirectory != null) {
                setupCameraModel(image, exifDirectory);
                setupCreatedDate(image, exifDirectory);
                if (jpgDirectory != null) {
                    setupDimensions(image, exifDirectory, jpgDirectory);
                }
            }
        } catch (Exception e) {
            addError(fileHandler, image, Constants.IMAGE_SAVING_ERROR);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                addError(fileHandler, image, Constants.IMAGE_SAVING_ERROR + " - " + e.getMessage());
            }
        }
    }

    private void setupCreatedDate(Image image, Directory exifDirectory) throws MetadataException {
        if (exifDirectory.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
            Date time = exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            image.setCreated(time);
        }
    }

    private void setupDimensions(Image image, Directory exifDirectory, Directory jpgDirectory) {
        try {
            if (exifDirectory.containsTag(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH)
                && exifDirectory.containsTag(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT)) {
                int width = exifDirectory.getInt(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH);
                image.setWidth(width);
                int height = exifDirectory.getInt(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT);
                image.setHeight(height);
            } else {
                if (jpgDirectory.containsTag(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT)) {
                    int width = jpgDirectory.getInt(JpegDirectory.TAG_JPEG_IMAGE_WIDTH);
                    image.setWidth(width);
                    int height = jpgDirectory.getInt(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT);
                    image.setHeight(height);
                }
            }
        } catch (MetadataException e) {
            addError(image, Constants.IMAGE_SAVING_ERROR);
        }
    }

    private void setupCameraModel(Image image, Directory exifDirectory) {
        if (exifDirectory.containsTag(ExifIFD0Directory.TAG_MODEL)) {
            String cameraModel = exifDirectory.getString(ExifIFD0Directory.TAG_MODEL);
            image.setCameraModel(cameraModel);
        } else {
            image.setCameraModel("");
        }
    }
}