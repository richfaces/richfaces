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
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.richfaces.log.Logger;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.optimizer.ResourceLibraryExpander;
import org.richfaces.resource.optimizer.ordering.IllegalPartialOrderingException;
import org.richfaces.resource.optimizer.ordering.PartialOrderToCompleteOrder;
import org.richfaces.resource.optimizer.resource.scan.ResourcesScanner;
import org.richfaces.resource.optimizer.resource.scan.impl.reflections.ReflectionsExt;
import org.richfaces.resource.optimizer.vfs.VFSRoot;
import org.richfaces.resource.optimizer.vfs.VFSType;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

/**
 * @author Nick Belaevski
 *
 */
public class ResourceOrderingScanner implements ResourcesScanner {
    private static final Function<ResourceDependency, ResourceKey> RESOURCE_DEPENDENCY_TO_KEY = new Function<ResourceDependency, ResourceKey>() {
        @Override
        public ResourceKey apply(ResourceDependency resourceDependency) {
            return new ResourceKey(resourceDependency.name(), resourceDependency.library());
        }
    };

    private static final Predicate<ResourceDependency> RESOURCE_DEPENDENCY_NOT_BODY = new Predicate<ResourceDependency>() {
        @Override
        public boolean apply(ResourceDependency resourceDependency) {
            return !"body".equals(resourceDependency.target());
        }
    };

    private Collection<ResourceKey> resources = Lists.newLinkedList();
    private PartialOrderToCompleteOrder<ResourceKey> ordering = new PartialOrderToCompleteOrder<ResourceKey>();
    private Collection<VFSRoot> cpFiles;
    private Logger log;

    public ResourceOrderingScanner(Collection<VFSRoot> cpFiles, Logger log) {
        super();
        this.cpFiles = cpFiles;
        this.log = log;
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
        configurationBuilder.setScanners(new SubTypesScanner(), new TypeAnnotationsScanner());

        ReflectionsExt refl = new ReflectionsExt(configurationBuilder);

        Collection<Class<?>> allClasses = Sets.newHashSet();
        addAnnotatedClasses(ResourceDependencies.class, refl, allClasses);
        addAnnotatedClasses(ResourceDependency.class, refl, allClasses);

        for (Class<?> annotatedClass : allClasses) {
            List<ResourceDependency> resourceDependencies = Lists.newLinkedList();
            if (annotatedClass.getAnnotation(ResourceDependency.class) != null) {
                resourceDependencies.add(annotatedClass.getAnnotation(ResourceDependency.class));
            }
            if (annotatedClass.getAnnotation(ResourceDependencies.class) != null) {
                resourceDependencies.addAll(Arrays.asList(annotatedClass.getAnnotation(ResourceDependencies.class).value()));
            }
            try {
                log.debug(annotatedClass.toString());
                addResourceDependencies(resourceDependencies);
            } catch (IllegalPartialOrderingException e) {
                throw new IllegalStateException("Exception caught when scanning " + annotatedClass, e);
            }
        }

        resources = ordering.getCompletelyOrderedItems();
    }

    private void addResourceDependencies(List<ResourceDependency> resourceDependencies) {
        Collection<ResourceDependency> filteredResourceDependencies = Collections2.filter(resourceDependencies, RESOURCE_DEPENDENCY_NOT_BODY);
        Collection<ResourceKey> resourceKeys = Collections2.transform(filteredResourceDependencies, RESOURCE_DEPENDENCY_TO_KEY);
        resourceKeys = new ResourceLibraryExpander().expandResourceLibraries(resourceKeys);
        log.debug(resourceKeys.toString());
        ordering.addPartialOrdering(resourceKeys);
    }

    private void addAnnotatedClasses(Class<? extends Annotation> annotationClass, ReflectionsExt refl,
            Collection<Class<?>> allClasses) {
        for (Class<?> annotatedClass : refl.getTypesAnnotatedWith(annotationClass)) {
            allClasses.add(annotatedClass);
        }
    }

    public Collection<ResourceKey> getResources() {
        return resources;
    }

    public Ordering<ResourceKey> getCompleteOrdering() {
        return ordering.getCompleteOrdering();
    }
}
