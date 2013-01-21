/*
 * $Id$
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.ajax4jsf.javascript;

import java.io.IOException;

import org.richfaces.resource.ResourceKey;

import com.google.common.collect.ImmutableList;

/**
 * <p class="changed_added_4_0">
 * Wrapper object that adds dependencies to any object whether it implements {@link ScriptString} or not.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class JSWithDependencies extends ScriptStringBase implements ScriptWithDependencies {
    private final Iterable<ResourceKey> resources;
    private final Object wrapped;

    public JSWithDependencies(Object wrapped, Iterable<ResourceKey> resources) {
        this.wrapped = wrapped;
        this.resources = ImmutableList.copyOf(resources);
    }

    public JSWithDependencies(Object wrapped, ResourceKey... resources) {
        this.wrapped = wrapped;
        this.resources = ImmutableList.copyOf(resources);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.resource.ResourceLibrary#getResources()
     */
    public Iterable<ResourceKey> getResources() {
        return resources;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.javascript.ScriptString#appendScript(java.lang.Appendable)
     */
    public void appendScript(Appendable target) throws IOException {
        ScriptUtils.appendScript(target, wrapped);
    }
}
