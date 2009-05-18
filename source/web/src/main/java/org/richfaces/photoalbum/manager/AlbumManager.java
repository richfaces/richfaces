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
 * Class encapsulated all functionality, related to working with album.
 *
 * @author Andrey Markhel
 */
import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IAlbumAction;

@Name("albumManager")
@Scope(ScopeType.EVENT)
@AutoCreate
public class AlbumManager implements Serializable{

	private static final long serialVersionUID = 2631634926126857691L;
	
	private boolean validationSuccess = false;
	
	private boolean errorInCreate = false;
	
	@In private IAlbumAction albumAction;
	
	@In private User user;
	
	@In FacesMessages facesMessages;
	
	/**
	 * Method, that invoked on creation of the new album. Only registered users can create new albums.
	 * @param album - new album
	 *
	 */
	@Restrict("#{s:hasRole('admin')}")
	public void addAlbum(Album album){
		//Shelf must be not-null
		if(album.getShelf() == null){
			facesMessages.addToControl(Constants.SHELF_ID, Constants.SHELF_MUST_BE_NOT_NULL_ERROR, new Object[0]);
			Contexts.getConversationContext().set(Constants.ALBUM_VARIABLE, album);
			return;
		}
		//Album name must be unique in shelf
		if(user.hasAlbumWithName(album)){
    		Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.SAME_ALBUM_EXIST_ERROR);
			return;
    	}
		//All data is valid
		validationSuccess = true;
		try{
			//Save to DB
			album.setCreated(new Date());
			albumAction.addAlbum(album);
		}catch(Exception e){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.ALBUM_SAVING_ERROR);
			return;
		}
		//Reset 'album' component in conversation scope
		Contexts.getConversationContext().set(Constants.ALBUM_VARIABLE, null);
		//Raise 'albumAdded' event
		Events.instance().raiseEvent(Constants.ALBUM_ADDED_EVENT, album);
	}
	
	/**
	 * Method, that invoked  when user want to create new album. Only registered users can create new albums.
	 * @param shelf - shelf, that will contain new album
	 *
	 */
	@Restrict("#{s:hasRole('admin')}")
	public void createAlbum(Shelf shelf){
		Album album = new Album();
		if(shelf == null){
			if(user.getShelves().size() > 0){
				shelf = user.getShelves().get(0);
			}
			if(shelf == null){
				Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.NO_SHELF_ERROR);
				setErrorInCreate(true);
				return;
			}
		}
		album.setShelf(shelf);
		//Reset 'album' component in conversation scope
		Contexts.getConversationContext().set(Constants.ALBUM_VARIABLE, album);
	}

	/**
	 * Method, that invoked when user click 'Edit album' button. Only registered users can edit albums.
	 * @param album - edited album
	 * @param editFromInplace - indicate whether edit process was initiated by inplaceInput component
	 */
	@Restrict("#{s:hasRole('admin')}")
	public void editAlbum(Album album, boolean editFromInplace){
		try{
			if(user.hasAlbumWithName(album)){
	    		Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.SAME_ALBUM_EXIST_ERROR);
    				albumAction.resetAlbum(album);
	    		return;
	    	}
			if(editFromInplace){
    			//We need validate album name manually
    			ClassValidator<Album> shelfValidator = new ClassValidator<Album>(Album.class );
        		InvalidValue[] validationMessages = shelfValidator.getInvalidValues(album, "name");
        		if(validationMessages.length > 0 ){
        			for(InvalidValue i : validationMessages){
        				Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, i.getMessage());
        			}
        			//If error occured we need refresh album to display correct value in inplaceInput
        			albumAction.resetAlbum(album);
    				return;
        		}
			}
			albumAction.editAlbum(album);
		}catch(Exception e){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.ALBUM_SAVING_ERROR);
			albumAction.resetAlbum(album);
			return;
		}
		//Reset 'album' component in conversation scope
		Events.instance().raiseEvent(Constants.ALBUM_EDITED_EVENT, album);
	}
	
	/**
	 * Method, that invoked when user click 'Delete album' button. Only registered users can delete albums.
	 * @param album - album to delete
	 *
	 */
	@Restrict("#{s:hasRole('admin')}")
	public void deleteAlbum(Album album){
		String pathToDelete = album.getPath();
		try{
			albumAction.deleteAlbum(album);
		}catch(Exception e){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.ALBUM_DELETING_ERROR);
			return;
		}
		//Raise 'albumDeleted' event, parameter path - path of Directory to delete
		Events.instance().raiseEvent(Constants.ALBUM_DELETED_EVENT, album, pathToDelete);
	}

	public boolean isValidationSuccess() {
		return validationSuccess;
	}

	public void setValidationSuccess(boolean validationSuccess) {
		this.validationSuccess = validationSuccess;
	}

	public boolean isErrorInCreate() {
		return errorInCreate;
	}

	public void setErrorInCreate(boolean errorInCreate) {
		this.errorInCreate = errorInCreate;
	}
}