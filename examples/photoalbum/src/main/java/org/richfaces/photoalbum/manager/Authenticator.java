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
 * Class encapsulated all functionality, related to working with authenticating/registering users.
 *
 * @author Andrey Markhel
 */
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.json.JSONObject;
import org.richfaces.photoalbum.bean.UserBean;
import org.richfaces.photoalbum.domain.Sex;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.event.EventType;
import org.richfaces.photoalbum.event.EventTypeQualifier;
import org.richfaces.photoalbum.event.Events;
import org.richfaces.photoalbum.event.NavEvent;
import org.richfaces.photoalbum.event.SimpleEvent;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IUserAction;
import org.richfaces.photoalbum.social.FacebookBean;
import org.richfaces.photoalbum.util.Environment;
import org.richfaces.photoalbum.util.HashUtils;
import org.richfaces.photoalbum.util.Utils;

@Named
@ApplicationScoped
public class Authenticator implements Serializable {

    private static final long serialVersionUID = -4585673256547342140L;

    @Inject
    LoggedUserTracker userTracker;

    @Inject
    IUserAction userAction;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private boolean loginFailed = false;

    private boolean conversationStarted = false;

    @Inject
    @Any
    Event<SimpleEvent> event;
    @Inject
    @EventType(Events.UPDATE_MAIN_AREA_EVENT)
    Event<NavEvent> navEvent;

    @Inject
    FileManager fileManager;

    @Inject
    UserBean userBean;

    @Inject
    FacebookBean fBean;

    /**
     * Method, that invoked when user try to login to the application.
     * 
     * @return boolean indicator, that denotative is user succesfully loginned to the system.
     * 
     */
    public boolean authenticate() {
        try {
            // If user with this login and password exist, the user object will be returned
            user = userBean.logIn(userBean.getUsername(), HashUtils.hash(userBean.getPassword()));
            if (user != null) {
                // This check is actual only on livedemo server to prevent hacks.
                // Check if pre-defined user login.
                if (Environment.isInProduction() && user.isPreDefined()) {
                    // If true assume that login failed
                    loginFailed();
                    user = new User();
                    return false;
                }
                addToTracker(user.getId());
                // Raise event to controller to update Model
                event.select(new EventTypeQualifier(Events.AUTHENTICATED_EVENT)).fire(new SimpleEvent());
                // Login was successful
                setLoginFailed(false);
                return true;
            }
        } catch (Exception nre) {
            loginFailed();
            return false;
        }
        return false;
    }

    private void addToTracker(Long userId) {
        // Remove previous session id from users store
        userTracker.removeUserId(userId);
        // Mark current user as actual
        userTracker.addUserId(userId, Utils.getSession().getId());
    }

    public boolean authenticateWithFacebook() {
        JSONObject userInfo = fBean.getUserInfo();

        try {
            String pictureUrl = userInfo.getJSONObject("picture").getJSONObject("data").getString("url");
            userBean.setFbPhotoUrl(pictureUrl);

            String facebookId = userInfo.getString("id");
            user = userBean.logIn(facebookId);

            if (user == null) { // user does not exist
                User newUser = new User();

                newUser.setFbId(facebookId);
                newUser.setFirstName(userInfo.getString("first_name"));
                newUser.setSecondName(userInfo.getString("last_name"));
                newUser.setEmail(userInfo.getString("email"));

                String username = userInfo.has("username") ? userInfo.getString("username") : userInfo.getString("first_name");
                newUser.setLogin(username);

                String sex = userInfo.getString("gender");
                newUser.setSex(sex.equals("male") ? Sex.MALE : Sex.FEMALE);

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                newUser.setBirthDate(sdf.parse(userInfo.getString("birthday")));

                newUser.setPasswordHash(HashUtils.hash("facebook"));
                // temporary, find a workaround

                userAction.register(newUser);

                userBean.logIn(facebookId);
            } else {
                addToTracker(user.getId());
            }

            return true;
        } catch (Exception nre) {
            loginFailed();
            return false;
        }
    }

    /**
     * Method, that invoked when user logout from application.
     * 
     * @return outcome string to redirect.
     * 
     */
    public void logout() {
        long id = userBean.getUser().getId();
        userBean.logout();
        this.user = null;
        // Remove user from users store
        userTracker.removeUserId(id);
        setConversationStarted(false);
        // return Constants.LOGOUT_OUTCOME;
        // return "index.seam";
        startConversation();
    }

