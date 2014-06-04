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
package org.richfaces.photoalbum.manager;

/**
 * Class encapsulated all functionality, related to working with the file system.
 *
 * @author Andrey Markhel
 */
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.photoalbum.model.Album;
import org.richfaces.photoalbum.model.Image;
import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.model.event.AlbumEvent;
import org.richfaces.photoalbum.model.event.ErrorEvent;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.ImageEvent;
import org.richfaces.photoalbum.model.event.ShelfEvent;
import org.richfaces.photoalbum.model.event.SimpleEvent;
import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.FileHandler;
import org.richfaces.photoalbum.util.FileManipulation;
import org.richfaces.photoalbum.util.ImageDimension;

import com.google.common.io.Files;

@Named
@ApplicationScoped
public class FileManager {

    @Inject
    private File uploadRoot;

    @Inject
    private String uploadRootPath;

    @Inject
    UserBean userBean;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    Event<ErrorEvent> error;

    /**
     * Method, that invoked at startup application. Used to determine where application will be write new images. This method
     * set uploadRoot field - it is reference to the file where images will be copied.
     */
    // @PostConstruct
    // public void create() {
    // }

    /**
     * This method used to get reference to the file with the specified relative path to the uploadRoot field
     *
     * @param path - relative path of file
     * @return File reference
     */
    public File getFileByPath(String path) {
        if (this.uploadRoot != null) {
            File result = new File(this.uploadRoot, path);
            try {
                final String resultCanonicalPath = result.getCanonicalPath();
                if (!resultCanonicalPath.startsWith(this.uploadRootPath)) {
                    result = null;
                }
                return result;
            } catch (IOException e) {
                error.fire(new ErrorEvent("no file found"));
                result = null;
            }
            return result;
        }
        return null;
    }

    /**
     * This method observes <code>Constants.ALBUM_DELETED_EVENT</code> and invoked after the user delete album. This method
     * delete album directory from the disk
     *
     * @param album - deleted album
     * @param path - relative path of the album directory
     *
     */
    public void onAlbumDeleted(@Observes @EventType(Events.ALBUM_DELETED_EVENT) AlbumEvent ae) {
        if (!userBean.isLoggedIn()) {
            return;
        }
        deleteDirectory(ae.getPath());
    }

    /**
     * This method observes <code>Constants.SHELF_DELETED_EVENT</code> and invoked after the user delete her shelf This method
     * delete shelf directory from the disk
     *
     * @param shelf - deleted shelf
     * @param path - relative path of the shelf directory
     */
    public void onShelfDeleted(@Observes @EventType(Events.SHELF_DELETED_EVENT) ShelfEvent se) {
        if (!userBean.isLoggedIn()) {
            return;
        }
        deleteDirectory(se.getPath());
    }

    /**
     * This method observes <code>Constants.USER_DELETED_EVENT</code> and invoked after the user was deleted(used in livedemo to
     * prevent flooding) This method delete user directory from the disk
     *
     * @param user - deleted user
     * @param path - relative path of the user directory
     */
    public void onUserDeleted(@Observes @EventType(Events.USER_DELETED_EVENT) SimpleEvent se) {
        deleteDirectory(userBean.getUser().getPath());
    }

    /**
     * This method observes <code>SHELF_ADDED_EVENT</code> and invoked after the user add new shelf This method add shelf
     * directory to the disk
     *
     * @param shelf - added shelf
     */
    public void onShelfAdded(@Observes @EventType(Events.SHELF_ADDED_EVENT) ShelfEvent se) {
        File directory = getFileByPath(se.getShelf().getPath());
        FileManipulation.addDirectory(directory);
    }

    /**
     * This method observes <code>ALBUM_ADDED_EVENT</code> and invoked after the user add new album This method add album
     * directory to the disk
     *
     * @param album - added album
     */
    public void onAlbumAdded(@Observes @EventType(Events.ALBUM_ADDED_EVENT) AlbumEvent ae) {
        File directory = getFileByPath(ae.getAlbum().getPath());
        FileManipulation.addDirectory(directory);
    }

    /**
     * This method invoked after user set new avatar icon
     *
     * @param avatarData - avatar file
     * @param user - user, that add avatar
     */
    public boolean saveAvatar(File avatarData, User user) {
        String avatarPath = File.separator + user.getLogin() + File.separator + Constants.AVATAR_JPG;
        createDirectoryIfNotExist(avatarPath);
        try {
            InputStream is = new FileInputStream(avatarData);
            return writeFile(avatarPath, is, "", Constants.AVATAR_SIZE, true);
        } catch (IOException ioe) {
            error.fire(new ErrorEvent("error saving avatar"));
            return false;
        }
    }

    /**
     * This method observes <code>Constants.IMAGE_DELETED_EVENT</code> and invoked after the user delete her image This method
     * delete image and all thumbnails of this image from the disk
     *
     * @param image - deleted image
     * @param path - relative path of the image file
     */
    public void deleteImage(@Observes @EventType(Events.IMAGE_DELETED_EVENT) ImageEvent ie) {
        if (!userBean.isLoggedIn()) {
            return;
        }
        for (ImageDimension d : ImageDimension.values()) {
            FileManipulation.deleteFile(getFileByPath(transformPath(ie.getPath(), d.getFilePostfix())));
        }
    }

    /**
     * This method invoked after user upload new image
     *
     * @param fileName - new relative path to the image file
     * @param tempFilePath - absolute path to uploaded image
     * @throws IOException
     */
    public boolean addImage(String fileName, FileHandler fileHandler) throws IOException {
        if (!userBean.isLoggedIn()) {
            return false;
        }
        createDirectoryIfNotExist(fileName);
        for (ImageDimension d : ImageDimension.values()) {
            try {
                InputStream in = fileHandler.getInputStream();
                if (!writeFile(fileName, in, d.getFilePostfix(), d.getX(), true)) {
                    return false;
                }
                in.close();
            } catch (IOException ioe) {
                error.fire(new ErrorEvent("Error", "error saving image: " + ioe.getMessage()));
                return false;
            }
        }

        return true;
    }

