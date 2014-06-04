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

package org.richfaces.photoalbum.model;

//import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * <p>
 * The {@link MediaType} describes the types of media this application can handle and render.
 * 
 * <p>
 * The media type is a <em>closed set</em> - as each different type of media requires support coded into the view layers, it
 * cannot be expanded upon without rebuilding the application. It is therefore represented by an enumeration. When used, you
 * should instruct JPA to store the enum value using it's String representation, so that we can later reorder the enum members,
 * without changing the data. Of course, this does mean we can't change the names of media items once the app is put into
 * production. To do this add <code>@Enumerated(STRING)</code> to the field declaration.
 * </p>
 * 
 * <p>
 * The {@link MediaType} also describes whether this type of media can be cached locally, and used when there is no internet
 * connection. For example images and or an mpeg video file can be cached, whilst a video streamed across the internet cannot.
 * </p>
 * 
 * @author Pete Muir
 * 
 */
//@Portable
public enum MediaType {

    /**
     * The types of media the application can currently handle. Right now, it can only handle images. We plan to add support for
     * streamed videos in the next development round.
     */
    IMAGE("Image", true);
    
    /**
     * A human readable description of the media type.
     */
    private final String description;
    
    /**
     * A boolean flag indicating whether the media type can be cached.
     */
    private final boolean cacheable;
    
    /* Boilerplate constructor and getters */

    private MediaType(String description, boolean cacheable) {
        this.description = description;
        this.cacheable = cacheable;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCacheable() {
        return cacheable;
    }

}