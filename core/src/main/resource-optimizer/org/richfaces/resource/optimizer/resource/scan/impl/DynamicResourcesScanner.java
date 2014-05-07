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
package org.richfaces.resource.optimizer.resource.scan.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Collection;

import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.richfaces.resource.DynamicUserResource;
import org.richfaces.resource.ResourceFactory;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.optimizer.resource.scan.ResourcesScanner;
import org.richfaces.resource.optimizer.resource.scan.impl.reflections.MarkerResourcesScanner;
import org.richfaces.resource.optimizer.resource.scan.impl.reflections.ReflectionsExt;
import org.richfaces.resource.optimizer.vfs.VFSRoot;
import org.richfaces.resource.optimizer.vfs.VFSType;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

/**
 * @author Nick Belaevski
 *
 */
public class DynamicResourcesScanner implements ResourcesScanner {
    private static final Function<Class<?>, ResourceKey> RESOURCE_LOCATOR_FUNCTION = new Function<Class<?>, ResourceKey>() {
        @Override
        public ResourceKey apply(Class<?> from) {
            ResourceKey key = ResourceKey.create(from.getName());

            return key;
        }
    };
    private static final Predicate<Class<?>> UNINSTANTIATABLE_CLASSES_PREDICATE = new Predicate<Class<?>>() {
        @Override
        public boolean apply(Class<?> input) {

            if (input.isInterface() || Modifier.isAbstract(input.getModifiers())) {
                return false;
            }

            return true;
        }
    };
    private Collection<ResourceKey> resources = Sets.newHashSet();
    private Collection<VFSRoot> cpFiles;
    private ResourceFactory resourceFactory;

    public DynamicResourcesScanner(Collection<VFSRoot> cpFiles, ResourceFactory resourceFactory) {
        super();
        this.cpFiles = cpFiles;
        this.resourceFactory = resourceFactory;
    }

    public void scan() throws IOException {
        Collection<URL> urls = Sets.newHashSet();
        for (VFSRoot cpFile : cpFiles) {
            if (cpFile.getType() == VFSType.zip) {
                if (cpFile.getChild("META-INF/faces-config.xml") == null) {
                    continue;
                }
            }

            URL url = cpFile.toURL();
            urls.add(url);
        }

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder().setUrls(urls);
        configurationBuilder.setScanners(new SubTypesScanner(), new TypeAnnotationsScanner(), new MarkerResourcesScanner());

        ReflectionsExt refl = new ReflectionsExt(configurationBuilder);
        Collection<Class<?>> allClasses = Sets.newHashSet();

        addAnnotatedClasses(DynamicUserResource.class, refl, allClasses);
        allClasses.addAll(refl.getMarkedClasses());

        allClasses = Collections2.filter(allClasses, UNINSTANTIATABLE_CLASSES_PREDICATE);

        resources.addAll(Collections2.transform(allClasses, RESOURCE_LOCATOR_FUNCTION));
        resources.addAll(resourceFactory.getMappedDynamicResourceKeys());
    }

    private void addAnnotatedClasses(Class<? extends Annotation> annotationClass, ReflectionsExt refl,
            Collection<Class<?>> allClasses) {
        // TODO - reflections library doesn't handle @Inherited correctly
        for (Class<?> annotatedClass : refl.getTypesAnnotatedWith(annotationClass)) {
            allClasses.add(annotatedClass);
            allClasses.addAll(refl.getSubTypesOf(annotatedClass));
        }
    }

    public Collection<ResourceKey> getResources() {
        return resources;
    }
}
