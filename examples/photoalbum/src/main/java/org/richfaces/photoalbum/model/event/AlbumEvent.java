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

package org.richfaces.photoalbum.model.event;

import org.richfaces.photoalbum.model.Album;
/**
 * Album event, carries an album and its (relative) path.
 * Temporary solution before I figure out how to inject the properties.
 * 
 * @author mpetrov
 *
 */
public class AlbumEvent extends SimpleEvent {
    private Album album;
    
    public AlbumEvent(Album album) {
        this(album, "");
    }
    
    public AlbumEvent(Album album, String path) {
        super(path);
        this.album = album;
    }
    
    public Album getAlbum() {
        return album;
    }
    
    public String getPath() {
        return super.getMessage();
    }
}
