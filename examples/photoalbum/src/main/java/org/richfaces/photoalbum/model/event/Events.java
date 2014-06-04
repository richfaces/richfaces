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

public enum Events {
    EDIT_USER_EVENT, 
    CANCEL_EDIT_USER_EVENT, 
    AUTHENTICATED_EVENT, 
    USER_DELETED_EVENT, 
    ADD_ERROR_EVENT, // SimpleEvent

    ALBUM_ADDED_EVENT,
    ALBUM_EDITED_EVENT,
    ALBUM_DELETED_EVENT,
    ALBUM_DRAGGED_EVENT, // AlbumEvent

    SHELF_ADDED_EVENT,
    SHELF_EDITED_EVENT,
    SHELF_DELETED_EVENT, // ShelfEvent

    IMAGE_ADDED_EVENT,
    IMAGE_DELETED_EVENT,
    IMAGE_DRAGGED_EVENT, // ImageEvent

    UPDATE_MAIN_AREA_EVENT, // NavEvent

    CHECK_USER_EXPIRED_EVENT,
    CLEAR_FILE_UPLOAD_EVENT,
    START_REGISTER_EVENT,
    CLEAR_EDITOR_EVENT, // SimpleEvent

    // these are never fired (only observed)
    UPDATE_SELECTED_TAG_EVENT,
    STOP_SLIDESHOW_EVENT, // SimpleEvent

    // this is never used (neither fired nor observed)
    ADD_IMAGE_EVENT,
    
    EVENT_ADDED_EVENT,
    EVENT_EDITED_EVENT,
    EVENT_DELETED_EVENT, 
    EVENT_DISPLAYED_EVENT // ShelfEvent
}