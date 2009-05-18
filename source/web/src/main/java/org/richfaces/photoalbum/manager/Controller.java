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

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Events;
import org.richfaces.component.UITree;
import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.domain.MetaTag;
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
/**
 * This class represent 'C' in MVC pattern. It is logic that determine what actions invoked and what next page need to be showed.
 * Typically on almost all user actions, this class populates the model and determine new view to show.
 * Also contain utility logic, such as checking is the given shelf belongs to the specified user etc..
 * @author Andrey Markhel
 */
@Name("controller")
@Scope(ScopeType.EVENT)
public class Controller implements Serializable{

	private static final long serialVersionUID = 5656562187249324512L;
	
	@In @Out Model model;

	@In(scope = ScopeType.SESSION) User user;

	/**
	 * This method invoked after the user want to see all predefined shelves, existed in application
	 */
	public void selectPublicShelves(){
		model.resetModel(NavigationEnum.ANONYM, user, null, null, null, null);
	}
	
	/**
	 * This method invoked after the user want to see all her shelves.
	 */
	public void selectShelves(){
		model.resetModel(NavigationEnum.ALL_SHELFS, user, null, null, null, null);
	}
	
	/**
	 * This method invoked after the user want to see all her albums.
	 */
	public void selectAlbums(){
		model.resetModel(NavigationEnum.ALL_ALBUMS, user, null, null, null, null);
	}
	
	/**
	 * This method invoked after the user want to see all her images.
	 */
	public void selectImages(){
		model.resetModel(NavigationEnum.ALL_IMAGES, user, null, null, null, user.getImages() );
	}
	
	/**
	 * This method invoked after the user want to edit specified shelf.
	 * @param shelf - shelf to edit
	 */
	@Restrict("#{s:hasRole('admin')}")
	public void startEditShelf(Shelf shelf){
		if(!canViewShelf(shelf)){
			pushEvent(Constants.ADD_ERROR_EVENT, Constants.HAVENT_ACCESS);
			return;
		}
		model.resetModel(NavigationEnum.SHELF_EDIT, shelf.getOwner(), shelf, null, null, null);		
	}
	
	/**
	 * This method invoked after the user want to interrupt edit shelf process
	 * 
	 */
	public void cancelEditShelf(){
		model.resetModel(NavigationEnum.SHELF_PREVIEW, model.getSelectedShelf().getOwner(), model.getSelectedShelf(), null, null, null);		
	}

	/**
	 * This method invoked after the user want to see specified album independently is it her album or not.
	 * @param album - album to show
	 */
	public void showAlbum(Album album){
		if(!canViewAlbum(album)){
			pushEvent(Constants.ADD_ERROR_EVENT, Constants.HAVENT_ACCESS);
			return;
		}
		FileManager fileManager = (FileManager)Contexts.getApplicationContext().get(Constants.FILE_MANAGER_COMPONENT);
		//Check, that album was not deleted recently.
		if(!fileManager.isDirectoryPresent(album.getPath())){
			pushEvent(Constants.ADD_ERROR_EVENT, Constants.ALBUM_RECENTLY_DELETED_ERROR);
			model.resetModel(NavigationEnum.SHELF_PREVIEW, album.getOwner(), album.getShelf(), null, null, null);
			return;
		}
		model.resetModel(NavigationEnum.ALBUM_PREVIEW, album.getOwner(), album.getShelf(), album, null, album.getImages());	
	}
	
	/**
	 * This method invoked in cases, when it is need to clear fileUpload component
	 * 
	 */
	public void resetFileUpload(){
		pushEvent(Constants.CLEAR_FILE_UPLOAD_EVENT);
	}
	
	/**
	 * This method invoked after the user want to see specified image independently is it her image or not.
	 * @param album - album to show
	 */
	public void showImage(Image image){
		//Clear not-saved comment in editor
		pushEvent(Constants.CLEAR_EDITOR_EVENT, "");
		if(!canViewImage(image)){
			pushEvent(Constants.ADD_ERROR_EVENT, Constants.HAVENT_ACCESS);
			return;
		}
		//Check, that image was not deleted recently
		final FileManager fileManager = (FileManager)Contexts.getApplicationContext().get(Constants.FILE_MANAGER_COMPONENT);
		if(!fileManager.isFilePresent(image.getFullPath())){
			pushEvent(Constants.ADD_ERROR_EVENT, Constants.IMAGE_RECENTLY_DELETED_ERROR);
			model.resetModel(NavigationEnum.ALBUM_PREVIEW, image.getAlbum().getOwner(), image.getAlbum().getShelf(), image.getAlbum(), null, image.getAlbum().getImages());
			return;
		}
		model.resetModel(NavigationEnum.ALBUM_IMAGE_PREVIEW, image.getAlbum().getOwner(), image.getAlbum().getShelf(), image.getAlbum(), image, image.getAlbum().getImages());
		image.setVisited(true);
	}
	
