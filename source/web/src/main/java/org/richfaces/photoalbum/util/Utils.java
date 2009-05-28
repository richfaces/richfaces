/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.ajax4jsf.context.AjaxContext;
/**
 * Utility class for actions, related to direct access or modification of current request
 * @author Andrey Markhel
 */
public class Utils {

	private Utils(){
		
	}
	
	/**
	 * Utility method for adding FacesMessages to specified component
	 * @param componentId - component identifier
	 * @param message - message to add
	 */
	public static void addFacesMessage(String componentId, String message) {
		UIComponent root = FacesContext.getCurrentInstance().getViewRoot();
		UIComponent component = root.findComponent(componentId);
		FacesContext.getCurrentInstance().addMessage(component
			.getClientId(FacesContext.getCurrentInstance()),new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}
	
	/**
	 * Utility method for get reference to current HTTPSession
	 * @return session object
	 */
	public static HttpSession getSession(){
		return (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}
	
	/**
	 * Utility method for programmatically adding specified component to rerender after AJAX request complete.
	 * @param componentId - id of component should be added to rerender
	 */
	public static void addToRerender(String componentId) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			AjaxContext ac = AjaxContext.getCurrentInstance();
			UIComponent destComponent = fc.getViewRoot().findComponent(componentId);
			ac.addComponentToAjaxRender(destComponent);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
