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
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.IShelfAction;
import org.richfaces.photoalbum.service.ShelfAction;
import org.richfaces.photoalbum.test.PhotoAlbumTestHelper;

@RunWith(Arquillian.class)
public class ShelfManagementTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(ShelfAction.class.getPackage())
            .addPackage(User.class.getPackage()).addClass(UserBean.class).addClass(PhotoAlbumTestHelper.class)
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // important
            .addAsWebInfResource("test-ds.xml").addAsResource("importmin.sql", "import.sql");
    }

    @Inject
    IShelfAction sa;

    @Inject
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Inject
    PhotoAlbumTestHelper helper;

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
    public void isShelfAdded() throws Exception {
        User user = helper.getAllUsers(em).get(0);

        Shelf newShelf = new Shelf();

        newShelf.setName("new shelf");
        newShelf.setDescription("a new shelf");
        newShelf.setCreated(new Date());
        newShelf.setShared(false);
        newShelf.setOwner(user);

        int originalSize = helper.getAllShelves(em).size();

        sa.addShelf(newShelf);
        List<Shelf> shelves = helper.getAllShelves(em);

        Assert.assertTrue(shelves.contains(newShelf));
        Assert.assertEquals(originalSize + 1, shelves.size());
    }

    @Test
    public void isShelfEdited() throws Exception {
        Shelf shelf = helper.getAllShelves(em).get(0);

        shelf.setName("edited shelf");

        int originalSize = helper.getAllShelves(em).size();

        sa.editShelf(shelf);

        Shelf editedShelf = helper.getAllShelves(em).get(0);
        Assert.assertEquals(shelf.getId(), editedShelf.getId());
        Assert.assertEquals("edited shelf", editedShelf.getName());
        Assert.assertEquals(originalSize, helper.getAllShelves(em).size());
    }

    @Test
    public void arePredefinedShelvesFound() throws Exception {
        // predefined shelf: is shared and owned by a predefined user

        User preDefined = helper.getAllUsers(em).get(0);
        User notPreDefined = helper.getAllUsers(em).get(1);

        Shelf shelf = helper.getAllShelves(em).get(0); // this is a predefined shelf

        Shelf shelf1 = new Shelf();
        Shelf shelf2 = new Shelf();
        Shelf shelf3 = new Shelf();
        Shelf shelf4 = new Shelf();

        shelf1.setName("shelf1");
        shelf1.setDescription("predefined and shared shelf");
        shelf1.setCreated(new Date());
        shelf1.setShared(true);
        shelf1.setOwner(preDefined);

        shelf2.setName("shelf2");
        shelf2.setDescription("not predefined and shared shelf");
        shelf2.setCreated(new Date());
        shelf2.setShared(true);
        shelf2.setOwner(notPreDefined);

        shelf3.setName("shelf3");
        shelf3.setDescription("predefined and not shared shelf");
        shelf3.setCreated(new Date());
        shelf3.setShared(false);
        shelf3.setOwner(preDefined);

        shelf4.setName("shelf4");
        shelf4.setDescription("not predefined and not shared shelf");
        shelf4.setCreated(new Date());
        shelf4.setShared(false);
        shelf4.setOwner(notPreDefined);

        sa.addShelf(shelf1);
        sa.addShelf(shelf2);
        sa.addShelf(shelf3);
        sa.addShelf(shelf4);

        List<Shelf> allShelves = helper.getAllShelves(em);

        Assert.assertTrue(allShelves.contains(shelf1));
        Assert.assertTrue(allShelves.contains(shelf2));
        Assert.assertTrue(allShelves.contains(shelf3));
        Assert.assertTrue(allShelves.contains(shelf4));

        List<Shelf> predefinedShelves = sa.getPredefinedShelves();

        Assert.assertEquals(2, predefinedShelves.size());
        Assert.assertTrue(predefinedShelves.contains(shelf));
        Assert.assertTrue(predefinedShelves.contains(shelf1));
    }
}
