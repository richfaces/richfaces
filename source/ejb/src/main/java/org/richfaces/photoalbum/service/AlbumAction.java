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

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Shelf;

/**
 * Class for manipulating with album entity. Analogous to DAO pattern.
 * EJB3 Bean
 *
 * @author Andrey Markhel
 */
@Name("albumAction")
@Stateless
@AutoCreate
public class AlbumAction implements IAlbumAction {

	@In(value = "entityManager")
	EntityManager em;

	/**
	 * Persist album entity to database
	 *
	 * @param album - album to add
	 * @throws PhotoAlbumException
	 */
	public void addAlbum(Album album) throws PhotoAlbumException {
		try {
			em.persist(album);
			//Add to shelf
			album.getShelf().addAlbum(album);
			em.flush();
		} catch (Exception e) {
			throw new PhotoAlbumException(e.getMessage());
		}
	}

	/**
	 * Remove album entity from database
	 *
	 * @param album - album to delete
	 * @throws PhotoAlbumException
	 */
	public void deleteAlbum(Album album) throws PhotoAlbumException {
		Shelf parentShelf = album.getShelf();
		try {
			if(parentShelf == null){
				return;
			}
			album.setCoveringImage(null);
			//Remove from previous shelf 
			parentShelf.removeAlbum(album);
			em.remove(album);
			em.flush();
		} catch (Exception e) {
			parentShelf.addAlbum(album);
			throw new PhotoAlbumException(e.getMessage());
		}
	}

	/**
	 * Synchronize state of album entity with database
	 *
	 * @param album - album to Synchronize
	 * @throws PhotoAlbumException
	 */
	public void editAlbum(Album album) throws PhotoAlbumException {
		try {
			em.flush();
		} catch (Exception e) {
			throw new PhotoAlbumException(e.getMessage());
		}
	}
	
	/**
     * Refresh state of given album
     * @param album - album to Synchronize
     */
	public void resetAlbum(Album album) {
		em.refresh(album);
	}
}
