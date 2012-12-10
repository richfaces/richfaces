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

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.richfaces.photoalbum.domain.Shelf;

/**
 * Class for manipulating with shelf entity. Analogous to DAO pattern. EJB3 Bean
 *
 * @author Andrey Markhel
 */

@Stateless
public class ShelfAction implements IShelfAction {

    @Inject
    private EntityManager em;

    /**
     * Persist shelf entity to database
     *
     * @param shelf - shelf to add
     * @throws PhotoAlbumException
     */
    public void addShelf(Shelf shelf) throws PhotoAlbumException {
        try {
            em.persist(shelf);
            // Add reference to user
            shelf.getOwner().addShelf(shelf);
            em.flush();
        } catch (Exception e) {
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Remove shelf entity from database
     *
     * @param shelf - shelf to delete
     * @throws PhotoAlbumException
     */
    public void deleteShelf(Shelf shelf) throws PhotoAlbumException {
        try {
            // Remove reference from user
            // user.removeShelf(shelf);
            shelf.getOwner().removeShelf(shelf);
            em.remove(em.merge(shelf)); // need to attach the instance first
            em.flush();
        } catch (Exception e) {
            // user.addShelf(shelf);
            shelf.getOwner().addShelf(shelf);
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Synchronize state of shelf entity with database
     *
     * @param shelf - shelf to Synchronize
     * @throws PhotoAlbumException
     */
    public void editShelf(Shelf shelf) throws PhotoAlbumException {
        try {
            em.flush();
        } catch (Exception e) {
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Return list of shared shelves(pre-defined)
     *
     * @param shelf - shelf to Synchronize
     */
    @SuppressWarnings("unchecked")
    public List<Shelf> getPredefinedShelves() {
        return em.createNamedQuery(Constants.USER_SHELVES_QUERY).getResultList();
    }

    /**
     * Refresh state of given shelf
     *
     * @param shelf - shelf to Synchronize
     */
    public void resetShelf(Shelf shelf) {
        em.refresh(shelf);
    }
}