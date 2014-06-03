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

import javax.ejb.Local;

import org.richfaces.photoalbum.model.Album;
import org.richfaces.photoalbum.util.PhotoAlbumException;

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

    public Album resetAlbum(Album album);

}