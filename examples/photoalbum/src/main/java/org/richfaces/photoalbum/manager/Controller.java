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

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.component.UITree;
import org.richfaces.photoalbum.model.Album;
import org.richfaces.photoalbum.model.Event;
import org.richfaces.photoalbum.model.Image;
import org.richfaces.photoalbum.model.MetaTag;
import org.richfaces.photoalbum.model.Shelf;
import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.model.event.AlbumEvent;
import org.richfaces.photoalbum.model.event.ErrorEvent;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.EventTypeQualifier;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.ImageEvent;
import org.richfaces.photoalbum.model.event.ShelfEvent;
import org.richfaces.photoalbum.model.event.SimpleEvent;
import org.richfaces.photoalbum.social.facebook.FacebookAlbumCache;
import org.richfaces.photoalbum.social.gplus.GooglePlusAlbumCache;
import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.Preferred;

/**
 * This class represent 'C' in MVC pattern. It is logic that determine what actions invoked and what next page need to be
 * showed. Typically on almost all user actions, this class populates the model and determine new view to show. Also contain
 * utility logic, such as checking is the given shelf belongs to the specified user etc..
 *
 * @author Andrey Markhel
 */

@Named
@SessionScoped
public class Controller implements Serializable {

    private static final long serialVersionUID = 5656562187249324512L;

    @Inject
    Model model;

    @Inject
    @Preferred
    private User loggedUser;
    @Inject
    UserBean loggedUserBean;

    @Inject
    FileManager fileManager;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    javax.enterprise.event.Event<ErrorEvent> error;

    @Inject
    @Any
    javax.enterprise.event.Event<SimpleEvent> event;

    @Inject
    FacebookAlbumCache fac;

    @Inject
    GooglePlusAlbumCache gpac;

    private int currentPage = 0;

    /**
     * Hack to prevent of returning 'null' user from @Produces method in UserBean.
     */
    private User getLoggedUser() {
        return (loggedUser == null ? loggedUserBean.getUser() : loggedUser);
    }

    /**
     * This method invoked after the user want to see all predefined shelves, existed in application
     */
    public void selectPublicShelves() {
        model.resetModel(NavigationEnum.ANONYM, getLoggedUser(), null, null, null, null);
    }

    /**
     * This method invoked after the user want to see all her shelves.
     */
    public void selectShelves() {
        model.resetModel(NavigationEnum.ALL_SHELFS, getLoggedUser(), null, null, null, null);
    }

    /**
     * This method invoked after the user want to see all her albums.
     */
    public void selectAlbums() {
        model.resetModel(NavigationEnum.ALL_ALBUMS, getLoggedUser(), null, null, null, null);
    }

    /**
     * This method invoked after the user want to see all her images.
     */
    public void selectImages() {
        model.resetModel(NavigationEnum.ALL_IMAGES, getLoggedUser(), null, null, null, getLoggedUser().getImages());
    }

    /**
     * This method invoked after the user want to edit specified shelf.
     *
     * @param shelf - shelf to edit
     */
    public void startEditShelf(Shelf shelf) {
        if (getLoggedUser() == null) {
            return;
        }
        if (!canViewShelf(shelf)) {
            showError("", Constants.HAVENT_ACCESS);
            return;
        }
        model.resetModel(NavigationEnum.SHELF_EDIT, shelf.getOwner(), shelf, null, null, null);
    }

    /**
     * This method invoked after the user want to interrupt edit shelf process
     *
     */
    public void cancelEditShelf() {
        model.resetModel(NavigationEnum.SHELF_PREVIEW, model.getSelectedShelf().getOwner(), model.getSelectedShelf(), null,
            null, null);
    }

    /**
     * This method invoked after the user want to see specified album independently is it her album or not.
     *
     * @param album - album to show
     */
    public void showAlbum(Album album) {
        if (!canViewAlbum(album)) {
            showError("", Constants.HAVENT_ACCESS);
            return;
        }
        // FileManager fileManager = (FileManager) Contexts.getApplicationContext().get(Constants.FILE_MANAGER_COMPONENT);
        // Check, that album was not deleted recently.
        if (!fileManager.isDirectoryPresent(album.getPath())) {
            showError("", Constants.ALBUM_RECENTLY_DELETED_ERROR);
            model.resetModel(NavigationEnum.SHELF_PREVIEW, album.getOwner(), album.getShelf(), null, null, null);
            return;
        }
        setPage(0); // reset page when album changes/resets
        model.resetModel(NavigationEnum.ALBUM_PREVIEW, album.getOwner(), album.getShelf(), album, null, album.getImages());
    }

