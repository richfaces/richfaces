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

/**
 * <p>The library of resources is references as '*.reslib' resource key and it can be represented by '*.library.properties' files under /META-INF/richfaces following JSF's resource library scheme.</p>
 *
 * <p>E.g.:</p>
 *
 * <ul>
 * <li>resource key 'my.library:custom.reslib'</li>
 * <li>is represented by '/META-INF/richfaces/my.library/custom.library.properties' file</li>
 * </ul>
 *
 * <p>library.properties files can either:</p>
 *
 * <ul>
 * <li>define a comma-separated list of resources, e.g. <tt>resources=javax.faces:jsf.js, org.richfaces:richfaces.js</tt></li>
 * <li>or container a reference to a class implementing {@link ResourceLibrary} that will (dynamically) return list of resources contained in the library, e.g. <tt>class=my.library.CustomResourceLibrary</tt></li>
 * </ul>
 *
 * @author Nick Belaevski
 *
 * @see ResourceLibraryFactory
 *
 */
public interface ResourceLibrary {

    /**
     * <p>Returns a list of resources contained in this resource library</p>
     *
     * <p>The result does not have to be stable; i.e. the returned list of resources may differ for each execution based on context, configuration, time, etc.</p>
     */
    Iterable<ResourceKey> getResources();
}
