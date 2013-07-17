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
package org.richfaces.photoalbum.service;

/**
 * Utility class. Group of constants
 *
 * @author Andrey Markhel
 */
public class Constants {

    // Errors(Internationalization pending)
    public static final String WRONG_SEARCH_PARAMETERS_ERROR = "Wrong search parameters";
    public static final String YOU_ALREADY_DOWNLOAD_MANY_COPIES_OF_THIS_IMAGE_ERROR = "You already download 256 copies of this image. Please specify another name for uploaded image";
    public static final String SAME_IMAGE_EXIST_ERROR = "You already have an image with the given name in selected album";
    public static final String SAME_ALBUM_EXIST_ERROR = "You already have an album with the given name in selected shelf";
    public static final String SAME_SHELF_EXIST_ERROR = "You already have a shelf with the given name.";
    public static final String USER_WITH_THIS_EMAIL_ALREADY_EXIST = "E-mail is not unique";
    public static final String REGISTRATION_ERROR = "Error while registration process";
    public static final String LOGIN_ERROR = "Login error";
    public static final String AVATAR_SAVING_ERROR = "An error occurred while saving the avatar to the disk";
    public static final String YOU_CAN_T_ADD_IMAGES_TO_THAT_ALBUM_ERROR = "You can't add images to this album";
    public static final String SHELF_RECENTLY_DELETED_ERROR = "This shelf has been recently deleted. Please, refresh your browser to see actual data.";
    public static final String IMAGE_RECENTLY_DELETED_ERROR = "This image has been recently deleted. Please, refresh your browser to see actual data.";
    public static final String ALBUM_RECENTLY_DELETED_ERROR = "This album has been recently deleted. Please, refresh your browser to see actual data.";
    public static final String UPDATE_USER_ERROR = " An error occurred while saving preferences to the database.";
    public static final String SHELF_DELETING_ERROR = " An error occurred while deleting the shelf from the database.";
    public static final String SHELF_SAVING_ERROR = " An error occurred while saving  the shelf to the database.";
    public static final String DELETE_COMMENT_ERROR = " An error occurred while deleting the comment from the database.";
    public static final String SAVE_COMMENT_ERROR = " An error occurred while saving the comment to the database.";
    public static final String IMAGE_SAVING_ERROR = " An error occurred while saving the image to the database.";
    public static final String IMAGE_DELETING_ERROR = " An error occurred while deleting the image from the database.";
    public static final String ERROR_IN_DB = "Error in DB.";
    public static final String ALBUM_DELETING_ERROR = " An error occurred while deleting the album from the database.";
    public static final String ALBUM_SAVING_ERROR = " An error occurred while saving the album to the database";
    public static final String SHELF_MUST_BE_NOT_NULL_ERROR = "Shelf name must not be null";
    public static final String NO_SHELF_ERROR = "You must create at least one shelf before creating an album!";
    public static final String FILE_UPLOAD_SHOW_ERROR = "You must create at least one shelf before uploading images!";
    public static final String DND_ALBUM_ERROR = "You can't add the album to this shelf";
    public static final String DND_PHOTO_ERROR = "You can't add photos to this album";
    public static final String NO_ALBUM_TO_DOWNLOAD_ERROR = "Please, specify the album to be downloaded";
    public static final String NULL_COMMENT_ERROR = "Comment must not be null";
    public static final String ADDING_COMMENT_ERROR = "For adding the comment for this picture you have login";
    public static final String NO_IMAGES_FOR_SLIDESHOW_ERROR = "No images for slideshow!";
    public static final String FILE_IO_ERROR = "File IO Error";
    public static final String UPLOAD_ROOT_CREATION_ERROR = "Upload root was not created";
    public static final String UPLOAD_FOLDER_PATH_ERROR = "Cannot bound image folder path";
    public static final String INVALID_LOGIN_OR_PASSWORD = "Invalid login or password";
    public static final String USER_WITH_THIS_LOGIN_ALREADY_EXIST = "A user with this login already exists";
    public static final String CONFIRM_PASSWORD_NOT_EQUALS_PASSWORD = "Confirm Password not equals password";
    public static final String HAVENT_ACCESS = "You have no rights to view this album";
    public static final String FILE_PROCESSING_ERROR = "Error processing occurred during the upload";
    public static final String FILE_SAVE_ERROR = "Error occurred during saving the image to the disk";
    public static final String SEARCH_NO_WHERE_OPTIONS_ERROR = "You must specify where to execute the search ";
    public static final String EVENT_SAVING_ERROR = " An error occurred while saving the event to the database";
    public static final String EVENT_DELETING_ERROR = " An error occurred while deleting the event to the database";
    // Outcomes
    public static final String LOGOUT_OUTCOME = "logout";
    public static final String REGISTER_OUTCOME = "register";
    public static final String MAIN_OUTCOME = "main";
    public static final String INDEX_OUTCOME = "index";

    // Context-variables
    public static final String FILE_MANAGER_COMPONENT = "fileManager";
    public static final String AVATAR_DATA_COMPONENT = "avatarData";
    public static final String ALBUM_VARIABLE = "album";
    public static final String SHELF_VARIABLE = "shelf";
    public static final String USER_VARIABLE = "user";