    public void showFBAlbum(String albumId) {
        fac.setCurrentAlbumId(albumId);
        model.resetModel(NavigationEnum.FB_ALBUM_PREVIEW, getLoggedUser(), null, null, null, null);
    }

    public void showFbImage(String imageId) {
        setFPage(0);
        fac.setCurrentImageId(imageId);
        model.resetModel(NavigationEnum.FB_IMAGE_PREVIEW, getLoggedUser(), null, null, null, null);
    }

    public void showFbShelf() {
        model.resetModel(NavigationEnum.FB_SHELF, getLoggedUser(), null, null, null, null);
    }

    public void showGPlusShelf() {
        model.resetModel(NavigationEnum.GPLUS_SHELF, getLoggedUser(), null, null, null, null);
    }

    public void showGPlusAlbum() {
        model.resetModel(NavigationEnum.GPLUS_ALBUM_PREVIEW, getLoggedUser(), null, null, null, null);
    }

    public void showGPlusImage(String imageId) {
        setGplusPage(0);
        gpac.setCurrentImageId(imageId);
        model.resetModel(NavigationEnum.GPLUS_IMAGE_PREVIEW, getLoggedUser(), null, null, null, null);
    }

    /**
     * This method invoked in cases, when it is need to clear fileUpload component
     *
     */
    public void resetFileUpload() {
        pushEvent(Events.CLEAR_FILE_UPLOAD_EVENT);
    }

    /**
     * This method invoked after the user want to see specified image independently is it her image or not.
     *
     * @param album - album to show
     */
    public void showImage(Image image) {
        // Clear not-saved comment in editor
        pushEvent(Events.CLEAR_EDITOR_EVENT);
        if (!canViewImage(image)) {
            showError("", Constants.HAVENT_ACCESS);
            return;
        }
        // Check, that image was not deleted recently
        if (!fileManager.isFilePresent(image.getFullPath())) {
            showError("", Constants.IMAGE_RECENTLY_DELETED_ERROR);
            model.resetModel(NavigationEnum.ALBUM_PREVIEW, image.getAlbum().getOwner(), image.getAlbum().getShelf(),
                image.getAlbum(), null, image.getAlbum().getImages());
            return;
        }
        model.resetModel(NavigationEnum.ALBUM_IMAGE_PREVIEW, image.getAlbum().getOwner(), image.getAlbum().getShelf(),
            image.getAlbum(), image, image.getAlbum().getImages());
        image.setVisited(true);
    }

    public void showNextImage() {
        int id = model.getSelectedAlbum().getImages().indexOf(model.getSelectedImage());
        int max = model.getSelectedAlbum().getImages().size() - 1;

        if (id == max) {
            id = -1;
        }

        setPage((id + 1) / 5 + 1);
        showImage(model.getSelectedAlbum().getImages().get(id + 1));
    }

    public void showPrevImage() {
        int id = model.getSelectedAlbum().getImages().indexOf(model.getSelectedImage());
        int max = model.getSelectedAlbum().getImages().size() - 1;

        if (id == 0) {
            id = max + 1;
        }

        setPage((id - 1) / 5 + 1);
        showImage(model.getSelectedAlbum().getImages().get(id - 1));
    }

    public void showNextFbImage() {
        int id = fac.getCurrentImages().indexOf(fac.getCurrentImage());
        int max = fac.getCurrentImages().size() - 1;

        if (id == max) {
            id = -1;
        }

        setPage((id + 1) / 5 + 1);
        showFbImage(fac.getCurrentImages().get(id + 1).optString("id"));
    }

    public void showPrevFbImage() {
        int id = fac.getCurrentImages().indexOf(fac.getCurrentImage());
        int max = fac.getCurrentImages().size() - 1;

        if (id == 0) {
            id = max + 1;
        }

        setPage((id - 1) / 5 + 1);
        showFbImage(fac.getCurrentImages().get(id - 1).optString("id"));
    }

