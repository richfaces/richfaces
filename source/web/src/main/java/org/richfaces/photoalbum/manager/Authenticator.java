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
 * Class encapsulated all functionality, related to working with authenticating/registering users.
 *
 * @author Andrey Markhel
 */
import java.io.File;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.security.auth.login.LoginException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IUserAction;
import org.richfaces.photoalbum.util.Environment;
import org.richfaces.photoalbum.util.HashUtils;

@Name("authenticator")
@Scope(ScopeType.CONVERSATION)
public class Authenticator implements Serializable {

	private static final long serialVersionUID = -4585673256547342140L;

	@In @Out
	User user;

	@In
	Identity identity;

	@In
	Credentials credentials;

	@In
	FacesMessages facesMessages;

	@In
	IUserAction userAction;

	private boolean loginFailed = false;

	private boolean conversationStarted = false;

	/**
	 * Method, that invoked when user try to login to the application.
	 * @return boolean indicator, that denotative is user succesfully loginned to the system.
	 *
	 */
	public boolean authenticate() {
		try {
			//If user with this login and password exist, the user object will be returned
			user = userAction.login(credentials.getUsername(), HashUtils.hash(credentials.getPassword()));
			if (user != null) {
				//This check is actual only on livedemo server to prevent hacks.
				//Check if pre-defined user login.
				if (Environment.isInProduction() && user.isPreDefined()) {
					//If true assume that login failed
					loginFailed();
					return false;
				}
				identity.addRole(Constants.ADMIN_ROLE);
				//Raise event to controller to update Model
				Events.instance().raiseEvent(Constants.AUTHENTICATED_EVENT, user);
				//Login was succesfull
				setLoginFailed(false);
				return true;
			}
		} catch (Exception nre) {
			loginFailed();
			return false;
		}
		return false;
	}

	/**
	 * Method, that invoked when user logout from application.
	 * @return outcome string to redirect.
	 *
	 */
	public String logout() {
		identity.logout();
		setConversationStarted(false);
		return Constants.LOGOUT_OUTCOME;
	}

	/**
	 * Method, that invoked when user try to register in the application.
	 * If registration was successfull, user immediately will be loginned to the system.
	 * @param user - user object, that will be passed to registration procedure.
	 *
	 */
	public void register(User user) {
		//Checks
		if (checkPassword(user) || checkUserExist(user)
				|| checkEmailExist(user.getEmail())) {
			return;
		}
		user.setPasswordHash(HashUtils.hash(user.getPassword()));
		//This check is actual only on livedemo server to prevent hacks.
		//Only admins can mark user as pre-defined
		user.setPreDefined(false);
		if(!handleAvatar(user)){
			return;
		}
		try {
			userAction.register(user);
		} catch (Exception e) {
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.REGISTRATION_ERROR);
			return;
		}
		//Registration was successfull, so we can login this user.
		credentials.setPassword(user.getPassword());
		credentials.setUsername(user.getLogin());
		try {
			identity.authenticate();
		} catch (LoginException e) {
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.LOGIN_ERROR);
		}

	}

	/**
	 * Method, that invoked when user want to go to the registration screen
	 * 
	 */
	public void goToRegister() {
		//create new User object
		user = new User();
		//Clear avatarData component in conversation scope
		Contexts.getConversationContext().set(Constants.AVATAR_DATA_COMPONENT, null);
		setLoginFailed(false);
		//raise event to controller to prepare Model.
		Events.instance().raiseEvent(Constants.START_REGISTER_EVENT);
	}

	/**
	 * Method, that invoked when new conversation is started.
	 * This method prevent instantiation of couples of conversations when user refresh the whole page. 
	 * 
	 * @return string outcome to properly redirect on the current page(to assign to page parameters conversationId.
	 */
	public String startConversation() {
		Events.instance().raiseEvent(Constants.UPDATE_MAIN_AREA_EVENT, NavigationEnum.ANONYM);
		setConversationStarted(true);
		return "";
	}
	
	private boolean handleAvatar(User user) {
		File avatarData = (File) Contexts.getConversationContext().get(Constants.AVATAR_DATA_COMPONENT);
		if (avatarData != null) {
			user.setHasAvatar(true);
			FileManager fileManager = (FileManager) Contexts.getApplicationContext().get(Constants.FILE_MANAGER_COMPONENT);
			if (fileManager == null || !fileManager.saveAvatar(avatarData, user)) {
				Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.AVATAR_SAVING_ERROR);
				return false;
			}
		}
		return true;
	}
	
	private boolean checkUserExist(User user) {
		if (userAction.isUserExist(user.getLogin())) {
			addFacesMessage(Constants.REGISTER_LOGIN_NAME_ID, Constants.USER_WITH_THIS_LOGIN_ALREADY_EXIST);
			return true;
		}
		return false;
	}

	private boolean checkEmailExist(String email) {
		if (userAction.isEmailExist(email)) {
			addFacesMessage(Constants.REGISTER_EMAIL_ID, Constants.USER_WITH_THIS_EMAIL_ALREADY_EXIST);
			return true;
		}
		return false;
	}

	private void addFacesMessage(String componentId, String message) {
		UIComponent root = FacesContext.getCurrentInstance().getViewRoot();
		UIComponent component = root.findComponent(componentId);
		FacesContext.getCurrentInstance().addMessage(component
			.getClientId(FacesContext.getCurrentInstance()),new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}

	private boolean checkPassword(User user) {
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			addFacesMessage(Constants.REGISTER_CONFIRM_PASSWORD_ID, Constants.CONFIRM_PASSWORD_NOT_EQUALS_PASSWORD);
			return true;
		}
		return false;
	}

	private void loginFailed() {
		setLoginFailed(true);
		facesMessages.clear();
		facesMessages.add(Constants.INVALID_LOGIN_OR_PASSWORD);
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