    /**
     * Method, that invoked when user try to register in the application. If registration was successfull, user immediately will
     * be loginned to the system.
     * 
     * @param user - user object, that will be passed to registration procedure.
     * 
     */
    public void register(User user) {
        // Checks
        if (checkPassword(user) || checkUserExist(user) || checkEmailExist(user.getEmail())) {
            return;
        }

        user.setPasswordHash(HashUtils.hash(user.getPassword()));
        // This check is actual only on livedemo server to prevent hacks.
        // Only admins can mark user as pre-defined
        user.setPreDefined(false);
        if (!handleAvatar(user)) {
            return;

        }
        try {
            userAction.register(user);
        } catch (Exception e) {
            event.select(new EventTypeQualifier(Events.ADD_ERROR_EVENT)).fire(new SimpleEvent(Constants.REGISTRATION_ERROR));
            return;
        }
        // Registration was successful, so we can login this user.
        try {
            this.user = userBean.logIn(user.getLogin(), HashUtils.hash(user.getPassword()));
        } catch (Exception e) {
            event.select(new EventTypeQualifier(Events.ADD_ERROR_EVENT)).fire(
                new SimpleEvent(Constants.LOGIN_ERROR + "\n" + e.getMessage()));
            return;
        }
        if (this.user == null) {
            event.select(new EventTypeQualifier(Events.ADD_ERROR_EVENT)).fire(new SimpleEvent(Constants.LOGIN_ERROR));
        }
    }

    /**
     * Method, that invoked when user want to go to the registration screen
     * 
     */
    public void goToRegister() {
        // create new User object
        user = new User();
        setLoginFailed(false);
        // raise event to controller to prepare Model.
        event.select(new EventTypeQualifier(Events.START_REGISTER_EVENT)).fire(new SimpleEvent());
    }

    /**
     * Method, that invoked when new conversation is started. This method prevent instantiation of couples of conversations when
     * user refresh the whole page.
     * 
     * @return string outcome to properly redirect on the current page(to assign to page parameters conversationId.
     */
    @PostConstruct
    public String startConversation() {
        navEvent.fire(new NavEvent(NavigationEnum.ANONYM));
        setConversationStarted(true);
        return "";
    }

    @SuppressWarnings("unused")
    private boolean handleAvatar(User user) {
        File avatarData = null; // temporary hack
        if (avatarData != null) {
            user.setHasAvatar(true);
            if (fileManager == null || !fileManager.saveAvatar(avatarData, user)) {
                event.select(new EventTypeQualifier(Events.ADD_ERROR_EVENT)).fire(
                    new SimpleEvent(Constants.AVATAR_SAVING_ERROR));
                return false;
            }
        }
        return true;
    }

    private boolean checkUserExist(User user) {
        if (userAction.isUserExist(user.getLogin())) {
            Utils.addFacesMessage(Constants.REGISTER_LOGIN_NAME_ID, "", Constants.USER_WITH_THIS_LOGIN_ALREADY_EXIST);
            return true;
        }
        return false;
    }

    private boolean checkEmailExist(String email) {
        if (userAction.isEmailExist(email)) {
            Utils.addFacesMessage(Constants.REGISTER_EMAIL_ID, "", Constants.USER_WITH_THIS_EMAIL_ALREADY_EXIST);
            return true;
        }
        return false;
    }

    private boolean checkPassword(User user) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            Utils.addFacesMessage(Constants.REGISTER_CONFIRM_PASSWORD_ID, "", Constants.CONFIRM_PASSWORD_NOT_EQUALS_PASSWORD);
            return true;
        }
        return false;
    }

    private void loginFailed() {
        setLoginFailed(true);
        Utils.addFacesMessage("overForm:loginPanel", "", Constants.INVALID_LOGIN_OR_PASSWORD);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public boolean isLoginFailed() {
        return loginFailed;
    }

    public void setLoginFailed(boolean loginFailed) {
        this.loginFailed = loginFailed;
    }

    public boolean isConversationStarted() {
        return conversationStarted;
    }

    public void setConversationStarted(boolean conversationStarted) {
        this.conversationStarted = conversationStarted;
    }
}