    public void showNextGPlusImage() {
        int id = gpac.getCurrentImages().indexOf(gpac.getCurrentImage());
        int max = gpac.getCurrentImages().size() - 1;

        if (id == max) {
            id = -1;
        }

        setPage((id + 1) / 5 + 1);
        showGPlusImage(gpac.getCurrentImages().get(id + 1).optString("id"));
    }

    public void showPrevGPlusImage() {
        int id = gpac.getCurrentImages().indexOf(gpac.getCurrentImage());
        int max = gpac.getCurrentImages().size() - 1;

        if (id == 0) {
            id = max + 1;
        }

        setPage((id - 1) / 5 + 1);
        showGPlusImage(gpac.getCurrentImages().get(id - 1).optString("id"));
    }

    /**
     * This method invoked after the user want to edit specified image.
     *
     * @param image - image to edit
     */
    public void startEditImage(Image image) {
        if (getLoggedUser() == null) {
            return;
        }
        if (!canViewImage(image)) {
            showError("", Constants.HAVENT_ACCESS);
            return;
        }
        model.resetModel(NavigationEnum.ALBUM_IMAGE_EDIT, image.getOwner(), image.getAlbum().getShelf(), image.getAlbum(),
            image, image.getAlbum().getImages());
    }

    /**
     * This method invoked after the user want to save just edited user to database.
     *
     */
    public void editUser() {
        if (getLoggedUser() == null) {
            return;
        }
        pushEvent(Events.EDIT_USER_EVENT);
    }

    /**
     * This method invoked after the user want to interrupt edit user process
     *
     */
    public void cancelEditUser() {
        pushEvent(Events.CANCEL_EDIT_USER_EVENT);
    }

    /**
     * This method invoked after the user want to interrupt edit image process
     *
     */
    public void cancelEditImage() {
        model.resetModel(NavigationEnum.ALBUM_IMAGE_PREVIEW, model.getSelectedImage().getAlbum().getShelf().getOwner(), model
            .getSelectedImage().getAlbum().getShelf(), model.getSelectedImage().getAlbum(), model.getSelectedImage(), model
            .getSelectedImage().getAlbum().getImages());
    }

    /**
     * This method invoked after the user want to see specified shelf independently is it her shelf or not.
     *
     * @param album - album to show
     */
    public void showShelf(Shelf shelf) {
        if (!fileManager.isDirectoryPresent(shelf.getPath())) {
            showError("", Constants.SHELF_RECENTLY_DELETED_ERROR);
            model.resetModel(NavigationEnum.ANONYM, shelf.getOwner(), null, null, null, null);
            return;
        }
        model.resetModel(NavigationEnum.SHELF_PREVIEW, shelf.getOwner(), shelf, null, null, null);
    }

    public void showEvent(Event event) {
        model.resetModel(NavigationEnum.EVENT_PREVIEW, getLoggedUser(), null, null, null, null, event);
        pushEvent(Events.EVENT_DISPLAYED_EVENT);
    }

    /**
     * This method invoked after the user want to edit specified album.
     *
     * @param album - album to edit
     */
    public void startEditAlbum(Album album) {
        if (getLoggedUser() == null) {
            return;
        }
        if (!album.isOwner(getLoggedUser())) {
            showError("", Constants.HAVENT_ACCESS);
            return;
        }
        model.resetModel(NavigationEnum.ALBUM_EDIT, album.getOwner(), album.getShelf(), album, null, album.getImages());
    }

    /**
     * This method invoked after the user want to interrupt edit album process
     *
     */
    public void cancelEditAlbum() {
        model.resetModel(NavigationEnum.ALBUM_PREVIEW, model.getSelectedAlbum().getOwner(),
            model.getSelectedAlbum().getShelf(), model.getSelectedAlbum(), null, model.getSelectedAlbum().getImages());
    }

    /**
     * This method observes <code>Constants.ALBUM_ADDED_EVENT</code> and invoked after the user add new album
     *
     * @param album - added album
     */
    public void onAlbumAdded(@Observes @EventType(Events.ALBUM_ADDED_EVENT) AlbumEvent ae) {
        Album album = ae.getAlbum();
        if (album.isShowAfterCreate()) {
            model.resetModel(NavigationEnum.ALBUM_PREVIEW, album.getOwner(), album.getShelf(), album, null, album.getImages());
        }
    }

