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
import org.richfaces.photoalbum.model.Image;
import org.richfaces.photoalbum.model.event.ImageEvent;
import org.richfaces.photoalbum.model.event.SimpleEvent;
import org.richfaces.photoalbum.util.ApplicationUtils;
import org.richfaces.photoalbum.util.PhotoAlbumException;

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
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addPackage(ImageEvent.class.getPackage())
            .addPackage(Image.class.getPackage()).addClass(PhotoAlbumTestHelper.class)
            .addClasses(ApplicationUtils.class, SimpleEvent.class)
            .addClass(PhotoAlbumException.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // important
            .addAsWebInfResource("test-ds.xml").addAsResource("importmin.sql", "import.sql");
    }

    @Inject
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Inject
    PhotoAlbumTestHelper helper;

    private final int numberOfUsers = 2;
    private final int numberOfShelves = 1;
    private final int numberOfAlbums = 2;
    private final int numberOfImages = 9;
    private final int numberOfMetaTags = 2;
    private final int numberOfComments = 13;

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
