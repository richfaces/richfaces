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
 * Class encapsulated all functionality, related to working with user.
 *
 * @author Andrey Markhel
 */
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.model.actions.IUserAction;
import org.richfaces.photoalbum.model.event.AlbumEvent;
import org.richfaces.photoalbum.model.event.ErrorEvent;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.SimpleEvent;
import org.richfaces.photoalbum.ui.UserPrefsHelper;
import org.richfaces.photoalbum.util.Preferred;

@SessionScoped
public class UserManager implements Serializable {

    private static final long serialVersionUID = 6027103521084558931L;

    @Inject
    @Preferred
    User user;

    @Inject
    FileManager fileManager;

    @Inject
    UserPrefsHelper uph;

    @Inject
    IUserAction userAction;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    Event<ErrorEvent> error;

    /**
     * This method observes <code>Constants.ALBUM_ADDED_EVENT</code> and invoked after the user add new album
     *
     * @param album - added album
     */
    public void onAlbumAdded(@Observes @EventType(Events.ALBUM_ADDED_EVENT) AlbumEvent ae) {
        user = userAction.refreshUser();
    }

    /**
     * Method, that invoked when user click 'Cancel' button during edit her profile.
     *
     */
    public void cancelEditUser(@Observes @EventType(Events.CANCEL_EDIT_USER_EVENT) SimpleEvent se) {
        uph.setAvatarData(null);
    }
}