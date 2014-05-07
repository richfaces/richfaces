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
package org.richfaces.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.jboss.test.faces.FacesEnvironment;
import org.jboss.test.faces.FacesEnvironment.FacesRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Nick Belaevski
 *
 */
public class UserResourcesTestCase {
    protected static class BaseUserResource extends AbstractUserResource {
        public void encode(FacesContext facesContext) throws IOException {
            facesContext.getResponseWriter().write(getClass().getSimpleName());
        }

        public String getContentType() {
            return "text/plain";
        }
    }

    protected static class BaseJava2DUserResource extends AbstractJava2DUserResource {
        public BaseJava2DUserResource() {
            super(new Dimension(10, 5));
        }

        public void paint(Graphics2D graphics2d) {
        }
    }

    @DynamicUserResource(versioned = false)
    public static final class NonVersionedUserResource extends BaseUserResource {
    }

    @DynamicUserResource(versioned = false)
    public static final class NonVersionedJava2DUserResource extends BaseJava2DUserResource {
    }

    @DynamicUserResource(cacheable = false)
    public static final class NonCacheableUserResource extends BaseUserResource {
    }

    @DynamicUserResource(cacheable = false)
    public static final class NonCacheableJava2DUserResource extends BaseJava2DUserResource {
    }

    @DynamicUserResource
    public static final class DefaultSettingsUserResource extends BaseUserResource {
    }

    @DynamicUserResource
    public static final class DefaultSettingsJava2DUserResource extends BaseJava2DUserResource {
    }

    @DynamicUserResource
    public static final class SettingsOverridableResource extends BaseUserResource implements VersionedResource,
        CacheableResource {
        public boolean isCacheable(FacesContext context) {
            return Boolean.TRUE.equals(context.getAttributes().get(CACHEABLE_OVERRIDE_KEY));
        }

        public Date getExpires(FacesContext context) {
            return null;
        }

        public int getTimeToLive(FacesContext context) {
            return 0;
        }

        public String getEntityTag(FacesContext context) {
            return null;
        }

        public String getVersion() {
            return (String) FacesContext.getCurrentInstance().getAttributes().get(VERSION_OVERRIDE_KEY);
        }
    }

    private static final String PACKAGE_VERSION = "test-package-1.0";
    private static final String CACHEABLE_OVERRIDE_KEY = "org.richfaces.test.CacheableOverrideKey";
    private static final String VERSION_OVERRIDE_KEY = "org.richfaces.test.VersionOverrideKey";
    private FacesEnvironment environment;
    private FacesRequest facesRequest;
    private ResourceHandler resourceHandler;

    @Before
    public void setUp() throws Exception {
        environment = FacesEnvironment.createEnvironment();
        environment.getServer().addInitParameter("org.richfaces.resourceDefaultVersion", PACKAGE_VERSION);
        environment.start();

        facesRequest = environment.createFacesRequest();
        facesRequest.start();

        resourceHandler = FacesContext.getCurrentInstance().getApplication().getResourceHandler();
    }

    @After
    public void tearDown() throws Exception {
        resourceHandler = null;

        facesRequest.release();
        facesRequest = null;

        environment.release();
        environment = null;
    }

    private void checkResource(Resource resource, boolean cacheable, String version) {
        assertTrue(resource instanceof VersionedResource);
        assertEquals(version, ((VersionedResource) resource).getVersion());

        assertTrue(resource instanceof CacheableResource);
        assertEquals(cacheable, ((CacheableResource) resource).isCacheable(FacesContext.getCurrentInstance()));
    }

    @Test
    @Ignore
    public void testResources() throws Exception {
        checkResource(resourceHandler.createResource(DefaultSettingsUserResource.class.getName()), true, PACKAGE_VERSION);
        checkResource(resourceHandler.createResource(NonCacheableUserResource.class.getName()), false, PACKAGE_VERSION);
        checkResource(resourceHandler.createResource(NonVersionedUserResource.class.getName()), true, null);
    }

    @Test
    @Ignore
    public void testJava2DResources() throws Exception {
        checkResource(resourceHandler.createResource(DefaultSettingsJava2DUserResource.class.getName()), true, PACKAGE_VERSION);
        checkResource(resourceHandler.createResource(NonCacheableJava2DUserResource.class.getName()), false, PACKAGE_VERSION);
        checkResource(resourceHandler.createResource(NonVersionedJava2DUserResource.class.getName()), true, null);
    }

    @Test
    public void testSettingsOverridableResource() throws Exception {
        Map<Object, Object> attributes = FacesContext.getCurrentInstance().getAttributes();

        attributes.put(CACHEABLE_OVERRIDE_KEY, true);
        attributes.put(VERSION_OVERRIDE_KEY, "some.version");

        checkResource(resourceHandler.createResource(SettingsOverridableResource.class.getName()), true, "some.version");

        attributes.put(CACHEABLE_OVERRIDE_KEY, false);
        attributes.put(VERSION_OVERRIDE_KEY, null);

        checkResource(resourceHandler.createResource(SettingsOverridableResource.class.getName()), false, null);
    }
}
