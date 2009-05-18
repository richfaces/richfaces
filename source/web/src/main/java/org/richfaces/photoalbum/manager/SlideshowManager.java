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
 * Class encapsulated all functionality, related to working with slideshow.
 *
 * @author Andrey Markhel
 */
import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Events;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.service.Constants;

@Name("slideshow")
@Scope(ScopeType.CONVERSATION)
public class SlideshowManager implements Serializable{

	private static final long serialVersionUID = 7801877176558409702L;
	
	private Integer slideshowIndex;
	
	private Integer startSlideshowIndex;
	
	private Image selectedImage;
	
	private boolean active;
	
	private boolean errorDetected;
	
	@In Model model;
	
	private int interval = Constants.INITIAL_DELAY;

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * This method invoked after user click on 'Start slideshow' button and no image is selected. After execution of this method slideshow will be activated.
	 *
	 */
	public void startSlideshow(){
		active = true;
		errorDetected = false;
		this.slideshowIndex = 0;
		this.startSlideshowIndex = 0;
		if(model.getImages() == null || model.getImages().size() < 1){
			stopSlideshow();
			errorDetected = true;
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.NO_IMAGES_FOR_SLIDESHOW_ERROR);
			return;
		}
		this.selectedImage = model.getImages().get(this.slideshowIndex);
		//mark image as 'visited'
		this.selectedImage.setVisited(true);
		//Check if that image was recently deleted. If yes, immediately stop slideshow process
		FileManager fileManager = (FileManager)Contexts.getApplicationContext().get(Constants.FILE_MANAGER_COMPONENT);
		if(!fileManager.isFilePresent(this.selectedImage.getFullPath())){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.IMAGE_RECENTLY_DELETED_ERROR);
			active = false;
			errorDetected = true;
			model.resetModel(NavigationEnum.ALBUM_IMAGE_PREVIEW, this.selectedImage.getAlbum().getOwner(), this.selectedImage.getAlbum().getShelf(), this.selectedImage.getAlbum(), null, this.selectedImage.getAlbum().getImages());
			return;
		}
	}
	
	/**
	 * This method invoked after user click on 'Start slideshow' button. After execution of this method slideshow will be activated starting from selected image.
	 *
	 *@param selectedImage - first image to show during slideshow
	 */
	public void startSlideshow(Image selectedImage){
		errorDetected = false;
		active = true;
		if(model.getImages() == null || model.getImages().size() < 1){
			stopSlideshow();
			errorDetected = true;
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.NO_IMAGES_FOR_SLIDESHOW_ERROR);
			return;
		}
		this.slideshowIndex = model.getImages().indexOf(selectedImage);
		this.startSlideshowIndex = this.slideshowIndex;
		this.selectedImage = selectedImage;
		//mark image as 'visited'
		this.selectedImage.setVisited(true);
		//Check if that image was recently deleted. If yes, immediately stop slideshow
		FileManager fileManager = (FileManager)Contexts.getApplicationContext().get(Constants.FILE_MANAGER_COMPONENT);
		if(!fileManager.isFilePresent(this.selectedImage.getFullPath())){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.IMAGE_RECENTLY_DELETED_ERROR);
			active = false;
			errorDetected = true;
			model.resetModel(NavigationEnum.ALBUM_IMAGE_PREVIEW, this.selectedImage.getAlbum().getOwner(), this.selectedImage.getAlbum().getShelf(), this.selectedImage.getAlbum(), null, this.selectedImage.getAlbum().getImages());
			return;
		}
	}
	
	/**
	 * This method invoked after user click on 'Stop slideshow' button. After execution of this method slideshow will be de-activated.
	 *
	 */
	@Observer(Constants.STOP_SLIDESHOW_EVENT)
	public void stopSlideshow(){
		active = false;
		errorDetected = false;
		this.selectedImage = null;
		this.slideshowIndex = 0;
		this.startSlideshowIndex = 0;
	}

	public Integer getSlideshowIndex() {
		return slideshowIndex;
	}

	public void setSlideshowIndex(Integer slideshowIndex) {
		this.slideshowIndex = slideshowIndex;
	}

	public Image getSelectedImage() {
		return selectedImage;
	}

	public void setSelectedImage(Image selectedImage) {
		this.selectedImage = selectedImage;
	}
	
	/**
	 * This method used to prepare next image to show during slideshow
	 *
	 */
	public void showNextImage(){
		if(!active){
			errorDetected = true;
			return;
		}
		//reset index if we reached last image
		if(slideshowIndex == model.getImages().size() - 1){
			slideshowIndex = -1;
		}
		slideshowIndex++;
		//To prevent slideshow mechanism working in cycle.
		if(slideshowIndex == startSlideshowIndex){
			stopSlideshow();
			errorDetected = true;
			return;
		}
		selectedImage = model.getImages().get(slideshowIndex);
		//mark image as 'visited'
		this.selectedImage.setVisited(true);
		//Check if that image was recently deleted. If yes, stopping slideshow
		FileManager fileManager = (FileManager)Contexts.getApplicationContext().get(Constants.FILE_MANAGER_COMPONENT);
		if(!fileManager.isFilePresent(this.selectedImage.getFullPath())){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.IMAGE_RECENTLY_DELETED_ERROR);
			active = false;
			errorDetected = true;
			model.resetModel(NavigationEnum.ALBUM_IMAGE_PREVIEW, this.selectedImage.getAlbum().getOwner(), this.selectedImage.getAlbum().getShelf(), this.selectedImage.getAlbum(), null, this.selectedImage.getAlbum().getImages());
			return;
		}
	}

	public Integer getStartSlideshowIndex() {
		return startSlideshowIndex;
	}

	public void setStartSlideshowIndex(Integer startSlideshowIndex) {
		this.startSlideshowIndex = startSlideshowIndex;
	}

	public boolean isErrorDetected() {
		return errorDetected;
	}

	public void setErrorDetected(boolean errorDetected) {
		this.errorDetected = errorDetected;
	}
}