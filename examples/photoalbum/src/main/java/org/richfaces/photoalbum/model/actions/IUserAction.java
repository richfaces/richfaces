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

import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.util.PhotoAlbumException;

/**
 * Interface for manipulating with user entity
 *
 * @author Andrey Markhel
 */

@Local
public interface IUserAction {
    //public User login(String username, String password);

    public void register(User user) throws PhotoAlbumException;

    public boolean isUserExist(String login);

    public User updateUser(User user) throws PhotoAlbumException;

    public User refreshUser();

    public boolean isEmailExist(String email);
}