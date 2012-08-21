/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.photoalbum.manager;

/**
 * Class encapsulated all functionality, related to working with user.
 *
 * @author Andrey Markhel
 */
import java.io.File;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IUserAction;

@RequestScoped
public class UserManager implements Serializable {

    private static final long serialVersionUID = 6027103521084558931L;

    // @In(scope=ScopeType.SESSION) @Out(scope=ScopeType.SESSION)
    @Inject
    User user;

    @Inject
    FileManager fileManager;

    // @In(required=false, scope=ScopeType.CONVERSATION) @Out(required=false, scope=ScopeType.CONVERSATION)
    @Inject
    File avatarData;

    @Inject
    IUserAction userAction;

    /**
     * Method, that invoked when user want to edit her profile.
     *
     */
    @Observer(Constants.EDIT_USER_EVENT)
    public void editUser() {
        // If new avatar was uploaded
        if (avatarData != null) {
            if (!fileManager.saveAvatar(avatarData, user)) {
                Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.FILE_IO_ERROR);
                return;
            }
            avatarData.delete();
            avatarData = null;
            user.setHasAvatar(true);
        }
        try {
            // This check is actual only on livedemo server to prevent hacks.
            // Prevent hackers to mark user as pre-defined
            user.setPreDefined(false);
            // user.setPasswordHash(HashUtils.hash(user.getPassword()));
            user = userAction.updateUser();
        } catch (Exception e) {
            Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.UPDATE_USER_ERROR);
            return;
        }
    }

    /**
     * This method observes <code>Constants.ALBUM_ADDED_EVENT</code> and invoked after the user add new album
     *
     * @param album - added album
     */
    @Observer(Constants.ALBUM_ADDED_EVENT)
    public void onAlbumAdded(Album album) {
        user = userAction.refreshUser();
    }

    /**
     * Method, that invoked when user click 'Cancel' button during edit her profile.
     *
     */
    @Observer(Constants.CANCEL_EDIT_USER_EVENT)
    public void cancelEditUser() {
        avatarData = null;
    }
}