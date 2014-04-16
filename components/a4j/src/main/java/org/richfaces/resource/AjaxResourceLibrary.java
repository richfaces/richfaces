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

import static org.richfaces.application.configuration.ConfigurationServiceHelper.getBooleanConfigurationValue;

import javax.faces.context.FacesContext;

import org.richfaces.configuration.CommonComponentsConfiguration;

import com.google.common.collect.ImmutableList;

/**
 * deprecated in RF-13314
 *
 * @author Nick Belaevski
 *
 */
@Deprecated
public class AjaxResourceLibrary implements ResourceLibrary {

    private static final ImmutableList<ResourceKey> AJAX_ONLY_KEYS = ImmutableList.of(
        ResourceKey.create("jsf.js", "javax.faces"),
        ResourceKey.create("jquery.js", "org.richfaces"),
        ResourceKey.create("richfaces.js", "org.richfaces"));

    private static final ImmutableList<ResourceKey> AJAX_WITH_QUEUE_KEYS = ImmutableList.<ResourceKey>builder()
        .addAll(AJAX_ONLY_KEYS)
        .add(ResourceKey.create("richfaces-queue.js", "org.richfaces")).build();

    public Iterable<ResourceKey> getResources() {
        // TODO - initialize at creation.
        if (getBooleanConfigurationValue(FacesContext.getCurrentInstance(), CommonComponentsConfiguration.Items.queueEnabled)) {
            return AJAX_WITH_QUEUE_KEYS;
        }

        return AJAX_ONLY_KEYS;
    }
}
