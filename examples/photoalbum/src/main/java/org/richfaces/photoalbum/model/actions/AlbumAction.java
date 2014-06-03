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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.richfaces.photoalbum.model.Album;
import org.richfaces.photoalbum.model.Shelf;
import org.richfaces.photoalbum.util.PhotoAlbumException;

/**
 * Class for manipulating with album entity. Analogous to DAO pattern. EJB3 Bean
 *
 * @author Andrey Markhel
 */
@Stateless
public class AlbumAction implements IAlbumAction {

    @Inject
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
        Shelf parentShelf = em.find(Shelf.class, album.getShelf().getId());
        try {
            if (parentShelf == null) {
                return;
            }

            em.remove(em.merge(album));
            parentShelf.removeAlbum(album);
            em.merge(parentShelf);
            em.flush();

            album.setShelf(parentShelf);
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
            em.merge(album);
            em.flush();
        } catch (Exception e) {
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Refresh state of given album
     *
     * @param album - album to Synchronize
     */
    public Album resetAlbum(Album album) {
        Album a = em.merge(album);
        em.refresh(a);
        return a;
    }
}
