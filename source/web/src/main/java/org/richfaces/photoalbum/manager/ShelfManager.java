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
 * Class encapsulated all functionality, related to working with shelf.
 *
 * @author Andrey Markhel
 */
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IShelfAction;

@Name("shelfManager")
@Scope(ScopeType.EVENT)
@AutoCreate
public class ShelfManager implements Serializable {

	private static final long serialVersionUID = 2631634926126857691L;
	
    private boolean validationSuccess = false;
    
    private List<Shelf> shelves;

    @In IShelfAction shelfAction;

    @In User user;

    /**
	 * Method, that invoked  when user want to create new shelf. Only registered users can create new shelves.
	 *
	 */
    @Restrict("#{s:hasRole('admin')}")
    public void createShelf() {
        Shelf shelf = new Shelf();
        Contexts.getConversationContext().set(Constants.SHELF_VARIABLE, shelf);
    }

    /**
	 * Method, that invoked on creation of the new shelf. Only registered users can create new shelves.
	 * @param album - new album
	 *
	 */
    @Restrict("#{s:hasRole('admin')}")
    public void addShelf(Shelf shelf) {
    	if(user.hasShelfWithName(shelf)){
    		Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.SAME_SHELF_EXIST_ERROR);
			return;
    	}
    	validationSuccess = true;
    	try{
    		shelf.setCreated(new Date());
    		shelfAction.addShelf(shelf);
		}catch(Exception e){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.SHELF_SAVING_ERROR);
			return;
		}
        Events.instance().raiseEvent(Constants.SHELF_ADDED_EVENT, shelf);
    }

    /**
	 * Method, that invoked when user click 'Edit shelf' button or by inplaceInput component. Only registered users can edit shelves.
	 * @param shelf - shelf to edit
	 * @param editFromInplace - indicate whether edit process was initiated by inplaceInput component
	 *
	 */
    @Restrict("#{s:hasRole('admin')}")
    public void editShelf(Shelf shelf, boolean editFromInplace) {
    	try{
    		if(user.hasShelfWithName(shelf)){
    			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.SAME_SHELF_EXIST_ERROR);
    				shelfAction.resetShelf(shelf);
    			return;
    		}
    		if(editFromInplace){
    			//We need validate shelf name manually
    			ClassValidator<Shelf> shelfValidator = new ClassValidator<Shelf>(Shelf.class );
        		InvalidValue[] validationMessages = shelfValidator.getInvalidValues(shelf, "name");
        		if(validationMessages.length > 0 ){
        			for(InvalidValue i : validationMessages){
        				Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, i.getMessage());
        			}
        			//If error occured we need refresh shelf to display correct value in inplaceInput
        			shelfAction.resetShelf(shelf);
    				return;
        		}
			}
    		shelfAction.editShelf(shelf);
		}catch(Exception e){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.SHELF_SAVING_ERROR);
			shelfAction.resetShelf(shelf);
			return;
		}
        Events.instance().raiseEvent(Constants.SHELF_EDITED_EVENT, shelf);
    }

    /**
	 * Method, that invoked when user click 'Delete shelf' button. Only registered users can delete shelves.
	 * @param image - shelf to delete
	 *
	 */
    @Restrict("#{s:hasRole('admin')}")
    public void deleteShelf(Shelf shelf) {
    	String pathToDelete = shelf.getPath();
    	try{
    		shelfAction.deleteShelf(shelf);
		}catch(Exception e){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.SHELF_DELETING_ERROR);
			return;
		}
        Events.instance().raiseEvent(Constants.SHELF_DELETED_EVENT, shelf, pathToDelete);
    }

    /**
	 * This method used to populate 'pre-defined shelves' tree
	 * 
	 * @return List of predefined shelves
	 *
	 */
    public List<Shelf> getPredefinedShelves() {
        if (shelves == null) {
            shelves = shelfAction.getPredefinedShelves();
        }
        return shelves;
    }
    
    /**
	 * This method used to populate 'my shelves' tree
	 * 
	 * @return List of users shelves
	 *
	 */
    public List<Shelf> getUserShelves(){
    	return user.getShelves();
    }
    
	public boolean isValidationSuccess() {
		return validationSuccess;
	}

	public void setValidationSuccess(boolean validationSuccess) {
		this.validationSuccess = validationSuccess;
	}
}