    /**
     * This method observes <code>Constants.ALBUM_EDITED_EVENT</code> and invoked after the user edit her album
     *
     * @param album - edited album
     */
    public void onAlbumEdited(@Observes @EventType(Events.ALBUM_EDITED_EVENT) AlbumEvent ae) {
        Album album = ae.getAlbum();
        model.resetModel(NavigationEnum.ALBUM_PREVIEW, model.getSelectedUser(), model.getSelectedShelf(), album, null,
            album.getImages());
    }

    /**
     * This method observes <code>Constants.ALBUM_DELETED_EVENT</code> and invoked after the user delete her album
     *
     * @param album - deleted album
     * @param path - relative path of the album directory
     */
    public void onAlbumDeleted(@Observes @EventType(Events.ALBUM_DELETED_EVENT) AlbumEvent ae) {
        loggedUserBean.refreshUser();
        model.resetModel(NavigationEnum.ALL_ALBUMS, getLoggedUser(), ae.getAlbum().getShelf(), null, null, null);
    }

    /**
     * This method observes <code>Constants.SHELF_DELETED_EVENT</code> and invoked after the user delete her shelf
     *
     * @param shelf - deleted shelf
     * @param path - relative path of the shelf directory
     */
    public void onShelfDeleted(@Observes @EventType(Events.SHELF_DELETED_EVENT) ShelfEvent se) {
        loggedUserBean.refreshUser();
        model.resetModel(NavigationEnum.ALL_SHELFS, getLoggedUser(), null, null, null, null);
    }

    /**
     * This method observes <code>Constants.SHELF_ADDED_EVENT</code> and invoked after the user add new shelf
     *
     * @param shelf - added shelf
     */
    public void onShelfAdded(@Observes @EventType(Events.SHELF_ADDED_EVENT) ShelfEvent se) {
        Shelf shelf = se.getShelf();
        model.resetModel(NavigationEnum.SHELF_PREVIEW, shelf.getOwner(), shelf, null, null, null);
    }

    /**
     * This method observes <code>Constants.SHELF_EDITED_EVENT</code> and invoked after the user edit her shelf
     *
     * @param shelf - edited shelf
     */
    public void onShelfEdited(@Observes @EventType(Events.SHELF_EDITED_EVENT) ShelfEvent se) {
        Shelf shelf = se.getShelf();
        model.resetModel(NavigationEnum.SHELF_PREVIEW, shelf.getOwner(), shelf, null, null, null);
    }

    public void onEventDeleted(@Observes @EventType(Events.EVENT_DELETED_EVENT) ShelfEvent se) {
        model.resetModel(NavigationEnum.ANONYM, getLoggedUser(), null, null, null, null, null);
    }

    public void onEventAdded(@Observes @EventType(Events.EVENT_ADDED_EVENT) ShelfEvent se) {
        model.resetModel(NavigationEnum.EVENT_PREVIEW, getLoggedUser(), null, null, null, null, se.getEvent());
        pushEvent(Events.EVENT_DISPLAYED_EVENT);
    }

    public void onEventEdited(@Observes @EventType(Events.EVENT_EDITED_EVENT) ShelfEvent se) {
        model.resetModel(NavigationEnum.EVENT_PREVIEW, getLoggedUser(), null, null, null, null, se.getEvent());
        pushEvent(Events.EVENT_DISPLAYED_EVENT);
    }

    /**
     * This method observes <code>Constants.IMAGE_DELETED_EVENT</code> and invoked after the user delete her image
     *
     * @param image - deleted image
     * @param path - relative path of the image file
     */
    public void onImageDeleted(@Observes @EventType(Events.IMAGE_DELETED_EVENT) ImageEvent ie) {
        loggedUserBean.refreshUser();
        Album album = ie.getImage().getAlbum();
        model.resetModel(NavigationEnum.ALBUM_PREVIEW, getLoggedUser(), album.getShelf(), album, null, album.getImages());
    }

    /**
     * This method observes <code>Constants.AUTHENTICATED_EVENT</code> and invoked after the user successfully authenticate to
     * the system
     *
     * @param u - authenticated user
     */
    public void onAuthenticate(@Observes @EventType(Events.AUTHENTICATED_EVENT) SimpleEvent se) {
        model.resetModel(NavigationEnum.ALL_SHELFS, getLoggedUser(), null, null, null, null);
    }

