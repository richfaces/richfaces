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

package org.richfaces.photoalbum.bean;

import static org.richfaces.photoalbum.event.Events.ADD_ERROR_EVENT;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.event.ErrorEvent;
import org.richfaces.photoalbum.event.EventType;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.util.Preferred;

/**
 * This bean will work as a part of a simple security checking
 *
 * @author mpetrov
 */

@Named
@ApplicationScoped
public class UserBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    EntityManager em;

    private User user;

    private String username;

    private String fbPhotoUrl;

    @Inject
    @EventType(ADD_ERROR_EVENT)
    Event<ErrorEvent> event;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    private boolean logged = false;

    private boolean loggedInFB = false;
    private boolean loggedInGPlus = false;

    public User logIn(String username, String passwordHash) throws Exception {
        user = (User) em.createNamedQuery(Constants.USER_LOGIN_QUERY).setParameter(Constants.USERNAME_PARAMETER, username)
            .setParameter(Constants.PASSWORD_PARAMETER, passwordHash).getSingleResult();
        logged = user != null;

        return user;
    }

    public User logIn(String facebookId) {
        List<?> users = em.createNamedQuery(Constants.USER_FB_LOGIN_QUERY).setParameter("fbId", facebookId).getResultList();

        if (users.isEmpty()) {
            logged = false;
            loggedInFB = false;
            return null;
        }

        user = (User) users.get(0);

        logged = true;
        loggedInFB = true;

        return user;
    }
    
    public User gPlusLogIn(String gPlusId) {
        List<?> users = em.createNamedQuery(Constants.USER_FB_LOGIN_QUERY).setParameter("fbId", gPlusId).getResultList();

        if (users.isEmpty()) {
            logged = false;
            loggedInGPlus = false;
            return null;
        }

        user = (User) users.get(0);

        logged = true;
        loggedInGPlus = true;

        return user;
    }

    @Produces
    @Preferred
    public User getUser() {
        if (!logged) {
            return null;
        }
        return user;
    }

    public void refreshUser() {
        if (logged) {
            user = (User) em.createNamedQuery(Constants.USER_LOGIN_QUERY)
                .setParameter(Constants.USERNAME_PARAMETER, user.getLogin())
                .setParameter(Constants.PASSWORD_PARAMETER, user.getPasswordHash()).getSingleResult();
            logged = user != null;
        }
    }

    public boolean isLoggedIn() {
        return logged;
    }

    public boolean isLoggedInFB() {
        return loggedInFB;
    }

    public boolean isLoggedInGPlus() {
        return loggedInGPlus;
    }

    public void logout() {
        user = null;
        logged = false;
        loggedInFB = false;
        fbPhotoUrl = "";
        loggedInGPlus = false;
    }

    public void reset() {
        username = "";
        password = "";
    }

    public String getFbPhotoUrl() {
        return fbPhotoUrl;
    }

    public void setFbPhotoUrl(String fbPhotoUrl) {
        this.fbPhotoUrl = fbPhotoUrl;
    }
}
