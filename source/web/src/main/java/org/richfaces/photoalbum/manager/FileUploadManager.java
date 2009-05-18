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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.core.Events;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IImageAction;
import org.richfaces.photoalbum.service.PhotoAlbumException;
import org.richfaces.photoalbum.ui.FileWrapper;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;
import com.drew.metadata.jpeg.JpegDirectory;

/**
 * Class encapsulated all functionality, related to file-upload process.
 *
 * @author Andrey Markhel
 */
@Name("fileUploadManager")
@Scope(ScopeType.EVENT)
public class FileUploadManager implements Serializable {
	
	private static final long serialVersionUID = 4969087557225414955L;
	
	@In IImageAction imageAction;
	
	@In(required = true, scope=ScopeType.CONVERSATION) @Out(scope=ScopeType.CONVERSATION)
	FileWrapper fileWrapper;

	@In Model model;
	
	@In private FileManager fileManager;

	/**
	 * Listenet, that invoked  during file upload process. Only registered users can upload images.
	 *
	 * @param event - event, indicated that file upload started
	 */
	@Restrict("#{s:hasRole('admin')}")
	public void listener(UploadEvent event){
		UploadItem item = event.getUploadItem();
		//Construct image from item
		Image image = constructImage(item);
		try {
			//Extract metadata(size, camera model etc..)
			extractMetadata(item, image);
		} catch (Exception e1) {
			addError(item, image, Constants.FILE_PROCESSING_ERROR);
			return;
		}
		image.setAlbum(model.getSelectedAlbum());
		if(image.getAlbum() == null){
			addError(item, image, Constants.NO_ALBUM_TO_DOWNLOAD_ERROR);
			return;
		}
		try{
			//Check if image with given name already exist
			if(imageAction.isImageWithThisPathExist(image.getAlbum(), image.getPath())){
				//If exist generate new path for image
				String newPath = generateNewPath(image);
				image.setPath(newPath);
				image.setName(newPath);
			}
			//Save to database
			imageAction.addImage(image);
		}catch(Exception e){
			addError(item, image, Constants.IMAGE_SAVING_ERROR);
			return;
		}
		//Save to disk
		if(!fileManager.addImage(image.getFullPath(), item.getFile().getPath())){
			addError(item, image, Constants.FILE_SAVE_ERROR);
			return;
		}
		//Prepare to show in UI
		fileWrapper.getFiles().add(image);
		Events.instance().raiseEvent(Constants.IMAGE_ADDED_EVENT, image);
		//Delete temporary file
		item.getFile().delete();
	}

	private String generateNewPath(Image image) throws PhotoAlbumException{
		String path = image.getPath().substring(0, image.getPath().lastIndexOf(Constants.DOT));
		Long countCopies = imageAction.getCountIdenticalImages(image.getAlbum(), path) + 1;
		String newPath = fileManager.transformPath(image.getPath(), "_" + countCopies);
		return newPath;
	}

	private void addError(UploadItem item, Image image, String error) {
		fileWrapper.onFileUploadError(image, error);
		item.getFile().delete();
	}
	
	private void addError(Image image, String error) {
		fileWrapper.onFileUploadError(image, error);
	}

	private Image constructImage(UploadItem item) {
		Image image = new Image();
		image.setUploaded(new Date());
		image.setDescription(item.getFileName());
		image.setName(item.getFileName());
		image.setSize(item.getFileSize());
		image.setPath(item.getFileName());
		image.setAllowComments(true);
		return image;
	}
	
	private void extractMetadata(UploadItem item, Image image){
		InputStream in = null;
		try{
			in = new FileInputStream(item.getFile());
			Metadata metadata = JpegMetadataReader.readMetadata(in);
			Directory exifDirectory = metadata.getDirectory(ExifDirectory.class);
			Directory jpgDirectory = metadata.getDirectory(JpegDirectory.class);
			setupCameraModel(image, exifDirectory);
			setupDimensions(image, exifDirectory, jpgDirectory);
			setupCreatedDate(image, exifDirectory);
		}catch(Exception e){
			addError(item, image, Constants.IMAGE_SAVING_ERROR);
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				addError(item, image, Constants.IMAGE_SAVING_ERROR);
			}
		}
	}

	private void setupCreatedDate(Image image, Directory exifDirectory)
			throws MetadataException {
		if (exifDirectory.containsTag(ExifDirectory.TAG_DATETIME_ORIGINAL)) {
			Date time = exifDirectory.getDate(ExifDirectory.TAG_DATETIME_ORIGINAL);
			image.setCreated(time);
		}
	}

	private void setupDimensions(Image image, Directory exifDirectory, Directory jpgDirectory){
		try{
			if (exifDirectory.containsTag(ExifDirectory.TAG_EXIF_IMAGE_WIDTH) && exifDirectory.containsTag(ExifDirectory.TAG_EXIF_IMAGE_HEIGHT)) {
				int width = exifDirectory.getInt(ExifDirectory.TAG_EXIF_IMAGE_WIDTH);
				image.setWidth(width);
				int height = exifDirectory.getInt(ExifDirectory.TAG_EXIF_IMAGE_HEIGHT);
				image.setHeight(height);
			} else {
				if (jpgDirectory.containsTag(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT)) {
					int width = jpgDirectory.getInt(JpegDirectory.TAG_JPEG_IMAGE_WIDTH);
					image.setWidth(width);
					int height = jpgDirectory.getInt(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT);
					image.setHeight(height);
				}
			}
		}catch(MetadataException e){
			addError(image, Constants.IMAGE_SAVING_ERROR);
		}
	}

	private void setupCameraModel(Image image, Directory exifDirectory) {
		if (exifDirectory.containsTag(ExifDirectory.TAG_MODEL)) {
			String cameraModel = exifDirectory.getString(ExifDirectory.TAG_MODEL);
			image.setCameraModel(cameraModel);
		}else{
			image.setCameraModel("");
		}
	}
}