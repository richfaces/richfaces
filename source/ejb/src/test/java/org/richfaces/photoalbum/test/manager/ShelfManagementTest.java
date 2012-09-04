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
import org.richfaces.photoalbum.domain.Sex;
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.IShelfAction;
import org.richfaces.photoalbum.service.ShelfAction;

// can't run needs injected user
@RunWith(Arquillian.class)
public class ShelfManagementTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addPackage(ShelfAction.class.getPackage())
            .addClass(Sex.class)
            .addPackage(User.class.getPackage())//.addClass(TestProducer.class)
            .addClass(UserBean.class)
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // important
            .addAsWebInfResource("test-ds.xml").addAsResource("importusers.sql", "import.sql");
    }

    @Inject
    IShelfAction sa;

    @Inject
    EntityManager em;

    @Inject
    UserTransaction utx;

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

    private List<Shelf> getAllShelves() {
        String fetchingAllShelves = "select s from Shelf s order by s.id";

        return em.createQuery(fetchingAllShelves, Shelf.class).getResultList();
    }

    @Test
    public void isShelfAdded() throws Exception {
        userBean.logIn("Noname", "8cb2237d0679ca88db6464eac60da96345513964");

        Assert.assertTrue(getAllShelves().isEmpty());

        Shelf newShelf = new Shelf();

        newShelf.setName("new shelf");
        newShelf.setDescription("a new shelf");
        newShelf.setCreated(new Date());
        newShelf.setShared(false);

        sa.addShelf(newShelf);
        Assert.assertEquals(newShelf.getOwner(), userBean.getUser());
        List<Shelf> shelves = getAllShelves();

        Assert.assertTrue(shelves.contains(newShelf));
        Assert.assertEquals(1, shelves.size());
    }
}
