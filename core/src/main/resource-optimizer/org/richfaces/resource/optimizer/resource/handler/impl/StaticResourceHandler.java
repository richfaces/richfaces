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
package org.richfaces.resource.optimizer.resource.handler.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.application.Resource;

import org.richfaces.resource.optimizer.resource.util.ResourceUtil;
import org.richfaces.resource.optimizer.vfs.VirtualFile;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 *
 */
public class StaticResourceHandler extends AbstractResourceHandler {
    private Collection<VirtualFile> roots;

    public StaticResourceHandler(Collection<VirtualFile> roots) {
        super();
        this.roots = roots;
    }

    private Collection<VirtualFile> findLibraries(String libraryName) {
        List<VirtualFile> libraryDirs = Lists.newLinkedList();
        for (VirtualFile file : roots) {
            VirtualFile child = file.getChild(libraryName);
            if (child == null) {
                continue;
            }

            VirtualFile libraryDir = ResourceUtil.getLatestVersion(child, true);
            if (libraryDir != null) {
                libraryDirs.add(libraryDir);
            }
        }

        return libraryDirs;
    }

    private VirtualFile findResource(Collection<VirtualFile> libraryDirs, String resourceName) {
        for (VirtualFile libraryDir : libraryDirs) {
            VirtualFile child = libraryDir.getChild(resourceName);
            if (child != null) {
                VirtualFile resource = ResourceUtil.getLatestVersion(child, false);
                if (resource != null) {
                    return resource;
                }
            }
        }

        return null;
    }

    @Override
    public Resource createResource(String resourceName, String libraryName, String contentType) {
        Collection<VirtualFile> libraryDirs = Collections.emptyList();
        if (!Strings.isNullOrEmpty(libraryName)) {
            libraryDirs = findLibraries(libraryName);
        } else {
            libraryDirs = roots;
        }

        VirtualFile resource = findResource(libraryDirs, resourceName);
        if (resource != null) {
            Resource result = new VFSResource(resource, resource.getRelativePath());

            result.setResourceName(resourceName);
            result.setLibraryName(libraryName);

            if (Strings.isNullOrEmpty(contentType)) {
                result.setContentType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(resourceName));
            } else {
                result.setContentType(contentType);
            }

            return result;
        }

        return null;
    }
}
