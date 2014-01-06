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

import static org.richfaces.configuration.ConfigurationServiceHelper.getBooleanConfigurationValue;

import java.util.Collections;

import javax.faces.context.FacesContext;

import org.richfaces.configuration.CommonComponentsConfiguration;

import com.google.common.collect.ImmutableList;

/**
 * This is a modified version of {@link AjaxResourceLibrary} that renders just richfaces-queue.js resource when it is configured.
 *
 * @author Nick Belaevski
 * @author Lukas Fryc
 */
public class QueueResourceLibrary implements ResourceLibrary {
    private static final ImmutableList<ResourceKey> AJAX_WITH_QUEUE_KEYS = ImmutableList.<ResourceKey>builder()
        .add(ResourceKey.create("richfaces-queue.js", "org.richfaces")).build();

    public Iterable<ResourceKey> getResources() {
        if (getBooleanConfigurationValue(FacesContext.getCurrentInstance(), CommonComponentsConfiguration.Items.queueEnabled)) {
            return AJAX_WITH_QUEUE_KEYS;
        }

        return Collections.emptyList();
    }
}
