/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and individual contributors
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

package org.richfaces.application;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by bleathem on 12/06/14.
 */
public class JsfVersionInspectorTest {

    @Test
    public void testParseString() throws Exception {
        JsfVersionInspector enforcer = new JsfVersionInspector("");
        JsfVersionInspector.Version version;

        version = enforcer.parseVersion("2.0.6");
        Assert.assertEquals(2, version.major);
        Assert.assertEquals(0, version.minor);
        Assert.assertEquals(6, version.micro);
        Assert.assertEquals("", version.qualifier);

        version = enforcer.parseVersion("2.1.27.redhat-9");
        Assert.assertEquals(2, version.major);
        Assert.assertEquals(1, version.minor);
        Assert.assertEquals(27, version.micro);
        Assert.assertEquals("redhat-9", version.qualifier);

        version = enforcer.parseVersion("2.1.4-jbossorg-1");
        Assert.assertEquals(2, version.major);
        Assert.assertEquals(1, version.minor);
        Assert.assertEquals(4, version.micro);
        Assert.assertEquals("jbossorg-1", version.qualifier);

        version = enforcer.parseVersion("2.1.6-SNAPSHOT");
        Assert.assertEquals(2, version.major);
        Assert.assertEquals(1, version.minor);
        Assert.assertEquals(6, version.micro);
        Assert.assertEquals("SNAPSHOT", version.qualifier);


    }

    @Test
    public void testExtractVersion() throws Exception {
        JsfVersionInspector enforcer = new JsfVersionInspector("");
        String version;
        version = enforcer.extractVersion("This sentence contains the version 2.1.4-jbossorg-2 in the middle");
        Assert.assertEquals("2.1.4-jbossorg-2", version);
        version = enforcer.extractVersion("This sentence contains the version 2.2.6 in the middle");
        Assert.assertEquals("2.2.6", version);
    }

    @Test
    public void testTestVersion() throws Exception {
        JsfVersionInspector enforcer = new JsfVersionInspector("");
        JsfVersionInspector.Version version;

        Assert.assertFalse(enforcer.testVersion(enforcer.parseVersion("2.2.4")));
        Assert.assertFalse(enforcer.testVersion(enforcer.parseVersion("2.2.5-jbossorg-2")));
        Assert.assertTrue(enforcer.testVersion(enforcer.parseVersion("2.2.5-jbossorg-3")));
        Assert.assertTrue(enforcer.testVersion(enforcer.parseVersion("2.2.6")));

    }
}
