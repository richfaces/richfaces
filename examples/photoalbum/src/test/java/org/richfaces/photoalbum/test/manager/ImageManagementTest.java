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

import java.util.Date;
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
import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.model.actions.IImageAction;
import org.richfaces.photoalbum.model.actions.ImageAction;
import org.richfaces.photoalbum.model.event.SimpleEvent;
import org.richfaces.photoalbum.test.PhotoAlbumTestHelper;
import org.richfaces.photoalbum.util.ApplicationUtils;
import org.richfaces.photoalbum.util.PhotoAlbumException;

@RunWith(Arquillian.class)
public class ImageManagementTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Image.class.getPackage())
            .addClasses(ImageAction.class, IImageAction.class).addClass(PhotoAlbumTestHelper.class)
            .addClass(PhotoAlbumException.class).addClasses(ApplicationUtils.class, SimpleEvent.class)
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

    @Before
    public void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }

    private List<Comment> getAllCommentsById(long id) {
        String fetchAllComments = "select c from Comment c where image_id = :id order by c.id";

        return em.createQuery(fetchAllComments, Comment.class).setParameter("id", id).getResultList();
    }

    @Test
    public void isImageAdded() throws Exception {

        int originalSize = helper.getAllImages(em).size();

        Album album = em.createQuery("select a from Album a where a.id = :id", Album.class).setParameter("id", (long) 0)
            .getSingleResult();
        Image newImage = new Image();

        newImage.setName("839245545_5db77619d5_o.jpg");
        newImage.setPath("839245545_5db77619d5_o.jpg");
        newImage.setDescription("Animals - 839245545_5db77619d5_o.jpg image");
        newImage.setCreated(new Date());
        newImage.setAlbum(album);
        newImage.setCameraModel("Canon PowerShot SX110 IS");
        newImage.setSize(1917);
        newImage.setWidth(1024);
        newImage.setHeight(768);
        newImage.setAllowComments(true);
        newImage.setShowMetaInfo(true);

        ia.addImage(newImage);

        List<Image> images = helper.getAllImages(em);
        Assert.assertTrue(images.contains(newImage));
        Assert.assertEquals(originalSize + 1, images.size());
    }

    @Test
    public void isCommentAdded() throws Exception {
        Image image = helper.getAllImages(em).get(0);
        User user = em.createQuery("select u from User u where u.id = :id", User.class).setParameter("id", (long) 1)
            .getSingleResult();

        int originalSize = helper.getAllComments(em).size();

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setDate(new Date());
        comment.setMessage("beautiful");
        comment.setImage(image);

        if (!image.isAllowComments()) {
            image.setAllowComments(true);
        }

        ia.addComment(comment);

        Assert.assertTrue(getAllCommentsById(image.getId()).contains(comment));
        Assert.assertEquals(originalSize + 1, helper.getAllComments(em).size());
    }

    @Test
    public void isCommentDeleted() throws Exception {
        Image image = helper.getAllImages(em).get(0);
        int originalSize = helper.getAllComments(em).size();
        Comment comment = getAllCommentsById(image.getId()).get(0);

        ia.deleteComment(comment);

        Assert.assertFalse(getAllCommentsById(image.getId()).contains(comment));
        Assert.assertEquals(originalSize - 1, helper.getAllComments(em).size());
    }

    @Test(expected = PhotoAlbumException.class)
    public void isCommentNotAllowed() throws Exception {
        Image image = helper.getAllImages(em).get(0);
        User user = em.createQuery("select u from User u where u.id = :id", User.class).setParameter("id", (long) 1)
            .getSingleResult();

        image.setAllowComments(false);

        int originalSize = helper.getAllComments(em).size();

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setDate(new Date());
        comment.setMessage("beautiful");
        comment.setImage(image);

        ia.addComment(comment);
        // the code below should not get executed

        Assert.assertFalse(getAllCommentsById(image.getId()).contains(comment));
        Assert.assertEquals(originalSize, helper.getAllComments(em).size());
    }

    @Test
    public void isImageEdited() throws Exception {
        Image image = helper.getAllImages(em).get(0);

        String name = image.getName();

        image.setName("edited image");

        int originalSize = helper.getAllImages(em).size();

        // due to auto-commit this command makes no difference
        ia.editImage(image, false);

        Image editedImage = helper.getAllImages(em).get(0);
        Assert.assertEquals(image.getId(), editedImage.getId());
        Assert.assertEquals("original name: " + name, "edited image", editedImage.getName());
        Assert.assertEquals(originalSize, helper.getAllImages(em).size());
    }

    @Test
    public void isImageEditedWithMetaTags() throws Exception {
        Image image = helper.getAllImages(em).get(0);
        List<MetaTag> tags = image.getImageTags();
        MetaTag removedTag = tags.get(0);

        String metaTagsByImageId = "select m from MetaTag m join m.images i where i.id = :id";
        List<MetaTag> _tagsById = em.createQuery(metaTagsByImageId, MetaTag.class).setParameter("id", image.getId())
            .getResultList();

        Assert.assertTrue(_tagsById.contains(removedTag));
        Assert.assertTrue(removedTag.getImages().contains(image));

        tags.remove(0);
        Assert.assertFalse(tags.contains(removedTag));

        removedTag.removeImage(image);
        Assert.assertFalse(removedTag.getImages().contains(image));
        image.setImageTags(tags);

        ia.editImage(image, true);

        List<MetaTag> tagsById = em.createQuery(metaTagsByImageId, MetaTag.class).setParameter("id", image.getId())
            .getResultList();

        Image editedImage = helper.getAllImages(em).get(0);

        String tagById = "select m from MetaTag m where id = :id";
        MetaTag m = em.createQuery(tagById, MetaTag.class).setParameter("id", removedTag.getId()).getSingleResult();

        Assert.assertFalse(tagsById.contains(removedTag));
        Assert.assertFalse(m.getImages().contains(editedImage));
    }

    @Test
    public void isIdenticalCountCorrect() throws Exception {
        Image image = helper.getAllImages(em).get(1);

        Assert.assertEquals(1, ia.getCountIdenticalImages(image.getAlbum(), image.getPath()).intValue());

        Image newImage = new Image();

        newImage.setName("new Image");
        newImage.setPath(image.getPath());
        newImage.setDescription("new description");
        newImage.setCreated(new Date());
        newImage.setAlbum(image.getAlbum());
        newImage.setCameraModel("Canon PowerShot SX110 IS");
        newImage.setSize(image.getSize());
        newImage.setWidth(image.getWidth());
        newImage.setHeight(image.getHeight());
        newImage.setAllowComments(true);
        newImage.setShowMetaInfo(true);

        ia.addImage(newImage);

        Assert.assertEquals(2, ia.getCountIdenticalImages(image.getAlbum(), image.getPath()).intValue());
    }

    @Test
    public void isMetaTagFoundByName() throws Exception {
        MetaTag tag = helper.getAllMetaTags(em).get(0);

        Assert.assertEquals(tag, ia.getTagByName(tag.getTag()));

        Assert.assertNull(ia.getTagByName("*" + tag.getTag()));
    }

    @Test
    public void areTagsSuggested() throws Exception {
        MetaTag tag = helper.getAllMetaTags(em).get(0);
        String tagName = tag.getTag();

        String[] parts = { tagName.substring(0, 4), tagName.substring(5) };

        List<MetaTag> suggestedTags = ia.getTagsLikeString(parts[0]);
        Assert.assertTrue("suggestion for '" + parts[0] + "'", suggestedTags.contains(tag));
        Assert.assertEquals("suggestion for '" + parts[0] + "'", 1, suggestedTags.size());

        suggestedTags = ia.getTagsLikeString(parts[1]);
        Assert.assertFalse("suggestion for '" + parts[1] + "'", suggestedTags.contains(tag));
        Assert.assertTrue("suggestion for '" + parts[1] + "'", suggestedTags.isEmpty());
    }

    @Test
    public void doesImageWithSamePathExist() throws Exception {
        Album album1 = helper.getAllAlbums(em).get(0);
        Album album2 = helper.getAllAlbums(em).get(1);

        String path = album1.getImages().get(0).getPath();

        Assert.assertTrue(ia.isImageWithThisPathExist(album1, path));

        Assert.assertFalse(ia.isImageWithThisPathExist(album2, path));
        Assert.assertFalse(ia.isImageWithThisPathExist(album1, path + path));
        Assert.assertFalse(ia.isImageWithThisPathExist(album2, path + path));
    }

    @Test
    public void areAllUserCommentsFound() throws Exception {
        User user = helper.getAllUsers(em).get(1);

        List<Comment> userComments = ia.findAllUserComments(user);

        Assert.assertEquals(4, userComments.size());
        for (Comment c : helper.getAllComments(em)) {
            if (c.getAuthor().getId() == user.getId()) {
                Assert.assertTrue(userComments.contains(c));
            }
        }
    }
}