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

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.richfaces.photoalbum.service.Constants;
/**
 * Convenience UI class for rich:editor component
 *
 * @author Andrey Markhel
 */
@Name("editorBean")
public class EditorBean {

	private String currentConfiguration = "/org/richfaces/photoalbum/editor/advanced";
	
	private String message;
	
	public String getMessage() {
		return message;
	}

	@Observer(Constants.CLEAR_EDITOR_EVENT)
	public void setMessage(String message) {
		this.message = message;
	}

	public String getCurrentConfiguration() {
		return currentConfiguration;
	}
}