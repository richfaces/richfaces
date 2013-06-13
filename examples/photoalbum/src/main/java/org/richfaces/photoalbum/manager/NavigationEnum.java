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
package org.richfaces.photoalbum.manager;

/**
 * Class encapsulated all possible states, that can be applied to so called 'mainArea' area on the page. This ensured that
 * properly template will be applied, and user will be redirected to desired page. Next template to show obviously determined in
 * Controller and pushes to Model.
 * 
 * @author Andrey Markhel
 */

public enum NavigationEnum {
    ANONYM("includes/publicShelves.xhtml"),
    FILE_UPLOAD("includes/fileUpload.xhtml"),
    USER_PREFS("includes/userPrefs.xhtml"),
    REGISTER("includes/register.xhtml"),
    SEARCH("includes/search.xhtml"),
    ALBUM_PREVIEW("includes/album.xhtml"),
    ALBUM_IMAGE_PREVIEW("/includes/image.xhtml"),
    SHELF_PREVIEW("/includes/shelf.xhtml"),
    ALL_SHELFS("/includes/userShelves.xhtml"),
    TAGS("includes/tag.xhtml"),
    ALL_ALBUMS("/includes/userAlbums.xhtml"),
    ALL_IMAGES("/includes/userImages.xhtml"),
    ALBUM_IMAGE_EDIT("/includes/image/imageEditInfo.xhtml"),
    ALBUM_EDIT("/includes/album/albumEditInfo.xhtml"),
    SHELF_EDIT("/includes/shelf/shelfEditInfo.xhtml"),
    SHELF_UNVISITED("/includes/shelfUnvisited.xhtml"),
    USER_SHARED_ALBUMS("/includes/userSharedAlbums.xhtml"),
    USER_SHARED_IMAGES("/includes/userSharedImages.xhtml"),
    ALBUM_UNVISITED("/includes/albumUnvisited.xhtml"),
    FB_ALBUM_PREVIEW("/includes/facebook/album.xhtml"),
    FB_IMAGE_PREVIEW("/includes/facebook/image.xhtml"),
    FB_SHELF("/includes/facebook/shelf.xhtml"),
    GPLUS_SHELF("/includes/gplus/shelf.xhtml"),
    GPLUS_ALBUM_PREVIEW("/includes/gplus/album.xhtml"),
    GPLUS_IMAGE_PREVIEW("/includes/gplus/image.xhtml"),
    EVENT_PREVIEW("/includes/event.xhtml"),
    EVENT_EDIT("/includes/event/EventEditInfo.xhtml");

    private NavigationEnum(String t) {
        template = t;
    }

    private String template;

    public String getTemplate() {
        return template;
    }
}
