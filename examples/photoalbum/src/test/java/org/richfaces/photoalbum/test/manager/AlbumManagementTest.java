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
import org.richfaces.photoalbum.model.Shelf;
import org.richfaces.photoalbum.model.actions.AlbumAction;
import org.richfaces.photoalbum.model.actions.IAlbumAction;
import org.richfaces.photoalbum.model.event.SimpleEvent;
import org.richfaces.photoalbum.test.PhotoAlbumTestHelper;
import org.richfaces.photoalbum.util.ApplicationUtils;
import org.richfaces.photoalbum.util.PhotoAlbumException;

@RunWith(Arquillian.class)
public class AlbumManagementTest {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Album.class.getPackage()).addClass(AlbumAction.class)
            .addClass(IAlbumAction.class).addClass(PhotoAlbumTestHelper.class).addClass(PhotoAlbumException.class)
            .addClass(ApplicationUtils.class).addClass(SimpleEvent.class)
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
    public void isAlbumAdded() throws Exception {
        int originalSize = helper.getAllAlbums(em).size();

        Shelf shelf = em.createQuery("select s from Shelf s order by s.id", Shelf.class).getResultList().get(0);

        Assert.assertNotNull(shelf);

        Album newAlbum = new Album();
        newAlbum.setName("new album");
        newAlbum.setDescription("new album description");
        newAlbum.setShelf(shelf);
        newAlbum.setCreated(new Date());

        aa.addAlbum(newAlbum);

        Assert.assertTrue(helper.getAllAlbums(em).contains(newAlbum));
        Assert.assertEquals(originalSize + 1, helper.getAllAlbums(em).size());
    }

    @Test
    public void isAlbumEdited() throws Exception {
        Album album = helper.getAllAlbums(em).get(0);

        album.setName("edited album");

        int originalSize = helper.getAllAlbums(em).size();

        aa.editAlbum(album);

        Album editedAlbum = helper.getAllAlbums(em).get(0);
        Assert.assertEquals(album.getId(), editedAlbum.getId());
        Assert.assertEquals("edited album", editedAlbum.getName());
        Assert.assertEquals(originalSize, helper.getAllAlbums(em).size());
    }

    // EntityManager auto-commits, entities don't need refreshing
}
