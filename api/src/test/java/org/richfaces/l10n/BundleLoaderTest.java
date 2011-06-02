/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.l10n;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.MissingResourceException;

import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Nick Belaevski
 *
 */
public class BundleLoaderTest {
    private MockFacesEnvironment facesEnvironment;
    private BundleLoader bundleLoader;

    @Before
    public void setUp() throws Exception {
        Locale.setDefault(Locale.ENGLISH);

        bundleLoader = new BundleLoader();

        facesEnvironment = MockFacesEnvironment.createEnvironment().withApplication();
    }

    @After
    public void tearDown() throws Exception {
        bundleLoader = null;

        facesEnvironment.verify();
        facesEnvironment.release();

        facesEnvironment = null;
    }

    @Test
    public void testGetMessageBundle() throws Exception {
        facesEnvironment.replay();

        assertEquals("Hello", bundleLoader.getBundle(BundleLoaderCoreMessages.message, Locale.US).getString("message"));
        assertEquals("Zdravstvujte", bundleLoader.getBundle(BundleLoaderCoreMessages.message, new Locale("ru", "RU"))
            .getString("message"));

        try {
            bundleLoader.getBundle(BundleLoaderCoreMessages.message, new Locale("by", "BY"));
            fail();
        } catch (MissingResourceException e) {
            // ok
        }
    }

    @Test
    public void testGetApplicationBundle() throws Exception {
        expect(facesEnvironment.getApplication().getMessageBundle()).andStubReturn("org.richfaces.l10n.AppMessages");

        facesEnvironment.replay();

        assertEquals("Welcome to app",
            bundleLoader.getApplicationBundle(facesEnvironment.getFacesContext(), BundleLoaderCoreMessages.message, Locale.US)
                .getString("message"));
        assertEquals(
            "Dobro pozhalovat'",
            bundleLoader.getApplicationBundle(facesEnvironment.getFacesContext(), BundleLoaderCoreMessages.message,
                new Locale("by", "BY")).getString("message"));

        try {
            bundleLoader.getApplicationBundle(facesEnvironment.getFacesContext(), BundleLoaderCoreMessages.message, new Locale(
                "ru", "RU"));
            fail();
        } catch (MissingResourceException e) {
            // ok
        }
    }
}