    /**
     * This method invoked after the user want to go to the file-upload page
     *
     */
    public void showFileUpload() {
        if (!(getLoggedUser().getShelves().size() > 0)) {
            // If user have no shelves, that can start fileupload process
            showError("", Constants.FILE_UPLOAD_SHOW_ERROR);
            return;
        }
        Album alb = null;
        // If selected album belongs to user
        alb = setDefaultAlbumToUpload(alb);
        model.resetModel(NavigationEnum.FILE_UPLOAD, getLoggedUser(), alb != null ? alb.getShelf() : null, alb, null,
            alb != null ? alb.getImages() : null);
    }

    /**
     * This method invoked after the user want to go to the file-upload page and download images to the specified album
     *
     * @param album - selected album
     *
     */
    public void showFileUpload(Album album) {
        if (!isUserAlbum(album)) {
            showError("", Constants.YOU_CAN_T_ADD_IMAGES_TO_THAT_ALBUM_ERROR);
            return;
        }
        model.resetModel(NavigationEnum.FILE_UPLOAD, album.getShelf().getOwner(), album.getShelf(), album, null,
            album.getImages());
    }

    /**
     * This method invoked after the user want to see all shared albums of the specified user
     *
     * @param user - user to see
     *
     */
    public void showSharedAlbums(User user) {
        model.resetModel(NavigationEnum.USER_SHARED_ALBUMS, user, null, null, null, user.getSharedImages());
    }

    /**
     * This method invoked after the user want to see all shared images of the specified user
     *
     * @param user - user to see
     *
     */
    public void showSharedImages(User user) {
        model.resetModel(NavigationEnum.USER_SHARED_IMAGES, user, null, null, null, user.getSharedImages());
    }

    /**
     * This method invoked after the user want to see profile of the specified user
     *
     * @param user - user to see
     *
     */
    public void showUser(User user) {
        model.resetModel(NavigationEnum.USER_PREFS, user, null, null, null, null);
        // Contexts.getConversationContext().set(Constants.AVATAR_DATA_COMPONENT, null);
    }
    
    public void showCurrentUser() {
        showUser(getLoggedUser());
    }

    /**
     * This method invoked after the user want to see all unvisited images, belongs to the of specified shelf
     *
     * @param shelf - shelf to see
     *
     */
    public void showUnvisitedImages(Shelf shelf) {
        model.resetModel(NavigationEnum.SHELF_UNVISITED, shelf.getOwner(), shelf, null, null, shelf.getUnvisitedImages());
    }

    /**
     * This method invoked after the user want to see all unvisited images, belongs to the of specified album
     *
     * @param album - album to see
     *
     */
    public void showUnvisitedImages(Album album) {
        model.resetModel(NavigationEnum.ALBUM_UNVISITED, album.getOwner(), album.getShelf(), album, null,
            album.getUnvisitedImages());
    }

    /**
     * This method invoked after the user want to see all images, related to the of specified album
     *
     * @param metatag - tag to see
     *
     */
    public void showTag(MetaTag metatag) {
        model.resetModel(NavigationEnum.TAGS, model.getSelectedUser(), model.getSelectedShelf(), model.getSelectedAlbum(),
            model.getSelectedImage(), metatag.getImages());
        model.setSelectedTag(metatag);
    }

    /**
     * This method observes <code>Constants.START_REGISTER_EVENT</code> and invoked after the user want to start registration
     * process.
     *
     */
    public void startRegistration(@Observes @EventType(Events.START_REGISTER_EVENT) SimpleEvent se) {
        model.resetModel(NavigationEnum.REGISTER, getLoggedUser(), null, null, null, null);
    }

    /**
     * This method invoked after the user want to interrupt registration process
     *
     */
    public void cancelRegistration() {
        model.resetModel(NavigationEnum.ANONYM, getLoggedUser(), null, null, null, null);
    }

    /**
     * This utility method determine if the specified node should be marked as selected. Used in internal rich:tree mechanism
     */
    public Boolean adviseNodeSelected(UITree tree) {
        Object currentNode = tree.getRowData();
        if (currentNode.equals(model.getSelectedAlbum()) || currentNode.equals(model.getSelectedShelf())) {
            return true;
        }
        return false;
    }

