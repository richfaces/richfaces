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

package org.richfaces.demo.tree.adaptors;

import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class FileSystemNode {
    private static final Function<String, FileSystemNode> FACTORY = new Function<String, FileSystemNode>() {
        public FileSystemNode apply(String from) {
            return new FileSystemNode(from.substring(0, from.length() - 1));
        }

        ;
    };
    private static final Function<String, String> TO_SHORT_PATH = new Function<String, String>() {
        public String apply(String from) {
            int idx = from.lastIndexOf('/');

            if (idx < 0) {
                return from;
            }

            return from.substring(idx + 1);
        }

        ;
    };
    private String path;
    private List<FileSystemNode> directories;
    private List<String> files;
    private String shortPath;

    public FileSystemNode(String path) {
        this.path = path;
        int idx = path.lastIndexOf('/');
        if (idx != -1) {
            shortPath = path.substring(idx + 1);
        } else {
            shortPath = path;
        }
    }

    public synchronized List<FileSystemNode> getDirectories() {
        if (directories == null) {
            directories = Lists.newArrayList();

            Iterables.addAll(directories, transform(filter(getResourcePaths(), containsPattern("/$")), FACTORY));
        }

        return directories;
    }

    public synchronized List<String> getFiles() {
        if (files == null) {
            files = new ArrayList<String>();

            Iterables.addAll(files, transform(filter(getResourcePaths(), not(containsPattern("/$"))), TO_SHORT_PATH));
        }

        return files;
    }

    private Iterable<String> getResourcePaths() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Set<String> resourcePaths = externalContext.getResourcePaths(this.path);

        if (resourcePaths == null) {
            resourcePaths = Collections.emptySet();
        }

        return resourcePaths;
    }

    public String getShortPath() {
        return shortPath;
    }
}