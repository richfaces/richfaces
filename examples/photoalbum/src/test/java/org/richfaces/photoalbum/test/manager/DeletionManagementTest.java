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
package org.richfaces.photoalbum.test.manager;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.photoalbum.model.Album;
import org.richfaces.photoalbum.model.Comment;
import org.richfaces.photoalbum.model.Image;
import org.richfaces.photoalbum.model.MetaTag;
import org.richfaces.photoalbum.model.Shelf;
import org.richfaces.photoalbum.model.actions.AlbumAction;
import org.richfaces.photoalbum.model.actions.IAlbumAction;
import org.richfaces.photoalbum.model.actions.IImageAction;
import org.richfaces.photoalbum.model.actions.IShelfAction;
import org.richfaces.photoalbum.model.actions.IUserAction;
import org.richfaces.photoalbum.model.actions.UserAction;
import org.richfaces.photoalbum.model.event.SimpleEvent;
import org.richfaces.photoalbum.test.PhotoAlbumTestHelper;
import org.richfaces.photoalbum.util.ApplicationUtils;
import org.richfaces.photoalbum.util.PhotoAlbumException;

/**
 * Test for cascade deletion - e.g. after an Image gets deleted, all Comments associated with it should get deleted too.
 *
 * @author mpetrov
 *
 */

@RunWith(Arquillian.class)
public class DeletionManagementTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(AlbumAction.class.getPackage())
            .addPackage(Album.class.getPackage()).addClass(PhotoAlbumTestHelper.class).addClass(PhotoAlbumException.class)
            .deleteClasses(UserAction.class, IUserAction.class)
            .addClasses(ApplicationUtils.class, SimpleEvent.class)
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // important
            .addAsWebInfResource("test-ds.xml").addAsResource("importmin.sql", "import.sql");
    }

    @Inject
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Inject
    PhotoAlbumTestHelper helper;

    @Inject
    IImageAction ia;

    @Inject
    IShelfAction sa;

    @Inject
    IAlbumAction aa;

    @Before
    public void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }

    @Test
    public void areCommentsDeletedWithImage() throws Exception {
        String commentById = "select c from Comment c where image_id = :id";
        Image image = helper.getAllImages(em).get(0);

        Assert.assertNotNull(image);

        int allCommentsSize = helper.getAllComments(em).size();
        int commentsSize = em.createQuery(commentById, Comment.class).setParameter("id", image.getId()).getResultList().size();

        ia.deleteImage(image);

        Assert.assertFalse(helper.getAllImages(em).contains(image));

        List<Comment> comments = em.createQuery(commentById, Comment.class).setParameter("id", image.getId()).getResultList();

        Assert.assertTrue(comments.isEmpty());
        Assert.assertEquals(allCommentsSize - commentsSize, helper.getAllComments(em).size());
    }

    @Test
    public void areMetaTagsNotPointingToDeletedImage() throws Exception {
        String metaTagById = "select m from MetaTag m join m.images i where i.id = :id";
        Image image = helper.getAllImages(em).get(0);
        Assert.assertNotNull(image);

        List<MetaTag> existingMetaTags = em.createQuery(metaTagById, MetaTag.class).setParameter("id", image.getId())
            .getResultList();
        Assert.assertFalse("size: " + existingMetaTags.size(), existingMetaTags.isEmpty());

        ia.deleteImage(image);

        List<MetaTag> metaTags = em.createQuery(metaTagById, MetaTag.class).setParameter("id", image.getId()).getResultList();
        Assert.assertTrue(metaTags.isEmpty());
    }

    @Test
    public void areImagesDeletedWithAlbum() throws Exception {
        String imageById = "select i from Image i where album_id = :id";
        Album album = helper.getAllAlbums(em).get(0);

        Assert.assertNotNull(album);

        int allImagesSize = helper.getAllImages(em).size();
        int imagesSize = em.createQuery(imageById, Image.class).setParameter("id", album.getId()).getResultList().size();

        aa.deleteAlbum(album);

        Assert.assertFalse(helper.getAllAlbums(em).contains(album));

        List<Image> images = em.createQuery(imageById, Image.class).setParameter("id", album.getId()).getResultList();

        Assert.assertTrue(images.isEmpty());
        Assert.assertEquals(allImagesSize - imagesSize, helper.getAllImages(em).size());
    }

    @Test
    public void areAlbumsDeletedWithShelf() throws Exception {
        String albumById = "select a from Album a where shelf_id = :id";
        Shelf shelf = helper.getAllShelves(em).get(0);

        Assert.assertNotNull(shelf);

        int allAlbumsSize = helper.getAllAlbums(em).size();
        int albumsSize = em.createQuery(albumById, Album.class).setParameter("id", shelf.getId()).getResultList().size();

        sa.deleteShelf(shelf);

        Assert.assertFalse(helper.getAllShelves(em).contains(shelf));

        List<Album> albums = em.createQuery(albumById, Album.class).setParameter("id", shelf.getId()).getResultList();

        Assert.assertTrue(albums.isEmpty());
        Assert.assertEquals(allAlbumsSize - albumsSize, helper.getAllAlbums(em).size());
    }
}