	/**
	 * This method invoked after the user want to edit specified image.
	 * @param image - image to edit
	 */
	@Restrict("#{s:hasRole('admin')}")
	public void startEditImage(Image image){
		if(!canViewImage(image)){
			pushEvent(Constants.ADD_ERROR_EVENT, Constants.HAVENT_ACCESS);
			return;
		}
		model.resetModel(NavigationEnum.ALBUM_IMAGE_EDIT, image.getOwner(), image.getAlbum().getShelf(), image.getAlbum(), image, image.getAlbum().getImages());
	}
	
	/**
	 * This method invoked after the user want to save just edited user to database.
	 * 
	 */
	@Restrict("#{s:hasRole('admin')}")
	public void editUser(){
		pushEvent(Constants.EDIT_USER_EVENT);
		model.resetModel(NavigationEnum.ALL_SHELFS, user, model.getSelectedShelf(), model.getSelectedAlbum(), model.getSelectedImage(), model.getImages());
	}
	
	/**
	 * This method invoked after the user want to interrupt edit user process
	 * 
	 */
	public void cancelEditUser(){
		pushEvent(Constants.CANCEL_EDIT_USER_EVENT);
		model.resetModel(NavigationEnum.ANONYM, user, null, null, null, null);
	}
	
	/**
	 * This method invoked after the user want to interrupt edit image process
	 * 
	 */
	public void cancelEditImage(){
		model.resetModel(NavigationEnum.ALBUM_IMAGE_PREVIEW, model.getSelectedImage().getAlbum().getShelf().getOwner(), model.getSelectedImage().getAlbum().getShelf(), model.getSelectedImage().getAlbum(), model.getSelectedImage(), model.getSelectedImage().getAlbum().getImages() );		
	}
	
	/**
	 * This method invoked after the user want to see specified shelf independently is it her shelf or not.
	 * @param album - album to show
	 */
	public void showShelf(Shelf shelf){
		final FileManager fileManager = (FileManager)Contexts.getApplicationContext().get(Constants.FILE_MANAGER_COMPONENT);
		if(!fileManager.isDirectoryPresent(shelf.getPath())){
			pushEvent(Constants.ADD_ERROR_EVENT, Constants.SHELF_RECENTLY_DELETED_ERROR);
			model.resetModel(NavigationEnum.ANONYM, shelf.getOwner(), null, null, null, null);
			return;
		}
		model.resetModel(NavigationEnum.SHELF_PREVIEW, shelf.getOwner(), shelf, null, null, null);
	}
	
	/**
	 * This method invoked after the user want to edit specified album.
	 * @param album - album to edit
	 */
	@Restrict("#{s:hasRole('admin')}")
	public void startEditAlbum(Album album){
		if(!album.isOwner(user)) {
			pushEvent(Constants.ADD_ERROR_EVENT, Constants.HAVENT_ACCESS);
			return;
		}
		model.resetModel(NavigationEnum.ALBUM_EDIT, album.getOwner(), album.getShelf(), album, null, album.getImages());
	}
	
	/**
	 * This method invoked after the user want to interrupt edit album process
	 * 
	 */
	public void cancelEditAlbum(){
		model.resetModel(NavigationEnum.ALBUM_PREVIEW, model.getSelectedAlbum().getOwner(), model.getSelectedAlbum().getShelf(), model.getSelectedAlbum(), null, model.getSelectedAlbum().getImages());
	}
	
	/**
	 * This method observes <code>Constants.ALBUM_ADDED_EVENT</code> and invoked after the user add new album
	 * @param album - added album
	 */
	@Observer(Constants.ALBUM_ADDED_EVENT)
	public void onAlbumAdded(Album album){
		model.resetModel(NavigationEnum.ALBUM_PREVIEW, album.getOwner(), album.getShelf(), album, null, album.getImages());
	}
	
	/**
	 * This method observes <code>Constants.ALBUM_EDITED_EVENT</code> and invoked after the user edit her album
	 * @param album - edited album
	 */
	@Observer(Constants.ALBUM_EDITED_EVENT)
	public void onAlbumEdited(Album album){
		model.resetModel(NavigationEnum.ALBUM_PREVIEW, model.getSelectedUser(), model.getSelectedShelf(), album, null, album.getImages());
	}
	
	/**
	 * This method observes <code>Constants.ALBUM_DELETED_EVENT</code> and invoked after the user delete her album
	 * @param album - deleted album
	 * @param path - relative path of the album directory
	 */
	@Observer(Constants.ALBUM_DELETED_EVENT)
	public void onAlbumDeleted(Album album, String path){
		model.resetModel(NavigationEnum.ALL_ALBUMS, model.getSelectedUser(), model.getSelectedShelf(), null, null, null);
	}
	
