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
package org.richfaces.photoalbum.util;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.richfaces.photoalbum.manager.LoggedUserTracker;
import org.richfaces.photoalbum.manager.UserBean;
import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.SimpleEvent;

/**
 * Utility class for check is the user session was expired or user were login in another browser. Observes
 * <code>Constants.CHECK_USER_EXPIRED_EVENT</code> event
 * 
 * @author Andrey Markhel
 */
@RequestScoped
public class SessionExpirationChecker {

    @Inject
    UserBean userBean;

    @Inject
    LoggedUserTracker userTracker;

    @Inject
    @Preferred
    HttpSession session;

    /**
     * Utility method for check is the user session was expired or user were login in another browser. Observes
     * <code>Constants.CHECK_USER_EXPIRED_EVENT</code> event. Redirects to error page if user were login in another browser.
     * 
     * @param session - user's session
     */
    public void checkUserExpiration(@Observes @EventType(Events.CHECK_USER_EXPIRED_EVENT) SimpleEvent se) {
        if (isShouldExpireUser(session)) {
            try {
                ApplicationUtils.getSession().invalidate();
                FacesContext.getCurrentInstance().getExternalContext().redirect("error.jsf");
            } catch (IOException e1) {
                FacesContext.getCurrentInstance().responseComplete();
            }
        }
    }

    private boolean isShouldExpireUser(HttpSession session) {
        User user = userBean.getUser();
        return userBean.isLoggedIn() && user != null && userTracker.containsUserId(user.getId())
            && !userTracker.containsUser(user.getId(), session.getId());
    }
}
