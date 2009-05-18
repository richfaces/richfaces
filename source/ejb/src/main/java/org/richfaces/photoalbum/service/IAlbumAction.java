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
package org.richfaces.photoalbum.service;

import javax.ejb.Local;

import org.richfaces.photoalbum.domain.Album;

/**
 * Interface for manipulating with album entity
 *
 * @author Andrey Markhel
 */

@Local
public interface IAlbumAction {

	void addAlbum(Album album) throws PhotoAlbumException;
	
	void deleteAlbum(Album album) throws PhotoAlbumException;
	
	void editAlbum(Album album) throws PhotoAlbumException;
	
	public void resetAlbum(Album album);

}