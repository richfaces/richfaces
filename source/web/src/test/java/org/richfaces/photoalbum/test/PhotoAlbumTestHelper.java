package org.richfaces.photoalbum.test;

import java.util.List;

import javax.persistence.EntityManager;

import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Comment;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.domain.MetaTag;
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;

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
