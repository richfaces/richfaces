package org.richfaces.photoalbum.event;

public enum Events {
    EDIT_USER_EVENT, // does not carry user
    CANCEL_EDIT_USER_EVENT, // does not carry user
    AUTHENTICATED_EVENT, // might not need to carry user
    // SimpleEvent

    USER_DELETED_EVENT, // needs to carry user
    // UserEvent

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

    CHECK_USER_EXPIRED_EVENT, // might need to carry a HttpSession
    CLEAR_FILE_UPLOAD_EVENT,
    START_REGISTER_EVENT,
    CLEAR_EDITOR_EVENT, // SimpleEvent

    // these are never fired (only observed)
    UPDATE_SELECTED_TAG_EVENT,
    STOP_SLIDESHOW_EVENT, // SimpleEvent

    // this is never used (neither fired nor observed)
    ADD_IMAGE_EVENT
}