    /**
     * This utility method used by custom datascroller to determine images to show. Used in internal rich:tree mechanism
     */
    public Integer getPage() {
        if (currentPage == 0) {
            Integer index = model.getSelectedAlbum().getIndex(model.getSelectedImage());
            currentPage = index / 5 + 1;
        }
        return currentPage;
    }

    public void setPage(Integer page) {
        currentPage = page;
    }
    
    public Integer getFPage() {
        if (currentPage == 0) {
            Integer index = fac.getCurrentImages().indexOf(fac.getCurrentImage());
            currentPage = index / 5 + 1;
        }
        return currentPage;
    }

    public void setFPage(Integer page) {
        currentPage = page;
    }
    
    public Integer getGplusPage() {
        if (currentPage == 0) {
            Integer index = gpac.getCurrentImages().indexOf(gpac.getCurrentImage());
            currentPage = index / 5 + 1;
        }
        return currentPage;
    }

    public void setGplusPage(Integer page) {
        currentPage = page;
    }

    /**
     * This utility method used to determine if the specified image belongs to the logged user
     *
     * @param image - image to check
     */
    public boolean isUserImage(Image image) {
        if (image == null || image.getOwner() == null || getLoggedUser() == null) {
            return false;
        }
        return getLoggedUser().equals(image.getOwner());
    }

    /**
     * This utility method used to determine if the logged user have any shelves.
     *
     */
    public boolean isUserHaveShelves() {
        return getLoggedUser().getShelves().size() > 0; // loggedUser might be null right after successful login
    }

    /**
     * This utility method used to determine if the logged user have any albums.
     *
     */
    public boolean isUserHaveAlbums() {
        return getLoggedUser().getAlbums().size() > 0;
    }

    /**
     * This utility method used to determine if the specified shelf belongs to the logged user
     *
     * @param shelf - shelf to check
     */
    public boolean isUserShelf(Shelf shelf) {
        return shelf != null && getLoggedUser() != null && getLoggedUser().equals(shelf.getOwner());
    }

    /**
     * This utility method used to determine if the specified album belongs to the logged user
     *
     * @param album - album to check
     */
    public boolean isUserAlbum(Album album) {
        return album != null && getLoggedUser() != null && getLoggedUser().equals(album.getOwner());
    }

    /**
     * This utility method used to determine if the specified user can be edited
     *
     * @param user - user to check
     */
    public boolean isProfileEditable(User selectedUser) {
        return selectedUser != null && selectedUser.equals(getLoggedUser());
    }

    private boolean canViewShelf(Shelf shelf) {
        return shelf != null && shelf.isOwner(getLoggedUser()) || shelf.isShared();
    }

    private boolean canViewAlbum(Album album) {
        return album != null && album.getShelf() != null && (album.getShelf().isShared() || album.isOwner(getLoggedUser()));
    }

    private boolean canViewImage(Image image) {
        return image != null
            && image.getAlbum() != null
            && (image.getAlbum().getShelf() != null && (image.getAlbum().getShelf().isShared() || image
                .isOwner(getLoggedUser())));
    }

    /**
     * This utility method invoked in case if you want to show to the user specified error in popup
     *
     * @param error - error to show
     *
     */
    public void showError(String summary, String errorMessage) {
        error.fire(new ErrorEvent(summary, errorMessage));
    }

    private void pushEvent(Events eventType) {
        event.select(new EventTypeQualifier(eventType)).fire(new SimpleEvent());
    }

    private Album setDefaultAlbumToUpload(Album alb) {
        if (isUserAlbum(model.getSelectedAlbum())) {
            alb = model.getSelectedAlbum();
        }
        if (alb == null) {
            if (getLoggedUser() != null && getLoggedUser().getShelves().size() > 0
                && getLoggedUser().getShelves().get(0).getAlbums().size() > 0) {
                for (Shelf s : getLoggedUser().getShelves()) {
                    if (s.getAlbums().size() > 0) {
                        alb = s.getAlbums().get(0);
                        break;
                    }
                }
            }
        }
        return alb;
    }

    /*
     * Checks if selected album belong to selected event
     */
    public boolean isEventFacebookAlbum(String id) {
        return model.getSelectedEvent() != null && model.getSelectedEvent().getFacebookAlbumIds().contains(id);
    }

    public boolean isEventGoogleAlbum(String id) {
        return model.getSelectedEvent() != null && model.getSelectedEvent().getGooglePlusAlbumIds().contains(id);
    }
}