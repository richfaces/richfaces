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
import org.richfaces.photoalbum.bean.UserBean;
import org.richfaces.photoalbum.domain.Sex;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IUserAction;
import org.richfaces.photoalbum.service.UserAction;
import org.richfaces.photoalbum.test.PhotoAlbumTestHelper;

/**
 * Test for user management (creating, searching) performed by UserAction class
 *
 * @author mpetrov
 *
 */
@RunWith(Arquillian.class)
public class UserManagementTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(UserAction.class.getPackage())
            .addPackage(User.class.getPackage()).addClass(UserBean.class)
            .addClass(PhotoAlbumTestHelper.class)
            // insert X as Y
            .addAsResource("importusers.sql", "import.sql")
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // important
            .addAsWebInfResource("test-ds.xml");
    }

    @Inject
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Inject
    PhotoAlbumTestHelper helper;

    @Inject
    IUserAction ua;

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

    @Test
    public void isUserAdded() throws Exception {
        User newUser = new User();

        int originalSize = helper.getAllUsers(em).size();

        newUser.setFirstName("Mike");
        newUser.setSecondName("Johnson");
        newUser.setEmail("mike.johnson@mail.co.uk");
        newUser.setLogin("jmike");
        newUser.setPasswordHash("8cb2237d0679ca88db6464eac60da96345513964");
        newUser.setBirthDate(new Date()); // TODO String -> Date
        newUser.setSex(Sex.MALE);
        newUser.setHasAvatar(false);
        newUser.setPreDefined(false);

        ua.register(newUser);
        List<User> users = helper.getAllUsers(em);
        Assert.assertTrue(users.contains(newUser));
        Assert.assertEquals(originalSize + 1, users.size());
    }

    @Test
    public void canUserLogIn() throws Exception {
        User anotherUser = new User();

        anotherUser.setFirstName("John");
        anotherUser.setSecondName("Tailor");
        anotherUser.setEmail("john.tailor@mail.co.uk");
        anotherUser.setLogin("tailorj");
        anotherUser.setPasswordHash("8cb2237d0679ca88db6464eac60da96345513964");
        anotherUser.setBirthDate(new Date()); // TODO String -> Date
        anotherUser.setSex(Sex.MALE);
        anotherUser.setHasAvatar(false);
        anotherUser.setPreDefined(false);

        ua.register(anotherUser);

        User loggedInUser = userBean.logIn("tailorj", "8cb2237d0679ca88db6464eac60da96345513964");

        Assert.assertEquals(anotherUser, loggedInUser);
    }

    @Test
    public void isUserUpdated() throws Exception {
        userBean.logIn("Noname", "8cb2237d0679ca88db6464eac60da96345513964");

        userBean.getUser().setEmail("mail@mail.net");

        ua.updateUser();

        User updatedUser = (User) em.createNamedQuery(Constants.USER_LOGIN_QUERY)
            .setParameter(Constants.USERNAME_PARAMETER, "Noname")
            .setParameter(Constants.PASSWORD_PARAMETER, "8cb2237d0679ca88db6464eac60da96345513964").getSingleResult();

        Assert.assertTrue("mail: " + updatedUser.getEmail() + " = 'mail@mail.net'",
            "mail@mail.net".equals(updatedUser.getEmail()));
    }

    @Test
    public void isUserRefreshed() throws Exception {
        String originalMail = userBean.getUser().getEmail();
        userBean.getUser().setEmail("mail@mail.org");

        ua.refreshUser();

        userBean.refreshUser();

        Assert.assertTrue("mail: " + userBean.getUser().getEmail() + " = '" + originalMail + "'",
            originalMail.equals(userBean.getUser().getEmail()));
    }

    @Test
    public void doesUserExist_Login() throws Exception {
        Assert.assertTrue(ua.isUserExist("amarkhel"));
        Assert.assertFalse(ua.isUserExist("mpetrov"));
    }

    @Test
    public void doesUserExist_Email() throws Exception {
        Assert.assertTrue(ua.isEmailExist("amarkhel@exadel.com"));
        Assert.assertFalse(ua.isEmailExist("jsmith@mail.net"));
    }
}
