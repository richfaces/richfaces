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
import org.richfaces.photoalbum.bean.UserBean;
import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Comment;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.IImageAction;
import org.richfaces.photoalbum.service.ImageAction;
import org.richfaces.photoalbum.service.PhotoAlbumException;
import org.richfaces.photoalbum.test.PhotoAlbumTestHelper;

@RunWith(Arquillian.class)
public class ImageManagementTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(ImageAction.class.getPackage())
            .addPackage(Image.class.getPackage()).addClass(UserBean.class).addClass(PhotoAlbumTestHelper.class)
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
    UserBean userBean;

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

        Album album = em.createQuery("select a from Album a where a.id = :id", Album.class)
            .setParameter("id", (long) 0).getSingleResult();
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
        User user = em.createQuery("select u from User u where u.id = :id", User.class)
            .setParameter("id", (long) 1).getSingleResult();

        int originalSize = helper.getAllComments(em).size();

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setDate(new Date());
        comment.setMessage("beautiful");
        comment.setImage(image);

        ia.addComment(comment);

        Assert.assertTrue(getAllCommentsById(image.getId()).contains(comment));
        Assert.assertEquals(originalSize + 1, helper.getAllComments(em).size());
    }

    @Test
    public void isCommentDeleted() throws Exception {
        Image image = helper.getAllImages(em).get(0);
        int originalSize = helper.getAllComments(em).size();
        Comment comment = getAllCommentsById(image.getId()).get(1);

        ia.deleteComment(comment);

        Assert.assertFalse(getAllCommentsById(image.getId()).contains(comment));
        Assert.assertEquals(originalSize - 1, helper.getAllComments(em).size());
    }

    @Test(expected=PhotoAlbumException.class)
    public void isCommentNotAllowed() throws Exception {
        Image image = helper.getAllImages(em).get(0);
        User user = em.createQuery("select u from User u where u.id = :id", User.class)
            .setParameter("id", (long) 1).getSingleResult();

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

        ia.editImage(image, false);

        Image editedImage = helper.getAllImages(em).get(0);
        Assert.assertEquals("original name: " + name,"edited image", editedImage.getName());
        Assert.assertEquals(originalSize, helper.getAllImages(em).size());
    }

    // TODO Metatags
}
