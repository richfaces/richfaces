package org.richfaces.photoalbum.manager;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.domain.MetaTag;
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
/**
 * This class represent 'M' in MVC pattern. It is storage to application flow related data such as selectedAlbum, image, mainArea to preview etc..
 *
 * @author Andrey Markhel
 */
@Name("model")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class Model implements Serializable{

	private static final long serialVersionUID = -1767281809514660171L;
	
	private Image selectedImage;

	private Album selectedAlbum;
	
	private User selectedUser;
	
	private Shelf selectedShelf;
	
	private MetaTag selectedTag;
	
	private NavigationEnum mainArea;
	
	private List<Image> images;
	
	/**
	 * This method invoked after the almost user actions, to prepare properly data to show in the UI.
	 * @param mainArea - next Area to show(determined in controller)
	 * @param selectedUser - user, that was selected(determined in controller)
	 * @param selectedShelf - shelf, that was selected(determined in controller)
	 * @param selectedAlbum - album, that was selected(determined in controller)
	 * @param selectedImage - image, that was selected(determined in controller)
	 * @param images - list of images, to show during slideshow process(determined in controller)
	 */
	public void resetModel(NavigationEnum mainArea, User selectedUser, Shelf selectedShelf, Album selectedAlbum, Image selectedImage, List<Image> images){
		this.setSelectedAlbum(selectedAlbum);
		this.setSelectedImage(selectedImage);
		this.setSelectedShelf(selectedShelf);
		this.setSelectedUser(selectedUser);
		this.setMainArea(mainArea);
		this.images = images;
	}
	
	/**
	 * This method observes <code> Constants.UPDATE_MAIN_AREA_EVENT </code>event and invoked after the user actions, that not change model, but change area to preview
	 * @param mainArea - next Area to show
	 * 
	 */
	@Observer(Constants.UPDATE_MAIN_AREA_EVENT)
	public void setMainArea(NavigationEnum mainArea) {
		if(this.mainArea != null && this.mainArea.equals(NavigationEnum.FILE_UPLOAD)){
			Events.instance().raiseEvent(Constants.CLEAR_FILE_UPLOAD_EVENT);
		}
		this.mainArea = mainArea;
	}

	/**
	 * This method observes <code> Constants.UPDATE_SELECTED_TAG_EVENT </code>event and invoked after the user click on any metatag.
	 * @param selectedTag - clicked tag
	 * 
	 */
	@Observer(Constants.UPDATE_SELECTED_TAG_EVENT)
	public void setSelectedTag(MetaTag selectedTag) {
		this.selectedTag = selectedTag;
	}
	
	public NavigationEnum getMainArea() {
		return mainArea;
	}

	public Image getSelectedImage() {
		return selectedImage;
	}

	private void setSelectedImage(Image selectedImage) {
		this.selectedImage = selectedImage;
	}

	public Album getSelectedAlbum() {
		return selectedAlbum;
	}

	public void setSelectedAlbum(Album selectedAlbum) {
		this.selectedAlbum = selectedAlbum;
	}
	
	public User getSelectedUser() {
		return selectedUser;
	}

	private void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public Shelf getSelectedShelf() {
		return selectedShelf;
	}

	private void setSelectedShelf(Shelf selectedShelf) {
		this.selectedShelf = selectedShelf;
	}
	
	public MetaTag getSelectedTag() {
		return selectedTag;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
}