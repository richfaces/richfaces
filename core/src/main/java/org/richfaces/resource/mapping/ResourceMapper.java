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
package org.richfaces.resource.mapping;

import org.richfaces.resource.ResourceKey;

/**
 * Allows to programatically configure Resource Mapping, it means for resource key it can decide that the resource is re-mapped to another resource.
 *
 * All the ResourceMappers will be checked to determine mapping - first resource mapper which comes will be used.
 *
 * @author Lukas Fryc
 */
public interface ResourceMapper {

    /**
     * Determines whether resource identified by given key is mapped to another resource.
     *
     * @param resourceKey the key of the resource to be checked for existence of its mapping
     * @return the mapping for a resource given by a key; or null if no mapping is specified by this
     */
    ResourceMapping mapResource(ResourceKey resourceKey);
}
