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
package org.richfaces.photoalbum.ui;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.richfaces.photoalbum.service.Constants;
/**
 * Convenience UI class for global eeror-checking mechanism
 *
 * @author Andrey Markhel
 */
@Name("errorHandlerBean")
@Scope(ScopeType.EVENT)
@AutoCreate
public class ErrorHandlerBean {
	private List<String> errors = new ArrayList<String>();

	public List<String> getErrors() {
		return errors;
	}
	
	public boolean isErrorExist(){
		return errors.size() > 0 ;
	}
	
	/**
	 * Convenience method that observes <code>Constants.ADD_ERROR_EVENT</code>. After error occured add error to the list of erors andon rerendering modal panel with all errors will be showed.
	 *
	 * @param e - string representation of error.
	 */
	@Observer(Constants.ADD_ERROR_EVENT)
	public void addToErrors(String e){
		errors.add(e);
	}
}
