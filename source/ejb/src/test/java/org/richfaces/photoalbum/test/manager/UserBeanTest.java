package org.richfaces.photoalbum.test.manager;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.photoalbum.bean.UserBean;
import org.richfaces.photoalbum.domain.Sex;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.LoggedIn;
import org.richfaces.photoalbum.service.Resources;

//import org.richfaces.photoalbum.test.TestProducer;

@RunWith(Arquillian.class)
public class UserBeanTest {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addClass(Sex.class)
            .addPackage(User.class.getPackage())
            // .addClass(TestProducer.class)
            .addClass(UserBean.class).addClass(Resources.class).addClass(LoggedIn.class)
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // important
            .addAsWebInfResource("test-ds.xml").addAsResource("importusers.sql", "import.sql");
    }

    @Inject
    UserBean bean;

    @Inject
    EntityManager em;
    
    @Inject
    UserTransaction utx;

    @Test
    public void isLoggedIn() throws Exception {
        User user = (User) em.createNamedQuery(Constants.USER_LOGIN_QUERY).setParameter(Constants.USERNAME_PARAMETER, "Noname")
            .setParameter(Constants.PASSWORD_PARAMETER, "8cb2237d0679ca88db6464eac60da96345513964").getSingleResult();

        Assert.assertNotNull(user);

        User loggedInUser = bean.logIn("Noname", "8cb2237d0679ca88db6464eac60da96345513964");

        Assert.assertNotNull(loggedInUser);
        Assert.assertNotNull(bean.getUser());
        Assert.assertEquals(loggedInUser, bean.getUser());
    }

    @Test
    public void isRefreshed() throws Exception {
        //String originalEmail = bean.getUser().getEmail();

        utx.begin();
        em.joinTransaction();
        em.createQuery("update User u set u.email = :email where u.login = :login").setParameter("email", "mail@mail.net")
            .setParameter("login", "Noname").executeUpdate();

        bean.refreshUser();

        Assert.assertTrue("mail: " + bean.getUser().getEmail() + " = 'mail@mail.net'", "mail@mail.net".equals(bean.getUser().getEmail()));
    }
}
