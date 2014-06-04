/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.photoalbum.model.actions;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.richfaces.photoalbum.model.Shelf;
import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.PhotoAlbumException;

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
        User owner = em.find(User.class, shelf.getOwner().getId());
        try {
            em.remove(em.merge(shelf));
            owner.removeShelf(shelf);
            em.merge(owner);
            em.flush();

            //shelf.setOwner(owner);
        } catch (Exception e) {
            owner.addShelf(shelf);
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
            em.merge(shelf);
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