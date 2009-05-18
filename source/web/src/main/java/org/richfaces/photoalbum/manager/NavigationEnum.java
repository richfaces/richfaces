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
 * Class encapsulated all possible states, that can be applied to so called 'mainArea' area on the page.
 * This ensured that properly template will be applied, and user will be redirected to desired page.
 * Next template to show obviously determined in Controller and pushes to Model.
 * @author Andrey Markhel
 */

public enum NavigationEnum {
	ANONYM("includes/publicShelves.xhtml"),
	FILE_UPLOAD("includes/fileUpload.xhtml"),
	USER_PREFS("includes/userPrefs.xhtml"),
	REGISTER("includes/register.xhtml"),
	SEARCH("includes/search.xhtml"),
	ALBUM_PREVIEW("includes/album.xhtml"),
	ALBUM_IMAGE_PREVIEW("/includes/image.xhtml"),
	SHELF_PREVIEW("/includes/shelf.xhtml"),
	ALL_SHELFS("/includes/userShelves.xhtml"),
	TAGS("includes/tag.xhtml"),
	ALL_ALBUMS("/includes/userAlbums.xhtml"),
	ALL_IMAGES("/includes/userImages.xhtml"),
	ALBUM_IMAGE_EDIT("/includes/imageEdit.xhtml"),
	ALBUM_EDIT("/includes/albumEdit.xhtml"),
	SHELF_EDIT("/includes/shelfEdit.xhtml"),
	SHELF_UNVISITED("/includes/shelfUnvisited.xhtml"),
	USER_SHARED_ALBUMS("/includes/userSharedAlbums.xhtml"),
	USER_SHARED_IMAGES("/includes/userSharedImages.xhtml"),
	ALBUM_UNVISITED("/includes/albumUnvisited.xhtml");
	
	private NavigationEnum(String t){
		template=t;
	}
	
	private String template;
	
	public String getTemplate() {
		return template;
	}
}