    // ID's
    public static final String REGISTER_LOGIN_NAME_ID = "overForm:userLoginName";
    public static final String REGISTER_CONFIRM_PASSWORD_ID = "overForm:userConfirmPassword";
    public static final String REGISTER_EMAIL_ID = "overForm:userEmail";
    public static final String SHELF_ID = "shelf";

    // Roles
    public static final String ADMIN_ROLE = "admin";

    // misc
    public static final int NUMBER_OF_IMAGE_COPIES = 256;
    public static final String AVATAR_JPG = "avatar.jpg";
    public static final int AVATAR_SIZE = 80;
    public static final int DEFAULT_IMAGE_SIZEVALUE = 120;
    public static final String SLASH = "/";
    public static final String DOT = ".";
    public static final String UPLOAD_ROOT_COMPONENT_NAME = "uploadRoot";
    public static final String UPLOAD_ROOT_PATH_COMPONENT_NAME = "uploadRootPath";
    public static final int INITIAL_DELAY = 4000;
    public static final int DELAY = 3000;
    public static final String DEFAULT_PICTURE = "default/noimage_small200.jpg";
    public static final String DEFAULT_ORIGINAL_PICTURE = "default/noimage.jpg";
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final String UPLOAD = "upload";
    public static final String FEMALE = "Female";
    public static final String MALE = "Male";
    public static final String TEMP_DIR = "java.io.tmpdir";
    public static final String WEB_INF = "WEB-INF";
    public static final String IMAGE_FOLDER = "/Upload";
    public static final String PHOTOALBUM_FOLDER = "richfaces_photoalbum";

    // Service -constants
    public static final String USER_EXIST_QUERY = "user-exist";
    public static final String USER_LOGIN_QUERY = "user-login";
    public static final String USER_FB_LOGIN_QUERY = "user-fb-login";
    public static final String USER_GPLUS_LOGIN_QUERY = "user-gplus-login";
    public static final String LOGIN_PARAMETER = "login";
    public static final String PASSWORD_PARAMETER = "password";
    public static final String USERNAME_PARAMETER = "username";
    public static final String USER_PARAMETER = "user";
    public static final String DATE_PARAMETER = "date";
    public static final String ALBUM_PARAMETER = "album";
    public static final String COMMA = ",";
    public static final int MAX_RESULTS = 20;
    public static final String PERCENT = "%";
    public static final String TAG_SUGGEST_QUERY = "tag-suggest";
    public static final String TAG_POPULAR_QUERY = "tag-popular";
    public static final String TAG_PARAMETER = "tag";
    public static final String TAG_BY_NAME_QUERY = "tag-byName";
    public static final String SEARCH_SHELF_SHARED_ADDON = " and sh.shared=true";
    public static final String SEARCH_SHELF_MY_ADDON = " and sh.owner.login=:login";
    public static final String SEARCH_SHELF_BOTH_ADDON = " and (sh.shared=true or sh.owner.login=:login)";
    public static final String SEARCH_SHELVES_QUERY = "from Shelf sh where (lower(sh.name) like :queryString or lower(sh.description) like :queryString) ";
    public static final String SEARCH_METATAG_QUERY = "from MetaTag t where lower(t.tag) like :queryString";
    public static final String SEARCH_USERS_QUERY = "select  u from User u where (lower(u.login) like :queryString or lower(u.firstName) like :queryString or lower(u.secondName) like :queryString) ";
    public static final String SEARCH_IMAGE_SHARED_ADDON = " and i.album.shelf.shared=true";
    public static final String SEARCH_IMAGE_MY_ADDON = " and i.album.shelf.owner.login=:login";
    public static final String SEARCH_IMAGE_BOTH_ADDON = "  and (i.album.shelf.shared=true or i.album.shelf.owner.login=:login)";
    public static final String SEARCH_IMAGE_QUERY = "from Image i where (lower(i.name) like :queryString or lower(i.description) like :queryString or lower(i.cameraModel) like :queryString) ";
    public static final String SHARED_PARAMETER = "shared";
    public static final String QUERY_PARAMETER = "queryString";
    public static final String SEARCH_ALBUM_SHARED_ADDON = " and a.shelf.shared=true";
    public static final String SEARCH_ALBUM_MY_ADDON = " and a.shelf.owner.login=:login";
    public static final String SEARCH_ALBUM_BOTH_ADDON = " and (a.shelf.shared=true or a.shelf.owner.login=:login)";
    public static final String SEARCH_ALBUM_QUERY = "from Album a where (lower(a.name) like :queryString or lower(a.description) like :queryString)";
    public static final String USER_SHELVES_QUERY = "user-shelves";
    public static final String SHELF_PARAMETER = "shelf";
    public static final String PATH_PARAMETER = "path";
    public static final String IMAGE_PATH_EXIST_QUERY = "image-exist";
    public static final String IMAGE_IDENTICAL_QUERY = "image-countIdenticalImages";
    public static final String SEARCH_NO_OPTIONS_ERROR = "You must select at least one search option";
    public static final String TREE_ID = "treeform";
    public static final String MAINAREA_ID = "overForm:mainArea";
    public static final String USER_COMMENTS_QUERY = "user-comments";
    public static final String AUTHOR_PARAMETER = "author";
    public static final String EMAIL_EXIST_QUERY = "email-exist";
    public static final String EMAIL_PARAMETER = "email";

    private Constants() {
    }
}