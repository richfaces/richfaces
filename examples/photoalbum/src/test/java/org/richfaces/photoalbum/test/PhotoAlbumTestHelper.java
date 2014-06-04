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
package org.richfaces.photoalbum.test;

import java.util.List;

import javax.persistence.EntityManager;

import org.richfaces.photoalbum.model.Album;
import org.richfaces.photoalbum.model.Comment;
import org.richfaces.photoalbum.model.Image;
import org.richfaces.photoalbum.model.MetaTag;
import org.richfaces.photoalbum.model.Shelf;
import org.richfaces.photoalbum.model.User;

public class PhotoAlbumTestHelper {

    private <T> List<T> getAll(EntityManager em, Class<T> klass, String name) throws Exception {
        String fetchingAll = "select x from " + name + " x order by x.id";

        return em.createQuery(fetchingAll, klass).getResultList();
    }

    public List<User> getAllUsers(EntityManager em) throws Exception {
        return getAll(em, User.class, "User");
    }

    public List<Shelf> getAllShelves(EntityManager em) throws Exception {
        return getAll(em, Shelf.class, "Shelf");
    }

    public List<Album> getAllAlbums(EntityManager em) throws Exception {
        return getAll(em, Album.class, "Album");
    }

    public List<Image> getAllImages(EntityManager em) throws Exception {
        return getAll(em, Image.class, "Image");
    }

    public List<Comment> getAllComments(EntityManager em) throws Exception {
        return getAll(em, Comment.class, "Comment");
    }

    public List<MetaTag> getAllMetaTags(EntityManager em) throws Exception {
        return getAll(em, MetaTag.class, "MetaTag");
    }
}