    /**
     * This method used to transform one path to another. For example you want get path of the file with dimensioms 80 of image
     * with path /user/1/2/image.jpg, this method return /user/1/2/image_substitute.jpg
     *
     * @param target - path to transform
     * @param substitute - new 'addon' to the path
     */
    public String transformPath(String target, String substitute) {
        if (target.length() < 2 || target.lastIndexOf(Constants.DOT) == -1) {
            return "";
        }
        final String begin = target.substring(0, target.lastIndexOf(Constants.DOT));
        final String end = target.substring(target.lastIndexOf(Constants.DOT));
        return begin + substitute + end;
    }

    /**
     * This method used to get reference to the file with the absolute path
     *
     * @param path - absolute path of file
     * @return File reference
     */
    public File getFileByAbsolutePath(String path) {
        return new File(path);
    }

    /**
     * This utility method used to determine if the directory with specified relative path exist
     *
     * @param path - absolute path of directory
     * @return File reference
     */
    public boolean isDirectoryPresent(String path) {
        final File file = getFileByPath(path);
        return file.exists() && file.isDirectory();
    }

    /**
     * This utility method used to determine if the file with specified relative path exist
     *
     * @param path - absolute path of file
     * @return File reference
     */
    public boolean isFilePresent(String path) {
        final File file = getFileByPath(path);
        return file.exists();
    }

    /**
     * This method observes <code>Constants.ALBUM_DRAGGED_EVENT</code> and invoked after the user dragged album form one shelf
     * to the another. This method rename album directory to the new directory
     *
     * @param album - dragged album
     * @param pathOld - old path of album directory
     */
    public void renameAlbumDirectory(@Observes @EventType(Events.ALBUM_DRAGGED_EVENT) AlbumEvent ae) {
        String pathOld = ae.getPath();
        Album album = ae.getAlbum();
        File file = getFileByPath(pathOld);
        File file2 = getFileByPath(album.getPath());
        if (file2.exists()) {
            if (file2.isDirectory()) {
                FileManipulation.deleteDirectory(file2, false);
            } else {
                FileManipulation.deleteFile(file2);
            }
        }

        try {
            Files.createParentDirs(file2);
        } catch (IOException ioe) {
            error.fire(new ErrorEvent("Error moving file", ioe.getMessage()));
        }

        file.renameTo(file2);
    }

    /**
     * This method observes <code>Constants.IMAGE_DRAGGED_EVENT</code> and invoked after the user dragged image form one album
     * to the another. This method rename image file and all thumbnails to the new name
     *
     * @param image - dragged image
     * @param pathOld - old path of image file
     */
    public void renameImageFile(@Observes @EventType(Events.IMAGE_DRAGGED_EVENT) ImageEvent ie) {
        File file = null;
        File file2 = null;

        String pathOld = ie.getPath();
        Image image = ie.getImage();
        for (ImageDimension dimension : ImageDimension.values()) {
            file = getFileByPath(transformPath(pathOld, dimension.getFilePostfix()));
            file2 = getFileByPath(transformPath(image.getFullPath(), dimension.getFilePostfix()));
            if (file2.exists()) {
                if (file2.isDirectory()) {
                    FileManipulation.deleteDirectory(file2, false);
                } else {
                    FileManipulation.deleteFile(file2);
                }
            }
            file.renameTo(file2);
        }
    }

    private boolean writeFile(String newFileName, InputStream inputStream, String pattern, int size, boolean includeUploadRoot) {
        BufferedImage bsrc = null;
        String format = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(newFileName).split("/")[1];
        try {
            // Read file form disk
            bsrc = FileManipulation.bitmapToImage(inputStream, format);
        } catch (IOException e1) {
            error.fire(new ErrorEvent("Error", "error reading file<br/>" + e1.getMessage()));
            return false;
        }
        int resizedParam = bsrc.getWidth() > bsrc.getHeight() ? bsrc.getWidth() : bsrc.getHeight();
        double scale = (double) size / resizedParam;
        Double widthInDouble = ((Double) scale * bsrc.getWidth());
        int width = widthInDouble.intValue();
        Double heightInDouble = ((Double) scale * bsrc.getHeight());
        int height = heightInDouble.intValue();
        // Too small picture or original size
        if (width > bsrc.getWidth() || height > bsrc.getHeight() || size == 0) {
            width = bsrc.getWidth();
            height = bsrc.getHeight();
        }
        // scale image if need
        BufferedImage bdest = FileManipulation
            .getScaledInstance(bsrc, width, height, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
        // Determine new path of image file
        String dest = includeUploadRoot ? this.uploadRootPath + transformPath(newFileName, pattern) : transformPath(
            newFileName, pattern);
        try {
            // save to disk
            FileManipulation.imageToBitmap(bdest, dest, format);
        } catch (IOException ex) {
            error.fire(new ErrorEvent("Error", "error saving image to disc: " + ex.getMessage()));
            return false;
        }
        return true;
    }

    private void deleteDirectory(String directory) {
        final File file = getFileByPath(directory);
        FileManipulation.deleteDirectory(file, false);
    }

    private void createDirectoryIfNotExist(String fileNameNew) {
        final int lastIndexOf = fileNameNew.lastIndexOf(File.separator);
        if (lastIndexOf > 0) {
            final String directory = fileNameNew.substring(0, lastIndexOf);
            final File file = getFileByPath(directory);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }
}