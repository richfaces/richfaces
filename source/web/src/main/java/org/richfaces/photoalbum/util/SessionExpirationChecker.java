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
package org.richfaces.photoalbum.util;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;
import org.jboss.seam.web.Session;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.manager.LoggedUserTracker;
import org.richfaces.photoalbum.service.Constants;
/**
 * Utility class for check is the user session was expired or user were login in another browser.
 * Observes <code>Constants.CHECK_USER_EXPIRED_EVENT</code> event
 * @author Andrey Markhel
 */
@Scope(ScopeType.EVENT)
@Name("sessionExpirationChecker")
@AutoCreate
public class SessionExpirationChecker {

	@In User user;
	@In Identity identity;
	@In LoggedUserTracker userTracker;
	/**
	 * Utility method for check is the user session was expired or user were login in another browser.
	 * Observes <code>Constants.CHECK_USER_EXPIRED_EVENT</code> event.
	 * Redirects to error page if user were login in another browser.
	 * @param session - user's session
	 */
	@Observer(Constants.CHECK_USER_EXPIRED_EVENT)
	public void checkUserExpiration(HttpSession session){
		if(isShouldExpireUser(session)){
	    	 try {
	    		Session.instance().invalidate();
				FacesContext.getCurrentInstance().getExternalContext().redirect("error.seam");
			} catch (IOException e1) {
				FacesContext.getCurrentInstance().responseComplete();
			}
	     }
	}
	
	private boolean isShouldExpireUser(HttpSession session) {
		return identity.isLoggedIn() && user!= null && userTracker.containsUserId(user.getId()) && !userTracker.containsUser(user.getId(), session.getId());
	}
}