	/**
	 * This method observes <code>Constants.SHELF_DELETED_EVENT</code> and invoked after the user delete her shelf
	 * @param shelf - deleted shelf
	 * @param path - relative path of the shelf directory
	 */
	@Observer(Constants.SHELF_DELETED_EVENT)
	public void onShelfDeleted(Shelf shelf, String path){
		model.resetModel(NavigationEnum.ALL_SHELFS, model.getSelectedUser(), null, null, null, null);
	}
	
	/**
	 * This method observes <code>Constants.SHELF_ADDED_EVENT</code> and invoked after the user add new shelf
	 * @param shelf - added shelf
	 */
	@Observer(Constants.SHELF_ADDED_EVENT)
	public void onShelfAdded(Shelf shelf){
		model.resetModel(NavigationEnum.SHELF_PREVIEW, shelf.getOwner(), shelf, null, null, null);
	}
	
	/**
	 * This method observes <code>Constants.SHELF_EDITED_EVENT</code> and invoked after the user edit her shelf
	 * @param shelf - edited shelf
	 */
	@Observer(Constants.SHELF_EDITED_EVENT)
	public void onShelfEdited(Shelf shelf){
		model.resetModel(NavigationEnum.SHELF_PREVIEW, shelf.getOwner(), shelf, null, null, null);
	}
	
	/**
	 * This method observes <code>Constants.IMAGE_DELETED_EVENT</code> and invoked after the user delete her image
	 * @param image - deleted image
	 * @param path - relative path of the image file
	 */
	@Observer(Constants.IMAGE_DELETED_EVENT)
	public void onImageDeleted(Image image, String path){
		model.resetModel(NavigationEnum.ALBUM_PREVIEW, model.getSelectedUser(), model.getSelectedShelf(), model.getSelectedAlbum(), null, model.getSelectedAlbum().getImages());
	}
	
	/**
	 * This method observes <code>Constants.AUTHENTICATED_EVENT</code> and invoked after the user successfully authenticate to the system
	 * @param u - authenticated user
	 */
	@Observer(Constants.AUTHENTICATED_EVENT)
	public void onAuthenticate(User u){
		model.resetModel(NavigationEnum.ALL_SHELFS, u, null, null, null, null);
	}
	
	/**
	 * This method invoked after the user want to go to the file-upload page
	 * 
	 */
	public void showFileUpload(){
		if(!(user.getShelves().size() > 0)){
			//If user have no shelves, that can start fileupload process
			pushEvent(Constants.ADD_ERROR_EVENT, Constants.FILE_UPLOAD_SHOW_ERROR);
			return;
		}
		Album alb = null;
		//If selected album belongs to user
		alb = setDefaultAlbumToUpload(alb);
		model.resetModel(NavigationEnum.FILE_UPLOAD, user, alb != null ? alb.getShelf() : null, alb, null, alb != null ? alb.getImages() : null);
	}
	
	/**
	 * This method invoked after the user want to go to the file-upload page and download images to the specified album
	 * @param album - selected album
	 * 
	 */
	public void showFileUpload(Album album){
		if(!isUserAlbum(album)){
			showError(Constants.YOU_CAN_T_ADD_IMAGES_TO_THAT_ALBUM_ERROR);
			return;
		}
		model.resetModel(NavigationEnum.FILE_UPLOAD, album.getShelf().getOwner(), album.getShelf(), album, null, album.getImages());
	}
	
	/**
	 * This method invoked after the user want to see all shared albums of the specified user
	 * @param user - user to see
	 * 
	 */
	public void showSharedAlbums(User user){
		model.resetModel(NavigationEnum.USER_SHARED_ALBUMS, user, null, null, null, user.getSharedImages());
	}
	
	/**
	 * This method invoked after the user want to see all shared images of the specified user
	 * @param user - user to see
	 * 
	 */
	public void showSharedImages(User user){
		model.resetModel(NavigationEnum.USER_SHARED_IMAGES, user, null, null, null, user.getSharedImages());
	}
	
	/**
	 * This method invoked after the user want to see profile of the specified user
	 * @param user - user to see
	 * 
	 */
	public void showUser(User user){
		model.resetModel(NavigationEnum.USER_PREFS, user, null, null, null, null);
		Contexts.getConversationContext().set(Constants.AVATAR_DATA_COMPONENT, null);
	}
	
	/**
	 * This method invoked after the user want to see all unvisited images, belongs to the of specified shelf
	 * @param shelf - shelf to see
	 * 
	 */
	public void showUnvisitedImages(Shelf shelf){
		model.resetModel(NavigationEnum.SHELF_UNVISITED, shelf.getOwner(), shelf, null, null, shelf.getUnvisitedImages());
	}
	
