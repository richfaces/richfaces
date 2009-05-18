/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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
/**
 * Class encapsulated all functionality, related to working with the file system.
 *
 * @author Andrey Markhel
 */
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.util.FileUtils;
import org.richfaces.photoalbum.util.ImageDimension;

@Name("fileManager")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class FileManager {

    private File uploadRoot;
    
    private String uploadRootPath;
    
    /**
	 * Method, that invoked  at startup application. Used to determine where application will be write new images.
	 * This method set uploadRoot field - it is reference to the file where images will be copied.
	 */
    @Create
	public void create() {
		uploadRoot = (File)Component.getInstance(Constants.UPLOAD_ROOT_COMPONENT_NAME, ScopeType.APPLICATION);
		uploadRootPath = (String)Component.getInstance(Constants.UPLOAD_ROOT_PATH_COMPONENT_NAME, ScopeType.APPLICATION);
	}

    /**
	 * This method used to get reference to the file with the specified relative path to the uploadRoot field
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
                result = null;
            }
            return result;
        }
        return null;
    }

    /**
	 * This method observes <code>Constants.ALBUM_DELETED_EVENT</code> and invoked after the user delete album.
	 * This method delete album directory from the disk
	 * @param album - deleted album
	 * @param path - relative path of the album directory
	 * 
	 */
    @Restrict("#{s:hasRole('admin')}")
    @Observer(Constants.ALBUM_DELETED_EVENT)
    public void onAlbumDeleted(Album album, String path) {
    	deleteDirectory(path);
    }
    
    /**
	 * This method observes <code>Constants.SHELF_DELETED_EVENT</code> and invoked after the user delete her shelf
	 * This method delete shelf directory from the disk
	 * @param shelf - deleted shelf
	 * @param path - relative path of the shelf directory
	 */
    @Restrict("#{s:hasRole('admin')}")
    @Observer(Constants.SHELF_DELETED_EVENT)
    public void onShelfDeleted(Shelf shelf, String path) {
    	deleteDirectory(path);
    }
    
    /**
	 * This method observes <code>Constants.USER_DELETED_EVENT</code> and invoked after the user was deleted(used in livedemo to prevent flooding)
	 * This method delete user directory from the disk
	 * @param user - deleted user
	 * @param path - relative path of the user directory
	 */
    @Observer(Constants.USER_DELETED_EVENT)
    public void onUserDeleted(User user){
    	deleteDirectory(user.getPath());
    }

    /**
	 * This method observes <code>SHELF_ADDED_EVENT</code> and invoked after the user add new shelf
	 * This method add shelf directory to the disk
	 * @param shelf - added shelf
	 */
    @Observer(Constants.SHELF_ADDED_EVENT)
	public void onShelfAdded(Shelf shelf){
    	File directory = getFileByPath(shelf.getPath());
		FileUtils.addDirectory(directory);
	}
    
    /**
	 * This method observes <code>ALBUM_ADDED_EVENT</code> and invoked after the user add new album
	 * This method add album directory to the disk
	 * @param album - added album
	 */
    @Observer(Constants.ALBUM_ADDED_EVENT)
	public void onAlbumAdded(Album album){
    	File directory = getFileByPath(album.getPath());
		FileUtils.addDirectory(directory);
	}
    
    /**
	 * This method invoked after user set new avatar icon
	 * @param avatarData - avatar file
	 * @param user - user, that add avatar
	 */
    public boolean saveAvatar(File avatarData, User user) {
    	String avatarPath = File.separator + user.getLogin() + File.separator + Constants.AVATAR_JPG;
		createDirectoryIfNotExist(avatarPath);
		return writeFile(avatarPath, avatarData.getPath(), "", Constants.AVATAR_SIZE, true);
	}

    /**
	 * This method observes <code>Constants.IMAGE_DELETED_EVENT</code> and invoked after the user delete her image
	 * This method delete image and all thumbnails of this image from the disk
	 * @param image - deleted image
	 * @param path - relative path of the image file
	 */
    @Restrict("#{s:hasRole('admin')}")
    @Observer(Constants.IMAGE_DELETED_EVENT)
	public void deleteImage(Image image, String path) {
    	for(ImageDimension d : ImageDimension.values()){
    		FileUtils.deleteFile(getFileByPath(transformPath(path, d.getFilePostfix())));
    	}
	}

    /**
	 * This method invoked after user upload new image
	 * @param fileName - new relative path to the image file
	 * @param tempFilePath - absolute path to uploaded image
	 */
    @Restrict("#{s:hasRole('admin')}")
	public boolean addImage(String fileName, String  tempFilePath) {
		createDirectoryIfNotExist(fileName);
		for(ImageDimension d : ImageDimension.values()){
			if(!writeFile(fileName, tempFilePath, d.getFilePostfix(), d.getX(), true)){
				return false;
			}
    	}
		return true;
	}
    
    /**
	 * This method used to transform one path to another.
	 * For example you want get path of the file with dimensioms 80 of image with path /user/1/2/image.jpg, this method return /user/1/2/image_substitute.jpg
	 * @param target - path to transform
	 * @param substitute - new 'addon' to the path
	 */
    public String transformPath(String target, String substitute) {
    	if(target.length()<2 || target.lastIndexOf(Constants.DOT) == -1){
    		return "";
    	}
        final String begin = target.substring(0, target.lastIndexOf(Constants.DOT));
        final String end = target.substring(target.lastIndexOf(Constants.DOT));
        return begin + substitute + end;
    }

    /**
	 * This method used to get reference to the file with the absolute path
	 * @param path - absolute path of file
	 * @return File reference
	 */
	public File getFileByAbsolutePath(String path) {
		return new File(path);
	}
	
	/**
	 * This utility method used to determine if the directory with specified relative path exist
	 * @param path - absolute path of directory
	 * @return File reference
	 */
	public boolean isDirectoryPresent(String path){
		final File file = getFileByPath(path);
		return file.exists() && file.isDirectory();
	}
	
	/**
	 * This utility method used to determine if the file with specified relative path exist
	 * @param path - absolute path of file
	 * @return File reference
	 */
	public boolean isFilePresent(String path){
		final File file = getFileByPath(path);
		return file.exists();
	}
	
	/**
	 * This method observes <code>Constants.ALBUM_DRAGGED_EVENT</code> and invoked after the user dragged album form one shelf to the another.
	 * This method rename album directory to the new directory
	 * @param album - dragged album
	 * @param pathOld - old path of album directory
	 */
	@Observer(Constants.ALBUM_DRAGGED_EVENT)
	public void renameAlbumDirectory(Album album, String pathOld){
		File file = getFileByPath(pathOld);
		File file2 = getFileByPath(album.getPath());
		if(file2.exists()){
			if(file2.isDirectory()){
				FileUtils.deleteDirectory(file2, false);
			}else{
				FileUtils.deleteFile(file2);
			}
		}
		file.renameTo(file2);
	}
	
	/**
	 * This method observes <code>Constants.IMAGE_DRAGGED_EVENT</code> and invoked after the user dragged image form one album to the another.
	 * This method rename image file and all thumbnails to the new name
	 * @param image - dragged image
	 * @param pathOld - old path of image file
	 */
	@Observer(Constants.IMAGE_DRAGGED_EVENT)
	public void renameImageFile(Image image, String pathOld){
		File file = null;
		File file2 = null;
		for(ImageDimension dimension : ImageDimension.values()){
			file = getFileByPath(transformPath(pathOld, dimension.getFilePostfix()));
			file2 = getFileByPath(transformPath(image.getFullPath(), dimension.getFilePostfix()));
			if(file2.exists()){
				if(file2.isDirectory()){
					FileUtils.deleteDirectory(file2, false);
				}else{
					FileUtils.deleteFile(file2);
				}
			}
			file.renameTo(file2);
		}
	}
	
	private boolean writeFile(String newFileName, String fileName,
			String pattern, int size, boolean includeUploadRoot) {
		BufferedImage bsrc = null;
		try {
			//Read file form disk
			bsrc = FileUtils.bitmapToImage(fileName, Constants.JPG);
		} catch (IOException e1) {
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
		//scale image if need
		BufferedImage bdest = FileUtils.getScaledInstance(bsrc, width, height,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
		//Determine new path of image file
		String dest = includeUploadRoot ? this.uploadRootPath
				+ transformPath(newFileName, pattern) : transformPath(
				newFileName, pattern);
		try {
			//save to disk
			FileUtils.imageToBitmap(bdest, dest, Constants.JPG);
		} catch (IOException ex) {
			return false;
		}
		return true;
	}
	
	private void deleteDirectory(String directory){
    	final File file = getFileByPath(directory);
        FileUtils.deleteDirectory(file, false);
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