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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.richfaces.photoalbum.model.MetaTag;
import org.richfaces.photoalbum.model.event.SimpleEvent;
import org.richfaces.photoalbum.util.ApplicationUtils;

@RunWith(Arquillian.class)
public class MetaTagTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(MetaTag.class.getPackage()).addClass(PhotoAlbumTestHelper.class)
            .addClasses(ApplicationUtils.class, SimpleEvent.class)
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // important
            .addAsWebInfResource("test-ds.xml");
    }

    private static final String[] TAG_NAMES = { "Tag 1", "TAG2", "Tag Three" };

    @Inject
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Before
    public void preparePersistenceTest() throws Exception {
        clearData();
        insertData();
        startTransaction();
    }

    private void clearData() throws Exception {
        utx.begin();
        em.joinTransaction();

        System.out.println("Dumping old records...");
        em.createQuery("delete from MetaTag").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String tag : TAG_NAMES) {
            MetaTag m = new MetaTag();
            m.setTag(tag);
            em.persist(m);
        }
        utx.commit();
        // clear the persistence context (first-level cache)
        em.clear();
    }

    private void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }

    @Test
    public void shouldFindAllMetaTagsUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllMetaTagsInJpql = "select m from MetaTag m order by m.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<MetaTag> metaTags = em.createQuery(fetchingAllMetaTagsInJpql, MetaTag.class).getResultList();

        // then
        System.out.println("Found " + metaTags.size() + " tags (using JPQL):");
        assertContainsAllMetaTags(metaTags);
    }

    private static void assertContainsAllMetaTags(Collection<MetaTag> retrievedMetaTags) {
        Assert.assertEquals(TAG_NAMES.length, retrievedMetaTags.size());
        final Set<String> retrievedMetaTagTitles = new HashSet<String>();
        for (MetaTag m : retrievedMetaTags) {
            retrievedMetaTagTitles.add(m.getTag());
        }
        Assert.assertTrue(retrievedMetaTagTitles.containsAll(Arrays.asList(TAG_NAMES)));
    }

    @Test
    public void idsShouldBeUnique() throws Exception {
        String fetchingAllMetaTagsInJpql = "select m from MetaTag m order by m.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<MetaTag> metaTags = em.createQuery(fetchingAllMetaTagsInJpql, MetaTag.class).getResultList();

        assertUniqueIds(metaTags);
    }

    private static void assertUniqueIds(Collection<MetaTag> retrievedMetaTags) {
        final Set<Long> metaTagIds = new HashSet<Long>();
        for (MetaTag m : retrievedMetaTags) {
            metaTagIds.add(m.getId());
        }
        Assert.assertTrue(metaTagIds.size() == retrievedMetaTags.size());
    }
}