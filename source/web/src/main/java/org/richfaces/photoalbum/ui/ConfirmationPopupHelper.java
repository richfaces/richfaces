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
package org.richfaces.photoalbum.ui;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.manager.AlbumManager;
import org.richfaces.photoalbum.manager.ImageManager;
import org.richfaces.photoalbum.manager.ShelfManager;

@Name("confirmationPopupHelper")
@Scope(ScopeType.CONVERSATION)
public class ConfirmationPopupHelper implements Serializable{

	private static final long serialVersionUID = 2561824019376412988L;
	
	private String caption;
	
	private String actionName;
	
	@In @Out private Image image;
	
	@In @Out private Shelf shelf;
	
	@In @Out private Album album;
	
	@In AlbumManager albumManager;
	
	@In ShelfManager shelfManager;
	
	@In ImageManager imageManager;
	
	public void initImagePopup( String actionName, String caption, Image image){
		this.caption = caption;
		this.actionName = actionName;
		this.image = image;
	}
	
	public void initAlbumData( String actionName, String caption, Album album){
		this.caption = caption;
		this.actionName = actionName;
		this.album = album;
	}
	
	public void initShelfData( String actionName, String caption, Shelf shelf){
		this.caption = caption;
		this.actionName = actionName;
		this.shelf = shelf;
	}
	
	public void deleteAlbum(){
		albumManager.deleteAlbum(this.album);
	}
	
	public void deleteShelf(){
		shelfManager.deleteShelf(this.shelf);
	}
	
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public void deleteImage(){
		imageManager.deleteImage(this.image);
	}
}