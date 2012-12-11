/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.integration.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Set;

import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

/**
 * Builder for creating FaceletAsset from components.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AssetBuilder {

    private final FaceletAsset fa = new FaceletAsset();
    private final ComponentsList componentList = new ComponentsList();
    private final ComponentsList inFormComponentList = new ComponentsList();

    public AssetBuilder addComponents(Component... components) {
        Preconditions.checkNotNull(components);
        Preconditions.checkArgument(components.length > 0);
        componentList.addAll(Arrays.asList(components));
        return this;
    }

    public AssetBuilder addComponents(ComponentsList components) {
        Preconditions.checkNotNull(components);
        Preconditions.checkArgument(components.size() > 0);
        componentList.addAll(components);
        return this;
    }

    public AssetBuilder addComponentsToForm(Component... components) {
        Preconditions.checkNotNull(components);
        Preconditions.checkArgument(components.length > 0);
        inFormComponentList.addAll(Arrays.asList(components));
        return this;
    }

    public AssetBuilder addComponentsToForm(ComponentsList components) {
        Preconditions.checkNotNull(components);
        Preconditions.checkArgument(components.size() > 0);
        inFormComponentList.addAll(components);
        return this;
    }

    private void _addComponents() {
        fa.body(componentList);
        fa.form(inFormComponentList);
    }

    private void _addNamespacesFromComponents() {
        Set<XMLNS> namespaces = Sets.<XMLNS>newLinkedHashSet();
        Iterable<Component> allComponents = Iterables.concat(componentList, inFormComponentList);
        for (Component component : allComponents) {
            for (XMLNS namespace : component.getAllNamespaces()) {
                namespaces.add(namespace);

            }
        }
        if (!namespaces.isEmpty()) {
            for (XMLNS namespace : namespaces) {
                fa.xmlns(namespace.name, namespace.URI);
            }
        }
    }

    public FaceletAsset build() {
        _addNamespacesFromComponents();
        _addComponents();
        return fa;
    }
}