	/**
	 * This method invoked after the user want to see all unvisited images, belongs to the of specified album
	 * @param album - album to see
	 * 
	 */
	public void showUnvisitedImages(Album album){
		model.resetModel(NavigationEnum.ALBUM_UNVISITED, album.getOwner(), album.getShelf(), album, null, album.getUnvisitedImages());
	}

	/**
	 * This method invoked after the user want to see all images, related to the of specified album
	 * @param metatag - tag to see
	 * 
	 */
	public void showTag(MetaTag metatag){
		model.resetModel(NavigationEnum.TAGS, model.getSelectedUser(), model.getSelectedShelf(), model.getSelectedAlbum(), model.getSelectedImage(), metatag.getImages());
		model.setSelectedTag(metatag);
	}
	
	/**
	 * This utility method invoked in case if you want to show to the user specified error in popup
	 * @param error - error to show
	 * 
	 */
	public void showError(String error){
		pushEvent(Constants.ADD_ERROR_EVENT, error);
	}
	
	/**
	 * This method observes <code>Constants.START_REGISTER_EVENT</code> and invoked after the user want to start registration process.
	 * 
	 */
	@Observer(Constants.START_REGISTER_EVENT)
	public void startRegistration(){
		model.resetModel(NavigationEnum.REGISTER, user, null, null, null, null);
	}
	
	/**
	 * This method invoked after the user want to interrupt registration process
	 * 
	 */
	public void cancelRegistration(){
		model.resetModel(NavigationEnum.ANONYM, user, null, null, null, null);
	}
	
	/**
	 * This utility method determine if the specified node should be marked as selected.
	 * Used in internal rich:tree mechanism
	 */
	@SuppressWarnings("unchecked")
	public Boolean adviseNodeSelected(UITree tree) {
		Object currentNode = tree.getRowData();
		if(currentNode.equals(model.getSelectedAlbum()) || currentNode.equals(model.getSelectedShelf())){
			return true;
		}
		return false;
	}
	
	/**
	 * This utility method used by custom datascroller to determine images to show.
	 * Used in internal rich:tree mechanism
	 */
	public Integer getPage(){
		final Integer index = model.getSelectedAlbum().getIndex(model.getSelectedImage());
		return index / 5 + 1;
	}
	
	/**
	 * This utility method used to determine if the specified image belongs to the logged user
	 * @param image - image to check
	 */
	public boolean isUserImage(Image image){
		if(image == null || image.getOwner() == null) {
			return false;
		}
		return image.isOwner(user);
	}
	
	/**
	 * This utility method used to determine if the logged user have any shelves.
	 * 
	 */
	public boolean isUserHaveShelves(){
		return user.getShelves().size() > 0 ;
	}
	
	/**
	 * This utility method used to determine if the logged user have any albums.
	 * 
	 */
	public boolean isUserHaveAlbums(){
		return user.getAlbums().size() > 0 ;
	}
	
	/**
	 * This utility method used to determine if the specified shelf belongs to the logged user
	 * @param shelf - shelf to check
	 */
	public boolean isUserShelf(Shelf shelf){
		return shelf != null && shelf.isOwner(user);
	}
	
	/**
	 * This utility method used to determine if the specified album belongs to the logged user
	 * @param album - album to check
	 */
	public boolean isUserAlbum(Album album){
		return album != null && album.isOwner(user);
	}
	
	/**
	 * This utility method used to determine if the specified user can be edited
	 * @param user - user to check
	 */
	public boolean isProfileEditable(User selectedUser){
		return selectedUser != null && selectedUser.equals(user);
	}
	
	private boolean canViewShelf(Shelf shelf) {
		return shelf != null && shelf.isOwner(user);
	}
	
	private boolean canViewAlbum(Album album) {
		return album != null && album.getShelf() != null && (album.getShelf().isShared() || album.isOwner(user));
	}

	private boolean canViewImage(Image image) {
		return image != null && image.getAlbum() != null && image.getAlbum().getShelf() != null && (image.getAlbum().getShelf().isShared() || image.isOwner(user));
	}
	
	private void pushEvent(String type, Object... parameters) {
		Events.instance().raiseEvent(type, parameters);
	}
	
	private Album setDefaultAlbumToUpload(Album alb) {
		if(isUserAlbum(model.getSelectedAlbum())){
			alb = model.getSelectedAlbum();
		}
		if(alb == null){
			if(user != null && user.getShelves().size() > 0 && user.getShelves().get(0).getAlbums().size() > 0)
			for(Shelf s : user.getShelves()){
				if(s.getAlbums().size() > 0){
					alb = s.getAlbums().get(0);
					break;
				}
			}
		}
		return alb;
	}	
}