/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.integration.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.mapping.ResourceAggregator;
import org.richfaces.resource.mapping.ResourceMapper;
import org.richfaces.resource.mapping.ResourceMapping;
import org.richfaces.resource.mapping.ResourcePath;
import org.richfaces.resource.mapping.ResourceServletMapping;

public class Mapper implements ResourceMapper, ResourceAggregator {

    private Map<ResourceKey, String> mapping = new HashMap<ResourceKey, String>();

    public Mapper() {
        mapping.put(ResourceKey.create("some.library:stylesheet.css"), "mapped.library/stylesheet.css");
        mapping.put(ResourceKey.create("another.library:stylesheet.css"), "mapped.library/stylesheet.css");
    }

    @Override
    public ResourceMapping mapResource(ResourceKey resourceKey) {
        final String mapped = mapping.get(resourceKey);

        if (mapped == null) {
            return null;
        }

        return new ResourceMapping() {
            @Override
            public ResourcePath getResourcePath(FacesContext context) {
                return new ResourceServletMapping(new ResourcePath(mapped)).getResourcePath(context);
            }
        };
    }

    @Override
    public Set<ResourceKey> getAggregatedResources(ResourcePath requestPath) {
        return mapping.keySet();
    }
}
