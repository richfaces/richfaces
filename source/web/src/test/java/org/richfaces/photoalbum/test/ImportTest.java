package org.richfaces.photoalbum.test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.photoalbum.bean.UserBean;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.service.ImageAction;

/**
 * Simple test to check if everything got imported correctly from importmin.sql
 *
 * @author mpetrov
 *
 */
@RunWith(Arquillian.class)
public class ImportTest {
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

    private final int numberOfUsers     = 2;
    private final int numberOfShelves   = 1;
    private final int numberOfAlbums    = 2;
    private final int numberOfImages    = 9;
    private final int numberOfMetaTags  = 2;
    private final int numberOfComments  = 13;

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
    public void isEverythingImported() throws Exception {
        Assert.assertEquals("users", numberOfUsers, helper.getAllUsers(em).size());
        Assert.assertEquals("shelves", numberOfShelves, helper.getAllShelves(em).size());
        Assert.assertEquals("albums", numberOfAlbums, helper.getAllAlbums(em).size());
        Assert.assertEquals("images", numberOfImages, helper.getAllImages(em).size());
        Assert.assertEquals("metatags", numberOfMetaTags, helper.getAllMetaTags(em).size());
        Assert.assertEquals("comments", numberOfComments, helper.getAllComments(em).size());
    }

}
