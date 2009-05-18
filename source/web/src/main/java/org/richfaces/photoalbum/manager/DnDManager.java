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
 * Class encapsulated all functionality, related to drag'n'drop process.
 *
 * @author Andrey Markhel
 */
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.context.AjaxContext;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.core.Events;
import org.richfaces.component.Dropzone;
import org.richfaces.event.DropEvent;
import org.richfaces.event.DropListener;
import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IAlbumAction;

@Name("dndManager")
public class DnDManager implements DropListener {

	@In User user;
	
	@In FileManager fileManager;
	
	@In IAlbumAction albumAction;
	
	/**
	 * Listenet, that invoked  during drag'n'drop process. Only registered users can drag images.
	 *
	 * @param event - event, indicated that drag'n'drop started
	 */
	@Restrict("#{s:hasRole('admin')}")
	public void processDrop(DropEvent dropEvent) {
		Dropzone dropzone = (Dropzone) dropEvent.getComponent();
		Object dragValue = dropEvent.getDragValue();
		Object dropValue = dropzone.getDropValue();
		if(dragValue instanceof Image){
			//If user drag image
			if(!((Album)dropValue).getOwner().getLogin().equals(user.getLogin())){
				//Drag in the album, that not belongs to user
				Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.DND_PHOTO_ERROR);
				return;
			}
			handleImage((Image)dragValue, (Album)dropValue);	
		}else if(dragValue instanceof Album){
			//If user drag album
			if(!((Shelf)dropValue).getOwner().getLogin().equals(user.getLogin())){
				//Drag in the shelf, that not belongs to user
				Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.DND_ALBUM_ERROR);
				return;
			}
			handleAlbum((Album)dragValue, (Shelf)dropValue);
		}
	}

	private void handleAlbum(Album dragValue, Shelf dropValue) {
		String pathOld = dragValue.getPath();
		dropValue.addAlbum(dragValue);
		try{
			albumAction.editAlbum(dragValue);
		}catch(Exception e){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.ERROR_IN_DB);
			return;
		}
		Events.instance().raiseEvent(Constants.ALBUM_DRAGGED_EVENT, dragValue, pathOld);
		addTreeToRerender();
	}

	private void handleImage(Image dragValue, Album dropValue) {
		if(dragValue.getAlbum().equals(dropValue)){
			return;
		}
		String pathOld = dragValue.getFullPath();
		dropValue.addImage(dragValue);
		try{
			albumAction.editAlbum(dropValue);
		}catch(Exception e){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.ERROR_IN_DB);
			return;
		}
		Events.instance().raiseEvent(Constants.IMAGE_DRAGGED_EVENT, dragValue, pathOld);
		addTreeToRerender();
	}
	
	private void addTreeToRerender() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			AjaxContext ac = AjaxContext.getCurrentInstance();
			UIComponent destTree = fc.getViewRoot().findComponent(Constants.TREE_ID);
			ac.addComponentToAjaxRender(destTree);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
